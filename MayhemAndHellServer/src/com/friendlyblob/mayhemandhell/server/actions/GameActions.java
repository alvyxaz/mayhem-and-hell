package com.friendlyblob.mayhemandhell.server.actions;

public class GameActions {

	public static GameAction[] friendlyPlayer = new GameAction[]{
		GameAction.TRADE,
		GameAction.WHISPER
	};
	
	public static GameAction[] hostineNpc = new GameAction[] {
		GameAction.ATTACK,
	};
	
	public static enum GameAction {
		ATTACK("Attack"),
		TRADE("Trade"),
		WHISPER("Whisper");
		
		private String name;
		
		private GameAction(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
	}
}
