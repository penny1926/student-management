INSERT INTO students (first_name, last_name, email)
VALUES ('John', 'Smith', 'john.smith@example.com');

INSERT INTO students (first_name, last_name, email)
VALUES ('Jane', 'Doe', 'jane.doe@example.com');

INSERT INTO courses (name, description)
VALUES ('Angular', 'Angular fundamentals');

INSERT INTO courses (name, description)
VALUES ('Java', 'Java programming');

INSERT INTO courses (name, description)
VALUES ('Database', 'Database fundamentals');

INSERT INTO student_courses (student_id, course_id)
VALUES (1, 1);

INSERT INTO student_courses (student_id, course_id)
VALUES (1, 2);

INSERT INTO student_courses (student_id, course_id)
VALUES (2, 2);

INSERT INTO student_courses (student_id, course_id)
VALUES (2, 3);