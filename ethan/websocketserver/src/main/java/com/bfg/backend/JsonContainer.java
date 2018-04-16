package com.bfg.backend;

import com.google.gson.JsonArray;

/**
 * Class used to hold json items to send to the client. This object is set and
 * then serialized with gson.
 * 
 * @author emball
 *
 */
public class JsonContainer {

	private Integer jsonOrigin;
	private Integer jsonType;
	private Integer matchId;
	private JsonArray matchStats;
	private Integer teamNum;
	private String message; // TODO

	/**
	 * Constructor initialized everything to null except jsonOrigin, which is 0 for
	 * the server.
	 */
	public JsonContainer() {
		this.setJsonOrigin(0);
		this.setJsonType(null);
		this.setMatchId(null);
		this.setMatchStats(null);
		this.setTeamNum(null);
		this.setMessage(null);
	}

	/**
	 * Retrieves the the origin of the Json Object
	 * 
	 * @return the jsonOrigin value, which is 0 since it is from the server.
	 */
	public Integer getJsonOrigin() {
		return jsonOrigin;
	}

	/**
	 * Sets the jsonOrigin value. This is actually set in the constructor.
	 * 
	 * @param jsonOrigin
	 */
	public void setJsonOrigin(Integer jsonOrigin) {
		this.jsonOrigin = jsonOrigin;
	}

	/**
	 * Gets the jsonType set.
	 * 
	 * @return jsonType
	 */
	public Integer getJsonType() {
		return jsonType;
	}

	/**
	 * Sets the jsonType for a message
	 * 
	 * @param jsonType
	 */
	public void setJsonType(Integer jsonType) {
		this.jsonType = jsonType;
	}

	/**
	 * Gets the match Id if set
	 * 
	 * @return matchId
	 */
	public Integer getMatchId() {
		return matchId;
	}

	/**
	 * Sets the Id for a match
	 * 
	 * @param matchId
	 */
	public void setMatchId(Integer matchId) {
		this.matchId = matchId;
	}

	/**
	 * Gets the matchStats if set
	 * 
	 * @return the match statistics
	 */
	public JsonArray getMatchStats() {
		return matchStats;
	}

	/**
	 * Sets the matchStats for a given match, which is a JsonArray
	 * 
	 * @param matchStats
	 */
	public void setMatchStats(JsonArray matchStats) {
		this.matchStats = matchStats;
	}

	/**
	 * Gets the team name/color
	 * 
	 * @return team
	 */
	public Integer getTeamNum() {
		return teamNum;
	}

	/**
	 * Sets the team name/color
	 * 
	 * @param team
	 */
	public void setTeamNum(Integer teamNum) {
		this.teamNum = teamNum;
	}

	/**
	 * Gets the message if set
	 * 
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets a general message to send to clients
	 * 
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
