package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.EmployeeIdNotFoundException;
import com.example.demo.exception.LeaveTypeNotFoundException;
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
	private static final Logger logger = LoggerFactory.getLogger(LeaveBalanceService.class);
    public void initializeLeaveBalance(long employeeId) {
        LeaveTypes.leaves().forEach((type, count) -> {
            LeaveBalance balance = new LeaveBalance(0, employeeId, type, count);
            repo.save(balance);
            logger.debug("Initialized leave type '{}' with {} days for employeeId: {}", type, count, employeeId);
        });
    }
    public Optional<LeaveBalance> getBalanceByType(int employeeId, String leaveType) throws EmployeeIdNotFoundException {
    	logger.info("Fetching leave balance for employeeId: {}, leaveType: {}", employeeId, leaveType);
    	Optional<LeaveBalance> optional =repo.findByEmployeeIdAndLeaveType(employeeId, leaveType);
    	if(optional.isEmpty()) {
    		logger.warn("Leave balance not found for employeeId: {}, leaveType: {}", employeeId, leaveType);
    		throw new EmployeeIdNotFoundException("Employee Id and Leave Type Not Found");}
        return optional;
    }
    public String updateLeaveBalance(LeaveBalance updated) throws LeaveTypeNotFoundException {
    	logger.info("Updating leave balance for employeeId: {}, leaveType: {}", updated.getEmployeeId(), updated.getLeaveType());
        Optional<LeaveBalance> optional = repo.findByEmployeeIdAndLeaveType(
             updated.getEmployeeId(), updated.getLeaveType());
        if (optional.isPresent()) {
            LeaveBalance existing = optional.get();
            existing.setBalance(updated.getBalance());
            repo.save(existing);
            logger.info("Leave balance updated for employeeId: {}, leaveType: {}", updated.getEmployeeId(), updated.getLeaveType());
            return "Leave balance updated successfully.";
        } else {
        	logger.error("Leave type '{}' not found for employeeId: {}", updated.getLeaveType(), updated.getEmployeeId());
            throw new LeaveTypeNotFoundException("Leave type not found for this employee.");
        }
    }
    public List<LeaveBalance> getLeaveBalancesByEmployeeId(int employeeId) throws EmployeeIdNotFoundException {
    	logger.info("Fetching all leave balances for employeeId: {}", employeeId);
    	 List<LeaveBalance> optional= repo.findByEmployeeId(employeeId);
    	 if(optional.isEmpty())
    		  throw new EmployeeIdNotFoundException("Employee not Found with id:"+employeeId);
    	 else
    		 return optional;
    }

    @Transactional
    public void deleteByEmployeeId(int employeeId) {
    	 logger.info("Deleting all leave balances for employeeId: {}", employeeId);
    	 repo.deleteByEmployeeId(employeeId);
    	 logger.info("Deleted leave balances for employeeId: {}", employeeId);
    }
}