# Projeto de Gerenciamento de Usuários

## Descrição

Este projeto é um microserviço de gerenciamento de usuários, desenvolvido em Java com Spring Boot. Ele fornece uma API
REST para gerenciar usuários, listas de compras e locais, com funcionalidades de autenticação e segurança.

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.3.5**
- **Spring Cloud 2023.0.3**
- **Spring Security (OAuth2)**
- **Spring Data JPA**
- **PostgreSQL**
- **Flyway**
- **Redis (Cache)**
- **Eureka (Service Discovery)**
- **OpenFeign (Client)**
- **Lombok**
- **Maven**

## Funcionalidades

### Usuários

- **Cadastro de Usuários:** `POST /usuarios/cadastrar`
- **Login:** `POST /usuarios/login`
- **Exclusão de Usuários:** `DELETE /usuarios/{id}`
- **Alteração de Senha:** `PUT /usuarios/alterar-senha`

### Listas de Compras

- **Cadastro de Listas de Compras:** `POST /lista-compra`
- **Listagem de Listas de Compras:** `GET /lista-compra`
- **Busca de Lista de Compras por ID:** `GET /lista-compra/{id}`
- **Exclusão de Listas de Compras:** `DELETE /lista-compra/{id}`
- **Atualização do Valor Total:** `PUT /lista-compra/atualizar-valor-total`

### Locais

- **Cadastro de Locais:** `POST /locais`
- **Listagem de Locais:** `GET /locais`
- **Busca de Local por ID:** `GET /locais/{id}`
- **Exclusão de Locais:** `DELETE /locais/{id}`

## Como Executar

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/seu-usuario/seu-repositorio.git
   ```
2. **Navegue até o diretório do projeto:**
   ```bash
   cd users
   ```
3. **Execute o projeto:**
   ```bash
   ./mvnw spring-boot:run
   ```

## Configuração

As configurações da aplicação podem ser encontradas no arquivo `src/main/resources/application.yml`.
