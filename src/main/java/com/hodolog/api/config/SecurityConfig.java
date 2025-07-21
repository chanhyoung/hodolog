package com.hodolog.api.config;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.hodolog.api.auth.jwt.JwtFilter;
import com.hodolog.api.auth.jwt.TokenProvider;
import com.hodolog.api.auth.jwt.handler.Http401Handler;
import com.hodolog.api.auth.jwt.handler.Http403Handler;
import com.hodolog.api.domain.User;
import com.hodolog.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity(debug = false)
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

	private final Http401Handler http401Handler;
	private final Http403Handler http403Handler;
	private final TokenProvider tokenProvider;

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring()
				.antMatchers(
						// "/courses/**",
						"/h2-console/**",
						"favicon.ico",
						"/js/**",
						"/css/**",
						"/fonts/**",
						"/img/**");
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.csrf(csrf -> csrf.disable())
				.cors(withDefaults())
				.exceptionHandling(handling -> handling
						.authenticationEntryPoint(http401Handler)
						.accessDeniedHandler(http403Handler))
				.sessionManagement(management -> management
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(requests -> requests
						.antMatchers("/auth/login", "/auth/signup", "/courses").permitAll()
						// .antMatchers("/courses/**").permitAll()
						.antMatchers("/api/user").hasRole("USER")
						.antMatchers("/api/admin").hasRole("ADMIN")
						.anyRequest().authenticated())
				.addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	UserDetailsService userDetailsService(UserRepository userRepository) {
		return new UserDetailsService() {
			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				User user = userRepository.findByEmail(username)
						.orElseThrow(() -> new UsernameNotFoundException(username + "을 찾을 수 없습니다."));

				log.info(">>>user={}", user);
				return new UserPrincipal(user);
			}
		};
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new SCryptPasswordEncoder();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		// configuration.addAllowedOrigin("*");
		configuration.addAllowedOriginPattern("*");
		configuration.addAllowedHeader("*");
		// configuration.addExposedHeader("X-AUTH-TOKEN");
		// configuration.addAllowedMethod("*");
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "PUT", "DELETE", "PATCH"));
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
