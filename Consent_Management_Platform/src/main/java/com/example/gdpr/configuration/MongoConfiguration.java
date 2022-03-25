package com.example.gdpr.configuration;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class MongoConfiguration extends AbstractMongoClientConfiguration {
 
//	@Autowired
//	private Environment env;
//	
//	private final String host = env.getProperty("spring.data.mongodb.host") ; 
//	private final String port = env.getProperty("spring.data.mongodb.port") ; 
//	private final String dataBase =env.getProperty("spring.data.mongodb.database") ;
//	private final String username = env.getProperty("spring.data.mongodb.username") ; 
//	private final String password =env.getProperty("spring.data.mongodb.password") ; 
    
	@Override
    protected String getDatabaseName() {
//        return env.getProperty("spring.data.mongodb.database");
		  return "Bankerise" ; 
    }
 
    @Override
    public MongoClient mongoClient() {
    	

    	
        ConnectionString connectionString = new ConnectionString(String.format("mongodb://mdbadmin:hQ97T9JJKZoqnFn2NXE@localhost:27017"));
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .build();
        
        return MongoClients.create(mongoClientSettings);
    }
    
 
   
}
