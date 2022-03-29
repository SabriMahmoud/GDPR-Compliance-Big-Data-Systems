package com.example.gdpr.controller;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.gdpr.services.BeneficialService;
import com.mongodb.client.FindIterable;

@RestController
public class BeneficialController{
	
	@Autowired
	BeneficialService analyticService ; 
	
	// To do getId from JSON Body 
	@GetMapping("/userId")
	public Document getUserById(@RequestParam String serviceName,@RequestParam String servicePassword , @RequestParam String id){
		// To do check for params
		
		Document doc  = analyticService.findUserById(id) ; 
		if(doc == null) return new Document().append("Error", "Your Role is not known by Vault try another service name") ;
		else return doc ;  
	}

}