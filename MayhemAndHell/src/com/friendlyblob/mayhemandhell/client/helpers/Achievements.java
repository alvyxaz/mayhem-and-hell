package com.friendlyblob.mayhemandhell.client.helpers;

import java.util.HashMap;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.friendlyblob.mayhemandhell.client.GoogleInterface;

public class Achievements {

	private static Preferences prefs;
	private static HashMap<String, Achievement> achievements;
	
	private static GoogleInterface google;
	
	public static void initialize(GoogleInterface googleService){
		prefs = Gdx.app.getPreferences("achievements");
		achievements = new HashMap<String,Achievement>();
		google = googleService;
		
		XmlReader reader = new XmlReader();
		try {
			Element achievementsXml = reader
					.parse(Gdx.files.local("data/achievements.xml"));
			
			for(int i = 0; i < achievementsXml.getChildCount(); i++){
				achievements.put(achievementsXml.getChild(i).get("id"),
						new Achievement(achievementsXml.getChild(i)));
			}
			
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public static void pushOnline(){
		if(google.isSignedIn()){
			for (Entry<String, Achievement> entry : achievements.entrySet()) {
				entry.getValue().pushOnline();
			}
		}
	}
	
	public static void notifyUnlocked(Achievement achievement){
		
	}
	
	public static Achievement get(String id){
		Achievement ach = achievements.get(id);
		if(ach == null){
			System.err.print("No achievement: " + id);
		}
		return ach;
	}
	 
	static public class Achievement {
		 
		public static final int LOCKED = 0;
		public static final int OFFLINE = 1;
		public static final int ONLINE = 2;
		
		private String id;
		private String title;
		private String description;
		private String googleId;
		private int steps;
		private int maxSteps;
		
		public int status;
		
		public Achievement(Element xml){
			this.id = xml.getAttribute("id");
			this.title = xml.getAttribute("title");
			this.description = xml.getAttribute("description", "");
			this.googleId = xml.getAttribute("googleid");
			this.maxSteps = xml.getIntAttribute("steps", 1);
			
			this.status = prefs.getInteger(id, Achievement.LOCKED);
		}
		
		public void setStatus(int status){
			this.status = status;
			prefs.putInteger(id, status);
			prefs.flush();
		}
		
		/*
		 * Unlock achievement
		 */
		public void unlock(){
			if(isUnlocked()) return;
			
			if(google.isSignedIn()){
				pushOnline();
			} else {
				setStatus(OFFLINE);
			}
			
			notifyUnlocked(this);
		}

		public void pushOnline(){
			if(status == ONLINE) return;
			
			// Is incremental
			if(maxSteps > 1){
				int stepsOnline = prefs.getInteger(id+"stepsOnline", 0);
				int onlineDifference = this.steps - stepsOnline;
				
				// Saving new online data
				prefs.putInteger(id+"stepsOnline", stepsOnline + onlineDifference);
				prefs.flush();
				
				google.incrementAchievements(onlineDifference, id, title);
			} else {
				google.unlockAchievement(id, title);
			}
			
			setStatus(ONLINE);
		}
		
		/*
		 * Increment by a given number of steps
		 */
		public void increment(int steps){
			if(isUnlocked()) return;
			
			this.steps += steps;
			prefs.putInteger(id+"steps", this.steps);
			prefs.flush();
			
			pushOnline();
			
			if(this.steps >= maxSteps){
				if(!google.isSignedIn()){
					setStatus(OFFLINE);
				} 
				notifyUnlocked(this);
			}
		}
		
		public boolean isUnlocked(){
			return status != LOCKED;
		}
		
		/*
		 * Increment to a given number
		 */
		public void incrementTo(int steps){
			if(isUnlocked()) return;
			
			if(this.steps < steps){
				int difference = steps - this.steps;
				this.steps = steps;
				increment(difference);
			}
		}
		
	}
	
}
