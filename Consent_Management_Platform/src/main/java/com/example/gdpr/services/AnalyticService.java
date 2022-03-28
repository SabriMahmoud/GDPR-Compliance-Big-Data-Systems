package com.example.gdpr.services;

import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.vault.core.VaultTemplate;

import com.example.gdpr.configuration.MongoConfiguration;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;

import static com.mongodb.client.model.Filters.eq;

@Service
public class AnalyticService {
	
	@Autowired
	VaultTemplate vaultTemplate ; 
	@Autowired
	VaultCommunicationService vaultCommunication ; 
	@Autowired
	MongoConfiguration mongoConfig ; 
	
	public Document findUserById(String id){
		
		//retreive data base creds from vault 
		Map<String,Object> response = vaultCommunication.getDataBaseCredentials(vaultTemplate);
		// retreive data base creds 
		String dataBaseUserName = response.get("username").toString();
		String dataBasePassword = response.get("password").toString() ; 
		
		MongoClient client = mongoConfig.mongoClient(dataBaseUserName,dataBasePassword)  ;
		// To do remove db name and collection and add them as static attributes 
		Bson equalComparison = eq("id",id);
		FindIterable<Document> usersIterator = client.getDatabase("Bankerise").getCollection("UsersView").find(equalComparison); 
		return usersIterator.first() ; 
	}

}
