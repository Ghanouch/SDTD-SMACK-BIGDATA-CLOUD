package Model;
/**  Cette classe représente le model de nos données (récupérés en streaming) **/
import java.io.Serializable;
	public  class Monnaie implements Serializable {
		private static final long serialVersionUID = 1L;
		private String name;
		private Double  price_usd;
		private Double   market_cap_usd;
		private Double  percent_change_1h;
		private Double percent_change_24h;
		private Double percent_change_7d;
		private String  last_updated;
		// attributes not used 
		private String id;
		private String symbol;
		private String rank;
		private String price_btc;
		private String  available_supply;
		private String  total_supply;
		private String  max_supply;
		private String _24h_volume_usd;
		  
		public Monnaie() {
			super();
			// TODO Auto-generated constructor stub
		}
		public Monnaie(String name, Double price_usd, Double market_cap_usd, Double percent_change_1h,
				Double percent_change_24h, Double percent_change_7d, String last_updated) {
			super();
			this.name = name;
			this.price_usd = price_usd;
			this.market_cap_usd = market_cap_usd;
			this.percent_change_1h = percent_change_1h;
			this.percent_change_24h = percent_change_24h;
			this.percent_change_7d = percent_change_7d;
			this.last_updated = last_updated;
		}
		
		
		public Monnaie(String name, Double price_usd, Double market_cap_usd, Double percent_change_1h,
				Double percent_change_24h, Double percent_change_7d, String last_updated, String id, String symbol,
				String rank, String price_btc, String available_supply, String total_supply, String max_supply) {
			super();
			this.name = name;
			this.price_usd = price_usd;
			this.market_cap_usd = market_cap_usd;
			this.percent_change_1h = percent_change_1h;
			this.percent_change_24h = percent_change_24h;
			this.percent_change_7d = percent_change_7d;
			this.last_updated = last_updated;
			this.id = id;
			this.symbol = symbol;
			this.rank = rank;
			this.price_btc = price_btc;
			this.available_supply = available_supply;
			this.total_supply = total_supply;
			this.max_supply = max_supply;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Double getPrice_usd() {
			return price_usd;
		}
		public void setPrice_usd(Double price_usd) {
			this.price_usd = price_usd;
		}
		public Double getMarket_cap_usd() {
			return market_cap_usd;
		}
		public void setMarket_cap_usd(Double market_cap_usd) {
			this.market_cap_usd = market_cap_usd;
		}
		public Double getPercent_change_1h() {
			return percent_change_1h;
		}
		public void setPercent_change_1h(Double percent_change_1h) {
			this.percent_change_1h = percent_change_1h;
		}
		public Double getPercent_change_24h() {
			return percent_change_24h;
		}
		public void setPercent_change_24h(Double percent_change_24h) {
			this.percent_change_24h = percent_change_24h;
		}
		public Double getPercent_change_7d() {
			return percent_change_7d;
		}
		public void setPercent_change_7d(Double percent_change_7d) {
			this.percent_change_7d = percent_change_7d;
		}
		public String getLast_updated() {
			return last_updated;
		}
		public void setLast_updated(String last_updated) {
			this.last_updated = last_updated;
		}
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getSymbol() {
			return symbol;
		}
		public void setSymbol(String symbol) {
			this.symbol = symbol;
		}
		public String getRank() {
			return rank;
		}
		public String get24h_volume_usd() {
			return _24h_volume_usd;
		}
		public void set24h_volume_usd(String _24h_volume_usd) {
			this._24h_volume_usd = _24h_volume_usd;
		}
		public void setRank(String rank) {
			this.rank = rank;
		}
		public String getPrice_btc() {
			return price_btc;
		}
		public void setPrice_btc(String price_btc) {
			this.price_btc = price_btc;
		}
		public String getAvailable_supply() {
			return available_supply;
		}
		public void setAvailable_supply(String available_supply) {
			this.available_supply = available_supply;
		}
		public String getTotal_supply() {
			return total_supply;
		}
		public void setTotal_supply(String total_supply) {
			this.total_supply = total_supply;
		}
		public String getMax_supply() {
			return max_supply;
		}
		public void setMax_supply(String max_supply) {
			this.max_supply = max_supply;
		}
		@Override
		public String toString() {
			return "Monnaie [name=" + name + ", price_usd=" + price_usd + ", market_cap_usd=" + market_cap_usd
					+ ", percent_change_1h=" + percent_change_1h + ", percent_change_24h=" + percent_change_24h
					+ ", percent_change_7d=" + percent_change_7d + ", last_updated=" + last_updated + "]";
		}
		
		
		
}
