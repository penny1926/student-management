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
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.andy.studentmanagement.domain.Course;
import com.andy.studentmanagement.domain.CourseRequest;

@ExtendWith(MockitoExtension.class)
class CourseDAOTest {

    @Mock
    private NamedParameterJdbcTemplate jdbcTemplate;

    private CourseDAO dao;

    @BeforeEach
    void setUp() {
        dao = new CourseDAO(jdbcTemplate);
    }

    @Test
    void findAll_shouldReturnAllCourses() {
        Course course = mock(Course.class);
        when(jdbcTemplate.query(anyString(), anyMap(), any(BeanPropertyRowMapper.class)))
                .thenReturn(List.of(course));

        List<Course> result = dao.findAll();

        assertEquals(1, result.size());
        assertSame(course, result.get(0));
        verify(jdbcTemplate).query(anyString(), eq(Map.of()), any(BeanPropertyRowMapper.class));
    }

    @Test
    void findById_shouldReturnCourseWhenFound() {
        Course course = mock(Course.class);
        when(jdbcTemplate.query(anyString(), anyMap(), any(BeanPropertyRowMapper.class)))
                .thenReturn(List.of(course));

        Course result = dao.findById(10L);

        assertSame(course, result);
        verify(jdbcTemplate).query(anyString(), eq(Map.of("courseId", 10L)),
                any(BeanPropertyRowMapper.class));
    }

    @Test
    void findById_shouldReturnNullWhenNotFound() {
        when(jdbcTemplate.query(anyString(), anyMap(), any(BeanPropertyRowMapper.class)))
                .thenReturn(List.of());

        assertNull(dao.findById(999L));
    }

    @Test
    void create_shouldInsertCourse() {
        CourseRequest request = mock(CourseRequest.class);
        when(request.getName()).thenReturn("Angular");
        when(request.getDescription()).thenReturn("Angular fundamentals");

        dao.create(request);

        verify(jdbcTemplate).update(
                anyString(),
                argThat((MapSqlParameterSource params) ->
                        "Angular".equals(params.getValue("name"))
                        && "Angular fundamentals".equals(params.getValue("description")))
        );
    }

    @Test
    void update_shouldUpdateCourse() {
        CourseRequest request = mock(CourseRequest.class);
        when(request.getName()).thenReturn("Java");
        when(request.getDescription()).thenReturn("Java fundamentals");

        dao.update(5L, request);

        verify(jdbcTemplate).update(
                anyString(),
                argThat((MapSqlParameterSource params) ->
                        Long.valueOf(5L).equals(params.getValue("courseId"))
                        && "Java".equals(params.getValue("name"))
                        && "Java fundamentals".equals(params.getValue("description")))
        );
    }

    @Test
    void delete_shouldDeleteEnrollmentsThenCourse() {
        dao.delete(5L);

        var inOrder = inOrder(jdbcTemplate);
        inOrder.verify(jdbcTemplate).update(anyString(), eq(Map.of("courseId", 5L)));
        inOrder.verify(jdbcTemplate).update(anyString(), eq(Map.of("courseId", 5L)));
    }
}
