package com.bfg.backend.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//import org.hibernate.validator.constraints.NotEmpty;

/**
 * 
 * 
 * @author emball, jln
 *
 */
@Entity // This tells Hibernate to make a table out of this class
@Table(name="User")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="user_id")
    private Long user_id;

    @Column(name="user_name")
    private String user_name;
    
    @Column(name="user_pass")
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
}