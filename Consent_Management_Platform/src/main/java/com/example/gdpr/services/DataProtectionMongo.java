package com.example.gdpr.services;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;


@Service
public class DataProtectionMongo {
	@Autowired
	MongoTemplate mongoTemplate ; 
	
	//creating a view with mongoTemplate
	
	public void test(){
		String command = "{create: 'UsersView' , viewOn: 'UserApp' ,pipeline: [{$lookup:{ from: \"usedData\",let: { id_u: \"$id\",},pipeline: [{ $match:{$expr:{ $and:[{ $eq: [ \"$id\",  \"$$id_u\" ] }]}}}],as: \"same_id\"}},{$replaceRoot: { newRoot: { $mergeObjects: [ { $arrayElemAt: [ \"$same_id\", 0 ] }, \"$$ROOT\" ] } }},{ $project: { same_id: 0 } },{ $match : {use_data : \"1\"}}] } " ; 
		Document parsed_command = Document.parse(command) ; 
		Document res = mongoTemplate.executeCommand(parsed_command); 
	}
	
	

}


