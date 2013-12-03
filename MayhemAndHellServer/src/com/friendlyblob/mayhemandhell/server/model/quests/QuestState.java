package com.friendlyblob.mayhemandhell.server.model.quests;

import java.util.Map;

import com.friendlyblob.mayhemandhell.server.model.actors.Player;
import com.friendlyblob.mayhemandhell.server.network.packets.server.UpdateCharacterHint;

import javolution.util.FastMap;

public class QuestState {
	private int questId;
	
	private Map<String, String> values;
	
	private QuestProgressState state;
	
	private Player player;
	
	public QuestState(Quest quest, Player player) {
		this.player = player;
		values = new FastMap<>();
		questId = quest.getQuestId();
		setState(QuestProgressState.STARTED);
	}
	
	public void setTurnIn() {
		setState(QuestProgressState.TURN_IN);
	}
	
	public void setCompleted() {
		setState(QuestProgressState.COMPLETED);
	}
	
	public void setState(QuestProgressState state) {
		this.state = state;
		player.sendPacket(new UpdateCharacterHint(player.getRegion().getVisibleCharacters(), player));
	}
	
	public boolean isTurnIn() {
		return state == QuestProgressState.TURN_IN;
	}
	
	public boolean isCompleted() {
		return state == QuestProgressState.COMPLETED;
	}
	
	public int getQuestId() {
		return questId;
	}
	
	private String getValue(String key) {
		return values.get(key);
	}
	
	private void setValue(String key, String value) {
		values.put(key, value);
	}
	
	private void setInt(String key, int value) {
		values.put(key, Integer.toString(value));
	}
	
	private int getInt(String key, int defaultValue) {
		try {
			return Integer.parseInt(getValue(key));
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	private enum QuestProgressState {
		STARTED(0),
		TURN_IN(1),
		COMPLETED(2);
		
		private int value;
		
		QuestProgressState(int value) {
			this.value = value;
		}
	}
	
}
