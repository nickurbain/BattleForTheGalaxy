package data;

/**
 * Contains generic information passed to/from the server such as MatchEnd or MatchStats. Extends JsonHeader
 */
public class GenericData extends JsonHeader {
	
	private String msg = "";
	private Boolean trigger = false;
	
	
	/**
	 * Constructor
	 * @param origin
	 * @param type
	 * @param msg
	 * @param trigger
	 */
	public GenericData(int jsonOrigin, int jsonType, String msg, Boolean trigger) {
		super(jsonOrigin, jsonType);
		this.msg = msg;
		this.trigger = trigger;
	}
	
	/**
	 * Constructor for just a message
	 * @param jsonOrigin
	 * @param jsonType
	 * @param msg
	 */
	public GenericData(int jsonOrigin, int jsonType, String msg) {
		super(jsonOrigin, jsonType);
		this.msg = msg;
	}
	
	/**
	 * Constructor for just a boolean trigger
	 * @param jsonOrigin
	 * @param jsonType
	 * @param trigger
	 */
	public GenericData(int jsonOrigin, int jsonType, Boolean trigger) {
		super(jsonOrigin, jsonType);
		this.trigger = trigger;
	}
	
	/**
	 * Constructor for no msg or trigger
	 * @param jsonOrigin
	 * @param jsonType
	 */
	public GenericData(int jsonOrigin, int jsonType) {
		super(jsonOrigin, jsonType);
	}

	/**
	 * Empty Constructor
	 */
	public GenericData() {
		
	}

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * @return the trigger
	 */
	public Boolean getTrigger() {
		return trigger;
	}

	/**
	 * @param trigger the trigger to set
	 */
	public void setTrigger(Boolean trigger) {
		this.trigger = trigger;
	}

}
