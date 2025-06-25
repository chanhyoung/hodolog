package com.hodolog.api.request;

import lombok.Builder;
import lombok.Data;

@Data
public class Login {
	private String email;
	private String password;
	
	@Builder
	public Login(String email, String password) {
		this.email = email;
		this.password = password;
	}
}
