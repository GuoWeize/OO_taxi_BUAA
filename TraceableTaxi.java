package homework11;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class TraceableTaxi extends Taxi {
	/*Overview:维护了一个可追踪出租车类，实现了出租车漫游、抢单、接单的行为，同时实时更新出租车的位置、信用和状态，同时可以记录每次服务
	*表示对象:int ID,Point location,int credit,Carstate state,Request doingreq,Vector<ServeInfo> information
	*抽象函数:AF(c)=(location,credit,state,information), where location==c.location, credit==c.credit, state==c.state, information=c.information
	*不变式:1<=c.ID<=100; location isIN(Map.p); credit>=0; doingreq!=null if state==ordering||state==serving
	*/
	private Vector<ServeInfo> information;
	
	public TraceableTaxi(int ID, RequestList requestlist) {
		/* @REQUIRES:(0<=ID<=99);
		 * @MODIFIES: Taxi;
		 * @EFFECTS:\result==a new taxi thread
		 */
		super(ID, requestlist);
	}
	
	
	protected boolean repOK () {
		if ( (ID<1)||(ID>100) )
			return false;
		if (credit<0)
			return false;
		if ( (state==Carstate.ordering)||(state==Carstate.serving) ) {
			if (doingreq==null)
				return false;
		}
		return true;
	}
	
	
	protected void move (int direction) {
		/* @REQUIRES:(1<=direction<=4);
		 * @MODIFIES: location,Map.p;
		 * @EFFECTS:向给定方向的移动一次(包括红绿灯等待)
		 */
		boolean cango = this.light(direction, cardirection, location);
		if (cango) {}
		else {
			Map.waitforatime(location.timetowait);
		}
		Map.waitforatime(200);
		switch (direction) {
		case 1:                
			if (location.d1>=0) location.d1++;
			location=Map.p[location.x][location.y+1];
			if (location.d3>=0) location.d3++;
			break;
		case 2:
			if (location.d2>=0) location.d2++;
			location=Map.p[location.x-1][location.y];
			if (location.d4>=0) location.d4++;
			break;
		case 3:
			if (location.d3>=0) location.d3++;
			location=Map.p[location.x][location.y-1];
			if (location.d1>=0) location.d1++;
			break;
		case 4:
			if (location.d4>=0) location.d4++;
			location=Map.p[location.x+1][location.y];
			if (location.d2>=0) location.d2++;
			break;
		default:
			System.out.print("移动参数出错！");
			System.exit(-2);
		}
		this.cardirection=direction;
		//System.out.println(this.toString());
	}
	
	
	public void serve() {
		/* @REQUIRES:(doingreq!=null);
		 * @MODIFIES:state;location;credit;distance;doingreq;
		 * @EFFECTS:location==\old(doingreq).getdst() && credit==\old(credit)+3 && distance==0 && doingreq==null
		 */
		FileWriter writer = null;
		try {
			writer = new FileWriter("out.txt", true);
		} catch (IOException e) {}
		
		ServeInfo info = new ServeInfo(doingreq);
		info.taxiway.add(location);
		
		if (state==Carstate.waiting) {
			this.state=Carstate.ordering;
			
			try {
				writer.write("第"+this.ID+"号出租车接单\r\n");
			} catch (IOException e) {}
			
			//System.out.println("第"+this.ID+"号出租车接单\r\n");
		}
		
		if ( ( !(Point.issame(location, doingreq.getsrc())) ) && (state==Carstate.ordering) ){
			int[] way = Map.findway(location, doingreq.getsrc(),-1);
			for (int i=0;i<way.length;i++) {
				this.move(way[i]);
				info.taxiway.add(location);
				try {
					writer.write(this.toString());
				} catch (IOException e) {}
				
				//System.out.println(this.toString());
			}
		}
		
		if (Point.issame(location, doingreq.getsrc())) {
			System.out.println(this.toString());
			
			try {
				writer.write("第"+this.ID+"号接上乘客");
			} catch (IOException e) {}
			
			//System.out.println("第"+this.ID+"号接上乘客");
			this.rest();
			this.state=Carstate.serving;
		}
		
		if ( ( !(Point.issame(location, doingreq.getdst())) ) && (state==Carstate.serving) ) {
			int[] way = Map.findway(location, doingreq.getdst(),-1);
			for (int i=0;i<way.length;i++) {
				this.move(way[i]);
				info.taxiway.add(location);
				try {
					writer.write(this.toString());
				} catch (IOException e) {}
				
				//System.out.println(this.toString());
			}
		}
		
		if (Point.issame(location, doingreq.getdst())) {
			
			try {
				writer.write(this.toString());
				writer.write("第"+this.ID+"号放下乘客");
			} catch (IOException e) {}
			
			//System.out.println("第"+this.ID+"号放下乘客");
			this.rest();
			this.credit=this.credit+3;
			this.doingreq=null;
			this.distance=0;
		}
		
		information.add(info);
	}
	
	
	public Iterator createIterator () {
		/* @REQUIRES:null;
		 * @MODIFIES:TraceableTaxi;
		 * @EFFECTS:\result==a iterator
		 */
		return new TaxiIterator(this.information);
	}
}
