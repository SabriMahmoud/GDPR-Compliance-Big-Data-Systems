package com.example.gdpr.configuration;

import java.net.URI;

import org.springframework.context.annotation.Configuration;
import org.springframework.vault.annotation.VaultPropertySource;
import org.springframework.vault.authentication.ClientAuthentication;
import org.springframework.vault.authentication.TokenAuthentication;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.config.AbstractVaultConfiguration;

/* Configuration of Vault End Point and Vault Authentication */  

// To do changing if with try catch 

@Configuration
@VaultPropertySource("secret/consent-management-platform")
public class VaultConfiguration extends AbstractVaultConfiguration{
	  
	  @Override
	  public VaultEndpoint vaultEndpoint() {
		
		  

		// Getting URI  from application.properties file  
		
		String uri = getEnvironment().getProperty("spring.cloud.vault.uri") ; 

		if (uri != null) {
			return VaultEndpoint.from(URI.create(uri));
		}
		throw new IllegalStateException();

	  }

	  @Override
	  public ClientAuthentication clientAuthentication() {
		 
		 // Getting Token from application.properties file  
		  
		String token = getEnvironment().getProperty("spring.cloud.vault.token") ; 
		if(token != null) {			
			return new TokenAuthentication(token);	
		}
		throw new IllegalStateException() ; 

		
	  }
}