### API Gateway - Core Digital Banking Microservice - NodeJS 🚀 🔄 🌐

Codificação em Javascrip Vanila com Node de API Gateway atua como um ponto de entrada único para diversas requisições, roteando-as para diferentes microserviços de backend. Ela foi projetada para ser modular, escalável e eficiente, integrando funcionalidades essenciais, como autenticação, cache com Redis e balanceamento de carga. O projeto segue boas práticas de Clean Architecture, garantindo uma clara separação de responsabilidades.

#### Funcionalidades

##### 1. **Roteamento de Requisições**
   A API Gateway roteia as requisições dos clientes para os microserviços apropriados no backend. As rotas são definidas no arquivo `Routes.js` dentro da camada `interfaces`. Cada rota é mapeada para um controlador específico, responsável por orquestrar a lógica da aplicação.

##### 2. **Middlewares**
   A camada `application/Middlewares.js` contém middlewares que são executados antes e/ou depois das requisições serem processadas pelos controladores. Algumas das funcionalidades oferecidas pelos middlewares são:
   - **Autenticação e Autorização**: Verificação de tokens JWT e permissões de usuários para garantir o acesso seguro aos recursos.
   - **Rate Limiting**: Limita o número de requisições que um cliente pode fazer em um determinado período, prevenindo ataques DDoS.
   - **Logging**: Registra detalhes das requisições para fins de auditoria e depuração.

##### 3. **Cache com Redis**
   A API Gateway utiliza o Redis como cache para armazenar dados frequentemente acessados e temporários, como tokens de sessão ou resultados de requisições, para melhorar o desempenho e reduzir a carga sobre os microserviços. A implementação do cliente Redis pode ser encontrada em `infrastructure/cache/RedisClient.js`.

##### 4. **Configuração Flexível**
   Todas as configurações críticas, como URLs de serviços externos, chaves de API e credenciais, estão centralizadas no arquivo `infrastructure/Config.js`, permitindo que a API Gateway seja facilmente configurável em diferentes ambientes (desenvolvimento, teste e produção). As variáveis de ambiente sensíveis são gerenciadas no arquivo `.env`.

##### 5. **Testes Automatizados**
   Testes unitários são implementados para garantir a integridade da API Gateway. O arquivo `tests/Controller.test.js` contém os testes que validam o comportamento dos controladores, garantindo que as rotas e suas funcionalidades funcionem conforme esperado.

##### 6. **Integração com Docker**
   O projeto inclui um `Dockerfile` e um `docker-compose.yml`, que permitem a criação de contêineres Docker para a aplicação e seus serviços de suporte, como o Redis. O uso do Docker facilita o deployment em diferentes ambientes de forma consistente e eficiente.

##### 7. **Modularidade e Escalabilidade**
   A arquitetura do projeto é modular, dividida em várias camadas, como `application`, `domain`, `infrastructure` e `interfaces`. Cada camada tem sua responsabilidade específica, facilitando a escalabilidade e a manutenção do código:
   - **Application**: Contém middlewares e regras de aplicação.
   - **Domain**: Define as entidades e a lógica de negócios.
   - **Infrastructure**: Implementa integrações com sistemas externos, como Redis.
   - **Interfaces**: Gerencia as rotas e controladores da API Gateway.


##### Pré-requisitos
- Node.js: Versão 14.x ou superior
- Docker: Para execução dos serviços em contêineres
- Redis: Para cache
- WSL 2
- Ubuntu 24.04

##### Instalação
- Clone o repositório:

```
git clone https://github.com/seu-usuario/api-gateway.git
```

```
cd api-gateway
```

- Instale as dependências:
```
npm install
```
```
npm install axios@^1.7.7 dotenv@^16.0.0 express@^4.21.1 redis@^4.0.0
```

Subir aplicação via Nodemon:
```
node server.js
```
- Ou você pode subir aplicação via Docker com o comando abaixo:

### Aplicação está toda configurada para subir Via Docker Desktop no Windows dentro do WSL2 com Ubuntu 24.04

#### Configure seu usuário do WSL2 ou Ubuntu no docker-compose.yml em:

```
build: /home/seu-usuario/corebankingmicroservices-cleanarch/api-gateway
```


#### Comando para buildar Imagem:

``` 
docker build -t api-gateway:latest .
``` 


#### Subir Aplicação via Docker

``` 
docker-compose up --build

```


- Fazer requisição Post via Postman:

- http://localhost:7000/forward-request

```
{
  "name": "CartoesService",
  "url": "http://localhost:8081",
  "status": "active"
}
```

##### Testes
Para executar os testes, utilize o comando:

```
npm test
```



#### Conclusão
Esse projeto de API Gateway é uma solução robusta e escalável para gerenciar o fluxo de requisições entre os clientes e os diversos microserviços no backend. Ao centralizar a autenticação, o roteamento, o cache e o monitoramento das requisições, esta API Gateway não apenas melhora a eficiência e segurança da comunicação entre os serviços, mas também facilita a manutenção e o desenvolvimento futuro da aplicação.

A implementação modular e bem organizada permite uma fácil expansão da API Gateway para novos microserviços ou funcionalidades, sem comprometer a estabilidade ou a performance da aplicação. Com a utilização de ferramentas como Redis para cache, Docker para orquestração de contêineres e testes automatizados, este projeto segue boas práticas de desenvolvimento, garantindo alta performance, segurança e confiabilidade.

Essa solução é essencial para aplicações distribuídas que requerem controle centralizado e otimização de recursos, sendo um componente fundamental para arquiteturas de microsserviços modernas.

### Desenvolvido por:
Emerson Amorim [@emerson-amorim-dev](https://www.linkedin.com/in/emerson-amorim-dev/)

