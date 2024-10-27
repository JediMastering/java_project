create table if not exists tb_users (user_id bigint not null auto_increment, password varchar(255), username varchar(255), primary key (user_id), unique (username)) engine=InnoDB;
create table if not exists tb_roles (role_id bigint not null auto_increment, name varchar(255), primary key (role_id)) engine=InnoDB;
create table if not exists tb_users_roles (user_id bigint not null, role_id bigint not null, primary key (user_id, role_id), foreign key (role_id) references tb_roles (role_id), foreign key (user_id) references tb_users (user_id)) engine=InnoDB;

create table if not exists tb_tweets (tweet_id bigint not null, content varchar(255), creation_timestamp datetime(6), user_id bigint, primary key (tweet_id), foreign key (user_id) references tb_users (user_id)) engine=InnoDB;
create table if not exists tb_tweets_seq (next_val bigint) engine=InnoDB;

INSERT IGNORE INTO tb_roles (role_id, name) VALUES (1, 'admin');
INSERT IGNORE INTO tb_roles (role_id, name) VALUES (2, 'basic');