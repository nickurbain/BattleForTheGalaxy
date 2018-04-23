package com.bfg.backend.enums;

/**
 * Used to set the jsonType for messages we send to clients.
 * 
 * @author emball
 *
 */
public enum ServerJsonType {
	LOGIN, 			// 0
	FRIEND_DATA,	// 1
	STATISTICS, 	// 2
	SHIP_DATA, 		// 3
	GAME_OVER, 		// 4
	MATCH_STATS, 	// 5
	NEW_MATCH,		// 6
	REGISTRATION,	// 7
	ALLIANCE,		// 8
	PICK_JUGGERNAUT,// 9
	TIME,			// 10
}
