package com.andy.studentmanagement.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.andy.studentmanagement.domain.Student;
import com.andy.studentmanagement.domain.StudentNotFoundException;
import com.andy.studentmanagement.domain.StudentRequest;
import com.andy.studentmanagement.persistence.StudentDAO;

@Service
public class StudentServiceImpl implements StudentService {

	private static final Logger LOG = LoggerFactory.getLogger(StudentServiceImpl.class);

	private final StudentDAO studentDAO;

	@Autowired
	public StudentServiceImpl(StudentDAO studentDAO) {
		this.studentDAO = studentDAO;
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void createStudent(StudentRequest studentRequest) {
		LOG.info("Creating new student");
		//we could add XSS validations here to make sure studenRequest doesn't contain XSS content
		studentDAO.create(studentRequest);
		LOG.info("Student created successfully");
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void deleteStudent(Long studentId) {

		LOG.info("Deleting student with id: {}", studentId);

        Student student = studentDAO.findById(studentId);

        if (student == null) {
            LOG.warn("Cannot delete student. Student not found: {}", studentId);
            throw new StudentNotFoundException(studentId);
        }

        studentDAO.delete(studentId);

        LOG.info("Student deleted successfully: {}", studentId);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void updateStudent(Long studentId, StudentRequest studentRequest) {

		LOG.info("Updating student with id: {}", studentId);

        Student existingStudent = studentDAO.findById(studentId);

        if (existingStudent == null) {
            LOG.warn("Cannot update student. Student not found: {}", studentId);
            throw new StudentNotFoundException(studentId);
        }

        studentDAO.update(studentId, studentRequest);

        LOG.info("Student updated successfully: {}", studentId);
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<Student> getAllStudents() {

		LOG.info("Retrieving all students");

        List<Student> students = studentDAO.findAll();

        LOG.info("Retrieved {} student(s)", students.size());

        return students;
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public Student getStudent(Long studentId) {

		LOG.info("Retrieving student with id: {}", studentId);

        Student student = studentDAO.findById(studentId);

        if (student == null) {
            LOG.warn("Student not found: {}", studentId);
            throw new StudentNotFoundException(studentId);
        }

        LOG.info("Student retrieved successfully: {}", studentId);

        return student;
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void enrollCourses(Long studentId, List<Long> courseIdList) {

		LOG.info("Enrolling student {} in {} course(s)",
                studentId,
                courseIdList == null ? 0 : courseIdList.size());

        Student student = studentDAO.findById(studentId);

        if (student == null) {
            LOG.warn("Cannot enroll courses. Student not found: {}", studentId);
            throw new StudentNotFoundException(studentId);
        }

        studentDAO.enrollCourses(studentId, courseIdList);

        LOG.info("Successfully enrolled student {} in course(s): {}",
                studentId,
                courseIdList);

	}


	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void unenrollCourses(Long studentId, List<Long> courseIdList) {

		LOG.info("Unenrolling student {} from {} course(s)",
                studentId,
                courseIdList == null ? 0 : courseIdList.size());

        Student student = studentDAO.findById(studentId);

        if (student == null) {
            LOG.warn("Cannot unenroll courses. Student not found: {}", studentId);
            throw new StudentNotFoundException(studentId);
        }

        studentDAO.removeCourses(studentId, courseIdList);

        LOG.info("Successfully unenrolled student {} from course(s): {}",
                studentId,
                courseIdList);

	}

}
