#!/bin/bash

# Starting Containers 
echo "=================================================\n"
echo "Starting Docker Container........"

docker-compose up -d 

echo "Downloading Connect plugin......."
# downloading connect plugin
curl -o mongodb-kafka-connect-mongodb-1.6.0.zip https://github.com/mongodb/mongo-kafka/releases/download/r1.6.0/mongodb-kafka-connect-mongodb-1.6.0.zip

unzip mongodb-kafka-connect-mongodb-1.6.0.zip

echo "Copying the plugin inside connect container......."
docker cp ./https://mongodb-kafka-connect-mongodb-1.6.0.zip connect:/usr/share/java/
docker restart connect 

