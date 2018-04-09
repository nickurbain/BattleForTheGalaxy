package controllers;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
public class JsonController {
	
	private Json json;
	private JsonReader jsonReader;
	
	public JsonController() {
		setJson(new Json());
		setJsonReader(new JsonReader());
	}
	
	/**
	 * General purpose method to convert a data object to a json string
	 * @param data the data to be converted to a json string
	 * @return string the json
	 */
	public String dataToJson(Object data) {
		System.out.println("JSON Controller");
		return json.toJson(data);
	}
	
	/**
	 * Special method for hit that appends caused death boolean to hit json
	 * @param data
	 * @return
	 */
	public String hitToJson(Object data, boolean causedDeath) {
		String hitString = dataToJson(data);
		hitString = hitString.substring(0, hitString.length() - 1);
		hitString += ",causedDeath:" + causedDeath + "}";
		return hitString;
	}
	/**
	 * @return the json
	 */
	public Json getJson() {
		return json;
	}
	/**
	 * @param json the json to set
	 */
	public void setJson(Json json) {
		this.json = json;
	}
	/**
	 * @return the jsonReader
	 */
	public JsonReader getJsonReader() {
		return jsonReader;
	}
	/**
	 * @param jsonReader the jsonReader to set
	 */
	public void setJsonReader(JsonReader jsonReader) {
		this.jsonReader = jsonReader;
	}
	
	
}