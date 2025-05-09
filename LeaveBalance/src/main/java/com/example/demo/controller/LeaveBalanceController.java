package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.EmployeeIdNotFoundException;
import com.example.demo.exception.LeaveTypeNotFoundException;
import com.example.demo.model.LeaveBalance;
import com.example.demo.service.LeaveBalanceService;
//import com.example.demo.util.client.Employeeclient;

@RestController
@RequestMapping("/balance")
public class LeaveBalanceController {
	@Autowired
     LeaveBalanceService service;
    @PostMapping("/initialize/{employeeId}")
    public ResponseEntity<String> init(@PathVariable long employeeId) throws EmployeeIdNotFoundException {
        service.initializeLeaveBalance(employeeId);
        return ResponseEntity.ok("Initialized");
    }


    @GetMapping("/{employeeId}/{leaveType}")
    public ResponseEntity<LeaveBalance> getBalanceByType(@PathVariable int employeeId, @PathVariable String leaveType) throws EmployeeIdNotFoundException {
        return service.getBalanceByType(employeeId, leaveType)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/update")
    public void updateBalance(@RequestBody LeaveBalance balance) throws LeaveTypeNotFoundException {
        service.updateLeaveBalance(balance);
    }
    @GetMapping("/employee/{employeeId}")
    public List<LeaveBalance> getBalancesByEmployeeId(@PathVariable int employeeId) {
        return service.getLeaveBalancesByEmployeeId(employeeId);
    }
    @DeleteMapping("/delete/{employeeId}")
    public void deleteByEmployeeId(@PathVariable("employeeId") int employeeId) {
    	 service.deleteByEmployeeId(employeeId);
    }
}