package homework11;

public class Point {
	/*Overview:维护了一个点类，实现了点的存储,方便了BFS查找，同时实现了判断相同、读入点的行为
	*表示对象:int x, int y , int d1-d4, boolean checked, boolean locked, int flow
	*抽象函数:AF(c)=((i,j),d1,d2,d3,d4), where i==c.x, j==c.y, d1==c.d1, d2==c.d2, d3==c.d3, d4==c.d4
	*不变式:d1,d2,d3,d4>=-2, 0<=flow<=100
	*/
	public int x=0; //纵坐标
	public int y=0; //横坐标
	
	//流量(-2表示不连通,-1表示道路封闭)
	public int d1= -2; //右
	public int d2= -2; //上
	public int d3= -2; //左
	public int d4= -2; //下
	
	public int lightround=0;  //记录红绿灯标变换周期(50-100ms),没有红绿灯则为0
	public long lighttime=0L; //记录最近红绿灯变换时间
	public int timetowait=0;  //记录需要等待的时间
	public boolean NSgreen = false;
	
	public boolean checked=false; //BFS中是否已经检查过
	public boolean locked=false;  //BFS中是否被锁住(不允许更改流量)
	public int flow=100;          //BFS中到该点的最小流量
	
	
	protected boolean repOK () {
		if ( (this.flow<0)||(this.flow>100) )
			return false;
		if ( (d1>=-2)&&(d2>=-2)&&(d3>=-2)&&(d4>=-2) )
			return true;
		return false;
	}
	
	
	public static boolean issame (Point a,Point b) {
		/* @REQUIRES:(\all Point a,b; a,b!=null);
		 * @MODIFIES: None;
		 * @EFFECTS:(a.x==b.x&&a.y==b.y) ==>\result==true;
		 * 			!(a.x==b.x&&a.y==b.y) ==> \result==false;
		 */
		if ( (a.x==b.x)&&(a.y==b.y) )
			return true;
		return false;
	}
	
	
	public static Point StringtoPoint (String a, String b) throws InvalidInputException {
		/* @REQUIRES:(\all a;a=="("+x && 0<=x<=79);
		 * 			 (\all b;b==y+")" && 0<=b<=79);
		 * @MODIFIES: None;
		 * @EFFECTS:normal_behavior
		 * 			\result==Point (x,y)
		 * 			exceptional_behavior(InvalidInputException);
		 */
		if ( (a.charAt(0)!='(')&&(b.charAt(b.length()-1)!=')') ) {
			throw new InvalidInputException(a+","+b);
		}
		a=a.substring(1);
		b=b.substring(0, b.length()-1);
		int x,y;
		try {
			x=Integer.valueOf(a);
			y=Integer.valueOf(b);
		}catch (NumberFormatException e) {
			throw new InvalidInputException(a+","+b);
		}
		if ( (x<0)||(x>79)||(y<0)||(y>79) ) {
			throw new InvalidInputException(a+","+b);
		}
		return Map.p[x][y];
	}
	
	
	public String toString () {
		//@EFFECTS:\return=="(x,y)" 当一位数时自动补零
		String i,j;
		if (this.x<10)
			i="0"+this.x;
		else i=""+this.x;
		if (this.y<10)
			j="0"+this.y;
		else j=""+this.y;
		return "("+i+","+j+")";
	}
	
}



