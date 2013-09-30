package com.friendlyblob.mayhemandhell.server.data;

import java.lang.reflect.InvocationTargetException;

import org.w3c.dom.Node;

import com.friendlyblob.mayhemandhell.server.model.stats.StatsSet;

public class DataParser {
	/**
	 * Parses a set element, and adds it to item set
	 * @param n
	 * @param set
	 * @throws InvocationTargetException
	 */
	public void parseSetPair(Node n, StatsSet set)  throws InvocationTargetException {
		String name = n.getAttributes().getNamedItem("name").getNodeValue().trim();
		String value = n.getAttributes().getNamedItem("val").getNodeValue().trim();
		char ch = value.isEmpty() ? ' ' : value.charAt(0);
		
		if (Character.isDigit(ch)) {
			set.set(name, String.valueOf(value));
		} else {
			set.set(name, value);
		}
	}
}
