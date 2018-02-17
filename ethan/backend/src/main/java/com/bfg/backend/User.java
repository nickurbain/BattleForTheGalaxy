package com.bfg.backend;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;


@Entity
@Table(name = "user")
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "Userid")
	@NotEmpty
	private Integer Userid;
	
	@Column(name = "UserName")
	@NotEmpty
	private String UserName;
	
	@Column(name = "UserPassword")
	@NotEmpty
	private String UserPassword;
	
	
	public User() {}
	
	public User(String UserName, String UserPassword) {
		this.UserName = UserName;
		this.UserPassword = UserPassword;
	}
	
	public Integer getId() {
		return Userid;
	}
	
	public void setId(Integer Userid) {
		this.Userid = Userid;
	}
	
	
	public String getName() {
		return UserName;
	}
	
	public void setName(String UserName) {
		this.UserName = UserName;
	}
	
	public void setPass(String UserPassword) {
		this.UserPassword = UserPassword;
	}
	
	public String getPass() {
		return UserPassword;
	}
}
