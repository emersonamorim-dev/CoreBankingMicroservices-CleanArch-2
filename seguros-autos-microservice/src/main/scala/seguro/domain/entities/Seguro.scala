package seguro.domain.entities

import java.time.ZonedDateTime

case class Segurado(
  nome: String,
  cpf: String,
  email: String,
  telefone: String
)

case class Veiculo(
  marca: String,
  modelo: String,
  ano: Int,
  placa: String
)

case class DetalhesSeguro(
  tipoSeguro: String,
  veiculo: Veiculo,
  valorSeguro: Double,
  valorFranquia: Double,
  cobertura: Seq[String],
  vigencia: ZonedDateTime
)

case class Pagamento(
  metodo: String,
  valor: Double
)

case class Seguro(
  segurado: Segurado,
  detalhesSeguro: DetalhesSeguro,
  pagamento: Pagamento
)
