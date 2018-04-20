package com.bfg.backend;

import com.bfg.backend.enums.MatchType;
import com.bfg.backend.match.AbstractMatch;

public class JuggernautTest extends MatchTest {

	MatchType type = MatchType.JUGGERNAUT;
	AbstractMatch m;
	
	@Override 
	public void init() {
		super.setType(type);
		super.init();
		m = super.getMatch();
	}
	
	
}
