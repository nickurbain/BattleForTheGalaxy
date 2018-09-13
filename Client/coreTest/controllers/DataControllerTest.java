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

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		
	}

	@BeforeEach
	void setUp() throws Exception {
		dc.setupWebSocket(true);
	}

	@Test
	void testParseOriginServer() {
		//Create types of data that could be sent from the server.
		NewMatchData nmd = new NewMatchData(12, 1, 5);  //New Match
		dc.newRawData(dc.getJsonController().dataToJson(nmd));
		String endMatch = "{jsonOrigin:0,jsonType:" + JsonHeader.TYPE_MATCH_END + "}"; //End Match
		dc.newRawData(endMatch);
		dc.parseRawData();
		assertEquals(dc.getMatchId(), 12);
	}
	
	@Test
	void testParseOriginClient() {
		//Give the DataController a MatchID to check against for data types
		NewMatchData nmd = new NewMatchData(12, 1, 5);  //New Match
		dc.newRawData(dc.getJsonController().dataToJson(nmd));
		dc.parseRawData();
		//Check all data types
		for(JsonHeader data: originClientData) {
			String msg = dc.getJsonController().dataToJson(data);
			int type = data.getJsonType();
			dc.newRawData(msg);
			dc.parseRawData();
			JsonHeader recieved = (JsonHeader) dc.getRxFromServer().get(0);
			dc.getRxFromServer().clear();
			assertEquals(type, recieved.getJsonType());
		}
		//Check ChatData
		ChatData cd = new ChatData(1, JsonHeader.C_TYPE_MESSAGE, "test", "test");
		dc.newRawData(dc.getJsonController().dataToJson(cd));
		dc.parseRawData();
		String str = dc.getChatDataFromServer().get(0);
		assertEquals("test", str);
	}
}
