package com.example.gdpr.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.gdpr.entities.User;
import com.example.gdpr.services.UserService;

@RestController
public class UserController {
	
	
	@Autowired
	UserService userService ; 

	
	
	@GetMapping("/users")
	public List<User> getAllUsers(){
		return userService.findAllUsers();
		
	}
	@PostMapping("/addUser")
	public String addUser(@RequestBody User user) {
		return userService.addUser(user);
	}

}
