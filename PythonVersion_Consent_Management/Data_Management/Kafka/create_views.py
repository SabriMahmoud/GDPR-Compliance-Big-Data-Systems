from pymongo import MongoClient
client = MongoClient('localhost',27017)
bankerise_db = client['Bankerise']





command = [
            {
                "$lookup":{ "from": "UsersPolicy","let": { "id_u": "$id"},
                "pipeline":[
                        { 
                            "$match":{
                                "$expr":{
                                    "$and":[
                                        {
                                            "$eq": [ "$id",  "$$id_u" ] 
                                        }
                                    ]
                                }
                            }
                        }],"as": "same_id"
                }
            },
    		{
                "$replaceRoot": {
                     "newRoot": {
                         "$mergeObjects": [ 
                            { "$arrayElemAt": [ "$same_id", 0 ] },
                            "$$ROOT" 
                            ]
                      } 
                }
            },
            { 
                "$project": {
                     "same_id": 0
                } 
            },
			{ "$project" : {  
					"id": 1,
					"first_name" : {"$cond": [{"$eq": ['$use_first_name', 1]},"$first_name","$$REMOVE" ]} , 
					"last_name" : {"$cond": [{"$eq": ['$use_last_name',1]},"$last_name" ,"$$REMOVE"]},  
					"email" : {"$cond": [{"$eq": ['$use_email', 1]}, "$email" ,"$$REMOVE"]} ,  
					"gender" : {"$cond": [{"$eq": ['$use_gender',1]},"$gender","$$REMOVE"]},  
					"ip_address" : {"$cond": [{"$eq": ['$use_ip_address', 1]},"$ip_address","$$REMOVE" ]}, 
					"transfert_amount" : {"$cond": [{"$eq": ['$use_transfert_amount',1]},"$transfert_amount" ,"$$REMOVE" ]},
                    "date":1
			    }
            }
        ]
    
bankerise_db.command({
        "create":"UsersView",
        "viewOn":"AppUsers",
        "pipeline": command } 
        )
