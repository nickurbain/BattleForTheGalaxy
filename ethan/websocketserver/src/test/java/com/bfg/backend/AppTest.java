package com.bfg.backend;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.bfg.backend.match.AllOutDeathmatch;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
    
    @RunWith(Suite.class)
    @Suite.SuiteClasses({
    	SocketHandler.class,
    	MatchTest.class,
    	AllOutDeathmatchTest.class,
    	TeamDeathmatchTest.class,
    })

    public class FeatureTestSuite {
      // the class remains empty,
      // used only as a holder for the above annotations
    }
}
