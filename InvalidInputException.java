package homework11;

import java.io.IOException;

/**InvalidInputException是受查异常,必须得到处理
 * <p>在读取输入时，读到了不符合格式的输入，将会抛出InvalidInputException
 */
public class InvalidInputException extends IOException {
	private static final long serialVersionUID = 2L;
	
	/**创建一个{@link InvalidInputException}异常
	 */
	public InvalidInputException () {}
	
	/**创建一个{@link InvalidInputException}异常
	 * @param message 错误读入的内容
	 */
	public InvalidInputException (String message) {
		super(message);
	}
}
