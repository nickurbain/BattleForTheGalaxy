package data;

/**
 * A data object for storing new match data
 */
public class NewMatchData extends JsonHeader{
	
	private int matchId;
	private int teamId;
	
	/**
	 * Constructor that takes in matchId and teamId
	 * @param matchId the matchId
	 * @param teamId the teamId
	 */
	public NewMatchData(int matchId, int teamId) {
		super(JsonHeader.ORIGIN_SERVER, JsonHeader.TYPE_MATCH_NEW);
		this.matchId = matchId;
		this.teamId = teamId;
	}
	
	/**
	 * Empty Constructor
	 */
	public NewMatchData() {
		
	}

	/**
	 * @return the matchId
	 */
	public int getMatchId() {
		return matchId;
	}

	/**
	 * @param matchId the matchId to set
	 */
	public void setMatchId(int matchId) {
		this.matchId = matchId;
	}

	/**
	 * @return the teamId
	 */
	public int getTeamId() {
		return teamId;
	}

	/**
	 * @param teamId the teamId to set
	 */
	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}
}
