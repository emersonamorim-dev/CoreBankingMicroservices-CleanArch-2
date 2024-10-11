use lapin::{
    options::*, types::FieldTable, BasicProperties, Channel, Connection, ConnectionProperties,
};
use std::sync::Arc;

pub struct RabbitMQClient {
    pub channel: Arc<Channel>, 
}

impl RabbitMQClient {
    pub async fn new(uri: &str) -> Result<Self, Box<dyn std::error::Error>> {

        let conn = Connection::connect(
            uri,
            ConnectionProperties::default(),
        )
        .await?;

        // Cria o canal e encapsula em um Arc para permitir compartilhamento seguro
        let channel = Arc::new(conn.create_channel().await?);
        Ok(Self { channel })
    }

    pub async fn declare_queue(&self, queue: &str, durable: bool) -> Result<(), Box<dyn std::error::Error>> {
        // Declara a fila
        self.channel
            .queue_declare(
                queue,
                QueueDeclareOptions {
                    durable,  
                    exclusive: false,  
                    auto_delete: true, 
                    ..Default::default()
                },
                FieldTable::default(),
            )
            .await?;
        Ok(())
    }

    pub async fn send(
        &self,
        queue: &str,
        message: &str,
    ) -> Result<(), Box<dyn std::error::Error>> {

        self.declare_queue(queue, true).await?; 

        self.channel
            .basic_publish(
                "",
                queue,
                BasicPublishOptions::default(),
                message.as_bytes(),
                BasicProperties::default(),
            )
            .await?;
    
        Ok(())
    }
}
