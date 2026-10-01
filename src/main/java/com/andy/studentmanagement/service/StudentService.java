package com.andy.studentmanagement.service;

import java.util.List;

import com.andy.studentmanagement.domain.Student;
import com.andy.studentmanagement.domain.StudentRequest;


public interface StudentService {

    Student createStudent(StudentRequest studentRequest);

    void deleteStudent(Long studentId);

    Student enrollCourse(Long studentId, Long courseId);

    List<Student> getAllStudents();

    Student getStudent(Long studentId);

    Student removeCourse(Long studentId, Long courseId);

    Student updateStudent(Long studentId, StudentRequest studentRequest);
}
