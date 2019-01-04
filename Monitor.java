package homework11;

public class Monitor extends Thread {
	/*Overview:维护了一个请求读出类，实现了实时关闭请求窗口、命令请求队列类分配请求的行为
	*表示对象:null
	*抽象函数:null
	*不变式:null
	*/
	private final RequestList requestlist;
	
	public Monitor (RequestList requestlist) {
		this.requestlist=requestlist;
	}
	
	public void run () {
		long time = 0L;
		while (true) {
			if (requestlist.getsize()==0) 
				continue;
			time = System.currentTimeMillis()/100*100;
			if (time==requestlist.getreqlist().get(0).gettime()+3000) {
				requestlist.removereq();
			}
		}
		
	}
}




