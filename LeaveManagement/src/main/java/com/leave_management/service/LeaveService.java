package com.leave_management.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leave_management.client.LeaveBalanceClient;
import com.leave_management.dto.LeaveBalanceDTO;
import com.leave_management.model.LeaveRequest;
import com.leave_management.repository.LeaveRequestRepository;

@Service
public class LeaveService {
	@Autowired
    LeaveRequestRepository requestRepo;
    @Autowired
    LeaveBalanceClient balanceClient;

    public String applyLeave(LeaveRequest request) {
        LeaveBalanceDTO balance = balanceClient.getBalanceByType(request.getEmployeeId(), request.getLeaveType());

        long diff = request.getEndDate().getTime() - request.getStartDate().getTime();
        int days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1;

        if (balance.getBalance() < days) {
            return "Insufficient leave balance.";
        }

        request.setStatus("Pending");
        requestRepo.save(request);
        return "Leave applied.";
    }

    public String approveLeave(int requestId) {
        Optional<LeaveRequest> opt = requestRepo.findById(requestId);
        if (opt.isEmpty()) return "Request not found";

        LeaveRequest req = opt.get();
        if (!"Pending".equals(req.getStatus())) return "Already processed";

        LeaveBalanceDTO balance = balanceClient.getBalanceByType(req.getEmployeeId(), req.getLeaveType());
        int days = (int) TimeUnit.DAYS.convert(req.getEndDate().getTime() - req.getStartDate().getTime(), TimeUnit.MILLISECONDS) + 1;

        if (balance.getBalance() < days) return "Insufficient balance";

        balance.setBalance(balance.getBalance() - days);
        balanceClient.updateLeaveBalance(balance);

        req.setStatus("Approved");
        requestRepo.save(req);
        return "Approved";
    }

    public String rejectLeave(int requestId) {
        Optional<LeaveRequest> opt = requestRepo.findById(requestId);
        if (opt.isEmpty()) return "Request not found";

        LeaveRequest req = opt.get();
        if (!"Pending".equals(req.getStatus())) return "Already processed";

        req.setStatus("Rejected");
        requestRepo.save(req);
        return "Rejected";
    }

    public List<LeaveRequest> getAllRequests() {
        return requestRepo.findAll();
    }

    public List<LeaveRequest> getRequestsByStatus(String status) {
        return requestRepo.findByStatus(status);
    }
    public List<LeaveRequest> getRequestsByEmployeeId(int employeeId) {
        return requestRepo.findByEmployeeId(employeeId);
    }
}