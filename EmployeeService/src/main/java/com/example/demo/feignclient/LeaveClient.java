package com.example.demo.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value="Leave-Balance",url="http://localhost:1002/balance")
public interface LeaveClient {

    @PostMapping("/initialize/{employeeId}")
    void initializeLeaveBalance(@PathVariable("employeeId") int employeeId);
    @DeleteMapping("/delete/{employeeId}")
    public void deleteByEmployeeId(@PathVariable("employeeId") int employeeId);
}