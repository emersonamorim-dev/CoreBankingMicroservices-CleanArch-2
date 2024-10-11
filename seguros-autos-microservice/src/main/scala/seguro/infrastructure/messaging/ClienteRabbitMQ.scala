package seguro.infrastructure.messaging

import com.rabbitmq.client.{Connection, ConnectionFactory, Channel, BuiltinExchangeType}
import scala.util.{Try, Failure, Success}
import scala.concurrent.duration._
import scala.annotation.tailrec

class ClienteRabbitMQ(host: String, exchangeName: String, queueName: String, routingKey: String, maxRetries: Int = 5, retryDelay: FiniteDuration = 5.seconds) {

  private var connection: Option[Connection] = None
  private var channel: Option[Channel] = None

  @tailrec
  final def conectar(tentativas: Int = 1): Unit = {
    val factory = new ConnectionFactory()
    factory.setHost(host)
    factory.setPort(5672)

    Try(factory.newConnection()) match {
      case Success(conn) =>
        connection = Some(conn)
        channel = Some(conn.createChannel())
        println("Conexão com RabbitMQ estabelecida.")
        declararExchangeEFilas() // Adiciona a declaração de exchange e fila

      case Failure(exception) if tentativas < maxRetries =>
        println(s"Erro ao conectar ao RabbitMQ: ${exception.getMessage}. Retentando...")
        Thread.sleep(retryDelay.toMillis)
        conectar(tentativas + 1)

      case Failure(exception) =>
        println(s"Falha ao conectar ao RabbitMQ após $tentativas tentativas. Exceção: ${exception.getMessage}")
        throw exception
    }
  }

  def declararExchangeEFilas(): Unit = {
    channel.foreach { ch =>
      Try {
        ch.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT, true)
        ch.queueDeclare(queueName, true, false, false, null)
        ch.queueBind(queueName, exchangeName, routingKey)
        println(s"Exchange '$exchangeName' e fila '$queueName' declaradas com sucesso.")
      } match {
        case Success(_) =>
          println(s"Exchange '$exchangeName' e fila '$queueName' vinculadas à chave de roteamento '$routingKey'.")

        case Failure(exception) =>
          println(s"Falha ao declarar Exchange e Fila: ${exception.getMessage}")
      }
    }
  }

  def publicarMensagem(mensagem: String): Unit = {
    channel match {
      case Some(ch) =>
        ch.basicPublish(exchangeName, routingKey, null, mensagem.getBytes("UTF-8"))
        println(s"Mensagem publicada: $mensagem")

      case None =>
        println("Erro ao publicar mensagem: Nenhum canal ativo encontrado.")
    }
  }

  def fecharConexao(): Unit = {
    channel.foreach(_.close())
    connection.foreach(_.close())
    println("Conexão com RabbitMQ encerrada.")
  }
}
