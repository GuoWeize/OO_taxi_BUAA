package homework11;
import java.util.*;

public class RequestList {
	/*Overview:维护了一个请求队列类，实现了添加请求、分配请求、获取大小的行为
	*表示对象:Vector<Request> reqlist
	*抽象函数:AF(c)=requestlist, where requestlist==c.reqlist
	*不变式:\all 0<=i<j<=reqlist.size,reqlist[i].time<=reqlist[j].time, \all 0<=i,j<=reqlist.size,reqlist[i]!=reqlist[j]
	*/
	private final Vector<Request> reqlist;
	
	public RequestList () {
		reqlist = new Vector<Request>();
	}
	
	
	protected boolean repOK () {
		for (int i=0;i<reqlist.size()-1;i++) {
			if (reqlist.get(i).gettime()>reqlist.get(i+1).gettime())
				return false;
		}
		return true;
	}
	
	
	public void newreq (Request req) {
		/* @REQUIRES:(req!=null);
		 * @MODIFIES: reqlist;
		 * @EFFECTS:(!contain(request).reqlist)==>add a request to the end of the reqlist;
		 * 			(contain(request).reqlist)==>do nothing;
		 */
		int flag=0;
		if (reqlist.size()==0) {
			this.reqlist.addElement(req);
		}
		else {
			for (int i=reqlist.size()-1;i>=0;i--) {
				if (Request.issame(req, reqlist.get(i))) {
					flag=1;
					break;
				}
			}
			if (flag==0)
				this.reqlist.addElement(req);
		}
	}
	
	public void removereq () {
		/* @REQUIRES:(reqlist!=null);
		 * @MODIFIES: reqlist;taxi.doingreq;
		 * @EFFECTS:send the first request of the reqlist to the chosed taxi;
		 */
		Request r = this.reqlist.get(0);
		int maxcredit=-1;
		int chosedtaxi=-1;
		int temp=0;
		for (int i=0;i<r.gettaxinumsize();i++) {
			if (TaxiSystem.taxi[r.gettaxinum(i)].getstate()==Carstate.waiting) {
				TaxiSystem.taxi[r.gettaxinum(i)].addcredit();
				temp = TaxiSystem.taxi[r.gettaxinum(i)].getcredit();
				if (temp>maxcredit) {
					maxcredit=temp;
					chosedtaxi=r.gettaxinum(i);
				}
			}
		}
		
		if (chosedtaxi==-1) {
			System.out.println("对不起，暂时无可用出租车，请再次下单");
			this.reqlist.remove(0);
		}
		else {
			TaxiSystem.taxi[chosedtaxi].setdoingreq(r);
			this.reqlist.remove(0);
		}
	}
	
	
	public Vector<Request> getreqlist () {
		//@EFFECTS:\result==reqlist;
		return this.reqlist;
	}
	
	public int getsize () {
		// @EFFECTS:\result==reqlist.length;
		return this.reqlist.size();
	}
	
}


