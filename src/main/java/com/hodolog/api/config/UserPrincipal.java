package com.hodolog.api.config;

import java.util.Arrays;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.ToString;

@ToString
public class UserPrincipal extends User {
	private final Long userId;
	
	public UserPrincipal(com.hodolog.api.domain.User user) {
		super(
				user.getEmail(),
				user.getPassword(),
				Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"))
		);
		
		this.userId = user.getId();
	}
	
	public Long getUserId() {
		return userId;
	}
}
