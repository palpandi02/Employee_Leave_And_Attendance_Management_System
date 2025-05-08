package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.exception.ClockingException;
import com.example.demo.model.Attendance;

public interface AttendanceService {
	public Attendance clockIn(int employeeId) throws ClockingException;
	public Attendance clockOut(int employeeId);
	public Long calculateWorkHours(LocalDateTime clockIn, LocalDateTime clockOut);
}
