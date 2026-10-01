package com.andy.studentmanagement.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.andy.studentmanagement.domain.Course;
import com.andy.studentmanagement.domain.CourseNotFoundException;
import com.andy.studentmanagement.domain.CourseRequest;
import com.andy.studentmanagement.persistence.CourseDAO;

@Service
public class CourseServiceImpl implements CourseService {

	private static final Logger LOG = LoggerFactory.getLogger(CourseServiceImpl.class);

	private final CourseDAO courseDAO;

	@Autowired
	public CourseServiceImpl(CourseDAO courseDAO) {
		this.courseDAO = courseDAO;
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void createCourse(CourseRequest courseRequest) {

		LOG.info("Creating course: {}", courseRequest.getName());

		 courseDAO.create(courseRequest);

		LOG.info("Successfully created course with name: {}",courseRequest.getName());

	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void deleteCourse(Long courseId) {

		LOG.info("Deleting course with id: {}", courseId);

		Course course = courseDAO.findById(courseId);

		if (course == null) {
			LOG.warn("Course not found: {}", courseId);
			throw new CourseNotFoundException(courseId);
		}

		courseDAO.delete(courseId);

		LOG.info("Successfully deleted course with id: {}", courseId);
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<Course> getAllCourses() {

		LOG.info("Getting all courses");

		List<Course> courses = courseDAO.findAll();

		LOG.info("Found {} course(s)", courses.size());

		return courses;
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public Course getCourse(Long courseId) {

		LOG.info("Getting course with id: {}", courseId);

		Course course = courseDAO.findById(courseId);

		if (course == null) {
			LOG.warn("Course not found: {}", courseId);
			throw new CourseNotFoundException(courseId);
		}

		return course;
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void updateCourse(Long courseId, CourseRequest courseRequest) {

		LOG.info("Updating course with id: {}", courseId);

		Course course = courseDAO.findById(courseId);

		if (course == null) {
			LOG.warn("Course not found: {}", courseId);
			throw new CourseNotFoundException(courseId);
		}

		courseDAO.update(courseId, courseRequest);

		LOG.info("Successfully updated course with id: {}", courseId);
	}

}
