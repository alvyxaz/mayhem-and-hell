package com.friendlyblob.mayhemandhell.server.utils;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javolution.util.FastMap;

import com.friendlyblob.mayhemandhell.server.model.GameObject;
import com.friendlyblob.mayhemandhell.server.network.ThreadPoolManager;

public class PerformanceTests extends Thread{

	public static ConcurrentHashMap <Integer, GameObject> list = new ConcurrentHashMap <Integer, GameObject>(1000);
	
	public static ScheduledThreadPoolExecutor executor;
	
	private static int threadCount = 1000000;
	private int[] randomNumbers;
	
	public static AtomicInteger count;
	
	public PerformanceTests() {

	}
	
	public void run() {
		this.executor = new ScheduledThreadPoolExecutor(1000);
		randomNumbers = new int[threadCount];
		
		Random random = new Random();
		
		for (int i = 0; i < threadCount; i++) {
			randomNumbers[i] = random.nextInt(1000);
			list.put(i, new GameObject());
		}
		
		long startTime = System.currentTimeMillis();
		
		this.executor.execute(new UserThread());
		
		for (int i = 0; i < threadCount; i++) {
			this.executor.schedule(new WorkerThread(i), randomNumbers[i], TimeUnit.MILLISECONDS);
		}
		
		try {
			while(count.get() < threadCount) {
					executor.getActiveCount();
					this.sleep(100);
			}
		} catch (Exception e) {
			
		}
		
		long estimatedTime = System.currentTimeMillis() - startTime;
		
		System.out.println("VA: " + estimatedTime + " " + count.get());
	}
	
//	public static void main(String [] args) {
//		count = new AtomicInteger(0);
//		new PerformanceTests().run();
//	}
	
	public static class WorkerThread implements Runnable {
		private int id;
		
		public WorkerThread(int id) {
			this.id = id;
		}
		
		@Override
		public void run() {
			list.remove(id);
			count.incrementAndGet();
		}
	}

	public static class UserThread implements Runnable {
		@Override
		public void run() {
			while(count.get() < threadCount) {
				try {
					for(GameObject object: list.values()) {
						int testas = 0;
						for (int i = 0; i < 100; i++) {
							testas += object.getPosition().getX() + 10;
						}
						testas *= 0;
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			System.out.println("OVER");
		}
	}
	
}
