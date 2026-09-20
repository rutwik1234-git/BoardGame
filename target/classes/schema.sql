-- Spring Security Default Schema
CREATE TABLE IF NOT EXISTS users (
    username VARCHAR(50) NOT NULL PRIMARY KEY,
    password VARCHAR(500) NOT NULL,
    enabled BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS authorities (
    username VARCHAR(50) NOT NULL,
    authority VARCHAR(50) NOT NULL,
    CONSTRAINT fk_authorities_users FOREIGN KEY (username) REFERENCES users(username)
);

CREATE UNIQUE INDEX IF NOT EXISTS ix_auth_username ON authorities (username, authority);

-- Boardgame Application Schema
CREATE TABLE IF NOT EXISTS boardgames (
    id          BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(128) NOT NULL,
    level       INT NOT NULL DEFAULT 1,
    min_players INT NOT NULL,
    max_players VARCHAR(50) NOT NULL,
    game_type   VARCHAR(50) NOT NULL DEFAULT 'General'
);

CREATE TABLE IF NOT EXISTS reviews (
    id      BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    game_id BIGINT NOT NULL,
    `text`  VARCHAR(1024) NOT NULL UNIQUE,
    CONSTRAINT game_review_fk FOREIGN KEY (game_id) REFERENCES boardgames (id) ON DELETE CASCADE
);
