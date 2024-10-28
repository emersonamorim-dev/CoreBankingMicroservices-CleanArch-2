### Core Digital Banking Microservices 2 - Scala, Rust e Python 🚀 🔄 🌐

O Core Digital Banking Microservice 2 é a segunda parte de uma solução robusta e resiliente para bancos digitais,
 baseada em uma Arquitetura de Microserviços Poliglota. Cada microserviço é responsável por uma função crítica 
 do sistema bancário, utilizando diferentes tecnologias para atender às suas necessidades específicas, 
 garantindo escalabilidade, flexibilidade e robustez.

Esse projeto é implementado com os princípios da Clean Architecture, onde cada camada tem responsabilidades 
bem definidas, facilitando a manutenção e expansão do sistema. A arquitetura de microserviços permite uma 
comunicação desacoplada, aumentando a tolerância a falhas e a escalabilidade do sistema.


#### 1. Seguros Autos Microservice  (Scala, DynamoDB, RabbitMQ, Redis)
Seguros Autos Microservice (Scala, DynamoDB, RabbitMQ, Redis)
O Seguros Autos Microservice é responsável por um fluxo organizado e assíncrono, utilizando tecnologias como 
RabbitMQ para lidar com eventos e Redis para caching. 

#### Tecnologia:
- Scala: Linguagem principal utilizada para desenvolvimento, oferecendo tipagem forte e suporte a programação funcional e orientada a objetos.
- DynamoDB: Utilizado como banco de dados NoSQL para armazenar informações dos seguros e das transações.
- RabbitMQ: Broker de mensagens para comunicação assíncrona e processamento de eventos entre serviços.
- Redis: Implementado como solução de cache para acelerar o acesso a dados e reduzir a carga em DynamoDB.
- Prometheus: Ferramenta de monitoramento para métricas de performance da aplicação.
- Grafana: Dashboard integrado para visualização das métricas exportadas por Prometheus.
- Docker e Docker Compose: Para containerização do microserviço e orquestração dos serviços auxiliares (DynamoDB, Redis, RabbitMQ, etc.).
- Arquitetura Limpa (Clean Architecture): Estrutura que separa as camadas de domínio, aplicação e infraestrutura, tornando o código mais organizado, testável e fácil de manter.
- WSL 2 com Ubuntu 24.04
- Docker Desktop


#### 2. Microservice de Pix (Rust, DynamoDB, RabbitMQ, Redis)
O microserviço foi desenvolvido para integrar o geração de QRCode para pagamento via Pix utiliza mensagens 
assíncronas para garantir a integridade e escalabilidade do sistema. Este documento descreve o funcionamento 
do sistema, as tecnologias utilizadas, e o processo de configuração e execução da aplicação.

#### Tecnologia:
- Rust: Linguagem de programação principal, escolhida pela sua performance, segurança de memória e capacidade 
de lidar com concorrência de forma eficiente.

- Actix Web: Framework web utilizado para construir as APIs HTTP, aproveitando seu suporte a operações assíncronas e alta performance.

- DynamoDB (via SDK da AWS): Banco de dados NoSQL utilizado para armazenar os pagamentos Pix. Utiliza um endpoint local configurado com credenciais fictícias para desenvolvimento.

- RabbitMQ: Broker de mensagens responsável por gerenciar filas e garantir a entrega de eventos de pagamento de forma assíncrona.

- Redis: Utilizado como cache para otimizar a recuperação de dados.

- Prometheus e Grafana: Ferramentas de monitoramento e visualização de métricas. O Prometheus coleta dados sobre o desempenho e o uso de recursos da aplicação, e o Grafana os exibe de forma amigável.

- Docker e Docker Compose: O ambiente de desenvolvimento e produção é containerizado para facilitar a configuração e a execução. O docker-compose.yml contém a configuração dos serviços, incluindo RabbitMQ, Redis e DynamoDB local.

#### 3. Microservice de Empréstimo (Python, FastApi MongoDB, RabbitMQ, Redis)
Cálculo de Empréstimos: Processa os dados de clientes, taxas de juros e valores para gerar um plano de pagamento.

#### Tecnologia:
- Python: Linguagem principal utilizada para o desenvolvimento do microserviço, conhecida por sua simplicidade e poder em projetos empresariais.
- MongoDB: Banco de dados NoSQL para armazenamento dos dados relacionados aos empréstimos, como informações dos clientes, condições de empréstimos e registros de pagamento.
- RabbitMQ: Utilizado como sistema de mensagens para garantir a comunicação entre serviços de forma assíncrona, permitindo maior escalabilidade e desacoplamento.
- Redis: Solução de cache para armazenar temporariamente dados e reduzir a latência de acesso ao MongoDB, além de otimizar o desempenho da aplicação.
- Prometheus: Ferramenta para monitorar métricas da aplicação e garantir o funcionamento adequado, monitorando a performance em tempo real.
- Grafana: Dashboard integrado para visualização das métricas coletadas pelo Prometheus, facilitando o monitoramento da saúde e desempenho do sistema.


#### Arquitetura
A arquitetura de Clean Architecture assegura uma separação clara de responsabilidades, com camadas de domínio, aplicação, infraestrutura e apresentação bem definidas. Isso garante que os serviços sejam facilmente testáveis, extensíveis e modulares.

Cada microserviço é independente e utiliza seu próprio banco de dados, garantindo que as mudanças em um serviço não impactem outros serviços. A comunicação entre os microserviços é realizada através de mensagens com o RabbitMQ, garantindo que as operações sejam processadas de maneira assíncrona e resiliente.

#### Diagrama da Aplicação

![](https://raw.githubusercontent.com/emersonamorim-dev/CoreBankingMicroservices-CleanArch-2/refs/heads/main/Diagrama/Core-Digital-Banking-Microservices-2.png)


#### Mensageria e Cache
RabbitMQ: É utilizado como um message broker, permitindo que os microserviços troquem informações de forma assíncrona. Cada microserviço publica e consome mensagens de filas específicas, garantindo o processamento correto de cada funcionalidade.
Redis: Implementado em todos os microserviços como cache de dados críticos, como tokens de autenticação, status de transações e outras operações que exigem alto desempenho.
Fluxo de Comunicação
A comunicação entre os microserviços é baseada em mensageria com RabbitMQ. Quando uma operação é iniciada, como a emissão de um cartão ou processamento de um pagamento, uma mensagem é publicada em uma fila específica no RabbitMQ. O microserviço apropriado consome a mensagem e processa a operação.

Este modelo de comunicação desacoplada garante resiliência e escalabilidade, permitindo que o sistema continue funcionando mesmo se um dos serviços estiver temporariamente indisponível.

#### Funcionalidades
1. Emissão e Gerenciamento de Cartões
Emissão de novos cartões com integração a contas bancárias.
Consulta de saldo e status do cartão.
Bloqueio e desbloqueio de cartões em tempo real.
2. Processamento de Pagamentos
Processamento de pagamentos via cartões de crédito e débito.
Autorização e captura de valores de transações.
Integração com gateways de pagamento externos.
Recuperação de transações e geração de relatórios.
3. Processamento de Transferência
Com transferência de valores.
Instalação e Execução
Requisitos
Docker e Docker Compose instalados
RabbitMQ, Redis, e bancos de dados respectivos (PostgreSQL, MySQL, MongoDB)

#### Passos de Instalação
Clone o repositório do microserviço:
```
git clone https://github.com/emersonamorim-dev/CoreBankingMicroservices-CleanArch-2.git
```
```
cd CoreBankingMicroservices-CleanArch-2
```

#### Monitoramento e Métricas
O sistema utiliza o Prometheus e Grafana para monitorar métricas de performance. O Prometheus coleta métricas expostas por cada microserviço, que podem ser visualizadas e analisadas através de painéis no Grafana.

#### Configuração do Monitoramento
Configure o Prometheus e Grafana nos contêineres Docker usando o arquivo docker-compose.yml.
As métricas estarão disponíveis na porta 15692 para Prometheus, e o painel de Grafana estará acessível na porta 3000.

#### Escalabilidade
O Core Digital Banking foi desenvolvido para ser escalável horizontalmente. Usando Kubernetes e Helm, é possível orquestrar os contêineres para ambientes de produção. A escalabilidade é garantida, pois cada microserviço pode ser escalado independentemente, dependendo da carga.

#### Resiliência
A arquitetura poliglota garante que cada microserviço possa ser desenvolvido e mantido em sua própria stack tecnológica, garantindo resiliência e flexibilidade para adotar novas tecnologias conforme necessário.


#### Conclusão
O Core Digital Banking 2 Microservice é uma solução avançada e moderna para a gestão de serviços bancários digitais, construída com base em uma arquitetura poliglota e resiliente. Sua implementação com Clean Architecture assegura modularidade, fácil manutenção e alta escalabilidade, sendo capaz de se adaptar a diversas necessidades e crescimentos de mercado. Com uma comunicação eficiente baseada em RabbitMQ, o uso de Redis para cache, e a integração com múltiplos bancos de dados como DynamoDB e MongoDB, este sistema oferece uma base sólida para operações financeiras robustas e seguras.

Além disso, a capacidade de cada microserviço operar de forma independente, utilizando diferentes stacks tecnológicas, garante que o sistema possa evoluir e integrar novas funcionalidades sem impactar outros serviços. Essa abordagem poliglota permite que o Core Digital Banking seja altamente flexível, inovador e capaz de se manter à frente em um cenário bancário digital competitivo.

### Desenvolvido por:
Emerson Amorim [@emerson-amorim-dev](https://www.linkedin.com/in/emerson-amorim-dev/)
