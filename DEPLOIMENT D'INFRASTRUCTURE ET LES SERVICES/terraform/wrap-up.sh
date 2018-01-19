#!/bin/bash

terraform init
terraform plan -var-file=secret.tfvars
echo yes | terraform apply -var-file=secret.tfvars



bastion_public_ip=$(terraform output bastion_public_ip)

if [ -f hosts ]
then
    sudo rm hosts
fi

cat > hosts << EOF
[utils_java_8]
$(echo > hosts | terraform output zookeeper_ips | sed 's/,//g')
$(echo > hosts | terraform output mesos_slave_ips | sed 's/,//g')
$(echo > hosts | terraform output mesos_master_ips | sed 's/,//g')
$(echo > hosts | terraform output kafka_ips | sed 's/,//g')
$(echo > hosts | terraform output producer_ip)

[zookeeper]
$(echo > hosts | terraform output zookeeper_ips | sed 's/,//g')

[mesos_master]
$(echo > hosts | terraform output mesos_master_ips | sed 's/,//g')

[mesos_slave]
$(echo > hosts | terraform output mesos_slave_ips | sed 's/,//g')

[kafka]
$(echo > hosts | terraform output kafka_ips | sed 's/,//g')

[cassandra_nodes]
$(echo > hosts | terraform output cassandra_ips | sed 's/,//g')

[cassandra_public_ips]
$(echo > hosts | terraform output cassandra_public_ips | sed 's/,//g')

[cassandra-opscenter]
$(echo > hosts | terraform output opscenter_ip)

[cassandra-opscenter-public]
$(echo > hosts | terraform output opscenter_public_ip)

[bastion]
$(echo > hosts | terraform output bastion_public_ip)

[producer]
$(echo > hosts | terraform output producer_ip)

EOF

scp -i "nom de la clé privée du bastion" -o StrictHostKeyChecking=no "nom de la clé privée du cluster" ubuntu@$bastion_public_ip:~/.ssh/id_rsa 
scp -r -i "nom de la clé privée du bastion" -o StrictHostKeyChecking=no ../ansible ubuntu@$bastion_public_ip:~/ 
scp -i "nom de la clé privée du bastion" -o StrictHostKeyChecking=no hosts ubuntu@$bastion_public_ip:~/ansible/ 

ssh -i "nom de la clé privée du bastion" -o StrictHostKeyChecking=no ubuntu@$bastion_public_ip "export ANSIBLE_HOST_KEY_CHECKING=False && cd ansible && ansible-playbook -i hosts main.yml" 
