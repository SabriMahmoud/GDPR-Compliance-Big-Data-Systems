import json
import time
from kafka import KafkaProducer
import argparse
from encrypt_with_vault import EncryptionDecryptionUnit


EDU = EncryptionDecryptionUnit('http://localhost:8200', token = 'myroot',transit="Test")
EDU.create_key("bankerise_key")
EDU.encryptData("bankerise_key","hello")
ciphertext= EDU.ciphertext
print(ciphertext)

parser = argparse.ArgumentParser()
parser.add_argument("-t","--topic",type=str,default="",help="Type topic name" )
arguments = parser.parse_args()
KAFKA_TOPIC = arguments.topic

if KAFKA_TOPIC=="":
    KAFKA_TOPIC = "events"
    
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
def encrypt_dict(row):
    for key in row.keys():
        plaintext = row[key]
        EDU.encryptData("bankerise_key",str(plaintext))
        row[key] = EDU.ciphertext

    return row 


data = read_from_data_source('MOCK_DATA.json')





print("===================================================\n")

for i in range(len(data)):
    producer.send(KAFKA_TOPIC, json.dumps(encrypt_dict(data[i])).encode("utf-8"))
    print(f"Done Sending..{i}")
    print(f"Remaining .. {len(data)-i-1}")
    time.sleep(1)
