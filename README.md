# Messenger 

## Sobre o Projeto

O Messenger é um sistema composto por dois microsserviços que se comunicam entre si via RabbitMQ. O fluxo é simples: quando um usuário é cadastrado no **user-service**, uma mensagem é publicada automaticamente na fila do RabbitMQ, e o **sender-service** consome essa mensagem e envia um email de boas-vindas para o usuário cadastrado.

## Arquitetura

```
[Cliente] 
    │
    ▼
[user-service] ──envia dados──▶ [RabbitMQ] ──consome e envia email──▶ [sender-service]
    │                                                               │
    ▼                                                               ▼
[user-db (PostgreSQL)]                                    [sender-db (PostgreSQL)]
```

---

## Tecnologias Utilizadas

- **Java 21** com **Spring Boot 4**
- **RabbitMQ** — mensageria entre os microsserviços
- **PostgreSQL** — banco de dados de cada serviço
- **Docker** — containerização dos serviços
- **Azure Container Apps** — ambiente de produção na nuvem
- **GitHub Actions** — pipeline de CI/CD
- **SonarCloud** — análise de qualidade de código
- **Application Insights** — monitoramento e observabilidade

## Como Rodar Localmente

### Pré-requisitos

- Docker e Docker Compose instalados
- Java 21
- Maven

### Passo a passo

1. Clone o repositório:
```bash
git clone https://github.com/guilhermes14/messenger.git
cd messenger
```

2. Crie o arquivo `.env` na raiz do projeto com as variáveis de ambiente(.env.example)

3. Suba os containers:
```bash
docker-compose up --build
```

4. Os serviços estarão disponíveis em:
   - **user-service** → `http://localhost:8081`
   - **sender-service** → `http://localhost:8082`

## Endpoints

### user-service — `http://localhost:8081`

#### Cadastrar usuário
```
POST /users
Content-Type: application/json

{
    "name": "João Silva",
    "email": "joao@email.com"
}
```

Após o cadastro, o user-service publica automaticamente uma mensagem no RabbitMQ com os dados do usuário, e o sender-service consome essa mensagem e envia um email de boas-vindas.

## Fluxo de Comunicação

1. O cliente faz um `POST /users` com `name` e `email`
2. O **user-service** salva o usuário no banco e publica uma mensagem na fila `default.email` do RabbitMQ
3. O **sender-service** escuta a fila `default.email` via `@RabbitListener`
4. O **sender-service** envia um email de boas-vindas para o email cadastrado
5. O status do envio (`SENT` ou `ERROR`) é salvo no banco do sender-service


## Monitoramento

- O projeto utiliza SonarCloud para análise contínua de qualidade:

- O projeto utiliza Azure Application Insights para monitoramento e observabilidade:

- Live Metrics para métricas em tempo real dos serviços
  
- Logs estruturados capturados automaticamente
  
- Trace para rastreamento de todas as requisições

- Alertas configurados para notificar em caso de falhas ou tempo de resposta elevado

## Ambiente de Produção

O projeto está publicado no Azure Container Apps:

- **user-service** 
- **sender-service** → interno, consome mensagens do RabbitMQ
- **rabbitmq-service** → interno, gerencia a fila de mensagens
