package com.example.gdpr.services;

import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.vault.VaultException;
import org.springframework.vault.core.VaultTemplate;

import com.example.gdpr.configuration.MongoConfiguration;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;

import static com.mongodb.client.model.Filters.eq;

@Service
public class BeneficialService {
	
	private static final String COLLECTION_NAME = "UsersView" ; 
	
	@Autowired
	VaultTemplate vaultTemplate ; 
	@Autowired
	VaultCommunicationService vaultCommunication ; 
	@Autowired
	MongoConfiguration mongoConfig ; 
	
	
	public Document findUserById(String id){
		
		//Retreive data base creds from vault 
		
		String dataBaseUserName = null ; 
		String dataBasePassword = null ; 
		
		// retreive data base creds 
		
		try{
			Map<String,Object> response = vaultCommunication.getDataBaseCredentials(vaultTemplate);
			
			dataBaseUserName = response.get("username").toString();
			dataBasePassword = response.get("password").toString() ; 
		
		}catch(VaultException exp){
			System.out.println("-----------------------------") ; 
			System.out.println("Role does not exist in Vault") ; 
			System.out.println("-----------------------------") ; 
			return null ;
		}
		
		//Connect to Mongo Data Base with a new thread 
		
		MongoClient client = mongoConfig.mongoClient(dataBaseUserName,dataBasePassword)  ;
		
		// To do remove db name and collection and add them as static attributes 
		String dataBaseName = client.listDatabaseNames().first() ;
		
		Bson equalComparison = eq("id",id);
		FindIterable<Document> usersIterator = client.getDatabase(dataBaseName).getCollection(COLLECTION_NAME).find(equalComparison); 
		return usersIterator.first() ; 
		}

		 
	}


