package com.example.gdpr.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.gdpr.entities.User;


public interface UserRepository extends MongoRepository<User, Integer> {
}