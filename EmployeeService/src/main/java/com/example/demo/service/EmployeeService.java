package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.Exception.EmployeeIdNotFound;
import com.example.demo.model.Employee;

public interface EmployeeService {
    Employee saveEmployee(Employee employee);
    List<Employee> getAllEmployees();
    Optional<Employee> getEmployeeById(int id) throws EmployeeIdNotFound;
    Optional<Employee> getEmployeeByEmail(String email);
    void deleteEmployee(int id);
    Employee updateEmployee(int id, Employee employeeDetails) throws EmployeeIdNotFound;
    boolean doesEmployeeExist(int id);
}
