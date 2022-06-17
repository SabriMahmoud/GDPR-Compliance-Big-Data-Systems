
import hvac
import sys
sys.path.append('../../Api')
from vault_security.Credentials import Credentials



class VaultConfiguration :
    def __init__(self,token=None,url=None,serviceName="myServices",database_name="Bankerise"):
        self.client = hvac.Client(url = url , token = token)
        self.writeInVault(serviceName,database_name)

    
    #create_response = client.secrets.kv.v2.create_or_update_secret( path = 'foo', secret = dict(baz ='baz') ) 

    # Declaring variables for Secret Engine  
    def writeInVault(self,serviceName,database_name):
        try :
            self.client.write("sys/mounts/mongodb",type = "database")
        except Exception as e :
            print('Connection Already established')

        try :
            self.client.write("/mongodb/config/"+database_name,plugin_name=Credentials.plugin_name,allowed_roles=Credentials.allowed_roles,
                    connection_url = Credentials.connection_url , username = Credentials.username , password = Credentials.password )
        except Exception as e : 
            print("DataSource Not Found")
        self.client.write("/mongodb/roles/"+serviceName,db_name=Credentials.db_name,creation_statements=Credentials.creation_statements,
                default_ttl= Credentials.default_ttl, max_ttl = Credentials.max_ttl )

    def retrieveCredentials(self,serviceName):
        vaultResponse = self.client.read("/mongodb/creds/"+serviceName)
        serviceUserName = vaultResponse["data"]["username"]
        servicePassword = vaultResponse["data"]["password"]

        return serviceUserName,servicePassword