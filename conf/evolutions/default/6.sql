# --- Documents, Assignments, and videos schemas

# --- !Ups

CREATE TABLE documents (
  did SERIAL NOT NULL,
  dtitle VARCHAR(255) NOT NULL,
  ddescription VARCHAR(255) NOT NULL,
  dpath VARCHAR(255) NOT NULL,
  lid INT NOT NULL,


  FOREIGN KEY (lid) REFERENCES lessons(lid),
  PRIMARY KEY (did)
);

CREATE TABLE assignments (
  aid SERIAL NOT NULL,
  atitle VARCHAR(255) NOT NULL,
  adescription VARCHAR(255) NOT NULL,
  apath VARCHAR(255) NOT NULL,
  lid INT NOT NULL,
  deadline DATE NOT NULL,


  FOREIGN KEY (lid) REFERENCES lessons(lid),
  PRIMARY KEY (aid)
);

CREATE TABLE videos (
  vid SERIAL NOT NULL,
  vtitle VARCHAR(255) NOT NULL,
  URL VARCHAR(255) NOT NULL,
  lid INT NOT NULL,


  FOREIGN KEY (lid) REFERENCES lessons(lid),
  PRIMARY KEY (vid)
);

INSERT INTO documents (dtitle, ddescription, lid, dpath)
VALUES ('Limits Practice', '15 limits exercises', 1, 'C:\Users\luisr\OneDrive\Uni\Database\Repo\ER Diagram'),
  ('Derivatives Practice', '15 derivatives exercises', 2, 'C:\Users\luisr\OneDrive\Uni\Database\Repo\ER Diagram'),
  ('Antiderivatives Practice', '15 antiderivatives exercises', 3, 'C:\Users\luisr\OneDrive\Uni\Database\Repo\ER Diagram'),
  ('Definite Integrals Practice', '15 definite integrals exercises', 4, 'C:\Users\luisr\OneDrive\Uni\Database\Repo\ER Diagram');

  INSERT INTO assignments (atitle, adescription, lid, deadline, apath)
VALUES ('Limits Practice', '15 limits exercises', 1, CURRENT_DATE, 'C:\Users\luisr\OneDrive\Uni\Database\Repo\ER Diagram' ),
  ('Derivatives Practice', '15 derivatives exercises', 2, CURRENT_DATE,'C:\Users\luisr\OneDrive\Uni\Database\Repo\ER Diagram' ),
  ('Antiderivatives Practice', '15 antiderivatives exercises', 3, CURRENT_DATE, 'C:\Users\luisr\OneDrive\Uni\Database\Repo\ER Diagram' ),
  ('Definite Integrals Practice', '15 definite integrals exercises', 4, CURRENT_DATE, 'C:\Users\luisr\OneDrive\Uni\Database\Repo\ER Diagram' );

  INSERT INTO videos (vtitle, URL, lid)
VALUES ('Limits', 'Q8TXgCzxEnw', 1),
  ('Derivatives', 'Q8TXgCzxEnw', 2),
  ('Antiderivatives', 'Q8TXgCzxEnw', 3),
  ('Definite Integrals', 'Q8TXgCzxEnw', 4);


# --- !Downs

DROP TABLE documents, assignments, videos;