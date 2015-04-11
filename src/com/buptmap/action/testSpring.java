package com.buptmap.action;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class testSpring {
	public static void main(String args[]){
		invokeBySpring();
	}
	public static void invokeBySpring(){
		ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
		TestPalceAction test = (TestPalceAction) context.getBean("testPalceAction",TestPalceAction.class);
		test.all();
		System.exit(0);
	}
}
