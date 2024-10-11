use dotenv::dotenv;
use std::env;

pub struct Settings {
    pub server_address: String,
    pub rabbitmq_uri: String,
    pub redis_uri: String,
    pub dynamodb_endpoint: String, // Endpoint local do DynamoDB
}

impl Settings {
    pub fn new() -> Self {
        dotenv().ok();

        Self {
            server_address: env::var("SERVER_ADDRESS").unwrap_or_else(|_| "0.0.0.0:8096".to_string()),
            rabbitmq_uri: env::var("RABBITMQ_URI").unwrap_or_else(|_| "amqp://guest:guest@rabbitmq:5672/%2f".to_string()),
            redis_uri: env::var("REDIS_URI").unwrap_or_else(|_| "redis://redis-cache:6379/".to_string()),
            dynamodb_endpoint: env::var("DYNAMODB_ENDPOINT").unwrap_or_else(|_| "http://localhost:8000".to_string()),  // Deve ser o endpoint correto do seu DynamoDB local
        }
    }
}
