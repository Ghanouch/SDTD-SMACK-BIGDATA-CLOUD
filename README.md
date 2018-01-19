# Systemes-Distribues-pour-le-Traitement-de-Donnees-sur-le-cloud

------------------------------ GUIDE D'INSTALLATION ET D'UTILISATION 

Copier le projet sur votre rmachine : 
#git clone https://github.com/Ghanouch/Systemes-Distribues-pour-le-Traitement-de-Donnees-sur-le-cloud.git


I - LANCER LE DEPLOIEMENT

ENTRER SUR LE CHEMIN SUIVANT  : 
#cd EPLOIMENT D'INFRASTRUCTURE ET LES SERVICES/terraform

Guide d'utilisation du script:

	1-Génerer 2 clé RSA une pour le bastion et l'autre pour le reste de la stack grâce à: "ssh-keygen"
	  Vous pouvez choisir le nom que vous voulez, exemple : deployer et cluster_interconnection
	  Elles doivent être mises dans le dossier "terraform"

		#ssh-keygen

	2-Ouvrir le fichier "secret.tfvars" dans le dossier "terraform" et remplir les champs suivants:
	  + access_key et secret_key : respectivement la clé d'accès et la clé secrète de votre utilisateur AWS  
	  + region: la région où le cluster va être déployé, exemple: "us-east-1"
	  + ami: ami de la machine ubuntu 16.04 d'AWS, on la retrouve lorsqu'on essaie de créer manuellement une machine
	  + cassandra_ami: même que "ami" mais celle de ubuntu 14.04
	  + deployer_key_name: le nom de la clé publique utilisée pour le bastion
	  + deployer_public_key: le contenu de la clé publique du bastion
	  + cluster_key_name: le nom de la clé publique utilisée pour la stack
	  + cluster_public_key: le contenu de la clé publique de la stack
	  + cassandra_instance_type: la taille des machines cassandra (minimum medium)
	  + opscenter_instace_type: la taille de la machine opscenter (minimum medium)

	3-Ouvrir le fichier wrap-up.sh est remplir les champs indiqués (ceux des noms des clés privées)

	4-Lancer le script wrap-up.sh en sudo si possible (parfois il faut des droits d'administrateur)

######


II - LANCER LE CAS D'UTILISATION

	1- COPIER LES FICHIERS SUIVANT :  hosts ET les deux clé générées ( cluster_interconnection_key,deploy_key )  QUI SONT SUR le dossier "DEPLOIMENT D'INFRASTRUCTURE ET LES SERVICES/terraform", ET VEUILLEZ LES METTRE  SUR LE DOSSIER
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


---------LA DESCRIPTION DE LA REALISATION DU CAS D'UTILISATION 

---I   PRODUCER  

  1- Architecture du Producer  :
	Voir :https://github.com/Ghanouch/SDTD-SMACK-BIGDATA-CLOUD/blob/master/IMAGES%20DE%20L'ARCHITECTURE/Architecture%20du%20Producer.PNG
  2- Réalisation du Producer   :  
		
 	- Cette réalisation permet de recevoir ( en streaming ) les données des Cryptomonnaie, pratiquer un filtrage avant de passer les résultats
 	 à un Kafka broker. Cela se fait en plusieurs étapes :

 	 	#A- LA RECUPERATION D'UN JSON EN AKKA HTTP 
	 		* Un acteur de AKKA HTTP récupére un JSON à parti de l'URI suivant :  https://api.coinmarketcap.com/v1/ticker/ 

	 	#B- FILTRAGE Des données récupérées
		 	* Grace à Akka HTTP, nous pouvons récupérer Le flux de données ( JSON ) en Streaming, cela, nous permettra de pratiquer un filtrage basé sur le classement du cryptomonnaie actuel afin de se concentrer que sur une partie ( par exemple : les 20 cryptomonnaies les plus classés ) 
       	
       		#C- Publication des résultats sur un TOPIC D'un  broker KAFKA 
       			* Aprés avoir réalisé le filtrage, nous pouvons publier les résultats sur un ou plusieurs topics, lesquels nous sommes abonnés sur un ou plusieurs broker KAFKA


  3- LANCEMENT DU JOB de Producer  :

  	* Ce job prend 4 paramétres qui sont : 
	 		A- Broker KAFKA     : l'URL du Kafka Broker
	 		B- TOPIC CHOISI     : L'ensemble des topics choisis 
	 		C- BORNE MINIMAL  : Le classement minimal du cryptomonnaie
	 		D- BORNE MAXIMAL  : Le classement maximal du cryptomonnaie
	 		E- NOMBRE REQUETE par minute : C'est le nombre de JSON à récupérer par minute (cela permet de controler la vitesse de requetage )

  	* Example pour lancer le JOB : 	

			#java -jar NOM_JOB_PRODUCER --broker:@IP_BROKER:PORT --topic:NOMTOPIC --min:BORNE_MINIMAL --max:BORNE_MAXIMAL --speed:NOMBRE_REQUETE_PAR_MINUTE


---II    CONSUMER 

	1- L'Architecture du Consumer 
	Voir : https://github.com/Ghanouch/SDTD-SMACK-BIGDATA-CLOUD/blob/master/IMAGES%20DE%20L'ARCHITECTURE/Architecture%20du%20Consumer.PNG
	2- Rélisation de Consumer : 
		Voir sur LE DOSSIER Consumer

	3- LANCEMENT DU JOB DE CONSUMER : 

	* Ce job prend 3 paramétres qui sont : 
		  A- Broker KAFKA      : L'URL du kafka Broker ou de la listes des kafka Broker
		  B- TOPIC CHOISI      : L'ensemble des topics choisis
		  C- L'URL du Cassandra: L'URL de Cassandra

    * Example pour lancer le JOB : 	

	  	   #java -jar NOM_JOB_CONSUMER :@IP_BROKER:PORT NOMTOPIC @IP_CASSANDRA 



---III  L'automatisation du cas d'utilisation  

   1- Introduction 

 L'idée principal est de pouvoir réaliser l'ensemble des tâches requises pour le lancement du cas d'utilisation en un seul clique, tout 
 	en respectant l'architecture préalablement déployée par terraform et ansible. ( Figure Architecture Global Bastillon )


  2- Architecture de l'automatisation 
	https://github.com/Ghanouch/SDTD-SMACK-BIGDATA-CLOUD/blob/master/IMAGES%20DE%20L'ARCHITECTURE/Architecture%20Automatisation%20Use%20Case.PNG
	 #-L'architecture suivante montre l'ensemble des machines requises et aussi le sénario mis en oeuvre  pour 
	 	réaliser cette automatisation :

  3- REALISATION ET Sénario de l'automatisation

	Le sénario de l'automatisation du cas d'utilisation se fait à travers deux étapes :

		1--Récupération des fichiers d'entrées :
		 	* La récupération d'un fichier HOSTS (par terraform) qui contient les adresses IP des différentes machines attribués à leurs roles.Example :

			https://github.com/Ghanouch/SDTD-SMACK-BIGDATA-CLOUD/blob/master/IMAGES%20DE%20L'ARCHITECTURE/exemple%20de%20HOSTS%20PNG.png
		 	* La récupération des clés ssh utilisé pour accéder à la machine bastian "deploy_cle", aussi que la clé utilisé par la machine bastian pour accéder aux différentes autres machines "cluster_interconnection_cle".


	 	2--Le lancement automatique du cas d'utilisation par l'éxécution d'un seul script ( "sh Remote-UseCase.sh") qui permettra de :

	 		* Envoyer à la machine bastian le script "UseCase.sh" sur lequel, il ya les différentes taches à éxécuter, et le paramétrage personalisé
	 		 ( Nombre de répliaction, nom de topic, La vitesse de requetage ...) 
	 		* Envoyer à la machine bastian le fichier HOSTS
	 		* Envoyer à la machine bastian la clé "cluster_interconnection_cle".
	 		* Envoyer à la machine bastian le lien pour accéder au JOB PRODUCER
	 		* Envoyer à la machine bastian le lien pour accéder au JOB CONSUMER
	 		* Exécuter le script "UseCase.sh" sur la machine bastian.

	 		Le script à éxécuté sur la machine Bastian "UseCase.sh" permettra automatiquement de :

	 			1- L'éxtraction de l'ensemble des adresses Ip, chacun représente une machine sur laquel est 					installé un service précis
	 			2- La création du Topic sur un ou plusieurs broker Kafka, tout en précisant un ensemble de 					paramétres ( Zookeper IP, La facteur de réplication, Le nom du Topic)	
	 			3- Lancement du JOB PRODUCER sur une machine qui a le role producer, cela se fait aprés que 					cette machine récupérera
	 			   l'ensemble des paramétres par la machine bastian :
	 			   	 -URI JOB PRODUCER : Lien permettant de télécharger le JOB PRODUCER EN LIGNE
	 			   	 -L'ENSEMBLE DES PARAMETRE PERMETTANT DE LANCER CE JOB ( Voir la partie "LANCEMENT 						DU JOB de Producer" )
	 			4- Lancement du JOB Consumer sur une machine sur laquel tourne le service Spark Driver ( 				sur notre cas, une machine avec  le role d'un Mesos MASTER, puisqu'elle contient les deux services : Mesos Master, Spark Driver) aussi cela se fait aprés avoirrécupérées l''ensemble des paramétres par la machine bastian:
	 			-URI permettant de télécharger le consumer (ou carrement, comme sur notre cas, la récéption 					du job préalablement recu par la machine bastian
	 			-L'ENSEMBLE DES PARAMETRE PERMETTANT DE LANCER CE JOB ( Voir la partie "LANCEMENT DU JOB de 					Consumer" )






	
