package com.example.gdpr;

import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.vault.core.VaultSysOperations;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultMount;

import com.example.gdpr.configuration.MongoSecretIntegration;

@SpringBootApplication
public class ConsentManagementPlatformApplication {
	
	//Prepare Template To communicate with Vault 
	// VaultTemplate implements VaultOperations and has RestTemplate as an attribute  
	@Autowired
	VaultTemplate vaultTemplate ; 
	@Autowired
	MongoSecretIntegration mongoSecrets ; 
	
	
	

	

	
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
	}
	

	

}
