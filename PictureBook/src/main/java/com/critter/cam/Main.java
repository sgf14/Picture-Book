package com.critter.cam;

public class Main {
	
	static String introduction(String intro) {
		if (intro.length() == 2) {
			return "hello";
		} else
			return "goodbye";
	}
	
	public static void main(String[] args) {
		System.out.println(introduction("hi"));
	}

}
