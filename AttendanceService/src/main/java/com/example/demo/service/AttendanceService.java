package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.model.Attendance;

public interface AttendanceService {
	public Attendance clockIn(int employeeId);
	public Attendance clockOut(int employeeId);
	public List<Attendance> getAttendanceHistory(int employeeId);
	public Long calculateWorkHours(LocalDateTime clockIn, LocalDateTime clockOut);
}
