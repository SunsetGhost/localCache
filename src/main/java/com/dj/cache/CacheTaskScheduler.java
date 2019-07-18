package com.dj.cache;

public abstract class CacheTaskScheduler implements Runnable {
	
	private long intervalMillis;
	
	private long lastRunTimeMillis;
	
	public CacheTaskScheduler(int interval) {
		this.intervalMillis = interval * 1000;
		this.lastRunTimeMillis = -1;
	}
	
	public boolean isTimeToSchedule(long timeMillis) {
		boolean result = false;
		if(timeMillis - this.lastRunTimeMillis >= this.intervalMillis) {
			this.lastRunTimeMillis = timeMillis;
			result = true;
		}
		return result;
	}

	public long getIntervalMillis() {
		return intervalMillis;
	}

	public void setIntervalMillis(long intervalMillis) {
		this.intervalMillis = intervalMillis;
	}

	public long getLastRunTimeMillis() {
		return lastRunTimeMillis;
	}

	public void setLastRunTimeMillis(long lastRunTimeMillis) {
		this.lastRunTimeMillis = lastRunTimeMillis;
	}

}
