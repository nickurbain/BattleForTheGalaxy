package data;

public class JuggernautData extends JsonHeader{

	private int currId;
	private int prevId;
	
	public JuggernautData(int jsonOrigin, int jsonType, int currId, int prevId) {
		super(jsonOrigin, jsonType);
		setCurrId(currId);
		setPrevId(prevId);
	}
	
	public JuggernautData() {
		
	}

	/**
	 * @return the currId
	 */
	public int getCurrId() {
		return currId;
	}

	/**
	 * @param currId the currId to set
	 */
	public void setCurrId(int currId) {
		this.currId = currId;
	}

	/**
	 * @return the prevId
	 */
	public int getPrevId() {
		return prevId;
	}

	/**
	 * @param prevId the prevId to set
	 */
	public void setPrevId(int prevId) {
		this.prevId = prevId;
	}
	
}
