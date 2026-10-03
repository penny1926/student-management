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

import com.andy.studentmanagement.domain.Course;
import com.andy.studentmanagement.domain.CourseRequest;
import com.andy.studentmanagement.service.CourseService;

@ExtendWith(MockitoExtension.class)
class CourseControllerTest {

    @Mock
    private CourseService courseService;

    @Mock
    private CourseRequest request;

    @Mock
    private Course course;

    private CourseController controller;

    @BeforeEach
    void setUp() {
        controller = new CourseController(courseService);
    }

    @Test
    void getAllCourses_shouldReturnOkWithCourses() {
        List<Course> courses = List.of(course);
        when(courseService.getAllCourses()).thenReturn(courses);

        ResponseEntity<List<Course>> response = controller.getAllCourses();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(courses, response.getBody());
    }

    @Test
    void createCourse_shouldReturnNoContent() {
        ResponseEntity<Void> response = controller.createCourse(request);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(courseService).createCourse(request);
    }

    @Test
    void getCourse_shouldReturnOkWithCourse() {
        when(courseService.getCourse(1L)).thenReturn(course);

        ResponseEntity<Course> response = controller.getCourse(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(course, response.getBody());
    }

    @Test
    void updateCourse_shouldReturnNoContent() {
        ResponseEntity<Void> response = controller.updateCourse(1L, request);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(courseService).updateCourse(1L, request);
    }

    @Test
    void deleteCourse_shouldReturnNoContent() {
        ResponseEntity<Void> response = controller.deleteCourse(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(courseService).deleteCourse(1L);
    }
}
