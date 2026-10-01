package com.andy.studentmanagement.persistence;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.andy.studentmanagement.domain.Student;
import com.andy.studentmanagement.domain.StudentRequest;

@Repository
public class StudentDAO {

	private final NamedParameterJdbcTemplate jdbcTemplate;

	public StudentDAO(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.jdbcTemplate = namedParameterJdbcTemplate;
	}

	private static final String FIND_ALL_STUDENTS = """
			SELECT id,
			       first_name,
			       last_name,
			       email
			FROM students
			ORDER BY id
			""";

	private static final String FIND_STUDENT_BY_ID = """
			SELECT id,
			       first_name,
			       last_name,
			       email
			FROM students
			WHERE id = :studentId
			""";

	private static final String INSERT_STUDENT = """
			INSERT INTO students (
			    first_name,
			    last_name,
			    email
			)
			VALUES (
			    :firstName,
			    :lastName,
			    :email
			)
			""";

	private static final String UPDATE_STUDENT = """
			UPDATE students
			SET first_name = :firstName,
			    last_name = :lastName,
			    email = :email
			WHERE id = :studentId
			""";

	private static final String DELETE_STUDENT_COURSES = """
			DELETE FROM student_courses
			WHERE student_id = :studentId
			""";

	private static final String DELETE_STUDENT = """
			DELETE FROM students
			WHERE id = :studentId
			""";

	private static final String INSERT_STUDENT_COURSE = """
			INSERT INTO student_courses (
			    student_id,
			    course_id
			)
			VALUES (
			    :studentId,
			    :courseId
			)
			""";

	private static final String DELETE_STUDENT_COURSE = """
			DELETE FROM student_courses
			WHERE student_id = :studentId
			  AND course_id = :courseId
			""";

	private static final String FIND_COURSE_IDS_BY_STUDENT_ID = """
			SELECT course_id
			FROM student_courses
			WHERE student_id = :studentId
			ORDER BY course_id
			""";

	public List<Student> findAll() {

		return jdbcTemplate.query(FIND_ALL_STUDENTS, Map.of(), (rs, rowNum) -> {

			Student student = new Student();

			student.setId(rs.getLong("id"));
			student.setFirstName(rs.getString("first_name"));
			student.setLastName(rs.getString("last_name"));
			student.setEmail(rs.getString("email"));

			return student;
		});
	}

	public Student findById(Long studentId) {

		List<Student> students = jdbcTemplate.query(FIND_STUDENT_BY_ID, Map.of("studentId", studentId),
				(rs, rowNum) -> {

					Student student = new Student();

					student.setId(rs.getLong("id"));
					student.setFirstName(rs.getString("first_name"));
					student.setLastName(rs.getString("last_name"));
					student.setEmail(rs.getString("email"));

					return student;
				});

		if (students.isEmpty()) {
			return null;
		}

		Student student = students.get(0);

		loadCourses(student);

		return student;
	}

	public Student create(StudentRequest studentRequest) {

		MapSqlParameterSource params = new MapSqlParameterSource().addValue("firstName", studentRequest.getFirstName())
				.addValue("lastName", studentRequest.getLastName()).addValue("email", studentRequest.getEmail());

		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(INSERT_STUDENT, params, keyHolder, new String[] { "id" });

		Long studentId = keyHolder.getKey().longValue();

		if (studentRequest.getCourseIds() != null) {

			for (Long courseId : studentRequest.getCourseIds()) {

				enrollCourse(studentId, courseId);
			}
		}

		return findById(studentId);
	}

	public Student update(Long studentId, StudentRequest studentRequest) {

		MapSqlParameterSource params = new MapSqlParameterSource().addValue("studentId", studentId)
				.addValue("firstName", studentRequest.getFirstName()).addValue("lastName", studentRequest.getLastName())
				.addValue("email", studentRequest.getEmail());

		jdbcTemplate.update(UPDATE_STUDENT, params);

		/*
		 * Replace existing course relationships.
		 */
		jdbcTemplate.update(DELETE_STUDENT_COURSES, Map.of("studentId", studentId));

		if (studentRequest.getCourseIds() != null) {

			for (Long courseId : studentRequest.getCourseIds()) {

				enrollCourse(studentId, courseId);
			}
		}

		return findById(studentId);
	}

	public void delete(Long studentId) {

		jdbcTemplate.update(DELETE_STUDENT_COURSES, Map.of("studentId", studentId));

		jdbcTemplate.update(DELETE_STUDENT, Map.of("studentId", studentId));
	}

	public void enrollCourse(Long studentId, Long courseId) {

		jdbcTemplate.update(INSERT_STUDENT_COURSE, Map.of("studentId", studentId, "courseId", courseId));
	}

	public void removeCourse(Long studentId, Long courseId) {

		jdbcTemplate.update(DELETE_STUDENT_COURSE, Map.of("studentId", studentId, "courseId", courseId));
	}

	private void loadCourses(Student student) {

		List<Long> courseIds = jdbcTemplate.query(FIND_COURSE_IDS_BY_STUDENT_ID, Map.of("studentId", student.getId()),
				(rs, rowNum) -> rs.getLong("course_id"));

		student.setCourseIds(courseIds);
	}

}
