#!/bin/sh

echo "================================================="
echo "Starting Docker Container........"


docker-compose up -d

sleep 5 


echo "Downloading Connect plugin......."

FILE=./mongodb-kafka-connect-mongodb-1.6.0.zip 

if test -f "$FILE";then 
	echo "Plugin Already exists"
else
	wget https://github.com/mongodb/mongo-kafka/releases/download/r1.6.0/mongodb-kafka-connect-mongodb-1.6.0.zip
	unzip mongodb-kafka-connect-mongodb-1.6.0.zip 
fi


echo "================================================="


echo "Copying the plugin inside connect container......."


docker cp mongodb-kafka-connect-mongodb-1.6.0 connect:/usr/share/java/
sleep 5
sudo docker restart connect
sudo docker ps

sleep 10

curl -s -X GET http://localhost:8083/connector-plugins | jq | head -n 20

sleep 10 

echo "Creating kafka topics........."


echo "================================================="

python3 ../manage_database.py

python3 ../create_kafka_topics.py -t events-events.deadletter -p 3-3

sleep 10 

python3 ../create_kafka_topics.py -t policyevents-policyevents.deadletter -p 3-3

sleep 20

curl --request POST --retry 50 --retry-all-errors --retry-delay 5 --fail   --url http://localhost:8083/connectors   --header 'Content-Type: application/json'   --data '{
    "name": "mongo-sink-connector",
    "config": {
        "connector.class": "com.mongodb.kafka.connect.MongoSinkConnector",
        "tasks.max": "1",
        "topics": "events",
        "connection.uri": "mongodb://mdbadmin:hQ97T9JJKZoqnFn2NXE@mongodb:27017/admin?tls=false",
        "database": "Bankerise",
        "collection": "AppUsers",
        "max.num.retries": 5,
        "mongo.errors.tolerance": "all",
        "mongo.errors.log.enable": true,
        "errors.log.include.messages": true,
        "errors.deadletterqueue.topic.name": "events.deadletter",
        "errors.deadletterqueue.context.headers.enable": true 
    }
}'

sleep 10

curl --request POST --retry 50 --retry-all-errors --retry-delay 5 --fail   --url http://localhost:8083/connectors   --header 'Content-Type: application/json'   --data '{
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
}'




echo " "

echo "================================================="

echo "Checking for existing connectors"


curl -s -X GET http://localhost:8083/connectors
sleep 10

echo " "

echo "================================================="

echo "Checking for MongoDB Sink Connector status"


curl -s -X GET http://localhost:8083/connectors/mongo-sink-connector/tasks/0/status | jq
echo " "
echo "================================================="

sleep 10
python3 ../producer.py
