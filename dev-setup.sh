#!/bin/bash

# Este script automatiza o setup e execução do ambiente de desenvolvimento.
# Certifique-se de que 'set-env.sh' e 'settings.xml' estejam configurados.

# Sair imediatamente se um comando falhar
set -e

echo "--- Iniciando setup do ambiente de desenvolvimento ---"

# 1. Carregar variáveis de ambiente
ENV_FILE="set-env.sh"
if [ -f "$ENV_FILE" ]; then
    echo "Carregando variáveis de ambiente de $ENV_FILE..."
    source "$ENV_FILE"
else
    echo "ERRO: Arquivo '$ENV_FILE' não encontrado. Por favor, crie-o a partir de 'set-env-example.sh' e preencha suas credenciais." >&2
    echo "Setup abortado." >&2
    exit 1
fi

# 2. Verificar settings.xml
SETTINGS_FILE="settings.xml"
if [ ! -f "$SETTINGS_FILE" ]; then
    echo "ERRO: Arquivo '$SETTINGS_FILE' não encontrado. Por favor, copie 'settings-example.xml' para '$SETTINGS_FILE' e preencha suas credenciais de banco de dados." >&2
    echo "Setup abortado." >&2
    exit 1
fi

echo "--- Executando Maven clean install ---"
mvn --settings "$SETTINGS_FILE" clean install

echo "--- Executando aplicação Spring Boot ---"
mvn --settings "$SETTINGS_FILE" spring-boot:run

echo "--- Setup de desenvolvimento concluído ---"
