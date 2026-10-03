package com.andy.studentmanagement.presentation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.andy.studentmanagement.domain.Student;
import com.andy.studentmanagement.domain.StudentRequest;
import com.andy.studentmanagement.service.StudentService;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @Mock
    private StudentRequest request;

    @Mock
    private Student student;

    private StudentController controller;

    @BeforeEach
    void setUp() {
        controller = new StudentController(studentService);
    }

    @Test
    void createStudent_shouldReturnNoContent() {
        ResponseEntity<Void> response = controller.createStudent(request);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(studentService).createStudent(request);
    }

    @Test
    void deleteStudent_shouldReturnNoContent() {
        ResponseEntity<Void> response = controller.deleteStudent(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(studentService).deleteStudent(1L);
    }

    @Test
    void enrollCourses_shouldReturnNoContent() {
        List<Long> courseIds = List.of(10L, 20L);

        ResponseEntity<Void> response =
                controller.enrollCourses(1L, courseIds);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(studentService).enrollCourses(1L, courseIds);
    }

    @Test
    void getAllStudents_shouldReturnOkWithStudents() {
        List<Student> students = List.of(student);
        when(studentService.getAllStudents()).thenReturn(students);

        ResponseEntity<List<Student>> response = controller.getAllStudents();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(students, response.getBody());
    }

    @Test
    void getStudent_shouldReturnOkWithStudent() {
        when(studentService.getStudent(1L)).thenReturn(student);

        ResponseEntity<Student> response = controller.getStudent(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(student, response.getBody());
    }

    @Test
    void unenrollCourses_shouldReturnNoContent() {
        List<Long> courseIds = List.of(10L, 20L);

        ResponseEntity<Void> response =
                controller.unenrollCourses(1L, courseIds);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(studentService).unenrollCourses(1L, courseIds);
    }

    @Test
    void updateStudent_shouldReturnNoContent() {
        ResponseEntity<Void> response =
                controller.updateStudent(1L, request);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(studentService).updateStudent(1L, request);
    }
}
