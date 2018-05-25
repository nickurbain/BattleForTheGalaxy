package com.bfg.backend;

import org.junit.Test;

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
	
	
//	@Test
//	public void testTimer() {
//		init();
//		
//		m.startTimer(10);
//		System.out.println(m.getTime());
//	}
}
