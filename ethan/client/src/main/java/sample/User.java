package sample;

public class User {
    private Long user_id;

    private String user_name;
    
    private String user_pass;
    
    
    public User() {}

	public Long getId() {
		return user_id;
	}
	
	public void setId(Long user_id) {
		this.user_id = user_id;
	}

	public String getName() {
		return user_name;
	}

	public void setName(String user_name) {
		this.user_name = user_name;
	}

	public String getPass() {
		return user_pass;
	}

	public void setPass(String user_pass) {
		this.user_pass = user_pass;
	}
	
	public String toString() {
		return String.format("{from: %1$-10s | pass: %2$-10s}", user_name, user_pass);
	}
}
