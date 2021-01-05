package requestHandler;

/**
 * This Class contains all thi info needed to handel a given data and controller
 * name
 * 
 * @author rafaelelkoby
 *
 */
public class RequestHandler {

	private controllerName myCon;
	private String func;
	private String data;

	/**
	 * 
	 * RequestHandler(controllerName , functionName, data)
	 * 
	 * @param myCon
	 * @param func
	 * @param data
	 */
	public RequestHandler(controllerName myCon, String func, String data) {
		this.myCon = myCon;
		this.func = func;
		this.data = data;
	}

	public controllerName getMyCon() {
		return myCon;
	}

	public String getFunc() {
		return func;
	}

	public String getData() {
		return data;
	}

}
