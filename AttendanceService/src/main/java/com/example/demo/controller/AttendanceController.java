package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.ClockingException;
import com.example.demo.exception.EmployeeNotFound;
import com.example.demo.model.Attendance;
import com.example.demo.service.AttendanceServiceImpl;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/attendance")
@AllArgsConstructor
public class AttendanceController {

	// @Autowired
	AttendanceServiceImpl service;

	// URL: http://localhost:1006/attendance/clockin
	@PostMapping("/clockin/{employeeId}")
	public Attendance clockIn(@PathVariable("employeeId") int employeeId) throws ClockingException {
		return service.clockIn(employeeId);

	}

	// URL: http://localhost:1006/attendance/clockout
	@PostMapping("/clockout/{employeeId}")
	public Attendance clockOut(@PathVariable("employeeId") int employeeId) throws ClockingException {
		return service.clockOut(employeeId);

	}

	// URL: http://localhost:1006/attendance/employee
	@GetMapping("/employee/{employeeId}")
	public List<Attendance> getAttendanceByEmployeeId(@PathVariable int employeeId) throws EmployeeNotFound {
		return service.getAttendanceByEmployeeId(employeeId);
	}

	@GetMapping("/detailed-stats/{employeeId}")
	public Map<String, Object> getDetailedStats(@PathVariable int employeeId) {
		return service.getDetailedAttendanceStats(employeeId);
	}
	@GetMapping("/getall")
	public List<Attendance> getAll(){
		return service.getAllAttendances();
	}
}
