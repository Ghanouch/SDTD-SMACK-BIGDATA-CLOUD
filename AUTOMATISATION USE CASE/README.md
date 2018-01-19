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
