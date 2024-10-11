package seguro.infrastructure.config

object Configuracoes {
  val taxaImposto: Double = sys.env.getOrElse("TAXA_IMPOSTO", "0.05").toDouble
  val taxaJuros: Double = sys.env.getOrElse("TAXA_JUROS", "0.02").toDouble
}
