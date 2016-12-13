
# --- Documents, Assignments, and videos schemas

# --- !Ups

CREATE TABLE documents (
  did SERIAL NOT NULL,
  dtitle VARCHAR(255) NOT NULL,
  ddescription VARCHAR(255) NOT NULL,
  dpath VARCHAR(255),
  lid INT NOT NULL REFERENCES lessons(lid),


  PRIMARY KEY (did)
);

CREATE TABLE assignments (
  aid SERIAL NOT NULL,
  atitle VARCHAR(255) NOT NULL,
  adescription VARCHAR(255) NOT NULL,
  apath VARCHAR(255),
  lid INT NOT NULL REFERENCES lessons(lid),
  deadline DATE NOT NULL,


  PRIMARY KEY (aid)
);

CREATE TABLE submissions(
  suid SERIAL NOT NULL,
  aid INT REFERENCES assignments(aid),
  stid INT REFERENCES students(id),
  grade INT,
  spath VARCHAR(255),

  PRIMARY KEY (suid)
);


CREATE TABLE videos (
  vid SERIAL NOT NULL,
  vtitle VARCHAR(255) NOT NULL,
  URL VARCHAR(255) NOT NULL,
  lid INT NOT NULL REFERENCES lessons(lid),


  PRIMARY KEY (vid)
);

INSERT INTO documents (dtitle, ddescription, lid, dpath)
VALUES ('Limits Practice', '15 limits exercises', 1, 'C:\Users\luisr\OneDrive\Uni\Database\Repo\ER Diagram'),
  ('Derivatives Practice', '15 derivatives exercises', 2, 'C:\Users\luisr\OneDrive\Uni\Database\Repo\ER Diagram'),
  ('Antiderivatives Practice', '15 antiderivatives exercises', 3, 'C:\Users\luisr\OneDrive\Uni\Database\Repo\ER Diagram'),
  ('Definite Integrals Practice', '15 definite integrals exercises', 4, 'C:\Users\luisr\OneDrive\Uni\Database\Repo\ER Diagram'),
  ('Basic Concepts','Exercises',5,'file:\\\C:\Users\luisr\OneDrive\Uni\Database\Repo\ER Diagram'),
  ('Linked Lists', 'Exercises',6,'file:\\\C:\Users\luisr\OneDrive\Uni\Database\Repo\ER Diagram'),
  ('Positional Lists','Exercises',7,'file:\\\C:\Users\luisr\OneDrive\Uni\Database\Repo\ER Diagram'),
  ('Runtime Complexity','Exercises',8,'file:\\\C:\Users\luisr\OneDrive\Uni\Database\Repo\ER Diagram'),
  ('Sorting', 'Exercises',9,'file:\\\C:\Users\luisr\OneDrive\Uni\Database\Repo\ER Diagram'),
  ('Hash Maps', 'Exercises',10,'file:\\\C:\Users\luisr\OneDrive\Uni\Database\Repo\ER Diagram'),
  ('Trees', 'Exercises',11,'file:\\\C:\Users\luisr\OneDrive\Uni\Database\Repo\ER Diagram'),
  ('Graphs','Exercises',12,'file:\\\C:\Users\luisr\OneDrive\Uni\Database\Repo\ER Diagram');



INSERT INTO videos (vtitle, URL, lid)
VALUES ('Limits', 'Q8TXgCzxEnw', 1),
  ('Derivatives', 'Q8TXgCzxEnw', 2),
  ('Antiderivatives', 'Q8TXgCzxEnw', 3),
  ('Definite Integrals', 'Q8TXgCzxEnw', 4),
  ('Basic Concepts','Q8TXgCzxEnw', 5),
  ('Linked Lists', 'Q8TXgCzxEnw', 6),
  ('Positional Lists', 'Q8TXgCzxEnw', 7),
  ('Runtime Complexity', 'Q8TXgCzxEnw', 8),
  ('Sorting', 'Q8TXgCzxEnw', 9),
  ('Hash Maps', 'Q8TXgCzxEnw', 10),
  ('Trees', 'Q8TXgCzxEnw', 11),
  ('Graphs','Q8TXgCzxEnw', 12);


# --- !Downs

DROP TABLE documents, assignments, videos, submissions;