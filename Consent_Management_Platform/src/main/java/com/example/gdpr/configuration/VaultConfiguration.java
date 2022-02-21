package com.example.gdpr.configuration;

import java.net.URI;

import org.springframework.context.annotation.Configuration;
import org.springframework.vault.annotation.VaultPropertySource;
import org.springframework.vault.authentication.ClientAuthentication;
import org.springframework.vault.authentication.TokenAuthentication;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.config.AbstractVaultConfiguration;


@Configuration
@VaultPropertySource("secret/vault-with-spring-boot")
public class VaultConfiguration extends AbstractVaultConfiguration{
	  
	  @Override
	  public VaultEndpoint vaultEndpoint() {
		String uri = getEnvironment().getProperty("spring.cloud.vault.uri") ; 
		if (uri != null) {
			return VaultEndpoint.from(URI.create(uri));
		}
		throw new IllegalStateException();

	  }

	  @Override
	  public ClientAuthentication clientAuthentication() {
		String token = getEnvironment().getProperty("spring.cloud.vault.token") ; 
		if(token != null) {
			return new TokenAuthentication(token);	
		}
		throw new IllegalStateException() ; 
		
	    
	  }
}