package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeReportDTO {
    private int employeeId;
    private List<AttendanceDTO> attendance;
    private List<LeaveRecordDTO> leaveHistory;
    private List<LeaveBalanceDTO> leaveBalances;
  //  private List<ShiftDTO> shift;
}