package it.at;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class SimpleEvaluatorCommand {	
	private final String template;
	
	public SimpleEvaluatorCommand(String template) {				
		this.template = template;
	}
	
	public String eval(Map<String, Object> context) {		
		final StringBuffer command = new StringBuffer(
			"require 'erb' \n" + 
			"require 'ostruct' \n"
		);	
		
		command.append("os = OpenStruct.new(" +
			context.entrySet().stream()
				.map(e -> e.getKey() + ": '" + e.getValue() + "'")
				.collect(Collectors.joining(", "))		
		+ ") \n");
		
		command.append("puts ERB.new(\"" + template + "\").result(os.instance_eval { binding })");		
		
		final StringBuilder sb = new StringBuilder();
		final String[] commands = new String[]{"ruby", "-e", command.toString()};
		//System.out.println(Arrays.toString(commands));
		
		Process proc = null;
	    try {
	    	proc = Runtime.getRuntime().exec(commands);	    		        	        
	        
	        {
	        	final BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
		        String s = null;	        
		        
		        while ((s = stdError.readLine()) != null) {
		            sb.append(s);
		            sb.append("\n");
		        }
	        }
	        
	        if (sb.toString().isEmpty()) {
		        final BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		        String s = null;
		        
		        while ((s = stdInput.readLine()) != null) {
		            sb.append(s);
		            sb.append("\n");
		        }	        	
	        } else {	        	
	        	System.err.println(sb.toString());
	        	sb.setLength(0); //clear all content of stringbuffer
	        }
	    } catch (Exception e) {
	    	System.err.println(e.getMessage());
	        sb.setLength(0); //clear all content of stringbuffer
	    } finally {
	    	proc.destroy();
	    }
	    
	    return sb.toString();		
	}

	public static void main(String[] args) {		
		final SimpleEvaluatorCommand sec = new SimpleEvaluatorCommand("Name: <%= name %> <%= last %>");
		
		final Map<String, Object> context = new HashMap<>();
		context.put("name", "Alan");
		context.put("last", "Toro");
		
		System.out.println(sec.eval(context));		
	}	
}
