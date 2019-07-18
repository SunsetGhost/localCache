package com.dj.cache.impl.local;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dj.cache.CacheTaskScheduler;
import com.dj.cache.ITaskSchedulerCache;
import com.dj.cache.impl.IIncrementLoadCacheProvider;

public class IncrementLoadCache<K, V> extends BaseGuavaCache<K, V> implements ITaskSchedulerCache {
	
	protected Logger logger;
	
	private boolean preLoad;
	
	private int refreshInterval;
	
	private CacheTaskScheduler cacheTaskScheduler;
	
	private IIncrementLoadCacheProvider<K, V> cacheProvider;
	
	public IncrementLoadCache() {
		this.logger = LoggerFactory.getLogger(IncrementLoadCache.class);
		this.refreshInterval = -1;
		this.cacheTaskScheduler = null;
	}
	
	@Override
	public CacheTaskScheduler getCacheTaskScheduler() {
		return this.cacheTaskScheduler;
	}

	@Override
	protected void initCache() {
		if(this.cacheProvider == null) {
			this.logger.warn("Can not find cache provider {}.", super.getName());
		}
		super.setCache(super.buildCache());
		if(this.preLoad) {
			this.refreshCache();
		}
		if(this.cacheProvider != null) {
			this.cacheTaskScheduler = createCacheTask();
			if(this.cacheTaskScheduler != null) {
				this.cacheTaskScheduler.setLastRunTimeMillis(System.currentTimeMillis());
			}
		}
	}
	
	private void refreshCache() {
		if(this.cacheProvider != null) {
			this.logger.info("RefreshCache {} start.", super.getName());
            this.cacheProvider.incrementLoad(this);
            this.logger.info("RefreshCache {} end. Cache size: {}", super.getName(), this.getCache().size());
		}
	}
	
	private CacheTaskScheduler createCacheTask() {
		if (this.refreshInterval != -1) {
			return new CacheTaskScheduler(this.refreshInterval) {
				
				@Override
				public void run() {
					try {
                        IncrementLoadCache.this.refreshCache();
                    }
                    catch (RuntimeException e) {
                        IncrementLoadCache.this.logger.error("Refresh cache exception.", e);
                    }
				}
			};
		}
		return null;
	}

	public int getRefreshInterval() {
		return refreshInterval;
	}

	public void setRefreshInterval(int refreshInterval) {
		this.refreshInterval = refreshInterval;
	}

	public IIncrementLoadCacheProvider<K, V> getCacheProvider() {
		return cacheProvider;
	}

	public void setCacheProvider(IIncrementLoadCacheProvider<K, V> cacheProvider) {
		this.cacheProvider = cacheProvider;
	}

	public void setCacheTaskScheduler(CacheTaskScheduler cacheTaskScheduler) {
		this.cacheTaskScheduler = cacheTaskScheduler;
	}

}
