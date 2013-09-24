package com.friendlyblob.mayhemandhell.server;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ServerStatistics {
	public static final boolean TRACK = true;
	
	public static boolean online = false;
	
	public final static long uptimeSince = System.currentTimeMillis();
	public static long uptime = 0;
	
	public final static AtomicInteger packetsSent = new AtomicInteger(); 
	public final static AtomicInteger packetsReceived = new AtomicInteger();
	
	public final static AtomicLong bytesSent = new AtomicLong();
	public final static AtomicLong bytesReceived = new AtomicLong();
	
	public final static AtomicInteger clientsConnected = new AtomicInteger();
	
	public String getUptime() {
		if (!online) {
			return "offline";
		}
		uptime = System.currentTimeMillis() - uptimeSince;
		uptime = uptime/1000;
		return String.format("%d:%02d:%02d", uptime/3600, (uptime%3600)/60, (uptime%60));
	}
	
	public static String getPacketsSent() {
		return packetsSent.toString();
	}
	
	public static String getPacketsReceived() {
		return packetsReceived.toString();
	}
	
	public static String getBytesSent() {
		return bytesSent.toString();
	}
	
	public static String getBytesReceived() {
		return bytesReceived.toString();
	}
	
	public static String getFreeMemory() {
		return Long.toString(Runtime.getRuntime().freeMemory()/(1024*1024));
	}
	
	public static String getTotalMemory() {
		return Long.toString(Runtime.getRuntime().totalMemory()/(1024*1024));
	}

	public static String getMemoryUsage() {
		return Long.toString(getUsedMemoryMB()) + " / " + getTotalMemory() + " mb";
	}
	
	public static long getUsedMemoryMB(){
		return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024*1024); // ;
	}
}
