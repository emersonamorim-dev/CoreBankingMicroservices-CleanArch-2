from pydantic import BaseSettings

class Settings(BaseSettings):
    MONGO_URL: str
    RABBITMQ_URL: str
    REDIS_URL: str
    PROMETHEUS_PORT: int

    class Config:
        env_file = ".env"
