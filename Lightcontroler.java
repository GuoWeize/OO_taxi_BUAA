package homework11;

public class Lightcontroler extends Thread {
	/*Overview:维护了一个红绿灯控制类，实现了红绿灯变换
	*表示对象:null
	*抽象函数:null
	*不变式:null
	*/
	
	public void run () {
		long time = 0L;
		
		while (true) {
			time = System.currentTimeMillis();
			for (int i=0;i<80;i++) {
				for (int j=0;j<80;j++) {
					if (time-Map.p[i][j].lighttime<Map.p[i][j].lightround) {
						Map.p[i][j].timetowait = (int) (Map.p[i][j].lighttime+Map.p[i][j].lightround-time);
					}
					if (time-Map.p[i][j].lighttime>Map.p[i][j].lightround) {
						Map.p[i][j].NSgreen=!Map.p[i][j].NSgreen;
						Map.p[i][j].lighttime=time;
					}
				}
			}
		}
		
		
	}
}
