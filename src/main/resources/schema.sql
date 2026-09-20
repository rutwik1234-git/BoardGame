CREATE TABLE boardgames (
  id          BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  name        VARCHAR(128) NOT NULL,
  level       INT NOT NULL,
  minPlayers  INT NOT NULL,
  maxPlayers  VARCHAR(50) NOT NULL,
  gameType    VARCHAR(50) NOT NULL
);

CREATE TABLE reviews (
  id      BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  gameId  BIGINT NOT NULL,
  `text`  VARCHAR(1024) NOT NULL UNIQUE,
  CONSTRAINT game_review_fk FOREIGN KEY (gameId) REFERENCES boardgames (id) ON DELETE CASCADE
);

-- Insert sample board games
INSERT INTO boardgames (name, level, minPlayers, maxPlayers, gameType)
VALUES 
  ('Splendor', 3, 2, '4', 'Strategy Game'),
  ('Clue', 2, 1, '6', 'Strategy Game'),
  ('Linkee', 1, 2, '+', 'Trivia Game');

-- Insert sample reviews
INSERT INTO reviews (gameId, `text`)
VALUES 
  (1, 'A great strategy game. The one who collects 15 points first wins. Calculation skill is required.'),
  (1, 'Collecting gemstones makes me feel like a wealthy merchant. Highly recommend!'),
  (2, 'A detective game to guess the criminal, weapon, and place of the crime scene. It is more fun with more than 3 players.');
