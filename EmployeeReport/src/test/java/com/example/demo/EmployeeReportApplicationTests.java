package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.demo.client.AttendanceClient;
import com.example.demo.client.EmployeeServiceClient;
import com.example.demo.client.LeaveBalanceClient;
import com.example.demo.client.LeaveManagementClient;
import com.example.demo.client.ShiftClient;
import com.example.demo.dto.AttendanceDTO;
import com.example.demo.dto.LeaveBalanceDTO;
import com.example.demo.dto.LeaveRecordDTO;
import com.example.demo.dto.ShiftDTO;
import com.example.demo.exception.EmployeeIdNotFound;
import com.example.demo.model.EmployeeReport;
import com.example.demo.service.EmployeeReportService;

public class EmployeeReportApplicationTests {

    @Mock
    private AttendanceClient attendanceClient;

    @Mock
    private LeaveManagementClient leaveManagementClient;

    @Mock
    private LeaveBalanceClient leaveBalanceClient;

    @Mock
    private ShiftClient shiftClient;

    @Mock
    private EmployeeServiceClient employeeServiceClient;

    @InjectMocks
    private EmployeeReportService employeeReportService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);//use to initilize the field annotation
    }

    @Test
    void testGenerateReportByEmployeeId_success() throws EmployeeIdNotFound {
        int employeeId = 1;

        // Mock Attendance
        List<AttendanceDTO> attendanceList = List.of(
            new AttendanceDTO(1, employeeId, null, null, null, 8L)
        );
        Map<String, Object> attendanceStats = Map.of("totalHours", 160);

        // Mock Shift
        List<ShiftDTO> shiftList = List.of(new ShiftDTO(1, employeeId, "Day", false));
        Map<String, Long> shiftStats = Map.of("Day", 20L);

        // Mock Leaves
        List<LeaveBalanceDTO> leaveBalance = List.of(new LeaveBalanceDTO(1, employeeId, "Casual", 5));
        List<LeaveRecordDTO> leaveRecords = List.of(
            new LeaveRecordDTO(1, employeeId, "Casual", new Date(), new Date(), "Approved")
        );

        // Mock external calls
        when(attendanceClient.getAttendanceByEmployeeId(employeeId)).thenReturn(attendanceList);
        when(attendanceClient.getDetailedStats(employeeId)).thenReturn(attendanceStats);
        when(shiftClient.getShiftsByEmployeeId(employeeId)).thenReturn(shiftList);
        when(shiftClient.getShiftCountByType(employeeId)).thenReturn(shiftStats);
        when(leaveBalanceClient.getBalancesByEmployeeId(employeeId)).thenReturn(leaveBalance);
        when(leaveManagementClient.getLeaveHistoryByEmployeeId(employeeId)).thenReturn(leaveRecords);

        // Call the method
        EmployeeReport report = employeeReportService.generateReportByEmployeeId(employeeId);

        // Assertions
        assertNotNull(report);
        assertEquals(employeeId, report.getEmployeeId());
        assertEquals(1, report.getAttendanceReport().getAttendance().size());
        assertEquals(1, report.getLeaveBalance().size());
        assertEquals(1, report.getLeaveRecords().size());
        assertEquals(1, report.getShift().getShift().size());
    }
}