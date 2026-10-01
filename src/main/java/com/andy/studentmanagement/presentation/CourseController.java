package com.andy.studentmanagement.presentation;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.andy.studentmanagement.api.CoursesApi;
import com.andy.studentmanagement.domain.Course;
import com.andy.studentmanagement.domain.CourseRequest;
import com.andy.studentmanagement.security.Authorizer;
import com.andy.studentmanagement.service.CourseService;



/**
 * Input validation constraints are defined in the OpenAPI YAML and applied through
 * the generated StudentsApi interface.
 */
@Authorizer(role = "ADMIN")
@RestController
public class CourseController implements CoursesApi{


	private final CourseService courseService;

	private static final Logger LOG =
            LoggerFactory.getLogger(CourseController.class);

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @Override
    public ResponseEntity<List<Course>> getAllCourses() {
        LOG.info("Getting all courses");

        List<Course> courses = courseService.getAllCourses();

        return ResponseEntity.ok(courses);
    }

    @Override
    public ResponseEntity<Void> createCourse(
            CourseRequest courseRequest) {

        LOG.info("Creating course: {}", courseRequest.getName());

         courseService.createCourse(courseRequest);

         return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Course> getCourse(
            Long courseId) {

        LOG.info("Getting course with id: {}", courseId);

        Course course = courseService.getCourse(courseId);

        return ResponseEntity.ok(course);
    }

    @Override
    public ResponseEntity<Void> updateCourse(
            Long courseId,
            CourseRequest courseRequest) {

        LOG.info("Updating course with id: {}", courseId);

        courseService.updateCourse(courseId, courseRequest);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> deleteCourse(
            Long courseId) {

        LOG.info("Deleting course with id: {}", courseId);

        courseService.deleteCourse(courseId);

        return ResponseEntity.noContent().build();
    }
	
}