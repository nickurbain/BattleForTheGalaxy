package data;

/*
 * Simple class so that login data can be packed into Json easier
 */
public class LoginData extends JsonHeader{
	private String id;
	private String pass;
	
	public LoginData(int jsonOrigin, int jsonType, String id, String pass){
		super(jsonOrigin, jsonType);
		this.setId(id);
		this.setPass(pass);
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}
	
	
	
}
