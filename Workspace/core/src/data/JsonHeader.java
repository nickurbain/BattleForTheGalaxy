package data;

/**
 * Abstract class which contains the the protocol for sending Json messages. Every message has
 * a Origin and a Type. 
 */
public abstract class JsonHeader {
	
	public final static int ORIGIN_SERVER = 0;
	public final static int ORIGIN_CLIENT = 1;
	
	//ORIGIN_SERVER
	public final static int TYPE_AUTH = 0;
	public final static int TYPE_DB_SHIP = 3;
	public final static int TYPE_MATCH_END = 4;
	public final static int TYPE_MATCH_NEW = 6;
	public final static int S_TYPE_REGISTRATION = 7;
	public final static int S_TYPE_ALLIANCE_QUERY = 8;
	public final static int SELECT_JUGGERNAUT = 9;
	public final static int PLAYER_DISCONNECT = 11;
	
	//ORIGIN_CLIENT
	public final static int TYPE_LOGIN = 0;
	public final static int TYPE_SHIP = 1;
	public final static int TYPE_PLAYER = 2;
	public final static int TYPE_PROJECTILE= 3;
	public final static int TYPE_HIT = 4;
	public final static int JOIN_MATCH = 12;
	public final static int TYPE_REGISTRATION = 13;
	public final static int C_TYPE_MESSAGE = 14;
	public final static int C_TYPE_JOIN_ALLIANCE = 15;
	public final static int C_TYPE_CREATE_ALLIANCE = 16;
	public final static int TYPE_CORE_UPDATE = 17;
	public final static int C_TYPE_RETRIEVE_ALLIANCES = 18;
	public final static int TYPE_DOUBLOONS = 19;
	
	
	private int jsonOrigin;
	private int jsonType;
	
	/**
	 * Constructor that sets the origin and type.
	 * @param origin the origin of the message
	 * @param type the type of message.
	 */
	public JsonHeader(int origin, int type) {
		this.setJsonOrigin(origin);
		this.setJsonType(type);
	}
	
	/**
	 * Empy constructor.
	 */
	public JsonHeader() {
		
	}

	/**
	 * @return the jsonOrigin
	 */
	public int getJsonOrigin() {
		return jsonOrigin;
	}

	/**
	 * @param jsonOrigin the jsonOrigin to set
	 */
	public void setJsonOrigin(int jsonOrigin) {
		this.jsonOrigin = jsonOrigin;
	}

	/**
	 * @return the jsonType
	 */
	public int getJsonType() {
		return jsonType;
	}

	/**
	 * @param jsonType the jsonType to set
	 */
	public void setJsonType(int jsonType) {
		this.jsonType = jsonType;
	}
	
	
}
