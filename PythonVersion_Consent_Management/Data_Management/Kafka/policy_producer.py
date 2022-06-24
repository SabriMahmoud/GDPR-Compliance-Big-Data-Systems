import json
import time
from kafka import KafkaProducer
import argparse
from encrypt_with_vault import EncryptionDecryptionUnit


parser = argparse.ArgumentParser()
parser.add_argument("-t","--topic",type=str,default="",help="Type topic name" )
arguments = parser.parse_args()
KAFKA_TOPIC = arguments.topic

if KAFKA_TOPIC=="":
    KAFKA_TOPIC = "policyevents"
    
producer = KafkaProducer(bootstrap_servers="localhost:9092")
print("===================================================\n")

print("Going to be generating data after 5 seconds")
print("Will generate one unique order every 2 seconds\n")

time.sleep(5)

# Retriving data from json file 
# Return a list object where each row is a transaction 
# Modify this function as needed el moufid raja3 list 

def read_from_data_source(file_name):
    with open(file_name) as data_source :
        data = json.load(data_source)
    return data 

data = read_from_data_source('PythonVersion_Consent_Management/Data_Management/Kafka/Policy.json')





print("===================================================\n")

for i in range(len(data)):
    producer.send(KAFKA_TOPIC, json.dumps(data[i]).encode("utf-8"))
    print(f"Done Sending..{i}")
    print(f"Remaining .. {len(data)-i-1}")
    time.sleep(1)
