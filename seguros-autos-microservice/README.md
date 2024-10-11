
### Seguros Autos Microservice - Scala com DynamoDB🚀 🔄 🌐

Codificação em Scala de aplicaçao de Seguros-Autos Microservice, com uso de DynamoDB, RabbitMQ, Redis, Prometheus e Grafana para um microserviço especializado no cálculo de seguros de automóveis, levando em consideração os impostos e as taxas de juros praticadas no Brasil. Este sistema foi desenvolvido para fornecer uma solução robusta, escalável e altamente eficiente para empresas que precisam calcular seguros de veículos de maneira automatizada e com suporte a pagamentos via PIX. Utilizando uma Arquitetura Limpa (Clean Architecture), a aplicação é organizada em camadas que garantem alta manutenibilidade, flexibilidade, e separação de responsabilidades.

Este projeto é projetado para funcionar em um ambiente empresarial de ponta, integrando tecnologias como DynamoDB, RabbitMQ, Redis, Prometheus e Grafana, proporcionando uma solução de monitoramento, armazenamento, cache e fila de mensagens com padrões altamente profissionais e escaláveis.


#### Principais Ferramentas e Tecnologias
- Scala: Linguagem principal utilizada para desenvolvimento, oferecendo tipagem forte e suporte a programação funcional e orientada a objetos.
- DynamoDB: Utilizado como banco de dados NoSQL para armazenar informações dos seguros e das transações.
- RabbitMQ: Broker de mensagens para comunicação assíncrona e processamento de eventos entre serviços.
- Redis: Implementado como solução de cache para acelerar o acesso a dados e reduzir a carga em DynamoDB.
- Prometheus: Ferramenta de monitoramento para métricas de performance da aplicação.
- Grafana: Dashboard integrado para visualização das métricas exportadas por Prometheus.
- Docker e Docker Compose: Para containerização do microserviço e orquestração dos serviços auxiliares (DynamoDB, Redis, RabbitMQ, etc.).

Arquitetura Limpa (Clean Architecture): Estrutura que separa as camadas de domínio, aplicação e infraestrutura, tornando o código mais organizado, testável e fácil de manter.


##### Componentes

##### Camada de Aplicação

Contém os casos de uso e os serviços que encapsulam a lógica de negócio e os serviços externos, como SeguroService para cálculo de seguros e PaymentService para lidar com pagamentos.

##### Camada de Domínio

Define as entidades principais do sistema como Seguro, e armazena os eventos relacionados ao seguro. Nesta camada, você também encontra o SeguroRepository que lida com as interações com o banco de dados DynamoDB.

##### Camada de Infraestrutura

Responsável pela comunicação com serviços externos como DynamoDB, RabbitMQ, Redis e Prometheus. Aqui encontramos componentes como ConexaoDynamoDB, RabbitMQClient, RedisClient, PrometheusMetrics, entre outros.

##### Camada de Interfaces

Implementa os controladores HTTP e manipuladores de eventos. Nesta camada estão as rotas HTTP que permitem a interação com a API do microserviço, assim como manipuladores de eventos para integrar com RabbitMQ.



#### Banco de Dados
O DynamoDB é utilizado para armazenar os dados dos clientes, informações sobre seguros, e registros de transações.

#### Cache
Redis é implementado para caching, garantindo respostas rápidas e evitando acesso desnecessário ao DynamoDB em consultas repetidas.

#### Mensageria
RabbitMQ é utilizado para a comunicação entre os microserviços de maneira assíncrona, garantindo que eventos relacionados a seguros e transações de pagamento sejam processados de forma eficiente.

#### Monitoramento
Prometheus é usado para monitorar métricas de performance da aplicação, como tempo de resposta das APIs e uso de memória. Grafana fornece um dashboard visual para acompanhar esses dados em tempo real.


#### Diagrama da Aplicação

![](https://raw.githubusercontent.com/emersonamorim-dev/CoreBankingMicroservices-CleanArch-2/refs/heads/main/Diagrama/Diagrama-Seguros-Autos-Microservices.png)





##### Clone o repositório do microserviço:
```
git clone https://github.com/emersonamorim-dev/CoreBankingMicroservices-CleanArch-2.git
```
```
cd CoreBankingMicroservices-CleanArch-2
```



### Aplicação está toda configurada para subir Via Docker Desktop no Windows dentro do WSL2 com Ubuntu 24.04

#### Configure seu usuário do WSL2 ou Ubuntu no docker-compose.yml em:

```
build: /home/seu-usuario/corebankingmicroservices-cleanarch/seguros-autos-microservice/
```

#### Comando para limpar aplicação e gerar o jar:

```
sbt clean
```

```
sbt update
```

```
sbt assembly
```

```
sbt clean compile assembly
```


#### Comando para buildar Imagem:

``` 
docker build -t seguros-autos-microservice:latest .
``` 


#### Subir Aplicação via Docker

``` 
docker-compose up --build

```

#### Realizar a Requisição via Postman

- Requisição Post

```
http://localhost:8020/calcular-seguro

```
#### Corpo Json da Requisição

```
{
  "segurado": {
    "nome": "Emerson Amorim",
    "cpf": "123.456.789-00",
    "email": "emerson_tecno@hotmail.com",
    "telefone": "(11) 99999-0000"
  },
  "detalhesSeguro": {
    "tipoSeguro": "Auto",
    "veiculo": {
      "marca": "Chevrolet",
      "modelo": "Tracker",
      "ano": 2021,
      "placa": "ELA-1981"
    },
    "valorSeguro": 50000,
    "valorFranquia": 2000,
    "cobertura": [
      "Roubo",
      "Colisão",
      "Danos a terceiros"
    ],
    "vigencia": "2024-01-01T00:00:00Z"
  },
  "pagamento": {
    "metodo": "PIX",
    "valor": 1500
  }
}


```

#### Retorno da Requisição

```
{
    "cliente": {
        "cpf": "123.456.789-00",
        "email": "emerson_tecno@hotmail.com",
        "nome": "Emerson Amorim",
        "telefone": "(11) 99999-0000"
    },
    "detalhesSeguro": {
        "coberturas": "Roubo, Colisão, Danos a terceiros",
        "franquia": "R$2000.00",
        "tipoSeguro": "Auto",
        "valorSeguro": "R$53500.00",
        "vigencia": "2025-10-05T10:18:43.092296861Z[Etc/UTC]"
    },
    "mensagem": "Seguro processado para o veículo Chevrolet Tracker, placa ELA-1981.",
    "metodoPagamento": "PIX"
}
```



#### Conclusão
O Seguros-Autos Microservice é uma solução poderosa e completa para o cálculo de seguros automotivos, oferecendo suporte a pagamentos digitais, monitoramento de métricas em tempo real, e uma arquitetura escalável e flexível. Utilizando uma infraestrutura moderna com DynamoDB, RabbitMQ, Redis, Prometheus, e Grafana, a aplicação é capaz de atender a necessidades empresariais de ponta, sendo ao mesmo tempo fácil de manter e evoluir, graças à implementação da Arquitetura Limpa.

Esta solução é ideal para empresas do setor de seguros que desejam automatizar processos e aumentar a eficiência operacional, oferecendo uma plataforma robusta e confiável para o cálculo de seguros automotivos.


### Desenvolvido por:
Emerson Amorim [@emerson-amorim-dev](https://www.linkedin.com/in/emerson-amorim-dev/)
