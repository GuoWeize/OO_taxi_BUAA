package homework11;
import java.util.*;

public class Request {
	/*Overview:维护了一个请求类，实现了判断是否相同、添加申请的出租车、获取请求范围、获取时间的行为
	*表示对象:Point src, Point dst, long time, Vector<Integer> taxinum
	*抽象函数:AF(c)=(src,dst,time,taxisnumber), where src==c.src, dst==c.dst, time==c.time, taxisnumber=c.taxinum
	*不变式:src!=dst, time>=0, time%100=0
	*/
	private Point src;
	private Point dst;
	private long time;
	private Vector<Integer> taxinum;
	
	public Request (Point src, Point dst) {
		this.src=src;
		this.dst=dst;
		time = System.currentTimeMillis()/100*100;
		taxinum = new Vector<Integer>();
	}
	
	
	protected boolean repOK () {
		if (Point.issame(src, dst))
			return false;
		if (time<0)
			return false;
		if (time%100!=0)
			return false;
		return true;
	}
	
	
	private boolean hasapplied (int ID) {
		/* @REQUIRES:(0<=ID<=99);
		 * @MODIFIES: None;
		 * @EFFECTS:(contain(ID).taxinum)==>\result==true;
		 * 			(!contain(ID).taxinum)==>\result==false;
		 */
		for (int i=0;i<this.taxinum.size();i++) {
			if (this.taxinum.get(i)==ID)
				return true;
		}
		return false;
	}
	
	
	public static boolean issame (Request a, Request b) {
		/* @REQUIRES: (a,b!=null);
		 * @MODIFIES: None;
		 * @EFFECTS:(a is the same as b)==>\result==true;
		 * 			(a is different from b)==>\result==false;
		 */
		if ( (Point.issame(a.src,b.src))&&(Point.issame(a.dst,b.dst))&&(a.time==b.time) )
			return true;
		return false;
	}
	
	
	public void addtaxinum (int ID) {
		/* @REQUIRES:(0<=ID<=99);
		 * @MODIFIES: taxinum;
		 * @EFFECTS:(contain(ID).taxinum)==>do nothing;
		 * 			(src==dst)==>do nothing;
		 * 			(contain(ID).taxinum)==>add ID to the taxinum;
		 */
		if (this.hasapplied(ID)) {}
		else {
			Integer a = ID;
			this.taxinum.addElement(a);
		}
	}
	
	public long gettime () {
		//@EFFECTS:\result==time;
		return this.time;
	}
	
	public Point getsrc () {
		//@EFFECTS:\result==src;
		return this.src;
	}
	
	public Point getdst () {
		//@EFFECTS:\result==dst;
		return this.dst;
	}
	
	public int getxmin () {
		//@EFFECTS:\result==边界纵坐标的最小值;
		if (src.x<2) 
			return 0;
		else return src.x-2;
	}
	
	public int getxmax () {
		//@EFFECTS:\result==边界纵坐标的最大值;
		if (src.x>77) 
			return 79;
		else return src.x+2;
	}
	
	public int getymin () {
		//@EFFECTS:\result==边界横坐标的最小值;
		if (src.y<2) 
			return 0;
		else return src.y-2;
	}
	
	public int getymax () {
		//@EFFECTS:\result==边界横坐标的最大值;
		if (src.y>77) 
			return 79;
		else return src.y+2;
	}
	
	public int gettaxinum (int index) {
		//@EFFECTS:\result==taxinum;
		return this.taxinum.get(index);
	}
	
	public int gettaxinumsize () {
		//@EFFECTS:\result==taxinum.length;
		return this.taxinum.size();
	}
	
}
