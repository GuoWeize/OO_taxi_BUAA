package homework11;

public class TaxiSystem {
	public static RequestList requestlist = new RequestList();
	public static Taxi[] taxi;
	
	
	public Taxi[] init_taxi (int type[]) {
		/* @REQUIRES:type.size==100; \all int a.isIn(type),a==0||a==1;
		 * @MODIFIES:taxi;
		 * @EFFECTS:\result==a new taxi array;
		 */
		for (int i=0;i<type.length;i++) {
			if (type[i]==0) taxi[i]=new Taxi(i, requestlist);
			if (type[i]==1) taxi[i]=new TraceableTaxi(i, requestlist);
		}
		return taxi;
	}
	
	
	public static void main(String[] args) {
		Map.readmap("map.txt");
		Map.readlight("light.txt");
		
		Monitor monitor = new Monitor(requestlist);
		monitor.start();
		
		Lightcontroler controler = new Lightcontroler();
		controler.start();
		
		Input input = new Input(requestlist);
		input.start();
		
		MapRefresh maprefresh = new MapRefresh();
		maprefresh.start();
		
		for (int i=0;i<100;i++) {
			taxi[i] = new Taxi(i,requestlist);
			taxi[i].start();
		}
		
		
	}

}
