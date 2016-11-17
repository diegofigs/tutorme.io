# --- Add comments schema

# --- !Ups

CREATE TABLE comments (
  id SERIAL NOT NULL,
  videoId INTEGER NOT NULL REFERENCES videos(vid),
  fromEmail VARCHAR(255) NOT NULL REFERENCES users(email),
  text TEXT NOT NULL,
  date VARCHAR(255) NOT NULL,
  favoriteOf VARCHAR(255)[] NOT NULL,
  PRIMARY KEY (id)
);

INSERT INTO comments (videoId, fromEmail, date, text, favoriteOf)
VALUES  ( 1,
          'diego.fv94@gmail.com',
          'Tue Nov 15 01:29:17 AST 2016',
          'Comment1 in video 1',
          '{}'),
        ( 2,
          'andres.hernandez2@upr.edu',
          'Tue Nov 15 01:29:18 AST 2016',
          'Comment2 in video 2. Favorite of Andres and Diego.',
          '{"andres.hernandez2@upr.edu", "diego.fv94@gmail.com"}'),
        ( 3,
          'luis.rivera157@upr.edu',
          'Tue Nov 15 01:29:19 AST 2016',
          'Comment3 in video 3',
          '{}'),
        ( 4,
          'luis.rivera157@upr.edu',
          'Tue Nov 15 01:29:20 AST 2016',
          'Comment4 in video 4. Favorite of Luis.',
          '{"luis.rivera157@upr.edu"}'),
        ( 1,
          'diego.fv94@gmail.com',
          'Tue Nov 15 01:29:21 AST 2016',
          'Comment5 in video 1',
          '{}');

# --- !Downs

DROP TABLE comments;
