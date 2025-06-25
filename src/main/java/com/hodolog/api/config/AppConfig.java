package com.hodolog.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "app")
public class AppConfig {
	private String jwtHeader;
	private String jwtKey;
	private Long jwtExpire;
}
