package com.hodolog.api.auth;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.hodolog.api.domain.User;
import com.hodolog.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserAuthUtil {
	private final UserRepository userRepository;
	
    public Optional<User> getLoginUser() {
    	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
    	UserDetails userDetails = (UserDetails)principal; 
    	String email = userDetails.getUsername();
    	
    	return userRepository.findByEmail(email);
    }
}
