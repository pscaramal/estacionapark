# EstacionaPark

Sistema desenvolvido com:

- Java 21
- Spring Boot 4.1.0
- MySQL
- Flyway

## Pré-requisitos

Antes de iniciar o projeto, certifique-se de possuir instalado:

- Java 21
- Maven 3.9+
- Docker
- Docker Compose

## Configuração do Ambiente

Configure as seguintes variáveis de ambiente:

| Variável | Descrição |
|-----------|------------|
| MYSQL_DATABASE | Nome do banco de dados |
| MYSQL_USER | Usuário da aplicação |
| MYSQL_PASSWORD | Senha do usuário da aplicação |
| MYSQL_ROOT_PASSWORD | Senha do usuário root do MySQL |
| VOLUME | Diretório utilizado para persistência dos volumes Docker |

Exemplo:

```bash
export MYSQL_DATABASE=garage
export MYSQL_USER=appuser
export MYSQL_PASSWORD=apppassword
export MYSQL_ROOT_PASSWORD=rootpassword
export VOLUME=/opt/estacionapark
```

## Subindo as Dependências

O projeto utiliza containers Docker para disponibilizar:

- Banco de dados MySQL
- Simulador utilizado pela aplicação

Acesse a pasta `docker`, localizada na raiz do projeto:

```bash
cd docker
```

Suba os containers:

```bash
docker compose up -d
```

Verifique se os containers foram iniciados corretamente:

```bash
docker compose ps
```

## Executando a Aplicação

Na raiz do projeto, execute:

```bash
mvn spring-boot:run
```

Ou gere o artefato e execute:

```bash
mvn clean package

java -jar target/estacionapark.jar
```

## Banco de Dados

As migrações são executadas automaticamente pelo Flyway durante a inicialização da aplicação.

Não é necessário criar tabelas ou executar scripts manualmente.