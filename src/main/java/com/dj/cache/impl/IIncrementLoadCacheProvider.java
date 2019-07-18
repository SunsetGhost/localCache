package com.dj.cache.impl;

import com.dj.cache.ICache;

public interface IIncrementLoadCacheProvider<K, V> {
	
	void incrementLoad(ICache<K, V> cache); 

}
