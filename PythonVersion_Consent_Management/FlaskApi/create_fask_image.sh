#!/bin/bash 
sudo docker container stop data_protection
sudo docker container rm data_protection 
sudo docker image rm data_protection 
sudo docker build -t data_protection .
sudo docker run -d --name data_protection --network kafka_connect_default -p 5000:5000 data_protection
sudo docker container logs data_protection
sudo docker ps

