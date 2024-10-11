FROM rust:latest

WORKDIR /app
COPY . .

# Instalar rustfmt
RUN rustup component add rustfmt

# Construir a aplicação em modo release
RUN cargo build --release

# Expor a porta 8096
EXPOSE 8096

# Comando para iniciar a aplicação
CMD ["./target/release/pix-microservice"]
