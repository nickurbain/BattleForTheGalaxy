package data;

public class ChatData extends JsonHeader {

	private String message, to;
	// private String alliance;

	/**
	 * Empty Constructor
	 */

	/*public ChatData(int jsonOrigin, int jsonType) {
		super(jsonOrigin, jsonType);
	}*/

	/**
	 * Constructor to create an Alliance object
	 * 
	 * @param jsonOrigin
	 *            The origin of the Json type
	 * @param jsonType
	 *            The type of alliance json
	 * @param alliance
	 *            The name of the alliance to either join or create
	 * @param name
	 *            The name of the user trying to join or create
	 */
	public ChatData(int jsonOrigin, int jsonType,  String to, String message) {
		super(jsonOrigin, jsonType);
		this.setTo(to);
		this.setMessage(message);
	}

	/**
	 * Returns the members name
	 * 
	 * @return the name of the member
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the name of the member
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String receiver) {
		this.to = receiver;
	}

}
