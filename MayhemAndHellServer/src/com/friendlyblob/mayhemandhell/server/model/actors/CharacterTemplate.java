package com.friendlyblob.mayhemandhell.server.model.actors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javolution.util.FastMap;

import com.friendlyblob.mayhemandhell.server.ai.AiData;
import com.friendlyblob.mayhemandhell.server.model.quests.Quest;
import com.friendlyblob.mayhemandhell.server.model.quests.Quest.QuestEventType;
import com.friendlyblob.mayhemandhell.server.model.stats.StatsSet;

// TODO try to implement values as final
public class CharacterTemplate {
	private static final int DEFAULT_SPRITE = 0;
	private int sprite = DEFAULT_SPRITE;
	
	// Vital stats
	private int baseMaxHealth;
	private int baseMaxMana;
	private int baseMaxEnergy;
	private int baseMaxWeight;
	
	// Movement
	private int baseWalkingSpeed;
	private int baseRunningSpeed;
	
	// Core stats
	private int baseStrength;
	private int baseDexterity;
	private int baseIntelligence;
	private int baseVitality;
	private int baseToughness;
	
	// Main damage
	private int basePhysicalDamage;
	private int baseMagicDamage;
	private int baseRangeDamage;
	
	// Main defence
	private int basePhysicalDefence;
	private int baseMagicalDefence;
	private int baseRangeDefence;
	
	// Elemental resistance
	private int baseFireResistance;
	private int baseFrostResistance;
	private int baseElectricityResistance;
	private int basePoisonResistance;
	private int baseHolyResistance;
	
	// Elemental damage
	private int baseFireDamage;
	private int baseFrostDamage;
	private int baseElectricDamage;
	private int basePoisonDamage;
	private int baseHolyDamage;
	
	public StatsSet set;
	
	protected AiData aiData = new AiData();
	
	private final Map<QuestEventType, List<Quest>> questEvents = new FastMap<>();
	
	public CharacterTemplate(StatsSet set) {
		this.set = set;
		parseSetData();
	}
	
	/**
	 * Attaches a quest event which is called when certain action happens.
	 * @param type quest event type
	 * @param quest quest to be added
	 */
	public void addQuestEvent(QuestEventType type, Quest quest) {
		if (!questEvents.containsKey(type)) {
			List<Quest> list = new ArrayList<>();
			list.add(quest);
			questEvents.put(type, list);
		} else {
			questEvents.get(type).add(quest);
		}
	}
	
	public List<Quest> getQuestEvents(QuestEventType type) {
		return questEvents.get(type);
	}
	
	public void parseSetData() {
		baseMaxHealth = set.getInt("baseMaxHealth", 100);
		baseMaxMana = set.getInt("baseMaxMana", 100);
		baseMaxEnergy = set.getInt("baseMaxEnergy", 100);
		baseMaxWeight = set.getInt("baseMaxWeight", 100);
		
		baseWalkingSpeed = set.getInt("baseWalkingSpeed", 50);
		baseRunningSpeed = set.getInt("baseRunningSpeed", 50);
		
		baseStrength = set.getInt("baseStrength", 10);
		baseDexterity = set.getInt("baseDexterity", 10);
		baseIntelligence = set.getInt("baseIntelligence", 10);
		baseVitality = set.getInt("baseVitality", 10);
		baseToughness = set.getInt("baseToughness", 10);
		
		basePhysicalDamage = set.getInt("basePhysicalDamage", 10);
		baseMagicDamage = set.getInt("baseMagicDamage", 10);
		baseRangeDamage = set.getInt("baseRangeDamage", 10);
		
		basePhysicalDefence = set.getInt("basePhysicalDefence", 0);
		baseMagicalDefence = set.getInt("baseMagicalDefence", 0);
		baseRangeDefence = set.getInt("baseRangeDefence", 0);
		
		baseFireResistance = set.getInt("baseFireResistance", 0);
		baseFrostResistance = set.getInt("baseFrostResistance", 0);
		baseElectricityResistance = set.getInt("baseElectricityResistance", 0);
		basePoisonResistance = set.getInt("basePoisonResistance", 0);
		baseHolyResistance = set.getInt("baseHolyResistance", 0);
		
		baseFireDamage = set.getInt("baseFireDamage", 0);
		baseFrostDamage = set.getInt("baseFrostDamage", 0);
		baseElectricDamage = set.getInt("baseElectricDamage", 0);
		basePoisonDamage = set.getInt("basePoisonDamage", 0);
		baseHolyDamage = set.getInt("baseHolyDamage", 0);
	}
	
	public void setSprite(int sprite) {
		this.sprite = sprite;
	}
	
	public int getSprite() {
		return this.sprite;
	}
	
	/**
	 * @return the baseMaxHealth
	 */
	public int getBaseMaxHealth() {
		return baseMaxHealth;
	}

	/**
	 * @return the baseMaxMana
	 */
	public int getBaseMaxMana() {
		return baseMaxMana;
	}

	/**
	 * @return the baseMaxEnergy
	 */
	public int getBaseMaxEnergy() {
		return baseMaxEnergy;
	}

	/**
	 * @return the baseMaxWeight
	 */
	public int getBaseMaxWeight() {
		return baseMaxWeight;
	}

	/**
	 * @return the baseWalkingSpeed
	 */
	public int getBaseWalkingSpeed() {
		return baseWalkingSpeed;
	}

	/**
	 * @return the baseRunningSpeed
	 */
	public int getBaseRunningSpeed() {
		return baseRunningSpeed;
	}

	/**
	 * @return the baseStrength
	 */
	public int getBaseStrength() {
		return baseStrength;
	}

	/**
	 * @return the baseDexterity
	 */
	public int getBaseDexterity() {
		return baseDexterity;
	}

	/**
	 * @return the baseIntelligence
	 */
	public int getBaseIntelligence() {
		return baseIntelligence;
	}

	/**
	 * @return the baseVitality
	 */
	public int getBaseVitality() {
		return baseVitality;
	}

	/**
	 * @return the baseToughness
	 */
	public int getBaseToughness() {
		return baseToughness;
	}

	/**
	 * @return the basePhysicalDamage
	 */
	public int getBasePhysicalDamage() {
		return basePhysicalDamage;
	}

	/**
	 * @return the baseMagicDamage
	 */
	public int getBaseMagicDamage() {
		return baseMagicDamage;
	}

	/**
	 * @return the baseRangeDamage
	 */
	public int getBaseRangeDamage() {
		return baseRangeDamage;
	}

	/**
	 * @return the basePhysicalDefence
	 */
	public int getBasePhysicalDefence() {
		return basePhysicalDefence;
	}

	/**
	 * @return the baseMagicalDefence
	 */
	public int getBaseMagicalDefence() {
		return baseMagicalDefence;
	}

	/**
	 * @return the baseRangeDefence
	 */
	public int getBaseRangeDefence() {
		return baseRangeDefence;
	}

	/**
	 * @return the baseFireResistance
	 */
	public int getBaseFireResistance() {
		return baseFireResistance;
	}

	/**
	 * @return the baseFrostResistance
	 */
	public int getBaseFrostResistance() {
		return baseFrostResistance;
	}

	/**
	 * @return the baseElectricityResistance
	 */
	public int getBaseElectricityResistance() {
		return baseElectricityResistance;
	}

	/**
	 * @return the basePoisonResistance
	 */
	public int getBasePoisonResistance() {
		return basePoisonResistance;
	}

	/**
	 * @return the baseHolyResistance
	 */
	public int getBaseHolyResistance() {
		return baseHolyResistance;
	}

	/**
	 * @return the baseFireDamage
	 */
	public int getBaseFireDamage() {
		return baseFireDamage;
	}

	/**
	 * @return the baseFrostDamage
	 */
	public int getBaseFrostDamage() {
		return baseFrostDamage;
	}

	/**
	 * @return the baseElectricDamage
	 */
	public int getBaseElectricDamage() {
		return baseElectricDamage;
	}

	/**
	 * @return the basePoisonDamage
	 */
	public int getBasePoisonDamage() {
		return basePoisonDamage;
	}

	/**
	 * @return the baseHolyDamage
	 */
	public int getBaseHolyDamage() {
		return baseHolyDamage;
	}
	
	public AiData getAiData() {
		return aiData;
	}
	
}
