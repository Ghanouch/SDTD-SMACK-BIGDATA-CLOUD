package Model;
import java.io.Serializable;
import java.sql.Timestamp;
/**classe n'est pas utilisée dans le projet, elle a été remplacée par la classe CryptoStat**/
public class Rsi implements Serializable{
		private static final long serialVersionUID = 1L;
		private Timestamp time ; 
		private double rsi_1h;
		private double rsi_24h;
		private double rsi_7d;
		public Rsi() {
			super();
			// TODO Auto-generated constructor stub
		}
		public Rsi(Timestamp time, double rsi_1h, double rsi_24h, double rsi_7d) {
			super();
			this.time = time;
			this.rsi_1h = rsi_1h;
			this.rsi_24h = rsi_24h;
			this.rsi_7d = rsi_7d;
		}
		public Timestamp getTime() {
			return time;
		}
		public void setTime(Timestamp time) {
			this.time = time;
		}
		public double getRsi_1h() {
			return rsi_1h;
		}
		public void setRsi_1h(double rsi_1h) {
			this.rsi_1h = rsi_1h;
		}
		public double getRsi_24h() {
			return rsi_24h;
		}
		public void setRsi_24h(double rsi_24h) {
			this.rsi_24h = rsi_24h;
		}
		public double getRsi_7d() {
			return rsi_7d;
		}
		public void setRsi_7d(double rsi_7d) {
			this.rsi_7d = rsi_7d;
		}
		@Override
		public String toString() {
			return "Rsi [time=" + time + ", rsi_1h=" + rsi_1h + ", rsi_24h=" + rsi_24h + ", rsi_7d=" + rsi_7d + "]";
		}
		
		
		
	}
