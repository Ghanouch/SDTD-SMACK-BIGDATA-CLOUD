package Cassandra;
import java.io.Serializable;

public class CryptoStats implements Serializable {

	/**
	 * Cette classe reporésente le model logique des enregistrement de la table Stats
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private Double rsi_1h;
	private Double rsi_24h;
	private Double rsi_7d;
	private Double rsi_annualise;
	private Double tauxMarche;
	public CryptoStats() {
		super();
	}
	public CryptoStats(String name, Double rsi_1h, Double rsi_24h, Double rsi_7d, Double rsi_annualise,
			Double tauxMarche) {
		super();
		this.name = name;
		this.rsi_1h = rsi_1h;
		this.rsi_24h = rsi_24h;
		this.rsi_7d = rsi_7d;
		this.rsi_annualise = rsi_annualise;
		this.tauxMarche = tauxMarche;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getRsi_1h() {
		return rsi_1h;
	}
	public void setRsi_1h(Double rsi_1h) {
		this.rsi_1h = rsi_1h;
	}
	public Double getRsi_24h() {
		return rsi_24h;
	}
	public void setRsi_24h(Double rsi_24h) {
		this.rsi_24h = rsi_24h;
	}
	public Double getRsi_7d() {
		return rsi_7d;
	}
	public void setRsi_7d(Double rsi_7d) {
		this.rsi_7d = rsi_7d;
	}
	public Double getRsi_annualise() {
		return rsi_annualise;
	}
	public void setRsi_annualise(Double rsi_annualise) {
		this.rsi_annualise = rsi_annualise;
	}
	public Double getTauxMarche() {
		return tauxMarche;
	}
	public void setTauxMarche(Double tauxMarche) {
		this.tauxMarche = tauxMarche;
	}
	@Override
	public String toString() {
		return "CryptoStats [name=" + name + ", rsi_1h=" + rsi_1h + ", rsi_24h=" + rsi_24h + ", rsi_7d=" + rsi_7d
				+ ", rsi_annualise=" + rsi_annualise + ", tauxMarche=" + tauxMarche + "]";
	}
	
	
	
	
	
	
}
