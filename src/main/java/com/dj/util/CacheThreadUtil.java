package com.dj.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheThreadUtil {
	
	private static Logger logger = LoggerFactory.getLogger(CacheThreadUtil.class);
	
	private static ExecutorService cacheThreadPool;
	
	public static void execute(Runnable runnable) {
		CacheThreadUtil.getCacheThreadPool().execute(runnable);
	}
	
	public static synchronized ExecutorService getCacheThreadPool() {
		if(CacheThreadUtil.cacheThreadPool == null) {
			int threadPoolSize = Runtime.getRuntime().availableProcessors();
			if (threadPoolSize < 6) {
                threadPoolSize = 6;
            }
			CacheThreadUtil.cacheThreadPool = Executors.newFixedThreadPool(threadPoolSize);
			logger.info("CacheThreadPool has been initialized.");
		}
		return cacheThreadPool;
	}

}
