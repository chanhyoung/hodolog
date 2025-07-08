package com.hodolog.api.service;

import org.springframework.stereotype.Service;

import com.hodolog.api.crypto.PasswordEncoder;
import com.hodolog.api.domain.User;
import com.hodolog.api.exception.AlreadyExistsEmailException;
import com.hodolog.api.repository.UserRepository;
import com.hodolog.api.request.Signup;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
	private final UserRepository userRepository;
	
	public void signup(Signup signup) {
		boolean isPresent = userRepository.findByEmail(signup.getEmail())
				.isPresent();
		
		if (isPresent) {
			throw new AlreadyExistsEmailException();
		}
		
		String encryptedPassword = new PasswordEncoder().encrypt(signup.getPassword());
		
		User user = User.builder()
				.email(signup.getEmail())
				.name(signup.getName())
				.password(encryptedPassword)
				.build();
		
		userRepository.save(user);
	}
}
