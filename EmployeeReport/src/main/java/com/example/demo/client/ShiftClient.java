package com.example.demo.client;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "Shift-Service", url = "http://localhost:1004/shifts")
public interface ShiftClient {

	@GetMapping("/employee/{employeeId}")
    public List<ShiftClient> getShiftsByEmployeeId(@PathVariable int employeeId);
}
