package com.bfg.backend.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity // This tells Hibernate to make a table out of this class
@Table(name = "Chat")
public class Chat {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "chat_id")
	private Long guild_id;

	@Column(name = "user_name")
	private String user_name;

	@Column(name = "message")
	private String message;

	@Column(name = "chat_type")
	private String guild_file;

	public Chat() {

	}

	public Long getGuild_id() {
		return guild_id;
	}

	public void setGuild_id(Long guild_id) {
		this.guild_id = guild_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getGuild_file() {
		return guild_file;
	}

	public void setGuild_file(String guild_file) {
		this.guild_file = guild_file;
	}

}
