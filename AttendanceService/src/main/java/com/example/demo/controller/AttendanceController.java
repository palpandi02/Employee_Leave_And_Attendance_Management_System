package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.ClockingException;
import com.example.demo.model.Attendance;
import com.example.demo.service.AttendanceServiceImpl;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

	@Autowired
	private AttendanceServiceImpl service;

	// URL: http://localhost:8081/attendance/clockin
	@PostMapping("/clockin/{employeeId}")
	public Attendance clockIn(@PathVariable("employeeId") int employeeId) throws ClockingException {
		return service.clockIn(employeeId);

	}

	// URL: http://localhost:8081/attendance/clockout
	@PostMapping("/clockout/{employeeId}")
	public Attendance clockOut(@PathVariable("employeeId") int employeeId) {
		return service.clockOut(employeeId);

	}

	// URL: http://localhost:8081/attendance/employee
	@GetMapping("/employee/{employeeId}")
	public List<Attendance> getAttendanceByEmployeeId(@PathVariable int employeeId) {
	    return service.getAttendanceByEmployeeId(employeeId);
	}
}
