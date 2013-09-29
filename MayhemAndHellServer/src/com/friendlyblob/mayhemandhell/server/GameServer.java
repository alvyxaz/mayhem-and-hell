package com.friendlyblob.mayhemandhell.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.mmocore.network.SelectorConfig;
import org.mmocore.network.SelectorThread;

import com.friendlyblob.mayhemandhell.server.data.ItemDataParser;
import com.friendlyblob.mayhemandhell.server.factories.IdFactory;
import com.friendlyblob.mayhemandhell.server.model.World;
import com.friendlyblob.mayhemandhell.server.model.datatables.CharacterTemplateTable;
import com.friendlyblob.mayhemandhell.server.model.datatables.ItemTable;
import com.friendlyblob.mayhemandhell.server.network.GameClient;
import com.friendlyblob.mayhemandhell.server.network.GamePacketHandler;
import com.friendlyblob.mayhemandhell.server.network.ThreadPoolManager;
import com.friendlyblob.mayhemandhell.server.network.utils.IPv4Filter;

public class GameServer{
	private static final Logger log = Logger.getLogger(GameServer.class.getName());
    public static GameServer gameServer;
    
    private final SelectorThread<GameClient> selectorThread;
    private final GamePacketHandler gamePacketHandler;
    
    public long getUsedMemoryMB(){
		return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576; // ;
	}
    
    public SelectorThread<GameClient> getSelectorThread(){
		return selectorThread;
	}
    
    public GamePacketHandler getGamePacketHandler()
	{
		return gamePacketHandler;
	}
    
    public GameServer() throws Exception{
    	gameServer = this;
    	log.finest(getClass().getSimpleName() + ": used mem:" + getUsedMemoryMB() + "MB");
    	
    	// Loading gameserver data (items and etc)
    	ItemTable.initialize();
    	CharacterTemplateTable.initialize();
    	
    	// Loading factories
    	IdFactory.initialize();

    	ThreadPoolManager.getInstance();
    	
    	GameTimeController.initialize();
    	
    	World.getInstance();
    	
    	// Shutdown hook
    	Runtime.getRuntime().addShutdownHook(Shutdown.getInstance());
    	
    	
    	System.gc();
		// maxMemory is the upper limit the jvm can use, totalMemory the size of
		// the current allocation pool, freeMemory the unused memory in the
		// allocation pool
		long freeMem = ((Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory()) + Runtime.getRuntime().freeMemory()) / 1048576;
		long totalMem = Runtime.getRuntime().maxMemory() / 1048576;
		log.info(getClass().getSimpleName() + ": Started, free memory " + freeMem + " Mb of " + totalMem + " Mb");
		
		// Packet selector configuration
		final SelectorConfig sc = new SelectorConfig();
		sc.MAX_READ_PER_PASS = Config.MMO_MAX_READ_PER_PASS;
		sc.MAX_SEND_PER_PASS = Config.MMO_MAX_SEND_PER_PASS;
		sc.SLEEP_TIME = Config.MMO_SELECTOR_SLEEP_TIME;
		sc.HELPER_BUFFER_COUNT = Config.MMO_HELPER_BUFFER_COUNT;
		sc.TCP_NODELAY = Config.MMO_TCP_NODELAY;
		
		gamePacketHandler = new GamePacketHandler();
		selectorThread = new SelectorThread<>(sc, gamePacketHandler, gamePacketHandler, gamePacketHandler, new IPv4Filter());
		
		InetAddress bindAddress = null;
		if (!Config.GAMESERVER_HOSTNAME.equals("*")) {
			try {
				bindAddress = InetAddress.getByName(Config.GAMESERVER_HOSTNAME);
			} catch (UnknownHostException e1) {
				log.log(Level.SEVERE, getClass().getSimpleName() + ": WARNING: The GameServer bind address is invalid, using all avaliable IPs. Reason: " + e1.getMessage(), e1);
			}
		}
		
		try {
			selectorThread.openServerSocket(bindAddress, Config.PORT_GAME);
		} catch (IOException e) {
			log.log(Level.SEVERE, getClass().getSimpleName() + ": FATAL: Failed to open server socket. Reason: " + e.getMessage(), e);
			System.exit(1);
		}
		
		selectorThread.start();
		
		// GUI thread
		(new GUI()).execute();
		
    }

	public static void main(String [] args) throws Exception {
		Config.load();
		
		final String LOG_FOLDER = "log"; // Name of folder for log file
		final String LOG_NAME = "log/log.cfg"; // Name of log file
		
		FileHandler fh = new FileHandler(LOG_FOLDER+"/serverLog.log"); 
		
		Logger.getLogger(GamePacketHandler.class.getName()).addHandler(fh);
		
		DatabaseFactory.getInstance();
		gameServer = new GameServer();
	}

}
