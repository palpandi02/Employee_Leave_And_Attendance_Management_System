package com.example.demo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "EMPLOYEESERVICE", path = "/employees")
public interface EmployeeServiceClient {
	@GetMapping("/check/{id}")
	public boolean doesEmployeeExist(@PathVariable int id);
}
