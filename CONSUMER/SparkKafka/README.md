 ----   CONSUMER 

	1- L'Architecture du Consumer 
	
Voir : https://github.com/Ghanouch/SDTD-SMACK-BIGDATA-CLOUD/blob/master/IMAGES%20DE%20L'ARCHITECTURE/Architecture%20du%20Consumer.PNG
	
	2- Description de Consumer : 
        VOIR ANNEXE  
	3- LANCEMENT DU JOB DE CONSUMER : 

	* Ce job prend 3 paramétres qui sont : 
		  A- Broker KAFKA      : L'URL du kafka Broker ou de la listes des kafka Broker
		  B- TOPIC CHOISI      : L'ensemble des topics choisis
		  C- L'URL du Cassandra: L'URL de Cassandra

    * Example pour lancer le JOB : 	

	  	   #java -jar NOM_JOB_CONSUMER :@IP_BROKER:PORT NOMTOPIC @IP_CASSANDRA 
         
ANNEXE :  
Application de d´emonstration :
L’application de d´emonstration consiste en un programme en Java (Consumer)
qui r´ealise les traitements suivants :
  1. Lecture en streaming depuis un ou plusieurs topics (dans des brokers) aux
  quels le consumer (notre programme) est abonn´e : les adresses IP et ports
  pour sp´ecifier ces topics sont pass´es en param`etre `a notre programme. La
  Lecture en streaming est faite en utilisant Kafka Spark streaming (Spark
  Streaming integration for Kafka), les donn´ees sont donc r´ecup´er´ees en
  format Json.
  2. Transformation des donn´ees en Json `a des objets de type notre model
  (classe Monnaie) `a l’aide de la librairie Jackson Mapper. Ainsi on r´ecup`ere
  une liste d’objets de type Monnaie apr`es chaque lecture (en streaming)
depuis le(s) topic.
  3. Apr`es avoir r´ecup´er´e cette liste on la converti en JavaRDD (`a l’aide de la
  fonction parallelize) pour pouvoir effectuer sur elle un ensemble de calcul
  de mani`ere distribu´ee.
  4. L’ensemble des calculs qu’on effectue ( en streaming ) sont les suivants :
        (a) Calcul du retour sur investissement (rsi) pour chaque cryptomonnaie
        r´ecup´er´e, sur une p´eriode d’une heure, 24 heures et 7 jours.
        (b) Calcul de la rsi annualis´ee : cette valeur permet de voir la variation
        logarithmique annuelle de la valeur d’une cryptomonnaie : en terme
        d’interpr´etation, elle n’ajoute pas une grande valeur vu la variation
        rapide de ces cryptomonnaie. Cependant elle peut ^etre vue comme un
        indice global pour la comparaison entre les d´ef´erentes cryptomonnaies
        : une cryptomonnaie qui a un fort croissement aura une RSI tr`es
        grand, cependant une cryptomonnaie de faible croissance aura un
        RSI petit.
        (c) Calcul de l’indice de de Herfindahl : cet indice refl`ete la part du
        march´e d’une cryptomonnaie parmi toutes les autres cryptomonnaies.
  5. Persistance des indices calcul´es dans une base de donn´ees Cassandra :
pour cela on effectue les actions suivantes :
          (a) Au d´ebut de l’application on appelle une seule fois :
          i. La cr´eation d’une connexion avec la base des donn´ees Cassandra
          `a l’aide de Spark Cassandra Connector.
          ii. La cr´eation d’un keyspace (crypto) s’il n’existe pas d´ej`a. Puis,
          on utilise ce Keyspace et on cr´ee une table Stats qui va contenir
          l’ensemble des indices calcul´ees pour toutes les cryptomonnaies
          apr`es un traitement (streaming), c’est une table statique dont
          les valeurs subissent des updates et non des insertions.
          1(b) Apr`es chaque traitement streaming :
          i. Edition de la table statique Stats avec les nouvelles valeurs cal- ´
          cul´es.
          ii. Pour chaque cryptomonnaie dans la liste du r´esultat, on cr´ee
          une table (s’elle n’existe pas d´ej`a) avec son nom (ex : Table :
          Bitcoin Cach) et on ins`erent les nouvelles valeurs calcul´es avec le
          champs TimeInsert qui sp´ecifie le temps d’insertion des nouvelles
          valeurs
