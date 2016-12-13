# --- Add payments schema

# --- !Ups

CREATE TABLE payments (
  id SERIAL NOT NULL,
  fromId INTEGER NOT NULL REFERENCES users(id),
  toId INTEGER NOT NULL REFERENCES users(id),
  quantity MONEY NOT NULL,
  cardholder VARCHAR(255) NOT NULL,
  cardnumber VARCHAR(255) NOT NULL,
  expirationmonth INTEGER NOT NULL,
  expirationyear INTEGER NOT NULL,
  cvv VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
);

# --- !Downs

DROP TABLE payments;