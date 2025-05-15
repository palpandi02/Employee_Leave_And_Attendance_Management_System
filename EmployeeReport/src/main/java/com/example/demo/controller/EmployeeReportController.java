package com.example.demo.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.EmployeeIdNotFound;
import com.example.demo.model.EmployeeReport;
import com.example.demo.service.EmployeeReportService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/report")
@AllArgsConstructor
public class EmployeeReportController {

    
    private EmployeeReportService reportService;

    @GetMapping("/{employeeId}")
    public EmployeeReport getReport(@PathVariable int employeeId) throws EmployeeIdNotFound {
        return reportService.generateReportByEmployeeId(employeeId);
    }
}
