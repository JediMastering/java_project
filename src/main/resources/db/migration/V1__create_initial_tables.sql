-- Cria a tabela de features (recursos do sistema)
CREATE TABLE tb_features (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    parent_id BIGINT,
    has_view BOOLEAN DEFAULT false,
    has_create BOOLEAN DEFAULT false,
    has_edit BOOLEAN DEFAULT false,
    has_delete BOOLEAN DEFAULT false,
    FOREIGN KEY (parent_id) REFERENCES tb_features(id)
);

-- Cria a tabela de grupos de acesso
CREATE TABLE tb_access_groups (
    access_group_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

-- Cria a tabela de permissões por grupo de acesso
CREATE TABLE tb_access_group_permissions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    access_group_id BIGINT NOT NULL,
    feature_id BIGINT NOT NULL,
    can_view BOOLEAN DEFAULT false,
    can_create BOOLEAN DEFAULT false,
    can_edit BOOLEAN DEFAULT false,
    can_delete BOOLEAN DEFAULT false,
    FOREIGN KEY (access_group_id) REFERENCES tb_access_groups(access_group_id),
    FOREIGN KEY (feature_id) REFERENCES tb_features(id)
);

-- Cria a tabela de usuários
CREATE TABLE tb_users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- Cria a tabela de junção entre usuários e grupos de acesso
CREATE TABLE tb_users_access_groups (
    user_id BIGINT NOT NULL,
    access_group_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, access_group_id),
    FOREIGN KEY (user_id) REFERENCES tb_users(user_id),
    FOREIGN KEY (access_group_id) REFERENCES tb_access_groups(access_group_id)
);

-- Cria a tabela de anexos
CREATE TABLE attachments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    original_file_name VARCHAR(255) NOT NULL,
    stored_file_name VARCHAR(255) NOT NULL UNIQUE,
    path VARCHAR(255) NOT NULL,
    file_type VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL,
    entity_id VARCHAR(255) NOT NULL,
    entity_type VARCHAR(255) NOT NULL
);
