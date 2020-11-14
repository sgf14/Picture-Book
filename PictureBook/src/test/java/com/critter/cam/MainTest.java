package com.critter.cam;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class MainTest {
	
	@Test
	void isJunitWorking() {    	
        assertEquals("hello", Main.introduction("hi"));
    }

}
