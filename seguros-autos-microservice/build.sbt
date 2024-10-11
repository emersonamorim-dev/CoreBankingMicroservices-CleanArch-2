import Dependencies._
import sbtassembly.AssemblyPlugin.autoImport._

ThisBuild / scalaVersion := "2.13.12"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "seguros-autos-microservice"
ThisBuild / organizationName := "Emerson Amorim"

// Configuração do projeto raiz
lazy val root = (project in file("."))
  .settings(
    name := "seguros-autos-microservice",
    libraryDependencies ++= Seq(
      // Biblioteca para DynamoDB
      "software.amazon.awssdk" % "dynamodb" % "2.16.79",
      
      // Biblioteca para RabbitMQ
      "com.rabbitmq" % "amqp-client" % "5.13.0",

      // Biblioteca para Redis (Jedis)
      "redis.clients" % "jedis" % "4.0.1",

      // Biblioteca para Prometheus (monitoramento)
      "io.prometheus" % "simpleclient" % "0.15.0",
      "io.prometheus" % "simpleclient_hotspot" % "0.15.0",
      "io.prometheus" % "simpleclient_common" % "0.15.0",

      // Biblioteca para Grafana (logging, opcionalmente integrada via Prometheus)
      "org.slf4j" % "slf4j-simple" % "1.7.32",

      // Biblioteca para testes (MUnit)
      "org.scalameta" %% "munit" % "0.7.29" % Test,

      // Akka HTTP
      "com.typesafe.akka" %% "akka-http" % "10.2.6",
      "com.typesafe.akka" %% "akka-stream" % "2.6.18",
      "com.typesafe.akka" %% "akka-actor" % "2.6.18",
      "com.typesafe.akka" %% "akka-slf4j" % "2.6.18",
      
      // Spray JSON e integração com Akka HTTP
      "com.typesafe.akka" %% "akka-http-spray-json" % "10.2.6",
      "io.spray" %% "spray-json" % "1.3.6"
    ),
    // Configurações para resolver conflitos ao gerar o JAR com sbt-assembly
    assemblyMergeStrategy in assembly := {
      case PathList("module-info.class") => MergeStrategy.first
      case PathList("META-INF", xs @ _*) =>
        xs.map(_.toLowerCase) match {
          case "manifest.mf" :: Nil => MergeStrategy.discard
          case "index.list" :: Nil => MergeStrategy.discard
          case "dependencies" :: Nil => MergeStrategy.discard
          case _ => MergeStrategy.first
        }
      case PathList("META-INF", "services", xs @ _*) => MergeStrategy.filterDistinctLines
      case PathList("reference.conf") => MergeStrategy.concat
      case _ => MergeStrategy.first
    }
  )
  .enablePlugins(sbtassembly.AssemblyPlugin)
