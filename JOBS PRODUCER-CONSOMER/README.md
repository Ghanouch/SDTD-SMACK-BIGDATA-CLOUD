--- PRODUCER  

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
