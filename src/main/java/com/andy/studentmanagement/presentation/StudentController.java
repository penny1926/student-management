package com.andy.studentmanagement.presentation;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.andy.studentmanagement.api.StudentsApi;
import com.andy.studentmanagement.domain.Student;
import com.andy.studentmanagement.domain.StudentRequest;
import com.andy.studentmanagement.security.Authorizer;
import com.andy.studentmanagement.service.StudentService;



/**
 * Input validation constraints are defined in the OpenAPI YAML and applied through
 * the generated StudentsApi interface.
 */
@Authorizer(role = "ADMIN")
@RestController
public class StudentController implements StudentsApi{

	private static final Logger LOG =
            LoggerFactory.getLogger(StudentController.class);

	private final StudentService studentService;

	@Autowired
	public StudentController(StudentService studentService) {
		this.studentService = studentService;		
	}

    @Override
    public ResponseEntity<Void> createStudent(StudentRequest studentRequest) {
        studentService.createStudent(studentRequest);
        LOG.info("Student with first name {}, last name {} is created.", studentRequest.getFirstName(),studentRequest.getLastName());
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> deleteStudent(Long studentId) {
        studentService.deleteStudent(studentId);
        LOG.info("Student is deleted with id {}.",studentId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> enrollCourses(
            Long studentId,
            List<Long> courseIdList) {

         studentService.enrollCourses(studentId, courseIdList);
         LOG.info("Student with id {} has successfully enrolled in courses {} .",studentId,courseIdList);
         return ResponseEntity.noContent().build();
    }
    

    @Override
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(
                studentService.getAllStudents()
        );
    }

    @Override
    public ResponseEntity<Student> getStudent(Long studentId) {
        Student student = studentService.getStudent(studentId);
        return ResponseEntity.ok(student);
    }

    @Override
    public ResponseEntity<Void> unenrollCourses(
            Long studentId,
            List<Long> courseIdList) {

        studentService.unenrollCourses(studentId, courseIdList);
        LOG.info("Student with id {} has successfully unenrolled in courses {} .",studentId,courseIdList);
        return ResponseEntity.noContent().build();
    }

	@Override
	public ResponseEntity<Void> updateStudent(Long studentId, StudentRequest studentRequest) {

		studentService.updateStudent(studentId, studentRequest);
		LOG.info("Student with id {} has successfully been updated .", studentId);
		return ResponseEntity.noContent().build();
	}
	
}