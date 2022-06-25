import pymongo


admin = pymongo.MongoClient("mongodb://mdbadmin:hQ97T9JJKZoqnFn2NXE@localhost:27017/admin?tls=false")

database = admin["Bankerise"]
roleName="myServices"
try:
    database.command("createRole",roleName,privileges=[{ "resource": { "db": "Bankerise", "collection": "UsersView" }, "actions": [ "find"] }],roles=[{ "role": "read", "db": "Bankerise" } ])
except Exception as e :
    print(e)




