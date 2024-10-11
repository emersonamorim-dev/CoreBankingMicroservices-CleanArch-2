package seguro.infrastructure.cache

import redis.clients.jedis.{Jedis, JedisPool, JedisPoolConfig}

class ClienteRedis(host: String = "localhost", port: Int = 6379) {
  
  private val poolConfig = new JedisPoolConfig()
  poolConfig.setMaxTotal(10)   // Configuração de pool de conexões com Redis
  private val jedisPool = new JedisPool(poolConfig, host, port)

  def conectar(): Jedis = {
    try {
      val jedis = jedisPool.getResource
      println(s"Conectado ao Redis no host $host e porta $port.")
      jedis
    } catch {
      case ex: Exception =>
        println(s"Erro ao conectar ao Redis: ${ex.getMessage}")
        throw ex
    }
  }

  def salvarChave(chave: String, valor: String): Unit = {
    val jedis = conectar()
    try {
      jedis.set(chave, valor)
      println(s"Chave '$chave' salva com valor '$valor'.")
    } finally {
      jedis.close()  
    }
  }

  def buscarValor(chave: String): Option[String] = {
    val jedis = conectar()
    try {
      val valor = jedis.get(chave)
      if (valor != null) {
        println(s"Valor encontrado para a chave '$chave': $valor")
        Some(valor)
      } else {
        println(s"Nenhum valor encontrado para a chave '$chave'.")
        None
      }
    } finally {
      jedis.close()  
    }
  }
}
