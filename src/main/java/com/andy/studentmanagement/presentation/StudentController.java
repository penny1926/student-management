package com.andy.studentmanagement.presentation;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.andy.studentmanagement.api.StudentsApi;
import com.andy.studentmanagement.domain.Student;
import com.andy.studentmanagement.domain.StudentRequest;
import com.andy.studentmanagement.service.StudentService;

import jakarta.validation.Validator;


@RestController
@Validated
public class StudentController implements StudentsApi{

	private static final Logger LOG =
            LoggerFactory.getLogger(StudentController.class);

	private final StudentService studentService;

	private final Validator validator;

	@Autowired
	public StudentController(StudentService studentService, Validator validator) {
		this.studentService = studentService;		
		this.validator = validator;
	}


    @Override
    public ResponseEntity<Student> createStudent(StudentRequest studentRequest) {
        Student student = studentService.createStudent(studentRequest);

        return ResponseEntity
                .status(201)
                .body(student);
    }

    @Override
    public ResponseEntity<Void> deleteStudent(Long studentId) {
        studentService.deleteStudent(studentId);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Student> enrollCourse(
            Long studentId,
            Long courseId) {

        Student student = studentService.enrollCourse(studentId, courseId);

        return ResponseEntity.ok(student);
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
    public ResponseEntity<Student> removeCourse(
            Long studentId,
            Long courseId) {

        Student student = studentService.removeCourse(studentId, courseId);

        return ResponseEntity.ok(student);
    }

    @Override
    public ResponseEntity<Student> updateStudent(
            Long studentId,
            StudentRequest studentRequest) {

        Student student =
                studentService.updateStudent(studentId, studentRequest);

        return ResponseEntity.ok(student);
    }
	
}