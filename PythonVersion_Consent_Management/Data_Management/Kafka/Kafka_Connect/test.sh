#!/bin/sh

sudo docker-compose up -d
wget https://github.com/mongodb/mongo-kafka/releases/download/r1.6.0/mongodb-kafka-connect-mongodb-1.6.0.zip
unzip mongodb-kafka-connect-mongodb-1.6.0.zip 
sudo docker cp mongodb-kafka-connect-mongodb-1.6.0 connect:/usr/share/java/
sleep 5
sudo docker restart connect
sudo docker ps

sleep 10

curl -s -X GET http://localhost:8083/connector-plugins | jq | head -n 20

sleep 10 

python3 ../create_kafka_topics.py -t events-events.deadletter -p 3-3

sleep 10

curl --request POST   --url http://localhost:8083/connectors   --header 'Content-Type: application/json'   --data '{
    "name": "mongo-sink-connector",
    "config": {
        "connector.class": "com.mongodb.kafka.connect.MongoSinkConnector",
        "tasks.max": "1",
        "topics": "events",
        "connection.uri": "mongodb://mongodb:27017/my_events",
        "database": "my_events",
        "collection": "kafka_events",
        "max.num.retries": 5,
        "mongo.errors.tolerance": "all",
        "mongo.errors.log.enable": true,
        "errors.log.include.messages": true,
        "errors.deadletterqueue.topic.name": "events.deadletter",
        "errors.deadletterqueue.context.headers.enable": true 
    }
}'

sleep 10 


curl -s -X GET http://localhost:8083/connectors
sleep 10
curl -s -X GET http://localhost:8083/connectors/mongo-sink-connector/tasks/0/status | jq
sleep 10
python3 ../producer.py