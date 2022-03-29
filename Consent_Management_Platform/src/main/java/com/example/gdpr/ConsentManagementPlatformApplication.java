package com.example.gdpr;


import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.data.mongodb.UncategorizedMongoDbException;

import org.springframework.vault.core.VaultTemplate;

import java.sql.*;

import com.example.gdpr.configuration.MongoConfiguration;
import com.example.gdpr.services.DataProtectionMongo;
import com.example.gdpr.services.MongoSecretIntegration;
import com.example.gdpr.services.VaultCommunicationService;



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
	@Autowired
	DataProtectionMongo protectData ; 
	
	@Autowired
	MongoConfiguration mongoConfig ; 
	

	

	
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
	public void run() throws SQLException{
		
		mongoSecrets.MountDataBaseSecretEngine(vaultTemplate);
		mongoSecrets.EnableVaultMongoConnection(vaultTemplate);	
		//verify error json cannot unmarshall string 
		mongoSecrets.CreateRole(vaultTemplate,"teee");
		
		
		

//		vaultCommunication.createPolicy(vaultTemplate);
//		
//		// verify how to connect to DB after getting the token 
//		vaultCommunication.createTokenAttachedToPolicy(vaultTemplate);

		
		//create view for all users 
		try {
			protectData.createView();
		}catch(UncategorizedMongoDbException mongoException){
			
			System.out.println("-----------------------------") ; 
			System.out.println("Cannot Create a View , UsersView Already Exists") ; 
			System.out.println("-----------------------------") ; 
			
		}		

		}
		      

		

	}
	

	


