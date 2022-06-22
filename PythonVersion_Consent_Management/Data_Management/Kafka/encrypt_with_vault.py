import base64
import sys
import hvac

class EncryptionDecryptionUnit :
    def __init__(self , url , token,transit) :
        self.client = hvac.Client(url = url , token = token )
        self.decrypted_plaintext = ""
        self.ciphertext = ""
        self.transit = transit
        self.mount_transit_engine()

    def base64ify(self,bytes_or_str):
        
        if isinstance(bytes_or_str, str):
            bytes_or_str +="1"
            input_bytes = bytes_or_str.encode('utf8')
        else:
            input_bytes = bytes_or_str

        output_bytes = base64.urlsafe_b64encode(input_bytes)

        return output_bytes.decode('ascii')

    def mount_transit_engine(self):
        try:
            self.client.write("sys/mounts/"+self.transit,type = "transit")
        except Exception as e : 
            print(e)


    def create_key(self,key_name) :
        # #  POST: /{mount_point}/keys/{key_name}. Produces: 204 (empty body) 

        self.client.write(path =self.transit+"/keys/"+key_name)
        print("Key created successfully")

    
    def encryptData(self,key_name,text):
        encrypt_data_response = self.client.secrets.transit.encrypt_data(
            name = key_name,
            plaintext= self.base64ify(text.encode()),
            mount_point = self.transit
        )
        self.ciphertext = encrypt_data_response['data']['ciphertext']




















# client = hvac.Client(url = 'http://localhost:8200', token = 'myroot')
# try :
#     client.write("sys/mounts/titii",type = "transit")
# except Exception as e:
#     print(e)

    

# #  POST: /{mount_point}/keys/{key_name}. Produces: 204 (empty body) 
# client.write(path="titii/keys/my-key")


# encrypt_data_response = client.secrets.transit.encrypt_data(
#     name='my-key',
#     plaintext=base64ify('hi its me hvac'.encode()),
#     mount_point = "titii"
# )
# ciphertext = encrypt_data_response['data']['ciphertext']
# print('Encrypted plaintext ciphertext is: {cipher}'.format(cipher=ciphertext))



