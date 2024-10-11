use serde::{Serialize, Deserialize};

#[derive(Debug, Serialize, Deserialize)]
pub struct PixPaymentEvent {
    pub payment_id: String,
    pub amount: f64,
    pub receiver_key: String,
    pub payer_key: String,
    pub status: String,
    pub timestamp: String,
}

impl PixPaymentEvent {
    pub fn new(
        payment_id: String,
        amount: f64,
        receiver_key: String,
        payer_key: String,
        status: String,
        timestamp: String,
    ) -> Self {
        Self {
            payment_id,
            amount,
            receiver_key,
            payer_key,
            status,
            timestamp,
        }
    }
}
