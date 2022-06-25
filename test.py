import base64
import hvac


class DecryptionUnit :
    def __init__(self ,client,transit) :
        self.client = client
        self.decrypted_plaintext = ""
        self.transit = transit


    def base64ify(self,bytes_or_str):
        
        if isinstance(bytes_or_str, str):

            input_bytes = bytes_or_str.encode('utf8')
        else:
            input_bytes = bytes_or_str

        output_bytes = base64.urlsafe_b64decode(input_bytes)
        

        return output_bytes.decode('ascii')


    def decryptData(self,key_name,ciphertext):
        decrypt_data_response = self.client.secrets.transit.decrypt_data(
            name=key_name,
            ciphertext=ciphertext,
            mount_point = self.transit
        )
        self.decrypted_plaintext = self.base64ify(decrypt_data_response['data']['plaintext'])



def base64ify(bytes_or_str):
        
        if isinstance(bytes_or_str, str):
            bytes_or_str +="1"
            input_bytes = bytes_or_str.encode('utf8')
        else:
            input_bytes = bytes_or_str

        output_bytes = base64.urlsafe_b64decode(input_bytes)
        

        return output_bytes.decode('ascii')

client = hvac.Client(url='http://127.0.0.1:8200',token="myroot")
DU = DecryptionUnit(client,'Test')

DU.decryptData("bankerise_key",ciphertext="vault:v1:L/4VjQOi88yzuSaW//16ZH6U+FJDSXzbvrNnaA5UfUSYrw==")
print(DU.decrypted_plaintext)
# for key,value in {"date":"4/23/2022","email":"bGdsZW50d29ydGgwQG92ZXItYmxvZy5jb20=","first name":"TGFtb250","gender":None,"ip adress":None,"transfert_amount":"MjIx","user_id":1}.items():
#     if key != "user_id" and key!="coll_id" and key !="date" and not isinstance(value, type(None)):
#         print(base64ify("TGFtb250"))
