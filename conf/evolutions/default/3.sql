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
          'From Diego to Andres, message 1'),
        ( 'luis.rivera157@upr.edu',
          'diego.fv94@gmail.com',
          'Tue Nov 15 01:29:18 AST 2016',
          'From Diego to Luis, message 2'),
        ( 'manuel.rodriguez7@upr.edu',
          'diego.fv94@gmail.com',
          'Tue Nov 15 01:29:19 AST 2016',
          'From Diego to Manuel, message 3'),
        ( 'diego.fv94@gmail.com',
          'andres.hernandez2@upr.edu',
          'Tue Nov 15 01:29:20 AST 2016',
          'From Andres to Diego, message 4'),
        ( 'diego.fv94@gmail.com',
          'andres.hernandez2@upr.edu',
          'Tue Nov 15 01:29:21 AST 2016',
          'From Andres to Diego, message 5'),
        ( 'luis.rivera157@upr.edu',
          'andres.hernandez2@upr.edu',
          'Tue Nov 15 01:29:22 AST 2016',
          'From Andres to Luis, message 6'),
        ( 'manuel.rodriguez7@upr.edu',
          'diego.fv94@gmail.com',
          'Tue Nov 15 01:29:23 AST 2016',
          'From Diego to Manuel, message 7'),
        ( 'diego.fv94@gmail.com',
          'andres.hernandez2@upr.edu',
          'Tue Nov 15 01:29:24 AST 2016',
          'From Andres to Diego, message 8'),
        ( 'andres.hernandez2@upr.edu',
          'luis.rivera157@upr.edu',
          'Tue Nov 15 01:29:25 AST 2016',
          'From Luis to Andres, message 9');

# --- !Downs

DROP TABLE messages;
