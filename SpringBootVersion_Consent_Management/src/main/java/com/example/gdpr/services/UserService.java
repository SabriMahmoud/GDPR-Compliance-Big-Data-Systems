package com.example.gdpr.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.gdpr.entities.User;
import com.example.gdpr.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepo ;

	
	public List<User> findAllUsers(){
		return userRepo.findAll() ;	
	}
	
	public String addUser(User user){
		userRepo.save(user) ; 
		return "success" ; 
	}
	
}
