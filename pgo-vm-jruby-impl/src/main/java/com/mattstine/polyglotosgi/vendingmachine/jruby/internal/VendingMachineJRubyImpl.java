package com.mattstine.polyglotosgi.vendingmachine.jruby.internal;

import org.jruby.embed.ScriptingContainer;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.mattstine.polyglotosgi.vendingmachine.api.VendingMachine;

public class VendingMachineJRubyImpl extends ExampleActivator {
	
	protected VendingMachine getJRubyImpl() throws Exception {
		StringBuffer fileData = new StringBuffer();
		BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass()
			.getClassLoader().getResourceAsStream("JRubyVendingMachine.rb")));
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
		  fileData.append(readData);
		  buf = new char[1024];
		}
		reader.close();
		
		ScriptingContainer container = new ScriptingContainer();
		return (VendingMachine) container.runScriptlet(fileData.toString());
	}
	
}