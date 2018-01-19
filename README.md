# Systemes-Distribues-pour-le-Traitement-de-Donnees-sur-le-cloud

------------------------------ GUIDE D'INSTALLATION ET D'UTILISATION 

Copier le projet sur votre rmachine : 
#git clone https://github.com/Ghanouch/Systemes-Distribues-pour-le-Traitement-de-Donnees-sur-le-cloud.git


I - LANCER LE DEPLOIEMENT

ENTRER SUR LE CHEMIN SUIVANT  : 
#cd DEPLOIMENT\ D\'INFRASTRUCTURE\ ET\ LES\ SERVICES/terraform

#####
Prérequis: Installer terraform: https://www.terraform.io/intro/getting-started/install.html
#####
Guide d'utilisation du script:

	1-Génerer 2 clé RSA une pour le bastion et l'autre pour le reste de la stack grâce à: "ssh-keygen"
	  +Vous devez choisir les noms "deployer" et "cluster_interconnection" respectivement pour la clé du bastion 
	   et la clé de la stack
	  +Elles doivent être mises dans le dossier "terraform"
		#ssh-keygen

	2-Ouvrir le fichier "secret.tfvars" dans le dossier "terraform" et remplir les champs suivants:
	  + access_key et secret_key : respectivement la clé d'accès et la clé secrète de votre utilisateur AWS  
	  + region: la région où le cluster va être déployé, exemple: "us-east-1"
	  + ami: ami de la machine ubuntu 16.04 d'AWS, on la retrouve lorsqu'on essaie de créer manuellement une machine
	  + cassandra_ami: même que "ami" mais celle de ubuntu 14.04
	  + deployer_public_key: le contenu de la clé publique du bastion
	  + cluster_public_key: le contenu de la clé publique de la stack
	  + cassandra_instance_type: la taille des machines cassandra (minimum medium)
	  + opscenter_instace_type: la taille de la machine opscenter (minimum medium)

	3-Lancer le script wrap-up.sh en sudo si possible (parfois il faut des droits d'administrateur)
        
	
Si vous voulez détruire la stack, ouvrez un termial dans le dossier "terraform" et utilisez la commande suivante:

		+terraform destroy -var-file secret.tfvars
######


II - LANCER LE CAS D'UTILISATION

	1- COPIER LES FICHIERS SUIVANT :  hosts ET les deux clé générées ( cluster_interconnection,deploy )  QUI SONT SUR le dossier "DEPLOIMENT D'INFRASTRUCTURE ET LES SERVICES/terraform", ET VEUILLEZ LES METTRE  SUR LE DOSSIER
	     "AUTOMATISATION USE CASE"

		#cd SDTD-SMACK-BIGDATA-CLOUD/AUTOMATISATION USE CASE

		#cp ../DEPLOIMENT D'INFRASTRUCTURE ET LES SERVICES/terraform/hosts .
		#cp ../DEPLOIMENT D'INFRASTRUCTURE ET LES SERVICES/terraform/deploy .
		#cp ../DEPLOIMENT D'INFRASTRUCTURE ET LES SERVICES/terraform/cluster_interconnection .

	2- ENTRER SUR LE DOSSIER "AUTOMATISATION USE CASE"

 	2-1.- METTRE SUR LE DOSSIER "AUTOMATISATION USE CASE" LE JOB DE CONSUMER 

	VOUS AVEZ DEUX CHOIX POUR L'AVOIR : 

		2.1 TELECHERGER LE JOB QUI EST SUR LE LIEN SUIVANT :
			http://www.mediafire.com/file/dd2dfyfkx9iyo9q/RDD_Crypto.jar

 		2.2 AVOIR LE JOB A PARTIR DU CODE SOURCE SUR LE DOSSIER : "CONSUMER/SparkKafka" PAR MAVEN 
			# cd CONSUMER/SparkKafka
			 # mvn package 
			 #copier le jar existant à CONSUMER/SparkKafka/target/ sur "AUTOMATISATION USE CASE"
			 #le renomer à RDD_Crypto.jar
	
	2-2-Configurer l'ensemble des paramétres :
		    2-1-1 sur le fichier "UseCase.sh"

			# PARAMS OF TOPIC CREATION 
			TOPIC_NAME="NV_TOPIC"
			REP_FACTOR="1"

			# PARAMS OF PRODUCER JOB

			BORNE_MINIMAL="0"
			BORNE_MAXIMAL="25"
			SPEED="2"
		   2-1-2 sur le fichier "Remote-UseCase.sh"
			# PARAMS OF KEY NAMES AND PATHS
			NOM_CLE_DEPLOY = "deploy"
			NOM_SCRIPT_HOSTS="hosts"
			NOM_KEY_INTERCONNECTION="cluster_interconnection"
			NOM_JOB_CONSUMER="RDD_Crypto.jar"
			
	3- LANCER LE FLUX DE CAS D'UTILISATION sur "AUTOMATISATION USE CASE"
		# cd "AUTOMATISATION USE CASE"
		# sh Remote-UseCase.sh
	4- VISUALISATION DES RESULTATS SUR CASSANDRA OU PAR UN OUTILS DE VISUALISATION	


III -  TOLERANCE AUX PANNES    

1 - TOLERANCE AUX FAUTES " KAFKA "
	  
	 +La création du TOPÎC $TOPICNAME doit avoir un facteur de réplication supérieur à 1  ( facteur sur le fichier AUTOAMISATION DE CAS D'UTILISATION/UseCase.sh)
	 +les adresses IP sur le fichier hosts 
	 +il faut se connecter à la machine bastion :  ssh -i deployer ubuntu@$bastion_Ip

	  1 - TUER LE MASTER DE KAFKA  : 
	  	 se connecter à une machine KAFKA 
	  	 	 #ssh -i cluster_interconnection ubuntu@$kafka_Ip1
	 	 A- Extraire  l'Id dun Leader KAFKA d'un TOPIC 
	 		
	 		#/home/ubuntu/kafka/kafka_2.11-1.0.0/bin/kafka-topics.sh --describe --zookeeper @ZookeperIP:2181 --topic 					$TOPICNAME | grep Leader

	 	 B- ARRETTER LA MACHINE QUI EST LEADER ( Qui a ID de Leader)
	 	 	# CHERCHER LA MACHINE SUR AWS ET L ARRETER MANUELLEMENT 
	 	 	|| ssh -i cluster_interconnection ubuntu@LeaderIP sh shutdown 

	  2 - VERIFIER LE NOUVEAU LEADER { les machines vivants ainsi que les machines mortes }
	  		#/home/ubuntu/kafka/kafka_2.11-1.0.0/bin/kafka-topics.sh --describe --zookeeper @ZookeperIP:2181 --topic 				$TOPICNAME | grep Leader

2 - TOLERANCE AUX FAUTES " ZOOKEPER "


		1 -   CREER UN TOPIC EN LE DONNANT DEUX MACHINES ZOOKEPER 
			# /home/ubuntu/kafka/kafka_2.11-1.0.0/bin/kafka-topics.sh --create --replication-factor 2 --partitions 1 --zookeeper $ZookeperIP1:2181,$ZookeperIP2:2181 --topic $TOPICNAME
			
		2 -   TUER UNE MACHINE SUR LE GROUP DE ZOOKEPPER
  	 		  #ssh -i cluster_interconnection ubuntu@$ZookeperIP1 sh shutdown 


		3 -   VOIR LA REPLICATIONS D'UN TOPIC MEME ZOOKEPER EST MORT 
			# /home/ubuntu/kafka/kafka_2.11-1.0.0/bin/kafka-topics.sh --describe --zookeeper 							$ZookeperIP1:2181,$ZookeperIP2:2181 --topic $TOPICNAME
		


PS : plus de détail, veuillez consulter le rapport ci-joint 

-------------------- FIN 

	
