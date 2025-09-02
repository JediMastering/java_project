DROP TABLE IF EXISTS tb_tweets;
DROP TABLE IF EXISTS tb_users_access_groups;
DROP TABLE IF EXISTS tb_access_group_permissions;
DROP TABLE IF EXISTS tb_users_roles;
DROP TABLE IF EXISTS tb_features;
DROP TABLE IF EXISTS tb_access_groups;
DROP TABLE IF EXISTS tb_users;

CREATE TABLE tb_features (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    parent_id BIGINT,
    has_view BOOLEAN NOT NULL,
    has_create BOOLEAN NOT NULL,
    has_edit BOOLEAN NOT NULL,
    has_delete BOOLEAN NOT NULL,
    FOREIGN KEY (parent_id) REFERENCES tb_features(id)
);

CREATE TABLE tb_access_groups (
    access_group_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE tb_access_group_permissions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    access_group_id BIGINT NOT NULL,
    feature_id BIGINT NOT NULL,
    can_view BOOLEAN NOT NULL,
    can_create BOOLEAN NOT NULL,
    can_edit BOOLEAN NOT NULL,
    can_delete BOOLEAN NOT NULL,
    FOREIGN KEY (access_group_id) REFERENCES tb_access_groups(access_group_id),
    FOREIGN KEY (feature_id) REFERENCES tb_features(id),
    UNIQUE (access_group_id, feature_id)
);

CREATE TABLE tb_users (
    user_id BINARY(16) PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE tb_users_access_groups (
    user_id BINARY(16) NOT NULL,
    access_group_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, access_group_id),
    FOREIGN KEY (user_id) REFERENCES tb_users(user_id),
    FOREIGN KEY (access_group_id) REFERENCES tb_access_groups(access_group_id)
);

INSERT INTO tb_features (id, name, parent_id, has_view, has_create, has_edit, has_delete) VALUES
(1, 'Página Principal', NULL, TRUE, FALSE, FALSE, FALSE),
(2, 'Usuários', NULL, TRUE, TRUE, TRUE, TRUE),
(3, 'Criar Usuário', 2, TRUE, TRUE, FALSE, FALSE),
(4, 'Deletar Usuário', 2, TRUE, FALSE, FALSE, TRUE),
(5, 'Grupos de Acesso', NULL, TRUE, TRUE, TRUE, TRUE);
