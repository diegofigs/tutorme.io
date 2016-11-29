# --- Add WallPosts schema

# --- !Ups

CREATE TABLE wallPosts (
  id SERIAL NOT NULL,
  sectionId INTEGER NOT NULL REFERENCES sections(id),
  fromEmail VARCHAR(255) NOT NULL REFERENCES users(email),
  text TEXT NOT NULL,
  date VARCHAR(255) NOT NULL,
  favoriteOf VARCHAR(255)[] NOT NULL,
  PRIMARY KEY (id)
);

INSERT INTO wallPosts (sectionId, fromEmail, date, text, favoriteOf)
VALUES  ( 1,
          'diego.fv94@gmail.com',
          'Tue Nov 15 01:29:17 AST 2016',
          'WallPost1 in section 1',
          '{}'),
        ( 1,
          'andres.hernandez2@upr.edu',
          'Tue Nov 15 01:29:18 AST 2016',
          'WallPost2 in section 1. Favorite of Andres and Diego.',
          '{"andres.hernandez2@upr.edu", "diego.fv94@gmail.com"}'),
        ( 1,
          'luis.rivera157@upr.edu',
          'Tue Nov 15 01:29:19 AST 2016',
          'WallPost3 in section 1',
          '{}'),
        ( 1,
          'luis.rivera157@upr.edu',
          'Tue Nov 15 01:29:20 AST 2016',
          'WallPost4 in section 1. Favorite of Luis.',
          '{"luis.rivera157@upr.edu"}'),
        ( 1,
          'diego.fv94@gmail.com',
          'Tue Nov 15 01:29:21 AST 2016',
          'WallPost5 in section 1',
          '{}'),
        ( 2,
          'diego.fv94@gmail.com',
          'Tue Nov 15 01:29:17 AST 2016',
          'WallPost1 in section 2',
          '{}'),
        ( 2,
          'andres.hernandez2@upr.edu',
          'Tue Nov 15 01:29:18 AST 2016',
          'WallPost2 in section 2. Favorite of Andres and Diego.',
          '{"andres.hernandez2@upr.edu", "diego.fv94@gmail.com"}'),
        ( 2,
          'luis.rivera157@upr.edu',
          'Tue Nov 15 01:29:19 AST 2016',
          'WallPost3 in section 2',
          '{}'),
        ( 2,
          'luis.rivera157@upr.edu',
          'Tue Nov 15 01:29:20 AST 2016',
          'WallPost4 in section 2. Favorite of Luis.',
          '{"luis.rivera157@upr.edu"}'),
        ( 2,
          'diego.fv94@gmail.com',
          'Tue Nov 15 01:29:21 AST 2016',
          'WallPost5 in section 2',
          '{}'),
        ( 3,
          'diego.fv94@gmail.com',
          'Tue Nov 15 01:29:17 AST 2016',
          'WallPost1 in section 3',
          '{}'),
        ( 3,
          'andres.hernandez2@upr.edu',
          'Tue Nov 15 01:29:18 AST 2016',
          'WallPost2 in section 3. Favorite of Andres and Diego.',
          '{"andres.hernandez2@upr.edu", "diego.fv94@gmail.com"}'),
        ( 3,
          'luis.rivera157@upr.edu',
          'Tue Nov 15 01:29:19 AST 2016',
          'WallPost3 in section 3',
          '{}'),
        ( 3,
          'luis.rivera157@upr.edu',
          'Tue Nov 15 01:29:20 AST 2016',
          'WallPost4 in section 3. Favorite of Luis.',
          '{"luis.rivera157@upr.edu"}'),
        ( 3,
          'diego.fv94@gmail.com',
          'Tue Nov 15 01:29:21 AST 2016',
          'WallPost5 in section 3',
          '{}');

# --- !Downs

DROP TABLE wallPosts;
