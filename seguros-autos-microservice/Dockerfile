FROM openjdk:17-slim

# Define o diretório de trabalho no container
WORKDIR /app

# Copia o JAR da aplicação para o diretório de trabalho
COPY ./target/scala-2.13/seguros-autos-microservice-assembly-0.1.0-SNAPSHOT.jar ./seguros-autos-microservice-assembly-0.1.0-SNAPSHOT.jar

# Expõe a porta 9000
EXPOSE 8020

# Define o comando de entrada que será executado ao iniciar o container
ENTRYPOINT ["java", "-jar", "seguros-autos-microservice-assembly-0.1.0-SNAPSHOT.jar"]
