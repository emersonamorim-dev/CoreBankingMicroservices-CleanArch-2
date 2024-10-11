use aws_sdk_dynamodb::{Client, Endpoint};
use aws_config::meta::region::RegionProviderChain;
use aws_sdk_dynamodb::config::Builder;
use aws_types::Credentials;

pub struct DynamoDbConnection {
    pub client: Client,
}

impl DynamoDbConnection {
    pub async fn new(endpoint: &str) -> Self {
        let region_provider = RegionProviderChain::default_provider().or_else("us-east-1");

        let credentials = Credentials::new(
            "fakeAccessKeyId",   // Chave de acesso falsa
            "fakeSecretAccessKey", // Chave secreta falsa
            None, 
            None, 
            "static"
        );

        let shared_config = aws_config::from_env()
            .region(region_provider)
            .credentials_provider(credentials)  
            .load()
            .await;

        // configuração do DynamoDB usando o endpoint local
        let config = Builder::from(&shared_config)
            .endpoint_resolver(Endpoint::immutable(endpoint.parse().expect("Inválido endpoint")))
            .build();

        let client = Client::from_conf(config);

        Self { client }
    }
}
