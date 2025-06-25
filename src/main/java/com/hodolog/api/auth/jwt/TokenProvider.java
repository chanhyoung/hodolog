package com.hodolog.api.auth.jwt;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.hodolog.api.config.AppConfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TokenProvider implements InitializingBean {
	@Autowired private AppConfig appConfig;
	private static final String AUTHORITIES_KEY = "auth";
//	private final String secret;
//	private final long tokenValidityInMilliseconds;
	private Key key;
	
	@Override	
	public void afterPropertiesSet() throws Exception {
		byte[] keyBytes = Decoders.BASE64.decode(appConfig.getJwtKey());
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}

	public String createToken(Authentication authentication) {
//		String authorities = authentication.getAuthorities().stream()
//				.map(GrantedAuthority::getAuthority)
//				.collect(Collectors.joining(","));
		String authorities = "";
		
		long now = (new Date()).getTime();
		Date validity = new Date(now + appConfig.getJwtExpire() * 1000);
		
		log.info(">>>>>> authentication.getName(): {}", authentication.getName());
		return Jwts.builder()
				.setSubject(authentication.getName())
				.claim(AUTHORITIES_KEY, authorities)
				.signWith(key, SignatureAlgorithm.HS512)
				.setExpiration(validity)
				.compact();
		
	}
	
	public String getEmailFromJwtToken(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}
	
	// 토큰에 담겨있는 권한 정보들을 이용해 Authentication 객체 리턴
	public Authentication getAuthentication(String token) {
		Claims claims = Jwts
				.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
		
//		Collection<? extends GrantedAuthority> authorities = 
//				Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
//					.map(SimpleGrantedAuthority::new)
//					.collect(Collectors.toList());
				
//		User principal = new User(claims.getSubject(), "", authorities);
//		return new UsernamePasswordAuthenticationToken(principal, token, authorities);
		
		User principal = new User(claims.getSubject(), "", new ArrayList<>());
		return new UsernamePasswordAuthenticationToken(principal, token, new ArrayList<>());
	}
	
	// 토큰 검증
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
			log.info("잘못된 JWT 서명입니다.");
		} catch (ExpiredJwtException e) {
			log.info("만료된 JWT 토큰입니다.");
		} catch (UnsupportedJwtException e) {
			log.info("지원되지 않는 JWT 토큰입니다.");
		} catch (IllegalArgumentException e) {
			log.info("JWT 토큰이 잘못되었습니다.");
		}
		return false;
	}
}
