package com.quangduong.redis.utils;

import com.quangduong.redis.dto.user.UserDTO;
import com.quangduong.redis.security.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

	@Autowired
	private Environment environment;

	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

	public String generateToken(Authentication authentication) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + Long.parseLong(environment.getProperty("jwt.expiration")));
		Map<String, Object> claims = new HashMap<>();
		CustomUserDetails userDetail = (CustomUserDetails) authentication.getPrincipal();
		return Jwts.builder().setClaims(claims).setSubject(userDetail.getUsername())
				.claim("username", userDetail.getUsername()).setIssuedAt(now).setExpiration(expiryDate)
				.signWith(SignatureAlgorithm.HS512, environment.getProperty("jwt.secret")).compact();
	}
	
	public String generateToken(String username) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + Long.parseLong(environment.getProperty("jwt.expiration")));
		Map<String, Object> claims = new HashMap<>();
		return Jwts.builder().setClaims(claims).setSubject(username)
				.claim("username", username).setIssuedAt(now).setExpiration(expiryDate)
				.signWith(SignatureAlgorithm.HS512, environment.getProperty("jwt.secret")).compact();
	}

	public String getUserNameFromJWT(String token) {
		Claims claims = Jwts.parser().setSigningKey(environment.getProperty("jwt.secret")).parseClaimsJws(token)
				.getBody();
		return claims.getSubject();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(environment.getProperty("jwt.secret")).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return false;
	}
}
