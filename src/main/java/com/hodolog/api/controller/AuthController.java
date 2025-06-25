package com.hodolog.api.controller;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hodolog.api.auth.jwt.JwtFilter;
import com.hodolog.api.auth.jwt.TokenProvider;
import com.hodolog.api.request.Login;
import com.hodolog.api.request.Signup;
import com.hodolog.api.response.SessionResponse;
import com.hodolog.api.service.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {
	private final AuthService authService;
	private final TokenProvider tokenProvider;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	
	@PostMapping("/auth/login")
	public SessionResponse authorize(@Valid @RequestBody Login login) {
		UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword());
		
		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String jwt = tokenProvider.createToken(authentication);
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
		
		return new SessionResponse(jwt);
	}
	
	@PostMapping("/auth/signup")
	public void signup(@RequestBody Signup signup) {
		authService.signup(signup);
	}
}
