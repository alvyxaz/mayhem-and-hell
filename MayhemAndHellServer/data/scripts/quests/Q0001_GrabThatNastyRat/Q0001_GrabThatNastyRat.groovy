import com.friendlyblob.mayhemandhell.server.model.quests.*;
import com.friendlyblob.mayhemandhell.server.model.actors.*;
import java.io.File;

public class Q0001_GrabThatNastyRat extends Quest{
	
	final RATS_KILLED = "rats";
	final RATS_REQUIRED = 1;
	
	// NPC id's
	final SMITH = 100;
	final RAT = 1;
	
	def Q0001_GrabThatNastyRat(int questId, String name, String scriptFilePath) {
		super(questId, name, scriptFilePath);
		
		setStartDialog(0);
		setCompleteDialog(1);
		
		addStartNpc(SMITH);
		addKillNpc(RAT);
	}
	
	def String onQuestStarted(Player player, QuestState state) {
		state.setInt(RATS_KILLED, 0);
		return null;
	}
	
	def String onKill(GameCharacter target, Player killer) {
		QuestState state =  killer.getQuestState(getQuestId());
		
		if (state != null && !state.isTurnIn() && !state.isCompleted()) {
			int ratsKilled = state.getInt(RATS_KILLED, 0) +1;
			
			killer.sendEventNotification("Rats killed (" + ratsKilled + "/" + RATS_REQUIRED + ")");
			
			if (ratsKilled >= RATS_REQUIRED) {
				state.setTurnIn();
			}
			state.setInt(RATS_KILLED, ratsKilled);
		}
		
		return null;
	}
	
}

QuestManager.getInstance().addQuest(
	new Q0001_GrabThatNastyRat(5, "Grab That Nasty Rat!", scriptFilePath))