package com.example.gdpr;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.vault.core.VaultSysOperations;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultMount;

import com.example.gdpr.services.MongoSecretIntegration;
import com.example.gdpr.services.VaultCommunicationService;
import com.mongodb.client.MongoClient;

@SpringBootApplication
public class ConsentManagementPlatformApplication {
	
	//Prepare Template To communicate with Vault 
	// VaultTemplate implements VaultOperations and has RestTemplate as an attribute  
	@Autowired
	VaultTemplate vaultTemplate ; 
	@Autowired
	MongoSecretIntegration mongoSecrets ; 
	@Autowired
	VaultCommunicationService vaultCommunication ; 
	
	
	

	

	
/********
 * 		To Do 
 * 		
 * 	Check 	Exceptions 
 * 
 * *******/
	
	public static void main(String[] args) {
		SpringApplication.run(ConsentManagementPlatformApplication.class, args);
	}
	

	@PostConstruct 
	public void run(){
		
		mongoSecrets.MountDataBaseSecretEngine(vaultTemplate);
		mongoSecrets.EnableVaultMongoConnection(vaultTemplate);	
		//verify error json cannot unmarshall string 
		mongoSecrets.CreateRole(vaultTemplate,"test");
		
		
		//To do try catch
		Map<String,Object> response = vaultCommunication.getDataBaseCredentials(vaultTemplate);
		System.out.println(response.get("username"));
		System.out.println(response.get("password"));
		vaultCommunication.createPolicy(vaultTemplate);
		// verify how to connect to DB after getting the token 
		vaultCommunication.createTokenAttachedToPolicy(vaultTemplate);
		
	}
	

	

}
