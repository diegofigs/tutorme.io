# --- Users schema

# --- !Ups

CREATE TABLE users (
  id SERIAL NOT NULL,
  firstname VARCHAR(255) NOT NULL,
  lastname VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  type INTEGER NOT NULL,
  PRIMARY KEY (id)
);

INSERT INTO users (firstname, lastname, email, password, type)
VALUES ('Diego', 'Figueroa', 'diego.fv94@gmail.com', 'diegofigs', 0),
  ('Andres', 'Hernandez', 'andres.hernandez2@upr.edu', 'andresher', 0),
  ('Luis', 'Rivera', 'luis.rivera157@upr.edu', 'lgrs05', 0),
  ('Manuel', 'Rodriguez', 'manuel.rodriguez7@upr.edu', 'manuelrodz', 0),
  ('Michael', 'Fuentes', 'micfuentes@gmail.com', 'micfuentes', 1),
  ('Dominic', 'Rodriguez', 'domrodz@gmail.com', 'domrodz', 1),
  ('Luis', 'Rivera', 'lgrs05@gmail.com', 'lgrs05', 1);

# --- !Downs

DROP TABLE users;