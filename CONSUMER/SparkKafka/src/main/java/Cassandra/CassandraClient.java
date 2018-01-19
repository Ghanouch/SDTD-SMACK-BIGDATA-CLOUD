package Cassandra;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.datastax.driver.core.Session;
import com.datastax.driver.core.utils.UUIDs;

import repository.CryptoStatsRepository;
import repository.KeyspaceRepository;
/**
Ce fichier est juste pour le test et ne fait pas partie du projet.
**/
public class CassandraClient {

    public static void main(String args[]) {
        CassandraConnector connector = new CassandraConnector();
        connector.connect("ec2-34-207-86-210.compute-1.amazonaws.com", null);
        Session session = connector.getSession();

        KeyspaceRepository sr = new KeyspaceRepository(session);
        sr.createKeyspace("crypto", "SimpleStrategy", 1);
        sr.useKeyspace("crypto");

        CryptoStatsRepository br = new CryptoStatsRepository(session);
      //  br.deleteTable("Stats");
        br.createTableStats();
        //br.alterTablebooks("publisher", "text");

        //br.createTableStats();
        CryptoStats stat = new CryptoStats();
       // CryptoStats stat = new Coin(UUIDs.timeBased(), "Euth", "1300000$");
        br.insertStat(stat);
        List<CryptoStats> list_cryptoStats = br.selectAll();
        System.out.println("NUmber of recoreds : " + list_cryptoStats.size());
        br.selectAll().forEach(o -> System.out.println("stats: " + o.toString()));
       // br.selectAllBookByTitle().forEach(o -> LOG.info("Title in booksByTitle: " + o.getName()));

//       br.deletebookByTitle("Effective Java");
 //       br.deleteTable("books");
  //     br.deleteTable("booksByTitle");

    //    sr.deleteKeyspace("library");

        connector.close();
    }
}
