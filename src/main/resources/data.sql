create table if not exists tb_users (user_id bigint not null auto_increment, password varchar(255), username varchar(255), primary key (user_id), unique (username)) engine=InnoDB;
create table if not exists tb_roles (role_id bigint not null auto_increment, name varchar(255), primary key (role_id)) engine=InnoDB;
create table if not exists tb_users_roles (user_id bigint not null, role_id bigint not null, primary key (user_id, role_id), foreign key (role_id) references tb_roles (role_id), foreign key (user_id) references tb_users (user_id)) engine=InnoDB;

create table if not exists tb_tweets (tweet_id bigint not null, content varchar(255), creation_timestamp datetime(6), user_id bigint, primary key (tweet_id), foreign key (user_id) references tb_users (user_id)) engine=InnoDB;
create table if not exists tb_tweets_seq (next_val bigint) engine=InnoDB;

INSERT IGNORE INTO tb_roles (role_id, name) VALUES (1, 'admin');
INSERT IGNORE INTO tb_roles (role_id, name) VALUES (2, 'basic');

CREATE TABLE IF NOT EXISTS tb_tokens (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    access_token VARCHAR(512) NOT NULL,
    access_token_expiration DATETIME NOT NULL,
    refresh_token VARCHAR(512) NOT NULL,
    refresh_token_expiration DATETIME NOT NULL,
    is_active INT,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES tb_users(user_id)  -- Relaciona com a tabela tb_users
) ENGINE=InnoDB;