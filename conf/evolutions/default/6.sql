# --- Lessons schemas

# --- !Ups

CREATE TABLE lessons (
  lid SERIAL NOT NULL,
  name VARCHAR(255) NOT NULL,
  sid INTEGER REFERENCES sections(id),

  PRIMARY KEY (lid)
);

INSERT INTO lessons (name, sid)
VALUES ('Limits', 2),
  ('Derivatives', 2),
  ('Antiderivatives',2),
  ('Definite Integrals',2),
  ('Basic Concepts',4),
  ('Linked Lists', 4),
  ('Positional Lists', 4),
  ('Runtime Complexity', 4),
  ('Sorting', 4),
  ('Hash Maps', 4),
  ('Trees', 4),
  ('Graphs',4);

# --- !Downs

DROP TABLE lessons;

