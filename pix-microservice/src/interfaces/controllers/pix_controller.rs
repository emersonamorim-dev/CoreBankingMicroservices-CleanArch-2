use actix_web::{web, HttpResponse, Responder};
use crate::application::usecase::process_payment_usecase::ProcessPaymentUsecase;
use crate::domain::entities::pix_payment::PixPayment;
use crate::domain::repositories::pix_repository::PixRepository; 
use crate::infrastructure::cache::redis_client::RedisClient;
use crate::infrastructure::config::settings::Settings;
use crate::infrastructure::db::dynamodb_connection::DynamoDbConnection;
use crate::infrastructure::db::dynamodb_repository::DynamoDBRepository;
use crate::infrastructure::messaging::rabbitmq_client::RabbitMQClient;
use crate::infrastructure::monitoring::prometheus_metrics::PrometheusMetrics;
use crate::application::services::qr_code_service::QrCodeService;
use prometheus::Registry;
use std::sync::Arc;

pub async fn process_pix_payment(payment: web::Json<PixPayment>) -> impl Responder {
    let settings = Settings::new();

    let db_connection = DynamoDbConnection::new(&settings.dynamodb_endpoint).await;
    let repository = Arc::new(DynamoDBRepository::new(db_connection));

    let rabbitmq_client = Arc::new(RabbitMQClient::new(&settings.rabbitmq_uri).await.unwrap());
    let redis_client = Arc::new(RedisClient::new(&settings.redis_uri).await.unwrap());

    let registry = Registry::new();
    let metrics = PrometheusMetrics::new(&registry);

    let qr_code_service = QrCodeService;

    let new_payment = PixPayment::new(
        payment.amount,
        payment.receiver_key.clone(),
        payment.payer_key.clone(),
        payment.timestamp.clone(),
    );

    // Inicializa `ProcessPaymentUsecase`
    let process_payment_usecase = ProcessPaymentUsecase::new(
        Arc::clone(&repository) as Arc<dyn PixRepository>, 
        &qr_code_service,
        &rabbitmq_client,
        &redis_client,
        &metrics,
    );

    // Executa caso de uso e retornar resposta
    match process_payment_usecase.execute(new_payment).await {
        Ok(qr_code) => HttpResponse::Ok()
            .json(serde_json::json!({
                "status": "sucesso",
                "message": "Pagamento processado com sucesso",
                "qr_code": qr_code
            })),
        Err(err) => HttpResponse::BadRequest().json(serde_json::json!({
            "status": "error",
            "message": err,
        })),
    }
}
