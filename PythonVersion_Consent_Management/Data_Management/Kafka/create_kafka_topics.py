from kafka.admin import KafkaAdminClient, NewTopic
import argparse 


def retrive_topic_list() :
    parser = argparse.ArgumentParser()
    parser.add_argument("-t","--topic-list",type=str,default="",help="Type topics as the following example : 'topic1-topic2-topic3' " )
    parser.add_argument("-p","--partitions",type=str,default="",help="Type the number of partitions for each topic as the following example '1-2-3' ")
    
    arguments = parser.parse_args()
    topics = arguments.topic_list
    partitions = arguments.partitions
    if topics != "" :
        topics = topics.split('-')
        partitions= partitions.split('-')
    return topics, partitions



if __name__ == '__main__' :
        topics_list , partitions_list = retrive_topic_list()  
        if len(topics_list) == len(partitions_list) :       
            if topics_list != "" :
                try : 
                        
                    admin_client = KafkaAdminClient(
                        bootstrap_servers="localhost:9092",
                        client_id ="BankeriseAdmin"
                    )
                    kafka_topics = []
                    for topic,partition in zip(topics_list,partitions_list):
                        kafka_topic = NewTopic(name = topic , num_partitions = int(partition) , replication_factor = 1)
                        kafka_topics.append(kafka_topic)
                    admin_client.create_topics(kafka_topics,validate_only = False)
                    print("Topics Created Successfully")
                    print("========================= Current Exsisting  Topics ==========================")
                    for topic in admin_client.list_topics() : 
                        print("- " + topic )
                    print("==============================================================================")
                except Exception as e :
                    print(e)
                

            else :
                print("Topic name is empty")
        else :
            print("Topics list and partitions list length mis-match")
        
            

