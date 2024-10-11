use crate::domain::events::pix_payment_event::PixPaymentEvent;
use crate::application::services::payment_service::PaymentService;
use crate::domain::entities::pix_payment::PixPayment;
use lapin::{options::*, types::FieldTable, Channel};
use futures_util::stream::StreamExt;
use serde_json;
use log::{info, error};
use std::sync::Arc;

pub struct PixEventHandler {
    pub channel: Arc<Channel>,
    pub payment_service: Arc<PaymentService>,
}

impl PixEventHandler {
    pub fn new(channel: Arc<Channel>, payment_service: Arc<PaymentService>) -> Self {
        Self {
            channel,
            payment_service,
        }
    }

    pub async fn start_consuming(&self, queue_name: &str) -> Result<(), Box<dyn std::error::Error>> {
        self.channel
            .queue_declare(
                queue_name,
                QueueDeclareOptions::default(),
                FieldTable::default(),
            )
            .await?;

        // Inicia o consumo de mensagens da fila
        let mut consumer = self
            .channel
            .basic_consume(
                queue_name,
                "pix_event_consumer",
                BasicConsumeOptions::default(),
                FieldTable::default(),
            )
            .await?;

        info!("Iniciando consumo da fila: {}", queue_name);

        while let Some(delivery_result) = consumer.next().await {
            match delivery_result {
                Ok(delivery) => {
                    let data = std::str::from_utf8(&delivery.data)?;
                    match serde_json::from_str::<PixPaymentEvent>(data) {
                        Ok(event) => {
                            
                            let payment = PixPaymentEvent::new(
                                event.payment_id.clone(),
                                event.amount,
                                event.receiver_key.clone(),
                                event.payer_key.clone(),
                                event.status.clone(),
                                event.timestamp.clone(),
                            );

                            // Processa o pagamento usando `payment_service`
                            if let Err(e) = self.payment_service.process_payment(PixPayment {
                                id: payment.payment_id,
                                amount: payment.amount,
                                receiver_key: payment.receiver_key,
                                payer_key: payment.payer_key,
                                timestamp: payment.timestamp,
                            }).await {
                                error!("Erro ao processar pagamento: {}", e);
                            }

                            delivery
                                .ack(BasicAckOptions::default())
                                .await
                                .map_err(|e| e.to_string())?;
                        }
                        Err(e) => {
                            error!("Falha ao deserializar o evento: {}", e);
                            delivery
                                .nack(BasicNackOptions {
                                    requeue: true,
                                    ..Default::default()
                                })
                                .await
                                .map_err(|e| e.to_string())?;
                        }
                    }
                }
                Err(e) => {
                    error!("Erro ao receber mensagem: {}", e);
                }
            }
        }

        Ok(())
    }
}
