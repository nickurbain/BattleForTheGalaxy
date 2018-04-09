package controllers;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;

public class JsonController {
	
	private Json json;
	private JsonReader jsonReader;
	
	/**
	 * Constructor that sets a new Json and JsonReader
	 */
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
	 * Convert a json string to an object
	 * @param data The json string to be converted
	 * @param classType The class the string should be converted to
	 * @return The object of type classType from data
	 */
	public <T> Object convertFromJson(String data, Class<T> classType) {
		return json.fromJson(classType, data);
	}
	
	/**
	 * Pull the value of a json element from a json string
	 * @param <T> The type of the value
	 * @param data The json string to pull an value from
	 * @param jsonElement The name of the element whose value we want
	 * @return
	 */
	public <T> Object getJsonElement(String data, String jsonElement, Class<T> type) {
		if(type == Integer.class) {
			return jsonReader.parse(data).getInt(jsonElement);
		}else if (type == Float.class){
			return jsonReader.parse(data).getFloat(jsonElement);
		}else if (type == String.class) {
			return jsonReader.parse(data).getString(jsonElement);
		}else if (type == Boolean.class) {
			return jsonReader.parse(data).getBoolean(jsonElement);
		}
		//If type is something else, simply return the element as a string
		return jsonReader.parse(data).get(jsonElement).asString();
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
