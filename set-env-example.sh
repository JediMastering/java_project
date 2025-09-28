#!/bin/bash

# Este arquivo é um EXEMPLO para configurar as variáveis de ambiente.
# COPIE este arquivo para 'set-env.sh' (ou similar) e preencha com seus valores reais.
# NÃO VERSIONAR o arquivo com seus valores reais no Git!

# Credenciais do Banco de Dados (para o Maven e para a aplicação)
export DB_URL="jdbc:mysql://localhost:3306/mydatabase"
export DB_USER="myuser"
export DB_PASSWORD="myuserpassword"

# Credenciais de E-mail (para a aplicação)
export SPRING_MAIL_HOST="YOUR_HOST_HERE" # Ou o host do seu servidor de e-mail
export SPRING_MAIL_PORT="587" # Ou a porta do seu servidor de e-mail
export SPRING_MAIL_USERNAME="YOUR_EMAIL_HERE"
export SPRING_MAIL_PASSWORD="YOUR_EMAIL_PASSWORD_HERE"

# URL do Frontend (para a aplicação)
export APP_FRONTEND_BASE_URL="http://localhost:5173"

echo "Variáveis de ambiente de desenvolvimento carregadas."
