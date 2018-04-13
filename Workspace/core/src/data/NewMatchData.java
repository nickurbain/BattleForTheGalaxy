package data;

/**
 * A data object for storing new match data
 */
public class NewMatchData extends JsonHeader{
	
	private int matchId;
	private int teamNum;
	
	/**
	 * Constructor that takes in matchId and teamId
	 * @param matchId the matchId
	 * @param teamId the teamId
	 */
	public NewMatchData(int matchId, int teamNum) {
		super(JsonHeader.ORIGIN_SERVER, JsonHeader.TYPE_MATCH_NEW);
		this.matchId = matchId;
		this.teamNum = teamNum;
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
	public int getTeamNum() {
		return teamNum;
	}

	/**
	 * @param teamId the teamId to set
	 */
	public void setTeamNum(int teamNum) {
		this.teamNum = teamNum;
	}
}
