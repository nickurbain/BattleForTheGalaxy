package com.bfg.backend.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity // This tells Hibernate to make a table out of this class
@Table(name="Guild")
public class Guild {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "guild_id")
	private Long guild_id;

	@Column(name = "guild_name")
	private String guild_name;

	@Column(name = "guild_picture")
	private String guild_picture;

	@Column(name = "guild_file")
	private String guild_file;
	
	public Guild() {
		
	}

	public Long getGuild_id() {
		return guild_id;
	}

	public void setGuild_id(Long guild_id) {
		this.guild_id = guild_id;
	}

	public String getGuild_name() {
		return guild_name;
	}

	public void setGuild_name(String guild_name) {
		this.guild_name = guild_name;
	}

	public String getGuild_picture() {
		return guild_picture;
	}

	public void setGuild_picture(String guild_picture) {
		this.guild_picture = guild_picture;
	}

	public String getGuild_file() {
		return guild_file;
	}

	public void setGuild_file(String guild_file) {
		this.guild_file = guild_file;
	}
}