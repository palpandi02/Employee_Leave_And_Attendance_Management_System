package com.example.demo.service;
 
import com.example.demo.exception.NoResultFoundException;
import com.example.demo.exception.ShiftNotFoundException;
import com.example.demo.model.Shift;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
 
public interface ShiftService {
    List<Shift> findAll();
    Shift findById(int id) throws ShiftNotFoundException;
    void save(Shift shift);
    String deleteById(int id);
    String requestSwap(int employeeId) throws ShiftNotFoundException;
    String processSwapRequests(LocalDate date);
    String approveSwapById(int employeeId) throws ShiftNotFoundException;
    String rejectSwapById(int employeeId);
    List<Shift> getShiftsByEmployeeId(int employeeId);
    Map<String, Long> countShiftsByTypeForEmployee(int employeeId);
    Optional<Shift> findByEmployeeIdAndDate(int employeeId, LocalDate date) throws NoResultFoundException;
    List<Shift> getShiftsByDate(LocalDate date) throws NoResultFoundException;
}