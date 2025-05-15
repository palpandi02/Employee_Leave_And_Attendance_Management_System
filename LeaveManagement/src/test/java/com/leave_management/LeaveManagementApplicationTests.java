package com.leave_management;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.leave_management.client.LeaveBalanceClient;
import com.leave_management.dto.LeaveBalanceDTO;
import com.leave_management.exception.AlreadyProcessedException;
import com.leave_management.exception.InsufficientLeaveBalanceException;
import com.leave_management.exception.RequestNotFoundException;
import com.leave_management.model.LeaveRequest;
import com.leave_management.repository.LeaveRequestRepository;
import com.leave_management.service.LeaveService;

class LeaveManagementApplicationTests {

    @InjectMocks
    private LeaveService leaveService;

    @Mock
    private LeaveRequestRepository requestRepo;

    @Mock
    private LeaveBalanceClient balanceClient;

    private LeaveRequest sampleRequest;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        sampleRequest = new LeaveRequest();
        sampleRequest.setLeaveId(1);
        sampleRequest.setEmployeeId(100);
        sampleRequest.setLeaveType("Sick");
        sampleRequest.setStartDate(new Date(System.currentTimeMillis()));
        sampleRequest.setEndDate(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(2)));
        sampleRequest.setStatus("Pending");
    }

    @Test
    void testApplyLeaveSuccess() throws Exception {
        LeaveBalanceDTO balanceDTO = new LeaveBalanceDTO(1, 100, "Sick", 5);

        when(balanceClient.getBalanceByType(100, "Sick")).thenReturn(balanceDTO);
        when(requestRepo.save(any(LeaveRequest.class))).thenReturn(sampleRequest);

        String result = leaveService.applyLeave(sampleRequest);

        assertEquals("Leave applied.", result);
        verify(requestRepo, times(1)).save(sampleRequest);
    }

    @Test
    void testApplyLeaveInsufficientBalance() {
        LeaveBalanceDTO balanceDTO = new LeaveBalanceDTO(1, 100, "Sick", 1);
        when(balanceClient.getBalanceByType(100, "Sick")).thenReturn(balanceDTO);

        assertThrows(InsufficientLeaveBalanceException.class, () -> leaveService.applyLeave(sampleRequest));
    }

    @Test
    void testApproveLeaveSuccess() throws Exception {
        LeaveBalanceDTO balanceDTO = new LeaveBalanceDTO(1, 100, "Sick", 5);
        when(requestRepo.findById(1)).thenReturn(Optional.of(sampleRequest));
        when(balanceClient.getBalanceByType(100, "Sick")).thenReturn(balanceDTO);
        when(requestRepo.save(any(LeaveRequest.class))).thenReturn(sampleRequest);

        String result = leaveService.approveLeave(1);

        assertEquals("Approved", result);
        verify(balanceClient, times(1)).updateLeaveBalance(any(LeaveBalanceDTO.class));
    }

    @Test
    void testApproveLeaveInsufficientBalance() {
        LeaveBalanceDTO balanceDTO = new LeaveBalanceDTO(1, 100, "Sick", 1);
        when(requestRepo.findById(1)).thenReturn(Optional.of(sampleRequest));
        when(balanceClient.getBalanceByType(100, "Sick")).thenReturn(balanceDTO);

        assertThrows(InsufficientLeaveBalanceException.class, () -> leaveService.approveLeave(1));
    }

    @Test
    void testRejectLeaveSuccess() throws Exception {
        when(requestRepo.findById(1)).thenReturn(Optional.of(sampleRequest));
        when(requestRepo.save(any(LeaveRequest.class))).thenReturn(sampleRequest);

        String result = leaveService.rejectLeave(1);

        assertEquals("Rejected", result);
    }

    @Test
    void testRejectLeaveAlreadyProcessed() {
        sampleRequest.setStatus("Approved");
        when(requestRepo.findById(1)).thenReturn(Optional.of(sampleRequest));

        assertThrows(AlreadyProcessedException.class, () -> leaveService.rejectLeave(1));
    }

    @Test
    void testApproveLeaveNotFound() {
        when(requestRepo.findById(1)).thenReturn(Optional.empty());

        assertThrows(RequestNotFoundException.class, () -> leaveService.approveLeave(1));
    }
}

