#!/bin/sh

sudo apt-get update

# Docker installation 

sudo apt-get install docker-ce docker-ce-cli containerd.io docker-compose-plugin

# Creating Docker group 

sudo groupadd docker

# Adding current user to docker group 
sudo usermod -aG docker $USER

# Refreshing configuration 

newgrp docker

# pip installation 

sudo apt install python3-pip 

# Python requirements to interact with Vault and Kafka APIs  

pip install kafka-python

pip install hvac 



