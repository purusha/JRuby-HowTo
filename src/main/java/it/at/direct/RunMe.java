package it.at.direct;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class RunMe {
	public static void main(String[] args) throws Exception {
		
		final ScriptEngine engine = new ScriptEngineManager().getEngineByName("ruby");
		
		//bootstrap script for f(x) setup
		engine.eval(
			n("") +
			n("def call_me(str)") +
			n("	 str * 42") +
			n("end") +
			n("")			
		);
		
		final Invocable i = (Invocable)engine;
		
		print("----#----#----");
		print(i.invokeFunction("call_me", "*"));		
		print(i.invokeFunction("call_me", "3"));		
		print(i.invokeFunction("call_me", 1.0));		
		
		print("----#----#----");
		print(i.invokeMethod(new Object(), "to_s"));
		
		print("----#----#----");
		Object mainMethods = i.invokeMethod(new Object(), "methods");
		Object methodSorted = i.invokeMethod(mainMethods, "sort");
		print(i.invokeMethod(methodSorted, "join", "\n"));
				
	}
	
	private static String n(String s) {
		return s + "\n";
	}
	
	private static void print(Object o) {
		System.err.println(String.valueOf(o));
	}
}
