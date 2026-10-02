package com.andy.studentmanagement.persistence;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.andy.studentmanagement.domain.Course;
import com.andy.studentmanagement.domain.Student;
import com.andy.studentmanagement.domain.StudentRequest;

@Repository
public class StudentDAO {

	private final NamedParameterJdbcTemplate jdbcTemplate;
	private static final String STUDENT_ID = "studentId";

	public StudentDAO(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.jdbcTemplate = namedParameterJdbcTemplate;
	}

	private static final String FIND_ALL_STUDENTS = """
			SELECT id,
			       first_name,
			       last_name,
			       email
			FROM students
			ORDER BY last_name
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
			  AND course_id in (:courseIdList)
			""";

	private static final String FIND_COURSES_BY_STUDENT_ID = """
			SELECT c.id,
			            c.name,
			            c.description
			     FROM courses c
			     INNER JOIN student_courses sc
			             ON sc.course_id = c.id
			     WHERE sc.student_id = :studentId
			     ORDER BY c.id
			""";

	/**
	 * Find all the students 
	 * @return
	 */
	public List<Student> findAll() {

		 
		 List<Student> students = jdbcTemplate.query(
		            FIND_ALL_STUDENTS,
		            Map.of(),
		            new BeanPropertyRowMapper<>(Student.class)
		    );

		    students.forEach(this::loadCourses);

		    return students;
	}

	/**
	 * Find a student by student ID
	 * @param studentId
	 * @return
	 */
	public Student findById(Long studentId) {

		List<Student> students = jdbcTemplate.query(FIND_STUDENT_BY_ID, Map.of(STUDENT_ID, studentId),
				new BeanPropertyRowMapper<>(Student.class));

		if (students.isEmpty()) {
			return null;
		}

		Student student = students.get(0);
		loadCourses(student);

		return student;
	}

	/**
	 * Create a new student
	 * @param studentRequest
	 * @return
	 */
	public void create(StudentRequest studentRequest) {

		MapSqlParameterSource params = new MapSqlParameterSource().addValue("firstName", studentRequest.getFirstName())
				.addValue("lastName", studentRequest.getLastName()).addValue("email", studentRequest.getEmail());

		jdbcTemplate.update(INSERT_STUDENT, params);

	}

	/**
	 * 
	 * @param studentId
	 * @param studentRequest
	 */
	public void update(Long studentId, StudentRequest studentRequest) {

		MapSqlParameterSource params = new MapSqlParameterSource().addValue(STUDENT_ID, studentId)
				.addValue("firstName", studentRequest.getFirstName()).addValue("lastName", studentRequest.getLastName())
				.addValue("email", studentRequest.getEmail());

		jdbcTemplate.update(UPDATE_STUDENT, params);

	}

	/**
	 * 
	 * @param studentId
	 */
	public void delete(Long studentId) {

		//delete all the student enrolled courses first
		jdbcTemplate.update(DELETE_STUDENT_COURSES, Map.of(STUDENT_ID, studentId));
        //delete the student record
		jdbcTemplate.update(DELETE_STUDENT, Map.of(STUDENT_ID, studentId));
	}

	/**
	 * 
	 * @param studentId
	 * @param courseIdList
	 */
	public void enrollCourses(Long studentId, List<Long> courseIdList) {
		 SqlParameterSource[] batch = courseIdList.stream()
		            .map(courseId -> new MapSqlParameterSource()
		                    .addValue(STUDENT_ID, studentId)
		                    .addValue("courseId", courseId))
		            .toArray(SqlParameterSource[]::new);

		jdbcTemplate.batchUpdate(INSERT_STUDENT_COURSE, batch);

	}

	/**
	 * 
	 * @param studentId
	 * @param courseIdList
	 */
	public void removeCourses(Long studentId, List<Long> courseIdList) {

		jdbcTemplate.update(DELETE_STUDENT_COURSE, Map.of(STUDENT_ID, studentId, "courseIdList", courseIdList));
	}

	/**
	 * 
	 * @param student
	 */
	private void loadCourses(Student student) {

		List<Course> courses = jdbcTemplate.query(FIND_COURSES_BY_STUDENT_ID, Map.of(STUDENT_ID, student.getId()),
				new BeanPropertyRowMapper<>(Course.class));

		student.setCourses(courses);
	}

}
