package homework11;
import java.util.*;
import java.io.FileWriter;
import java.io.IOException;

public class Taxi extends Thread implements Car {
	/*Overview:维护了一个出租车类，实现了出租车漫游、抢单、接单的行为，同时实时更新出租车的位置、信用和状态
	*表示对象:int ID,Point location,int credit,Carstate state,Request doingreq
	*抽象函数:AF(c)=(location,credit,state), where location==c.location, credit==c.credit, state==c.state
	*不变式:1<=c.ID<=100; location isIN(Map.p); credit>=0; doingreq!=null if state==ordering||state==serving
	*/
	protected final int ID;                     //编号
	protected Point location;                   //位置
	protected int credit=0;                     //信用
	protected Carstate state=Carstate.waiting;  //状态
	protected Request doingreq;                 //抢到的请求
	protected int distance=0;                   //等待状态行驶距离
	protected int cardirection=0;				  //当前车头朝向
	
	protected final RequestList requestlist;
	
	public Taxi (int ID, RequestList requestlist) {
		/* @REQUIRES:(0<=ID<=99);
		 * @MODIFIES: Taxi;
		 * @EFFECTS:\result==a new taxi thread
		 */
		this.ID = ID;
		Random random = new Random();
		int x = random.nextInt(80);
		int y = random.nextInt(80);
		//int x=4,y=4;
		this.location=Map.p[x][y];
		this.requestlist=requestlist;
		this.doingreq=null;
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
			location.d1++;
			location=Map.p[location.x][location.y+1];
			location.d3++;
			break;
		case 2:
			location.d2++;
			location=Map.p[location.x-1][location.y];
			location.d4++;
			break;
		case 3:
			location.d3++;
			location=Map.p[location.x][location.y-1];
			location.d1++;
			break;
		case 4:
			location.d4++;
			location=Map.p[location.x+1][location.y];
			location.d2++;
			break;
		default:
			System.out.print("移动参数出错！");
			System.exit(-2);
		}
		this.cardirection=direction;
		//System.out.println(this.toString());
	}
	
	
	protected boolean light (int direction, int cardirection, Point location) {
		/* @REQUIRES:(1<=direction<=4);(1<=cardirection<=4);
		 * @MODIFIES:null;
		 * @EFFECTS:(if the car can go)==>(\return==true);
		 * 			(if the car can't go)==>(\return==false);
		 */			
		if (location.lightround==0)
			return true;
		
		switch (cardirection) {
		case 1:                
			if ( (direction==3)||(direction==4) )
				return true;
			if ( (direction==2)&&(location.NSgreen==true) )
				return true;
			if ( (direction==1)&&(location.NSgreen==false) )
				return true;
			return false;
			
		case 2:
			if ( (direction==1)||(direction==4) )
				return true;
			if ( (direction==2)&&(location.NSgreen==true) )
				return true;
			if ( (direction==3)&&(location.NSgreen==false) )
				return true;
			return false;
			
		case 3:
			if ( (direction==1)||(direction==4) )
				return true;
			if ( (direction==2)&&(location.NSgreen==true) )
				return true;
			if ( (direction==3)&&(location.NSgreen==false) )
				return true;
			return false;
			
		case 4:
			if ( (direction==3)||(direction==2) )
				return true;
			if ( (direction==4)&&(location.NSgreen==true) )
				return true;
			if ( (direction==1)&&(location.NSgreen==false) )
				return true;
			return false;
			
		default:
			System.out.print("移动参数出错！");
			return false;
		}
	}
	
	
	public synchronized void apply() {
		/* @REQUIRES: (state==waiting);
		 * @MODIFIES: credit;requestlist;
		 * @EFFECTS:(this car is in a request's area)==>add this car to the taxinum of this request && credit==\old(credit)+1
		 * 			(this car isn't in a request's area)==>do nothing
		 */
		for (int i=0;i<requestlist.getreqlist().size();i++) {
			Request r = requestlist.getreqlist().get(i);
			if ( (r.getxmin()<=this.location.x)&&(this.location.x<=r.getxmax())
					&&(r.getymin()<=this.location.y)&&(this.location.y<=r.getymax()) ) {
				requestlist.getreqlist().get(i).addtaxinum(ID);
			}
		}
	}
	
	
	public void wander() {
		/* @REQUIRES: (doingreq==null);
		 * @MODIFIES: location;distance;
		 * @EFFECTS:(有一条流量最小的边)==>选择这条边走 && distance=\old(distance)+1
		 * 			(有多条流量最小的边)==>随机选择一条流量最小的边 && distance=\old(distance)+1
		 */
		Random random = new Random();
		int n=0;
		int m=0;
		int temp=0;
		int flow=100;
		
		if ( (location.d1>=0)&&(location.d1<flow) ) 
			flow=location.d1;
		if ( (location.d2>=0)&&(location.d2<flow) ) 
			flow=location.d2;
		if ( (location.d3>=0)&&(location.d3<flow) ) 
			flow=location.d3;
		if ( (location.d4>=0)&&(location.d4<flow) ) 
			flow=location.d4;
		
		if (location.d1==flow) n++;
		if (location.d2==flow) n++;
		if (location.d3==flow) n++;
		if (location.d4==flow) n++;
		temp=random.nextInt(n);
		if (this.location.d1==flow) {
			if (temp==0) m=1;
			temp--;
		}
		if (this.location.d2==flow) {
			if (temp==0) m=2;
			temp--;
		}
		if (this.location.d3==flow) {
			if (temp==0) m=3;
			temp--;
		}
		if (this.location.d4==flow) {
			if (temp==0) m=4;
			temp--;
		}
		this.move(m);
		this.apply();
		this.distance++;
		if (this.distance==100) {
			this.rest();
			this.distance=0;
		}
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
		
		if (state==Carstate.waiting) {
			this.state=Carstate.ordering;
			
			try {
				writer.write("第"+this.ID+"号出租车接单\r\n");
			} catch (IOException e) {}
			
			//System.out.println("第"+this.ID+"号出租车接单\r\n");
		}

		if ((!Point.issame(location, doingreq.getsrc())) && (state == Carstate.ordering)) {
			int[] way = Map.findway(location, doingreq.getsrc(),0);
			for (int i=0;i<way.length;i++) {
				this.move(way[i]);

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
			int[] way = Map.findway(location, doingreq.getdst(),0);
			for (int i=0;i<way.length;i++) {
				this.move(way[i]);
				
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
	}
	
	
	public void rest() {
		/* @REQUIRES: None;
		 * @MODIFIES: state;
		 * @EFFECTS:state==stopping for 1s then state==waiting
		 */
		this.state=Carstate.stopping;
		Map.waitforatime(1000);
		this.state=Carstate.waiting;
	}
	
	
	public void addcredit () {
		/* @REQUIRES: None;
		 * @MODIFIES: credit;
		 * @EFFECTS:credit==\old(credit)+1;
		 */
		this.credit++;
	}
	
	
	public void setdoingreq (Request r) {
		/* @REQUIRES: (doingreq=null);
		 * @MODIFIES: doingreq;
		 * @EFFECTS:change the doingreq of this taxi;
		 */
		this.doingreq=r;
	}
	
	public Point getlocation() {
		//@EFFECTS:\result==location;
		return this.location;
	}

	public Carstate getstate() {
		//@EFFECTS:\result==state;
		return this.state;
	}
	
	public int getcredit () {
		//@EFFECTS:\result==credit;
		return this.credit;
	}
	
	
	public String toString () {
		//@EFFECTS:\return==当前的状态(所有内容自动对齐)
		String idnum,state;
		if (this.ID<10)
			idnum="0"+this.ID;
		else idnum=""+this.ID;
		if ( (this.state==Carstate.serving)||(this.state==Carstate.waiting) )
			state=this.state+" ";
		else state=""+this.state;
		
		return "Taxi ID:"+idnum+"  状态:"+state+"  位置:"+this.location.toString()+"  信用:"+this.credit;
	}
	
	
	public void run() {
		/* @REQUIRES: None;
		 * @MODIFIES: \all except ID;
		 * @EFFECTS:run a taxi!
		 */
		while (true) {
			if (this.doingreq==null) 
				this.wander();
			else {
				this.serve();
			}
		}
	}
	
}




