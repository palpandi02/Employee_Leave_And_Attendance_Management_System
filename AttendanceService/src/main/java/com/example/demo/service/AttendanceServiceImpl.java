package com.example.demo.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.demo.exception.ClockingException;
import com.example.demo.exception.EmployeeNotFound;
import com.example.demo.model.Attendance;
import com.example.demo.repository.AttendanceRepository;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Service
@AllArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private static final Logger logger = LoggerFactory.getLogger(AttendanceServiceImpl.class);
    private AttendanceRepository repo;
	public Attendance clockIn(int employeeId) throws ClockingException {
        logger.info("Attempting to clock in for employeeId: {}", employeeId);
        LocalDate today = LocalDate.now();
        Optional<Attendance> existingAttendance = repo.findByEmployeeIdAndDate(employeeId, today);

        if (existingAttendance.isPresent()) {
            logger.warn("Clock-in attempt failed: already clocked in for employeeId: {}", employeeId);
            throw new ClockingException("Already clocked in");
        }

        Attendance attendance = new Attendance();
        attendance.setEmployeeId(employeeId);
        attendance.setDate(today);
        attendance.setClockIn(LocalDateTime.now());

        logger.info("Clock-in successful for employeeId: {}", employeeId);
        return repo.save(attendance);
    }

    public Attendance clockOut(int employeeId) throws ClockingException {
        logger.info("Attempting to clock out for employeeId: {}", employeeId);
        LocalDate today = LocalDate.now();
        Attendance attendance = repo.findByEmployeeIdAndDate(employeeId, today)
                .orElseThrow(() -> {
                    logger.error("Clock-out failed: no clock-in record found for employeeId: {}", employeeId);
                    return new ClockingException("No clock-in record found for today");
                });

        attendance.setClockOut(LocalDateTime.now());
        attendance.setWorkHours(calculateWorkHours(attendance.getClockIn(), attendance.getClockOut()));

        logger.info("Clock-out successful for employeeId: {}", employeeId);
        return repo.save(attendance);
    }

    public Long calculateWorkHours(LocalDateTime clockIn, LocalDateTime clockOut) {
        Duration duration = Duration.between(clockIn, clockOut);
        long hours = duration.toHours();
        logger.debug("Calculated work hours: {} (from {} to {})", hours, clockIn, clockOut);
        return hours;
    }

    public List<Attendance> getAttendanceByEmployeeId(int employeeId) throws EmployeeNotFound {
        logger.info("Fetching attendance records for employeeId: {}", employeeId);
        List<Attendance> optional=repo.findByEmployeeId(employeeId);
        if(optional!=null)
         return optional;
        else
         throw new EmployeeNotFound("Employee Not Found with id:"+employeeId);
    }

    public Map<String, Object> getDetailedAttendanceStats(int employeeId) {
        logger.info("Generating detailed attendance stats for employeeId: {}", employeeId);
        List<Attendance> records = repo.findByEmployeeId(employeeId);

        List<Attendance> validRecords = records.stream()
            .filter(a -> a.getClockIn() != null && a.getClockOut() != null)
            .collect(Collectors.toList());

        Map<String, Object> result = new LinkedHashMap<>();

        Map<String, List<Attendance>> byMonth = validRecords.stream()
            .collect(Collectors.groupingBy(
                a -> a.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM")),
                LinkedHashMap::new,
                Collectors.toList()
            ));

        for (Map.Entry<String, List<Attendance>> entry : byMonth.entrySet()) {
            String month = entry.getKey();
            List<Attendance> monthlyRecords = entry.getValue();

            long totalDays = monthlyRecords.size();
            double totalHours = monthlyRecords.stream()
                .mapToLong(Attendance::getWorkHours)
                .sum();

            double monthlyAvgHours = totalDays == 0 ? 0 : totalHours / totalDays;

            Map<String, Double> weeklyAvgMap = new LinkedHashMap<>();
            Map<Integer, List<Attendance>> byWeek = monthlyRecords.stream()
                .collect(Collectors.groupingBy(
                    a -> (a.getDate().getDayOfMonth() - 1) / 7 + 1,
                    TreeMap::new,
                    Collectors.toList()
                ));

            for (int week = 1; week <= 4; week++) {
                List<Attendance> weekRecords = byWeek.getOrDefault(week, new ArrayList<>());
                double weekHours = weekRecords.stream()
                    .mapToLong(Attendance::getWorkHours)
                    .sum();
                double avg = weekRecords.isEmpty() ? 0.0 : weekHours / weekRecords.size();
                weeklyAvgMap.put("Week " + week, avg);
            }

            Map<String, Object> monthDetails = new LinkedHashMap<>();
            monthDetails.put("TotalDays", totalDays);
            monthDetails.put("MonthlyAverageHours", monthlyAvgHours);
            monthDetails.put("WeeklyAverageHours", weeklyAvgMap);

            result.put(month, monthDetails);
        }

        logger.info("Completed generating attendance report for employeeId: {}", employeeId);
        return result;
    }
}
