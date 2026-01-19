package com.ey.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTUtil {
	private final String jwtSecret = "ThisIsASecretKeyThatIsAtLeast32CharsLong";
	private final long jwtExpirationMs = 86400000; 

	private SecretKey getSigningKey() {
		byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public String generateToken(String username, java.util.Collection<? extends GrantedAuthority> authorities) {
		List<String> roles = authorities.stream().map(GrantedAuthority::getAuthority).toList();
		Date now = new Date();
		Date expiry = new Date(now.getTime() + jwtExpirationMs);
		return Jwts.builder().setSubject(username).setIssuedAt(now).setExpiration(expiry)
				.addClaims(Map.of("roles", roles)).signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
	}

	public String extractUsername(String token) {
		return getClaim(token, Claims::getSubject);
	}

	public List<String> extractRoles(String token) {
		Object roles = getAllClaims(token).get("roles");
		if (roles instanceof List<?> list) {
			return list.stream().map(Object::toString).toList();
		}
		return List.of();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

	public boolean isTokenValidForUser(String token, String expectedUsername) {
		return validateToken(token) && expectedUsername.equals(extractUsername(token)) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return getClaim(token, Claims::getExpiration);
	}

	private <T> T getClaim(String token, Function<Claims, T> resolver) {
		return resolver.apply(getAllClaims(token));
	}

	private Claims getAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
	}
}