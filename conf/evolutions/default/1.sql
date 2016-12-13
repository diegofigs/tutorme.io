# --- Users schema

# --- !Ups

CREATE TABLE users (
  id SERIAL NOT NULL,
  firstname VARCHAR(255) NOT NULL,
  lastname VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  type INTEGER NOT NULL,
  active BOOLEAN NOT NULL DEFAULT FALSE,
  PRIMARY KEY (id)
);

INSERT INTO users (firstname, lastname, email, password, type, active)
VALUES ('Diego', 'Figueroa', 'diego.fv94@gmail.com', 'diegofigs', 0, true),
  ('Andres', 'Hernandez', 'andres.hernandez2@upr.edu', 'andresher', 0, true),
  ('Luis', 'Rivera', 'luis.rivera157@upr.edu', 'lgrs05', 0, true),
  ('Manuel', 'Rodriguez', 'manuel.rodriguez7@upr.edu', 'manuelrodz', 0, true),
  ('Michael', 'Fuentes', 'micfuentes@gmail.com', 'micfuentes', 1, true),
  ('Dominic', 'Rodriguez', 'domrodz@gmail.com', 'domrodz', 1, true),
  ('Luis', 'Rivera', 'lgrs05@gmail.com', 'lgrs05', 1, true);

# --- !Downs

DROP TABLE users;