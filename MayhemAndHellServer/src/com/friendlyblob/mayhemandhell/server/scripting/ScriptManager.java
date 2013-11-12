package com.friendlyblob.mayhemandhell.server.scripting;

public abstract class ScriptManager<S extends Script> {
	
	public boolean reload(S script) {
		return script.reload();
	}
	
	public boolean unload(S script) {
		return script.unload();
	}
	
	public void setActive(S script, boolean status) {
		script.setActive(status);
	}

	public abstract String getScriptManagerName();
	
}
