import base64

class DecryptionUnit :
    # Transit stored name it is like a directory where all keys are stored 

    def __init__(self ,client,transit) :
        self.client = client
        self.decrypted_plaintext = ""
        self.transit = transit

    ########################################################
    ########################################################
    ########################################################

    def base64ify(self,bytes_or_str):
        
        if isinstance(bytes_or_str, str):
           
            input_bytes = bytes_or_str.encode('utf8')
        else:
            input_bytes = bytes_or_str

        output_bytes = base64.urlsafe_b64decode(input_bytes)
        

        return output_bytes.decode('ascii')



    ########################################################
    ########################################################
    ########################################################


    def decryptData(self,key_name,ciphertext):
        
        # The key name is already created in application producer with name bankerise key 

        decrypt_data_response = self.client.secrets.transit.decrypt_data(
            name=key_name,
            ciphertext=ciphertext,
            mount_point = self.transit
        )
        self.decrypted_plaintext = self.base64ify(decrypt_data_response['data']['plaintext'])

