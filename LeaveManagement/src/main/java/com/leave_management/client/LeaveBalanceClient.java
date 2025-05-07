package com.leave_management.client;

import com.leave_management.dto.LeaveBalanceDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value="Leave-Balance",url="http://localhost:1002/balance")
public interface LeaveBalanceClient {

    @GetMapping("/{employeeId}/{leaveType}")
    LeaveBalanceDTO getBalanceByType(@PathVariable long employeeId, @PathVariable String leaveType);

    @PutMapping("/update")
    void updateLeaveBalance(@RequestBody LeaveBalanceDTO balance);
}