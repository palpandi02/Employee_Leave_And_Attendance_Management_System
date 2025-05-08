package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.LeaveBalance;

import jakarta.transaction.Transactional;

public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Integer> {
    List<LeaveBalance> findByEmployeeId(long employeeId);
    Optional<LeaveBalance> findByEmployeeIdAndLeaveType(long employeeId, String leaveType);
    @Modifying
    @Transactional
    @Query("DELETE FROM LeaveBalance lb WHERE lb.employeeId = :employeeId")
	void deleteByEmployeeId(int employeeId);
}