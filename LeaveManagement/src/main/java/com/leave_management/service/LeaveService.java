package com.leave_management.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leave_management.client.LeaveBalanceClient;
import com.leave_management.dto.LeaveBalanceDTO;
import com.leave_management.exception.AlreadyProcessedException;
import com.leave_management.exception.InsufficientLeaveBalanceException;
import com.leave_management.exception.InvalidRequest;
import com.leave_management.exception.NoResultFoundException;
import com.leave_management.exception.RequestNotFoundException;
import com.leave_management.model.LeaveRequest;
import com.leave_management.repository.LeaveRequestRepository;

@Service
public class LeaveService {
	@Autowired
    LeaveRequestRepository requestRepo;
	@Autowired
    LeaveBalanceClient balanceClient;
    String pending="Pending";
    private static final Logger logger = LoggerFactory.getLogger(LeaveService.class);
    public String applyLeave(LeaveRequest request) throws InsufficientLeaveBalanceException, InvalidRequest {
    	logger.info("Applying leave for employeeId: {}", request.getEmployeeId());
        LeaveBalanceDTO balance = balanceClient.getBalanceByType(request.getEmployeeId(), request.getLeaveType());
        if(request.getStartDate().after(request.getEndDate()))
        	throw new InvalidRequest("End date"+request.getEndDate()+"must greater than Start date "+request.getStartDate());
        long diff = request.getEndDate().getTime() - request.getStartDate().getTime();
        int days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1;

        if (balance.getBalance() < days) {
        	logger.warn("Insufficient leave balance for employeeId: {}. Required: {}, Available: {}",request.getEmployeeId(), days, balance.getBalance());
             throw new InsufficientLeaveBalanceException("does not have required leave balance");
        }

        request.setStatus(pending);
        requestRepo.save(request);
        logger.info("Leave request saved as Pending for employeeId: {}", request.getEmployeeId());
        return "Leave applied.";
    }

    public String approveLeave(int requestId) throws InsufficientLeaveBalanceException, RequestNotFoundException, AlreadyProcessedException {
    	logger.info("Approving leave request with ID: {}", requestId);
        Optional<LeaveRequest> opt = requestRepo.findById(requestId);
        if (opt.isEmpty()) {
        	logger.error("Leave request not found with ID: {}", requestId);
        	throw new RequestNotFoundException("Request Not Found");}

        LeaveRequest req = opt.get();
        if (!pending.equals(req.getStatus())) { 
        	logger.warn("Leave request already processed. ID: {}, Status: {}", requestId, req.getStatus());
        	throw new AlreadyProcessedException("Already processed");}

        LeaveBalanceDTO balance = balanceClient.getBalanceByType(req.getEmployeeId(), req.getLeaveType());
        int days = (int) TimeUnit.DAYS.convert(req.getEndDate().getTime() - req.getStartDate().getTime(), TimeUnit.MILLISECONDS) + 1;

        if (balance.getBalance() < days) {
        	logger.warn("Insufficient balance for approval. EmployeeId: {}, Required: {}, Available: {}",req.getEmployeeId(), days, balance.getBalance());
        	throw new InsufficientLeaveBalanceException("does not have required leave balance");
        }

        balance.setBalance(balance.getBalance() - days);
        balanceClient.updateLeaveBalance(balance);

        req.setStatus("Approved");
        requestRepo.save(req);
        logger.info("Leave request approved. ID: {}, EmployeeId: {}", requestId, req.getEmployeeId());
        return "Approved";
    }

    public String rejectLeave(int requestId) throws RequestNotFoundException, AlreadyProcessedException {
    	logger.info("Rejecting leave request with ID: {}", requestId);
        Optional<LeaveRequest> opt = requestRepo.findById(requestId);
        if (opt.isEmpty()) {
        	logger.error("Leave request not found with ID: {}", requestId);
        	throw new RequestNotFoundException("Request not found");
        }

        LeaveRequest req = opt.get();
        if (!pending.equals(req.getStatus())) {
        	logger.warn("Leave request already processed. ID: {}, Status: {}", requestId, req.getStatus());
        	throw new AlreadyProcessedException("Already processed");
        }
        req.setStatus("Rejected");
        requestRepo.save(req);
        logger.info("Leave request rejected. ID: {}, EmployeeId: {}", requestId, req.getEmployeeId());
        return "Rejected";
    }

    public List<LeaveRequest> getAllRequests() {
    	logger.info("Fetching all leave requests");
        return requestRepo.findAll();
    }

    public List<LeaveRequest> getRequestsByStatus(String status) throws NoResultFoundException {
    	logger.info("Fetching leave requests with status: {}", status);
    	List<LeaveRequest> optional=requestRepo.findByStatus(status);
        if(!optional.isEmpty())
        	return optional;
        else
        	throw new NoResultFoundException("No Result Found with status:"+status);
    }
    public List<LeaveRequest> getRequestsByEmployeeId(int employeeId) throws NoResultFoundException {
    	logger.info("Fetching leave requests for employeeId: {}", employeeId);
    	List<LeaveRequest> optional=requestRepo.findByEmployeeId(employeeId);
        if(!optional.isEmpty())
        	return optional;
        else
        	throw new NoResultFoundException("No Result Found with id:"+employeeId);
    }
    public void deleteById(int id) {
    	 logger.info("Deleting leave request with ID: {}", id);
    	 requestRepo.deleteById(id);
    	 logger.info("Deleted leave request with ID: {}", id);
    }
}