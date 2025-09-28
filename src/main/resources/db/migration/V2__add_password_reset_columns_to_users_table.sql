ALTER TABLE tb_users ADD COLUMN password_reset_token VARCHAR(255) NULL;
ALTER TABLE tb_users ADD COLUMN password_reset_token_expiry_date TIMESTAMP NULL;
