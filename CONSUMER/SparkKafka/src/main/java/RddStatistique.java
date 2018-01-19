import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;

import com.datastax.driver.core.Session;
import com.fasterxml.jackson.databind.ObjectMapper;

import Cassandra.CassandraConnector;
import Cassandra.CryptoStats;
import Model.GlobalStat;
import Model.Monnaie;
import repository.CryptoStatsRepository;
import repository.KeyspaceRepository;
import scala.Tuple2;

public class RddStatistique implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static List<Monnaie> Ref = new LinkedList<>();
	
		/**
		Cette fonction calcule sur une RDD de type Monnaie les indices suivants : 
		1- le retour sur investissement :  ln(VF/Vi) ou Vf est la valeur actuelle du prix de la cryptomonnaie et Vi est la valeur intiale à une date t = (date_actuelle - T).
		ou T est une période de temps qui est soit :
			-- T = 1h : ainsi on calcule rsi_1h : le retour d'investissement sur une heure.
			-- T = 24h : ainsi on calcule rsi_24h : le retour d'investissement sur 24 heures.
                        -- T = 7d : ainsi on calcule rsi_7d : le retour d'investissement sur 7 jours.
		2- retour sur investissement annualisée : 365*rsi_7d : elle permet de voir l'évolution de l'indice rsi (surtout utilisé pour la visualisation)
		3- indice de Herfindahl : c'est la part de marché (pourcentage) d'une cryptommonaie : capital(crypto) divisé par total ou total est la somme de tous les capitaux.

		***/
		public static JavaPairRDD<String, CryptoStats > compute_stat(JavaRDD<Monnaie> monnaies){
			//calcul du total des capitaux
			double Total_market_cap = monnaies.map(m -> m.getMarket_cap_usd()).reduce((a,b)->a+b);
			// calcul des différents indices
			JavaPairRDD<String, CryptoStats > stats =
					monnaies.mapToPair(
							new PairFunction<Monnaie, String, CryptoStats >() {
							@Override
						    public Tuple2<String,CryptoStats> call(Monnaie item) throws Exception {
								 return new Tuple2<String,CryptoStats>(
									 item.getName(),
									 new CryptoStats(
										item.getName(),
										Math.log((double) 1+item.getPercent_change_1h()/100.0),
										Math.log((double) 1+item.getPercent_change_24h()/100.0),
										Math.log((double) 1+item.getPercent_change_7d()/100.0),
										365*Math.log((double) 1+item.getPercent_change_24h()/100.0),
										100.0*item.getMarket_cap_usd()/Total_market_cap
										
											 )
									 
										 );
								   }
							});
			return stats ; 
		}
		//la fonction compute calcule sur une rdd de type CryptoStat différents indices comme moyenne , écart type...
		public static GlobalStat compute(String name , JavaRDD<CryptoStats> all_stats){
			/** compute part of market **/
			String evolution, volatilite;
			double N = all_stats.count();
			double seuil = 1/(N*N); // c'est juste pour tester
			double moyenne = all_stats.map(e-> e.getRsi_1h()/N).reduce((a,b)->a+b);
			double ecart_type = all_stats.map(e-> (1.0/N)*Math.pow(e.getRsi_1h() - moyenne,2)).reduce((a,b)->a+b);
			if(moyenne > 0 ) evolution = "Croissant";
			else evolution = "Decroissant";
			if(ecart_type> seuil) volatilite = "Instable";
			else volatilite = "Plutot Stable";
			return new GlobalStat(name, moyenne, ecart_type,evolution,volatilite);

	}
	@Override
		public String toString() {
			return "RddStatistique []";
		}
	/** Main 
	passage des arguments suiavnts : 
	  ---
	***/
	public static void main(String[] args) {
		//Ouverture de la connexion avec la base de données Cassandra
		System.out.println("--Starting Connection...");
		CassandraConnector connector = new CassandraConnector();
		connector.connect(args[2], null);
		//Récupération de l'objet session de connexion.
		Session session = connector.getSession();
		System.out.println("--Connection : Done");
		//Creation d'un objet de type KeyspaceRepository (pour la gestion des keyspaces)
		KeyspaceRepository sr = new KeyspaceRepository(session);
		//Creation du keyspace crypto
		sr.createKeyspace("crypto", "SimpleStrategy", 1);
		// Utilisation du Keyspace crée
		sr.useKeyspace("crypto");
		// Création d'un objet de type CryptoStatsRepository pour la gestion des tables
		CryptoStatsRepository br = new CryptoStatsRepository(session);
		// Création de la table Stats : qui va contenir les indices calculés
		br.createTableStats();
		//Création de la table GlobalStats
		br.createTableGloablStats();
		System.out.println("Create Table Done");

		// Configuration de spark context
		SparkConf conf = new SparkConf()
		        .setAppName("kafka-sandbox")
		        .setMaster("local[*]")
		        .set("spark.driver.memory", "501859200");
		// Création de l'objet SparkContext 
		JavaSparkContext sc = new JavaSparkContext(conf);
		// Création de l'objet JavaStreamingContext : Duration spécifie le temps entre chaque deux requetes de lecture en streaming
		JavaStreamingContext ssc = new JavaStreamingContext(sc, new Duration(5000));
		System.out.println("Configure Spark Done ");
        
		Map<String, Object> kafkaParams = new HashMap<>();
		kafkaParams.put("bootstrap.servers", args[0]);
		kafkaParams.put("key.deserializer", StringDeserializer.class);
		kafkaParams.put("value.deserializer", StringDeserializer.class);
		kafkaParams.put("group.id", "use_a_separate_group_id_for_each_stream");
		kafkaParams.put("auto.offset.reset", "latest");
		kafkaParams.put("enable.auto.commit", false);
		
		Collection<String> topics = Arrays.asList(args[1]);

		JavaInputDStream<ConsumerRecord<String, String>> stream =
		  KafkaUtils.createDirectStream(
				  ssc,
		    LocationStrategies.PreferConsistent(),
		    ConsumerStrategies.<String, String>Subscribe(topics, kafkaParams)
		  );
		System.out.println("Subscribe to topic done ");

		stream.foreachRDD(rdd -> { 
		     rdd.foreach(record -> {
		     // Mapping des fichiers json en objets de type Monnaie
		     Monnaie[] myMonnai = new ObjectMapper().readValue(record.value(), Monnaie[].class);
		     List<Monnaie>  liste_money = Arrays.asList(myMonnai);
		     Ref = liste_money; 
		    });
		// On vérifie que la lecture est bien faite.
		if(Ref.size()!=0) {
		    System.out.println("Start computing...");
		    // Parallelisation de la liste récupérée 
   		    JavaRDD<Monnaie> monnaies = sc.parallelize(Ref);
   		    // Calculs des indices statistiques 
   		    System.out.println("---Computing statistics...");
   		    JavaPairRDD<String, CryptoStats >	stats = compute_stat(monnaies);
   		    List< Tuple2<String,CryptoStats> > collect_stats = stats.collect();
   		    System.out.println("---Computing statistics : Done");
		    /**
		    Édition de la table stats
		     */

   		    //Spressions des valeures anciennes
   		    br.deleteAllStats();
   		    System.out.println("---Inserting stats into DataBase...");
		    /** 
		    Insertion des nouvelles valeures
		    */
   		    List<String> crypto_names = new ArrayList<String>(); 
   		     for(Tuple2<String,CryptoStats > t : collect_stats){
   				crypto_names.add(t._1.replace(" ","_")); // récupération de la liste des noms des cryptommonnaies traitées (utilisé dans le 2ème traitement)
   				br.insertStat(t._2); // Ajout dans la table stats (contients toutes les cryptos)
   				br.AddStatRecord(t._2); // Insertion dans la table qui a le meme nom que la cryptomonnaie : l'enrigestremment contient en plus un champ timeInsert qui précise le temps d'insertion	
   			}
   			System.out.println("---Inserting stats into DataBase Done");
   			//Début de la deuxième partie de traitement
   			int n_last_hours = 24; // calcul sur la période des dernière 24heures
			// Récuperer l'historiques de tous les cryptommonnaies (traités dans la première parite)
   			List< List<CryptoStats> > all = br.GetAllRecords(crypto_names,n_last_hours);
   			List<GlobalStat> globalStats = new ArrayList<GlobalStat>();
   			int index = 0 ;
   			for(List<CryptoStats> L : all) {
   				if(!L.isEmpty()) {
   				JavaRDD<CryptoStats> records = sc.parallelize(L);
   				// Récupération de la liste des objets de type GlobalCrypto Calculées à l'aide de la fonction compute
   				globalStats.add(compute(crypto_names.get(index),records));
   				}
   				index++;
   			}
			// Tri de la liste récupérée 
   			 globalStats.sort(new Comparator<GlobalStat>() {

				@Override
				public int compare(GlobalStat o1, GlobalStat o2) {
					// TODO Auto-generated method stub
					if(o1.getMoyenne()<=o2.getMoyenne()) {
						return 1 ; 
					}
					return -1 ;
				}
   				
			});
   	//Spressions des valeures anciennes dans la table GlobalStats
    	br.deleteAllGlobalStats();
   			 for(GlobalStat gs : globalStats) {
   				 System.out.println(gs);
				 // Insertion des nouvelles valeures
   				 br.insertGlobalStat(gs);
   			 }
		}});
		
		
		ssc.start();
        try {
			ssc.awaitTermination();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
