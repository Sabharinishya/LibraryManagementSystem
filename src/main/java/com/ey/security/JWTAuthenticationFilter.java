package com.ey.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ey.dto.request.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private final AuthenticationManager authenticationManager;
	private final JWTUtil jwtUtil;
	private final ObjectMapper objectMapper = new ObjectMapper();

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
		try {
			LoginRequest creds = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(creds.getEmail(),
					creds.getPassword());
			return authenticationManager.authenticate(authToken);
		} catch (IOException e) {
			throw new RuntimeException("Invalid login request");
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException {
		User principal = (User) authResult.getPrincipal();
		String token = jwtUtil.generateToken(principal.getUsername(), principal.getAuthorities());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		Map<String, Object> body = new HashMap<>();
		body.put("accessToken", token);
		body.put("tokenType", "Bearer");
		body.put("username", principal.getUsername());
		body.put("roles",
				principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
		new ObjectMapper().writeValue(response.getOutputStream(), body);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		Map<String, Object> body = new HashMap<>();
		body.put("error", "Unauthorized");
		body.put("message", failed.getMessage());
		new ObjectMapper().writeValue(response.getOutputStream(), body);
	}
}