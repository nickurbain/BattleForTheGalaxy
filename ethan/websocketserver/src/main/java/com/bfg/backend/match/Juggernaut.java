package com.bfg.backend.match;

import com.bfg.backend.enums.MatchType;

public class Juggernaut extends AbstractMatch {
	
	/*
	 * Assigns the jugg to the first player to join the match
	 * Tracks
	 */
	public Juggernaut() {
		setMatchType(MatchType.JUGGERNAUT);
	}

}
