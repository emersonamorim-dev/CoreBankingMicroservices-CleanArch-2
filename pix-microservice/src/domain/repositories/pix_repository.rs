use async_trait::async_trait;
use crate::domain::entities::pix_payment::PixPayment;

#[async_trait]
pub trait PixRepository: Send + Sync {
    async fn save_payment(&self, payment: PixPayment) -> Result<(), String>;
}
