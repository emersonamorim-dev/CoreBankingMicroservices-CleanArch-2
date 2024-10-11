package seguro.interfaces.controllers

import seguro.application.usecase.CalcularSeguroUseCase
import seguro.application.services.PagamentoService
import seguro.domain.entities.{Segurado, Veiculo}
import seguro.infrastructure.messaging.ClienteRabbitMQ
import spray.json.DefaultJsonProtocol._
import spray.json._
import java.time.format.DateTimeFormatter

case class ClienteInfo(
  nome: String,
  cpf: String,
  email: String,
  telefone: String
)

case class DetalhesSeguroInfo(
  tipoSeguro: String,
  valorSeguro: String,
  franquia: String,
  coberturas: String,
  vigencia: String
)

case class SeguroResponse(
  mensagem: String,
  cliente: ClienteInfo,
  detalhesSeguro: DetalhesSeguroInfo,
  metodoPagamento: String
)

// Protocolo JSON para as classes de resposta
object SeguroResponseProtocol extends DefaultJsonProtocol {
  implicit val clienteInfoFormat: RootJsonFormat[ClienteInfo] = jsonFormat4(ClienteInfo)
  implicit val detalhesSeguroInfoFormat: RootJsonFormat[DetalhesSeguroInfo] = jsonFormat5(DetalhesSeguroInfo)
  implicit val seguroResponseFormat: RootJsonFormat[SeguroResponse] = jsonFormat4(SeguroResponse)
}

class SeguroController(
  calcularSeguroUseCase: CalcularSeguroUseCase,
  pagamentoService: PagamentoService,
  clienteRabbitMQ: ClienteRabbitMQ
) {

  import SeguroResponseProtocol._

  def calcularSeguro(
    nome: String, cpf: String, email: String, telefone: String,
    tipoSeguro: String, marca: String, modelo: String, ano: Int,
    placa: String, valorSeguro: Double, valorFranquia: Double,
    cobertura: Seq[String], vigencia: String, metodoPagamento: String
  ): JsValue = { // Retorna JsValue ao invés de String

    val segurado = Segurado(nome, cpf, email, telefone)
    val veiculo = Veiculo(marca, modelo, ano, placa)

    val seguroCalculado = calcularSeguroUseCase.calcularSeguro(
      segurado = segurado,
      tipoSeguro = tipoSeguro,
      veiculo = veiculo,
      valorVeiculo = valorSeguro,
      franquia = valorFranquia,
      cobertura = cobertura,
      vigencia = vigencia,
      metodoPagamento = metodoPagamento
    )

    // Publicar a mensagem no RabbitMQ
    val mensagem = s"Seguro processado para o veículo ${veiculo.marca} ${veiculo.modelo}, placa ${veiculo.placa}."
    clienteRabbitMQ.publicarMensagem(mensagem)

    // Formatando os valores de seguro usando String.format para garantir que esteja no formato correto
    val valorSeguroFormatado = String.format("R$%.2f", Double.box(seguroCalculado.detalhesSeguro.valorSeguro))
    val franquiaFormatada = String.format("R$%.2f", Double.box(seguroCalculado.detalhesSeguro.valorFranquia))
    val vigenciaFormatada = seguroCalculado.detalhesSeguro.vigencia.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)

    val clienteInfo = ClienteInfo(
      nome = segurado.nome,
      cpf = segurado.cpf,
      email = segurado.email,
      telefone = segurado.telefone
    )

    val detalhesSeguroInfo = DetalhesSeguroInfo(
      tipoSeguro = tipoSeguro,
      valorSeguro = valorSeguroFormatado,
      franquia = franquiaFormatada,
      coberturas = seguroCalculado.detalhesSeguro.cobertura.mkString(", "),
      vigencia = vigenciaFormatada
    )

    val response = SeguroResponse(
      mensagem = mensagem,
      cliente = clienteInfo,
      detalhesSeguro = detalhesSeguroInfo,
      metodoPagamento = metodoPagamento
    )

    response.toJson
  }
}
