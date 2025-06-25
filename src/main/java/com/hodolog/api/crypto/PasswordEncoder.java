package com.hodolog.api.crypto;

import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

public class PasswordEncoder {
	private static final SCryptPasswordEncoder encoder = new SCryptPasswordEncoder();
	
	public String encrypt(String rawPassword) {
		return encoder.encode(rawPassword);
	}
	
	public boolean matches(String rawPassword, String encryptedPassword) {
		return encoder.matches(rawPassword, encryptedPassword);
	}
}
