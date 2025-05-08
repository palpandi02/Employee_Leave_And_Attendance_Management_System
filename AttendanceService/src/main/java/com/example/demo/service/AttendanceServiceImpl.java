package com.example.demo.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.ClockingException;
import com.example.demo.model.Attendance;
import com.example.demo.repository.AttendanceRepository;

@Service
public class AttendanceServiceImpl implements AttendanceService{

    @Autowired
    private AttendanceRepository repo;

    public Attendance clockIn(int employeeId) throws ClockingException {
        LocalDate today = LocalDate.now();
        Optional<Attendance> existingAttendance = repo.findByEmployeeIdAndDate(employeeId, today);
        
        if (existingAttendance.isPresent()) {
            throw new ClockingException("Already clocked in");
        }

        Attendance attendance = new Attendance();
        attendance.setEmployeeId(employeeId);
        attendance.setDate(today);
        attendance.setClockIn(LocalDateTime.now());

        return repo.save(attendance);
    }


    public Attendance clockOut(int employeeId) {
        LocalDate today = LocalDate.now();
        Attendance attendance = repo.findByEmployeeIdAndDate(employeeId, today)
                .orElseThrow(() -> new RuntimeException("No clock-in record found for today"));

        attendance.setClockOut(LocalDateTime.now());
        attendance.setWorkHours(calculateWorkHours(attendance.getClockIn(), attendance.getClockOut()));

        return repo.save(attendance);
    }

    public Long calculateWorkHours(LocalDateTime clockIn, LocalDateTime clockOut) {
        Duration duration = Duration.between(clockIn, clockOut);
        return duration.toHours();
    }
    public List<Attendance> getAttendanceByEmployeeId(int employeeId) {
        return repo.findByEmployeeId(employeeId);
    }
}
