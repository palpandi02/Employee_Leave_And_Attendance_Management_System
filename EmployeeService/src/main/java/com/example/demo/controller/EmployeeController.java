package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Exception.EmployeeNotFound;
import com.example.demo.model.Employee;
import com.example.demo.service.EmployeeServiceImpl;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/employees")
@AllArgsConstructor
public class EmployeeController {
	private EmployeeServiceImpl employeeService;
	@GetMapping("/check/{id}")
	public boolean doesEmployeeExist(@PathVariable int id) throws EmployeeNotFound {
	    return employeeService.getEmployeeById(id).isPresent();
	}
	
	@PostMapping("/save")
	public Employee createEmployee(@RequestBody Employee employee) {
		return employeeService.saveEmployee(employee);
	}

	
	@GetMapping("/getAll")
	public List<Employee> getAllEmployees() {
		return employeeService.getAllEmployees();
	}

	
	@GetMapping("/{id}")
	public Employee getEmployeeById(@PathVariable Integer id) throws EmployeeNotFound {
	    Optional<Employee> optional=employeeService.getEmployeeById(id);
	    if(optional.isPresent()) {
		 return optional.get();}
	    else {
	       throw new EmployeeNotFound("Employee Not Found");}
	}

	
	@GetMapping("/email/{email}")
	public Optional<Employee> getEmployeeByEmail(@PathVariable String email) throws EmployeeNotFound {
		return employeeService.getEmployeeByEmail(email);
	}

	
	@PutMapping("/update/{id}")
	public Employee updateEmployee(@PathVariable Integer id, @RequestBody Employee employeeDetails) throws EmployeeNotFound {
		return employeeService.updateEmployee(id, employeeDetails);
	}
	@GetMapping("/count")
	public long getEmployeeCount() {
		return employeeService.countEmployee();
	}
	@DeleteMapping("/delete/{id}")
	public void deleteEmployee(@PathVariable Integer id) {
		employeeService.deleteEmployee(id);
	}
}

