
class Credentials:
    name = "Bankerise"
    plugin_name = "mongodb-database-plugin"
    allowed_roles = '*'
    connection_url = "mongodb://{{username}}:{{password}}@mongodb3:27017/admin?tls=false"
    username = "mdbadmin" 
    password = "hQ97T9JJKZoqnFn2NXE"

    db_name =  "Bankerise"
    creation_statements =  ["{ \"db\": \"Bankerise\", \"roles\": [{ \"role\": \"myServices\" }, {\"role\": \"myServices\", \"db\": \"Bankerise\"}] }"]
    default_ttl =  "1h"
    max_ttl =  "24h"