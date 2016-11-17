# --- Add comments schema

# --- !Ups

CREATE TABLE comments (
  id SERIAL NOT NULL,
  videoId INTEGER NOT NULL REFERENCES videos(vid),
  fromEmail VARCHAR(255) NOT NULL REFERENCES users(email),
  text TEXT NOT NULL,
  date DATE NOT NULL,
  favoriteOf VARCHAR(255)[] NOT NULL,
  PRIMARY KEY (id)
);

INSERT INTO comments (videoId, fromEmail, date, text, favoriteOf)
VALUES  ( 1,
          'diego.fv94@gmail.com',
          CURRENT_DATE ,
          'Comment1 in video 1',
          '{}'),
        ( 2,
          'andres.hernandez2@upr.edu',
          CURRENT_DATE ,
          'Comment2 in video 2. Favorite of Andres and Diego.',
          '{"andres.hernandez2@upr.edu", "diego.fv94@gmail.com"}'),
        ( 3,
          'luis.rivera157@upr.edu',
          CURRENT_DATE ,
          'Comment3 in video 3',
          '{}'),
        ( 4,
          'luis.rivera157@upr.edu',
          CURRENT_DATE ,
          'Comment4 in video 4. Favorite of Luis.',
          '{"luis.rivera157@upr.edu"}'),
        ( 1,
          'diego.fv94@gmail.com',
          CURRENT_DATE ,
          'Comment5 in video 1',
          '{}');

# --- !Downs

DROP TABLE comments;
