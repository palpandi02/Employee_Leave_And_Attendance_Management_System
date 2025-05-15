package com.example.demo.controller;
 
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.exception.NoResultFoundException;
import com.example.demo.exception.ShiftNotFoundException;
import com.example.demo.model.Shift;
import com.example.demo.service.ShiftService;
 
@RestController
@RequestMapping("/shifts")
public class ShiftController {
	@Autowired
    private ShiftService shiftService;
 
    @GetMapping("findall")
    public List<Shift> getAllShifts() {
        return shiftService.findAll();
    }
 
    @GetMapping("/shiftByShiftId/{id}")
    public Shift getShiftById(@PathVariable int id) throws ShiftNotFoundException {
        return shiftService.findById(id);
    }
 
    @PostMapping("/save")
    public void createShift(@RequestBody Shift shift) {
        shiftService.save(shift);
    }
 
    @DeleteMapping("/delete/{id}")
    public String deleteShift(@PathVariable int id) {
        return shiftService.deleteById(id);
    }
 
    @PostMapping("/requestSwap/{employeeId}")
    public String requestSwap(@PathVariable int employeeId) throws ShiftNotFoundException {
        return shiftService.requestSwap(employeeId);
    }
 
    @PostMapping("/processSwaps/{date}")
    public String processSwaps(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return shiftService.processSwapRequests(date);
    }
 
    @PostMapping("/approveSwap/{employeeId}")
    public String approveSwap(@PathVariable int employeeId) throws ShiftNotFoundException {
        return shiftService.approveSwapById(employeeId);
    }
 
    @PostMapping("/rejectSwap/{employeeId}")
    public String rejectSwap(@PathVariable int employeeId) {
        return shiftService.rejectSwapById(employeeId);
    }
 
    @GetMapping("/employee/{employeeId}")
    public List<Shift> getShiftsByEmployeeId(@PathVariable int employeeId) {
        return shiftService.getShiftsByEmployeeId(employeeId);
    }
 
    @GetMapping("/shiftCountByType/{employeeId}")
    public Map<String, Long> getShiftCountByType(@PathVariable int employeeId) {
        return shiftService.countShiftsByTypeForEmployee(employeeId);
    }
 
    @GetMapping("/byDate/{date}")
    public List<Shift> getShiftsByDate(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) throws NoResultFoundException {
        return shiftService.getShiftsByDate(date);
    }
 
    @GetMapping("/byEmployeeAndDate/{employeeId}/{date}")
    public ResponseEntity<Shift> getShiftByEmployeeIdAndDate(
            @PathVariable int employeeId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) throws NoResultFoundException {
        return shiftService.findByEmployeeIdAndDate(employeeId, date)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
 