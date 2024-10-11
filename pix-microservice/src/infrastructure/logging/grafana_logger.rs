use log::{Record, Level, Metadata, LevelFilter};
use prometheus::{Encoder, HistogramVec, IntCounterVec, Opts, TextEncoder};
use std::sync::Mutex;
use lazy_static::lazy_static;

pub struct GrafanaLogger {
    pub request_count: IntCounterVec,
    pub request_duration: HistogramVec,
}

impl GrafanaLogger {
    pub fn new() -> Self {
        let request_count_opts = Opts::new("request_count", "Número de requisições HTTP processadas");
        let request_duration_opts = Opts::new("request_duration_seconds", "Duração das requisições HTTP");

        let request_count = IntCounterVec::new(request_count_opts, &["method", "endpoint", "status"])
            .expect("Erro ao criar métrica de contagem de requisições");
        
        let request_duration = HistogramVec::new(request_duration_opts, &["method", "endpoint", "status"])
            .expect("Erro ao criar métrica de duração das requisições");

        prometheus::register(Box::new(request_count.clone())).expect("Erro ao registrar contador no Prometheus");
        prometheus::register(Box::new(request_duration.clone())).expect("Erro ao registrar histogram no Prometheus");

        GrafanaLogger {
            request_count,
            request_duration,
        }
    }

    pub fn expose_metrics(&self) -> String {
        let metric_families = prometheus::gather();
        let mut buffer = Vec::new();
        let encoder = TextEncoder::new();
        encoder.encode(&metric_families, &mut buffer).expect("Erro ao codificar métricas para Prometheus");
        String::from_utf8(buffer).expect("Erro ao converter buffer de métricas para String")
    }
}

pub struct Logger;

impl log::Log for Logger {
    fn enabled(&self, metadata: &Metadata) -> bool {
        metadata.level() <= Level::Info
    }

    fn log(&self, record: &Record) {
        if self.enabled(record.metadata()) {
            println!(
                "[{}] - {}: {}",
                record.level(),
                record.target(),
                record.args()
            );
        }
    }

    fn flush(&self) {}
}

lazy_static! {
    pub static ref LOGGER: Logger = Logger;
    pub static ref GRAFANA_LOGGER: Mutex<GrafanaLogger> = Mutex::new(GrafanaLogger::new());
}

/// Inicializa o logger global
pub fn init() {
    log::set_logger(&*LOGGER).expect("Erro ao inicializar o logger");
    log::set_max_level(LevelFilter::Info);
}

/// coleta as métricas e as expõe no endpoint HTTP para serem consumidas pelo Prometheus
pub async fn serve_metrics() -> String {
    GRAFANA_LOGGER.lock().unwrap().expose_metrics()
}
