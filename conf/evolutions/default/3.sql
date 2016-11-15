# --- Add Messages schema

# --- !Ups

CREATE TABLE messages (
  id SERIAL NOT NULL,
  toEmail VARCHAR(255) NOT NULL REFERENCES users(email),
  fromEmail VARCHAR(255) NOT NULL REFERENCES users(email),
  messageText TEXT NOT NULL,
  date VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
);

INSERT INTO messages (toEmail, fromEmail, date, messageText)
VALUES  ( 'andres.hernandez2@upr.edu',
          'diego.fv94@gmail.com',
          'Tue Nov 15 01:29:17 AST 2016',
          'Test Message 1'),
        ( 'luis.rivera157@upr.edu',
          'diego.fv94@gmail.com',
          'Tue Nov 15 01:29:18 AST 2016',
          'Test Message 1'),
        ( 'manuel.rodriguez7@upr.edu',
          'diego.fv94@gmail.com',
          'Tue Nov 15 01:29:19 AST 2016',
          'Test Message 1'),
        ( 'diego.fv94@gmail.com',
          'andres.hernandez2@upr.edu',
          'Tue Nov 15 01:29:20 AST 2016',
          'Test Message 1');

# --- !Downs

DROP TABLE messages;
