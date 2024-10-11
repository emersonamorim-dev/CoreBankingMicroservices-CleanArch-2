package seguro.application.services

import seguro.domain.entities.Pagamento
import java.time.ZonedDateTime

class PagamentoService {

  def processarPagamento(valor: Double, metodoPagamento: String): Pagamento = {
    
    val validPaymentMethods = Seq("PIX", "Cartao de Credito", "Cartao de Debito", "Boleto")
    require(validPaymentMethods.contains(metodoPagamento), s"Método de pagamento $metodoPagamento não é suportado.")

    println(s"Processando pagamento de R$$valor via $metodoPagamento.")

    Pagamento(
      metodo = metodoPagamento,
      valor = valor
    )
  }

  def validarStatusPagamento(metodoPagamento: String): Boolean = {
    metodoPagamento match {
      case "PIX" | "Cartao de Credito" | "Cartao de Debito" => true
      case "Boleto" => false
    }
  }
}
