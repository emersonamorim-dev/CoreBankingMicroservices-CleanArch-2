package seguro.application.usecase

import seguro.domain.entities.{Seguro, Segurado, DetalhesSeguro, Veiculo, Pagamento}
import seguro.application.services.{SeguroService, PagamentoService}
import seguro.infrastructure.config.Configuracoes

class CalcularSeguroUseCase(seguroService: SeguroService, pagamentoService: PagamentoService) {
  
  def calcularSeguro(
    segurado: Segurado,
    tipoSeguro: String,
    veiculo: Veiculo,
    valorVeiculo: Double,
    franquia: Double,
    cobertura: Seq[String],
    vigencia: String,
    metodoPagamento: String
  ): Seguro = {

    val taxaImposto = Configuracoes.taxaImposto
    val taxaJuros = Configuracoes.taxaJuros
    
    // Calcula o seguro
    seguroService.calcularSeguro(segurado, tipoSeguro, veiculo, valorVeiculo, taxaImposto, taxaJuros, franquia, cobertura, metodoPagamento)
  }
}
