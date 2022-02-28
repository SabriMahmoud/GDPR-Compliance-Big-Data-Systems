# GDPR Compliance BigData Systems 
This projects aims at implementing Vault as a solution for data protection the research paper **A framework for GDPR compliance in Big Data
systems** made by Mouna Rhahla, Sahar Allegue and Takoua Abdellatif in **Proxym-Lab** 

## Steps to run the project properly 
- First : start Vault Docker container using this command 
``sudo docker run  --net BigData --name vault1 -d --cap-add=IPC_LOCK -e 'VAULT_DEV_ROOT_TOKEN_ID=myroot' -e 'VAULT_DEV_LISTEN_ADDRESS=0.0.0.0:8200' -p 8200:8200 vault``
