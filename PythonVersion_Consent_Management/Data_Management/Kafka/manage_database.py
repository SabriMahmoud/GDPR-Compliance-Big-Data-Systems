from pymongo import MongoClient
try :
    client = MongoClient("mongodb://mdbadmin:hQ97T9JJKZoqnFn2NXE@localhost:27017/admin?tls=false")

    bankerise_db = client['Bankerise']


    """*********** Preparing the View pipline *************"""
    """***************************************************"""
    """***************************************************"""


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
    try:
        # Creating the view on the AppUsers using the pipeline defined previously 
        bankerise_db.command({
                "create":"UsersView",
                "viewOn":"AppUsers",
                "pipeline": command } 
                )
    except Exception as e :
        print(e)

    """********* Creating a Role for The Service **********"""
    """***************************************************"""
    """***************************************************"""

    
    roleName="myServices"
    try:
        # Creating a role for the service to be able to see only the View when connecting to the database 
        bankerise_db.command("createRole",roleName,privileges=[{ "resource": { "db": "Bankerise", "collection": "UsersView" }, "actions": [ "find"] }],roles=[{ "role": "read", "db": "Bankerise" } ])
    except Exception as e :
        print(e)
except Exception as e :
    print(e)