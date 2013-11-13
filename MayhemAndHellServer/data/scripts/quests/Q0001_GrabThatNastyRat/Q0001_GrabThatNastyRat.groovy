import com.friendlyblob.mayhemandhell.server.model.quests.*;
import java.io.File;

public class Q0001_GrabThatNastyRat extends Quest{
	
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
}

QuestManager.getInstance().addQuest(
	new Q0001_GrabThatNastyRat(5, "Grab That Nasty Rat!", scriptFilePath))