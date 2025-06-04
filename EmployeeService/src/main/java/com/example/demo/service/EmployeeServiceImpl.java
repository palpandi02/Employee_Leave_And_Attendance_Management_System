package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.demo.Exception.EmployeeNotFound;
import com.example.demo.feignclient.LeaveClient;
import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);
    private EmployeeRepository employeeRepository;
    private LeaveClient leaveServiceClient;
    public Employee saveEmployee(Employee employee) {
        logger.info("Saving new employee: {}", employee.getEmail());
        Employee savedEmployee = employeeRepository.save(employee);
        leaveServiceClient.initializeLeaveBalance(savedEmployee.getId());
        logger.info("Initialized leave balance for employeeId: {}", savedEmployee.getId());
        return savedEmployee;
    }

    public boolean doesEmployeeExist(int id) throws EmployeeNotFound {
        boolean exists = employeeRepository.findById(id).isPresent();
        logger.debug("Checked existence for employeeId {}: {}", id, exists);
        if(!exists) {
    		throw new EmployeeNotFound("Employee not found with id:"+id);
    	}
        return exists;
    }

    public List<Employee> getAllEmployees() {
        logger.info("Fetching all employees");
        return employeeRepository.findAll();
    }

    public Optional<Employee> getEmployeeById(int id) throws EmployeeNotFound {
        logger.info("Fetching employee by ID: {}", id);
        Optional<Employee> optional = employeeRepository.findById(id);
        if (optional.isPresent()) {
            logger.debug("Employee found: {}", optional.get().getEmail());
            return optional;
        } else {
            logger.warn("Employee not found with id: {}", id);
            throw new EmployeeNotFound("Employee not found with id " + id);
        }
    }

    public Optional<Employee> getEmployeeByEmail(String email) throws EmployeeNotFound {
        logger.info("Fetching employee by email: {}", email);
        Optional<Employee> optional = employeeRepository.findByEmail(email);
        if (optional.isPresent()) {
            logger.debug("Employee found: {}", optional.get().getEmail());
            return optional;
        } else {
            logger.warn("Employee not found with Email: {}", email);
            throw new EmployeeNotFound("Employee not found with id " + email);
        }
    }

    public void deleteEmployee(int id) {
        logger.info("Deleting employee with id: {}", id);
        leaveServiceClient.deleteByEmployeeId(id);
        employeeRepository.deleteById(id);
        logger.info("Deleted employee and associated leave data for id: {}", id);
    }

    public Employee updateEmployee(int id, Employee employeeDetails) throws EmployeeNotFound {
        logger.info("Updating employee with id: {}", id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Update failed: Employee not found with id: {}", id);
                    return new EmployeeNotFound("Employee not found with id " + id);
                });

        employee.setName(employeeDetails.getName());
        employee.setEmail(employeeDetails.getEmail());
        employee.setPassword(employeeDetails.getPassword());
        employee.setRole(employeeDetails.getRole());
        employee.setDepartment(employeeDetails.getDepartment());
        employee.setContact(employeeDetails.getContact());

        logger.info("Updated employee details for id: {}", id);
        return employeeRepository.save(employee);
    }
    public long countEmployee() {
    	return employeeRepository.count();
    }
}
