package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.Exception.EmployeeNotFound;
import com.example.demo.model.Employee;

public interface EmployeeService {
    Employee saveEmployee(Employee employee);
    List<Employee> getAllEmployees();
    Optional<Employee> getEmployeeById(int id) throws EmployeeNotFound;
    Optional<Employee> getEmployeeByEmail(String email) throws EmployeeNotFound;
    void deleteEmployee(int id);
    Employee updateEmployee(int id, Employee employeeDetails) throws EmployeeNotFound;
    boolean doesEmployeeExist(int id) throws EmployeeNotFound;
    public long countEmployee();
}
