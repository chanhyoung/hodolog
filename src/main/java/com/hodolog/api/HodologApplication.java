package com.hodolog.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.hodolog.api.config.AppConfig;

@SpringBootApplication
@EnableConfigurationProperties(AppConfig.class)
public class HodologApplication {

	public static void main(String[] args) {
		SpringApplication.run(HodologApplication.class, args);
	}

}
