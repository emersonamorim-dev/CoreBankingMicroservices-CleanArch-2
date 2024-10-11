### Discovery Service - NodeJS Vanila 🚀 🔄 🌐

Codificação de um Discovery Service (serviço de descoberta) em Node com Javascript Vanila utilizado em arquiteturas de microserviços para facilitar a comunicação entre diferentes serviços distribuídos. Em uma arquitetura de microserviços, serviços podem ser adicionados, removidos ou redimensionados dinamicamente, e o Discovery Service tem como principal funcionalidade registrar, gerenciar e localizar instâncias de serviços, garantindo que outros microserviços possam se comunicar entre si de maneira eficiente e sem a necessidade de configurações manuais.

Esse serviço utiliza várias tecnologias e padrões de projeto para fornecer as funcionalidades principais. A implementação que estamos analisando parece utilizar Node.js, com a arquitetura dividida em várias camadas, como application, domain, infrastructure, interfaces, e uma estrutura de testes bem definida.

#### Tecnologias Utilizadas
- Node.js: Usado para implementar a lógica de backend do Discovery Service, fornecendo uma base leve e eficiente para execução de serviços distribuídos.

- Express.js (provavelmente): Embora não seja diretamente visível na estrutura, com base na convenção de uso de controllers e services, parece que o serviço pode estar utilizando Express.js como framework HTTP para criar e expor os endpoints RESTful.

- Docker: Utilizado para containerização da aplicação, facilitando a implantação em múltiplos ambientes sem necessidade de configuração complexa.

- DynamoDB: Para salvar informações da aplicação em um Banco de dados em Cloud.

- Jest: O sistema de testes parece utilizar Jest para automatizar a validação das funcionalidades do serviço, com foco em testes unitários e de integração.

- Microservices Architecture: O Discovery Service é fundamental em arquiteturas baseadas em microserviços, permitindo a descoberta e comunicação dinâmica entre as instâncias de serviços.

### Execução Local 🚀

```
npm install
```

```
npm install mongodb
```

```
npm install axios body-parser express dotenv
```


### Aplicação está toda configurada para subir Via Docker Desktop no Windows dentro do WSL2 com Ubuntu 24.04


#### Comando para buildar Imagem:

``` 
docker build -t discovery-service:latest .
``` 


#### Subir Aplicação via Docker

``` 
docker-compose up --build

```


- Requisição Post para salvar informações no Postman:

- http://localhost:8088/discovery/services

```
{
  "name": "emprestimos",
  "host": "192.168.0.10",
  "port": 8099,
  "status": "active",
  "region": "us-west-2"
}
```



#### Funcionalidades Implementadas

1. Camada de Application
DiscoveryClient.js: Provavelmente, esse arquivo contém a lógica para os clientes que se conectam ao serviço de descoberta, permitindo que microserviços se registrem e se descubram entre si. Essa interface pode permitir que os serviços façam solicitações para se registrar ou buscar outros serviços registrados.

2. Camada de Domain
ServiceRegistry.js: Essa classe deve ser responsável por gerenciar o registro dos serviços. O ServiceRegistry armazena as instâncias de serviços que estão ativamente registradas, associando serviços a endereços IP, portas e informações sobre a saúde dessas instâncias. Essa camada geralmente lida com o domínio central da descoberta de serviços, garantindo que os registros sejam geridos de forma eficiente.

3. Camada de Infrastructure
Config.js: Normalmente, este arquivo armazena as configurações globais da aplicação, como variáveis de ambiente e parâmetros de configuração relacionados ao serviço de descoberta, como URLs de serviços, portas de comunicação, e possivelmente a configuração do banco de dados ou cache para armazenar serviços registrados.

4. Camada de Interfaces
DiscoveryController.js: Esse arquivo deve expor as rotas HTTP ou endpoints para interação com o Discovery Service. Ele deve conter os métodos REST (como POST, GET, DELETE) para registrar, buscar ou remover serviços. Esta camada faz a ponte entre as requisições externas e a lógica de aplicação, recebendo as requisições de serviços e as encaminhando para o ServiceRegistry.

5. Camada de Testes
DiscoveryController.test.js e DiscoveryService.test.js: Esses arquivos são responsáveis pelos testes automatizados do sistema. Geralmente, são implementados para garantir a qualidade e a funcionalidade do sistema, testando as interações com o controlador e a lógica do serviço de descoberta. Devem simular requisições HTTP para garantir que o Discovery Service esteja funcionando corretamente sob diferentes cenários.


#### Conclusão
Este Discovery Service fornece uma base sólida para gerenciar a descoberta e o registro de serviços em uma arquitetura distribuída de microserviços. Ele é essencial para permitir que serviços dinâmicos se registrem e se comuniquem sem a necessidade de configurações manuais ou acoplamentos rígidos. Com a separação clara de responsabilidades entre as camadas de aplicação, domínio e infraestrutura, além de testes automatizados, a aplicação é altamente modular, escalável e preparada para ambientes produtivos.

A adição do Docker e da estrutura de testes contribui significativamente para garantir a consistência e a confiabilidade do sistema em diferentes ambientes, além de permitir que a solução seja facilmente escalável, utilizando tecnologias amplamente adotadas no ecossistema de microserviços.

### Desenvolvido por:
Emerson Amorim [@emerson-amorim-dev](https://www.linkedin.com/in/emerson-amorim-dev/)