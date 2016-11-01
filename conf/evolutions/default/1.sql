# --- Users schema

# --- !Ups

CREATE TABLE users (
  id SERIAL NOT NULL,
  firstname VARCHAR(255) NOT NULL,
  lastname VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
);

INSERT INTO users (firstname, lastname, email, password)
VALUES ('Diego', 'Figueroa', 'diego.fv94@gmail.com', 'diegofigs'),
  ('Andres', 'Hernandez', 'andres.hernandez2@upr.edu', 'andresher'),
  ('Luis', 'Rivera', 'luis.rivera157@upr.edu', 'lgrs05');

# --- !Downs

DROP TABLE users;