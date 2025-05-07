package com.leave_management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leave_management.model.LeaveRequest;
import com.leave_management.service.LeaveService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/leave")
@RequiredArgsConstructor
public class LeaveController {
	@Autowired
    LeaveService service;

    @PostMapping("/apply")
    public ResponseEntity<String> apply(@RequestBody LeaveRequest req) {
        return ResponseEntity.ok(service.applyLeave(req));
    }

    @PostMapping("/approve/{id}")
    public ResponseEntity<String> approve(@PathVariable int id) {
        return ResponseEntity.ok(service.approveLeave(id));
    }

    @PostMapping("/reject/{id}")
    public ResponseEntity<String> reject(@PathVariable int id) {
        return ResponseEntity.ok(service.rejectLeave(id));
    }

    @GetMapping("/history")
    public List<LeaveRequest> all() {
        return service.getAllRequests();
    }

    @GetMapping("/history/{status}")
    public List<LeaveRequest> byStatus(@PathVariable String status) {
        return service.getRequestsByStatus(status);
    }
}