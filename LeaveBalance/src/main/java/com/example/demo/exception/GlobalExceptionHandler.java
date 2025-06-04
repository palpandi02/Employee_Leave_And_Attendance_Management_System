package com.example.demo.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
 
@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(FeignException.class)
	public ResponseEntity<ErrorResponse> handleException(FeignException e, HttpServletRequest request,
			HttpServletResponse response) {
 
		String message = e.getMessage();
		int start = message.indexOf("\"message\":\"") + 11; // Start after "message":""
		int end = message.indexOf("\",", start); // End before the next field
		String errorMessage = (start > 9 && end > start) ? message.substring(start, end) : "Unknown error";
		ErrorResponse res = new ErrorResponse(HttpStatus.NOT_FOUND.value(), errorMessage, LocalDateTime.now(),
				request.getRequestURI());
		return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
	}
    @ExceptionHandler(EmployeeIdNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(EmployeeIdNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }
 
    @ExceptionHandler(LeaveTypeNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(LeaveTypeNotFoundException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
 
 
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldError().getDefaultMessage();
        return buildResponse(HttpStatus.BAD_REQUEST, errorMessage);
    }
 
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred. Please try again later.");
    }
 
    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", status.value());
        error.put("error", status.getReasonPhrase());
        error.put("message", message);
        return new ResponseEntity<>(error, status);
    }
}