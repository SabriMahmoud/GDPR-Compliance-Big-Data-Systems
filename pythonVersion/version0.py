from tkinter import E
import hvac
from pymongo import MongoClient
import pymongo
import hvac.api.secrets_engines.database as DataBase



client = hvac.Client(url = 'http://localhost:8200',token = 'myroot')

print(client.is_authenticated())
print(client.sys.is_initialized())
print(client.sys.is_sealed())

create_response = client.secrets.kv.v2.create_or_update_secret( path = 'foo', secret = dict(baz ='baz') ) 

# Declaring variables for Secret Engine  
name = "Bankerise"
plugin_name = "mongodb-database-plugin"
allowed_roles = '*'
root_rotation_statements = ""
connection_url = "mongodb://{{username}}:{{password}}@mongodb3:27017/admin?tls=false"
username = "mdbadmin" 
password = "hQ97T9JJKZoqnFn2NXE"


try :
    client.write("sys/mounts/mongodb",type = "database")
except Exception as e :
    print('Connection Already established')

try :
    client.write("/mongodb/config/Bankerise",plugin_name=plugin_name,allowed_roles=allowed_roles,
            connection_url = connection_url , username = username , password = password )
except Exception as e : 
    print("DataSource Not Found")

db_name =  "Bankerise"
creation_statements =  ["{ \"db\": \"Bankerise\", \"roles\": [{ \"role\": \"service1\" }, {\"role\": \"service1\", \"db\": \"Bankerise\"}] }"]
default_ttl =  "1h"
max_ttl =  "24h"
client.write("/mongodb/roles/service1",db_name=db_name,creation_statements=creation_statements,
            default_ttl= default_ttl, max_ttl = max_ttl )


def get_database(dataBaseName,username,password):


    # Provide the mongodb atlas url to connect python to mongodb using pymongo
    CONNECTION_STRING = "mongodb://"+ username+ ":" +password + "@localhost:27017/" + dataBaseName
    # Create a connection using MongoClient. You can import MongoClient or use pymongo.MongoClient
    from pymongo import MongoClient
    client = MongoClient(CONNECTION_STRING)

    # Create the database for our example (we will use the same database throughout the tutorial
    return client[dataBaseName] 

vaultResponse = client.read("/mongodb/creds/service1")
serviceUserName = vaultResponse["data"]["username"]
servicePassword = vaultResponse["data"]["password"]


c = get_database("Bankerise",serviceUserName,servicePassword)
for element in c.get_collection("UsersView").find():
    if element['id'] == '1':
        print(element)
