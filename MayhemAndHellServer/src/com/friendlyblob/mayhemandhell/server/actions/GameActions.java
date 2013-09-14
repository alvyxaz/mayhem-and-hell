package com.friendlyblob.mayhemandhell.server.actions;

public class GameActions {

	public static GameAction [] onPlayer = new GameAction[]{
		GameAction.ATTACK,
		GameAction.TRADE,
		GameAction.WHISPER
	};
	
	public static enum GameAction {
		ATTACK("Attack"),
		TRADE("Trade"),
		WHISPER("Whisper"),
		USE_SKILL("UseSkill");
		
		private String title;
		
		private GameAction(String title) {
			this.title = title;
		}
	}
}
