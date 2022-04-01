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


## Project Architecture 

**Admin** : Application admin 

**Clients** : Spring Boot Services 

**MongoDB** : Application Data Base contains users and Application Data 


![alt text](https://mktg-content-api-hashicorp.vercel.app/api/assets?product=tutorials&version=main&asset=public%2Fimg%2Fvault%2Fvault-mongodb.png)

## Steps to run the project properly 
- First : start Vault Docker container using this command 


``sudo docker run  --net BigData --name vault1 -d --cap-add=IPC_LOCK
-e 'VAULT_DEV_ROOT_TOKEN_ID=myroot' -e 'VAULT_DEV_LISTEN_ADDRESS=0.0.0.0:8200' 
-p 8200:8200 vault``

**Note** : 

           - For more information about building Vault image  visit the official Docker image on Docker Hub 

           - Specifying the container network is certain to assure the communication between other containers  within the Application 
   
