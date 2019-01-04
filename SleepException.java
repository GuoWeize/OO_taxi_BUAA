package homework11;

/**SleepException是非受查异常 
 * <p>在调用TimeUnit.MILLISECONDS.sleep()函数时，出现InterruptedException，将会被转化为SleepException
 */
public class SleepException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	/**创建一个{@link SleepException}异常
	 */
	public SleepException () {}
	
	/**创建一个{@link SleepException}异常
	 * @param message 错误读入的内容
	 */
	public SleepException (String message) {
		super(message);
	}
	
}
