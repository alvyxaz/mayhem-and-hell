package com.friendlyblob.mayhemandhell.server.actions;

public class GameActions {

	public static GameAction[] friendlyPlayer = new GameAction[]{
		GameAction.FOLLOW,
		GameAction.TRADE,
		GameAction.WHISPER
	};
	
	public static GameAction[] hostineNpc = new GameAction[] {
		GameAction.FOLLOW,
	};
	
	public static enum GameAction {
		ATTACK("Attack"),
		TRADE("Trade"),
		WHISPER("Whisper"),
		FOLLOW("Follow");
		
		private String name;
		
		private GameAction(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
	}
}
