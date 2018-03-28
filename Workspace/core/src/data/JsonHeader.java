package data;

public abstract class JsonHeader {
	
	public final static int ORIGIN_SERVER = 0;
	public final static int ORIGIN_CLIENT = 1;
	
	//ORIGIN_SERVER
	public final static int TYPE_AUTH = 0;
	public final static int TYPE_DB_SHIP = 3;
	public final static int TYPE_MATCH_END = 4;
	public final static int TYPE_MATCH_NEW = 6;
	public final static int S_TYPE_REGISTRATION = 7;
	
	//ORIGIN_CLIENT
	public final static int TYPE_LOGIN = 0;
	public final static int TYPE_SHIP = 1;
	public final static int TYPE_PLAYER = 2;
	public final static int TYPE_PROJECTILE= 3;
	public final static int TYPE_HIT = 4;
	public final static int TYPE_JOINMATCH = 12;
	public final static int TYPE_REGISTRATION = 13;
	
	private int jsonOrigin;
	private int jsonType;
	
	public JsonHeader(int origin, int type) {
		this.setJsonOrigin(origin);
		this.setJsonType(type);
	}
	
	public JsonHeader() {
		
	}

	public int getJsonOrigin() {
		return jsonOrigin;
	}

	public void setJsonOrigin(int origin) {
		this.jsonOrigin = origin;
	}

	public int getJsonType() {
		return jsonType;
	}

	public void setJsonType(int type) {
		this.jsonType = type;
	}
	
	
}
