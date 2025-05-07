package com.example.demo.controller;
 
import com.example.demo.model.Shift;
import com.example.demo.service.ShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
 
@RestController
@RequestMapping("/shifts")
public class ShiftController {
 
    @Autowired
    private ShiftService shiftService;
 
    @GetMapping
    public List<Shift> getAllShifts() {
        return shiftService.findAll();
    }
 
    @GetMapping("/{id}")
    public Shift getShiftById(@PathVariable int id) {
        return shiftService.findById(id);
    }
 
    @PostMapping
    public void createShift(@RequestBody Shift shift) {
        shiftService.save(shift);
    }
 
    @DeleteMapping("/{id}")
    public String deleteShift(@PathVariable int id) {
        return shiftService.deleteById(id);
    }
 
    @PostMapping("/requestSwap/{employeeId}")
    public String requestSwap(@PathVariable int employeeId) {
        return shiftService.requestSwap(employeeId);
    }
 
    @PostMapping("/processSwaps")
    public String processSwaps() {
        return shiftService.processSwapRequests();
    }
 
    @PostMapping("/approveSwap/{employeeId}")
    public String approveSwap(@PathVariable int employeeId) {
        return shiftService.approveSwapByEmployeeId(employeeId);
    }
 
    @PostMapping("/rejectSwap/{employeeId}")
    public String rejectSwap(@PathVariable int employeeId) {
        return shiftService.rejectSwapByEmployeeId(employeeId);
    }
}
 