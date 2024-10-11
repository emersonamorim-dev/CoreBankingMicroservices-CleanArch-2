### Microservice de Pix - Rust Application 🚀 🔄 🌐

Codificação de aplicação para Pix Microservice, um projeto desenvolvido em Rust com o objetivo de implementar um sistema de microserviços para processamento de pagamentos via Pix. Este sistema faz uso de tecnologias avançadas como DynamoDB, RabbitMQ, e Redis, com monitoramento provido por Prometheus e Grafana. O projeto exemplifica a arquitetura de eventos, com filas gerenciadas pelo RabbitMQ para garantir alta performance e escalabilidade no processamento de pagamentos, enquanto o armazenamento e recuperação de dados são feitos via DynamoDB.

O microserviço foi desenvolvido com uma arquitetura orientada a eventos e utiliza mensagens assíncronas para garantir a integridade e escalabilidade do sistema. Este documento descreve o funcionamento do sistema, as tecnologias utilizadas, e o processo de configuração e execução da aplicação.

#### Tecnologias Utilizadas

- Rust: Linguagem de programação principal, escolhida pela sua performance, segurança de memória e capacidade de lidar com concorrência de forma eficiente.

- Actix Web: Framework web utilizado para construir as APIs HTTP, aproveitando seu suporte a operações assíncronas e alta performance.

- DynamoDB (via SDK da AWS): Banco de dados NoSQL utilizado para armazenar os pagamentos Pix. Utiliza um endpoint local configurado com credenciais fictícias para desenvolvimento.

- RabbitMQ: Broker de mensagens responsável por gerenciar filas e garantir a entrega de eventos de pagamento de forma assíncrona.

- Redis: Utilizado como cache para otimizar a recuperação de dados.

- Prometheus e Grafana: Ferramentas de monitoramento e visualização de métricas. O Prometheus coleta dados sobre o desempenho e o uso de recursos da aplicação, e o Grafana os exibe de forma amigável.

- Docker e Docker Compose: O ambiente de desenvolvimento e produção é containerizado para facilitar a configuração e a execução. O docker-compose.yml contém a configuração dos serviços, incluindo RabbitMQ, Redis e DynamoDB local.


#### Explicação do Código

O projeto segue uma arquitetura organizada em camadas, promovendo separação de responsabilidades e facilitando a manutenção. Aqui está uma visão geral das principais camadas do sistema:

- domain: Contém as entidades e interfaces que definem a lógica de domínio, como PixPayment e o PixRepository.
- application: Implementa os casos de uso da aplicação, como o serviço de processamento de pagamentos (PaymentService) e geração de QR Codes.
- infrastructure: Gerencia as integrações com serviços externos, como o cliente DynamoDB, RabbitMQ, Redis e Prometheus.
-interfaces: Responsável pela comunicação externa, implementando as APIs HTTP com Actix e os manipuladores de eventos via RabbitMQ.


#### Funcionamento
- PixPayment: É a entidade principal que representa um pagamento via Pix. Ela possui campos como id, amount, receiver_key, payer_key, e timestamp.

- DynamoDbConnection: Essa estrutura inicializa a conexão com o DynamoDB, configurando um endpoint local com credenciais fictícias para ambiente de desenvolvimento.

- RabbitMQClient: Gerencia a comunicação com o RabbitMQ, declarando filas e publicando mensagens de pagamento.

- PaymentService: O serviço responsável por orquestrar o processo de salvar o pagamento no banco de dados e notificar outras partes do sistema sobre o evento de pagamento via filas RabbitMQ.

- PrometheusMetrics: Implementa o monitoramento da aplicação, exportando métricas que podem ser consumidas pelo Prometheus e visualizadas no Grafana.


#### Diagrama da Aplicação

![](https://raw.githubusercontent.com/emersonamorim-dev/CoreBankingMicroservices-CleanArch-2/refs/heads/main/Diagrama/Diagrama-Arquitetura-Pix-Microservices.png)



##### Comandos para rodar aplicação

```
cargo clean
```

```
cargo update
```

```
cargo build
```

```
cargo run
```


##### Exemplo de configuração no arquivo .env

```
SERVER_ADDRESS=0.0.0.0:8096
RABBITMQ_URI=amqp://guest:guest@rabbitmq:5672/
REDIS_URI=redis://redis-cache:6379/
DYNAMODB_ENDPOINT=http://localhost:8000

```

### Aplicação está toda configurada para subir Via Docker Desktop no Windows dentro do WSL2 com Ubuntu 24.04

#### Configure seu usuário do WSL2 ou Ubuntu no docker-compose.yml em:

```
build: /home/seu-usuario/corebankingmicroservices-cleanarch-2/pix-microservice/
```


#### Comando para buildar Imagem:

``` 
docker build -t pix-microservice:latest .
``` 

#### Subir Aplicação via Docker

``` 
docker-compose up --build

```


#### Principais Endpoints
[POST]: Recebe um pagamento Pix, salva no banco de dados e publica um evento no RabbitMQ.

http://localhost:8096/pix/payment

```
{
  "id": "9a569126-2c2e-8153-b50b-e71913aa798e",
  "amount": 19200.50,
  "receiver_key": "emerson_tecno@example.com",
  "payer_key": "emerson_luiz99@example.com",
  "timestamp": "2024-10-01T12:34:56Z"
}


```

#### Tabelas DynamoDB
A tabela principal do sistema é a PixPayments, onde os dados dos pagamentos são armazenados, com id como chave primária.



#### Conclusão
Este projeto é um exemplo robusto de como implementar um microserviço em Rust utilizando uma arquitetura orientada a eventos. Ao integrar tecnologias modernas como RabbitMQ, DynamoDB e Redis, garantimos um sistema escalável e resiliente, adequado para aplicações de alta performance, como um sistema de pagamentos via Pix. Com o monitoramento via Prometheus e Grafana, é possível garantir a saúde da aplicação em produção, enquanto Docker e Docker Compose simplificam a configuração e implantação do sistema.

Sinta-se à vontade para explorar o código e contribuir com melhorias. Este projeto foi construído com foco em escalabilidade, performance e segurança, aproveitando ao máximo os recursos que a linguagem Rust e sua vasta ecossistema têm a oferecer.

### Desenvolvido por:
Emerson Amorim [@emerson-amorim-dev](https://www.linkedin.com/in/emerson-amorim-dev/)
