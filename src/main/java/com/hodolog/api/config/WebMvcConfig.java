package com.hodolog.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
//@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
//	private final AppConfig appConfig;
	
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
        		.allowedOrigins("*")
        		.exposedHeaders("X-AUTH-TOKEN")
//        		.allowedOrigins("http://localhost:5173")
                .allowedOrigins("http://localhost:3000");
    }
}
