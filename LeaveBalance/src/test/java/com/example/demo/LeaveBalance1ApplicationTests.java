package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.exception.EmployeeIdNotFoundException;
import com.example.demo.exception.LeaveTypeNotFoundException;
import com.example.demo.model.LeaveBalance;
import com.example.demo.repository.LeaveBalanceRepository;
import com.example.demo.service.LeaveBalanceService;
import com.example.demo.util.LeaveTypes;
@SpringBootTest
class LeaveBalance1ApplicationTests {
	@Mock
    private LeaveBalanceRepository repo;

    @InjectMocks
	private LeaveBalanceService service;

    private LeaveBalance leaveBalance;

    @BeforeEach
    void setUp() {
        leaveBalance = new LeaveBalance(1, 101L, "Sick Leave", 10);
    }

    @Test
    void testInitializeLeaveBalance() {
        Map<String, Integer> leaveTypes = LeaveTypes.leaves();

        leaveTypes.forEach((type, count) -> {
            LeaveBalance lb = new LeaveBalance(0, 101L, type, count);
            when(repo.save(any(LeaveBalance.class))).thenReturn(lb);
        });

        service.initializeLeaveBalance(101L);

        verify(repo, times(leaveTypes.size())).save(any(LeaveBalance.class));
    }

    @Test
    void testGetBalanceByType_Found() throws EmployeeIdNotFoundException {
        when(repo.findByEmployeeIdAndLeaveType(101, "Sick Leave")).thenReturn(Optional.of(leaveBalance));

        Optional<LeaveBalance> result = service.getBalanceByType(101, "Sick Leave");

        assertTrue(result.isPresent());
        assertEquals("Sick Leave", result.get().getLeaveType());
    }

    @Test
    void testGetBalanceByType_NotFound() {
        when(repo.findByEmployeeIdAndLeaveType(101, "Sick Leave")).thenReturn(Optional.empty());

        assertThrows(EmployeeIdNotFoundException.class, () -> service.getBalanceByType(101, "Sick Leave"));
    }

    @Test
    void testUpdateLeaveBalance_Success() throws LeaveTypeNotFoundException {
        LeaveBalance updated = new LeaveBalance(1, 101L, "Sick Leave", 5);
        when(repo.findByEmployeeIdAndLeaveType(101L, "Sick Leave")).thenReturn(Optional.of(leaveBalance));
        when(repo.save(any(LeaveBalance.class))).thenReturn(updated);

        String result = service.updateLeaveBalance(updated);

        assertEquals("Leave balance updated successfully.", result);
        verify(repo).save(any(LeaveBalance.class));
    }

    @Test
    void testUpdateLeaveBalance_NotFound() {
        LeaveBalance updated = new LeaveBalance(1, 101L, "Sick Leave", 5);
        when(repo.findByEmployeeIdAndLeaveType(101L, "Sick Leave")).thenReturn(Optional.empty());

        assertThrows(LeaveTypeNotFoundException.class, () -> service.updateLeaveBalance(updated));
    }

    @Test
    void testGetLeaveBalancesByEmployeeId() throws EmployeeIdNotFoundException {
        when(repo.findByEmployeeId(101)).thenReturn(List.of(leaveBalance));

        List<LeaveBalance> result = service.getLeaveBalancesByEmployeeId(101);

        assertEquals(1, result.size());
        assertEquals("Sick Leave", result.get(0).getLeaveType());
    }

    @Test
    void testDeleteByEmployeeId() {
        doNothing().when(repo).deleteByEmployeeId(101);

        service.deleteByEmployeeId(101);

        verify(repo).deleteByEmployeeId(101);
    }
}
