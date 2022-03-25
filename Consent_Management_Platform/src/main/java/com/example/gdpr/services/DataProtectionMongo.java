package com.example.gdpr.services;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class DataProtectionMongo {
	@Autowired
	MongoTemplate mongoTemplate ; 
	
	//creating a view with mongoTemplate
	
	public void test(){
		mongoTemplate.executeCommand("{create:UsersView , viewOn:Users ,pipline: [\n" + 
				"   {\n" + 
				"      $lookup:\n" + 
				"         {\n" + 
				"           from: \"usedData\",\n" + 
				"           let: { id_u: \"$id\",},\n" + 
				"           pipeline: [\n" + 
				"              { $match:\n" + 
				"                 { $expr:\n" + 
				"                    { $and:\n" + 
				"                       [\n" + 
				"                         { $eq: [ \"$id\",  \"$$id_u\" ] }\n" + 
				"                       ]\n" + 
				"                    }\n" + 
				"                 }\n" + 
				"              }\n" + 
				"           ],\n" + 
				"           as: \"same_id\"\n" + 
				"         }\n" + 
				"    },   \n" + 
				"    {\n" + 
				"      $replaceRoot: { newRoot: { $mergeObjects: [ { $arrayElemAt: [ \"$same_id\", 0 ] }, \"$$ROOT\" ] } }\n" + 
				"    },\n" + 
				"    { $project: { same_id: 0 } },\n" + 
				"    { $match : {\n" + 
				"        use_data : \"1\"\n" + 
				"    }}\n" + 
				"    \n" + 
				"]}"); 
	}
	

	

}


