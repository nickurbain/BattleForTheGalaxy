package com.bfg.backend.match;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.bfg.backend.enums.MatchType;
import com.bfg.backend.enums.ServerJsonType;
import com.google.gson.JsonObject;

public class Juggernaut extends AbstractMatch {
	private Integer killLimit;
	private List<Team> teams;
	
	/*
	 * Assigns the jugg to the first player to join the match
	 * Tracks
	 */
	public Juggernaut() {
		setMatchType(MatchType.JUGGERNAUT);
		teams = new CopyOnWriteArrayList<>();
		teams.add(new Team(0));		// juggernaut
		teams.add(new Team(1));		// everyone else
		killLimit = 3;
		startTimer(180);
	}
	
	
//	public WebSocketSession assignJuggernaut() {
//		Random rand = new Random();
//		int newJugg = rand.nextInt(getPlayerListSize());
//		return getPlayerList().get(newJugg);
//	}
	
	
	public void assignNewJuggernaut(Player player, Player enemy) {
		player.setTeam(1);
		enemy.setTeam(0);
		enemy.setHP(200);
		
		teams.get(0).removeAllPlayers();
		teams.get(1).removePlayer(enemy);
		teams.get(1).addMember(player);
		teams.get(0).addMember(enemy);
		
		sendNewJugg(player, enemy);
		
//		// Get the player, add them to the non-jugg team, then clear the jugg team
//		List<Player> p = teams.get(0).getMembers();
//		Player oldJugg = p.get(0);
//		teams.get(1).addMember(oldJugg);
//		teams.get(0).removeAllPlayers();
//		oldJugg.setTeam(1);
//	
//		WebSocketSession jugg = assignJuggernaut();
//		
//		// Get the new juggernaut
//		WebSocketSession newJugg = getPlayerList().get(getPlayerList().indexOf(jugg));
//		p = teams.get(1).getMembers();
//		Player juggPlayer = getPlayer(newJugg);
//		p.remove(juggPlayer);
//		teams.get(0).addMember(juggPlayer);
//		juggPlayer.setTeam(0);
		
	}
	
	public void sendNewJugg(Player player, Player enemy) {
		// send out id of jugg and whoever was the last jugg
		JsonObject json = new JsonObject();
		json.addProperty("jsonOrigin", 0);
		json.addProperty("jsonType", ServerJsonType.PICK_JUGGERNAUT.ordinal());
//		int currId = teams.get(0).getMembers().get(0).getId();
		json.addProperty("currId", enemy.getId());
		json.addProperty("prevId", player.getId());
		
		System.out.println(json.toString());
		
		try {
			addMessageToBroadcast(new TextMessage(json.toString()));
		} catch (IOException e) {
			System.err.println("Error with adding message to broadcase in juggernaught: sendNewJugg()");
			System.out.println(json.toString());
		}
		
	}
	
	/**
	 * Adds a player to the match
	 */
	@Override
	public void addPlayer(WebSocketSession player) {
		super.addPlayerStd(player);
		
		Player p = getPlayer(player);
		
		if(teams.get(0).getMembers().isEmpty()) {
			teams.get(0).addMember(p);
			p.setHP(200);
			addTeamtoPlayer(p, 0);
		}
		else {
			teams.get(1).addMember(p);
			addTeamtoPlayer(p, 1);
		}
		
		super.welcomeMessageWithTeam(player, p.getTeam());
		super.addClientToBC(player);
	}
	
	
	/**
	 * Adds the team to the player
	 * 
	 * @param player
	 * @param team
	 */
	public void addTeamtoPlayer(Player player, Integer team) {
		player.setTeam(team);
	}
	
	
	@Override
	public void registerHit(Integer playerId, Integer sourceId, boolean causedDeath, Integer dmg) {
		Player player = getPlayerById(playerId);
		Player enemy = getPlayerById(sourceId);
		if(player.getTeam() == enemy.getTeam()) {	// Check for team hit
			return;
		}
		
		player.takeDmg(dmg);
		enemy.addDamageDealt(dmg);
		if (causedDeath) {
			registerKill(player, enemy);
		}
	}
	
	
	/**
	 * Registers a kill
	 * Changes juggernaut
	 */
	@Override
	public void registerKill(Player player, Player enemy) {
		// If you died as the juggernaut
		if(player.getTeam() == 0) {
			assignNewJuggernaut(player, enemy);
			player.addDeath();
		}
		else {
			super.registerKill(player, enemy);
		}

		// Add a kill to the team kills;
		if(checkEndMatch()) {
			super.endMatch();
		}
	}
	
	/**
	 * Checks if the match has ended
	 */
	@Override
	public boolean checkEndMatch() {
		List<Player> players = getPlayers();
		for(Player player: players) {			
			if(player.getKills() >= killLimit) {
				System.err.println("	KILL LIMIT REACHED! ENDING GAME. WINNER: " + player.getId());
				return true;
			}
		}
		return false;
	}

}
