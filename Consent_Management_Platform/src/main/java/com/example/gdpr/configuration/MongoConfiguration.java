package com.example.gdpr.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration(proxyBeanMethods = false)
public class MongoConfiguration{
	
	private String dataBaseName ; 
 

    
	protected void setDataBaseName(String name){
		dataBaseName = name ; 
	}
    protected String getDatabaseName() {

		  return "Bankerise" ; 
    }
 
    public MongoClient mongoClient(String username, String password) {
    	

    	
        ConnectionString connectionString = new ConnectionString(String.format("mongodb://%s:%s@localhost:27017/%s",username,password,this.getDatabaseName()));
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .build();
        
        return MongoClients.create(mongoClientSettings);
    }
    
 
   
}