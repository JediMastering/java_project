# Guia de Configuração e Execução do Ambiente de Desenvolvimento

Este documento detalha como configurar e executar a aplicação localmente.

## 1. Pré-requisitos

Certifique-se de ter o seguinte instalado:

* **Java Development Kit (JDK):** Versão 21 ou superior.
* **Apache Maven:** Versão 3.8.x ou superior.
* **Docker / Docker Compose:** Para rodar o banco de dados MySQL localmente (se não tiver um MySQL já configurado).
* **Cliente de E-mail:** Para verificar o envio de e-mails de recuperação de senha.

## 2. Configuração de Credenciais e Variáveis de Ambiente

Para manter a segurança, todas as credenciais sensíveis são gerenciadas via variáveis de ambiente e um arquivo settings.xml local.

### 2.1. Configuração do Maven (settings.xml)

O Maven precisa de credenciais para interagir com o banco de dados durante o build (para Flyway e jOOQ).

1. **Copie o template:** Na raiz do projeto, você encontrará o arquivo settings-example.xml. Copie-o para settings.xml na **raiz do projeto**.  
   cp settings-example.xml settings.xml

2. **Edite o settings.xml:** Abra o settings.xml que você acabou de copiar e preencha os valores das propriedades do banco de dados (db.url, db.user, db.password) com as credenciais do seu ambiente local.  
   * **Importante:** Este settings.xml **não deve ser versionado**. Ele já está no .gitignore.

### 2.2. Configuração de Variáveis de Ambiente (set-env.sh)

A aplicação Spring Boot precisa de variáveis de ambiente para suas credenciais de e-mail e a URL do frontend.

1. **Copie o template:** Na raiz do projeto, você encontrará o arquivo set-env-example.sh. Copie-o para set-env.sh.  
   cp set-env-example.sh set-env.sh

2. **Edite o set-env.sh:** Abra o set-env.sh e preencha os valores das variáveis de ambiente com suas credenciais reais e a URL do frontend.  
   * **Importante:** Este set-env.sh **não deve ser versionado**. Ele já está no .gitignore.

## 3. Executando a Aplicação

Para configurar o ambiente, construir o projeto e executar a aplicação, use o script dev-setup.sh.

1. **Torne o script executável:**  
   chmod +x dev-setup.sh

2. **Execute o script:**  
   ./dev-setup.sh

### O que o dev-setup.sh faz:

* Carrega as variáveis de ambiente do set-env.sh.
* Executa mvn --settings settings.xml clean install para construir o projeto, aplicar migrações de banco de dados e gerar código jOOQ.
* Executa mvn --settings settings.xml spring-boot:run para iniciar a aplicação Spring Boot.

## 4. Gerando o JAR Final

Se você precisar gerar o arquivo .jar executável da aplicação (por exemplo, para deploy), use o seguinte comando:  
mvn --settings settings.xml clean package

O arquivo .jar será gerado no diretório target/.

---
