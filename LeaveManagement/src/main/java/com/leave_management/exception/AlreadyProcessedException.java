package com.leave_management.exception;

public class AlreadyProcessedException extends Exception{
	public AlreadyProcessedException(String message) {
		super(message);
	}
}
