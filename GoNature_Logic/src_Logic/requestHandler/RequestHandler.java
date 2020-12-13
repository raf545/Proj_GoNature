package requestHandler;

public class RequestHandler {

	private controllerName myCon;
	private String func;
	private String data;
	
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
