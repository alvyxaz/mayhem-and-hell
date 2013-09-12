package com.friendlyblob.mayhemandhell.server.network;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.friendlyblob.mayhemandhell.server.Config;
import com.friendlyblob.mayhemandhell.server.network.utils.StringUtil;

public class ThreadPoolManager {

	protected static final Logger log = Logger.getLogger(ThreadPoolManager.class.getName());
	
	private static final class RunnableWrapper implements Runnable {
		private final Runnable r;
		
		public RunnableWrapper(final Runnable r) {
			this.r = r;
		}
		
		@Override
		public final void run() {
			try {
				this.r.run();
			} catch (final Throwable e) {
				final Thread t = Thread.currentThread();
				final UncaughtExceptionHandler h = t.getUncaughtExceptionHandler();
				if (h != null) {
					h.uncaughtException(t, e);
				}
			}
		}
	}
	
	protected ScheduledThreadPoolExecutor effectsScheduledThreadPool;
	protected ScheduledThreadPoolExecutor generalScheduledThreadPool;
	protected ScheduledThreadPoolExecutor aiScheduledThreadPool;
	private final ThreadPoolExecutor generalPacketsThreadPool;
	private final ThreadPoolExecutor ioPacketsThreadPool;
	private final ThreadPoolExecutor generalThreadPool;
	
	/** Temp workaround for VM issue */
	private static final long MAX_DELAY = Long.MAX_VALUE / 1000000 / 2;
	
	private boolean shutdown;
	
	public static ThreadPoolManager getInstance(){
		return SingletonHolder.INSTANCE;
	}
	
	protected ThreadPoolManager()
	{
		effectsScheduledThreadPool = new ScheduledThreadPoolExecutor(Config.THREAD_POOL_SIZE_EFFECTS, new PriorityThreadFactory("EffectsSTPool", Thread.NORM_PRIORITY));
		generalScheduledThreadPool = new ScheduledThreadPoolExecutor(Config.THREAD_POOL_SIZE_GENERAL, new PriorityThreadFactory("GeneralSTPool", Thread.NORM_PRIORITY));
		ioPacketsThreadPool = new ThreadPoolExecutor(Config.IO_PACKET_THREAD_CORE_SIZE, Integer.MAX_VALUE, 5L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new PriorityThreadFactory("I/O Packet Pool", Thread.NORM_PRIORITY + 1));
		generalPacketsThreadPool = new ThreadPoolExecutor(Config.GENERAL_PACKET_THREAD_CORE_SIZE, Config.GENERAL_PACKET_THREAD_CORE_SIZE + 2, 15L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new PriorityThreadFactory("Normal Packet Pool", Thread.NORM_PRIORITY + 1));
		generalThreadPool = new ThreadPoolExecutor(Config.GENERAL_THREAD_CORE_SIZE, Config.GENERAL_THREAD_CORE_SIZE + 2, 5L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new PriorityThreadFactory("General Pool", Thread.NORM_PRIORITY));
		aiScheduledThreadPool = new ScheduledThreadPoolExecutor(Config.AI_MAX_THREAD, new PriorityThreadFactory("AISTPool", Thread.NORM_PRIORITY));
		// Initial 10 minutes, delay 5 minutes.
		scheduleGeneralAtFixedRate(new PurgeTask(), 600000L, 300000L);
	}
	
	/**
	 * Schedules an effect to be executed later
	 * @param thread Effect thread
	 * @param delay Time to delay in milliseconds
	 */
	public ScheduledFuture<?> scheduleEffect(Runnable thread, long delay) {
		try {
			delay = ThreadPoolManager.validateDelay(delay);
			return effectsScheduledThreadPool.schedule(new RunnableWrapper(thread), delay, TimeUnit.MILLISECONDS);
		} catch (RejectedExecutionException e) {
			return null;
		}
	}
	
	/**
	 * Schedules an effect to be repeated 
	 * @param thread Effect thread
	 * @param initial Initial delay in milliseconds
	 * @param period Repetition period in milliseconds
	 */
	public ScheduledFuture<?> scheduleEffectAtFixedRate(Runnable thread, long initial, long period) {
		try {
			period = ThreadPoolManager.validateDelay(period);
			initial = ThreadPoolManager.validateDelay(initial);
			return effectsScheduledThreadPool.scheduleAtFixedRate(new RunnableWrapper(thread), initial, period, TimeUnit.MILLISECONDS);
		} catch (RejectedExecutionException e) {
			return null; /* shutdown, ignore */
		}
	}
	
	/**
	 * Schedules a general use thread
	 * @param thread Thread 
	 * @param delay Time to delay in milliseconds
	 */
	public ScheduledFuture<?> scheduleGeneral(Runnable thread, long delay) {
		try {
			delay = ThreadPoolManager.validateDelay(delay);
			return generalScheduledThreadPool.schedule(new RunnableWrapper(thread), delay, TimeUnit.MILLISECONDS);
		} catch (RejectedExecutionException e) {
			return null; /* shutdown, ignore */
		}
	}
	
	/**
	 * Schedules a general thread to be repeated
	 * @param thread Effect thread
	 * @param initial Initial delay in milliseconds
	 * @param period Repetition period in milliseconds
	 */
	public ScheduledFuture<?> scheduleGeneralAtFixedRate(Runnable thread, long initial, long period){
		try {
			period = ThreadPoolManager.validateDelay(period);
			initial = ThreadPoolManager.validateDelay(initial);
			return generalScheduledThreadPool.scheduleAtFixedRate(new RunnableWrapper(thread), initial, period, TimeUnit.MILLISECONDS);
		} catch (RejectedExecutionException e) {
			return null; /* shutdown, ignore */
		}
	}
	
	/**
	 * Schedules an AI thread
	 * @param thread AI thread 
	 * @param delay Time to delay in milliseconds
	 */
	public ScheduledFuture<?> scheduleAi(Runnable thread, long delay) {
		try {
			delay = ThreadPoolManager.validateDelay(delay);
			return aiScheduledThreadPool.schedule(new RunnableWrapper(thread), delay, TimeUnit.MILLISECONDS);
		} catch (RejectedExecutionException e) {
			return null; /* shutdown, ignore */
		}
	}
	
	/**
	 * Schedules an AI thread to be repeated
	 * @param thread AI thread
	 * @param initial Initial delay in milliseconds
	 * @param period Repetition period in milliseconds
	 */
	public ScheduledFuture<?> scheduleAiAtFixedRate(Runnable thread, long initial, long period) {
		try {
			period = ThreadPoolManager.validateDelay(period);
			initial = ThreadPoolManager.validateDelay(initial);
			return aiScheduledThreadPool.scheduleAtFixedRate(new RunnableWrapper(thread), initial, period, TimeUnit.MILLISECONDS);
		} catch (RejectedExecutionException e) {
			return null; /* shutdown, ignore */
		}
	}
	
	public void executePacket(Runnable packet) {
		generalPacketsThreadPool.execute(packet);
	}
	
	public void executeCommunityPacket(Runnable packet) {
		generalPacketsThreadPool.execute(packet);
	}
	
	public void executeIOPacket(Runnable packet) {
		ioPacketsThreadPool.execute(packet);
	}
	
	public void executeTask(Runnable task) {
		generalThreadPool.execute(task);
	}
	
	public void executeAi(Runnable ai) {
		aiScheduledThreadPool.execute(new RunnableWrapper(ai));
	}
	
	public void shutdown(){
		shutdown = true;
		try {
			int timeout = 0; // TODO change to 1 for safety
			effectsScheduledThreadPool.awaitTermination(timeout, TimeUnit.SECONDS);
			generalScheduledThreadPool.awaitTermination(timeout, TimeUnit.SECONDS);
			generalPacketsThreadPool.awaitTermination(timeout, TimeUnit.SECONDS);
			ioPacketsThreadPool.awaitTermination(timeout, TimeUnit.SECONDS);
			generalThreadPool.awaitTermination(timeout, TimeUnit.SECONDS);
			effectsScheduledThreadPool.shutdown();
			generalScheduledThreadPool.shutdown();
			generalPacketsThreadPool.shutdown();
			ioPacketsThreadPool.shutdown();
			generalThreadPool.shutdown();
			log.info("All ThreadPools are now stopped");	
		} catch (InterruptedException e) {
			log.log(Level.WARNING, "", e);
		}
	}
	
	public boolean isShutdown(){
		return shutdown;
	}
	
	public void purge(){
		effectsScheduledThreadPool.purge();
		generalScheduledThreadPool.purge();
		aiScheduledThreadPool.purge();
		ioPacketsThreadPool.purge();
		generalPacketsThreadPool.purge();
		generalThreadPool.purge();
	}
	
	/*
	 * Basic information about packets
	 */
	public String getPacketStats(){
		final StringBuilder sb = new StringBuilder(1000);
		ThreadFactory tf = generalPacketsThreadPool.getThreadFactory();
		
		if (tf instanceof PriorityThreadFactory) {
			
			PriorityThreadFactory ptf = (PriorityThreadFactory) tf;
			int count = ptf.getGroup().activeCount();
			Thread[] threads = new Thread[count + 2];
			ptf.getGroup().enumerate(threads);
			StringUtil.append(sb, "General Packet Thread Pool:" + Config.EOL + "Tasks in the queue: ", String.valueOf(generalPacketsThreadPool.getQueue().size()), Config.EOL + "Showing threads stack trace:" + Config.EOL + "There should be ", String.valueOf(count), " Threads" + Config.EOL);
			
			for (Thread t : threads) {
				if (t == null) {
					continue;
				}
				
				StringUtil.append(sb, t.getName(), Config.EOL);
				for (StackTraceElement ste : t.getStackTrace()) {
					StringUtil.append(sb, ste.toString(), Config.EOL);
				}
			}
		}
		
		sb.append("Packet Tp stack traces printed.");
		sb.append(Config.EOL);
		return sb.toString();
	}
	
	/*
	 * Basic information about general threads
	 */
	public String getGeneralStats() {
		final StringBuilder sb = new StringBuilder(1000);
		ThreadFactory tf = generalThreadPool.getThreadFactory();
		
		if (tf instanceof PriorityThreadFactory) {
			
			PriorityThreadFactory ptf = (PriorityThreadFactory) tf;
			int count = ptf.getGroup().activeCount();
			Thread[] threads = new Thread[count + 2];
			ptf.getGroup().enumerate(threads);
			StringUtil.append(sb, "General Thread Pool:" + Config.EOL + "Tasks in the queue: ", String.valueOf(generalThreadPool.getQueue().size()), Config.EOL + "Showing threads stack trace:" + Config.EOL + "There should be ", String.valueOf(count), " Threads" + Config.EOL);
			
			for (Thread t : threads) {
				if (t == null) {
					continue;
				}
				
				StringUtil.append(sb, t.getName(), Config.EOL);
				
				for (StackTraceElement ste : t.getStackTrace()) {
					StringUtil.append(sb, ste.toString(), Config.EOL);
				}
			}
		}
		
		sb.append("Packet Tp stack traces printed." + Config.EOL);
		
		return sb.toString();
	}
	
	/**
	 * Making sure that delay is within acceptable bounds
	 */
	public static long validateDelay(long delay) {
		if (delay < 0) {
			delay = 0;
		} else if (delay > MAX_DELAY) {
			delay = MAX_DELAY;
		}
		return delay;
	}
	
	private static class PriorityThreadFactory implements ThreadFactory {
		private final int priority;
		private final String name;
		private final AtomicInteger threadNumber = new AtomicInteger(1);
		private final ThreadGroup group;
		
		public PriorityThreadFactory(String name, int priority) {
			this.priority = priority;
			this.name = name;
			group = new ThreadGroup(name);
		}
		
		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(group, r, name + "-" + threadNumber.getAndIncrement());
			t.setPriority(priority);
			return t;
		}
		
		public ThreadGroup getGroup() {
			return group;
		}
	}
	
	protected class PurgeTask implements Runnable {
		@Override
		public void run() {
			effectsScheduledThreadPool.purge();
			generalScheduledThreadPool.purge();
			aiScheduledThreadPool.purge();
		}
	}
	
	public static class SingletonHolder{
		public static final ThreadPoolManager INSTANCE = new ThreadPoolManager(); 
	}
	
}
