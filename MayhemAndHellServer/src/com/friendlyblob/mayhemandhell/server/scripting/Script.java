package com.friendlyblob.mayhemandhell.server.scripting;

import java.io.File;

/**
 * Represents a script file.
 * @author Alvys
 *
 */
public abstract class Script {

	private File scriptFile;
	private boolean active;
	
	public boolean reload() {
		try {
			ScriptEngineAdapter.getInstance().executeScript(scriptFile);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public abstract boolean unload();
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public void setScriptFile(File scriptFile) {
		this.scriptFile = scriptFile;
	}
	
	public File getScriptFile() {
		return scriptFile;
	}
	
	public abstract String getScriptName();
	
	public abstract ScriptManager getScriptManager();
}
