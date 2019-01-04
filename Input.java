package homework11;
import java.util.*;
import java.io.FileWriter;
import java.io.IOException;

public class Input extends Thread {
	/*Overview:维护了一个输入类，实现了读取、处理输入的行为，同时在违规输入时报错
	*表示对象:null
	*抽象函数:null
	*不变式:null
	*/
	private final RequestList requestlist;
	
	public Input (RequestList requestlist) {
		this.requestlist=requestlist;
	}
	
	
	public void scanCR (String s) throws InvalidInputException {
		/* @REQUIRES:(s="[GG,point1,point2");
		 * @MODIFIES: requestlist;
		 * @EFFECTS:normal_behavior
		 * 			创建一个新的Request对象，并将其加到requestlist.reqlist的最后
		 * 			exceptional_behavior(InvalidInputException);
		 */
		String[] block = s.split(",");
		
		if (block.length!=5) 
			throw new InvalidInputException(s+"]");
		Point src = Point.StringtoPoint(block[1],block[2]);
		Point dst = Point.StringtoPoint(block[3],block[4]);
		if (Point.issame(src, dst)) {
			throw new InvalidInputException("出发地和目的地相同："+s+"]");
		}
		else {
			Request req = new Request (src, dst);
			requestlist.newreq(req);
		}
	}
	
	
	public void scanCX (String s) throws InvalidInputException {
		/* @REQUIRES:(\all number;[CX,number;0<=number<=99);
		 * @MODIFIES: None;
		 * @EFFECTS:normal_behavior
		 * 			print(出租车信息)
		 * 			exceptional_behavior(InvalidInputException);
		 */
		FileWriter writer = null;
		try {
			writer = new FileWriter("out.txt", true);
		} catch (IOException e) {}
		
		String[] block = s.split(",");
		int num;
		
		if (block.length!=2) 
			throw new InvalidInputException(s+"]");
		try {
			num=Integer.valueOf(block[1]);
		}catch (NumberFormatException e) {
			throw new InvalidInputException(s+"]");
		}
		if ( (num<0)||(num>99) ) 
			throw new InvalidInputException(s+"]");
		try {
			writer.write(TaxiSystem.taxi[num].toString()+"\r\n");
		} catch (IOException e) {}
		
	}
	
	
	public void scanGG (String s) throws InvalidInputException {
		/* @REQUIRES:(\all status;[GG,point1,point2,status;status==0||status==1);
		 * @MODIFIES: Map.p;
		 * @EFFECTS:normal_behavior
		 * 			(status==0)==>point1和point2间的道路关闭;
		 * 			(status==1)==>point1和point2间的道路关闭;
		 * 			(status!=0 && status!=1)==>exceptional_behavior(InvalidInputException);
		 */
		String[] block = s.split(",");
		int status;
		
		if (block.length!=6) 
			throw new InvalidInputException(s+"]");
		Point from = Point.StringtoPoint(block[1],block[2]);
		Point to = Point.StringtoPoint(block[3],block[4]);
		try {
			status=Integer.valueOf(block[5]);
		}catch (NumberFormatException e) {
			throw new InvalidInputException(s+"]");
		}
		if (status*status!=status) 
			throw new InvalidInputException(s+"]");
		if (status==0)
			Map.closeroad(from, to);
		if (status==1)
			Map.openroad(from, to);
	}
	
	
	public void run () {
		//@EFFECTS:listening to the input;
		Scanner in=new Scanner(System.in); 
		String sline;
		String[] command;
		String signal;
		
		while (in.hasNextLine()) {
			sline=in.nextLine();
			sline.replaceAll(" ", "");
			sline.replaceAll("\t", "");
			command=sline.split("]");
			
			for (int i=0;i<command.length;i++) {
				if (command[i].charAt(0)!='[') {
					System.out.println("输入格式错误！错误信息：[]格式错误");
					continue;
				}
				signal = command[i].substring(1,3);
				if (signal.equals("CR")) {
					try {
						scanCR(command[i]);
					} catch (InvalidInputException e) {
						System.out.println("输入格式错误！错误信息："+e.getMessage());
						continue;
					}
				}
				else if (signal.equals("CX")) {
					try {
						scanCX(command[i]);
					} catch (InvalidInputException e) {
						System.out.println("输入格式错误！错误信息："+e.getMessage());
						continue;
					}
				}
				else if (signal.equals("GG")) {
					try {
						scanCX(command[i]);
					} catch (InvalidInputException e) {
						System.out.println("输入格式错误！错误信息："+e.getMessage());
						continue;
					}
				}
				else {
					System.out.println("输入格式错误！错误信息：标识符错误");
					continue;
				}
			}
			
		}
		in.close();
	}
	
}



