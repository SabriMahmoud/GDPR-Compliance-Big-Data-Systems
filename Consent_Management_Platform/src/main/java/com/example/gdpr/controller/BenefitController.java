package com.example.gdpr.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.gdpr.entities.User;
import com.example.gdpr.services.UserService;
import com.example.gdpr.services.VaultCommunicationService;

@RestController
public class BenefitController {
	
	
	@Autowired
	UserService userService ; 
	@Autowired
	VaultCommunicationService vaultCommunication ;
	@Autowired
	VaultTemplate vaultTemplate ; 
	
	@GetMapping("/users")
	public List<User> getAllUsers(){
		return userService.findAllUsers();
		
	}
	@PostMapping("/user")
	public String addUser(@RequestBody User user) {
		return userService.addUser(user);
	}
	
	@GetMapping("/creds")
	public Map<String,Object> getDataBaseCredentials(@RequestParam String serviceName , @RequestParam String servicePassword){
		//To Do verify serviceName with servicePassword
		Map<String,Object> response = vaultCommunication.getDataBaseCredentials(vaultTemplate);
		return  response ;
		
	}

}
