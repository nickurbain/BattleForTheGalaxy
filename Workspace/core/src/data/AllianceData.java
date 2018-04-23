package data;

/**
 * Simple class to encapsulate alliance queries.
 */
public class AllianceData extends JsonHeader {
	private String member;
	private String alliance;

	/**
	 * Empty Constructor
	 */
	public AllianceData() {

	}

	public AllianceData(int jsonOrigin, int jsonType) {
		super(jsonOrigin, jsonType);
	}
	
	/**
	 * Constructor to create an Alliance object
	 * 
	 * @param jsonOrigin The origin of the Json type
	 * @param jsonType The type of alliance json
	 * @param alliance The name of the alliance to either join or create
	 * @param name The name of the user trying to join or create
	 */
	public AllianceData(int jsonOrigin, int jsonType, String alliance, String member) {
		super(jsonOrigin, jsonType);
		this.setAllianceName(alliance);
		this.setMemberName(member);
	}

	/**
	 * Returns the alliances' name
	 * @return the name of the alliance
	 */
	public String getAllianceName() {
		return alliance;
	}
	
	/**
	 * Sets the name of the alliance to join or create
	 * @param alliance The name of the alliance
	 */
	private void setAllianceName(String alliance) {
		this.alliance = alliance;
	}

	/**
	 * Returns the members name
	 * @return the name of the member
	 */
	public String getMemberName() {
		return member;
	}

	/**
	 * Sets the name of the member
	 * @param id
	 *            the id to set
	 */
	public void setMemberName(String name) {
		this.member = name;
	}
}