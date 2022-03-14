package com.example.gdpr.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.vault.core.VaultSysOperations;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultMount;

@Service
public class MongoSecretIntegration {
	
	
	private static final int MONGODB_PORT = 27017;
	//container name 
	private static final String MONGODB_HOST = "mongodb3";
	private static final String MONGODB_USER = "mdbadmin" ; 
	private static final String MONGODB_PASS = "hQ97T9JJKZoqnFn2NXE" ; 
	private static final String BACKEND_ENGINE = "mongodb" ; 
	private static final String BACKEND_PLUGIN_NAME="mongodb-database-plugin" ; 
	
	
	private static final String DATABASE_NAME = "Bankerise" ; 
	
	private static final String ROOT_CREDENTIALS = String.format("mongodb://%s:%s@%s:%d/admin?ssl=false", MONGODB_USER,MONGODB_PASS,MONGODB_HOST, MONGODB_PORT);
	

	private static final String TIME_TO_LIVE = "1h" ; 
	private static final String MAX_TIME_TO_LIVE ="24h" ; 
	
	// To Do verify custom statements 
	private static final String ROLE_STATEMENT = String.format("\"{ \\\"db\\\": \\\"%s\\\", \\\"roles\\\": [{ \\\"role\\\": \\\"readWrite\\\" }, {\\\"role\\\": \\\"read\\\", \\\"db\\\": \\\"foo\\\"}] }\"",DATABASE_NAME) ;  
	
	
	
	
	
	public void MountDataBaseSecretEngine(VaultTemplate vaultTemplate){
		
		VaultSysOperations sysOperations = vaultTemplate.opsForSys();
		if (sysOperations != null && !sysOperations.getMounts().containsKey("mongodb/")) {
		      sysOperations.mount("mongodb", VaultMount.create("database"));
		    }
	}
	
	
	
	
    //Enabling Connection between Vault Back-end Secret Engine  and MongoDb
    //Be careful you may encounter Read Time Out Exception from Socket if both containers are not in the same network  
	
	//to do *** try catch ***
	public void EnableVaultMongoConnection(VaultTemplate vaultTemplate) {
	    HashMap<String, String> secretEngineMap = new HashMap<String, String>() ;
	    
	    secretEngineMap.put("plugin_name", BACKEND_PLUGIN_NAME) ; 
	    secretEngineMap.put("connection_url", ROOT_CREDENTIALS) ;
	    secretEngineMap.put("allowed_roles", "*") ; 
	    secretEngineMap.put("username",MONGODB_USER) ;
	    secretEngineMap.put("password",MONGODB_PASS) ;
	    
	    
	    vaultTemplate.write(String.format("%s/config/%s",BACKEND_ENGINE,DATABASE_NAME),secretEngineMap); 
	}
	
	public void CreateRole(VaultTemplate vaultTemplate,String roleName){
		HashMap<String,Object> roleMap = new HashMap<String,Object>();
		
		List<String> statements = new ArrayList<String>() ; 
		
		statements.add(ROLE_STATEMENT) ; 
		
		
		
		roleMap.put("db_name",DATABASE_NAME) ;
		roleMap.put("creation_statements", statements) ; 
		roleMap.put("default_ttl",TIME_TO_LIVE) ;
		roleMap.put("max_ttl",MAX_TIME_TO_LIVE) ; 
		
		
		vaultTemplate.write(String.format("%s/roles/%s",BACKEND_ENGINE,roleName),roleMap);
		
		
		
		
	}
	
	
	
}
