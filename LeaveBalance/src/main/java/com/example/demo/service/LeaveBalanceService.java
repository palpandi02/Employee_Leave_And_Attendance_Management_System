package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.LeaveTypeNotFound;
import com.example.demo.model.LeaveBalance;
import com.example.demo.repository.LeaveBalanceRepository;
import com.example.demo.util.LeaveTypes;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LeaveBalanceService {
	@Autowired
     LeaveBalanceRepository repo;

    public void initializeLeaveBalance(long employeeId) {
        LeaveTypes.leaves().forEach((type, count) -> {
            LeaveBalance balance = new LeaveBalance(0, employeeId, type, count);
            repo.save(balance);
        });
    }
    public Optional<LeaveBalance> getBalanceByType(int employeeId, String leaveType) {
        return repo.findByEmployeeIdAndLeaveType(employeeId, leaveType);
    }
    public String updateLeaveBalance(LeaveBalance updated) throws LeaveTypeNotFound {
        Optional<LeaveBalance> optional = repo.findByEmployeeIdAndLeaveType(
            (long) updated.getEmployeeId(), updated.getLeaveType());

        if (optional.isPresent()) {
            LeaveBalance existing = optional.get();
            existing.setBalance(updated.getBalance());
            repo.save(existing);
            return "Leave balance updated successfully.";
        } else {
            throw new LeaveTypeNotFound("Leave type not found for this employee.");
        }
    }
    public List<LeaveBalance> getLeaveBalancesByEmployeeId(int employeeId) {
        return repo.findByEmployeeId(employeeId);
    }

    @Transactional
    public void deleteByEmployeeId(int employeeId) {
    	 repo.deleteByEmployeeId(employeeId);
    }
}