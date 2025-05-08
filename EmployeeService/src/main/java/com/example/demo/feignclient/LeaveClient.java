package com.example.demo.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="LEAVEBALANCE",path="/balance")
public interface LeaveClient {

    @PostMapping("/initialize/{employeeId}")
    void initializeLeaveBalance(@PathVariable("employeeId") int employeeId);
    @DeleteMapping("/delete/{employeeId}")
    public void deleteByEmployeeId(@PathVariable("employeeId") int employeeId);
}