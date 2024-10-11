file://<WORKSPACE>/src/main/scala/seguro/Main.scala
### java.lang.AssertionError: assertion failed: tree: StringFormat[A](null:
  (scala.concurrent.ExecutionContext.Implicits.global :
    => scala.concurrent.ExecutionContext)
), pt: <notype>

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 3.3.3
Classpath:
<HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala3-library_3/3.3.3/scala3-library_3-3.3.3.jar [exists ], <HOME>/.cache/coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.13.12/scala-library-2.13.12.jar [exists ]
Options:



action parameters:
uri: file://<WORKSPACE>/src/main/scala/seguro/Main.scala
text:
```scala
package seguro

import seguro.infrastructure.db.ConexaoFaunaDB
import seguro.infrastructure.messaging.ClienteRabbitMQ
import seguro.infrastructure.cache.ClienteRedis
import seguro.infrastructure.monitoring.PrometheusMetricas

import scala.concurrent.ExecutionContext.Implicits.global

object Main extends App {

  // Definição das variáveis de conexão
  val faunaDB = new ConexaoFaunaDB("your-fauna-secret")
  val rabbitMQClient = new ClienteRabbitMQ("localhost", "fila_seguros")
  
  // Inicialização do sistema
  iniciarAplicacao()

  def iniciarAplicacao(): Unit = {
    println("Iniciando o microserviço de seguros de autos...")

    // Conectar ao FaunaDB
    faunaDB.conectar()

    // Conectar ao RabbitMQ
    rabbitMQClient.conectar()

    // Registrar métricas do Prometheus
    registrarMetricas()

    // Simular o processamento de seguros e envio de mensagens
    processarSeguros()
  }

  def registrarMetricas(): Unit = {
    println("Registrando métricas no Prometheus...")
    PrometheusMetricas.incrementarSegurosCalculados()
  }

  def processarSeguros(): Unit = {
    PrometheusMetricas.iniciarProcessamentoSeguro()

    PrometheusMetricas.medirLatenciaCalculoSeguro {
      println("Processando seguro...")
      Thread.sleep(1000)  // Simulação de processamento

      rabbitMQClient.publicarMensagem("Seguro processado com sucesso!")
    }

    PrometheusMetricas.finalizarProcessamentoSeguro()
  }

  // Hook para encerrar a aplicação e fechar as conexões corretamente
  sys.addShutdownHook {
    println("Encerrando a aplicação e fechando conexões...")
    faunaDB.fecharConexao()
    rabbitMQClient.fecharConexao()
  }
}

```



#### Error stacktrace:

```
scala.runtime.Scala3RunTime$.assertFailed(Scala3RunTime.scala:8)
	dotty.tools.dotc.typer.Typer.adapt1(Typer.scala:3598)
	dotty.tools.dotc.typer.Typer.adapt(Typer.scala:3590)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3187)
	dotty.tools.dotc.typer.Implicits.tryConversion$1(Implicits.scala:1137)
	dotty.tools.dotc.typer.Implicits.typedImplicit(Implicits.scala:1168)
	dotty.tools.dotc.typer.Implicits.typedImplicit$(Implicits.scala:819)
	dotty.tools.dotc.typer.Typer.typedImplicit(Typer.scala:117)
	dotty.tools.dotc.typer.Implicits$ImplicitSearch.tryImplicit(Implicits.scala:1243)
	dotty.tools.dotc.typer.Implicits$ImplicitSearch.rank$1(Implicits.scala:1342)
	dotty.tools.dotc.typer.Implicits$ImplicitSearch.searchImplicit(Implicits.scala:1512)
	dotty.tools.dotc.typer.Implicits$ImplicitSearch.searchImplicit(Implicits.scala:1540)
	dotty.tools.dotc.typer.Implicits$ImplicitSearch.bestImplicit(Implicits.scala:1573)
	dotty.tools.dotc.typer.Implicits.inferImplicit(Implicits.scala:1061)
	dotty.tools.dotc.typer.Implicits.inferImplicit$(Implicits.scala:819)
	dotty.tools.dotc.typer.Typer.inferImplicit(Typer.scala:117)
	dotty.tools.dotc.typer.Implicits.inferView(Implicits.scala:857)
	dotty.tools.dotc.typer.Implicits.inferView$(Implicits.scala:819)
	dotty.tools.dotc.typer.Typer.inferView(Typer.scala:117)
	dotty.tools.dotc.typer.Implicits.viewExists(Implicits.scala:832)
	dotty.tools.dotc.typer.Implicits.viewExists$(Implicits.scala:819)
	dotty.tools.dotc.typer.Typer.viewExists(Typer.scala:117)
	dotty.tools.dotc.typer.Implicits.ignoredConvertibleImplicits$1$$anonfun$3(Implicits.scala:961)
	scala.collection.Iterator$$anon$6.hasNext(Iterator.scala:479)
	scala.collection.Iterator.isEmpty(Iterator.scala:466)
	scala.collection.Iterator.isEmpty$(Iterator.scala:466)
	scala.collection.AbstractIterator.isEmpty(Iterator.scala:1300)
	scala.collection.View$Filter.isEmpty(View.scala:146)
	scala.collection.IterableOnceOps.nonEmpty(IterableOnce.scala:853)
	scala.collection.IterableOnceOps.nonEmpty$(IterableOnce.scala:853)
	scala.collection.AbstractIterable.nonEmpty(Iterable.scala:933)
	dotty.tools.dotc.reporting.MissingImplicitArgument.noChainConversionsNote$1(messages.scala:2929)
	dotty.tools.dotc.reporting.MissingImplicitArgument.msgPostscript$$anonfun$4(messages.scala:2944)
	scala.Option.orElse(Option.scala:477)
	dotty.tools.dotc.reporting.MissingImplicitArgument.msgPostscript(messages.scala:2944)
	dotty.tools.dotc.reporting.Message.message$$anonfun$1(Message.scala:344)
	dotty.tools.dotc.reporting.Message.inMessageContext(Message.scala:340)
	dotty.tools.dotc.reporting.Message.message(Message.scala:344)
	dotty.tools.dotc.reporting.Message.isNonSensical(Message.scala:321)
	dotty.tools.dotc.reporting.HideNonSensicalMessages.isHidden(HideNonSensicalMessages.scala:16)
	dotty.tools.dotc.reporting.HideNonSensicalMessages.isHidden$(HideNonSensicalMessages.scala:10)
	dotty.tools.dotc.interactive.InteractiveDriver$$anon$5.isHidden(InteractiveDriver.scala:156)
	dotty.tools.dotc.reporting.Reporter.issueUnconfigured(Reporter.scala:156)
	dotty.tools.dotc.reporting.Reporter.go$1(Reporter.scala:181)
	dotty.tools.dotc.reporting.Reporter.issueIfNotSuppressed(Reporter.scala:200)
	dotty.tools.dotc.reporting.Reporter.report(Reporter.scala:203)
	dotty.tools.dotc.reporting.StoreReporter.report(StoreReporter.scala:50)
	dotty.tools.dotc.report$.error(report.scala:68)
	dotty.tools.dotc.typer.Typer.issueErrors$1$$anonfun$1(Typer.scala:3811)
	scala.runtime.function.JProcedure3.apply(JProcedure3.java:15)
	scala.runtime.function.JProcedure3.apply(JProcedure3.java:10)
	scala.collection.LazyZip3.foreach(LazyZipOps.scala:248)
	dotty.tools.dotc.typer.Typer.issueErrors$1(Typer.scala:3813)
	dotty.tools.dotc.typer.Typer.addImplicitArgs$1(Typer.scala:3835)
	dotty.tools.dotc.typer.Typer.adaptNoArgsImplicitMethod$1(Typer.scala:3852)
	dotty.tools.dotc.typer.Typer.adaptNoArgs$1(Typer.scala:4047)
	dotty.tools.dotc.typer.Typer.adapt1(Typer.scala:4277)
	dotty.tools.dotc.typer.Typer.adapt(Typer.scala:3590)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3187)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3191)
	dotty.tools.dotc.typer.Typer.typedExpr(Typer.scala:3303)
	dotty.tools.dotc.typer.Namer.typedAheadExpr$$anonfun$1(Namer.scala:1656)
	dotty.tools.dotc.typer.Namer.typedAhead(Namer.scala:1646)
	dotty.tools.dotc.typer.Namer.typedAheadExpr(Namer.scala:1656)
	dotty.tools.dotc.typer.Namer.typedAheadRhs$1$$anonfun$1(Namer.scala:1909)
	dotty.tools.dotc.inlines.PrepareInlineable$.dropInlineIfError(PrepareInlineable.scala:243)
	dotty.tools.dotc.typer.Namer.typedAheadRhs$1(Namer.scala:1909)
	dotty.tools.dotc.typer.Namer.rhsType$1(Namer.scala:1917)
	dotty.tools.dotc.typer.Namer.cookedRhsType$1(Namer.scala:1935)
	dotty.tools.dotc.typer.Namer.lhsType$1(Namer.scala:1936)
	dotty.tools.dotc.typer.Namer.inferredResultType(Namer.scala:1947)
	dotty.tools.dotc.typer.Namer.inferredType$1(Namer.scala:1694)
	dotty.tools.dotc.typer.Namer.valOrDefDefSig(Namer.scala:1701)
	dotty.tools.dotc.typer.Namer$Completer.typeSig(Namer.scala:787)
	dotty.tools.dotc.typer.Namer$Completer.completeInCreationContext(Namer.scala:934)
	dotty.tools.dotc.typer.Namer$Completer.complete(Namer.scala:814)
	dotty.tools.dotc.core.SymDenotations$SymDenotation.completeFrom(SymDenotations.scala:174)
	dotty.tools.dotc.core.Denotations$Denotation.completeInfo$1(Denotations.scala:187)
	dotty.tools.dotc.core.Denotations$Denotation.info(Denotations.scala:189)
	dotty.tools.dotc.core.SymDenotations$SymDenotation.ensureCompleted(SymDenotations.scala:393)
	dotty.tools.dotc.typer.Typer.retrieveSym(Typer.scala:2991)
	dotty.tools.dotc.typer.Typer.typedNamed$1(Typer.scala:3016)
	dotty.tools.dotc.typer.Typer.typedUnadapted(Typer.scala:3114)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3187)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3191)
	dotty.tools.dotc.typer.Typer.traverse$1(Typer.scala:3213)
	dotty.tools.dotc.typer.Typer.typedStats(Typer.scala:3259)
	dotty.tools.dotc.typer.Typer.typedClassDef(Typer.scala:2669)
	dotty.tools.dotc.typer.Typer.typedTypeOrClassDef$1(Typer.scala:3038)
	dotty.tools.dotc.typer.Typer.typedNamed$1(Typer.scala:3042)
	dotty.tools.dotc.typer.Typer.typedUnadapted(Typer.scala:3114)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3187)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3191)
	dotty.tools.dotc.typer.Typer.traverse$1(Typer.scala:3213)
	dotty.tools.dotc.typer.Typer.typedStats(Typer.scala:3259)
	dotty.tools.dotc.typer.Typer.typedPackageDef(Typer.scala:2812)
	dotty.tools.dotc.typer.Typer.typedUnnamed$1(Typer.scala:3083)
	dotty.tools.dotc.typer.Typer.typedUnadapted(Typer.scala:3115)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3187)
	dotty.tools.dotc.typer.Typer.typed(Typer.scala:3191)
	dotty.tools.dotc.typer.Typer.typedExpr(Typer.scala:3303)
	dotty.tools.dotc.typer.TyperPhase.typeCheck$$anonfun$1(TyperPhase.scala:44)
	dotty.tools.dotc.typer.TyperPhase.typeCheck$$anonfun$adapted$1(TyperPhase.scala:50)
	scala.Function0.apply$mcV$sp(Function0.scala:42)
	dotty.tools.dotc.core.Phases$Phase.monitor(Phases.scala:440)
	dotty.tools.dotc.typer.TyperPhase.typeCheck(TyperPhase.scala:50)
	dotty.tools.dotc.typer.TyperPhase.runOn$$anonfun$3(TyperPhase.scala:84)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	scala.collection.immutable.List.foreach(List.scala:333)
	dotty.tools.dotc.typer.TyperPhase.runOn(TyperPhase.scala:84)
	dotty.tools.dotc.Run.runPhases$1$$anonfun$1(Run.scala:246)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	scala.collection.ArrayOps$.foreach$extension(ArrayOps.scala:1323)
	dotty.tools.dotc.Run.runPhases$1(Run.scala:262)
	dotty.tools.dotc.Run.compileUnits$$anonfun$1(Run.scala:270)
	dotty.tools.dotc.Run.compileUnits$$anonfun$adapted$1(Run.scala:279)
	dotty.tools.dotc.util.Stats$.maybeMonitored(Stats.scala:71)
	dotty.tools.dotc.Run.compileUnits(Run.scala:279)
	dotty.tools.dotc.Run.compileSources(Run.scala:194)
	dotty.tools.dotc.interactive.InteractiveDriver.run(InteractiveDriver.scala:165)
	scala.meta.internal.pc.MetalsDriver.run(MetalsDriver.scala:45)
	scala.meta.internal.pc.WithCompilationUnit.<init>(WithCompilationUnit.scala:28)
	scala.meta.internal.pc.SimpleCollector.<init>(PcCollector.scala:373)
	scala.meta.internal.pc.PcSemanticTokensProvider$Collector$.<init>(PcSemanticTokensProvider.scala:61)
	scala.meta.internal.pc.PcSemanticTokensProvider.Collector$lzyINIT1(PcSemanticTokensProvider.scala:61)
	scala.meta.internal.pc.PcSemanticTokensProvider.Collector(PcSemanticTokensProvider.scala:61)
	scala.meta.internal.pc.PcSemanticTokensProvider.provide(PcSemanticTokensProvider.scala:90)
	scala.meta.internal.pc.ScalaPresentationCompiler.semanticTokens$$anonfun$1(ScalaPresentationCompiler.scala:117)
```
#### Short summary: 

java.lang.AssertionError: assertion failed: tree: StringFormat[A](null:
  (scala.concurrent.ExecutionContext.Implicits.global :
    => scala.concurrent.ExecutionContext)
), pt: <notype>