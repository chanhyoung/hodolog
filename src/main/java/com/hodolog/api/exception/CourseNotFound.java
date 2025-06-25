package com.hodolog.api.exception;

public class CourseNotFound extends HodologException {
	private static final String MESSAGE = "존재하지 않는 강의입니다.";
	
	public CourseNotFound() {
		super(MESSAGE);
	}
	
	@Override
	public int getStatusCode() {
		return 404;
	}
}
