package homework11;
import java.util.Vector;

public class TaxiIterator implements Iterator {
	Vector<ServeInfo> list;
	int forward=0;
	int backward=list.size()-1;
	
	public TaxiIterator (Vector<ServeInfo> list) {
		/* @REQUIRES:null;
		 * @MODIFIES:this;
		 * @EFFECTS:\result==a new TaxiIterator;
		 */
		this.list=list;
	}
	
	
	public boolean hasNext() {
		/* @REQUIRES:null;
		 * @MODIFIES:null;
		 * @EFFECTS:(Has next object)==>(\result==true);
		 * 			(Hasn't next object)==>(\result==true);
		 */
		if (forward>=list.size())
			return false;
		return true;
	}
	
	
	public Object next() {
		/* @REQUIRES:null;
		 * @MODIFIES:forward;
		 * @EFFECTS:\return==next object;
		 */
		Object item = list.get(forward);
		forward++;
		return item;
	}

	
	public boolean hasPrevious() {
		/* @REQUIRES:null;
		 * @MODIFIES:null;
		 * @EFFECTS:(Has previous object)==>(\result==true);
		 * 			(Hasn't previous object)==>(\result==true);
		 */
		if (backward<=0)
			return false;
		return true;
	}

	
	public Object previous() {
		/* @REQUIRES:null;
		 * @MODIFIES:backward;
		 * @EFFECTS:\return==previous object;
		 */
		Object item = list.get(backward);
		backward--;
		return item;
	}

}
