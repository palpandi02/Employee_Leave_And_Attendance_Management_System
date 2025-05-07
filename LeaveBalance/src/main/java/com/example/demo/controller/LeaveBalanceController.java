package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.LeaveBalance;
import com.example.demo.service.LeaveBalanceService;

@RestController
@RequestMapping("/balance")
public class LeaveBalanceController {
	@Autowired
     LeaveBalanceService service;

    @PostMapping("/initialize/{employeeId}")
    public ResponseEntity<String> init(@PathVariable int employeeId) {
        service.initializeLeaveBalance(employeeId);
        return ResponseEntity.ok("Initialized");
    }

    @GetMapping("/{employeeId}")	
    public List<LeaveBalance> getBalance(@PathVariable int employeeId) {
        return service.getLeaveBalance(employeeId);
    }

    @GetMapping("/{employeeId}/{leaveType}")
    public ResponseEntity<LeaveBalance> getBalanceByType(@PathVariable int employeeId, @PathVariable String leaveType) {
        return service.getBalanceByType(employeeId, leaveType)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/update")
    public void updateBalance(@RequestBody LeaveBalance balance) {
        service.updateLeaveBalance(balance);
    }
}