# GDPR Compliance BigData Systems 
This projects aims at implementing Vault as a solution for data protection the research paper **A framework for GDPR compliance in Big Data
systems** made by **Mouna Rhahla**, **Sahar Allegue** and **Takoua Abdellatif** in **Proxym-Lab** Refer to Documents directory for more information.

**SUPERVISER :** **Tarek Sghair**

## Context and Objectives

### Context

In order to retrive **restrictions** to protect customer data from the application services a **pop-up** will be viewed in customer side to **choose** **who** is allowed and **which** data to consume in processing.

Here is an example for a **single** internal service: 

- Customer 1 : service cannot use his last name and the amount of transfer 

![alt text](https://github.com/SabriMahmoud/GDPR_Compliance_BigData_Systems/blob/development/Documents/context.png)

### Objective 
Now after **storing** the data for authorization,our Bankerise application services will try to **proccess** it, the **senario** is like the following: 
- **Request** : The service will try to get the needed data from the data base. 
- **Response** : The response must be only the authorized data of the required customer.
![alt text](https://github.com/SabriMahmoud/GDPR_Compliance_BigData_Systems/blob/development/Documents/objective.png)

### Data partition in MongoDB 
The data is devided into two main Collections which are the all customer data and the authorized per service and for each service a pipline generates UsersView where all data within it is eligible to use.
Example : if we have **n** services the number of collections will be **n+2**. 


<p align="center">
  <img src="https://github.com/SabriMahmoud/GDPR_Compliance_BigData_Systems/blob/development/Documents/dataPartition.png" />
</p>

**Autorized Data Format** : 
- 0 : not allowed to use by service 
- 1 : allowed to use by service 

<p align="center">
  <img src="https://github.com/SabriMahmoud/GDPR_Compliance_BigData_Systems/blob/development/Documents/authorized_data.png" />
</p>



## Project Architecture 

**Admin** : Application admin 

**Clients** : Spring Boot Services 

**MongoDB** : Application Data Base contains users and Application Data 


![alt text](https://mktg-content-api-hashicorp.vercel.app/api/assets?product=tutorials&version=main&asset=public%2Fimg%2Fvault%2Fvault-mongodb.png)

**Note:** when the service access the DB with the provided token the only collection that will be visible is the authorized one which is the View in our case.

### 1.Enable The Data Base Secret Engine 
- Services package MongoSecretIntegration class

```java
	public void MountDataBaseSecretEngine(VaultTemplate vaultTemplate){
		
		VaultSysOperations sysOperations = vaultTemplate.opsForSys();
		if (sysOperations != null && !sysOperations.getMounts().containsKey("mongodb/")) {
		      sysOperations.mount("mongodb", VaultMount.create("database"));
		    }
	}
  
``` 

### 2.Configure database connection

```java
	public void EnableVaultMongoConnection(VaultTemplate vaultTemplate) {
	    HashMap<String, String> secretEngineMap = new HashMap<String, String>() ;
	    
	    secretEngineMap.put("plugin_name", BACKEND_PLUGIN_NAME) ; 
	    secretEngineMap.put("connection_url", ROOT_CREDENTIALS) ;
	    secretEngineMap.put("allowed_roles", "*") ; 
	    secretEngineMap.put("username",MONGODB_USER) ;
	    secretEngineMap.put("password",MONGODB_PASS) ;
	    
	    
	    vaultTemplate.write(String.format("%s/config/%s",BACKEND_ENGINE,DATABASE_NAME),secretEngineMap); 
	}
  
``` 

### 3.Create Role for each database client 

  ```java
    public void CreateRole(VaultTemplate vaultTemplate,String roleName){

          HashMap<String,Object> roleMap = new HashMap<String,Object>();


          roleMap.put("db_name",DATABASE_NAME) ;
          roleMap.put("creation_statements", ROLE_STATEMENT) ; 
          roleMap.put("default_ttl",TIME_TO_LIVE) ;
          roleMap.put("max_ttl",MAX_TIME_TO_LIVE) ; 


          vaultTemplate.write(String.format("%s/roles/%s",BACKEND_ENGINE,roleName),roleMap);

    }
  
``` 
### 4.Get database credentials for the service 

```java
      public Map<String, Object> getDataBaseCreds(VaultTemplate vaultTemplate , String serviceName){
        VaultResponse response = vaultTemplate.read(String.format("mongodb/creds/%s",serviceName));
        return response.getData() ; 
      }
  
``` 

### 5.Connect to database 

  ```java
    public MongoClient mongoClient(String username, String password) {
    	

    	
        ConnectionString connectionString = new ConnectionString(String.format("mongodb://%s:%s@localhost:27017/%s",username,password,this.getDatabaseName()));
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .build();
        
        return MongoClients.create(mongoClientSettings);
    }
  
``` 


## Steps to run the project properly 
Before running the Spring Boot Application  there are two steps that you need to perform 
- **1** : start Vault Docker container using this command 


``docker run  --net BigData --name vault1 -d --cap-add=IPC_LOCK
-e 'VAULT_DEV_ROOT_TOKEN_ID=myroot' -e 'VAULT_DEV_LISTEN_ADDRESS=0.0.0.0:8200' 
-p 8200:8200 vault``

- **2** : start Mongodb container using this command 

``docker run -d -p 0.0.0.0:27017:27017 --name=mongodb -e MONGO_INITDB_ROOT_USERNAME="mdbadmin" -e MONGO_INITDB_ROOT_PASSWORD="hQ97T9JJKZoqnFn2NXE" mongo
``

**Note** : 

           - For more information about building Vault image  visit the official Docker image on Docker Hub 

           - Specifying the container network is certain to assure the communication between other containers  within the Application 
           

   
