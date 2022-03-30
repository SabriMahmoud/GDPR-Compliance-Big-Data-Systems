package com.example.gdpr.services;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.vault.core.RestOperationsCallback;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;
import org.springframework.web.client.RestOperations;

@Service
public class VaultCommunicationService {
	
	
	private static final String VAULT_TOKEN = "myroot"; 
	
	private static final String CLIENT_POLICY =  "# Manage auth methods broadly across Vault\npath \"auth/*\"\n{\n  capabilities = [\"create\", \"read\", \"update\", \"delete\", \"list\", \"sudo\"]\n}\n\n# Create, update, and delete auth methods\npath \"sys/auth/*\"\n{\n  capabilities = [\"create\", \"update\", \"delete\", \"sudo\"]\n}\n\n# List auth methods\npath \"sys/auth\"\n{\n  capabilities = [\"read\"]\n}\n\n# List existing policies\npath \"sys/policies/acl\"\n{\n  capabilities = [\"list\"]\n}\n\n# Create and manage ACL policies \npath \"sys/policies/acl/*\"\n{\n  capabilities = [\"create\", \"read\", \"update\", \"delete\", \"list\", \"sudo\"]\n}\n\n# List, create, update, and delete key/value secrets\npath \"secret/*\"\n{\n  capabilities = [\"create\", \"read\", \"update\", \"delete\", \"list\", \"sudo\"]\n}\n\n# Manage secrets engines\npath \"sys/mounts/*\"\n{\n  capabilities = [\"create\", \"read\", \"update\", \"delete\", \"list\", \"sudo\"]\n}\n\n# List existing secrets engines.\npath \"sys/mounts\"\n{\n  capabilities = [\"read\"]\n}\n\n# Read health checks\npath \"sys/health\"\n{\n  capabilities = [\"read\", \"sudo\"]\n}" ; 
	private static final String APPLICATIONS_POLYCY = "# Required: Get credentials from the database secrets engine for 'tester' role.\npath \"mongodb/creds/service1\"{\n     capabilities = [\"read\" , \"update\"]\n}\npath \"mongodb/roles\" {\n    capabilities = [ \"list\" ]\n}\npath \"mongodb/static-roles\" {\n   capabilities = [ \"list\"]\n}\n" ;
	
	private static final Map<String,String> policySingleton = Collections.singletonMap("policy",CLIENT_POLICY) ; 
	private static final Map<String,String> ApplicationPolicySingleton = Collections.singletonMap("policy",APPLICATIONS_POLYCY) ;
	 
	private static final String ROLE = "client" ; 
	private static final Map<String,String> tokenSingleton = Collections.singletonMap("policies",ROLE) ;
	
	
	
	
	public Map<String, Object> getDataBaseCredentials(VaultTemplate vaultTemplate){
        
	
		VaultResponse response = vaultTemplate.doWithVault(new RestOperationsCallback<VaultResponse>(){

			@Override
			public VaultResponse doWithRestOperations(RestOperations restOperations) {
				
				
	            HttpHeaders headers = new HttpHeaders();
	            //To Do get Vault Token from spring.cloud.vault.token 
	            headers.add("X-Vault-Token",VAULT_TOKEN);
	            
	            ResponseEntity<VaultResponse> responseEntity =   restOperations.exchange("/mongodb/creds/service1",HttpMethod.GET, new HttpEntity<Object>(headers), VaultResponse.class); 
	           
	            return responseEntity.getBody() ;
	            
				
			}
	    });
		
		return response.getData();
		
	}
	
	// second methode to get creds 
	
	public Map<String, Object> getDataBaseCreds(VaultTemplate vaultTemplate , String serviceName){
		VaultResponse response = vaultTemplate.read(String.format("mongodb/creds/%s",serviceName));
		return response.getData() ; 
	}
	
	
	
	public void createPolicy(VaultTemplate vaultTemplate){
		vaultTemplate.write(String.format("/sys/policies/acl/%s",ROLE),policySingleton); 
		vaultTemplate.write("/sys/policies/acl/Application",ApplicationPolicySingleton); 
	}
	
	public void createTokenAttachedToPolicy(VaultTemplate vaultTemplate){
		vaultTemplate.write(String.format("/auth/token/create"),tokenSingleton) ; 
		
	}
	
	

}
