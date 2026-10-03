package com.andy.studentmanagement.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.andy.studentmanagement.domain.Student;
import com.andy.studentmanagement.domain.StudentNotFoundException;
import com.andy.studentmanagement.domain.StudentRequest;
import com.andy.studentmanagement.persistence.StudentDAO;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @Mock
    private StudentDAO studentDAO;

    @Mock
    private StudentRequest request;

    @Mock
    private Student student;

    private StudentServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new StudentServiceImpl(studentDAO);
    }

    @Test
    void createStudent_shouldDelegateToDao() {
        service.createStudent(request);

        verify(studentDAO).create(request);
    }

    @Test
    void getAllStudents_shouldReturnStudentsFromDao() {
        List<Student> students = List.of(student);
        when(studentDAO.findAll()).thenReturn(students);

        assertSame(students, service.getAllStudents());

        verify(studentDAO).findAll();
    }

    @Test
    void getStudent_shouldReturnStudentWhenFound() {
        when(studentDAO.findById(1L)).thenReturn(student);

        assertSame(student, service.getStudent(1L));

        verify(studentDAO).findById(1L);
    }

    @Test
    void getStudent_shouldThrowWhenNotFound() {
        when(studentDAO.findById(999L)).thenReturn(null);

        assertThrows(StudentNotFoundException.class,
                () -> service.getStudent(999L));
    }

    @Test
    void deleteStudent_shouldDeleteWhenFound() {
        when(studentDAO.findById(1L)).thenReturn(student);

        service.deleteStudent(1L);

        verify(studentDAO).delete(1L);
    }

    @Test
    void deleteStudent_shouldThrowWhenNotFound() {
        when(studentDAO.findById(999L)).thenReturn(null);

        assertThrows(StudentNotFoundException.class,
                () -> service.deleteStudent(999L));

        verify(studentDAO, never()).delete(anyLong());
    }

    @Test
    void updateStudent_shouldUpdateWhenFound() {
        when(studentDAO.findById(1L)).thenReturn(student);

        service.updateStudent(1L, request);

        verify(studentDAO).update(1L, request);
    }

    @Test
    void updateStudent_shouldThrowWhenNotFound() {
        when(studentDAO.findById(999L)).thenReturn(null);

        assertThrows(StudentNotFoundException.class,
                () -> service.updateStudent(999L, request));

        verify(studentDAO, never()).update(anyLong(), any());
    }

    @Test
    void enrollCourses_shouldEnrollWhenStudentExists() {
        when(studentDAO.findById(1L)).thenReturn(student);

        List<Long> courseIds = List.of(10L, 20L);

        service.enrollCourses(1L, courseIds);

        verify(studentDAO).enrollCourses(1L, courseIds);
    }

    @Test
    void enrollCourses_shouldThrowWhenStudentDoesNotExist() {
        when(studentDAO.findById(999L)).thenReturn(null);

        List<Long> courseIds = List.of(10L);

        assertThrows(StudentNotFoundException.class,
                () -> service.enrollCourses(999L, courseIds));

        verify(studentDAO, never()).enrollCourses(anyLong(), anyList());
    }

    @Test
    void unenrollCourses_shouldRemoveCoursesWhenStudentExists() {
        when(studentDAO.findById(1L)).thenReturn(student);

        List<Long> courseIds = List.of(10L, 20L);

        service.unenrollCourses(1L, courseIds);

        verify(studentDAO).removeCourses(1L, courseIds);
    }

    @Test
    void unenrollCourses_shouldThrowWhenStudentDoesNotExist() {
        when(studentDAO.findById(999L)).thenReturn(null);

        List<Long> courseIds = List.of(10L);

        assertThrows(StudentNotFoundException.class,
                () -> service.unenrollCourses(999L, courseIds));

        verify(studentDAO, never()).removeCourses(anyLong(), anyList());
    }
}
