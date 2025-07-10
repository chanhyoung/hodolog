package com.hodolog.api.response;

import lombok.Getter;

@Getter
public class SessionResponse {
	private final Long userId;
	private final String accessToken;
	
	public SessionResponse(Long userId, String accessToken) {
		this.userId = userId;
		this.accessToken = accessToken;
	}
}
