from flask import Flask, jsonify, make_response ,request
from flask_mongoengine import  MongoEngine
from bson import json_util
import json
from waitress import serve
import hvac
from model.User import UsersView
import sys
sys.path.append('../Api')
from vault_security.vault_configuration import VaultConfiguration
from vault_security.DecryptionUnit import DecryptionUnit


"""*************** Global variables *******************"""
"""***************************************************"""
"""***************************************************"""

app = Flask(__name__)
try:
  vault = VaultConfiguration(url = 'http://vault:8200' ,token = 'myroot')
except Exception as e:
  print(e,flush=True)

database = MongoEngine()
database_name ="Bankerise"



"""***************************************************"""
"""***************************************************"""
"""***************************************************"""

@app.route('/<service_name>/users',methods=['GET'])
def getUsers(service_name):
    if request.method == "GET":
        try:
            try:
                connectToDataBase(serviceName=service_name) 
            except ValueError as e :
                print(e,flush=True)
            users = []
            for user in UsersView.objects : 
                users.append(user.to_json())
            users = parse_json(users)
            return make_response(jsonify(users),200)
        except Exception as e :
            return "unknown service name : " + service_name 


"""***************************************************"""
"""***************************************************"""
"""***************************************************"""

def connectToDataBase(serviceName):
      # Retrieving token from vault based on the service name 
      serviceUserName,servicePassword = vault.retrieveCredentials(serviceName)
      # Initializing application database
      DB_URI = "mongodb://"+ serviceUserName + ":" +servicePassword + "@mongodb:27017/" + database_name
      app.config["MONGODB_HOST"] = DB_URI
      database.init_app(app)      

"""***************************************************"""
"""***************************************************"""
"""***************************************************"""


@app.route('/<service_name>/users/<user_id>',methods=['GET'])
def getUserById(service_name,user_id):
        parsed_user = {}
        try :
            try:
                connectToDataBase(serviceName=service_name)
            except ValueError as e :
                print(e,flush=True)
            for user in UsersView.objects: 
                if user.id == int(user_id) :
                    user = decrypt_with_vault(user.to_json())
                    parsed_user = parse_json(user)
                    return make_response(jsonify(parsed_user),200)
            return make_response(jsonify(parsed_user),404)
        except hvac.exceptions.InvalidRequest as e :
            print(e, flush=True)
            return "unknown service name : " + service_name 


"""***************************************************"""
"""***************************************************"""
"""***************************************************"""

def decrypt_with_vault(user):

    try :
        DU = DecryptionUnit(vault.client,'Test')

        for key,value in user.items():
            if key != "user_id" and key!="coll_id" and key !="date" and not isinstance(value, type(None)):
                DU.decryptData(key_name="bankerise_key",ciphertext=value)
                user[key] = DU.decrypted_plaintext
    except Exception as e :
        print(e,flush=True)
    return user




"""***************************************************"""
"""***************************************************"""
"""***************************************************"""


def parse_json(data):
    return json.loads(json_util.dumps(data)) 



if __name__ == '__main__' :
    
    serve(app, host="0.0.0.0", port=5000)

