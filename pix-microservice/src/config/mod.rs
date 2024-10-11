[package]
name = "pix-microservice"
version = "0.1.0"
edition = "2021"

[dependencies]
tokio = { version = "1", features = ["full"] }
lapin = { version = "2.5.0", default-features = false, features = ["tokio", "native-tls", "tcp"] }
serde = { version = "1.0", features = ["derive"] }
serde_json = "1.0"
uuid = { version = "1.0" }
cassandra-cpp = "2.0"
redis = "0.21"
prometheus = "0.13"
tracing = "0.1"
tracing-subscriber = "0.2"
reqwest = { version = "0.11", features = ["json", "native-tls"] }

[[bin]]
name = "pix-microservice"
path = "src/main.rs"
