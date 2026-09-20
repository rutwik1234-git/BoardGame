-- 1. Spring Security default schema
CREATE TABLE IF NOT EXISTS users (
    username VARCHAR(50) NOT NULL PRIMARY KEY,
    password VARCHAR(500) NOT NULL,
    enabled BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS authorities (
    username VARCHAR(50) NOT NULL,
    authority VARCHAR(50) NOT NULL,
    CONSTRAINT fk_authorities_users FOREIGN KEY(username) REFERENCES users(username)
);

CREATE UNIQUE INDEX IF NOT EXISTS ix_auth_username ON authorities (username, authority);

-- 2. Domain application tables
CREATE TABLE IF NOT EXISTS boardgames (
    id          BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(128) NOT NULL,
    level       INT NOT NULL,
    minPlayers  INT NOT NULL,
    maxPlayers  VARCHAR(50) NOT NULL, -- Set to VARCHAR to allow '+' values
    gameType    VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS reviews (
    id      BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    gameId  BIGINT NOT NULL,
    `text`  VARCHAR(1024) NOT NULL UNIQUE,
    CONSTRAINT game_review_fk FOREIGN KEY (gameId) REFERENCES boardgames (id) ON DELETE CASCADE
);
