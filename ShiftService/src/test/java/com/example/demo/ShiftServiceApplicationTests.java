package com.example.demo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.exception.NoResultFoundException;
import com.example.demo.exception.ShiftNotFoundException;
import com.example.demo.model.Shift;
import com.example.demo.repository.ShiftRepository;
import com.example.demo.service.ShiftServiceImp;

@SpringBootTest
public class ShiftServiceApplicationTests {

    @Mock
    private ShiftRepository repository;

    @InjectMocks
    private ShiftServiceImp service;

    private Shift shift;

    @BeforeEach
    void setup() {
        shift = new Shift(1, 100, "Day", false, LocalDate.of(2025, 5, 14));
    }

    @Test
    void testFindById_WhenShiftExists() throws ShiftNotFoundException {
        when(repository.findById(1)).thenReturn(Optional.of(shift));

        Shift result = service.findById(1);
        assertEquals(100, result.getEmployeeId());
    }

    @Test
    void testFindById_WhenShiftDoesNotExist() {
        when(repository.findById(2)).thenReturn(Optional.empty());

        assertThrows(ShiftNotFoundException.class, () -> service.findById(2));
    }

    @Test
    void testSave() {
        service.save(shift);
        verify(repository, times(1)).save(shift);
    }

    @Test
    void testDeleteById() {
        String result = service.deleteById(1);
        verify(repository, times(1)).deleteById(1);
        assertEquals("Shift Deleted", result);
    }

    @Test
    void testRequestSwap_WhenShiftExists() throws ShiftNotFoundException {
        shift.setSwapRequested(false);
        when(repository.findById(1)).thenReturn(Optional.of(shift));

        String result = service.requestSwap(1);
        assertTrue(shift.isSwapRequested());
        assertEquals("Swap request submitted for ID 1", result);
    }

    @Test
    void testRequestSwap_WhenShiftDoesNotExist() {
        when(repository.findById(2)).thenReturn(Optional.empty());

        assertThrows(ShiftNotFoundException.class, () -> service.requestSwap(2));
    }

    @Test
    void testGetShiftsByEmployeeId() {
        List<Shift> shifts = List.of(shift);
        when(repository.findShiftsByEmployeeId(100)).thenReturn(shifts);

        List<Shift> result = service.getShiftsByEmployeeId(100);
        assertEquals(1, result.size());
    }

    

    @Test
    void testFindByEmployeeIdAndDate_WhenFound() throws NoResultFoundException {
        when(repository.findByEmployeeIdAndDate(100, shift.getDate())).thenReturn(Optional.of(shift));

        Optional<Shift> result = service.findByEmployeeIdAndDate(100, shift.getDate());
        assertTrue(result.isPresent());
    }

    @Test
    void testFindByEmployeeIdAndDate_WhenNotFound() {
        when(repository.findByEmployeeIdAndDate(100, shift.getDate())).thenReturn(Optional.empty());

        assertThrows(NoResultFoundException.class,
            () -> service.findByEmployeeIdAndDate(100, shift.getDate()));
    }

    @Test
    void testGetShiftsByDate_WhenFound() throws NoResultFoundException {
        List<Shift> shifts = List.of(shift);
        when(repository.getAllShiftsByDate(shift.getDate())).thenReturn(shifts);

        List<Shift> result = service.getShiftsByDate(shift.getDate());
        assertEquals(1, result.size());
    }

    @Test
    void testGetShiftsByDate_WhenNotFound() {
        when(repository.getAllShiftsByDate(shift.getDate())).thenReturn(Collections.emptyList());

        assertThrows(NoResultFoundException.class, () -> service.getShiftsByDate(shift.getDate()));
    }
}