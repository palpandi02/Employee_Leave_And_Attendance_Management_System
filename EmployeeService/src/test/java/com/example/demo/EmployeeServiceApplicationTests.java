package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.Exception.EmployeeNotFound;
import com.example.demo.feignclient.LeaveClient;
import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.service.EmployeeServiceImpl;
@ExtendWith(MockitoExtension.class)//paka
@SpringBootTest
class EmployeeServiceApplicationTests {

	@Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private LeaveClient leaveServiceClient;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1);
        employee.setName("John Doe");
        employee.setEmail("john@example.com");
        employee.setPassword("password123");
        employee.setRole("Developer");
        employee.setDepartment("Engineering");
        employee.setContact("9876543210");
    }

    @Test
    void testSaveEmployee() {
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        Employee saved = employeeService.saveEmployee(employee);

        assertNotNull(saved);
        assertEquals("john@example.com", saved.getEmail());
        verify(leaveServiceClient).initializeLeaveBalance(saved.getId());
    }

    @Test
    void testDoesEmployeeExist_True() throws EmployeeNotFound {
        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));

        assertTrue(employeeService.doesEmployeeExist(1));
    }

   

    @Test
    void testGetAllEmployees() {
        when(employeeRepository.findAll()).thenReturn(List.of(employee));

        List<Employee> result = employeeService.getAllEmployees();

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
    }

    @Test
    void testGetEmployeeById_Found() throws EmployeeNotFound {
        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));

        Optional<Employee> result = employeeService.getEmployeeById(1);

        assertTrue(result.isPresent());
        assertEquals("john@example.com", result.get().getEmail());
    }

    @Test
    void testGetEmployeeById_NotFound() {
        when(employeeRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFound.class, () -> employeeService.getEmployeeById(1));
    }

    @Test
    void testGetEmployeeByEmail() throws EmployeeNotFound {
        when(employeeRepository.findByEmail("john@example.com")).thenReturn(Optional.of(employee));

        Optional<Employee> result = employeeService.getEmployeeByEmail("john@example.com");

        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getName());
    }

    @Test
    void testDeleteEmployee() {
        doNothing().when(leaveServiceClient).deleteByEmployeeId(1);
        doNothing().when(employeeRepository).deleteById(1);

        employeeService.deleteEmployee(1);

        verify(leaveServiceClient).deleteByEmployeeId(1);
        verify(employeeRepository).deleteById(1);
    }

    @Test
    void testUpdateEmployee_Success() throws EmployeeNotFound {
        Employee updatedDetails = new Employee();
        updatedDetails.setName("Jane Smith");
        updatedDetails.setEmail("jane@example.com");
        updatedDetails.setPassword("newpass");
        updatedDetails.setRole("Manager");
        updatedDetails.setDepartment("HR");
        updatedDetails.setContact("1234567890");

        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(updatedDetails);

        Employee updated = employeeService.updateEmployee(1, updatedDetails);

        assertEquals("Jane Smith", updated.getName());
        assertEquals("HR", updated.getDepartment());
    }

    @Test
    void testUpdateEmployee_NotFound() {
        when(employeeRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFound.class, () -> employeeService.updateEmployee(1, employee));
    }	

}