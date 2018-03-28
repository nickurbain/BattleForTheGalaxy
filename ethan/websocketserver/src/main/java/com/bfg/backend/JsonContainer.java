package com.bfg.backend;

import com.google.gson.JsonArray;

public class JsonContainer {
	
	private Integer jsonOrigin;
	private Integer jsonType;
	private Integer matchId;
	private JsonArray matchStats;
	
	public JsonContainer() {
		this.setJsonOrigin(0);
		this.setJsonType(null);
		this.setMatchId(null);
		this.setMatchStats(null);
	}

	public Integer getJsonOrigin() {
		return jsonOrigin;
	}

	public void setJsonOrigin(Integer jsonOrigin) {
		this.jsonOrigin = jsonOrigin;
	}

	public Integer getJsonType() {
		return jsonType;
	}

	public void setJsonType(Integer jsonType) {
		this.jsonType = jsonType;
	}

	public Integer getMatchId() {
		return matchId;
	}

	public void setMatchId(Integer matchId) {
		this.matchId = matchId;
	}

	public JsonArray getMatchStats() {
		return matchStats;
	}

	public void setMatchStats(JsonArray matchStats) {
		this.matchStats = matchStats;
	}	
}
