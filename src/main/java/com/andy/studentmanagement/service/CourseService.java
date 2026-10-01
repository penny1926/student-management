package com.andy.studentmanagement.service;

import java.util.List;

import com.andy.studentmanagement.domain.Course;
import com.andy.studentmanagement.domain.CourseRequest;

public interface CourseService {

    /**
     * Create a new course record
     *
     * @param courseRequest
     * @return
     */
    void createCourse(CourseRequest courseRequest);

    /**
     * Delete a course
     *
     * @param courseId
     */
    void deleteCourse(Long courseId);

    /**
     * Get all the courses
     *
     * @return
     */
    List<Course> getAllCourses();

    /**
     * Get one course by course ID
     *
     * @param courseId
     * @return
     */
    Course getCourse(Long courseId);

    /**
     * Update a course's information
     *
     * @param courseId
     * @param courseRequest
     */
    void updateCourse(Long courseId, CourseRequest courseRequest);
}
