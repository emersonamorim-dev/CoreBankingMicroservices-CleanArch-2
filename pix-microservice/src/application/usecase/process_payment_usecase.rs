use crate::application::services::qr_code_service::QrCodeService;
use crate::domain::entities::pix_payment::PixPayment;
use crate::domain::repositories::pix_repository::PixRepository;
use crate::infrastructure::cache::redis_client::RedisClient;
use crate::infrastructure::messaging::rabbitmq_client::RabbitMQClient;
use crate::infrastructure::monitoring::prometheus_metrics::PrometheusMetrics;
use std::sync::Arc; 

pub struct ProcessPaymentUsecase<'a> {
    pub pix_repository: Arc<dyn PixRepository>, 
    pub qr_code_service: &'a QrCodeService,
    pub rabbitmq_client: &'a RabbitMQClient,
    pub redis_client: &'a RedisClient,
    pub metrics: &'a PrometheusMetrics,
}

impl<'a> ProcessPaymentUsecase<'a> {
    pub fn new(
        pix_repository: Arc<dyn PixRepository>, 
        qr_code_service: &'a QrCodeService,
        rabbitmq_client: &'a RabbitMQClient,
        redis_client: &'a RedisClient,
        metrics: &'a PrometheusMetrics,
    ) -> Self {
        Self {
            pix_repository,
            qr_code_service,
            rabbitmq_client,
            redis_client,
            metrics,
        }
    }

    pub async fn execute(&self, payment: PixPayment) -> Result<String, String> {

        if payment.amount <= 0.0 {
            return Err("Valor do pagamento inválido.".to_string());
        }

        if let Some(_) = self.redis_client.get(&payment.id).await.unwrap_or(None) {
            return Err("Pagamento já processado.".to_string());
        }

        // Gera os dados para o QR Code simulando integração com o BACEN
        let qr_code_data = format!("https://bacen.gov.br/pix/qrcode/{}", payment.id);

        // Gera a imagem do QR Code
        let qr_code_image = self
            .qr_code_service
            .generate_qr_code(&qr_code_data)
            .await
            .map_err(|e| e.to_string())?;

        self.pix_repository
            .save_payment(payment.clone())
            .await
            .map_err(|e| e.to_string())?;

        // Publica o evento de pagamento no RabbitMQ
        let event_message = serde_json::to_string(&payment).map_err(|e| e.to_string())?;
        self.rabbitmq_client
            .send("pix_payments", &event_message)
            .await
            .map_err(|e| e.to_string())?;

        self.redis_client
            .set(&payment.id, &event_message)
            .await
            .map_err(|e| e.to_string())?;

        self.metrics.increment_payment_counter();

        // Retorna a imagem do QR Code
        Ok(qr_code_image)
    }
}
