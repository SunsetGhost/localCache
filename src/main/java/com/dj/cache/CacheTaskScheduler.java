package com.dj.cache;

public class CacheTaskScheduler implements Runnable {
	
	private long intervalMillis;
	
	private long lastRunTimeMillis;
	
	public CacheTaskScheduler(int interval) {
		this.intervalMillis = interval;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
