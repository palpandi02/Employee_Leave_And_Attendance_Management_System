package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.client.AttendanceClient;
import com.example.demo.client.LeaveBalanceClient;
import com.example.demo.client.LeaveManagementClient;
import com.example.demo.client.ShiftClient;
import com.example.demo.dto.AttendanceDTO;
import com.example.demo.dto.LeaveBalanceDTO;
import com.example.demo.dto.LeaveRecordDTO;
import com.example.demo.dto.ShiftDTO;
import com.example.demo.model.EmployeeReport;

@Service
public class EmployeeReportService {

    @Autowired
    private AttendanceClient attendanceClient;

    @Autowired
    private LeaveManagementClient leaveManagementClient;

    @Autowired
    private LeaveBalanceClient leaveBalanceClient;

    @Autowired
    private ShiftClient shiftClient;

    public EmployeeReport generateReportByEmployeeId(int employeeId) {
        // Fetch data from all services
       // AttendanceDTO attendance = attendanceClient.getAttendanceByEmployeeId(employeeId);
//        List<LeaveRecordDTO> allLeaves = leaveManagementClient.getAllLeaveRecords();
//        List<LeaveBalanceDTO> balances = leaveBalanceClient.getAllBalances();
//        ShiftDTO shift = shiftClient.getShiftByEmployeeId(employeeId);
//
//        // Filter records by employeeId
//        List<LeaveRecordDTO> employeeLeaves = allLeaves.stream()
//                .filter(l -> l.getEmployeeId() == employeeId)
//                .toList();
//
//        LeaveBalanceDTO leaveBalance = balances.stream()
//                .filter(b -> b.getEmployeeId() == employeeId)
//                .findFirst()
//                .orElse(null);
//
//        // Construct report
//        return EmployeeReport.builder()
//                .employeeId(employeeId)
//              //  .attendance(attendance)
//                .leaveBalance(leaveBalance)
//                .leaveRecords(employeeLeaves)
//                .shift(shift)
//                .build();
    	return new EmployeeReport(employeeId,
    			attendanceClient.getAttendanceByEmployeeId(employeeId),
    			leaveBalanceClient.getBalancesByEmployeeId(employeeId),
    			leaveManagementClient.getLeaveHistoryByEmployeeId(employeeId)
    			//shiftClient.getShiftsByEmployeeId(employeeId)
    			);
    }
}


