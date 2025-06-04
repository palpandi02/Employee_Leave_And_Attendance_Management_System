package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.example.demo.exception.ClockingException;
import com.example.demo.model.Attendance;

public interface AttendanceService {
	public Attendance clockIn(int employeeId) throws ClockingException;
	public Attendance clockOut(int employeeId) throws ClockingException;
	public Long calculateWorkHours(LocalDateTime clockIn, LocalDateTime clockOut);
	Map<String, Object> getDetailedAttendanceStats(int employeeId);
	public List<Attendance> getAllAttendances();
}
