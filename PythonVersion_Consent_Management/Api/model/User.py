from flask_mongoengine import  MongoEngine



database = MongoEngine()

class UsersView(database.Document) :
    _id = database.ObjectIdField()
    id = database.StringField()
    first_name = database.StringField()
    last_name = database.StringField()
    email =database.StringField()
    gender = database.StringField()
    ip_address = database.StringField()
    meta = {'collection': 'UsersView'}

    def to_json(self):
        return {
            "coll_id" : self._id,
            "user_id" : self.id,
            "first name" : self.first_name,
            "email" : self.email,
            "gender" : self.gender,
            "ip adress" : self.ip_address
        }

