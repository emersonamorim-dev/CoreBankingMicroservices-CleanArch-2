use std::sync::Arc;
use crate::domain::repositories::pix_repository::PixRepository;
use crate::infrastructure::messaging::rabbitmq_client::RabbitMQClient;
use crate::infrastructure::cache::redis_client::RedisClient;
use crate::application::services::qr_code_service::QrCodeService;
use crate::infrastructure::monitoring::prometheus_metrics::PrometheusMetrics;
use crate::domain::entities::pix_payment::PixPayment;

pub struct PaymentService {
    pub repository: Arc<dyn PixRepository>, 
    pub rabbitmq_client: Arc<RabbitMQClient>, 
    pub redis_client: Arc<RedisClient>, 
    pub qr_code_service: QrCodeService,
    pub metrics: PrometheusMetrics,
}

impl PaymentService {
    pub fn new(
        repository: Arc<dyn PixRepository>,
        rabbitmq_client: Arc<RabbitMQClient>,
        redis_client: Arc<RedisClient>,
        qr_code_service: QrCodeService,
        metrics: PrometheusMetrics,
    ) -> Self {
        Self {
            repository,
            rabbitmq_client,
            redis_client,
            qr_code_service,
            metrics,
        }
    }

    pub async fn process_payment(&self, payment: PixPayment) -> Result<String, String> {

        if let Some(_) = self.redis_client.get(&payment.id).await.unwrap_or(None) {
            return Err("Pagamento já processado".to_string());
        }

        // Gera o QR Code com integração ao BACEN (simulação)
        let qr_code_data = format!("https://bacen.gov.br/pix/qrcode/{}", payment.id);

        let qr_code = self
            .qr_code_service
            .generate_qr_code(&qr_code_data)
            .await
            .map_err(|e| e.to_string())?;

        // Salva o pagamento no DynamoDB
        self.repository
            .save_payment(payment.clone())
            .await
            .map_err(|e| e.to_string())?;

        // Publica o evento no RabbitMQ
        let event_message = serde_json::to_string(&payment).map_err(|e| e.to_string())?;
        self.rabbitmq_client
            .send("pix_payments", &event_message)
            .await
            .map_err(|e| e.to_string())?;

        // Armazena no Redis
        self.redis_client
            .set(&payment.id, &event_message)
            .await
            .map_err(|e| e.to_string())?;

        self.metrics.increment_payment_counter();

        // Retorna o QR Code gerado
        Ok(qr_code)
    }
}
