package com.critter.cam;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class MainTest {
	
	@Test
	void isJunitWorking() {    	
        assertTrue(true);        
    }
	
	@Test
	void helloTest() {    	
        assertEquals("hello to you", Main.introduction("hi"));
    }

}
