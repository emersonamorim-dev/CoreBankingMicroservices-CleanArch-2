import redis
import logging

class RedisClient:
    def __init__(self, redis_url: str = "redis://redis-cache:6380/0"):  # Atualize para 'redis-cache'
        try:
            self.client = redis.StrictRedis.from_url(redis_url, decode_responses=True)
            self.client.ping()  # Verifica se o Redis está acessível
            logging.info(f"Conectado ao Redis na URL {redis_url}")
        except redis.ConnectionError as e:
            logging.error(f"Erro ao conectar ao Redis: {e}")
            raise e

    def set_cache(self, chave: str, valor: str, exp: int):
        self.client.set(chave, valor, ex=exp)

    def get_cache(self, chave: str) -> str:
        return self.client.get(chave)
