package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.exception.ClockingException;
import com.example.demo.exception.EmployeeNotFound;
import com.example.demo.model.Attendance;
import com.example.demo.repository.AttendanceRepository;
import com.example.demo.service.AttendanceServiceImpl;
@SpringBootTest
class AttendanceServiceApplicationTests {

	@Mock
    private AttendanceRepository repo;

    @InjectMocks
    private AttendanceServiceImpl service;

    private Attendance attendance;

    @BeforeEach
    void setUp() {
        attendance = new Attendance();
        attendance.setEmployeeId(1);
        attendance.setDate(LocalDate.now());
        attendance.setClockIn(LocalDateTime.now().minusHours(8));
        attendance.setClockOut(LocalDateTime.now());
        attendance.setWorkHours(8L);
    }

    @Test
    void testClockIn_Success() throws ClockingException {
        when(repo.findByEmployeeIdAndDate(anyInt(), any(LocalDate.class))).thenReturn(Optional.empty());
        when(repo.save(any(Attendance.class))).thenReturn(attendance);

        Attendance result = service.clockIn(1);

        assertNotNull(result);
        assertEquals(1, result.getEmployeeId());
        verify(repo).save(any(Attendance.class));
    }

    @Test
    void testClockIn_AlreadyClockedIn() {
        when(repo.findByEmployeeIdAndDate(anyInt(), any(LocalDate.class))).thenReturn(Optional.of(attendance));

        assertThrows(ClockingException.class, () -> service.clockIn(1));
    }

    @Test
    void testClockOut_Success() throws ClockingException {
        when(repo.findByEmployeeIdAndDate(anyInt(), any(LocalDate.class))).thenReturn(Optional.of(attendance));
        when(repo.save(any(Attendance.class))).thenReturn(attendance);

        Attendance result = service.clockOut(1);

        assertNotNull(result);
        assertNotNull(result.getClockOut());
        assertEquals(8L, result.getWorkHours());
    }

    @Test
    void testClockOut_NoClockIn() {
        when(repo.findByEmployeeIdAndDate(anyInt(), any(LocalDate.class))).thenReturn(Optional.empty());

        assertThrows(ClockingException.class, () -> service.clockOut(1));
    }

    @Test
    void testCalculateWorkHours() {
        LocalDateTime in = LocalDateTime.now().minusHours(5);
        LocalDateTime out = LocalDateTime.now();

        Long hours = service.calculateWorkHours(in, out);

        assertEquals(5L, hours);
    }

    @Test
    void testGetAttendanceByEmployeeId() throws EmployeeNotFound {
        when(repo.findByEmployeeId(1)).thenReturn(List.of(attendance));

        List<Attendance> result = service.getAttendanceByEmployeeId(1);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getEmployeeId());
    }

    @Test
    void testGetDetailedAttendanceStats() {
        when(repo.findByEmployeeId(1)).thenReturn(List.of(attendance));

        Map<String, Object> stats = service.getDetailedAttendanceStats(1);

        assertFalse(stats.isEmpty());
        assertTrue(stats.containsKey(LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM"))));
    }

}
