package com.bfg.backend.match;

import com.bfg.backend.enums.MatchType;

public class MatchFactory {
	
	public MatchFactory() {}
	
	/**
	 * Returns an abstract match build to the type specified.
	 * 
	 * @param type, an integer which corresponds to a match type in the MatchType enum
	 * @return a built abstract match gametype
	 */
	public AbstractMatch buildMatch(MatchType match) {
	
		System.out.println("Match Type in MatchFactory: " + match);
		AbstractMatch m;
		
		switch(match) {
			case ALLOUTDEATHMATCH:
				m = new AllOutDeathmatch();
				return m;
			case TEAMDEATHMATCH:
				m = new TeamDeathmatch();
				return m;
			default:
				return null;
		}
	}
}
