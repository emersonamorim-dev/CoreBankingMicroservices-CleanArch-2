#### Empréstimo Microservice - Python com FastAPI 🚀 🔄 🌐

Codificação em Python com FastAPI para aplicação Empréstimo Mmicroservice, um microserviço projetado para gerenciar empréstimos, incluindo cálculos de parcelas com base em taxas de juros e impostos aplicáveis no Brasil. O microserviço é desenvolvido com Python e utiliza uma arquitetura moderna e escalável para atender a necessidades empresariais. Ele faz uso de tecnologias como MongoDB para persistência de dados, RabbitMQ para gerenciamento de mensagens assíncronas, Redis para cache, e monitoração eficiente com Prometheus e Grafana.

Este projeto segue os padrões de Arquitetura Limpa (Clean Architecture), garantindo que o código seja modular, fácil de manter e extensível para futuras melhorias.


#### Principais Tecnologias Utilizadas

- Python: Linguagem principal utilizada para o desenvolvimento do microserviço, conhecida por sua simplicidade e poder em projetos empresariais.
- MongoDB: Banco de dados NoSQL para armazenamento dos dados relacionados aos empréstimos, como informações dos clientes, condições de empréstimos e registros de pagamento.
- RabbitMQ: Utilizado como sistema de mensagens para garantir a comunicação entre serviços de forma assíncrona, permitindo maior escalabilidade e desacoplamento.
- Redis: Solução de cache para armazenar temporariamente dados e reduzir a latência de acesso ao MongoDB, além de otimizar o desempenho da aplicação.
- Prometheus: Ferramenta para monitorar métricas da aplicação e garantir o funcionamento adequado, monitorando a performance em tempo real.
- Grafana: Dashboard integrado para visualização das métricas coletadas pelo Prometheus, facilitando o monitoramento da saúde e desempenho do sistema.

#### Diagrama da Aplicação

![](https://raw.githubusercontent.com/emersonamorim-dev/CoreBankingMicroservices-CleanArch-2/refs/heads/main/Diagrama/Diagrama-Arquitetura-Empr%C3%A9stimo-Microservices.png)


#### Clone o repositório do microserviço:
```
git clone https://github.com/emersonamorim-dev/CoreBankingMicroservices-CleanArch-2.git
```
```
cd CoreBankingMicroservices-CleanArch-2
```

```
cd emprestimo-microservice
```

```
python3 -m venv env
```

```
source env/bin/activate
```

```
pip install -r requirements.txt
```

### Aplicação está toda configurada para subir Via Docker Desktop no Windows dentro do WSL2 com Ubuntu 24.04

#### Configure seu usuário do WSL2 ou Ubuntu no docker-compose.yml em:

```
build: /home/seu-usuario/corebankingmicroservices-cleanarch-2/emprestimo-microservice/
```

### Remover o Volume do MongoDB para conseguir rodar a imagem:
```
sudo rm -rf /home/seu-usuario/corebankingmicroservices-cleanarch-2/emprestimo-microservice/databases/mongo/data/diagnostic.data
```

#### Comando para buildar Imagem:

``` 
docker build -t emprestimo-microservice:latest .
``` 


#### Subir Aplicação via Docker

``` 
docker-compose up --build

```

#### Realizar a Requisição via Postman

- Requisição Post

```
http://localhost:8099/api/emprestimo

```
#### Corpo Json da Requisição

```
{
  "valor_principal": 299000,
  "taxa_juros": 7.5,
  "prazo": 18
}



```

#### Retorno da Requisição

```
{
    "resultado": {
        "valor_principal": 299000,
        "taxa_juros": 7.5,
        "prazo": 18,
        "valor_total": 1099065.4225821479,
        "id": "6aa67d08-e163-4132-a91c-d30ce27c978c"
    }
}
```


#### Como Funciona o Código

1. Camada de Aplicação
- Services: Contém a lógica de aplicação para processar empréstimos, cálculos de taxas e interações com serviços externos.
- Use Cases: Casos de uso encapsulam a lógica de negócios, como criação de um empréstimo, cálculo de parcelas e envio de notificações.

2. Camada de Domínio
- Entities: Definem as entidades do domínio, como Emprestimo, Cliente, entre outras. Cada entidade representa um conceito central do sistema.
- Events: Eventos de domínio que são disparados quando certas ações acontecem, como a criação de um novo empréstimo ou a aprovação de um pagamento.
- Repositories: Interfaces que definem como o sistema interage com o MongoDB. Esta camada abstrai a persistência de dados, garantindo que o domínio não dependa diretamente da infraestrutura.

3. Camada de Infraestrutura
- Cache: Utiliza o Redis para armazenar dados em cache, evitando o acesso frequente ao banco de dados e otimizando o desempenho.
- Config: Gerencia as configurações do sistema, como variáveis de ambiente e dados de conexão com serviços externos.
- DB: Gerencia a conexão com o banco de dados MongoDB, responsável por armazenar os dados dos empréstimos e clientes.
- Logging: Integração com Grafana para garantir que os logs e métricas sejam corretamente exportados e visualizados.
- Messaging: Utiliza RabbitMQ para o envio e recebimento de mensagens relacionadas ao processo de empréstimo, como aprovação de crédito ou notificação de pagamento.
- Monitoring: Implementa métricas para serem exportadas ao Prometheus, que monitora o desempenho da aplicação em tempo real.

4. Interfaces
- Controllers: são responsáveis por processar as requisições recebidas e delegar as ações necessárias para outras camadas, principalmente a camada de Application (que contém os casos de uso do sistema).
- Event Handlers: tratam eventos que ocorrem dentro ou fora do sistema. Esses eventos podem ser desencadeados por mudanças de estado de outros sistemas, por mensagens recebidas em uma fila, ou por ações específicas que ocorrem internamente.
- http: routes define o roteamento HTTP. Ele mapeia URLs para os métodos apropriados dos Controllers, que são os responsáveis por processar essas requisições e delegá-las à camada de aplicação, onde a lógica de negócios reside.

main.py: O ponto de entrada da aplicação. Aqui é onde o servidor inicia, as rotas HTTP são configuradas, e os serviços de background são inicializados.

##### Funcionalidades Principais:
Cálculo de Empréstimos: Processa os dados de clientes, taxas de juros e valores para gerar um plano de pagamento.
Cache: Utiliza Redis para reduzir a carga sobre o banco de dados e melhorar o tempo de resposta.
Mensageria: Processa eventos de empréstimo através do RabbitMQ, permitindo comunicação eficiente entre diferentes partes do sistema.
Monitoramento: A aplicação exporta métricas para o Prometheus, e essas métricas podem ser visualizadas em tempo real no Grafana.



pip install -r requirements.txt


docker build -t emprestimo-microservice:latest .

sudo rm -rf /home/emersondev/corebankingmicroservices-cleanarch-2/emprestimo-microservice/databases/mongo/data/diagnostic.data




#### Conclusão
O Empréstimo Microservice oferece uma solução completa e robusta para a gestão de empréstimos, sendo flexível e altamente escalável para atender às necessidades de empresas do setor financeiro. A utilização de tecnologias modernas como MongoDB, RabbitMQ, Redis, Prometheus e Grafana garante um desempenho excepcional, monitoramento eficiente e uma arquitetura de software limpa e profissional. Essa arquitetura, baseada em Clean Architecture, separa de forma clara as responsabilidades, tornando o código fácil de manter e evoluir conforme novas funcionalidades são adicionadas.

Essa solução foi projetada para ser altamente confiável, minimizando falhas e garantindo que a experiência do cliente final seja fluida e eficiente. É ideal para instituições financeiras que desejam melhorar a gestão de empréstimos, desde a análise de crédito até o processamento de pagamentos.

### Desenvolvido por:
Emerson Amorim [@emerson-amorim-dev](https://www.linkedin.com/in/emerson-amorim-dev/)
