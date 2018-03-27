package com.bfg.backend;

public class JsonContainer {
	
	private Integer jsonOrigin;
	private Integer jsonType;
	private Integer matchId;
	private String matchStats;
	
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

	public String getMatchStats() {
		return matchStats;
	}

	public void setMatchStats(String matchStats) {
		this.matchStats = matchStats;
	}
	
	
}
