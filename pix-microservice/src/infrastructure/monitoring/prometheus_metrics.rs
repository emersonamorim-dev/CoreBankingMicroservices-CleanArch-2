use prometheus::{IntCounter, Opts, Registry};

pub struct PrometheusMetrics {
    pub pix_payment_counter: IntCounter,
}

impl PrometheusMetrics {
    pub fn new(registry: &Registry) -> Self {
        let opts = Opts::new("pix_payments_total", "Total de pagamentos Pix processados");
        let pix_payment_counter = IntCounter::with_opts(opts).unwrap();
        registry.register(Box::new(pix_payment_counter.clone())).unwrap();

        Self { pix_payment_counter }
    }

    pub fn increment_payment_counter(&self) {
        self.pix_payment_counter.inc();
    }
}
