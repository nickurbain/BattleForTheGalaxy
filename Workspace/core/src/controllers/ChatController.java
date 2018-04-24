package controllers;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.utils.JsonWriter.OutputType;

import data.ChatData;
import data.JsonHeader;
import master.classes.MasterScreen;

public class ChatController extends MasterScreen {

	private static ArrayList<String> messages = new ArrayList<String>();
	private static ChatData chat;
	
	public static void SendMessage(String message, String sendTo) {
		chat = new ChatData(JsonHeader.ORIGIN_CLIENT, JsonHeader.C_TYPE_MESSAGE, sendTo, message);
		System.out.println("Chat obj: " + game.getDataController().getJsonController().dataToJson(chat));
		game.getDataController().getJsonController().getJson().setOutputType(OutputType.json);
		game.getDataController().sendToServer(chat);
		game.getDataController().getJsonController().getJson().setOutputType(OutputType.minimal);
	}
	
	public static void addMessage(String message) {
		messages.add(message);
	}
	
	/**
	 * @return the messages
	 */
	public static ArrayList<String> getMessages() {
		return messages;
	}
	
	/**
	 * Pull messages from ChatData from the DataController 
	 */
	public static void getMessagesFromServer() {
		game.getDataController().parseRawData();
		for(Iterator<String> iter = game.getDataController().getChatDataFromServer().iterator(); iter.hasNext();) {
			String cd = iter.next();
			messages.add(cd);
			iter.remove();
		}
	}
}
