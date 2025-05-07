package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import com.example.demo.model.Employee;

public interface EmployeeService {
    Employee saveEmployee(Employee employee);
    List<Employee> getAllEmployees();
    Optional<Employee> getEmployeeById(Integer id);
    Optional<Employee> getEmployeeByEmail(String email);
    void deleteEmployee(Integer id);
    Employee updateEmployee(Integer id, Employee employeeDetails);
}
