package com.hodolog.api.exception;

public class UserNotFound extends HodologException {
	private static final String MESSAGE = "존재하지 않는 사용자입니다.";
	
	public UserNotFound() {
		super(MESSAGE);
	}
	
	@Override
	public int getStatusCode() {
		return 404;
	}
}
