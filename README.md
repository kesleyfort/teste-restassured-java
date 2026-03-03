# Sicredi QE Challenge - DummyJSON API Tests

Projeto de testes automatizados para a API [DummyJSON](https://dummyjson.com) desenvolvido como desafio tecnico para Quality Engineer do Sicredi.

## Stack Tecnica

| Tecnologia | Versao | Finalidade |
|---|---|---|
| Java JDK | 17 | Linguagem |
| Maven | 3.9+ | Build e dependencias |
| REST Assured | 5.5.0 | Testes de API REST |
| JUnit 5 | 5.11.3 | Framework de testes |
| Jackson | 2.17.2 | Serializacao JSON |
| Allure | 2.29.0 | Relatorios de teste |
| AssertJ | 3.26.3 | Assercoes fluentes |
| Log4j2 | 2.24.1 | Logging |

## Estrutura do Projeto

```
src/test/
├── java/com/sicredi/api/
│   ├── base/          # BaseTest com configuracao do REST Assured
│   ├── config/        # Configuration singleton (config.properties)
│   ├── model/         # POJOs para request/response (auth, product, user, common)
│   ├── spec/          # RequestSpecs e ResponseSpecs reutilizaveis
│   ├── service/       # Camada de servico encapsulando chamadas HTTP
│   ├── data/          # TestDataFactory para objetos de teste
│   └── tests/         # 7 classes de teste (uma por endpoint)
└── resources/
    ├── config.properties
    ├── allure.properties
    ├── log4j2.xml
    └── schemas/       # JSON Schemas para validacao de resposta
```

## Arquitetura

O projeto segue uma arquitetura em camadas:

1. **Config** - Singleton carrega propriedades com suporte a override via env/system properties
2. **Model** - POJOs com `@JsonIgnoreProperties(ignoreUnknown = true)` para desserializacao type-safe
3. **Spec** - Especificacoes reutilizaveis do REST Assured (base, autenticada, JSON body)
4. **Service** - Encapsula chamadas HTTP, retorna `Response` para controle total nos testes
5. **Data** - Factory methods para criar objetos de teste consistentes
6. **Tests** - Testes organizados por endpoint com Javadoc descritivo

## Como Executar

### Pre-requisitos

- Java 17+
- Maven 3.9+

### Executar todos os testes

```bash
mvn clean test
```

### Executar teste especifico

```bash
mvn clean test -Dtest=AuthLoginTest
```

### Gerar relatorio Allure

```bash
mvn allure:serve
```

### Executar com URL customizada

```bash
mvn clean test -Dbase.url=https://dummyjson.com
```

## Plano de Testes

### Endpoints Cobertos (7 endpoints, 54+ testes)

| # | Endpoint | Classe de Teste | Testes |
|---|---|---|---|
| 1 | GET /test | HealthCheckTest | 3 |
| 2 | GET /users | UserTest | 8 |
| 3 | POST /auth/login | AuthLoginTest | 11 |
| 4 | GET /auth/products | AuthProductsTest | 7 |
| 5 | GET /products | ProductListTest | 8 |
| 6 | GET /products/{id} | ProductByIdTest | 9 |
| 7 | POST /products/add | ProductCreateTest | 9 |

### Cenarios por Endpoint

#### GET /test (Health Check)
- Status 200 com body `{status: "ok", method: "GET"}`
- Content-type JSON
- Validacao de JSON Schema

#### GET /users
- Listagem com paginacao padrao (total, limit=30, skip=0)
- Campos obrigatorios preenchidos (id, firstName, lastName, email, username)
- Usuario `emilys` com credenciais corretas (regra de negocio)
- Paginacao customizada (parametrizado com limit/skip)
- Edge cases: skip > total, limit=0
- Limite de usuarios respeitado
- Validacao de JSON Schema

#### POST /auth/login
- Login com credenciais validas: POJO LoginResponse com id/username/email/accessToken/refreshToken
- Token JWT valido (formato eyJ...)
- Refresh token presente
- Dados corretos do usuario na resposta
- Credenciais invalidas retornam 400
- Username ausente retorna 400
- Password ausente retorna 400
- Campos vazios retornam 400
- Custom expiresInMins aceito
- Validacao de JSON Schema (sucesso e erro)

#### GET /auth/products
- Token valido: 200 com lista de produtos paginada
- Campos de paginacao presentes
- Sem token: 401 "Access Token is required"
- Token invalido: 401 "Invalid/Expired Token"
- Bearer vazio: 401
- Token malformado: 401
- Validacao de JSON Schema

#### GET /products
- Paginacao padrao: total=194, limit=30, skip=0
- Lista nao vazia respeitando limite
- Campos essenciais preenchidos (id, title, price, category)
- Paginacao customizada (parametrizado)
- Precos positivos
- Ratings entre 0-5
- Skip > total retorna lista vazia
- Validacao de JSON Schema

#### GET /products/{id}
- Produto por ID: 200 com dados corretos
- Dados especificos do produto 1
- Reviews presentes com campos validos
- Dimensions com width/height/depth
- Meta com createdAt/updatedAt/barcode
- IDs inexistentes: 404 (parametrizado: 0, -1, 9999)
- Boundary: ultimo valido (194) vs primeiro invalido (195)
- Validacao de JSON Schema

#### POST /products/add
- Criacao com todos os campos: 201 com dados corretos
- Criacao com campos minimos (so titulo)
- Body vazio aceito (BUG documentado)
- Campos da resposta batem com request
- Produto NAO persiste (mock behavior)
- ID retornado e 195 (BUG vs spec que diz 101)
- Description e brand retornados
- Stock e rating retornados
- Validacao de JSON Schema

## Bugs e Discrepancias Identificados

| # | Descricao | Spec Diz | API Retorna | Severidade |
|---|---|---|---|---|
| 1 | Status code do login | 201 (Created) | **200 (OK)** | Media |
| 2 | Campo de token no login | `token` | **`accessToken`** | Alta |
| 3 | Sem token em /auth/products | 403 "Authentication Problem" | **401 "Access Token is required"** | Media |
| 4 | Token invalido - campo name | `{"name":"JsonWebTokenError",...}` | **Sem campo `name`** | Baixa |
| 5 | ID do produto criado | id=101 | **id=195** (total+1) | Media |
| 6 | Body vazio em POST /products/add | Nao especificado | **201 com `{"id":195}`** (sem validacao) | Alta |
| 7 | GET /users com limit=0 | Lista vazia (comportamento esperado) | **Retorna lista padrao ignorando limit=0** | Media |

### Detalhamento dos Bugs

**BUG #1 - Status Code do Login**: A documentacao sugere que o login retorna 201 (Created), porem a API retorna 200 (OK). Embora 200 seja semanticamente aceitavel para login, a inconsistencia com a documentacao e um problema.

**BUG #2 - Nome do Campo Token**: A documentacao referencia o campo como `token`, mas a API retorna `accessToken`. Isso quebra integracoes que seguem a documentacao.

**BUG #3 - Status/Mensagem sem Token**: A documentacao indica 403 com "Authentication Problem", mas a API retorna 401 com "Access Token is required". O status 401 e mais correto semanticamente, porem diverge da spec.

**BUG #4 - Resposta de Token Invalido**: A documentacao mostra campo `name` na resposta de erro, mas a API nao retorna esse campo.

**BUG #5 - ID do Produto Criado**: A documentacao indica id=101, mas a API retorna id=195 (total de produtos + 1).

**BUG #6 - Sem Validacao no POST /products/add**: A API aceita body completamente vazio e retorna 201 com apenas um ID. Nao ha validacao de campos obrigatorios como `title`.

**BUG #7 - Limit=0 Ignorado em GET /users**: Quando `limit=0` e enviado como parametro, a API ignora e retorna a lista com paginacao padrao ao inves de retornar uma lista vazia.

## Sugestoes de Melhoria

1. **Validacao de request body**: O endpoint POST /products/add deveria validar campos obrigatorios (ao menos `title`) e retornar 400 para body vazio ou invalido.

2. **Consistencia na documentacao**: Alinhar a documentacao com o comportamento real da API (status codes, nomes de campos, mensagens de erro).

3. **Versionamento da API**: Implementar versionamento (ex: /v1/products) para evitar breaking changes.

4. **Rate limiting**: Adicionar headers de rate limit (X-RateLimit-Limit, X-RateLimit-Remaining) para documentar os limites da API.

5. **Paginacao consistente**: Padronizar os valores padrao de paginacao (limit/skip) em todos os endpoints.

6. **Respostas de erro padronizadas**: Unificar o formato de erro em todos os endpoints com campos consistentes (message, code, timestamp).

7. **Validacao de tipos**: Campos numericos como price e stock deveriam ser validados na criacao (ex: preco negativo nao deveria ser aceito).

8. **CORS e Security Headers**: Adicionar headers de seguranca como X-Content-Type-Options, X-Frame-Options.

## CI/CD

O projeto inclui pipeline GitHub Actions (`.github/workflows/ci.yml`) que:
- Executa em push para `main`/`develop` e PRs para `main`
- Usa JDK 17 (Temurin)
- Roda todos os testes com Maven
- Gera e publica relatorio Allure no GitHub Pages

## Autor

Desenvolvido para o desafio tecnico de Quality Engineer - Sicredi.
