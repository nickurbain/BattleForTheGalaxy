package data;

/**
 * Simple class to encapsulate user queries.
 */
public class UserQueryData extends JsonHeader{
	private String id;
	private String pass;
	
	/**
	 * Empty Constructor
	 */
    public UserQueryData() {
		
	}

    /**
     * Constructor which takes in a password and id
     * @param jsonOrigin
     * @param jsonType
     * @param id the username
     * @param pass the password
     */
	public UserQueryData(int jsonOrigin, int jsonType, String id, String pass){
		super(jsonOrigin, jsonType);
		this.setId(id);
		this.setPass(pass);
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the pass
	 */
	public String getPass() {
		return pass;
	}

	/**
	 * @param pass the pass to set
	 */
	public void setPass(String pass) {
		this.pass = pass;
	}
		
}