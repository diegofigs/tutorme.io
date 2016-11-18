# --- Lessons schemas

# --- !Ups

CREATE TABLE lessons (
  lid SERIAL NOT NULL,
  name VARCHAR(255) NOT NULL,
  sid INTEGER REFERENCES sections(id),

  PRIMARY KEY (lid)
);

INSERT INTO lessons (name, sid)
VALUES ('Limits', 1),
  ('Derivatives', 1),
  ('Antiderivatives',1),
  ('Definite Integrals',1);

# --- !Downs

DROP TABLE lessons;

