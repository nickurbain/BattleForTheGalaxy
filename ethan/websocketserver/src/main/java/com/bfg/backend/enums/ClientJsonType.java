package com.bfg.backend.enums;

/**
 * Used to track what type of message the client sent us.
 * 
 * @author emball
 *
 */
public enum ClientJsonType {
	LOGIN,			// 0
	SHIP_DATA,		// 1
	LOCATION,		// 2
	PROJECTILE,		// 3
	HIT,			// 4
	DEATH,			// 5
	RESPAWN,		// 6
	QUIT,			// 7
	DB_FRIEND,		// 8
	DB_STATS,		// 9
	DB_SHIP,		// 10
	MATCH_STATS,	// 11
	JOIN_MATCH,		// 12
	REGISTRATION,	// 13
	TEAMDEATHMATCH,	// 14
}
