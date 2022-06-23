#!/bin/sh

echo "================================================="
echo "Starting Docker Container........"


sudo docker-compose up -d

sleep 5 

docker run --name vault -d --cap-add=IPC_LOCK -e 'VAULT_DEV_ROOT_TOKEN=myroot' -e 'VAULT_DEV_LISTEN_ADRESS=0.0.0.0:8200' -P 8200:8200 vault

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


sudo docker cp mongodb-kafka-connect-mongodb-1.6.0 connect:/usr/share/java/
sleep 5
sudo docker restart connect
sudo docker ps

sleep 10

curl -s -X GET http://localhost:8083/connector-plugins | jq | head -n 20

sleep 10 

echo "Creating kafka topics........."


echo "================================================="

python3 ../create_kafka_topics.py -t events-events.deadletter -p 3-3

sleep 20

curl --request POST --retry 50 --retry-all-errors --retry-delay 5 --fail   --url http://localhost:8083/connectors   --header 'Content-Type: application/json'   --data '{
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

sleep 20

echo " "

echo "================================================="


curl -s -X GET http://localhost:8083/connectors
sleep 10

echo " "

echo "================================================="

curl -s -X GET http://localhost:8083/connectors/mongo-sink-connector/tasks/0/status | jq
echo " "
echo "================================================="

sleep 10
python3 ../producer.py
