# Documentação da API

Este documento descreve as rotas da API, seus parâmetros e exemplos de uso.

## Autenticação (`TokenController`)

Componente responsável pela autenticação e geração de tokens.

### **POST /login**

Autentica um usuário com base no nome de usuário e senha, retornando um token de acesso JWT.

**Request Body:**

```json
{
  "username": "admin",
  "password": "123"
}
```

**Response (200 OK):**

```json
{
  "accessToken": "ey...",
  "expiresIn": 300
}
```

**Response (401 Unauthorized):**

Se as credenciais forem inválidas.

---

## Usuários (`UserController`)

Componente para o gerenciamento de usuários (CRUD).

### **POST /users**

Cria um novo usuário no sistema.

**Request Body:**

```json
{
  "username": "newUser",
  "password": "password123",
  "accessGroupIds": [2]
}
```

| Campo            | Tipo          | Descrição                                     |
| ---------------- | ------------- | --------------------------------------------- |
| `username`       | String        | Nome de usuário (único). Mínimo 5 caracteres. |
| `password`       | String        | Senha do usuário. Mínimo 8 caracteres.        |
| `accessGroupIds` | Array[Long]   | Lista de IDs dos grupos de acesso do usuário. |

**Response (200 OK):**

```json
{
  "id": "a1b2c3d4-e5f6-7890-1234-567890abcdef",
  "username": "newUser"
}
```

### **GET /users**

Lista os usuários de forma paginada e com suporte a filtros.

**Query Parameters:**

| Parâmetro | Tipo   | Descrição                                                                 |
| --------- | ------ | ------------------------------------------------------------------------- |
| `name`    | String | Filtra usuários pelo nome de usuário (case-insensitive).                  |
| `email`   | String | Filtra usuários pelo email (case-insensitive).                            |
| `role`    | String | Filtra usuários que possuem uma role específica em algum de seus grupos.  |
| `page`    | Int    | Número da página (padrão: 0).                                             |
| `size`    | Int    | Quantidade de itens por página (padrão: 20).                              |
| `sort`    | String | Ordenação. Ex: `sort=username,asc`.                                       |

**Response (200 OK):**

Retorna um objeto de paginação contendo a lista de `UserDTO`.

```json
{
  "content": [
    {
      "id": "a1b2c3d4-e5f6-7890-1234-567890abcdef",
      "username": "admin"
    }
  ],
  "pageable": { ... },
  "totalElements": 1,
  "totalPages": 1,
  "last": true,
  ...
}
```

### **GET /users/{userId}**

Busca um usuário específico pelo seu ID.

**Path Parameter:**

| Parâmetro | Tipo | Descrição             |
| --------- | ---- | --------------------- |
| `userId`  | UUID | ID do usuário a ser buscado. |

**Response (200 OK):**

```json
{
  "id": "a1b2c3d4-e5f6-7890-1234-567890abcdef",
  "username": "admin"
}
```

### **PUT /users/{userId}**

Atualiza os dados de um usuário existente.

**Path Parameter:**

| Parâmetro | Tipo | Descrição               |
| --------- | ---- | ----------------------- |
| `userId`  | UUID | ID do usuário a ser atualizado. |

**Request Body:**

Mesmo formato do `POST /users`.

```json
{
  "username": "updatedUser",
  "password": "newPassword123",
  "accessGroupIds": [1, 2]
}
```

**Response (200 OK):**

```json
{
  "id": "a1b2c3d4-e5f6-7890-1234-567890abcdef",
  "username": "updatedUser"
}
```

### **DELETE /users/{userId}**

Deleta um usuário do sistema.

**Path Parameter:**

| Parâmetro | Tipo | Descrição             |
| --------- | ---- | --------------------- |
| `userId`  | UUID | ID do usuário a ser deletado. |

**Response (204 No Content):**

Retorna uma resposta vazia em caso de sucesso.
