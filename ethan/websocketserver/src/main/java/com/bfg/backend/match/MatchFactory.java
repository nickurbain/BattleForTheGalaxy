package com.bfg.backend.match;

import com.bfg.backend.enums.MatchType;

public class MatchFactory {
	
	/**
	 * Returns an abstract match build to the type specified.
	 * 
	 * @param type, an integer which corresponds to a match type in the MatchType enum
	 * @return a built abstract match gametype
	 */
	public AbstractMatch buildMatch(Integer type) {
	
		MatchType match = MatchType.valueOf(type.toString());	
		AbstractMatch m;
		
		switch(match) {
			case AllOutDeathmatch:
				m = new AllOutDeathmatch();
				return m;
			case TeamDeathmatch:
				m = new TeamDeathmatch();
				return m;
			default:
				return null;
		}
	}
	
}
