package com.friendlyblob.mayhemandhell.server;

import java.io.File;

public class Config {

	// --------------------------------------------------
	// Constants
	// --------------------------------------------------
	public static final String EOL = System.getProperty("line.separator");
	
	// --------------------------------------------------
	// MMO Settings
	// --------------------------------------------------
	public static int MMO_SELECTOR_SLEEP_TIME = 20;
	public static int MMO_MAX_SEND_PER_PASS = 12;
	public static int MMO_MAX_READ_PER_PASS = 12;
	public static int MMO_HELPER_BUFFER_COUNT = 20;
	public static boolean MMO_TCP_NODELAY = false;
	
	// --------------------------------------------------
	// Database
	// --------------------------------------------------
	public static int DATABASE_MAX_CONNECTIONS = 10;
	public static int DATABASE_MAX_IDLE_TIME = 0;
	public static String DATABASE_DRIVER = "com.mysql.jdbc.Driver";
	public static String DATABASE_URL = "jdbc:mysql://127.0.0.1/mayhemandhell";
	public static String DATABASE_LOGIN = "root";
	public static String DATABASE_PASSWORD = "";
	public static long CONNECTION_CLOSE_TIME = 60000;
	
	
	// --------------------------------------------------
	// Server Settings
	// --------------------------------------------------
	public static String GAMESERVER_HOSTNAME = "*";
	public static int PORT_GAME = 7777;
	
	// Debugging
	public static boolean PACKET_HANDLER_DEBUG = false;
	
	// Networking
	public static int CLIENT_PACKET_QUEUE_SIZE = 20;
	
	// Stats
	public static int CLIENT_PACKET_QUEUE_MEASURE_INTERVAL = 5;
	public static int CLIENT_PACKET_QUEUE_MAX_UNKNOWN_PER_MIN = 5;
	public static int CLIENT_PACKET_QUEUE_MAX_BURST_SIZE = 12;
	public static int CLIENT_PACKET_QUEUE_MAX_OVERFLOWS_PER_MIN = 1;
	public static int CLIENT_PACKET_QUEUE_MAX_UNDERFLOWS_PER_MIN = 1;
	public static int CLIENT_PACKET_QUEUE_MAX_FLOODS_PER_MIN = 2;
	public static int CLIENT_PACKET_QUEUE_MAX_AVERAGE_PACKETS_PER_SECOND = 40;
	public static int CLIENT_PACKET_QUEUE_MAX_PACKETS_PER_SECOND = 80;

	// Thread pools
	public static int THREAD_POOL_SIZE_EFFECTS = 10;
	public static int THREAD_POOL_SIZE_GENERAL = 13;
	public static int IO_PACKET_THREAD_CORE_SIZE = 2;
	public static int GENERAL_PACKET_THREAD_CORE_SIZE = 4;
	public static int GENERAL_THREAD_CORE_SIZE = 4;
	public static int AI_MAX_THREAD = 6;
	
	
	public static File DATAPACK_ROOT;
	
	public static void load(){
		DATAPACK_ROOT = new File(".");
	}
}
