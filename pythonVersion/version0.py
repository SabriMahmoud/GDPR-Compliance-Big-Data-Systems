from tkinter import E
import hvac

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
