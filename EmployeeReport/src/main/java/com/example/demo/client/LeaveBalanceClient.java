package com.example.demo.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.dto.LeaveBalanceDTO;

@FeignClient(name = "Leave-Balance", url = "http://localhost:1002/balance")
public interface LeaveBalanceClient {

    @GetMapping("/employee/{employeeId}")
    List<LeaveBalanceDTO> getBalancesByEmployeeId(@PathVariable int employeeId);
}
