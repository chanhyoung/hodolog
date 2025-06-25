package com.hodolog.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class MainController {
	
	@GetMapping("/")
	public String main() {
		return "메인 페이지입니다.";
	}
	
//	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/user")
	public String user() {
		return "사용자 페이지입니다.";
	}
	
//	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/admin")
	public String admin() {
		return "admin 페이지입니다.";
	}
}
