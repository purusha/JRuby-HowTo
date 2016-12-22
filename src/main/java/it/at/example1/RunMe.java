package it.at.example1;

import org.apache.bsf.BSFManager;

public class RunMe {
	public static void main(String[] args) throws Exception {

		BSFManager.registerScriptingEngine("ruby", "org.jruby.javasupport.bsf.JRubyEngine", new String[] { "rb" });
		
		final BSFManager manager = new BSFManager();
		
		/*
		 * 	evaluate simple template using ERB
		 */
		
		Object result = manager.eval("ruby", "<script>", -1, -1, 
			"require 'erb'\n" +
			"ERB.new(\"" + "<%= 42 * 3.14 %>" + "\").result(binding)"
		);		
		
		System.out.println(result);
		
		/*
		 * create one class and instantiate any times
		 */
		
		manager.exec("ruby", "<script>", -1, -1, "\n" +
			"class MyBean\n" +
			"end"
		);
		
		for(int x = 0; x < 3; x++) {
			Object o = manager.eval("ruby", "<script>", -1, -1, "MyBean.new");			
			
			System.out.println(o);
		}

	}
}
