package com.example.demo.model;

import java.util.List;

import com.example.demo.client.ShiftClient;
import com.example.demo.dto.AttendanceDTO;
import com.example.demo.dto.LeaveBalanceDTO;
import com.example.demo.dto.LeaveRecordDTO;
import com.example.demo.dto.ShiftDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeReport {
    
	private int employeeId;
    private List<AttendanceDTO> attendance;
    private List<LeaveBalanceDTO> leaveBalance;
    private List<LeaveRecordDTO> leaveRecords;
    private List<ShiftDTO> shift;
	public EmployeeReport(int employeeId, List<AttendanceDTO> attendance, List<LeaveBalanceDTO> leaveBalance,
			List<LeaveRecordDTO> leaveRecords) {
		super();
		this.employeeId = employeeId;
		this.attendance = attendance;
		this.leaveBalance = leaveBalance;
		this.leaveRecords = leaveRecords;
	}
	
    
    //private ShiftDTO shift;
}