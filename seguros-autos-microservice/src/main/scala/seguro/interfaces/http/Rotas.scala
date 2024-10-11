package seguro.interfaces.http

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import spray.json.DefaultJsonProtocol._
import spray.json._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

import seguro.interfaces.controllers.SeguroController

// Classes de request e formatadores JSON
final case class Segurado(nome: String, cpf: String, email: String, telefone: String)
final case class Veiculo(marca: String, modelo: String, ano: Int, placa: String)
final case class DetalhesSeguro(tipoSeguro: String, veiculo: Veiculo, valorSeguro: Double, valorFranquia: Double, cobertura: List[String], vigencia: String)
final case class Pagamento(metodo: String, valor: Double)
final case class CalcularSeguroRequest(segurado: Segurado, detalhesSeguro: DetalhesSeguro, pagamento: Pagamento)

// Protocolo JSON para serialização e desserialização de objetos
object RotasJsonProtocol extends DefaultJsonProtocol {
  implicit val seguradoFormat: RootJsonFormat[Segurado] = jsonFormat4(Segurado)
  implicit val veiculoFormat: RootJsonFormat[Veiculo] = jsonFormat4(Veiculo)
  implicit val detalhesSeguroFormat: RootJsonFormat[DetalhesSeguro] = jsonFormat6(DetalhesSeguro)
  implicit val pagamentoFormat: RootJsonFormat[Pagamento] = jsonFormat2(Pagamento)
  implicit val calcularSeguroRequestFormat: RootJsonFormat[CalcularSeguroRequest] = jsonFormat3(CalcularSeguroRequest)
}

class Rotas(seguroController: SeguroController) {

  import RotasJsonProtocol._

  val rotas: Route = path("calcular-seguro") {
    post {
      entity(as[CalcularSeguroRequest]) { request =>
        // Chamando o controller para calcular o seguro e retornando a resposta no formato JSON
        try {
          val resultado = seguroController.calcularSeguro(
            nome = request.segurado.nome,
            cpf = request.segurado.cpf,
            email = request.segurado.email,
            telefone = request.segurado.telefone,
            tipoSeguro = request.detalhesSeguro.tipoSeguro,
            marca = request.detalhesSeguro.veiculo.marca,
            modelo = request.detalhesSeguro.veiculo.modelo,
            ano = request.detalhesSeguro.veiculo.ano,
            placa = request.detalhesSeguro.veiculo.placa,
            valorSeguro = request.detalhesSeguro.valorSeguro,
            valorFranquia = request.detalhesSeguro.valorFranquia,
            cobertura = request.detalhesSeguro.cobertura,
            vigencia = request.detalhesSeguro.vigencia,
            metodoPagamento = request.pagamento.metodo
          )
          complete(StatusCodes.OK, resultado.toJson)
        } catch {
          case e: Exception =>
            println(s"Erro ao calcular seguro: ${e.getMessage}")
            complete(StatusCodes.InternalServerError, s"Erro ao calcular seguro: ${e.getMessage}")
        }
      }
    }
  } ~ path("teste") {
    get {
      complete("Servidor em funcionamento")
    }
  }
}
