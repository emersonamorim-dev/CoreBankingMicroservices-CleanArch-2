use actix_web::web;
use crate::interfaces::controllers::pix_controller::process_pix_payment;

pub fn init_routes(cfg: &mut web::ServiceConfig) {
    cfg.service(
        web::resource("/pix/payment")
            .route(web::post().to(process_pix_payment)),
    );
}
