pub struct Config {
    pub cassandra_session: cassandra_cpp::Session,
}

impl Config {
    pub async fn new() -> Self {
        let cassandra_session = cassandra_cpp::Session::default();
        Config {
            cassandra_session,
        }
    }
}
