package homework11;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Map {
	/*Overview:维护了一个地图类，实现了地图的存储，同时实现了更改、寻路、清除流量的行为
	*表示对象:Point[][] p
	*抽象函数:AF(c)=Point[index], where index=c.i*80+c.j
	*不变式:对于所有p中的点point,满足0<=point.x<=79 && 0<=point.y<=79 
	*/
	public static Point[][] p = new Point[80][80];
	
	protected static boolean repOK () {
		for (int i=0;i<80;i++) {
			for (int j=0;j<80;j++) {
				if ( (Map.p[i][j].x<0)||(Map.p[i][j].x>79)||(Map.p[i][j].y<0)||(Map.p[i][j].y>79) )
					return false;
			}
		}
		return true;
	}
	
	public static void waitforatime (int millisecond) {
		/* @REQUIRES: (millisecond>0);
		 * @MODIFIES: None;
		 * @EFFECTS:wait for a while;
		 */
		try {
			TimeUnit.MILLISECONDS.sleep(millisecond);
		} catch (InterruptedException e) {
			throw new SleepException();
		}
	}
	
	
	private static int howlinked (Point from, Point to) {
		/* @REQUIRES:(from!=to);(from is linked with to);
		 * @MODIFIES: None;
		 * @EFFECTS:normal_behavior
		 * 			(to is on the right of from)==>\result==1;
		 * 			(to is on the top of from)==>\result==2;
		 * 			(to is on the left of from)==>\result==3;
		 * 			(to is on the bottom of from)==>\result==4;
		 * 			exceptional_behavior
		 * 			(from==to)&&(from isn't linked with to)==>\result==0;
		 */
		if ( (from.x==to.x)&&(from.y+1==to.y) ) 
			return 1;
		if ( (from.x-1==to.x)&&(from.y==to.y) ) 
			return 2;
		if ( (from.x==to.x)&&(from.y-1==to.y) ) 
			return 3;
		if ( (from.x+1==to.x)&&(from.y==to.y) ) 
			return 4;
		return 0;
	}
	
	
	public static void readmap (String filename) {
		/* @REQUIRES:a file that contains the map;
		 * @MODIFIES: Map.p;
		 * @EFFECTS:load the map
		 * 			(p can go right)==>p.d1==0;
		 * 			(p can go up)==>p.d2==0;
		 * 			(p can go left)==>p.d3==0;
		 * 			(p can go down)==>p.d4==0;
		 */
		for (int i=0;i<80;i++) {
			for (int j=0;j<80;j++) 
				Map.p[i][j] = new Point();
		}
		
		FileReader reader = null;
		try {
			reader = new FileReader(filename);
		} catch (FileNotFoundException e) {
			System.out.println("指定的地图文件不存在！");
			System.exit(-1);
		}
		BufferedReader br = new BufferedReader(reader);
		String sline = null;
		
		//读入行
		for (int i=0;i<80;i++) {
			try {
				sline = br.readLine();
			} catch (IOException e) {
				System.out.println("无效输入！");
				System.exit(-1);
			}
			if (sline==null) {
				System.out.println("地图不足80行！");
				System.exit(-1);
			}
			
			else {
				sline=sline.replaceAll(" ", "");
				sline=sline.replaceAll("\t", "");
				
				if (sline.length()!=80) {
					System.out.println("地图第"+i+"行不是80列！");
					System.exit(-1);
				}
				
				for (int j=0;j<80;j++) {
					int c=sline.charAt(j)-'0';
					if ( (c>=0)&&(c<=3) ) {
						Map.p[i][j].x=i;        //纵坐标是x
						Map.p[i][j].y=j;        //横坐标是y
						switch (c) {
						case 0:
							break;
						case 1:
							p[i][j].d1=0;
							p[i][j+1].d3=0;
							break;
						case 2:
							p[i][j].d4=0;
							p[i+1][j].d2=0;
							break;
						case 3:
							p[i][j].d1=0;
							p[i][j].d4=0;
							p[i][j+1].d3=0;
							p[i+1][j].d2=0;
							break;
						default :
							break;
						}
					}
					else {
						System.out.println("第"+i+"行第"+j+"列数字必须是0-3！");
						System.exit(-1);
					}
				}
			}
		}
		try {
			sline = br.readLine();
		} catch (IOException e) {
		}
		if (sline!=null) {
			System.out.println("地图超出80行！");
			System.exit(-1);
		}
	}
	
	
	public static void readlight (String filename) {
		/* @REQUIRES:a file that contains the light;
		 * @MODIFIES: Map.p;
		 * @EFFECTS:load and initialize the light
		 * 			(p hasn't a light)==>p.lighttime==0;
		 * 			(NorthSouth is green)==>p.NSgreen==true
		 * 			(EastWest is green)==>p.NSgreen==false;
		 */
		FileReader reader = null;
		try {
			reader = new FileReader(filename);
		} catch (FileNotFoundException e) {
			System.out.println("指定的文件不存在！");
			System.exit(-1);
		}
		BufferedReader br = new BufferedReader(reader);
		String sline = null;
		Random random = new Random();
		
		//读入行
		for (int i=0;i<80;i++) {
			try {
				sline = br.readLine();
			} catch (IOException e) {
				System.out.println("无效输入！");
				System.exit(-1);
			}
			if (sline==null) {
				System.out.println("文件不足80行！");
				System.exit(-1);
			}
			
			else {
				sline=sline.replaceAll(" ", "");
				sline=sline.replaceAll("\t", "");
				
				if (sline.length()!=80) {
					System.out.println("文件第"+i+"行不是80列！");
					System.exit(-1);
				}
				
				for (int j=0;j<80;j++) {
					int c=sline.charAt(j)-'0';
					if (c==0) {}
					if (c==1) {
						Map.p[i][j].lightround = 200+random.nextInt(301);
					}
					else {
						System.out.println("第"+i+"行第"+j+"列数字必须是0或1！");
						System.exit(-1);
					}
				}
			}
		}
		try {
			sline = br.readLine();
		} catch (IOException e) {
		}
		if (sline!=null) {
			System.out.println("文件超出80行！");
			System.exit(-1);
		}
	}
	
	
	public static int[] findway (Point src, Point dst, int type) {
		/* @REQUIRES:(scr!=dst);
		 * @MODIFIES: None;
		 * @EFFECTS:(type==0 \return==普通出租车寻路结果：移动方向数组)
		 * 			(type==-1 \return==特殊出租车寻路结果：移动方向数组)
		 */
		//初始化checked，设为false
		for (int i=0;i<80;i++) {
			for (int j=0;j<80;j++) {
				Map.p[i][j].locked=false;
				Map.p[i][j].checked=false;
				Map.p[i][j].flow=200;
			}
		}
		
		if (Point.issame(src, dst))
			return null;
		
		Vector<Vector<Point>> list = new Vector<Vector<Point>>(); 
		int pathlength = 0;
		
		//第0层 就是src自己
		Map.p[src.x][src.y].checked=true;
		Map.p[src.x][src.y].locked=true;
		Map.p[src.x][src.y].flow=0;
		Vector<Point> round = new Vector<Point>();
		round.add(src);
		list.add(round);
		
		//第1层 是src相邻的点
		round = new Vector<Point>();
		if (src.d1>=type) {
			round.add(Map.p[src.x][src.y+1]);
			Map.p[src.x][src.y+1].checked=true;
			Map.p[src.x][src.y+1].flow=src.d1;
			Map.p[src.x][src.y+1].locked=true;
		}
		if (src.d2>=type) {
			round.add(Map.p[src.x-1][src.y]);
			Map.p[src.x-1][src.y].checked=true;
			Map.p[src.x-1][src.y].flow=src.d2;
			Map.p[src.x-1][src.y].locked=true;
		}
		if (src.d3>=type) {
			round.add(Map.p[src.x][src.y-1]);
			Map.p[src.x][src.y-1].checked=true;
			Map.p[src.x][src.y-1].flow=src.d3;
			Map.p[src.x][src.y-1].locked=true;
		}
		if (src.d4>=type) {
			round.add(Map.p[src.x+1][src.y]);
			Map.p[src.x+1][src.y].checked=true;
			Map.p[src.x+1][src.y].flow=src.d4;
			Map.p[src.x+1][src.y].locked=true;
		}
			
		list.add(round);
		
		
		//从第2层开始循环 (注意去除相同的点)
		int flag = 0;
		for (int i=1;flag==0;i++) {
			round = new Vector<Point>();
			//遍历list[i]
			for (int j=0;j<list.get(i).size();j++) {
				Point temp = list.get(i).get(j);
				//将与list[i][j]相连接的点 加入到round中
				if (temp.d1>=type) {
					if (Map.p[temp.x][temp.y+1].locked==false) {
						Map.p[temp.x][temp.y+1].flow = Math.min(Map.p[temp.x][temp.y+1].flow, temp.flow+temp.d1);
					}
					if (Map.p[temp.x][temp.y+1].checked==false) {
						round.add(Map.p[temp.x][temp.y+1]);
						Map.p[temp.x][temp.y+1].checked=true;
					}
				}
				if (temp.d2>=type) {
					if (Map.p[temp.x-1][temp.y].locked==false) {
						Map.p[temp.x-1][temp.y].flow = Math.min(Map.p[temp.x-1][temp.y].flow, temp.flow+temp.d2);
					}
					if (Map.p[temp.x-1][temp.y].checked==false) {
						round.add(Map.p[temp.x-1][temp.y]);
						Map.p[temp.x-1][temp.y].checked=true;
					}
				}	
				if (temp.d3>=type) {
					if (Map.p[temp.x][temp.y-1].locked==false) {
						Map.p[temp.x][temp.y-1].flow = Math.min(Map.p[temp.x][temp.y-1].flow, temp.flow+temp.d3);
					}
					if (Map.p[temp.x][temp.y-1].checked==false) {
						round.add(Map.p[temp.x][temp.y-1]);
						Map.p[temp.x][temp.y-1].checked=true;
					}
				}	
				if (temp.d4>=type) {
					if (Map.p[temp.x+1][temp.y].locked==false) {
						Map.p[temp.x+1][temp.y].flow = Math.min(Map.p[temp.x+1][temp.y].flow, temp.flow+temp.d4);
					}
					if (Map.p[temp.x+1][temp.y].checked==false) {
						round.add(Map.p[temp.x+1][temp.y]);
						Map.p[temp.x+1][temp.y].checked=true;
					}
				}
			}
			
			//遍历list[i]结束，判断是否找到了终点，将round所有元素lock，将round加入到list中，作为list[i+1]
			for (int j=0;j<round.size();j++) {
				if (Point.issame(dst, round.get(j))) {
					pathlength = i+1;
					dst.flow=round.get(j).flow;
					flag=1;
					break;
				}
				round.get(j).locked=true;
			}
			list.add(round);
		}
		
		//新建一个数组，存储结果
		int[] path = new int[pathlength];
		for (int i=pathlength-1;i>=0;i--) {
			//遍历list[i]，找到与dst相邻且流量符合的点，记录到dst的方向，将dst变为该点
			for (int j=0;j<list.get(i).size();j++) {
				int a = howlinked(list.get(i).get(j), dst);
				if ( (a==1)&&((list.get(i).get(j).d1+list.get(i).get(j).flow == dst.flow)) ) {
					path[i]=a;
					dst=list.get(i).get(j);
					break;
				}
				if ( (a==2)&&((list.get(i).get(j).d2+list.get(i).get(j).flow == dst.flow)) ) {
					path[i]=a;
					dst=list.get(i).get(j);
					break;
				}
				if ( (a==3)&&((list.get(i).get(j).d3+list.get(i).get(j).flow == dst.flow)) ) {
					path[i]=a;
					dst=list.get(i).get(j);
					break;
				}
				if ( (a==4)&&((list.get(i).get(j).d4+list.get(i).get(j).flow == dst.flow)) ) {
					path[i]=a;
					dst=list.get(i).get(j);
					break;
				}
			}
		}
		
		return path;
	}
	
	
	public static void openroad (Point a, Point b) {
		/* @REQUIRES:(a!=b);(a isn't linked with b);
		 * @MODIFIES: Map.p;
		 * @EFFECTS:add a new road between a and b;
		 */
		if ( (a.x==b.x)&&(a.y+1==b.y)&&(a.d1<0) ) {
			a.d1=0;
			b.d3=0;
		}
		if ( (a.x-1==b.x)&&(a.y==b.y)&&(a.d2<0) ) {
			a.d2=0;
			b.d4=0;
		}	
		if ( (a.x==b.x)&&(a.y-1==b.y)&&(a.d3<0) ) {
			a.d3=0;
			b.d1=0;
		}	
		if ( (a.x+1==b.x)&&(a.y==b.y)&&(a.d4<0) ) {
			a.d4=0;
			b.d2=0;
		}	
	}
	
	
	public static void closeroad (Point a, Point b) {
		/* @REQUIRES:(a!=b);(a isn't linked with b);
		 * @MODIFIES: Map.p;
		 * @EFFECTS:close the road between a and b;
		 */
		if ( (a.x==b.x)&&(a.y+1==b.y)&&(a.d1>=0) ) {
			a.d1=-1;
			b.d3=-1;
		}
		if ( (a.x-1==b.x)&&(a.y==b.y)&&(a.d2>=0) ) {
			a.d2=-1;
			b.d4=-1;
		}	
		if ( (a.x==b.x)&&(a.y-1==b.y)&&(a.d3>=0) ) {
			a.d3=-1;
			b.d1=-1;
		}	
		if ( (a.x+1==b.x)&&(a.y==b.y)&&(a.d4>=0) ) {
			a.d4=-1;
			b.d2=-1;
		}
	}
	
	
	public static void clearflow () {
		/* @REQUIRES: None;
		 * @MODIFIES: Map.p;
		 * @EFFECTS:clear the flow-number of all the map;
		 */
		for (int i=0;i<80;i++) {
			for (int j=0;j<80;j++) {
				if(Map.p[i][j].d1>0)
					Map.p[i][j].d1=0;
				if(Map.p[i][j].d2>0)
					Map.p[i][j].d2=0;
				if(Map.p[i][j].d3>0)
					Map.p[i][j].d3=0;
				if(Map.p[i][j].d4>0)
					Map.p[i][j].d4=0;
			}
		}
	}
	
	
}
