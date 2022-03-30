package com.example.gdpr;


import javax.annotation.PostConstruct;

import org.bson.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.data.mongodb.UncategorizedMongoDbException;
import org.springframework.vault.core.VaultKeyValueOperations;
import org.springframework.vault.core.VaultKeyValueOperationsSupport.KeyValueBackend;
import org.springframework.vault.core.VaultSysOperations;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultMount;
import org.springframework.vault.support.VaultResponse;

import java.net.URISyntaxException;
import java.sql.*;
import java.util.HashMap;

import com.example.gdpr.configuration.MongoConfiguration;
import com.example.gdpr.entities.Credentials;
import com.example.gdpr.services.CredentialsService;
import com.example.gdpr.services.DataProtectionMongo;
import com.example.gdpr.services.MongoSecretIntegration;
import com.example.gdpr.services.VaultCommunicationService;
import com.fasterxml.jackson.core.JsonParser;



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
	@Autowired
	CredentialsService credentialsService ; 
	
	@Value("spring.data.mongodb.username")
	private  String dataBaseUserName ; 
	@Value("spring.data.mongodb.password")
	private String dataBasePassword ; 
	
	//Secure path 
	
	private static final String MONGO_VAULT_CREDENTIALS_PATH = "mongodbSecrets/app" ; 

	

	
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
		
//		vaultCommunication.createPolicy(vaultTemplate);
		String serviceName = "service1";
		mongoSecrets.CreateRole(vaultTemplate,serviceName);
		System.out.println(String.format("Service : %s Created Successfully ",serviceName));
		
		
		//secure credentials in Vault
		
		Credentials dataBaseCredentials = new Credentials(dataBaseUserName,dataBasePassword) ; 
		vaultTemplate.write(MONGO_VAULT_CREDENTIALS_PATH,dataBaseCredentials);
		
//		System.out.println(vaultTemplate.list("mongodb/roles"));
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
	

	


