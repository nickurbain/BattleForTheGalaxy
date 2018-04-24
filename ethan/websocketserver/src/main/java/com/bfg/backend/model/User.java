package com.bfg.backend.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//import org.hibernate.validator.constraints.NotEmpty;

/**
 * User model for database queries. 
 * Each field in the user corresponds to a column in the users table in the database.
 * 
 * @author emball, jln
 */
@Entity // This tells Hibernate to make a table out of this class
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="user_id")
    private Long user_id;

    @Column(name="user_name")
    private String user_name;
    
    @Column(name="user_pass")
    private String user_pass;
    
    @Column(name="doubloons")
    private Integer doubloons;
    
    /**
     * Blank constructor
     */
    public User() {}

    /**
     * Gets the user's id
     * 
     * @return 
     * 		user_id
     */
	public Long getId() {
		return user_id;
	}
	
	/**
	 * Sets the user's id
	 * 
	 * @param user_id
	 */
	public void setId(Long user_id) {
		this.user_id = user_id;
	}

	/**
	 * Retrieves the users name
	 * 
	 * @return the user's name
	 */
	public String getName() {
		return user_name;
	}

	/**
	 * Sets the user's name
	 * 
	 * @param user_name
	 *            The name of the user
	 */
	public void setName(String user_name) {
		this.user_name = user_name;
	}

	/**
	 * Retrieves the password of the user
	 * 
	 * @return the password
	 */
	public String getPass() {
		return user_pass;
	}

	/**
	 * Sets the user's password
	 * 
	 * @param user_pass The password
	 */
	public void setPass(String user_pass) {
		this.user_pass = user_pass;
	}
	
	public Integer getDoubloons() {
		return doubloons;
	}
	
	public void setDoubloons(Integer doubloons) {
		this.doubloons = doubloons;
	}
	
	public void addDoubloons(Integer doubloons) {
		this.doubloons += doubloons;
	}
}