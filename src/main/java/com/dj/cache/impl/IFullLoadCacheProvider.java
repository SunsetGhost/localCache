package com.dj.cache.impl;

import java.util.Map;

public interface IFullLoadCacheProvider<K, V> {
	
	Map<K, V> reload();
	
}
