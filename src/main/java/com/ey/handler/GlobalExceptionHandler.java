package com.ey.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ey.exception.CopyNotAvailableException;
import com.ey.exception.CopyNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(CopyNotAvailableException.class)
	public ResponseEntity<String> handleCopyNotAvailable(CopyNotAvailableException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}

	@ExceptionHandler(CopyNotFoundException.class)
	public ResponseEntity<String> handleCopyNotFound(CopyNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}
}