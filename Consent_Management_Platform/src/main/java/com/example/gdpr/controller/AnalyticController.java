package com.example.gdpr.controller;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.gdpr.services.AnalyticService;
import com.mongodb.client.FindIterable;

@RestController
public class AnalyticController{
	
	@Autowired
	AnalyticService analyticService ; 
	
	// To do getId from JSON Body 
	@GetMapping("/userId")
	public Document getUserById(@RequestParam String serviceName,@RequestParam String servicePassword , @RequestParam String id){
		// To do check for params
		return analyticService.findUserById(id); 
	}

}