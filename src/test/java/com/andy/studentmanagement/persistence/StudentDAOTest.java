package com.andy.studentmanagement.persistence;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.andy.studentmanagement.domain.Course;
import com.andy.studentmanagement.domain.Student;
import com.andy.studentmanagement.domain.StudentRequest;

@ExtendWith(MockitoExtension.class)
class StudentDAOTest {

    @Mock
    private NamedParameterJdbcTemplate jdbcTemplate;

    private StudentDAO dao;

    @BeforeEach
    void setUp() {
        dao = new StudentDAO(jdbcTemplate);
    }

    @Test
    void findAll_shouldReturnStudentsAndLoadFullCourses() {
        Student student = mock(Student.class);
        Course course = mock(Course.class);

        when(student.getId()).thenReturn(1L);

        when(jdbcTemplate.query(anyString(), anyMap(), any(BeanPropertyRowMapper.class)))
                .thenReturn(List.of(student))
                .thenReturn(List.of(course));

        List<Student> result = dao.findAll();

        assertEquals(1, result.size());
        assertSame(student, result.get(0));
        verify(student).setCourses(List.of(course));
    }

    @Test
    void findById_shouldReturnStudentAndLoadFullCourses() {
        Student student = mock(Student.class);
        Course course = mock(Course.class);

        when(student.getId()).thenReturn(1L);

        when(jdbcTemplate.query(anyString(), anyMap(), any(BeanPropertyRowMapper.class)))
                .thenReturn(List.of(student))
                .thenReturn(List.of(course));

        Student result = dao.findById(1L);

        assertSame(student, result);
        verify(student).setCourses(List.of(course));
    }

    @Test
    void findById_shouldReturnNullWhenStudentDoesNotExist() {
        when(jdbcTemplate.query(anyString(), anyMap(), any(BeanPropertyRowMapper.class)))
                .thenReturn(List.of());

        assertNull(dao.findById(999L));
    }

    @Test
    void create_shouldInsertStudent() {
        StudentRequest request = mock(StudentRequest.class);
        when(request.getFirstName()).thenReturn("John");
        when(request.getLastName()).thenReturn("Smith");
        when(request.getEmail()).thenReturn("john@example.com");

        dao.create(request);

        verify(jdbcTemplate).update(
                anyString(),
                argThat((MapSqlParameterSource params) ->
                        "John".equals(params.getValue("firstName"))
                        && "Smith".equals(params.getValue("lastName"))
                        && "john@example.com".equals(params.getValue("email")))
        );
    }

    @Test
    void update_shouldUpdateStudent() {
        StudentRequest request = mock(StudentRequest.class);
        when(request.getFirstName()).thenReturn("Jane");
        when(request.getLastName()).thenReturn("Doe");
        when(request.getEmail()).thenReturn("jane@example.com");

        dao.update(7L, request);

        verify(jdbcTemplate).update(
                anyString(),
                argThat((MapSqlParameterSource params) ->
                        Long.valueOf(7L).equals(params.getValue("studentId"))
                        && "Jane".equals(params.getValue("firstName"))
                        && "Doe".equals(params.getValue("lastName"))
                        && "jane@example.com".equals(params.getValue("email")))
        );
    }

    @Test
    void delete_shouldDeleteEnrollmentsThenStudent() {
        dao.delete(7L);

        var inOrder = inOrder(jdbcTemplate);
        inOrder.verify(jdbcTemplate).update(anyString(), eq(Map.of("studentId", 7L)));
        inOrder.verify(jdbcTemplate).update(anyString(), eq(Map.of("studentId", 7L)));
    }

    @Test
    void enrollCourses_shouldBatchInsertAllCourses() {
        dao.enrollCourses(1L, List.of(10L, 20L, 30L));

        verify(jdbcTemplate).batchUpdate(
                anyString(),
                argThat((SqlParameterSource[] batch) -> batch.length == 3)
        );
    }

    @Test
    void removeCourses_shouldDeleteSpecifiedCourses() {
        dao.removeCourses(1L, List.of(10L, 20L));

        verify(jdbcTemplate).update(anyString(),
                eq(Map.of("studentId", 1L, "courseIdList", List.of(10L, 20L))));
    }
}
