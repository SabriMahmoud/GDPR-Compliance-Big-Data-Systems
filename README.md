# GDPR Compliance BigData Systems 

This projects aims at implementing a solution for the European Data Protection Regulation, the research paper **A framework for GDPR compliance in Big Data
systems** made by **Mouna Rhahla**, **Sahar Allegue** and **Takoua Abdellatif** in **Proxym-Lab** Refer to Documents directory for more information.

**SUPERVISER :** **Tarek Sghair**

## Context and Objectives

#### Key words 
   - **Service** : Which is one of the programmed services it may be any API that needs data to process such as **Machine Learning** service that demands customer data at REST to perform clustering or client segmentation,**Analytics** service that performs data aggregations in order to make a statistical dashboard that helps in decision making etc ... 
   - **Restriction** : It is the order of control of which the subject data is not allowed for any use.

### Context

Log data is the information recorded by your application server about when, how, and which visitors are using your website. Application server providers  commonly collect the following information about each user:
  
  - **IP address and device identifier**: This is the unique identifying address broadcasted by the browser or device by which each user is accessing your online platform.
  - **Request date/time**: The specific date and time of each action the user takes.
  - **Personal details**: Including the full name of a user ,identity number and password.
  - **Device**: Wether the user is connected from a web or mobile application.
  - **Transaction Amount**: The amount of a triggered  transaction process.
  - **Service**: The service provided by the application .

It is trivial to ignore the **sensitivity** and the **privacy** of the logs generated, a GDPR based application contains the necessary components stacked in layers in order to build a **trusted** application and ensure **confidentiality** by providing all users the ability to **control** all data usage except the ones of which needed for server or application required operations.

In order to do so **restrictions** to protect customer data from the application services must be provided to **choose** **who**(Service?) is allowed and **which**(attribute?) data to consume in processing.All restrictions will be stored in datebase for further needs.


Here is an example illustrating a senario for more understanding of the problem:

Let serviceA be one of the mentioned services in the above paragraph
  - User 1 : The serviceA has a restriction on the last name and the amount of transfer 
  - User 2 : The serviceA has a restriction on the customer name, the device and the service provided to the user
  - User 3 : The serviceA has a restriction on the customer name; customer last name and the device 
	 

![alt text](https://github.com/SabriMahmoud/GDPR_Compliance_BigData_Systems/blob/development/Documents/context.png)



Now after **storing** the data for authorization,our application services will try to **proccess** it, the **senario** is like the following: 

- **Request** : The service will try to get the required data (user1 in the example) from the database. 
- **Response** : The response must contain only the authorized data of the required customers.

![alt text](https://github.com/SabriMahmoud/GDPR_Compliance_BigData_Systems/blob/development/Documents/objective.png)

### Objective 

Our main two goals are to garantee end to end secure data flow in real time  by delivering encrypted logs and  maintaining access control layer built on top of the database with respect to all GDPR constraints. 

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

# Database Architecture

![alt text](https://github.com/SabriMahmoud/GDPR_Compliance_BigData_Systems/blob/development/Documents/database_architecture.png)


# Project Architecture 

![alt text](https://github.com/SabriMahmoud/GDPR_Compliance_BigData_Systems/blob/development/Documents/architecture.png)


# Technologies Identification 

![alt text](https://github.com/SabriMahmoud/GDPR_Compliance_BigData_Systems/blob/development/Documents/technologies_id.png)

# Project Implementation  
## Module 1 Data Protection Officier 


**Admin** : Application admin 

**Clients** : FLASK Services 

**MongoDB** : Application Data Base contains users and Application Data 





![alt text](https://mktg-content-api-hashicorp.vercel.app/api/assets?product=tutorials&version=main&asset=public%2Fimg%2Fvault%2Fvault-mongodb.png)

**Note:** when the service access the DB with the provided token the only collection that will be visible is the authorized one which is the View in our case.



### Steps to run Module 1 properly 


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
           
## Module 2 

### Server Status 

![alt text](https://github.com/SabriMahmoud/GDPR_Compliance_BigData_Systems/blob/development/Documents/server_status.png)

### Data Flow Manager
 
![alt text](https://github.com/SabriMahmoud/GDPR_Compliance_BigData_Systems/blob/development/Documents/kafka_and_zookeeper.png)
           
#### Database Sink Connector 

![alt text](https://github.com/SabriMahmoud/GDPR_Compliance_BigData_Systems/blob/development/Documents/kafka_connect.png) 




   
