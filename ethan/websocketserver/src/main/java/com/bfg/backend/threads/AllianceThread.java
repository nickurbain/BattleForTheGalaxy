package com.bfg.backend.threads;

import java.io.IOException;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.bfg.backend.enums.ClientJsonType;
import com.bfg.backend.enums.ServerJsonType;
import com.bfg.backend.model.Alliance;
import com.bfg.backend.model.User;
import com.bfg.backend.repository.AllianceRepository;
import com.bfg.backend.repository.UserRepository;
import com.google.gson.JsonObject;

/**
 * LoginThread for querying the database for login and register requests
 * 
 * @author emball, jln
 *
 */
public class AllianceThread extends Thread {
	AllianceRepository allianceRepository;

	private WebSocketSession client;
	private Thread t;
	private User user;
	private Alliance alliance;
	private int type;

	/**
	 * Constructor for initializing an AllianceThread
	 * 
	 * @param allianceRepository
	 *            The repository to query
	 * @param alliance
	 *            The alliance to create or join
	 * @param user
	 *            The user to create the alliance with or to add to an existing
	 *            alliance
	 * @param client
	 *            A websocketsession for sending a response to
	 * @param type
	 *            Create or add to the alliance database.
	 */
	public AllianceThread(AllianceRepository allianceRepository, Alliance alliance, User user, WebSocketSession client,
			int type) {
		this.allianceRepository = allianceRepository;
		this.user = user;
		this.alliance = alliance;
		this.client = client;
		this.type = type;
	}

	@Override
	public void run() {
		if (type == ClientJsonType.ALLIANCE_CREATE.ordinal()) {
			createAlliance();
		} else {
			joinAlliance();
		}
	}

	/**
	 * Starts the alliance thread
	 */
	public void start() {
		System.out.println("Starting Alliance thread");
		if (t == null) {
			t = new Thread(this);
			t.start();
		}
	}

	/**
	 * Adds user to the requested alliance
	 */
	private void joinAlliance() {
		String response = user.getName() + " has not been added to " + alliance;
		System.out.println("User: " + user.getName());
		System.out.println("Alliance: " + alliance.getAlliance_name());

		if (allianceRepository.findByAlliancename(user.getName()) != null) {
			allianceRepository.addMember(alliance.getAlliance_name(), user.getName());
			response = "Successful";
		}
		sendMessage(response);
	}

	/**
	 * Creates the Alliance if the alliance does not exist
	 */
	private void createAlliance() {
		String response = "Alliance " + alliance + " already exists";
		System.out.println("User: " + user.getName());
		System.out.println("Alliance: " + alliance.getAlliance_name());

		if (allianceRepository.findByAlliancename(user.getName()) == null) {
			allianceRepository.createAlliance(alliance.getAlliance_name(), user.getName());
			response = "Successful";
		}
		sendMessage(response);
	}

	/**
	 * Sends the given message to the associated session
	 * 
	 * @param message
	 */
	public void sendMessage(String message) {
		JsonObject res = new JsonObject();
		res.addProperty("jsonOrigin", 0);
		res.addProperty("jsonType", ServerJsonType.ALLIANCE.ordinal());

		if (type == ClientJsonType.ALLIANCE_CREATE.ordinal()) {
			res.addProperty("createResponse", message);
		} else {
			res.addProperty("addResponse", message);
		}
		try {
			System.out.println("Alliance Data: " + res.toString());
			client.sendMessage(new TextMessage(res.toString()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Alliance message sent, ending alliance thread");
	}
}