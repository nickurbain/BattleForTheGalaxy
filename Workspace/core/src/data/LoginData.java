package data;

/*
 * Simple class so that login data can be packed into Json easier
 */
public class LoginData {
	private String type = "login";
	private String id;
	private String pass;
	
	public LoginData(String id, String pass) {
		this.setId(id);
		this.setPass(pass);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
