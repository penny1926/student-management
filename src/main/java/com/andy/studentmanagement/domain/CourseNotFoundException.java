package com.andy.studentmanagement.domain;

public class CourseNotFoundException extends RuntimeException {

   

	/**
	 * 
	 */
	private static final long serialVersionUID = -6824490558876500923L;

	public CourseNotFoundException(Long courseId) {
        super("course not found: " + courseId);
    }
}
