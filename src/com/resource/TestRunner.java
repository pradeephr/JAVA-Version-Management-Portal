package com.resource;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

public class TestRunner {
	public static void main(String[] args) {
		JUnitCore j= new JUnitCore();
		Result res=j.run(SampleTest.class);
		System.out.println("res is  "+res.wasSuccessful());
	}
}
