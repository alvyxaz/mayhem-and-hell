package com.friendlyblob.mayhemandhell.server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.friendlyblob.mayhemandhell.server.network.ThreadPoolManager;

public class Shutdown extends Thread{

	private static final Logger log = Logger.getLogger(Shutdown.class.getName());
	
	private int countdownTimer;
	
	public static final ShutDownListener LISTENER = new ShutDownListener(); 
	
	@Override
	public void run() {
		
		TimeCounter tc = new TimeCounter();
		
		// Shutting down all thread pools
		try {
			ThreadPoolManager.getInstance().shutdown();
			log.info("Thread Pool Manager: Manager has been shut down(" + tc.getEstimatedTimeAndRestartCounter() + "ms).");
		} catch (Throwable t) {
			log.warning("ThreadPoolManager failed to shutdown");
		}
		
		// Shutting down selector thread
		try {
			GameServer.gameServer.getSelectorThread().shutdown();
			log.info("Game Server: Selector thread has been shut down(" + tc.getEstimatedTimeAndRestartCounter() + "ms).");
		} catch (Throwable t) {
			log.warning("Selector thread failed to shut down");
		}
		
		// Terminating server
		Runtime.getRuntime().halt(0);
		
		log.info("Server successfully shut down");
	}
	
	/*
	 * Countsdown until shutdown
	 */
	private void countdown(){
		try {
			while (countdownTimer > 0){
				
				countdownTimer--;
				int delay = 1000; // milliseconds
				Thread.sleep(delay);
			}
		} catch (InterruptedException e){
			log.log(Level.WARNING, "Countdown timer failed to sleep");
		}
	}
	
	public Shutdown(){
		countdownTimer = -1;
	}
	
	public static final Shutdown getInstance(){
		return SingletonHolder.INSTACE;
	}
	
	private static class SingletonHolder {
		public static final Shutdown INSTACE =new Shutdown();
	}
	
	/**
	 * A simple class used to track down the estimated time of method executions.<br>
	 * Once this class is created, it saves the start time, and when you want to get the estimated time, use the getEstimatedTime() method.
	 */
	private static final class TimeCounter {
		private long startTime;
		
		protected TimeCounter() {
			restartCounter();
		}
		
		protected void restartCounter() {
			startTime = System.currentTimeMillis();
		}
		
		protected long getEstimatedTimeAndRestartCounter() {
			final long toReturn = System.currentTimeMillis() - startTime;
			restartCounter();
			return toReturn;
		}
		
		protected long getEstimatedTime() {
			return System.currentTimeMillis() - startTime;
		}
	}
	
	public static final class ShutDownListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Shutdown.getInstance().run();
		}
		
	}
	
}
