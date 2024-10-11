package seguro.infrastructure.logging

import io.prometheus.client.{Counter, Gauge}

class LoggerGrafana {

  // Contador de eventos de log, monitorado pelo Prometheus
  private val logCounter: Counter = Counter.build()
    .name("app_logs_total")
    .help("Total de logs gerados pela aplicação.")
    .register()

  private val logGauge: Gauge = Gauge.build()
    .name("app_log_gauge")
    .help("Estado atual do sistema monitorado via logs.")
    .register()

  def log(mensagem: String, nivel: String = "INFO"): Unit = {
    nivel.toUpperCase match {
      case "INFO" => logInfo(mensagem)
      case "ERROR" => logError(mensagem)
      case "DEBUG" => logDebug(mensagem)
      case _ => println(s"[$nivel] $mensagem")
    }
    atualizarMetricas()
  }

  private def logInfo(mensagem: String): Unit = {
    logCounter.inc() 
    println(s"[INFO] $mensagem")
  }

  private def logError(mensagem: String): Unit = {
    logCounter.inc(2) 
    println(s"[ERROR] $mensagem")
  }

  private def logDebug(mensagem: String): Unit = {
    logCounter.inc() 
    println(s"[DEBUG] $mensagem")
  }

  private def atualizarMetricas(): Unit = {
    logGauge.set(1) 
  }
}
