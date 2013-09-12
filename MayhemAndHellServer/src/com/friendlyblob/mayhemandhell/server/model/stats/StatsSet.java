package com.friendlyblob.mayhemandhell.server.model.stats;

import java.util.Map;
import java.util.logging.Level;

import javolution.util.FastMap;

/**
 * StatsSet represents a set of stats entity has and it's values.
 * @author Alvys
 */
public class StatsSet {
	
	private Map<String, Object> set;
	
	public StatsSet() {
		this(new FastMap<String, Object>());
	}
	
	public StatsSet(Map<String, Object> map) {
		set = map;
	}
	
	/**
	 * Return the boolean associated to the key put in parameter ("name")
	 * @param name : String designating the key in the set
	 * @return boolean : value associated to the key
	 */
	public boolean getBool(String name) {
		Object val = set.get(name);
		
		if (val == null) {
			throw new IllegalArgumentException("Boolean value required, but not specified");
		}
		
		if (val instanceof Boolean) {
			return ((Boolean) val).booleanValue();
		} 
		
		try {
			return Boolean.parseBoolean((String) val);
		} catch (Exception e) {
			throw new IllegalArgumentException("Boolean value required, but found: " + val);
		}
	}
	
	/**
	 * Return the boolean associated to the key put in parameter ("name"). If the value associated to the key is null, this method returns the value of the parameter deflt.
	 * @param name : String designating the key in the set
	 * @param deflt : boolean designating the default value if value associated with the key is null
	 * @return boolean : value of the key
	 */
	public boolean getBool(String name, boolean deflt) {
		Object val = set.get(name);
		if (val == null) {
			return deflt;
		} 
		
		if (val instanceof Boolean) {
			return ((Boolean) val).booleanValue();
		} 
		
		try {
			return Boolean.parseBoolean((String) val);
		} catch (Exception e) {
			throw new IllegalArgumentException("Boolean value required, but found: " + val);
		}
	}
	
	/**
	 * Returns the int associated to the key put in parameter ("name").
	 * @param name : String designating the key in the set
	 * @return int : value associated to the key
	 */
	public int getInteger(String name) {
		final Object val = set.get(name);
		if (val == null) {
			throw new IllegalArgumentException("Integer value required, but not specified: " + name + "!");
		}
		
		if (val instanceof Number) {
			return ((Number) val).intValue();
		}
		
		try {
			return Integer.parseInt((String) val);
		} catch (Exception e) {
			throw new IllegalArgumentException("Integer value required, but found: " + val + "!");
		}
	}
	
	/**
	 * Returns the int associated to the key put in parameter ("name"). If the value associated to the key is null, this method returns the value of the parameter deflt.
	 * @param name : String designating the key in the set
	 * @param deflt : int designating the default value if value associated with the key is null
	 * @return int : value associated to the key
	 */
	public int getInteger(String name, int deflt)
	{
		Object val = set.get(name);
		
		if (val == null) {
			return deflt;
		}
		
		if (val instanceof Number) {
			return ((Number) val).intValue();
		} 
		
		try {
			return Integer.parseInt((String) val);
		} catch (Exception e) {
			throw new IllegalArgumentException("Integer value required, but found: " + val);
		}
	}
	
	/**
	 * Add the String hold in param "value" for the key "name"
	 * @param name : String designating the key in the set
	 * @param value : String corresponding to the value associated with the key
	 */
	public void set(String name, String value) {
		set.put(name, value);
	}
	
	/**
	 * Add the boolean hold in param "value" for the key "name"
	 * @param name : String designating the key in the set
	 * @param value : boolean corresponding to the value associated with the key
	 */
	public void set(String name, boolean value) {
		set.put(name, value);
	}
	
	/**
	 * Add the int hold in param "value" for the key "name"
	 * @param name : String designating the key in the set
	 * @param value : int corresponding to the value associated with the key
	 */
	public void set(String name, int value) {
		set.put(name, value);
	}
	
	/**
	 * Add the double hold in param "value" for the key "name"
	 * @param name : String designating the key in the set
	 * @param value : double corresponding to the value associated with the key
	 */
	public void set(String name, double value) {
		set.put(name, value);
	}
	
	/**
	 * Add the long hold in param "value" for the key "name"
	 * @param name : String designating the key in the set
	 * @param value : double corresponding to the value associated with the key
	 */
	public void set(String name, long value) {
		set.put(name, value);
	}
	
	/**
	 * Add the Enum hold in param "value" for the key "name"
	 * @param name : String designating the key in the set
	 * @param value : Enum corresponding to the value associated with the key
	 */
	public void set(String name, Enum<?> value) {
		set.put(name, value);
	}
	
}
