package data;

public abstract class JsonHeader {
	
	public final static byte ORIGIN_SERVER = 0;
	public final static byte ORIGIN_CLIENT = 1;
	
	//ORIGIN_SERVER
	public final static byte TYPE_AUTH = 0;
	
	//ORIGIN_CLIENT
	public final static byte TYPE_LOGIN = 0;
	public final static byte TYPE_PLAYER = 1;
	public final static byte TYPE_PROJECTILE= 2;
	
	private byte jsonOrigin;
	private byte jsonType;
	
	public JsonHeader(byte origin, byte type) {
		this.setJsonOrigin(origin);
		this.setJsonType(type);
	}

	public byte getJsonOrigin() {
		return jsonOrigin;
	}

	public void setJsonOrigin(byte origin) {
		this.jsonOrigin = origin;
	}

	public byte getJsonType() {
		return jsonType;
	}

	public void setJsonType(byte type) {
		this.jsonType = type;
	}
	
	
}
