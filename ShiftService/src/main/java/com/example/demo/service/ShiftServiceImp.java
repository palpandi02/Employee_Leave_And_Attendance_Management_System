package com.example.demo.service;
 
import com.example.demo.model.Shift;
import com.example.demo.repository.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
 
@Service
public class ShiftServiceImp implements ShiftService {
 
    @Autowired
    private ShiftRepository repository;
 
    @Override
    public List<Shift> findAll() {
        return repository.findAll();
    }
 
    @Override
    public Shift findById(int id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Shift not found"));
    }
 
    @Override
    public void save(Shift shift) {
        repository.save(shift);
    }
 
    @Override
    public String deleteById(int id) {
        repository.deleteById(id);
        return "Shift Deleted";
    }
 
    @Override
    public String requestSwap(int employeeId) {
        Optional<Shift> optionalShift = repository.findByEmployeeId(employeeId);
        if (optionalShift.isPresent()) {
            Shift shift = optionalShift.get();
            shift.setSwapRequested(true);
            repository.save(shift);
            return "Swap request submitted for employee ID " + employeeId;
        }
        return "Shift not found for employee ID " + employeeId;
    }
 
    @Override
    public String processSwapRequests() {
        List<Shift> requestedShifts = repository.findBySwapRequestedTrue();
        for (int i = 0; i < requestedShifts.size(); i++) {
            Shift shift1 = requestedShifts.get(i);
            for (int j = i + 1; j < requestedShifts.size(); j++) {
                Shift shift2 = requestedShifts.get(j);
                if (!shift1.getShiftType().equals(shift2.getShiftType())) {
                    // Swap employee IDs
                    int tempEmployeeId = shift1.getEmployeeId();
                    shift1.setEmployeeId(shift2.getEmployeeId());
                    shift2.setEmployeeId(tempEmployeeId);
 
                    // Reset swapRequested flags
                    shift1.setSwapRequested(false);
                    shift2.setSwapRequested(false);
 
                    repository.save(shift1);
                    repository.save(shift2);
 
                    return "Swap processed between employee ID " + shift1.getEmployeeId() + " and employee ID " + shift2.getEmployeeId();
                }
            }
        }
        return "No matching swap requests found";
    }
 
    @Override
    public String approveSwapByEmployeeId(int employeeId) {
        Optional<Shift> optionalShift = repository.findByEmployeeId(employeeId);
        if (optionalShift.isPresent()) {
            Shift shift = optionalShift.get();
            if (shift.isSwapRequested()) {
                shift.setSwapRequested(false);
                repository.save(shift);
                return "Swap approved for employee ID " + employeeId;
            } else {
                return "No swap request found for employee ID " + employeeId;
            }
        }
        return "Shift not found for employee ID " + employeeId;
    }
 
    @Override
    public String rejectSwapByEmployeeId(int employeeId) {
        Optional<Shift> optionalShift = repository.findByEmployeeId(employeeId);
        if (optionalShift.isPresent()) {
            Shift shift = optionalShift.get();
            if (shift.isSwapRequested()) {
                shift.setSwapRequested(false);
                repository.save(shift);
                return "Swap request rejected for employee ID " + employeeId;
            } else {
                return "No swap request found for employee ID " + employeeId;
            }
        }
        return "Shift not found for employee ID " + employeeId;
    }
    public List<Shift> getShiftsByEmployeeId(int employeeId) {
        return repository.findShiftsByEmployeeId(employeeId);
    }
}