package controllers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controllers.*;
import battle.galaxy.*;
import data.*;
import entities.*;

class DataControllerTest {
	
	BattleForTheGalaxy game = new BattleForTheGalaxy();
	DataController dc = new DataController(game);
	JsonHeader[] originClientData = new JsonHeader[] {new PlayerData(1, JsonHeader.TYPE_PLAYER), 
			new ProjectileData(1, JsonHeader.TYPE_PROJECTILE), new HitData(1, JsonHeader.TYPE_HIT), new CoreData(1, JsonHeader.TYPE_CORE_UPDATE)};
	JsonHeader[] originServerData = new JsonHeader[] {new JuggernautData(0, JsonHeader.SELECT_JUGGERNAUT, 12, 9),
			new PlayerDisconnectData(0, JsonHeader.PLAYER_DISCONNECT, 9), new GenericData(0, JsonHeader.TYPE_MATCH_END),
			new GenericData(0, JsonHeader.TYPE_MATCH_STATS, "TestStats")};

	@BeforeEach
	void setUp() throws Exception {
		dc.setupWebSocket(true);	//Set up the WebSocket for testing (Connect locally)
		//Give the DC a matchID for comparisons during parsing
		NewMatchData nmd = new NewMatchData(12, 1, 5);  //New Match
		dc.newRawData(dc.getJsonController().dataToJson(nmd));
		dc.parseRawData();
		assertEquals(dc.getMatchId(), 12); //Check that the NewMatch actually worked
	}

	@Test
	void testParseOriginServer() {
		for(JsonHeader data: originServerData) {
			String msg = dc.getJsonController().dataToJson(data);
			int type = data.getJsonType();
			dc.newRawData(msg);
			dc.parseRawData();
			try {
				JsonHeader recieved = (JsonHeader) dc.getRxFromServer().get(0);
				dc.getRxFromServer().clear();
				assertEquals(type, recieved.getJsonType());
			} catch (ClassCastException e) {
				System.out.println("Unknown Data type recieved from Server: " + dc.getRxFromServer().get(0));
			}
		}
	}
	
	@Test
	void testParseOriginClient() {
		//Check all data types
		for(JsonHeader data: originClientData) {
			String msg = dc.getJsonController().dataToJson(data);
			int type = data.getJsonType();
			dc.newRawData(msg);
			dc.parseRawData();
			try {
				JsonHeader recieved = (JsonHeader) dc.getRxFromServer().get(0);
				dc.getRxFromServer().clear();
				assertEquals(type, recieved.getJsonType());
			} catch (ClassCastException e) {
				System.out.println("Unknown Data type recieved from Server: " + dc.getRxFromServer().get(0));
			}
		}
		//Check ChatData
		ChatData cd = new ChatData(1, JsonHeader.C_TYPE_MESSAGE, "test", "test");
		dc.newRawData(dc.getJsonController().dataToJson(cd));
		dc.parseRawData();
		String str = dc.getChatDataFromServer().get(0);
		assertEquals("test", str);
	}
}
