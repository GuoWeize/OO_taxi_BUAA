package homework11;

public class MapRefresh extends Thread {
	/*Overview:维护了一个地图更改类，实现了实时更改地图的行为
	*表示对象:null
	*抽象函数:null
	*不变式:null
	*/
	
	public void run () {
		while (true) {
			Map.clearflow();
			Map.waitforatime(200);
		}
		
	}
	
	
}
