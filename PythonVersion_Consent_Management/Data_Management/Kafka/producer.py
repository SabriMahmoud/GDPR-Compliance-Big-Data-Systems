import json
import time

from kafka import KafkaProducer

KAFKA_TOPIC = "evt"


producer = KafkaProducer(bootstrap_servers="localhost:9092")

print("Going to be generating data after 10 seconds")
print("Will generate one unique order every 2 seconds")
time.sleep(5)

# Retriving data from json file 
# Return a list object where each row is a transaction 
# Modify this function as needed el moufid raja3 list 

def read_from_data_source(file_name):
    with open('kafka/'+file_name) as data_source :
        data = json.load(data_source)
    return data 

data = read_from_data_source('MOCK_DATA.json')


for i in range(len(data)):
    producer.send(KAFKA_TOPIC, json.dumps(data[i]).encode("utf-8"))
    print(f"Done Sending..{i}")
    print(f"Remaining .. {len(data)-i-1}")
    time.sleep(1)