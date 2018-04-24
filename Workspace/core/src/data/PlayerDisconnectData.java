package data;

public class PlayerDisconnectData extends JsonHeader{

	private int matchId;
	
	public PlayerDisconnectData(int jsonOrigin, int jsonType, int matchId) {
		super(jsonOrigin, jsonType);
		setMatchId(matchId);
	}
	
	public PlayerDisconnectData() {
		
	}

	/**
	 * @return the playerId
	 */
	public int getMatchId() {
		return matchId;
	}

	/**
	 * @param playerId the playerId to set
	 */
	public void setMatchId(int matchId) {
		this.matchId = matchId;
	}
	
}
