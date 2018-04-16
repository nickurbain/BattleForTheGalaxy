package com.bfg.backend.match;

import com.bfg.backend.enums.MatchType;

/**
 * Factory for creating different match types
 * 
 * @author emball
 *
 */
public class MatchFactory {
	
	/**
	 * Constructor
	 */
	public MatchFactory() {}
	
	/**
	 * Returns an abstract match built to the subtype type specified.
	 * 
	 * @param type, an integer which corresponds to a match type in the MatchType enum
	 * @return a built abstract match gametype
	 */
	public AbstractMatch buildMatch(MatchType match) {
	
		System.out.println("Match Type in MatchFactory: " + match); // TODO
		
		switch(match) {
			case ALLOUTDEATHMATCH:
				return new AllOutDeathmatch();
			case TEAMDEATHMATCH:
				return new TeamDeathmatch();
			case CAPTURETHEFLAG:
				return new CaptureTheFlag();
			case ALLIANCEDEATHMATCH:
				return new AllianceDeathmatch();
			case JUGGERNAUT:
				return new Juggernaut();
			default:
				return null;
		}
	}
}