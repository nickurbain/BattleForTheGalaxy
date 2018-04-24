package controllers;

import java.net.UnknownHostException;

import com.badlogic.gdx.utils.Queue;

import battle.galaxy.AllianceScreen;
import data.ChatData;
import data.JsonHeader;
import master.classes.MasterScreen;

public class ChatController extends MasterScreen {

	// private static Queue<String> messages;
	private static ChatData chat;

	public static void SendMessage(String message, String sendTo) {
		chat = new ChatData(JsonHeader.ORIGIN_CLIENT, JsonHeader.C_TYPE_MESSAGE, sendTo, message);
		System.out.println("Chat obj: " + game.getDataController().getJsonController().dataToJson(chat));
		game.getDataController().sendToServer(chat);
	}
}
