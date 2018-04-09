package com.bfg.backend.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity // This tells Hibernate to make a table out of this class
@Table(name="Alliance")
public class Alliance {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "faction_id")
	private Long guild_id;

	@Column(name = "faction_name")
	private String guild_name;
	
	public Alliance() {
		
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
	
	
}
