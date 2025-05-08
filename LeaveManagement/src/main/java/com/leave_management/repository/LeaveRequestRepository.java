package com.leave_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leave_management.model.LeaveRequest;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Integer> {
    List<LeaveRequest> findByStatus(String status);
    List<LeaveRequest> findByEmployeeId(int employeeId);
}