

# GDPR Compliance BigData Systems 

This projects aims at implementing a solution for the European Data Protection Regulation, the research paper **A framework for GDPR compliance in Big Data
systems** made by **Mouna Rhahla**, **Sahar Allegue** and **Takoua Abdellatif** in **Proxym-Lab** Refer to Documents directory for more information.

**PROJECT SUPERVISER :** **Takoua Abdellatif**

**MODULE 2 SUPERVISOR:** **Tarek Sghir**

You can find the project source code on : <a href="https://github.com/SabriMahmoud/GDPR-Compliance-Big-Data-Systems" target="_blank">GitHub</a>


You can reach me on :<a href="https://www.linkedin.com/in/sabri-mahmoud-a30b101b7/" target="_blank">Linked In</a>


I'll be pleased to explain any unclear information.

# Table of Contents

1. [GDPR Overview](#gdpr-overview)  
2. [Context And Objectives](#context-and-objectives)  
3. [Project Architecture](#project-architecture)
4. [Database Architecture](#database-architecture)
6. [Technologies Identification](#technologies-identification)
6. [Project Implementation](#project-implementation)
7. [Running The First  Module](#running-module-1)
8. [Running The Second  Module](#running-module-2)

# GDPR Overview
## Principles

You can check one of the research documents provided for an explanation of each principle .

![alt text](https://github.com/SabriMahmoud/GDPR_Compliance_BigData_Systems/blob/development/Documents/principles.png)

## Actors 
You can check one of the research documents provided for an explanation of each actor .

![alt text](https://github.com/SabriMahmoud/GDPR_Compliance_BigData_Systems/blob/development/Documents/actors.png)

# Context and Objectives

## Key words 
   - **Service**: Which is one of the programmed services it may be any of the enterprise application built API that needs data to process such as a **Machine Learning** service that demands customer data at REST to perform clustering or client segmentation and an **Analytics** service that performs data aggregations in order to make a statistical dashboard that helps in decision making etc ... 
   - **Restriction** : It is the order of control of which the subject data is not allowed for any use.

## Context

Log data is the information recorded by your application server about when, how, and which visitors are using your website. Application server providers  commonly collect the following information about each user:
  
  - **IP address and device identifier**: This is the unique identifying address broadcasted by the browser or device by which each user is accessing your online platform.
  - **Request date/time**: The specific date and time of each action the user takes.
  - **Personal details**: Including the full name of a user, identity number, and password.
  - **Device**: Whether the user is connected from a web or mobile application.
  - **Transaction Amount**: The amount of a triggered  transaction process.
  - **Service**: The service provided by the application.

It is trivial to ignore the **sensitivity** and the **privacy** of the logs generated, a GDPR-based application contains the necessary components stacked in layers in order to build a **trusted** application and ensure **confidentiality** by providing all users the ability to **control** all data usage except the ones of which needed for server or application required operations.

In order to do so, **restrictions** to protect customer data from the application services must be provided to **choose** **who**(Service?) is allowed and **which**(attribute?) data to consume in processing. All restrictions will be stored in a database for further needs.


Here is an example illustrating a scenario for more understanding of the problem:

Let serviceA be one of the mentioned services in the above paragraph
  - **User 1**: The serviceA has a restriction on the last name and the amount of transfer 
  - **User 2**: The serviceA has a restriction on the customer name, the device, and the service provided to the user
  - **User 3**: The serviceA has a restriction on the customer name; customer last name and the device 
	 

![alt text](https://github.com/SabriMahmoud/GDPR_Compliance_BigData_Systems/blob/development/Documents/context.png)



Now after **storing** the data for authorization, our application services will try to **process** it, the **scenario** is like the following: 

- **Request**: The service will try to get the required data (user1 in the example) from the database. 
- **Response**: The response must contain only the authorized data of the required customers.

![alt text](https://github.com/SabriMahmoud/GDPR_Compliance_BigData_Systems/blob/development/Documents/objective.png)

## Objective 

Our two primary objectives are to guarantee secure, end-to-end, real-time data flow  by delivering encrypted logs and  maintaining an access control layer built on top of the database with respect to all GDPR constraints. 




# Project Architecture 

First of all,at a time **T** and event **E** will occur during the normal or the malfunctioning of the application and this event record will be stored in a log file in the image below the data source representing our application.

Events may be one of the following :
  - **Transaction**
  - **Application Error**
  
The data flow manager will be intercepting and ready to process any incoming information to finally merge it into the database.

On the other side, the data protection officer will be managing access control to the database by providing a dynamic  token with a limitless lifetime triggered by a request from any existing service in the enterprise. 
    
Once the token is available, the service can get the required data except the ones restricted by the user.


![alt text](https://github.com/SabriMahmoud/GDPR_Compliance_BigData_Systems/blob/development/Documents/architecture.png)

# Database Architecture


For each service we have authorized customer data, a pipeline generates the **View** where all data within it is eligible to use.
For example: if we have **n** services the number of collections will be **n + 1 policy collection + all data collection**.
 


<p align="center">
  <img src="https://github.com/SabriMahmoud/GDPR_Compliance_BigData_Systems/blob/development/Documents/dataPartition.png" />
</p>

**Authorized Data Format** : 
  - 0 : not allowed to use by service 
  - 1 : allowed to use by service 

<p align="center">
  <img src="https://github.com/SabriMahmoud/GDPR_Compliance_BigData_Systems/blob/development/Documents/authorized_data.png" />
</p>


# Technologies Identification 

![alt text](https://github.com/SabriMahmoud/GDPR_Compliance_BigData_Systems/blob/development/Documents/technologies_id.png)

# Project Implementation        
## Module 1 


### Data Flow Manager
####  Apache Kafka Quick Overview
Apache Kafka's concept is very simple and easy to understand, it is essentially a distributed platform for high-performance data pipelines, streaming analytics, data integration, and mission-critical applications.

It has these core APIs:
  - **Producer API**: Applications can publish a stream of records to one or more Kafka topics.
  - **Consumer API**: Applications can subscribe to topics and process the stream of records produced to them.
  
The core abstraction Kafka provides for a stream of records is the **topic**.
A topic is a category or feed name to which records are published. Topics in Kafka are always multi-subscriber. This means that a topic can have zero, one, or many consumers that subscribe to the data written to it.

For each topic, the Kafka cluster maintains a **partitioned** log like the one shown in the image below.

Each partition is an ordered, immutable sequence of records that is continually appended to a structured commit log. The records in the partitions are each assigned a sequential ID number called the offset, that uniquely identifies each record within the partition.

The Kafka cluster also  durably persists all published records, whether they have been consumed using a configurable retention period or not,it???s performance is effectively constant with respect to data size, which means storing data for a long time is not a problem.

To learn more about Kafka visite the official website : [Apache Kafka](https://kafka.apache.org/)


#### Roles Identification
  - **Producer:** Any integrated Application
  - **Consumer:** Kafka Connect 
  
  
![alt text](https://github.com/SabriMahmoud/GDPR_Compliance_BigData_Systems/blob/development/Documents/kafka_and_zookeeper.png)
  
#### Zookeeper Quick Overveiw   

For now, Kafka services cannot be used in production without first installing ZooKeeper. * This is true even if your use case requires just a single broker, single topic, and single partition.

For any distributed system, there needs to be a way to coordinate tasks.  Kafka is a distributed system that was built to use ZooKeeper.  However, other technologies like Elasticsearch and MongoDB have their own built-in mechanisms for coordinating tasks.

ZooKeeper has five primary functions.  Specifically, ZooKeeper is used for controller election, cluster membership, topic configuration, access control lists, and quotas.

 - **1. Controller Election**.  The controller is the broker responsible for maintaining the leader/follower relationship for all partitions.  If ever a node shuts down, ZooKeeper ensures that other replicas take up the role of partition leaders replacing the partition leaders in the node that is shutting down.

 - **2. Cluster Membership**.  ZooKeeper keeps a list of all functioning brokers in the cluster.

 - **3. Topic Configuration**.  ZooKeeper maintains the configuration of all topics, including the list of existing topics, number of partitions for each topic, location of the replicas, configuration overrides for topics, preferred leader node, among other details.

 - **4. Access Control Lists (ACLs)**.  ZooKeeper also maintains the ACLs for all topics.  This includes who or what is allowed to read/write to each topic, list of consumer groups, members of the groups, and the most recent offset each consumer group received from each partition.

 - **5. Quotas**.  ZooKeeper accesses how much data each client is allowed to read/write.        
ZooKeeper is used in distributed systems for service synchronization and as a naming registry.  When working with Apache Kafka, ZooKeeper is primarily used to track the status of nodes in the Kafka cluster and maintain a list of Kafka topics and messages. 

To learn more about Zookeeper visite the official website : [Zookeeper](https://zookeeper.apache.org/)

 


## Module 2 Data Protection Officier 
### Vault Quick Overview 

Vault is an identity-based secrets and encryption management system. A secret is anything that you want to tightly control access to, such as API encryption keys, passwords, or certificates. Vault provides encryption services that are gated by authentication and authorization methods. Using Vault???s UI, CLI, or HTTP API, access to secrets and other sensitive data can be securely stored and managed, tightly controlled (restricted), and auditable.

A modern system requires access to a multitude of secrets: database credentials, API keys for external services, credentials for service-oriented architecture communication, etc. Understanding who is accessing what secrets is already very difficult and platform-specific. Adding on key rolling, secure storage, and detailed audit logs is almost impossible without a custom solution. This is where Vault steps in.

Examples work best to showcase Vault. Please see the use cases.

The key features of Vault are:

  - **Secure Secret Storage**: Arbitrary key/value secrets can be stored in Vault. Vault encrypts these secrets prior to writing them to persistent storage, so gaining access to the raw storage isn't enough to access your secrets. Vault can write to disk, Consul, and more.

  - **Dynamic Secrets**: Vault can generate secrets on-demand for some systems, such as AWS or SQL databases. For example, when an application needs to access an S3 bucket, it asks Vault for credentials, and Vault will generate an AWS keypair with valid permissions on demand. After creating these dynamic secrets, Vault will also automatically revoke them after the lease is up.

  - **Data Encryption**: Vault can encrypt and decrypt data without storing it. This allows security teams to define encryption parameters and developers to store encrypted data in a location such as a SQL database without having to design their own encryption methods.

  - **Leasing and Renewal**: All secrets in Vault have a lease associated with them. At the end of the lease, Vault will automatically revoke that secret. Clients are able to renew leases via built-in renew APIs.

  - **Revocation**: Vault has built-in support for secret revocation. Vault can revoke not only single secrets, but a tree of secrets, for example all secrets read by a specific user, or all secrets of a particular type. Revocation assists in key rolling as well as locking down systems in the case of an intrusion.

To learn more about Hashicorp Vault visit the official website : [Hashicorp Vault](https://www.vaultproject.io/docs/what-is-vault)

**Admin** : Application admin 

**Clients** : FLASK Services 

**MongoDB** : Application Data Base contains users and Application Data 





![alt text](https://mktg-content-api-hashicorp.vercel.app/api/assets?product=tutorials&version=main&asset=public%2Fimg%2Fvault%2Fvault-mongodb.png)

**Note:** when the service access the DB with the provided token the only collection that will be visible is the authorized one which is the View in our case.







# Running Module 1  

* Before proceeding to run any script or command let's first install the requirements by running **requirements_installation.sh** :

  **Note**: 
   - The script will also add your local machine user to the docker group to be able to run docker without super user privilege because docker uses the Unix sockets for communication other wise it will demand credentials every time you run it .
   - during installation always proceed with yes  

```bash
git clone https://github.com/SabriMahmoud/GDPR-Compliance-Big-Data-Systems.git
cd GDPR-Compliance-Big-Data-Systems/PythonVersion_Consent_Management/Data_Management/Kafka/Kafka_Connect/
./requirements_installation.sh
```

To make the project up and running clone the repository and run the **test_rootless.sh** script by typing these commands .

**Note**: 
   - The script will get our docker containers up and running in the server
   - Download the MongoDB connect Plugin
   - Copy the Plugin inside the connect container
   - Create Kafka topics 
   - Configure the MongoDB Sink connectors
   - Produce test data 

```bash
./test_rootless.sh
```

**Note:** If you encounter an error **bash: ./test_rootless.sh Permission denied**  you should give the permision of execution to prevent it from occuring by typing this command 

```bash
chmod +x ./test_rootless.sh
```

Otherwise docker containers are invading the host server at this moment let's check the server status.
 
### Server Status 

You can check whether everythiing is good and compatible with the image below  type this command .
```bash
docker ps 
```
<p align="center">
  <img src="https://github.com/SabriMahmoud/GDPR_Compliance_BigData_Systems/blob/development/Documents/server_status.png" />
</p>

 If everything looks good let's see the MongoDB Sink connector configuration.

### Database Sink Connector 

#### Configuration 
Kafka connect Sink configuration is already done inside the script **test_rootless.sh** that you've runned earlier this is how the configuration looks like.

```java
{
    "name": "mongo-sink-policy-connector",
    "config": {
        "connector.class": "com.mongodb.kafka.connect.MongoSinkConnector",
        "tasks.max": "1",
        "topics": "policyevents",
        "connection.uri": "mongodb://mdbadmin:hQ97T9JJKZoqnFn2NXE@mongodb:27017/admin?tls=false",
        "database": "Bankerise",
        "collection": "UsersPolicy",
        "max.num.retries": 5,
        "mongo.errors.tolerance": "all",
        "mongo.errors.log.enable": true,
        "errors.log.include.messages": true,
        "errors.deadletterqueue.topic.name": "policyevents.deadletter",
        "errors.deadletterqueue.context.headers.enable": true 
    }
}

```
**Note** : 
   - I have configured two connector, one for the policy and the other for the application logs and both are consuming from two different kafka topics.

You can clearly see that the connector is confugured as a Sink Connecter by looking to **config** attribute **connector.class**. Here we mentioned one topic which is events and we gave the connector the database **url** and the **collection** to where to transfer.

Same as the application connecter only the topics and the collection have changed.

#### Data Factory 

After we setteled the environement and created a kafka topic named **events** it is time to produce some logs.So as to achieve, proceed to 
**GDPR-Compliance-Big-Data-Systems/PythonVersion_Consent_Management/Data_Management/Kafka/** directory and run **producer.py**.



```!#/bin/sh
python3 producer.py
```

As shown in the image below the producer uses Vault API to encrypt data. you can check **GDPR-Compliance-Big-Data-Systems/PythonVersion_Consent_Management/Data_Management/Kafka/encrypt_with_vault.py** to see how the encryption process is done. 


![alt text](https://github.com/SabriMahmoud/GDPR_Compliance_BigData_Systems/blob/development/Documents/kafka_connect.png) 


To verify that data was delivered as planned you can either access MongoDB container or use the MongoDB compass GUI.

**1. Access MongoDB container**

open a new terminal and run this command

```!#/bin/sh
sudo docker exec -it mongodb /bin/sh 
```

to open the shell command line  run this command 

```!#/bin/sh
mongo
```

Inside the mongo shell run these commands 

```!#/bin/sh
use Bankerise
```

```!#/bin/sh
db.AppUsers.find()
```

**2. Use MongoDB compass**
you can follow this link on how to install MongoDB compass : [Installation](https://www.mongodb.com/docs/compass/current/install/) 

Now we are happy that we did the job correctly as you can the data was delivered securly encrypted with vault, we can move on tho the second module.

- The result will look like this for the user **id=1** 

```python
{
   "_id":{
      "$oid":"62b6eb1727a00961febb829a"
   },
   "id":1,
   "date":"4/23/2022",
   "first_name":"vault:v1:L/4VjQOi88yzuSaW//16ZH6U+FJDSXzbvrNnaA5UfUSYrw==",
   "last_name":"vault:v1:Q1jZihGqfXDHCpqTSb01L+L0naXVyV7albvqjNPojc4VvgI37iY=",
   "email":"vault:v1:/LJ13Mo8ljxas/jezUkvXt4Ur1CVwILPQma4ZkuUrK4uZQW5czGlWIdfNz+0+ORVCdVdfzbg",
   "transfert_amount":"vault:v1:N5ZRGjUOvFbqZVswWXrXcJN/hUuktzWFdRzgCaiOpg=="
}

```


# Running Module 2

## Build Flask Docker Image

Vault and MongoDB containers are already running in server the final step is to create the Flask docker image and run the container.
Proceed tro **GDPR-Compliance-Big-Data-Systems/PythonVersion_Consent_Management/FlaskApi/** and run the **create_flask_image.sh**

```!#/bin/sh
./create_flask_image.sh

```

## MongoDB  View Creation


The view is already created within the script **test_rootless.sh** the script calls the file **manage_database.py** to perform all required actions.
 

```python
	command = [
	   {
	      "$lookup":{
		 "from":"UsersPolicy",
		 "let":{
		    "id_u":"$id"
		 },
		 "pipeline":[
		    {
		       "$match":{
		          "$expr":{
		             "$and":[
		                {
		                   "$eq":[
		                      "$id",
		                      "$$id_u"
		                   ]
		                }
		             ]
		          }
		       }
		    }
		 ],
		 "as":"same_id"
	      }
	   },
	   {
	      "$replaceRoot":{
		 "newRoot":{
		    "$mergeObjects":[
		       {
		          "$arrayElemAt":[
		             "$same_id",
		             0
		          ]
		       },
		       "$$ROOT"
		    ]
		 }
	      }
	   },
	   {
	      "$project":{
		 "same_id":0
	      }
	   },
	   {
	      "$project":{
		 "id":1,
		 "first_name":{
		    "$cond":[
		       {
		          "$eq":[
		             "$use_first_name",
		             1
		          ]
		       },
		       "$first_name",
		       "$$REMOVE"
		    ]
		 },
		 "last_name":{
		    "$cond":[
		       {
		          "$eq":[
		             "$use_last_name",
		             1
		          ]
		       },
		       "$last_name",
		       "$$REMOVE"
		    ]
		 },
		 "email":{
		    "$cond":[
		       {
		          "$eq":[
		             "$use_email",
		             1
		          ]
		       },
		       "$email",
		       "$$REMOVE"
		    ]
		 },
		 "gender":{
		    "$cond":[
		       {
		          "$eq":[
		             "$use_gender",
		             1
		          ]
		       },
		       "$gender",
		       "$$REMOVE"
		    ]
		 },
		 "ip_address":{
		    "$cond":[
		       {
		          "$eq":[
		             "$use_ip_address",
		             1
		          ]
		       },
		       "$ip_address",
		       "$$REMOVE"
		    ]
		 },
		 "transfert_amount":{
		    "$cond":[
		       {
		          "$eq":[
		             "$use_transfert_amount",
		             1
		          ]
		       },
		       "$transfert_amount",
		       "$$REMOVE"
		    ]
		 },
		 "date":1
	      }
	   }
	]
```

## Testing Module 2 
You can check for results in the browser open http://localhost:5000 where the Data protection Officer is listening and ask for data.

Example:
- To get the user with a specific  id for a specific service  
```!#/bin/sh
http://localhost:5000/<service_name>/users/<id>

```

The existing service name is **myServices** and the **id** range is from 1 to 1000 try this one :

```!#/bin/sh
http://localhost:5000/myServices/users/1

```


