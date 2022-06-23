#!/bin/sh

echo "Requirements Installation....." 

sudo sed -i '/cdrom/d' /etc/apt/sources.list
 

sudo apt-get update

sleep 5

# pip installation 
sudo apt install python3-pip 

sleep 5 
# Python requirements to interact with Vault and Kafka APIs  

pip install kafka-python hvac 

sleep 5 

sudo apt-get install jq 


sleep 5

#############################################

# Docker installation 

sudo apt-get install \
    ca-certificates \
    curl \
    gnupg \
    lsb-release

sleep 1
sudo mkdir -p /etc/apt/keyrings

sleep 2 
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
sleep 2 

echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

sudo apt-get install docker-ce docker-ce-cli containerd.io docker-compose-plugin

sleep 5 

sudo apt-get install docker-compose 


#############################################
sleep 5


# Adding current user to docker group 
sudo usermod -aG docker $USER

# Refreshing configuration 

sleep 5 

newgrp docker






