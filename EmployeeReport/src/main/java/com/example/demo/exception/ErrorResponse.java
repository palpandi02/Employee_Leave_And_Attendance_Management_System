package com.example.demo.exception;
import java.time.LocalDateTime;
 
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
 
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
 
	private int status;
	private String message;
	private LocalDateTime timeStamp;
	private String path;
}