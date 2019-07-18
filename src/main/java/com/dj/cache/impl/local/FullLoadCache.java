package com.dj.cache.impl.local;

import java.util.Collections;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dj.cache.CacheTaskScheduler;
import com.dj.cache.ITaskSchedulerCache;
import com.dj.cache.impl.IFullLoadCacheProvider;
import com.google.common.cache.Cache;

public class FullLoadCache<K, V> extends BaseGuavaCache<K, V> implements ITaskSchedulerCache {
	
	protected Logger logger;
	
	protected IFullLoadCacheProvider<K, V> cacheProvider; // 数据加载器
	
	private boolean preLoad; // 是否提前加载
	
	private int refreshInterval;
	
	private CacheTaskScheduler cacheTaskScheduler;
	
	public FullLoadCache() {
		this.logger = LoggerFactory.getLogger(FullLoadCache.class);
		this.preLoad = true;
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
			this.logger.warn("Can not find cache provider {}.", this.getName());
		}
		super.setCache(this.buildCache());
		if(this.preLoad) {
			this.refreshCache(true);
		}
		if(this.cacheProvider != null) {
			this.cacheTaskScheduler = createCacheTask();
			if (null != this.cacheTaskScheduler && this.preLoad) {
                this.cacheTaskScheduler.setLastRunTimeMillis(System.currentTimeMillis());
            }
		}
	}
	
	private CacheTaskScheduler createCacheTask() {
		if(this.refreshInterval != -1) {
			return new CacheTaskScheduler(this.refreshInterval) {
				
				@Override
				public void run() {
					try {
						FullLoadCache.this.refreshCache(true);
					} catch(Exception e) {
						FullLoadCache.this.logger.error("Refresh cache exception.", e);
					}
				}
			};
		}
		return null;
	}
	
	private void refreshCache(boolean isClear) {
		if(this.cacheProvider != null) {
			this.logger.info("RefreshCache {} start.", (Object)super.getName());
			Map<K, V> values = this.cacheProvider.reload();
			if (null == values) {
                values = Collections.emptyMap();
            }
			if (values.size() > this.getMaximumSize()) {
                throw new RuntimeException("Exceed maximum size of the cache \"" + this.getName() + "\", actual : " + values.size() + ", maximum : " + this.getMaximumSize());
            }
			if (isClear) {
				Cache<K, V> newCache = this.buildCache();
				newCache.putAll(values);
				super.setCache(newCache);
			} else {
				this.put(values);
			}
			this.logger.info("RefreshCache {} end.", (Object)super.getName());
		}
	}

	public boolean isPreLoad() {
		return preLoad;
	}

	public void setPreLoad(boolean preLoad) {
		this.preLoad = preLoad;
	}

	public int getRefreshInterval() {
		return refreshInterval;
	}

	public void setRefreshInterval(int refreshInterval) {
		this.refreshInterval = refreshInterval;
	}

	public IFullLoadCacheProvider<K, V> getCacheProvider() {
		return cacheProvider;
	}

	public void setCacheProvider(IFullLoadCacheProvider<K, V> cacheProvider) {
		this.cacheProvider = cacheProvider;
	}

}
