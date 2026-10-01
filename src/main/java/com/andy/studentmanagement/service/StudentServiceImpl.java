package com.andy.studentmanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.andy.studentmanagement.domain.Student;
import com.andy.studentmanagement.domain.StudentNotFoundException;
import com.andy.studentmanagement.domain.StudentRequest;
import com.andy.studentmanagement.persistence.StudentDAO;

@Service
public class StudentServiceImpl implements StudentService{
/**
 * Even though this project uses JSON server for CRUD operations instead of a real DB, Spring transaction annotation is still added here
 * to show that in real project, service layer methods should have proper transaction handling
 */
	
	private final StudentDAO  studentDAO;
	
	@Autowired
	public StudentServiceImpl(StudentDAO  studentDAO) {
		this.studentDAO = studentDAO;
	}
	
	 @Override
	    @Transactional(
	        rollbackFor = Exception.class,
	        propagation = Propagation.REQUIRED
	    )
	    public Student createStudent(StudentRequest studentRequest) {

	        validateStudentRequest(studentRequest);

	        return studentDAO.create(studentRequest);
	    }

	    @Override
	    @Transactional(
	        rollbackFor = Exception.class,
	        propagation = Propagation.REQUIRED
	    )
	    public void deleteStudent(Long studentId) {

	        Student student = studentDAO.findById(studentId);

	        if (student == null) {
	            throw new StudentNotFoundException(studentId);
	        }

	        studentDAO.delete(studentId);
	    }

	    @Override
	    @Transactional(
	        rollbackFor = Exception.class,
	        propagation = Propagation.REQUIRED
	    )
	    public Student enrollCourse(Long studentId, Long courseId) {

	        Student student = studentDAO.findById(studentId);

	        if (student == null) {
	            throw new StudentNotFoundException(studentId);
	        }

	        studentDAO.enrollCourse(studentId, courseId);

	        return studentDAO.findById(studentId);
	    }

	    @Override
	    @Transactional(
	        readOnly = true,
	        propagation = Propagation.REQUIRED
	    )
	    public List<Student> getAllStudents() {

	        return studentDAO.findAll();
	    }

	    @Override
	    @Transactional(
	        readOnly = true,
	        propagation = Propagation.REQUIRED
	    )
	    public Student getStudent(Long studentId) {

	        Student student = studentDAO.findById(studentId);

	        if (student == null) {
	            throw new StudentNotFoundException(studentId);
	        }

	        return student;
	    }

	    @Override
	    @Transactional(
	        rollbackFor = Exception.class,
	        propagation = Propagation.REQUIRED
	    )
	    public Student removeCourse(Long studentId, Long courseId) {

	        Student student = studentDAO.findById(studentId);

	        if (student == null) {
	            throw new StudentNotFoundException(studentId);
	        }

	        studentDAO.removeCourse(studentId, courseId);

	        return studentDAO.findById(studentId);
	    }

	    @Override
	    @Transactional(
	        rollbackFor = Exception.class,
	        propagation = Propagation.REQUIRED
	    )
	    public Student updateStudent(
	            Long studentId,
	            StudentRequest studentRequest) {

	        Student existingStudent = studentDAO.findById(studentId);

	        if (existingStudent == null) {
	            throw new StudentNotFoundException(studentId);
	        }

	        validateStudentRequest(studentRequest);

	        return studentDAO.update(studentId, studentRequest);
	    }

	    private void validateStudentRequest(
	            StudentRequest studentRequest) {

	        if (studentRequest == null) {
	            throw new IllegalArgumentException(
	                    "Student request cannot be null");
	        }

	        if (studentRequest.getFirstName() == null ||
	                studentRequest.getFirstName().isBlank()) {

	            throw new IllegalArgumentException(
	                    "First name is required");
	        }

	        if (studentRequest.getLastName() == null ||
	                studentRequest.getLastName().isBlank()) {

	            throw new IllegalArgumentException(
	                    "Last name is required");
	        }

	        if (studentRequest.getEmail() == null ||
	                studentRequest.getEmail().isBlank()) {

	            throw new IllegalArgumentException(
	                    "Email is required");
	        }
	    }

}
