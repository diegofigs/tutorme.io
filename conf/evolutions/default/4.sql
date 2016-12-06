# --- Add Section and Courses schema

# --- !Ups

CREATE TABLE courses (
  id SERIAL NOT NULL,
  tutor_id INTEGER REFERENCES tutors(id),
  title TEXT,
  description TEXT,
  PRIMARY KEY (id)
);

INSERT INTO courses (tutor_id, title, description)
VALUES (4, 'Database Systems', 'An introduction course to what databases are, ' ||
                            'how they are distributed and deployed, ' ||
                            'and how they work in real-world systems.'),
  (1, 'Calculus 1', 'Derivatives, Intro to Equation Systems'),
  (1, 'Calculus 2', 'Integration, Intro to Power and Exponential Series'),
  (3, 'Data Structures','an introduction to data structures
                      and algorithms, including their design, ' ||
                        'analysis, and implementation.');

CREATE TABLE sections (
  id SERIAL NOT NULL,
  course_id INTEGER REFERENCES courses(id),
  tutor_id INTEGER REFERENCES tutors(id),
  PRIMARY KEY (id)
);

INSERT INTO sections (course_id, tutor_id)
VALUES (1, 4),
  (2, 1),
  (3, 1),
  (4, 3);

CREATE TABLE enroll (
  student_id INTEGER NOT NULL REFERENCES students(id),
  section_id INTEGER NOT NULL REFERENCES sections(id),
  PRIMARY KEY (student_id, section_id)
);

INSERT into enroll (student_id, section_id)
VALUES (5, 2),
  (5, 3),
  (6, 2),
  (7,4);

# --- !Downs

DROP TABLE enroll;

DROP TABLE sections;

DROP TABLE courses;