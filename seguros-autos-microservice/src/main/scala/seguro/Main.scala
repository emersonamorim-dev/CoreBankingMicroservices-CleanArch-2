package seguro

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import seguro.infrastructure.db.ConexaoDynamoDB
import seguro.infrastructure.messaging.ClienteRabbitMQ
import seguro.infrastructure.monitoring.PrometheusMetricas
import seguro.interfaces.http.Rotas
import seguro.domain.repositories.InMemorySeguroRepository
import seguro.application.services.{SeguroService, PagamentoService}
import seguro.application.usecase.CalcularSeguroUseCase
import seguro.interfaces.controllers.SeguroController
import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}
import com.typesafe.config.ConfigFactory

object Main extends App {
  implicit val system: ActorSystem = ActorSystem("seguroServiceSystem")
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  // Configuração de variáveis de ambiente para RabbitMQ
  val rabbitMqHost = sys.env.getOrElse("RABBITMQ_HOST", "rabbitmq")
  val rabbitMqExchange = sys.env.getOrElse("RABBITMQ_EXCHANGE", "direct_exchange_seguros")
  val rabbitMqQueue = sys.env.getOrElse("RABBITMQ_QUEUE", "fila_seguros")
  val rabbitMqRoutingKey = sys.env.getOrElse("RABBITMQ_ROUTING_KEY", "seguro_key")
  val port = 8020

  // Inicialização de DynamoDB e RabbitMQ
  val dynamoDB = new ConexaoDynamoDB("fakeAccessKey", "fakeSecretKey")
  val rabbitMQClient = new ClienteRabbitMQ(rabbitMqHost, rabbitMqExchange, rabbitMqQueue, rabbitMqRoutingKey)

  // Instancia serviços e use cases
  val seguroRepository = new InMemorySeguroRepository()
  val seguroService = new SeguroService(seguroRepository)
  val pagamentoService = new PagamentoService()
  val calcularSeguroUseCase = new CalcularSeguroUseCase(seguroService, pagamentoService)

  // Instancia o controller com clienteRabbitMQ
  val seguroController = new SeguroController(calcularSeguroUseCase, pagamentoService, rabbitMQClient)

  // Inicializa as rotas com o controller correto
  val rotas = new Rotas(seguroController).rotas

  iniciarAplicacao()

  def iniciarAplicacao(): Unit = {
    println("Iniciando o microserviço de seguros de autos...")
    try {
      dynamoDB.testarConexao()
      tentarConectarRabbitMQ(5)
      registrarMetricas()
      iniciarServidorHttp(rotas)
      processarSeguros()
    } catch {
      case e: Exception =>
        println(s"Erro durante a inicialização da aplicação: ${e.getMessage}")
        e.printStackTrace()
    }
  }

  def tentarConectarRabbitMQ(maxTentativas: Int): Unit = {
    var tentativa = 1
    var conectado = false

    while (tentativa <= maxTentativas && !conectado) {
      try {
        println(s"Tentativa $tentativa: Conectando ao RabbitMQ no host $rabbitMqHost...")
        rabbitMQClient.conectar()
        println(s"Conexão com RabbitMQ estabelecida.")
        rabbitMQClient.declararExchangeEFilas()
        conectado = true
      } catch {
        case e: Exception =>
          println(s"Erro ao conectar ao RabbitMQ: ${e.getMessage}. Retentando em 3 segundos...")
          Thread.sleep(3000)
          tentativa += 1
      }
    }

    if (!conectado) {
      throw new Exception("Falha ao conectar ao RabbitMQ após várias tentativas.")
    }
  }

  def registrarMetricas(): Unit = {
    println("Registrando métricas no Prometheus...")
    PrometheusMetricas.incrementarSegurosCalculados()
  }

  def processarSeguros(): Unit = {
    PrometheusMetricas.iniciarProcessamentoSeguro()
    PrometheusMetricas.medirLatenciaCalculoSeguro {
      println("Processando seguro...")
      Thread.sleep(1000)
      rabbitMQClient.publicarMensagem("Seguro processado com sucesso!")
    }
    PrometheusMetricas.finalizarProcessamentoSeguro()
  }

  def iniciarServidorHttp(rotas: Route): Future[Http.ServerBinding] = {
    val bindingFuture = Http().newServerAt("0.0.0.0", port).bind(rotas)
    bindingFuture.onComplete {
      case Success(binding) =>
        println(s"Servidor HTTP rodando em http://localhost:$port/")
      case Failure(exception) =>
        println(s"Erro ao iniciar o servidor HTTP: ${exception.getMessage}")
        system.terminate()
    }
    bindingFuture
  }

  sys.addShutdownHook {
    println("Encerrando a aplicação e fechando conexões...")
    rabbitMQClient.fecharConexao()
    system.terminate()
  }
}
