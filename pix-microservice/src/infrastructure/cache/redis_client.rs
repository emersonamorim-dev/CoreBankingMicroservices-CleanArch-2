use redis::AsyncCommands;

pub struct RedisClient {
    pub client: redis::Client,
}

impl RedisClient {
    pub async fn new(connection_string: &str) -> Result<Self, redis::RedisError> {
        let client = redis::Client::open(connection_string)?;
        Ok(Self { client })
    }

    pub async fn get(&self, key: &str) -> Result<Option<String>, redis::RedisError> {
        let mut conn = self.client.get_async_connection().await?;
        conn.get(key).await
    }

    pub async fn set(&self, key: &str, value: &str) -> Result<(), redis::RedisError> {
        let mut conn = self.client.get_async_connection().await?;
        conn.set(key, value).await
    }
}
