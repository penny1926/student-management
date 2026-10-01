package com.andy.studentmanagement.service;

import java.util.List;

import com.andy.studentmanagement.domain.Student;
import com.andy.studentmanagement.domain.StudentRequest;

public interface StudentService {

	/**
	 * Create a new student record 
	 * @param studentRequest
	 * @return
	 */
	void createStudent(StudentRequest studentRequest);

	/**
	 * Delete a student and enrollment of courses
	 * @param studentId
	 */
	void deleteStudent(Long studentId);

	/**
	 * Enroll one or more courses for a given student 
	 * @param studentId
	 * @param courseIdList
	 * @return
	 */
	void enrollCourses(Long studentId, List<Long> courseIdList);

	/**
	 * Get all the students
	 * @return
	 */
	List<Student> getAllStudents();

	/**
	 * Get one student by student ID
	 * @param studentId
	 * @return
	 */
	Student getStudent(Long studentId);

	/**
	 * Remove course enrollments for a given student
	 * @param studentId
	 * @param courseIdList
	 * @return
	 */
	void unenrollCourses(Long studentId, List<Long> courseIdList);

	/**
	 * Update a student's info
	 * @param studentId
	 * @param studentRequest
	 * @return
	 */
	void updateStudent(Long studentId, StudentRequest studentRequest);
}
