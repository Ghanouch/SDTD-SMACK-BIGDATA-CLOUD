#!/bin/bash


#I-- Set all the parameters

# PARAMS OF TOPIC CREATION 
TOPIC_NAME="NV_TOPIC"
REP_FACTOR="1"

# PARAMS OF PRODUCER JOB
BORNE_MINIMAL="0"
BORNE_MAXIMAL="25"
SPEED="2"

# PARAMS OF CONSUMER 
LIEN_JOB_CONSUMER="https://docs.google.com/uc?export=download&id=1R56-3o1JJk2tlMgxGVHR-9vYZSZmgjkw"
className="RddStatistique"



#II-- The extraction of the set of Ip addresses, each represents a machine on which is installed a specific service

#2 Kafka IP 
cmd_kafka=`grep -n "kafka" hosts | cut -d : -f1`
KAFKA1=$(head -n $((cmd_kafka+1)) hosts | tail -n 1)
KAFKA2=$(head -n $((cmd_kafka+2)) hosts | tail -n 1)

#Zookeeper IP
cmd_zk=`grep -n "zookeeper" hosts | cut -d : -f1`
ZK1=$(head -n $((cmd_zk+1)) hosts | tail -n 1)

#Producer IP
cmd_prod=`grep -n "producer" hosts | cut -d : -f1`
PRODUCER=$(head -n $((cmd_prod+1)) hosts | tail -n 1)

#Mesos Master IP
cmd_prod=`grep -n "mesos_slave" hosts | cut -d : -f1`
MESOS_MASTER=$(head -n $((cmd_prod+1)) hosts | tail -n 1)

#Cassandra IP
cmd_Cassandra=`grep -n "cassandra" hosts | cut -d : -f1`
CASSANDRA=$(head -n $((cmd_prod+1)) hosts | tail -n 1)


#III--The creation of the Topic on one or more Kafka broker, while specifying a set of parameters (Zookeper IP, the factor of replication, the name of the Topic)

ssh -i cluster_interconnection ubuntu@$KAFKA1 /home/ubuntu/kafka/kafka_2.11-1.0.0/bin/kafka-topics.sh --create --replication-factor $REP_FACTOR --partitions 1 --zookeeper $ZK1:2181 --topic $TOPIC_NAME

#IV-- Launch of JOB PRODUCER on a machine that has the role producer 
LIEN_JOB_PRODUCER="https://docs.google.com/uc?export=download&id=1QO4hABoVMplr_IKkZA87BesDWcRdRCdy"

cmd_get_producer="wget '$LIEN_JOB_PRODUCER' -O Producer_Crypto.tar.gz"
NOM_JOB_PRODUCER="Producer_Crypto.jar"

#WGET JOB ON PRODUCER
ssh -i cluster_interconnection ubuntu@$PRODUCER $cmd_get_producer
ssh -i cluster_interconnection ubuntu@$PRODUCER tar -xvzf Producer_Crypto.tar.gz

#EXECUTE JOB ON PRODUCER
ssh -i cluster_interconnection ubuntu@$PRODUCER java -jar $NOM_JOB_PRODUCER --broker:$KAFKA1:9092 --topic:$TOPIC_NAME --BORNE_MINIMAL:$BORNE_MINIMAL --BORNE_MAXIMAL:$BORNE_MAXIMAL --speed:$SPEED &


# V-Launching the JOB Consumer on a machine running the Spark Driver service

cmd_get_consumer="wget '$LIEN_JOB_CONSUMER' -O RDD_Crypto.tar"
NOM_JOB_CONSUMER="RDD_Crypto.jar"


# METHOD 1 : - WGET JOB ON CONSUMER ( MESOS )

#ssh -i cluster_interconnection ubuntu@$MESOS_MASTER $cmd_get_consumer
#ssh -i cluster_interconnection ubuntu@$MESOS_MASTER tar RDD_Crypto.tar

# METHOD 2 : Send JOB to Mesos Master 
scp -i cluster_interconnection -o StrictHostKeyChecking=no $NOM_JOB_CONSUMER ubuntu@$MESOS_MASTER:~/
#Execute Job COnsumer With Spark
ssh -i cluster_interconnection ubuntu@$MESOS_MASTER /home/ubuntu/spark-2.2.1-bin-hadoop2.7/bin/spark-submit --class \"$className\" --master local[4] $NOM_JOB_CONSUMER $KAFKA1:9092 $TOPIC_NAME $CASSANDRA &




