use serde::{Deserialize, Serialize};
use uuid::Uuid;

#[derive(Clone, Serialize, Deserialize)]
pub struct PixPayment {
    pub id: String,
    pub amount: f64,
    pub receiver_key: String,
    pub payer_key: String,
    pub timestamp: String,
}

impl PixPayment {
    pub fn new(amount: f64, receiver_key: String, payer_key: String, timestamp: String) -> Self {
        Self {
            id: Uuid::new_v4().to_string(), 
            amount,
            receiver_key,
            payer_key,
            timestamp,
        }
    }
}
