# Sobre Rodas - Site de Notícias Automotivas

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.3-brightgreen)
![Maven](https://img.shields.io/badge/Maven-4.0.0-red)
![MySQL](https://img.shields.io/badge/MySQL-8.0-orange)

O "Sobre Rodas" é um projeto de um portal de notícias focado no universo automotivo, desenvolvido com o framework Spring Boot. A plataforma permite a visualização de notícias, eventos, comparativos e avaliações de veículos, além de possuir uma área administrativa para gerenciamento de conteúdo.

## Funcionalidades

- **Página Inicial Dinâmica:** Apresenta notícias em destaque, últimas publicações, mais lidas e seções de testes e comparativos.
- **Categorias de Conteúdo:** Seções dedicadas para `Eventos` e `Comparativos`.
- **Busca Inteligente:** Funcionalidade de pesquisa de notícias por palavras-chave no título ou conteúdo.
- **Visualização Detalhada:** Página dedicada para cada notícia com conteúdo completo e notícias relacionadas.
- **Segurança:** Área administrativa protegida por login e senha para gerenciamento de notícias (CRUD).

---

## Tecnologias Utilizadas

- **Backend:**
  - **Java 17:** Linguagem de programação principal.
  - **Spring Boot:** Framework para criação da aplicação.
  - **Spring Web:** Para construir os endpoints REST e servir as páginas.
  - **Spring Data JPA (Hibernate):** Para persistência de dados e comunicação com o banco.
  - **Spring Security:** Para controle de autenticação e autorização.
  - **Spring Boot Starter Mail:** Para funcionalidades de envio de e-mail.
- **Frontend:**
  - **Thymeleaf:** Template engine para renderizar as páginas HTML no lado do servidor.
- **Banco de Dados:**
  - **MySQL:** Sistema de gerenciamento de banco de dados relacional.
- **Build & Dependências:**
  - **Maven:** Ferramenta de automação de compilação e gerenciamento de dependências.

---

## Pré-requisitos

Antes de começar, garanta que você tenha os seguintes softwares instalados em sua máquina:

- **JDK 17** (Java Development Kit)
- **MySQL Server** (versão 8.0 ou superior é recomendada)
- **Git**

---

## Guia de Instalação e Execução

Siga os passos abaixo para configurar e executar o projeto localmente.

### 1. Clone o Repositório

Abra seu terminal, navegue até o diretório de sua preferência e clone o projeto:

```bash
git clone https://github.com/seu-usuario/SobreRodas.git
cd SobreRodas
```

### 2. Configure o Banco de Dados

O projeto está configurado para criar o banco de dados automaticamente, mas você precisa informar a senha do seu MySQL.

1.  Abra o arquivo `src/main/resources/application.properties`.
2.  Localize a propriedade `spring.datasource.password`.
3.  Insira a senha do usuário `root` do seu MySQL.

    ```properties
    # Senha do banco de dados (!!! SUBSTITUA PELA SUA SENHA DO MYSQL !!!)
    spring.datasource.password=sua_senha_aqui
    ```

O banco de dados `sobrerodas_db` e as tabelas necessárias serão criados automaticamente na primeira vez que a aplicação for iniciada, graças às configurações do `application.properties`.

### 3. Execute a Aplicação

Use o Maven Wrapper (incluído no projeto) para executar a aplicação. No terminal, a partir da raiz do projeto, execute:

**No Windows:**
```cmd
.\mvnw.cmd spring-boot:run
```

**No Linux ou macOS:**
```bash
./mvnw spring-boot:run
```

A aplicação será iniciada e estará acessível em **http://localhost:8080**.

---

## Estrutura do Projeto

O código-fonte está organizado nos seguintes pacotes principais:

- `com.carro.SobreRodas.controller`: Controladores Spring MVC que recebem as requisições HTTP e direcionam para as views ou retornam dados.
  - `HomeController.java`: Gerencia as páginas públicas do site.
  - `AdminController.java`: (Exemplo) Gerencia as páginas da área administrativa.
- `com.carro.SobreRodas.service`: Contém a lógica de negócio da aplicação.
- `com.carro.SobreRodas.model`: Classes de entidade (JPA) que mapeiam as tabelas do banco de dados.
- `com.carro.SobreRodas.repository`: Interfaces do Spring Data JPA para operações de acesso a dados.
- `src/main/resources/templates`: Contém os arquivos HTML (Thymeleaf) que formam a interface do usuário.
- `src/main/resources/application.properties`: Arquivo central de configurações da aplicação.