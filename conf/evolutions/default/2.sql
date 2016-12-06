# --- Add Students and Tutors schema

# --- !Ups

CREATE TABLE students (
  id INTEGER NOT NULL REFERENCES users(id),
  summary TEXT,
  interests TEXT,
  activities TEXT,
  skills TEXT,
  PRIMARY KEY (id)
);

INSERT INTO students (id)
    VALUES (5), (6),(7);

CREATE TABLE tutors (
  id INTEGER NOT NULL REFERENCES users(id),
  summary TEXT,
  interests TEXT,
  education TEXT,
  certifications TEXT,
  PRIMARY KEY (id)
);

INSERT INTO tutors (id)
    VALUES (1), (2), (3), (4);

# --- !Downs

DROP TABLE students;

DROP TABLE tutors;

