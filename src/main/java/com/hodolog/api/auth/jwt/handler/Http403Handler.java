package com.hodolog.api.auth.jwt.handler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hodolog.api.response.ErrorResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class Http403Handler implements AccessDeniedHandler {
	private final ObjectMapper objectMapper;

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException {
		ErrorResponse errorResponse = ErrorResponse.builder()
				.code("403")
				.message("접근할 수 없습니다.")
				.build();
		
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		objectMapper.writeValue(response.getWriter(), errorResponse);		
	}

}
