package com.example.gdpr.controller;

import java.util.Map;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.vault.core.VaultKeyValueOperationsSupport.KeyValueBackend;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;
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
	@Autowired
	VaultTemplate vaultTemplate ; 
	
	
	// To do getId from JSON Body 
	@GetMapping("/userId")
	public Document getUserById(@RequestParam String serviceName,@RequestParam String servicePassword , @RequestParam String id){
		
		//checking  for params validation 
		
		VaultResponse response = vaultTemplate.opsForKeyValue("secret", KeyValueBackend.KV_2).get("ApplicationServices");
		Map<String, Object> responseMap = response.getData() ;
		
		
		//Checking for existence of serviceName 
		if(responseMap.containsKey(serviceName)){
			
			//Checking for password validation 
			
			if(responseMap.get(serviceName).equals(servicePassword)){
				Document doc  = analyticService.findUserById(id,serviceName) ; 
				if(doc == null) return new Document().append("Error", "Unknown Role by Vault Contact Administrator for role creation")  ; 
				else return doc ;	
			}
			else return new Document().append("Error", "Authentication Failed try Another Password") ;

		}
		else return new Document().append("Error", "Authentication Failed, Try Another Service Name") ;
		 
	}

}