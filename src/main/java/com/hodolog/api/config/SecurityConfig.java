package com.hodolog.api.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.hodolog.api.auth.jwt.JwtFilter;
import com.hodolog.api.auth.jwt.TokenProvider;
import com.hodolog.api.auth.jwt.handler.Http401Handler;
import com.hodolog.api.auth.jwt.handler.Http403Handler;
import com.hodolog.api.domain.Users;
import com.hodolog.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity(debug = false)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private final Http401Handler http401Handler;
	private final Http403Handler http403Handler;
	private final TokenProvider tokenProvider;
	
	@Override
	public void configure(WebSecurity web) {
		web
			.ignoring()
			.antMatchers(
					"/courses/**",
					"/h2-console/**",
					"favicon.ico",
					"/js/**",
					"/css/**",
					"/fonts/**",
					"/img/**"
			);
			
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(withDefaults())
                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint(http401Handler)
                        .accessDeniedHandler(http403Handler))
                .sessionManagement(management -> management
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeRequests(requests -> requests
                        .antMatchers("/auth/login").permitAll()
                        .antMatchers("/auth/signup").permitAll()
                        .antMatchers("/courses").permitAll()
//				.antMatchers("/courses/**").permitAll()
                        .antMatchers("/api/user").hasRole("USER")
                        .antMatchers("/api/admin").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
			;
	}
	
	@Bean
	UserDetailsService userDetailsService(UserRepository userRepository) {
		return new UserDetailsService() {
			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				Users user = userRepository.findByEmail(username)
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
		
//		configuration.addAllowedOrigin("*");
		configuration.addAllowedOriginPattern("*");
		configuration.addAllowedHeader("*");
		configuration.addAllowedMethod("*");
		configuration.setAllowCredentials(true);
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
