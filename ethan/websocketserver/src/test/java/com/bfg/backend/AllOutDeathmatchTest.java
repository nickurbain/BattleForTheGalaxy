package com.bfg.backend;

import com.bfg.backend.enums.MatchType;
import com.bfg.backend.match.AbstractMatch;

public class AllOutDeathmatchTest extends MatchTest {
	MatchType type = MatchType.ALLOUTDEATHMATCH;
	AbstractMatch m;
	
	@Override 
	public void init() {
		super.setType(type);
		super.init();
		m = super.getMatch();
	}
}
