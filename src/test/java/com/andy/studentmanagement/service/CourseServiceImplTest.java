package com.andy.studentmanagement.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.andy.studentmanagement.domain.Course;
import com.andy.studentmanagement.domain.CourseNotFoundException;
import com.andy.studentmanagement.domain.CourseRequest;
import com.andy.studentmanagement.persistence.CourseDAO;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {

    @Mock
    private CourseDAO courseDAO;

    @Mock
    private CourseRequest request;

    @Mock
    private Course course;

    private CourseServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new CourseServiceImpl(courseDAO);
    }

    @Test
    void createCourse_shouldDelegateToDao() {
        service.createCourse(request);

        verify(courseDAO).create(request);
    }

    @Test
    void getAllCourses_shouldReturnCoursesFromDao() {
        List<Course> courses = List.of(course);
        when(courseDAO.findAll()).thenReturn(courses);

        assertSame(courses, service.getAllCourses());

        verify(courseDAO).findAll();
    }

    @Test
    void getCourse_shouldReturnCourseWhenFound() {
        when(courseDAO.findById(1L)).thenReturn(course);

        assertSame(course, service.getCourse(1L));

        verify(courseDAO).findById(1L);
    }

    @Test
    void getCourse_shouldThrowWhenNotFound() {
        when(courseDAO.findById(999L)).thenReturn(null);

        assertThrows(CourseNotFoundException.class,
                () -> service.getCourse(999L));

        verify(courseDAO, never()).update(anyLong(), any());
    }

    @Test
    void deleteCourse_shouldDeleteWhenFound() {
        when(courseDAO.findById(1L)).thenReturn(course);

        service.deleteCourse(1L);

        verify(courseDAO).delete(1L);
    }

    @Test
    void deleteCourse_shouldThrowWhenNotFound() {
        when(courseDAO.findById(999L)).thenReturn(null);

        assertThrows(CourseNotFoundException.class,
                () -> service.deleteCourse(999L));

        verify(courseDAO, never()).delete(anyLong());
    }

    @Test
    void updateCourse_shouldUpdateWhenFound() {
        when(courseDAO.findById(1L)).thenReturn(course);

        service.updateCourse(1L, request);

        verify(courseDAO).update(1L, request);
    }

    @Test
    void updateCourse_shouldThrowWhenNotFound() {
        when(courseDAO.findById(999L)).thenReturn(null);

        assertThrows(CourseNotFoundException.class,
                () -> service.updateCourse(999L, request));

        verify(courseDAO, never()).update(anyLong(), any());
    }
}
