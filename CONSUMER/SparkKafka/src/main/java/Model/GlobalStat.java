package Model;
/** Cette classe représente le model logique pour encapsuler les variables calculées à partir de l'historqie des données
de chaque cryptomonnaie , à savoir : moyenne , ecart_type , sens evolution et volatilité ***/
public class GlobalStat{
	String name ; 
	double moyenne ; 
	double ecart_type;
	String evolution;
	String volatilite;
	
	
	public GlobalStat(String name, double moyenne, double ecart_type, String evolution, String volatilite) {
		super();
		this.name = name;
		this.moyenne = moyenne;
		this.ecart_type = ecart_type;
		this.evolution = evolution;
		this.volatilite = volatilite;
	}
	public String getEvolution() {
		return evolution;
	}
	public void setEvolution(String evolution) {
		this.evolution = evolution;
	}
	public String getVolatilite() {
		return volatilite;
	}
	public void setVolatilite(String volatilite) {
		this.volatilite = volatilite;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getMoyenne() {
		return moyenne;
	}
	public void setMoyenne(double moyenne) {
		this.moyenne = moyenne;
	}
	public double getEcart_type() {
		return ecart_type;
	}
	public void setEcart_type(double ecart_type) {
		this.ecart_type = ecart_type;
	}
	@Override
	public String toString() {
		return "GlobalStat [name=" + name + ", moyenne=" + moyenne + ", ecart_type=" + ecart_type + ", evolution="
				+ evolution + ", volatilite=" + volatilite + "]";
	}
	
	
}
