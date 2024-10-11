### Message Broker - Node Javascript Vanila 🚀 🔄 🌐

Codificação do componente message-broker em Node Javascript Vanila desta aplicação é responsável por gerenciar o sistema de filas de mensagens e a comunicação assíncrona entre diferentes microsserviços. Esse design é particularmente útil em sistemas distribuídos, onde vários serviços precisam trocar informações ou executar tarefas sem dependerem diretamente uns dos outros. O message broker garante a entrega confiável e eficiente de mensagens, utilizando filas para diferentes tipos de eventos e operações.


##### Manipulação de Mensagens
- Filas
O message-broker utiliza um sistema baseado em filas para gerenciar a comunicação entre microsserviços. Cada fila é responsável por um domínio específico de funcionalidades, como processamento de cartões, pagamentos e transferências.

Fila de Cartões (cardsQueue.js): Esta fila gerencia o ciclo de vida dos eventos relacionados a cartões, como emissão de novos cartões, atualização de informações e desativação de cartões. Qualquer serviço que precise operar com cartões interage com esta fila.

Fila de Pagamentos (paymentQueue.js): A fila de pagamentos é focada em lidar com eventos relacionados a pagamentos. Ela garante que todos os pedidos de pagamento sejam processados de maneira assíncrona e confiável.

Fila de Transferências (transferQueue.js): Esta fila lida com o envio e recebimento de mensagens necessárias para transferências de fundos entre contas, garantindo o correto processamento de diferentes tipos de transferências.

- Produtor e Consumidor
Produtor (producer.js): O produtor é responsável por enviar mensagens para a fila apropriada. Ele é utilizado por microsserviços para publicar eventos que serão processados por consumidores.

Consumidor (consumer.js): O consumidor escuta mensagens em filas específicas e as processa. Por exemplo, quando um novo pagamento é recebido na paymentQueue, o consumidor lida com a lógica de processamento do pagamento de forma assíncrona.

#### Lógica do Broker
O arquivo broker.js contém a lógica principal do message broker. Ele gerencia a conexão com o broker (como o RabbitMQ), garante que as filas sejam criadas e coordena a produção e o consumo de mensagens.

#### Configuração
O arquivo config.js armazena as configurações, como strings de conexão, detalhes do host e definições das filas. Ele abstrai a configuração, facilitando a gestão de diferentes ambientes (ex.: desenvolvimento, homologação, produção).

#### Como Executar o Message Broker
Configuração: Certifique-se de que todas as variáveis de ambiente estejam definidas corretamente no arquivo .env. As configurações necessárias incluem o host do broker, nomes das filas e outros detalhes relevantes.

Docker: O serviço pode ser executado usando o Docker. Use o arquivo docker-compose.yml para iniciar o message broker e quaisquer dependências.

### Aplicação está toda configurada para subir Via Docker Desktop no Windows dentro do WSL2 com Ubuntu 24.04


#### Comando para buildar Imagem:

``` 
docker build -t message-broker:latest .
``` 


#### Subir Aplicação via Docker

``` 
docker-compose up --build

```

Iniciar o Serviço: Quando o broker estiver ativo, você pode iniciar o serviço executando:

```
node server.js
```

#### Rodar os Testes
Testes: Os testes unitários podem ser executados usando o framework jest, conforme definido no arquivo jest.config.js. Execute os testes com:

Rode esse comando no WSL2 com Ubuntu:
```
sudo npm install -g jest
```

Para testar o teste rode o comando abaixo:

```
jest --config jest.config.js
```


#### Conclusão
O componente message-broker desempenha um papel crucial ao garantir a comunicação assíncrona entre diferentes serviços em uma arquitetura de microsserviços. Ao desacoplar os serviços por meio de filas de mensagens, ele permite uma arquitetura mais escalável e resiliente. Cada fila é responsável por domínios específicos, garantindo que as mensagens sejam processadas de forma eficiente e que os serviços possam operar de maneira independente. A implementação garante uma configuração adequada, tratamento de mensagens e interação com o RabbitMQ (ou outro broker similar).

### Desenvolvido por:
Emerson Amorim [@emerson-amorim-dev](https://www.linkedin.com/in/emerson-amorim-dev/)