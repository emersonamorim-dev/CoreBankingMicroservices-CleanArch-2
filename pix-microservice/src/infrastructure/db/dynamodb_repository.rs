use async_trait::async_trait;
use aws_sdk_dynamodb::model::{AttributeValue, KeySchemaElement, ScalarAttributeType, ProvisionedThroughput, KeyType}; // Ajuste correto das importações
use crate::domain::entities::pix_payment::PixPayment;
use crate::domain::repositories::pix_repository::PixRepository;
use crate::infrastructure::db::dynamodb_connection::DynamoDbConnection;

pub struct DynamoDBRepository {
    pub connection: DynamoDbConnection,
}

impl DynamoDBRepository {
    pub fn new(connection: DynamoDbConnection) -> Self {
        Self { connection }
    }

    // verifica se a tabela existe e, se não, criá-la
    pub async fn ensure_table_exists(&self, table_name: &str) -> Result<(), String> {
        let client = &self.connection.client;

        let table_exists = client
            .list_tables()
            .send()
            .await
            .map_err(|e| e.to_string())?
            .table_names()
            .unwrap_or_default()
            .contains(&table_name.to_string());

        if !table_exists {
            // Se a tabela não existir, cria uma nova tabela
            client
                .create_table()
                .table_name(table_name)
                .key_schema(
                    KeySchemaElement::builder()
                        .attribute_name("id")
                        .key_type(KeyType::Hash) 
                        .build(),
                )
                .attribute_definitions(
                    aws_sdk_dynamodb::model::AttributeDefinition::builder()
                        .attribute_name("id")
                        .attribute_type(ScalarAttributeType::S) 
                        .build(),
                )
                .provisioned_throughput(
                    ProvisionedThroughput::builder()
                        .read_capacity_units(5)
                        .write_capacity_units(5)
                        .build(),
                )
                .send()
                .await
                .map_err(|e| e.to_string())?;

            println!("Tabela '{}' criada com sucesso!", table_name);
        }

        Ok(())
    }
}

#[async_trait]
impl PixRepository for DynamoDBRepository {
    async fn save_payment(&self, payment: PixPayment) -> Result<(), String> {
        let table_name = "PixPayments";

        self.ensure_table_exists(table_name).await?;

        let mut item = std::collections::HashMap::new();
        item.insert("id".to_string(), AttributeValue::S(payment.id.clone()));
        item.insert("amount".to_string(), AttributeValue::N(payment.amount.to_string()));
        item.insert("receiver_key".to_string(), AttributeValue::S(payment.receiver_key.clone()));
        item.insert("payer_key".to_string(), AttributeValue::S(payment.payer_key.clone()));
        item.insert("timestamp".to_string(), AttributeValue::S(payment.timestamp.clone()));

        self.connection
            .client
            .put_item()
            .table_name(table_name)
            .set_item(Some(item))
            .send()
            .await
            .map_err(|e| e.to_string())?;

        Ok(())
    }
}
