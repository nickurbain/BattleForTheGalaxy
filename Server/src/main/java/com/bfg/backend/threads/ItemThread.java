package com.bfg.backend.threads;

import org.springframework.web.socket.WebSocketSession;

import com.bfg.backend.enums.ClientJsonType;
import com.bfg.backend.model.Items;
import com.bfg.backend.repository.ItemRepository;
import com.bfg.backend.repository.UserRepository;

public class ItemThread extends Thread {
	
	ItemRepository itemRepository;
	UserRepository userRepository;
	
	private WebSocketSession client;
	private Thread t;
	private Items item;
	private String userName;
	private int type;

	
	/**
	 * Constructor for initializing an ItemThread
	 * 
	 * @param itemRepository
	 * 			The repository to query
	 * @param item
	 * 			The item to retrieve
	 * @param userName
	 * 			The User who is querying an item
	 * @param client
	 * 			A websocket session for sending the response to
	 * @param type
	 * 			Determines what to do with an item
	 */
	public ItemThread(ItemRepository itemRepository, UserRepository userRepository, Items item, String userName,  WebSocketSession client, int type) {
		this.userRepository = userRepository;
		this.client = client;
		this.item = item;
		this.userName = userName;
		this.type = type;
	}
	
	/**
	 * Starts the item thread
	 */
	public void start() {
		System.out.println("Starting Item thread");
		if (t == null) {
			t = new Thread(this);
			t.start();
		}
	}
	
	@Override
	public void run() {
		//TODO
	}
	
	public void getInventory() { 
		//TODO
	}
	
	public void getEquipped() {
		//TODO
	}
	
	public void equipItem() {
		//TODO
	}

}
