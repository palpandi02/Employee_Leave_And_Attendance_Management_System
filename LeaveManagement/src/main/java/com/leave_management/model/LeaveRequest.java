package com.leave_management.model;
 
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
 
import java.util.Date;
 
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequest {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int leaveId;
 
    @Min(value = 1, message = "Employee ID must be a positive number")
    private int employeeId;
 
    @NotBlank(message = "Leave type must not be blank")
    private String leaveType;
 
    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date cannot be in the past")
    private Date startDate;
 
    @NotNull(message = "End date is required")
    @FutureOrPresent(message = "End date cannot be in the past")
    private Date endDate;
 
    @NotBlank(message = "Status must not be blank")
    @Pattern(regexp = "Pending|Approved|Rejected", message = "Status must be 'Pending', 'Approved', or 'Rejected'")
    private String status;
}