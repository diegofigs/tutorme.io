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
  ('Manuel', 'Rodriguez', 'manuel.rodriguez7@upr.edu', 'manuelrodz', 0);

# --- !Downs

DROP TABLE users;