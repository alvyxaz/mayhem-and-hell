package com.friendlyblob.mayhemandhell.server.scripting;

import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleScriptContext;

import com.friendlyblob.mayhemandhell.server.model.quests.QuestManager;

public class ScriptEngineAdapter {
	public static final File SCRIPT_FOLDER = new File("data/scripts");
	
	private static ScriptEngineAdapter instance;
	
	private ScriptEngine engine;

	public ScriptEngineAdapter() {
		ScriptEngineManager manager = new ScriptEngineManager();
		engine = manager.getEngineByName("groovy");
		
		manager.getEngineFactories().get(0).getEngineName();
	}
	
	public void executeScript(File file) throws Exception {
		if (engine instanceof Compilable) {
			ScriptContext newContext = new SimpleScriptContext();
			Bindings engineScope = newContext.getBindings(ScriptContext.ENGINE_SCOPE);
			engineScope.put("scriptFilePath", file.getPath());
			
			Compilable compilableEngine = (Compilable) this.engine;
			CompiledScript compiledScript = compilableEngine.compile(new FileReader(file.getPath()));
			compiledScript.eval(engineScope);
		} else {
			engine.eval(new FileReader(file.getPath()));
		}
	}
	
	public void executeScriptList(File file) throws Exception{
		BufferedReader input = new BufferedReader(new FileReader(file));
		String line;
		while((line = input.readLine()) != null) {
			line = line.trim();
			
			// If not a comment line
			if (line.length() > 0 && line.charAt(0) != '#') {
				executeScript(new File("data/scripts/"+line));
			}
			
		}
		input.close();
	}
	
	public static void initialize() {
		instance = new ScriptEngineAdapter();
	}
	
	public static ScriptEngineAdapter getInstance() {
		return instance;
	}
}
