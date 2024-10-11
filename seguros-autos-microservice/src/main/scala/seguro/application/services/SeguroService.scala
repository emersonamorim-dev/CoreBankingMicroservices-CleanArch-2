package seguro.application.services

import seguro.domain.entities._
import seguro.domain.repositories.SeguroRepository

import java.time.ZonedDateTime

class SeguroService(seguroRepository: SeguroRepository) {

  def calcularSeguro(
    segurado: Segurado,
    tipoSeguro: String,
    veiculo: Veiculo,
    valorVeiculo: Double,
    taxaImposto: Double,
    taxaJuros: Double,
    franquia: Double,
    cobertura: Seq[String],
    metodoPagamento: String
  ): Seguro = {

    val valorSeguro = valorVeiculo + (valorVeiculo * taxaImposto) + (valorVeiculo * taxaJuros)
    val valorPagamento = valorSeguro - franquia
    val vigencia = ZonedDateTime.now().plusYears(1) 

    val detalhesSeguro = DetalhesSeguro(
      tipoSeguro = tipoSeguro,
      veiculo = veiculo,
      valorSeguro = valorSeguro,
      valorFranquia = franquia,
      cobertura = cobertura,
      vigencia = vigencia
    )

    val pagamento = Pagamento(
      metodo = metodoPagamento,
      valor = valorPagamento
    )

    val seguro = Seguro(
      segurado = segurado,
      detalhesSeguro = detalhesSeguro,
      pagamento = pagamento
    )

    seguroRepository.salvar(seguro)

    seguro
  }

  def buscarSeguroPorPlaca(placa: String): Option[Seguro] = {
    seguroRepository.buscarPorVeiculo(placa)
  }

  def atualizarSeguro(placa: String, atualizado: Seguro): Boolean = {
    seguroRepository.buscarPorVeiculo(placa) match {
      case Some(_) => 
        seguroRepository.atualizar(placa, atualizado)
        true
      case None => false
    }
  }

  def removerSeguro(placa: String): Boolean = {
    seguroRepository.remover(placa)
  }
}
