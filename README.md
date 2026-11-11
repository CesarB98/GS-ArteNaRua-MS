# ArteNaRua — Global Solution 2026-02 (Microservice and Web Engineering)

## Integrantes

| Nome | RM |
|------|----|
| Caio Leria | 557833 |
| Cesar Brasil | 556236 |

## Problema

A arte urbana (grafites, murais, esculturas) e o patrimônio cultural das cidades passam despercebidos: as obras ficam
sem contexto (quem fez, quando, por quê) e sem acessibilidade para pessoas com deficiência visual.

## Solução

O **ArteNaRua** cataloga obras de arte urbana com ficha completa (artista, história, localização e **audiodescrição**).
A comunidade sugere obras novas e um moderador aprova ou rejeita. Esta entrega é o backend em microsserviços:

| Componente | Porta | Papel |
|------------|-------|-------|
| `eureka-server` | 8761 | Registro e descoberta de serviços |
| `api-gateway` | 8082 | Porta de entrada única, rotas `lb://` com Load Balancer |
| `ms-obras` | aleatória | Catálogo de artistas e obras, busca por proximidade (banco `db_obras`) |
| `ms-contribuicoes` | aleatória | Sugestões de obras e moderação (banco `db_contribuicoes`) |
| MySQL 8.4 (Docker) | 3307 | Um banco independente por microsserviço |

### Comunicação síncrona

Ao **aprovar** uma contribuição, o `ms-contribuicoes` chama o `ms-obras` por **OpenFeign** (pelo nome do serviço no
Eureka, sem URL fixa, balanceado pelo Spring Cloud LoadBalancer): cria a obra e a publica
(`Obra.status`: `EM_MODERACAO` → `PUBLICADA`). Só depois a contribuição passa de `PENDENTE` para `APROVADA`.

Se o `ms-obras` estiver fora do ar, o **Circuit Breaker** (Resilience4j) aciona o **fallback**: a resposta é
`503` com mensagem clara e a contribuição continua `PENDENTE`, podendo ser aprovada de novo depois.

## Pré-requisitos

- **JDK 25** (o microsserviço exige Java 25 e Spring Boot 4.0.9)
- **Docker** com Docker Compose
- Nada mais: o Maven vem no projeto (`mvnw`)

> Se o `java` do seu terminal não for o 25, aponte `JAVA_HOME` para o JDK 25 antes de rodar (veja abaixo).
> No IntelliJ, configure o Project SDK dos módulos para o JDK 25 e execute as classes `*Application`.

## Como executar (ambiente limpo)

Todos os comandos a partir da raiz do repositório.

### 1. MySQL com Docker

```bash
docker compose up -d
```

Cria o MySQL na porta **3307** (usuário `root`, senha `root`) com os bancos `db_obras` e `db_contribuicoes`.
Aguarde cerca de **20 segundos** na primeira subida (o MySQL reinicia uma vez ao inicializar).

### 2. Microsserviços (um terminal para cada, nesta ordem)

Linux/macOS/Git Bash:

```bash
export JAVA_HOME=/caminho/do/jdk-25   # só se o java padrão não for o 25
cd eureka-server && ./mvnw spring-boot:run
cd ms-obras && ./mvnw spring-boot:run
cd ms-contribuicoes && ./mvnw spring-boot:run
cd api-gateway && ./mvnw spring-boot:run
```

Windows (PowerShell):

```powershell
$env:JAVA_HOME = "C:\caminho\do\jdk-25"   # só se o java padrão não for o 25
cd eureka-server; .\mvnw.cmd spring-boot:run
cd ms-obras; .\mvnw.cmd spring-boot:run
cd ms-contribuicoes; .\mvnw.cmd spring-boot:run
cd api-gateway; .\mvnw.cmd spring-boot:run
```

O **Flyway** cria as tabelas (`V1`) e carrega os dados iniciais (`V2`) de cada banco na primeira subida.
Nenhuma alteração de código ou configuração é necessária.

### 3. Verificar

- Eureka: <http://localhost:8761> — devem aparecer `API-GATEWAY`, `MS-OBRAS` e `MS-CONTRIBUICOES`.
- Para **várias instâncias**, suba o mesmo serviço de novo em outro terminal: cada uma pega uma porta aleatória.

## Swagger / OpenAPI

As portas dos serviços são aleatórias. Abra o Eureka (<http://localhost:8761>), clique no link da instância do
serviço (coluna *Status*) para ver a porta e acesse:

- `http://localhost:<porta>/swagger-ui.html` (UI interativa)
- `http://localhost:<porta>/v3/api-docs` (contrato OpenAPI)

## Endpoints (via Gateway, `http://localhost:8082`)

**ms-obras**

| Método | Rota | Descrição |
|--------|------|-----------|
| GET/POST | `/api/v1/artistas` | Lista / cria artista |
| GET/PUT/DELETE | `/api/v1/artistas/{id}` | Busca / atualiza / exclui artista |
| GET/POST | `/api/v1/obras` | Lista / cria obra (nasce `EM_MODERACAO`) |
| GET | `/api/v1/obras/proximas?lat=&lng=&raioMetros=100` | Obras publicadas próximas |
| GET/PUT/DELETE | `/api/v1/obras/{id}` | Busca / atualiza / exclui obra |
| PATCH | `/api/v1/obras/{id}/status` | Altera o status (usado pelo ms-contribuicoes) |

**ms-contribuicoes**

| Método | Rota | Descrição |
|--------|------|-----------|
| GET/POST | `/api/v1/contribuicoes` | Lista (filtro `?status=`) / cria (nasce `PENDENTE`) |
| GET/PUT/DELETE | `/api/v1/contribuicoes/{id}` | Busca / atualiza (pendente) / exclui (não aprovada) |
| PATCH | `/api/v1/contribuicoes/{id}/aprovar` | **Chama o ms-obras (OpenFeign)** e aprova |
| PATCH | `/api/v1/contribuicoes/{id}/rejeitar` | Rejeita com `{"motivo": "..."}` |

## Roteiro de teste rápido

```bash
# 1. Obras publicadas perto do Beco do Batman (carga inicial)
curl "http://localhost:8082/api/v1/obras/proximas?lat=-23.5558&lng=-46.6869&raioMetros=200"

# 2. Contribuições pendentes
curl "http://localhost:8082/api/v1/contribuicoes?status=PENDENTE"

# 3. Aprovar a contribuição 1 (comunicação síncrona com o ms-obras)
curl -X PATCH http://localhost:8082/api/v1/contribuicoes/1/aprovar

# 4. A nova obra agora está PUBLICADA
curl http://localhost:8082/api/v1/obras/5

# 5. Fallback: pare o ms-obras (Ctrl+C), crie outra contribuição e tente aprovar -> 503 e continua PENDENTE
curl -X POST http://localhost:8082/api/v1/contribuicoes -H "Content-Type: application/json" \
  -d '{"titulo":"Mural Teste","latitude":-23.55,"longitude":-46.68,"tipo":"MURAL","artistaId":1}'
curl -X PATCH http://localhost:8082/api/v1/contribuicoes/4/aprovar
```

Para refazer os testes do zero (banco limpo com a carga inicial):

```bash
docker compose down -v && docker compose up -d
```

## Tecnologias

Java 25, Spring Boot 4.0.9, Spring Cloud 2025.1.4 (Eureka, Gateway, OpenFeign, LoadBalancer, Resilience4j),
Spring Data JPA, Flyway, MySQL 8.4, springdoc-openapi (Swagger UI), Docker Compose.

## Estrutura

```
eureka-server/      Eureka Server (8761)
api-gateway/        API Gateway (8082)
ms-obras/           entity · dto (request/response) · repository · service · controller · exception
ms-contribuicoes/   entity · dto · client (Feign + fallback) · repository · service · controller · exception
docker-compose.yml  MySQL (3307) + criação dos bancos em docker/mysql-init
```
