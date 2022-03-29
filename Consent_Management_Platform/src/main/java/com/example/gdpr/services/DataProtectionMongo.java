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
	/***************************************************************************/
	/**************************************************************************
	 *   command = {  
	 *              create : 'UsersView' ,
	 *              viewOn : 'UserApp' ,
	 *              pipeline :[{
							      $lookup:
							         {
							           from: "usedData",
							           let: { id_u: "$id",},
							           pipeline: [
							              { $match:
							                 { $expr:
							                    { $and:
							                       [
							                         { $eq: [ "$id",  "$$id_u" ] }
							                       ]
							                    }
							                 }
							              }
							           ],
							           as: "same_id"
							         }
							    },   
							    {
							      $replaceRoot: { newRoot: { $mergeObjects: [ { $arrayElemAt: [ "$same_id", 0 ] }, "$$ROOT" ] } }
							    },
							    { $project: { same_id: 0 } },
							    { $project : {
							        id : 1 ,
							        first_name : {$cond: [{$eq: ['$use_first_name', "1"]},'$first_name','$$REMOVE' ]} ,
							        last_name : {$cond: [{$eq: ['$use_last_name',"1"]},'$last_name' ,'$$REMOVE']},
							        email : {$cond: [{$eq: ['$use_email', "1"]}, '$email' ,'$$REMOVE']} ,
							        gender : {$cond: [{$eq: ['$use_gender',"1"]},'$gender','$$REMOVE']},
							        ip_address : {$cond: [{$eq: ['$use_ip', "1"]},'$ip_address','$$REMOVE' ]} ,
							        credit : {$cond: [{$eq: ['$credit',"1"]},'$credit' ,'$$REMOVE' ]}
							    }}
							    
							]
	 *               }
	 * 
	 ***************************************************************************
	 ***************************************************************************/
	
	
	public void createView(){
		
		String command = "{create: 'UsersView' ,"
						+ "viewOn: 'UserApp' ,"
						+ "pipeline: [{$lookup:{ from: \"usedData\",let: { id_u: \"$id\",},"
						+ "pipeline: [{ $match:{$expr:{ $and:[{ $eq: [ \"$id\",  \"$$id_u\" ] }]}}}],as: \"same_id\"}},"
						+ "{$replaceRoot: { newRoot: { $mergeObjects: [ { $arrayElemAt: [ \"$same_id\", 0 ] }, \"$$ROOT\" ] } }},"
						+ "{ $project: { same_id: 0 } },"
						+ "{ $project : {\n" + 
								 "id: 1 \n"+
						"        first_name : {$cond: [{$eq: ['$use_first_name', \"1\"]},'$first_name','$$REMOVE' ]} ,\n" + 
						"        last_name : {$cond: [{$eq: ['$use_last_name',\"1\"]},'$last_name' ,'$$REMOVE']},\n" + 
						"        email : {$cond: [{$eq: ['$use_email', \"1\"]}, '$email' ,'$$REMOVE']} ,\n" + 
						"        gender : {$cond: [{$eq: ['$use_gender',\"1\"]},'$gender','$$REMOVE']},\n" + 
						"        ip_address : {$cond: [{$eq: ['$use_ip', \"1\"]},'$ip_address','$$REMOVE' ]} ,\n" + 
						"        credit : {$cond: [{$eq: ['$credit',\"1\"]},'$credit' ,'$$REMOVE' ]}\n" + 
						"    }}] } " ; 
		
		Document parsed_command = Document.parse(command) ; 
		Document res = mongoTemplate.executeCommand(parsed_command); 
	}


}


