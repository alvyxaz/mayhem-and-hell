package com.friendlyblob.mayhemandhell.server.actions;

public class GameActions {

	public static GameAction[] friendlyNpc = new GameAction[] {
		GameAction.TALK
	};
	
	public static GameAction[] friendlyPlayer = new GameAction[]{
		GameAction.FOLLOW,
		GameAction.TRADE,
		GameAction.WHISPER
	};
	
	public static GameAction[] hostileNpc = new GameAction[] {
		GameAction.ATTACK,
	};
	
	public static GameAction[] item = new GameAction[] {
		GameAction.PICK_UP,
	};
	
	public static GameAction[] resource = new GameAction[] {
		GameAction.GATHER,
		GameAction.INSPECT
	};
	
	public static enum GameAction {
		ATTACK("Attack"),
		TRADE("Trade"),
		WHISPER("Whisper"),
		FOLLOW("Follow"),
		TALK("Talk"),
		PICK_UP("Pick up"),
		GATHER("Gather"),
		MINE("Mine"),
		INSPECT("Inspect");
		
		private String name;
		
		private GameAction(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
	}
}
