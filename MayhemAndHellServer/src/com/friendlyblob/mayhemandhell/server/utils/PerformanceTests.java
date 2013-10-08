package com.friendlyblob.mayhemandhell.server.utils;

import java.util.Random;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.friendlyblob.mayhemandhell.server.network.ThreadPoolManager;

public class PerformanceTests extends Thread{

	private ScheduledThreadPoolExecutor executor;
	
	private int threadCount = 2000000;
	private int[] randomNumbers;
	
	public static AtomicInteger count;
	
	public PerformanceTests() {

	}
	
	public void run() {
		this.executor = new ScheduledThreadPoolExecutor(5);
		randomNumbers = new int[threadCount];
		
		Random random = new Random();
		
		for (int i = 0; i < threadCount; i++) {
			randomNumbers[i] = random.nextInt(1000);
		}
		
		long startTime = System.currentTimeMillis();
		
		for (int i = 0; i < threadCount; i++) {
			this.executor.schedule(new WorkerThread(), randomNumbers[i], TimeUnit.MILLISECONDS);
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
//	
//	public static void main(String [] args) {
//		count = new AtomicInteger(0);
//		new PerformanceTests().run();
//	}
	
	public static class WorkerThread implements Runnable {
		@Override
		public void run() {
			count.incrementAndGet();
		}
	}

	
}
