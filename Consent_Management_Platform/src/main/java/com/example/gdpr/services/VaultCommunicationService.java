package com.example.gdpr.services;

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

	
	public Map<String, Object> getDataBaseCredentials(VaultTemplate vaultTemplate){
        
		
		
		
		VaultResponse response = vaultTemplate.doWithVault(new RestOperationsCallback<VaultResponse>(){
        
			 
			

			@Override
			public VaultResponse doWithRestOperations(RestOperations restOperations) {
				
				
	            HttpHeaders headers = new HttpHeaders();
	            //To Do get Vault Token from spring.cloud.vault.token 
	            headers.add("X-Vault-Token",VAULT_TOKEN);
	            
	            ResponseEntity<VaultResponse> responseEntity =   restOperations.exchange("/mongodb/creds/test",HttpMethod.GET, new HttpEntity<Object>(headers), VaultResponse.class); 
	            
	            return responseEntity.getBody() ;
	        
				
			}
	    });
		
		return response.getData() ;
		
		 
		
	}

}
