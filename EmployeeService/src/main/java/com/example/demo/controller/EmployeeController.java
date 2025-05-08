package com.example.demo.controller;

import com.example.demo.Exception.EmployeeIdNotFound;
import com.example.demo.model.Employee;
import com.example.demo.service.EmployeeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

	@Autowired
	private EmployeeServiceImpl employeeService;
	@GetMapping("/check/{id}")
	public boolean doesEmployeeExist(@PathVariable Integer id) {
	    return employeeService.getEmployeeById(id).isPresent();
	}
	//  http://localhost:8082/employees
	@PostMapping
	public Employee createEmployee(@RequestBody Employee employee) {
		return employeeService.saveEmployee(employee);
	}

	//  http://localhost:8082/employees
	@GetMapping
	public List<Employee> getAllEmployees() {
		return employeeService.getAllEmployees();
	}

	//  http://localhost:8082/employees/{id}
	@GetMapping("/{id}")
	public Optional<Employee> getEmployeeById(@PathVariable Integer id) {
		return employeeService.getEmployeeById(id);
	}

	//  http://localhost:8082/employees/email/{email}
	@GetMapping("/email/{email}")
	public Optional<Employee> getEmployeeByEmail(@PathVariable String email) {
		return employeeService.getEmployeeByEmail(email);
	}

	//  http://localhost:8082/employees/{id}
	@PutMapping("/update/{id}")
	public Employee updateEmployee(@PathVariable Integer id, @RequestBody Employee employeeDetails) throws EmployeeIdNotFound {
		return employeeService.updateEmployee(id, employeeDetails);
	}

	//  http://localhost:8082/employees/{id}
	@DeleteMapping("/delete/{id}")
	public void deleteEmployee(@PathVariable Integer id) {
		employeeService.deleteEmployee(id);
	}
}

