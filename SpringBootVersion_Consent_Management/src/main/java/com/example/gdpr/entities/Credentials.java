package com.example.gdpr.entities;

import java.util.HashMap;

public class Credentials {
	
	
   private HashMap<String,Object> credentials = new HashMap<String, Object>(); 
	
	
	
	
	public Credentials(String username, String password) {
		super();
		this.credentials.put("username",username) ; 
		this.credentials.put("password",password) ; 
	}




	public HashMap<String, Object> getCredentials() {
		return credentials;
	}




	public void setCredentials(HashMap<String, Object> credentials) {
		this.credentials = credentials;
	}
	
	
	

}