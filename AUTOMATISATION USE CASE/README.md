### II - Lancement du cas d'utilisation : ###

- **Copier** les fichiers : hosts et les deux clés générées ( cluster_interconnection, deployer ) qui sont dans le dossier "DEPLOIMENT D'INFRASTRUCTURE ET LES SERVICES/terraform", et les mettre dans le dossier "AUTOMATISATION USE CASE"

		#cd AUTOMATISATION\ USE\ CASE/

		#cp ../DEPLOIMENT\ D\'INFRASTRUCTURE\ ET\ LES\ SERVICES/terraform/hosts .
		#cp ../DEPLOIMENT\ D\'INFRASTRUCTURE\ ET\ LES\ SERVICES/terraform/deployer .
		#cp ../DEPLOIMENT\ D\'INFRASTRUCTURE\ ET\ LES\ SERVICES/terraform/cluster_interconnection .

- Aller dans le dossier "AUTOMATISATION USE CASE"

- Inclure dans le dossier "AUTOMATISATION USE CASE" le Job du Consumer: 

*Vous avez deux choix pour l'avoir :* 

- A travers le lien suivant :
http://www.mediafire.com/file/dd2dfyfkx9iyo9q/RDD_Crypto.jar

- A partir du dossier : "CONSUMER/SparkKafka" PAR MAVEN 
```
			 # cd SDTD-SMACK-BIGDATA-CLOUD/CONSUMER/SparkKafka/
			 # mvn package 
			 copier le jar généré sur "AUTOMATISATION USE CASE" et le renomer à RDD_Crypto.jar
			 # mv target/SparkKafka-0.0.1-SNAPSHOT.jar RDD_Crypto.jar
			 # cp RDD_Crypto.jar ../../AUTOMATISATION\ USE\ CASE/
```

- Configurer l'ensemble des paramétres :
-  Dans le fichier "UseCase.sh" ( Path  : SDTD-SMACK-BIGDATA-CLOUD/AUTOMATISATION\ USE\ CASE/)

			# PARAMS OF TOPIC CREATION 
			TOPIC_NAME="NV_TOPIC"
			REP_FACTOR="1"

			# PARAMS OF PRODUCER JOB

			BORNE_MINIMAL="0"
			BORNE_MAXIMAL="25"
			SPEED="2"

 - Dans le fichier "Remote-UseCase.sh"

			# PARAMS OF KEY NAMES AND PATHS
			NOM_CLE_DEPLOY = "deployer"
			NOM_SCRIPT_HOSTS="hosts"
			NOM_KEY_INTERCONNECTION="cluster_interconnection"
			NOM_JOB_CONSUMER="RDD_Crypto.jar"
			
 - Lancer le script sur "AUTOMATISATION USE CASE"
```
		# cd AUTOMATISATION\ USE\ CASE/
		# sh Remote-UseCase.sh
```
-  Vous pourrez visualiser le tout grâce au Zeppelin ou à l'OpsCenter (mdp : admin, mdp : admin) 	
