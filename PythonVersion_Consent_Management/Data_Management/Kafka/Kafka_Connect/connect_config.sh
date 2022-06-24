#!/bin/bash

# Starting Containers 
echo "Requirements installation........"

echo "=================================================\n"
echo "Starting Docker Container........"

docker-compose up -d 

echo "Downloading Connect plugin......."
# downloading connect plugin
wget https://github.com/mongodb/mongo-kafka/releases/download/r1.6.0/mongodb-kafka-connect-mongodb-1.6.0.zip
sleep 10 
echo "================================================="

unzip 'mongodb-kafka-connect-mongodb-1.6.0.zip'

echo "Copying the plugin inside connect container......."


echo "                                                   "

docker cp ./mongodb-kafka-connect-mongodb-1.6.0 connect:/usr/share/java/

sleep 10

docker restart connect
docker ps 


sleep 10

curl -s -X GET http://localhost:8083/connector-plugins | grep '{"class":"com.mongodb.kafka.connect.MongoSinkConnector","type":"sink","version":"1.6.0"},{"class":"com.mongodb.kafka.connect.MongoSourceConnector","type":"source","version":"1.6.0"}' 

echo "Creating kafka topics........."


echo "================================================="



function retreive_topics_and_partitions {
length="$#"
let x="$#" y=2 topic_partition_separator=x/y

topics=""
partitions=""

let x="$length" y=1 stop=x-y

for((i=1;i<="$length";i++))
do
        index_param="$i"
        parameter=${!index_param}
        if [ "$i" -gt "$topic_partition_separator" ] ; then
                partitions+="$parameter"
                if [ "$i" -lt "$length" ] ; then 
                        partitions+="-"
                fi
        else
                topics+="$parameter"
                if [ "$i" -lt "$topic_partition_separator" ] ; then 
                        topics+="-"
                fi 

        fi

done

return 0
}


retreive_topics_and_partitions "$@"
python3 ../create_kafka_topics.py -t "$topics" -p "$partitions" 

sleep 10 


curl --request POST \
  --url http://localhost:8083/connectors \
  --header 'Content-Type: application/json' \
  --data '{
    "name": "mongo-sink-connector",
    "config": {
        "connector.class": "com.mongodb.kafka.connect.MongoSinkConnector",
        "tasks.max": "1",
        "topics": "events",
        "connection.uri": "mongodb://mongodb:27017/Bankerise",
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


curl -s -X GET http://localhost:8083/connectors/mongo-sink-connector/tasks/0/status | jq



echo "Enter topic name to test data flow with MOCK DATA" 

read topic 


python3 ../producer.py -t "$topic"

docker-compose down 


