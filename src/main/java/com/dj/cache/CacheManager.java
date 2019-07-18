package com.dj.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dj.util.CacheThreadUtil;

/**
 * 
 * 缓存管理器
 * 所有缓存的容器，并负责刷新缓存
 *
 */
public class CacheManager {
	
	private static final Logger logger = LoggerFactory.getLogger(CacheManager.class);
	
	private static CacheManager instance = new CacheManager();
	
	private Map<String, ICache<?, ?>> cacheMap = new ConcurrentHashMap<>();
	
	private Map<String, CacheTaskScheduler> cacheTaskMap = new ConcurrentHashMap<>();
	
	private Thread scheduleThread;
	
	private long scheduleInterval = 10;
	
	private CacheManager() {
	}
	
	public static CacheManager getInstance() {
		return CacheManager.instance;
	}
	
	@SuppressWarnings("unchecked")
	public <K, V> ICache<K, V> getCache(String cacheName) {
		return (ICache<K, V>) cacheMap.get(cacheName);
	}
	
	private synchronized void startScheduleThread() {
		if(this.scheduleThread == null) {
			this.scheduleThread = new ScheduleThread();
			this.scheduleThread.start();
		}
	}
	
	void register(ICache<?, ?> cache) {
		if(cache != null) {
			if(cache instanceof ITaskSchedulerCache) {
				cacheTaskMap.put(cache.getName(), ((ITaskSchedulerCache) cache).getCacheTaskScheduler());
				this.startScheduleThread();
			}
			cacheMap.put(cache.getName(), cache);
			logger.info("Cache which name is " + cache.getName() + " has been registered.");
		}
	}
	
	private class ScheduleThread extends Thread {
		
		@Override
		public void run() {
			while(true) {
				try {
					doRun();
				} catch (Exception e) {
					logger.error("", e);
				} finally {
					try {
						TimeUnit.SECONDS.sleep(scheduleInterval);
					} catch (InterruptedException e) {
						logger.error("", e);
					}
				}
			}
		}
		
		private void doRun() {
			long timeMillis = System.currentTimeMillis();
			for(Map.Entry<String, CacheTaskScheduler> entry : cacheTaskMap.entrySet()) {
				CacheTaskScheduler scheduler = entry.getValue();
				try {
					if(scheduler.isTimeToSchedule(timeMillis)) {
						CacheThreadUtil.execute(scheduler);
					}
				} catch(Exception e) {
					logger.error("", e);
				}
			}
		}
		
	}

}
