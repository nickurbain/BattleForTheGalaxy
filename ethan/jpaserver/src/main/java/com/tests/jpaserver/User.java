package com.tests.jpaserver;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//import org.hibernate.validator.constraints.NotEmpty;


@Entity // This tells Hibernate to make a table out of this class
@Table(name="User")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="userid")
    private Long userid;

    @Column(name="user_name")
    private String user_name;
    
    @Column(name="user_password")
    private String user_password;
    
    
    public User() {}

	public Long getId() {
		return userid;
	}
	
	public void setId(Long userid) {
		this.userid = userid;
	}

	public String getName() {
		return user_name;
	}

	public void setName(String user_name) {
		this.user_name = user_name;
	}

	public String getPass() {
		return user_password;
	}

	public void setPass(String user_password) {
		this.user_password = user_password;
	}


}