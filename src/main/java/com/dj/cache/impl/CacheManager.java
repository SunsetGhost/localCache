package com.dj.cache.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dj.cache.CacheTaskScheduler;
import com.dj.cache.ICache;

public class CacheManager {
	
	private static final Logger logger = LoggerFactory.getLogger(CacheManager.class);
	
	private static CacheManager instance = new CacheManager();
	
	private Map<String, ICache<?, ?>> cacheMap = new ConcurrentHashMap<>();
	
	private Map<String, CacheTaskScheduler> cacheTaskMap = new ConcurrentHashMap<>();
	
	private CacheManager() {
		
	}
	
	public static CacheManager getInstance() {
		return CacheManager.instance;
	}
	
	void register() {
		
	}
	
	private static class ScheduleThread extends Thread {
		
		@Override
		public void run() {
			while(true) {
				try {
					
					TimeUnit.SECONDS.sleep(5);
				} catch (InterruptedException e) {
					logger.error("", e);
				}
			}
		}
		
	}

}
