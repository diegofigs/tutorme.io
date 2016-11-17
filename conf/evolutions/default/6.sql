# --- Lessons schemas

# --- !Ups

CREATE TABLE lessons (
  lid SERIAL NOT NULL,
  name VARCHAR(255) NOT NULL,

  PRIMARY KEY (lid)
);

INSERT INTO lessons (name)
VALUES ('Limits'),
  ('Derivatives'),
  ('Antiderivatives'),
  ('Definite Integrals');

# --- !Downs

DROP TABLE lessons;

