package com.example.gdpr.services;

import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;
import org.springframework.vault.support.VaultResponseSupport;

import com.example.gdpr.entities.Credentials;

@Service
public class CredentialsService {
	
	

    
    /**
    *  To Secure Credentials
    * @param credentials
     * @return 
    * @return VaultResponse
    * @throws URISyntaxException
    */
    public VaultResponse secureCredentials(Credentials credentials,VaultTemplate vaultTemplate,String credentialsSecretsPath) throws URISyntaxException {
    	
        return vaultTemplate.write(credentialsSecretsPath, credentials);
    }

    /**
     * To Retrieve Credentials
     * @return Credentials
     * @throws URISyntaxException
     */
    
    public Credentials accessCredentials(VaultTemplate vaultTemplate,String credentialsSecretsPath) throws URISyntaxException {

        VaultResponseSupport<Credentials> response = vaultTemplate.read(credentialsSecretsPath, Credentials.class);
        return response.getData();
    }

}