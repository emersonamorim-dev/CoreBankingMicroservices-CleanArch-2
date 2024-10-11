package seguro.infrastructure.monitoring

import io.prometheus.client.{Counter, Gauge, Histogram, Summary}

object PrometheusMetricas {

  // Contador para o total de seguros calculados
  val totalSegurosCalculados: Counter = Counter.build()
    .name("seguros_calculados_total")
    .help("Total de seguros calculados.")
    .register()

  val segurosEmProcessamento: Gauge = Gauge.build()
    .name("seguros_em_processamento")
    .help("Número de seguros em processamento no momento.")
    .register()

  // Histogram para medir a latência no cálculo de seguros
  val latenciaCalculoSeguro: Histogram = Histogram.build()
    .name("latencia_calculo_seguro")
    .help("Latência do cálculo de seguros.")
    .register()

  val tempoTotalCalculoSeguro: Summary = Summary.build()
    .name("tempo_total_calculo_seguro")
    .help("Tempo total gasto no cálculo de seguros.")
    .register()

  def incrementarSegurosCalculados(): Unit = {
    totalSegurosCalculados.inc() 
  }

  def iniciarProcessamentoSeguro(): Unit = {
    segurosEmProcessamento.inc() 
  }

  def finalizarProcessamentoSeguro(): Unit = {
    segurosEmProcessamento.dec() 
  }

  // Mede a latência do cálculo de seguro
  def medirLatenciaCalculoSeguro(blocoCalculo: => Unit): Unit = {
    val timer = latenciaCalculoSeguro.startTimer()
    try {
      blocoCalculo
    } finally {
      timer.observeDuration() 
    }
  }

  def medirTempoTotalCalculoSeguro(blocoCalculo: => Unit): Unit = {
    val timer = tempoTotalCalculoSeguro.startTimer()
    try {
      blocoCalculo
    } finally {
      timer.observeDuration() 
    }
  }
}
