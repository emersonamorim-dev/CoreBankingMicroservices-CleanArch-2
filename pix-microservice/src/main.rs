use actix_web::{App, HttpServer};
use dotenv::dotenv;
use std::sync::Arc;

mod application;
mod domain;
mod infrastructure;
mod interfaces;

use crate::domain::repositories::pix_repository::PixRepository;
use crate::interfaces::http::routes::init_routes;
use crate::interfaces::event_handlers::pix_event_handler::PixEventHandler;
use crate::infrastructure::messaging::rabbitmq_client::RabbitMQClient;
use crate::infrastructure::db::dynamodb_connection::DynamoDbConnection;
use crate::infrastructure::db::dynamodb_repository::DynamoDBRepository;
use crate::infrastructure::cache::redis_client::RedisClient;
use crate::application::services::payment_service::PaymentService;
use crate::application::services::qr_code_service::QrCodeService;
use crate::infrastructure::monitoring::prometheus_metrics::PrometheusMetrics;
use crate::infrastructure::config::settings::Settings;
use prometheus::Registry;
use tokio::spawn;
use log::error;

#[actix_web::main]
async fn main() -> std::io::Result<()> {
    dotenv().ok();
    env_logger::init();

    let settings = Settings::new();

    // Inicializa a conexão com o DynamoDB usando o endpoint da configuração
    let db_connection = DynamoDbConnection::new(&settings.dynamodb_endpoint).await;
    let repository = Arc::new(DynamoDBRepository::new(db_connection));

    let rabbitmq_client = Arc::new(RabbitMQClient::new(&settings.rabbitmq_uri).await.unwrap());
    let redis_client = Arc::new(RedisClient::new(&settings.redis_uri).await.unwrap());

    let registry = Registry::new();
    let metrics = PrometheusMetrics::new(&registry);

    let qr_code_service = QrCodeService;

    let payment_service = Arc::new(PaymentService::new(
        Arc::clone(&repository) as Arc<dyn PixRepository>,
        Arc::clone(&rabbitmq_client),
        Arc::clone(&redis_client),
        qr_code_service,
        metrics,
    ));

    // passa o Arc<Channel> corretamente para o PixEventHandler
    let pix_event_handler = PixEventHandler::new(Arc::clone(&rabbitmq_client.channel), Arc::clone(&payment_service));

    // Inicia o consumo de eventos em uma tarefa assíncrona
    spawn(async move {
        if let Err(e) = pix_event_handler.start_consuming("pix_payments").await {
            error!("Erro ao iniciar o manipulador de eventos: {}", e);
        }
    });

    // Inicia o servidor HTTP
    let server_address = settings.server_address.clone();

    HttpServer::new(move || App::new().configure(init_routes))
        .bind(server_address)?
        .run()
        .await
}
