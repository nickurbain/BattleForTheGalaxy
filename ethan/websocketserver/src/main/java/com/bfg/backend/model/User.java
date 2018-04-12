package com.bfg.backend.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//import org.hibernate.validator.constraints.NotEmpty;

/**
 * The model for the user database
 * 
 * @author emball, jln
 */
@Entity // This tells Hibernate to make a table out of this class
@Table(name = "User")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id")
	private Long user_id;

	@Column(name = "user_name")
	private String user_name;

	@Column(name = "user_pass")
	private String user_pass;

	/**
	 * An empty constructor
	 */
	public User() {

	}

	/**
	 * Retrieves the users id
	 * 
	 * @return the users id
	 */
	public Long getId() {
		return user_id;
	}

	/**
	 * Auto generated
	 * 
	 * @param user_id
	 *            Auto generated
	 */
	public void setId(Long user_id) {
		this.user_id = user_id;
	}

	/**
	 * Retrieves the users name
	 * 
	 * @return the users name
	 */
	public String getName() {
		return user_name;
	}

	/**
	 * Sets the users name
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
	 * Sets the password for the user
	 * 
	 * @param user_pass
	 *            The password
	 */
	public void setPass(String user_pass) {
		this.user_pass = user_pass;
	}
}