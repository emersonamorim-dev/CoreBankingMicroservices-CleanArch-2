use std::error::Error;
use qrcode::{QrCode, render::svg};

pub struct QrCodeService;

impl QrCodeService {
    pub async fn generate_qr_code(&self, data: &str) -> Result<String, Box<dyn Error>> {
        let code = QrCode::new(data)?;
        let image = code.render::<svg::Color>().build();
        Ok(image)
    }
}
