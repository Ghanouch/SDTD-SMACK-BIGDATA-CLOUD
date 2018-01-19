#!/bin/bash

# Author : SDTD Group

#-----PARAMS OF KEY NAMES AND PATHS

NOM_CLE_DEPLOY = "deploy"
NOM_SCRIPT_HOSTS="hosts"
NOM_KEY_INTERCONNECTION="cluster_interconnection"
NOM_JOB_CONSUMER="RDD_Crypto.jar"


#----------------- Extract Ip of bastion 

cmd_ip=`grep -n "bastion" hosts | cut -d : -f1`
bastion_Ip=$(head -n $((cmd_ip+1)) hosts | tail -n 1)


#------------------ Send Use Case Script to bastion

NOM_SCRIPT_BASTION="UseCase.sh"
scp -i $NOM_CLE_DEPLOY -o StrictHostKeyChecking=no $NOM_SCRIPT_BASTION ubuntu@$bastion_Ip:~/
echo "Sending Use Case Script ..."

#cmd_send_script="scp -i deploy_cle -o StrictHostKeyChecking=no $NOM_SCRIPT ubuntu@$bastion_Ip:~/"
#echo "cmd_send_script ...: $cmd_send_script"

#------------------ Send Host Script to bastion

scp -i deploy_cle -o StrictHostKeyChecking=no $NOM_SCRIPT_HOSTS ubuntu@$bastion_Ip:~/
echo "Sending Hosts File ..."

#cmd_send_script="scp -i deploy_cle -o StrictHostKeyChecking=no $NOM_SCRIPT ubuntu@$bastion_Ip:~/"
#echo "cmd_hosts_script ...: $cmd_send_script"



#------------------ Send cluster_interconnection key  to bastion

scp -i deploy_cle -o StrictHostKeyChecking=no $NOM_KEY_INTERCONNECTION ubuntu@$bastion_Ip:~/
echo "Sending cluster_interconnection key File ..."

#cmd_send_script="scp -i deploy_cle -o StrictHostKeyChecking=no $NOM_SCRIPT ubuntu@$bastion_Ip:~/"
#echo "cmd_hosts_script ...: $cmd_send_script"



#-----------------------Send Jar Consumer to bastion


#cmd_send_conso="scp -i deploy_cle -o StrictHostKeyChecking=no $NOM_JOB_CONSUMER ubuntu@$bastion_Ip:~/"
scp -i deploy_cle -o StrictHostKeyChecking=no $NOM_JOB_CONSUMER ubuntu@$bastion_Ip:~/
echo " SEND CONSUMER JAR ...: $cmd_send_conso "



#-------------------- Lunch the script on bastion

ssh -i deploy_cle ubuntu@$bastion_Ip sh $NOM_SCRIPT_BASTION
echo "Lunching use case ..."
#cmd_lunch_bastion="ssh -i deploy_cle ubuntu@$bastion_Ip sh $NOM_SCRIPT_BASTION"
#echo "cmd_lunch_bastion $cmd_lunch_bastion"



