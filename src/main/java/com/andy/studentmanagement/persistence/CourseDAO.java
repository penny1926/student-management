package com.andy.studentmanagement.persistence;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.andy.studentmanagement.domain.Course;
import com.andy.studentmanagement.domain.CourseRequest;

@Repository
public class CourseDAO {

	private final NamedParameterJdbcTemplate jdbcTemplate;

	private static final String COURSE_ID = "courseId";

	public CourseDAO(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.jdbcTemplate = namedParameterJdbcTemplate;
	}

	private static final String FIND_ALL_COURSES = """
			SELECT id,
			       name,
			       description
			FROM courses
			ORDER BY name
			""";

	private static final String FIND_COURSE_BY_ID = """
			SELECT id,
			       name,
			       description
			FROM courses
			WHERE id = :courseId
			""";

	private static final String INSERT_COURSE = """
			INSERT INTO courses (
			    name,
			    description
			)
			VALUES (
			    :name,
			    :description
			)
			""";

	private static final String UPDATE_COURSE = """
			UPDATE courses
			SET name = :name,
			    description = :description
			WHERE id = :courseId
			""";

	private static final String DELETE_COURSE_ENROLLMENTS = """
			DELETE FROM student_courses
			WHERE course_id = :courseId
			""";

	private static final String DELETE_COURSE = """
			DELETE FROM courses
			WHERE id = :courseId
			""";

	/**
	 * Find all the courses
	 *
	 * @return
	 */
	public List<Course> findAll() {

		return jdbcTemplate.query(FIND_ALL_COURSES, Map.of(), new BeanPropertyRowMapper<>(Course.class));
	}

	/**
	 * Find a course by course ID
	 *
	 * @param courseId
	 * @return
	 */
	public Course findById(Long courseId) {

		List<Course> courses = jdbcTemplate.query(FIND_COURSE_BY_ID, Map.of(COURSE_ID, courseId),
				new BeanPropertyRowMapper<>(Course.class));

		if (courses.isEmpty()) {
			return null;
		}

		return courses.get(0);
	}

	/**
	 * Create a new course
	 *
	 * @param courseRequest
	 * @return
	 */
	public void create(CourseRequest courseRequest) {

		MapSqlParameterSource params = new MapSqlParameterSource().addValue("name", courseRequest.getName())
				.addValue("description", courseRequest.getDescription());

		jdbcTemplate.update(INSERT_COURSE, params);
	}

	/**
	 * Update a course
	 *
	 * @param courseId
	 * @param courseRequest
	 */
	public void update(Long courseId, CourseRequest courseRequest) {

		MapSqlParameterSource params = new MapSqlParameterSource().addValue(COURSE_ID, courseId)
				.addValue("name", courseRequest.getName()).addValue("description", courseRequest.getDescription());

		jdbcTemplate.update(UPDATE_COURSE, params);
	}

	/**
	 * Delete a course and its student enrollments
	 *
	 * @param courseId
	 */
	public void delete(Long courseId) {

		// Delete all student enrollments for this course first
		jdbcTemplate.update(DELETE_COURSE_ENROLLMENTS, Map.of(COURSE_ID, courseId));

		// Delete the course record
		jdbcTemplate.update(DELETE_COURSE, Map.of(COURSE_ID, courseId));
	}
}