-- Base de données du TP
CREATE DATABASE IF NOT EXISTS security_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE security_db;

-- Tables créées aussi automatiquement par Hibernate (ddl-auto=update).
-- Ce script est fourni pour créer la base manuellement dans MySQL.

CREATE TABLE IF NOT EXISTS roles (
    id   BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS users (
    id       BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    active   BIT(1) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_users_username (username)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS users_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_users_roles_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_users_roles_role FOREIGN KEY (role_id) REFERENCES roles (id)
) ENGINE = InnoDB;

-- Rôles
INSERT INTO roles (id, name) VALUES (1, 'ROLE_ADMIN')
    ON DUPLICATE KEY UPDATE name = name;
INSERT INTO roles (id, name) VALUES (2, 'ROLE_USER')
    ON DUPLICATE KEY UPDATE name = name;

-- Utilisateurs : mots de passe hachés avec BCrypt
-- admin / 1234
-- user  / 1111
INSERT INTO users (id, username, password, active)
VALUES (1, 'admin', '$2a$10$SMiozseoqtj3wE7K54NKH.UK1zP51tn6LZDs1knYLcmNjKv2Zqhf.', TRUE)
    ON DUPLICATE KEY UPDATE username = username;
INSERT INTO users (id, username, password, active)
VALUES (2, 'user', '$2a$10$TzGRvPJAus4HUBexOq5yNuhGk5kSuguGwyjKUSokCvBzpwAW6Zz4q', TRUE)
    ON DUPLICATE KEY UPDATE username = username;

-- Association utilisateurs <-> rôles
INSERT IGNORE INTO users_roles (user_id, role_id) VALUES (1, 1);
INSERT IGNORE INTO users_roles (user_id, role_id) VALUES (1, 2);
INSERT IGNORE INTO users_roles (user_id, role_id) VALUES (2, 2);
