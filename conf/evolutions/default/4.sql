# --- Add Section and Courses schema

# --- !Ups

CREATE TABLE courses (
  id SERIAL NOT NULL,
  title TEXT,
  description TEXT,
  PRIMARY KEY (id)
);

INSERT INTO courses (title, description)
VALUES ('Database Systems', 'An introduction course to what databases are, ' ||
                            'how they are distributed and deployed, ' ||
                            'and how they work in real-world systems.');

CREATE TABLE sections (
  id SERIAL NOT NULL,
  course_id INTEGER REFERENCES courses(id),
  tutor_id INTEGER REFERENCES tutors(id),
  PRIMARY KEY (id)
);

INSERT INTO sections (course_id, tutor_id)
    VALUES (1, 4);

CREATE TABLE enroll (
  student_id INTEGER NOT NULL REFERENCES students(id),
  section_id INTEGER NOT NULL REFERENCES sections(id),
  PRIMARY KEY (student_id, section_id)
);

# --- !Downs

DROP TABLE enroll;

DROP TABLE sections;

DROP TABLE courses;