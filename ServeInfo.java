package homework11;
import java.util.ArrayList;

public class ServeInfo {
	/*Overview:维护了一个服务情况类
	*表示对象:request, taxiway
	*抽象函数:AF(c)=(请求时刻,请求位置,目的地位置,服务时的路径),where 请求时刻==request.time,请求位置=request.src,目的地位置=request.dst,服务时的路径=taxiway
	*不变式:request!=null, taxiway.size!=0, taxiway.include(request.src), taxiway.include(request.dst)
	*/
	public Request request;
	public ArrayList<Point> taxiway;
	
	protected boolean repOK () {
		if (request!=null) return true;
		if ( (taxiway.contains(request.getsrc())) && (taxiway.contains(request.getdst())) ) 
			return true;
		return false;
	}
	
	
	public ServeInfo (Request request) {
		/* @REQUIRES:request!=null;
		 * @MODIFIES:ServeInfo;
		 * @EFFECTS:\result==a new serve information
		 */
		this.request=request;
		this.taxiway=new ArrayList<Point>();
	}
	
	
	public String toString () {
		//@EFFECTS:\return=="Request:[time,src,dst]\n" + (\all point.isIn(taxiway)),point.toString+" "
		int size = taxiway.size();
		String s = "Request:["+request.gettime()+","+request.getsrc()+","+request.getdst()+"]\n";
		for (int i=0;i<size;i++) {
			s=s+taxiway.get(i).toString()+" ";
		}
		return s;
	}
	
}
