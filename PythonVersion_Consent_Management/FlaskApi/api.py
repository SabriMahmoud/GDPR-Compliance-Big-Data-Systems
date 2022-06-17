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


"""*************** Global variables *******************"""
"""***************************************************"""
"""***************************************************"""

app = Flask(__name__)
vault = VaultConfiguration(url = 'http://vault:8200' ,token = 'myroot')
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
    serviceUserName,servicePassword = vault.retrieveCredentials(serviceName)
    DB_URI = "mongodb://"+ serviceUserName + ":" +servicePassword + "@mongodb3:27017/" + database_name
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
                if user.id == user_id :
                    parsed_user = parse_json(user.to_json())
                    return make_response(jsonify(parsed_user),200)
            return make_response(jsonify(parsed_user),404)
        except hvac.exceptions.InvalidRequest as e :
            print(e, flush=True)
            return "unknown service name : " + service_name 


"""***************************************************"""
"""***************************************************"""
"""***************************************************"""


def parse_json(data):
    return json.loads(json_util.dumps(data)) 



if __name__ == '__main__' :
    
    serve(app, host="0.0.0.0", port=5000)

