package com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.entity.UserInfo;
import com.feignclient.Employeeclient;
import com.repository.UserInfoRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
	private UserInfoRepository repository;
	private PasswordEncoder passwordEncoder;
	private Employeeclient employeeClient;
 
	public String addUser(UserInfo userInfo) {
		String name = userInfo.getName();
		UserInfo obj1 = repository.findByName(name).orElse(null);
		
 
		if (obj1 == null) {
			//System.out.println("Checking if employee exists with ID: " + userInfo.getEmployeeId());
			boolean employeeExists = employeeClient.doesEmployeeExist(userInfo.getEmployeeId());
			//System.out.println("Employee exists: " + employeeExists);
 
			if (employeeExists) {
				userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
				repository.save(userInfo);
				return "Registration Successfully";
			} else {
				return "Employee does not exist.";
			}
		} else {
			return "This UserName is Already Registered.";
		}
	}
 

	public String getRoles(String username) {
		UserInfo obj2 = repository.findByName(username).orElse(null);
		if (obj2 != null) {
			return obj2.getRole();
		}
		return "Not Found";
	}
}
