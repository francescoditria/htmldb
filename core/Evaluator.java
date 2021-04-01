package core;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Evaluator {

	public String evaluation(String expr,String[] userVar) throws ScriptException
	{

		ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
		//String userVar[] = {"a = 1", "b = 3"};

		/*
		for (String s : userVar) {
		    engine.eval(s);
		}
		*/

		//String expr = "a < b";
		Object b=(Object) engine.eval(expr);
	    //System.out.println(b.toString());
		return b.toString();

	}
	
}
