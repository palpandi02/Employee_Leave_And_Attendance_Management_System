package com.example.demo.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.exception.NoResultFoundException;
import com.example.demo.exception.ShiftNotFoundException;
import com.example.demo.model.Shift;
import com.example.demo.repository.ShiftRepository;

@Service
public class ShiftServiceImp implements ShiftService {

    private static final Logger logger = LoggerFactory.getLogger(ShiftServiceImp.class);
    @Autowired
    private ShiftRepository repository;

    @Override
    public List<Shift> findAll() {
        logger.info("Fetching all shifts");
        return repository.findAll();
    }

    @Override
    public Shift findById(int id) throws ShiftNotFoundException {
        logger.info("Fetching shift by ID: {}", id);
        Optional<Shift> optional = repository.findById(id);
        if (optional.isEmpty()) {
            logger.warn("Shift not found with ID: {}", id);
            throw new ShiftNotFoundException("Shift not found");
        }
        return optional.get();
    }

    @Override
    public void save(Shift shift) {
        logger.info("Saving shift for employeeId: {}", shift.getEmployeeId());
        repository.save(shift);
    }

    @Override
    public String deleteById(int id) {
        logger.info("Deleting shift with ID: {}", id);
        repository.deleteById(id);
        return "Shift Deleted";
    }

    @Override
    public String requestSwap(int id) throws ShiftNotFoundException {
        logger.info("Requesting shift swap for Id: {}", id);
        Optional<Shift> optionalShift = repository.findById(id);
        if (optionalShift.isPresent()) {
            Shift shift = optionalShift.get();
            shift.setSwapRequested(true);
            repository.save(shift);
            logger.info("Swap request submitted for id: {}", id);
            return "Swap request submitted for ID " + id;
        } else {
            logger.warn("Shift not found for id: {}", id);
            throw new ShiftNotFoundException("Shift not found for id  " + id);
        }
    }

    @Override
    public String processSwapRequests(LocalDate date) {

        logger.info("Processing shift swap requests for date: {}", date);

        List<Shift> requestedShifts = repository.findBySwapRequestedTrue();

        // Filter shifts by the given date
        requestedShifts = requestedShifts.stream()
                .filter(shift -> date.equals(shift.getDate()))
                .toList();

        List<Shift> dayShifts = new ArrayList<>();
        List<Shift> nightShifts = new ArrayList<>();

        for (Shift shift : requestedShifts) {
            if ("Day".equalsIgnoreCase(shift.getShiftType())) {
                dayShifts.add(shift);
            } else if ("Night".equalsIgnoreCase(shift.getShiftType())) {
                nightShifts.add(shift);
            }
        }

        int swapCount = Math.min(dayShifts.size(), nightShifts.size());
        if (swapCount == 0) {
            logger.info("No matching Day-Night swap requests available for date: {}", date);
            return "No matching Day-Night swap requests available for " + date + ".";
        }

        for (int i = 0; i < swapCount; i++) {
            Shift day = dayShifts.get(i);
            Shift night = nightShifts.get(i);

            String temp = day.getShiftType();
            day.setShiftType(night.getShiftType());
            night.setShiftType(temp);

            day.setSwapRequested(false);
            night.setSwapRequested(false);

            repository.save(day);
            repository.save(night);
        }

        logger.info("{} Day-Night swaps processed successfully for date {}", swapCount, date);
        return swapCount + " Day-Night swaps processed successfully for " + date + ".";
    }

    @Override
    public String approveSwapById(int employeeId) throws ShiftNotFoundException {
        logger.info("Approving swap for employeeId: {}", employeeId);
        Optional<Shift> optionalShift = repository.findById(employeeId);
        if (optionalShift.isPresent()) {
            Shift shift = optionalShift.get();
            if (shift.isSwapRequested()) {
                shift.setSwapRequested(false);
                repository.save(shift);
                logger.info("Swap approved for employeeId: {}", employeeId);
                return "Swap approved for employee ID " + employeeId;
            } else {
                logger.info("No swap request found for employeeId: {}", employeeId);
                return "No swap request found for employee ID " + employeeId;
            }
        } else {
            logger.warn("Shift not found for employeeId: {}", employeeId);
            throw new ShiftNotFoundException("Shift not found for employee ID " + employeeId);
        }
    }

    @Override
    public String rejectSwapById(int employeeId) {
        logger.info("Rejecting swap for employeeId: {}", employeeId);
        Optional<Shift> optionalShift = repository.findById(employeeId);
        if (optionalShift.isPresent()) {
            Shift shift = optionalShift.get();
            if (shift.isSwapRequested()) {
                shift.setSwapRequested(false);
                repository.save(shift);
                logger.info("Swap request rejected for employeeId: {}", employeeId);
                return "Swap request rejected for employee ID " + employeeId;
            } else {
                logger.info("No swap request found for employeeId: {}", employeeId);
                return "No swap request found for employee ID " + employeeId;
            }
        }
        logger.warn("Shift not found for employeeId: {}", employeeId);
        return "Shift not found for employee ID " + employeeId;
    }

    @Override
    public List<Shift> getShiftsByEmployeeId(int employeeId) {
        logger.info("Fetching shifts for employeeId: {}", employeeId);
        return repository.findShiftsByEmployeeId(employeeId);
    }

    @Override
    public Map<String, Long> countShiftsByTypeForEmployee(int employeeId) {
        logger.info("Counting shifts by type for employeeId: {}", employeeId);
        List<Object[]> results = repository.countShiftsByTypeForEmployee(employeeId);
        Map<String, Long> map = new HashMap<>();
        for (Object[] row : results) {
            String type = (String) row[0];
            Long count = (Long) row[1];
            map.put(type, count);
        }
        return map;
    }

    @Override
    public Optional<Shift> findByEmployeeIdAndDate(int employeeId, LocalDate date) throws NoResultFoundException {
        logger.info("Fetching shift for employeeId: {} on date: {}", employeeId, date);
        Optional<Shift> optional=repository.findByEmployeeIdAndDate(employeeId, date);
        if(optional.isPresent())
        	return optional;
        else
        	throw new  NoResultFoundException("No Result Found with Employee id:"+employeeId+" with date :"+date);
    }
	@Override
    public List<Shift> getShiftsByDate(LocalDate date) throws NoResultFoundException {
        logger.info("Fetching all shifts for date: {}", date);
        List<Shift> optional= repository.getAllShiftsByDate(date);
        if(!optional.isEmpty())
        	return optional;
        else
        	throw new NoResultFoundException("No Result Found with date :"+date);
    }
}
