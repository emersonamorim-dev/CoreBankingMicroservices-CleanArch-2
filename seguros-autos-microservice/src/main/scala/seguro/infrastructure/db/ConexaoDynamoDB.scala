package seguro.infrastructure.db

import software.amazon.awssdk.auth.credentials.{AwsBasicCredentials, StaticCredentialsProvider}
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.{ListTablesRequest, ListTablesResponse}
import scala.util.{Failure, Success, Try}

class ConexaoDynamoDB(
    accessKeyId: String = "fakeAccessKey",
    secretAccessKey: String = "fakeSecretKey"
) {
  private val dynamoDbEndpoint = "http://dynamodb-local:8000" // Utilize o nome do serviço aqui

  private val client = DynamoDbClient.builder()
    .region(Region.US_EAST_1)
    .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKeyId, secretAccessKey)))
    .endpointOverride(new java.net.URI(dynamoDbEndpoint))
    .build()

  def testarConexao(): Unit = {
    println("Testando conexão com DynamoDB...")
    Try(client.listTables(ListTablesRequest.builder().build())) match {
      case Success(response: ListTablesResponse) => 
        println(s"Conectado ao DynamoDB. Tabelas disponíveis: ${response.tableNames()}")
      case Failure(exception) => 
        println(s"Erro ao conectar ao DynamoDB: ${exception.getMessage}")
        throw exception
    }
  }

  def fecharConexao(): Unit = {
    client.close()
    println("Conexão com DynamoDB fechada.")
  }
}
