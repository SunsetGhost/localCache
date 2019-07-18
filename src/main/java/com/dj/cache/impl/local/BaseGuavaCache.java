package com.dj.cache.impl.local;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.dj.cache.BaseCache;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;

public abstract class BaseGuavaCache<K, V> extends BaseCache<K, V> {
	
	private long maximumSize; // 缓存最大数量
    private int expiredAfterAccess; // 访问后超时时间
    private int expiredAfterWrite;  // 写入后超时时间
    protected volatile Cache<K, V> cache;
    
    public BaseGuavaCache() {
    	this.maximumSize = 2000000L;
    	this.expiredAfterAccess = -1;
    	this.expiredAfterWrite = -1;
    }
	
	protected abstract void initCache();
	
	public void afterPropertiesSet() throws Exception {
		this.initCache(); // 初始化缓存
		super.afterPropertiesSet();
	}
	
	public Cache<K, V> getCache() {
        return this.cache;
    }
	
	public void setCache(Cache<K, V> cache) {
		this.cache = cache;
	}

	@Override
	public V get(K key) {
		return this.cache.getIfPresent(key);
	}

	@Override
	public Map<K, V> get(Collection<K> col) {
		return this.cache.getAllPresent(col);
	}

	@Override
	public Map<K, V> getAll() {
		return Collections.unmodifiableMap(this.cache.asMap());
	}

	@Override
	public void put(K key, V value) {
		this.cache.put(key, value);
	}

	@Override
	public void put(Map<K, V> map) {
		this.cache.putAll(map);
	}

	@Override
	public void remove(K key) {
		this.cache.invalidate(key);
	}

	@Override
	public void remove(Collection<K> col) {
		this.cache.invalidateAll(col);
	}

	@Override
	public void removeAll() {
		this.cache.invalidateAll();
	}

	public long getMaximumSize() {
		return maximumSize;
	}

	public void setMaximumSize(long maximumSize) {
		this.maximumSize = maximumSize;
	}

	public int getExpiredAfterAccess() {
		return expiredAfterAccess;
	}

	public void setExpiredAfterAccess(int expiredAfterAccess) {
		this.expiredAfterAccess = expiredAfterAccess;
	}

	public int getExpiredAfterWrite() {
		return expiredAfterWrite;
	}

	public void setExpiredAfterWrite(int expiredAfterWrite) {
		this.expiredAfterWrite = expiredAfterWrite;
	}
	
	protected Cache<K, V> buildCache() {
		return buildCache(null);
	}
	
	protected Cache<K, V> buildCache(CacheLoader<K, V> cacheLoader) {
		@SuppressWarnings("unchecked")
		CacheBuilder<K, V> builder = (CacheBuilder<K, V>) CacheBuilder.newBuilder();
		if (-1 != this.getExpiredAfterAccess()) {
			builder.expireAfterAccess(this.getExpiredAfterAccess(), TimeUnit.SECONDS);
		}
		if(-1 != this.getExpiredAfterWrite()) {
			builder.expireAfterWrite(this.getExpiredAfterWrite(), TimeUnit.SECONDS);
		}
		builder.maximumSize(this.getMaximumSize());
		return cacheLoader == null ? builder.build() : builder.build(cacheLoader);
	}

}
