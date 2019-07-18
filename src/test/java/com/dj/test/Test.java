package com.dj.test;

import java.util.HashMap;
import java.util.Map;

import com.dj.cache.ICache;
import com.dj.cache.impl.IFullLoadCacheProvider;
import com.dj.cache.impl.IIncrementLoadCacheProvider;
import com.dj.cache.impl.local.FullLoadCache;
import com.dj.cache.impl.local.IncrementLoadCache;

public class Test {
	
	public static void main(String[] args) {
		testFullLoadCache();
		testIncrementLoadCache();
	}
	
	private static void testIncrementLoadCache() {
		try {
			IncrementLoadCache<String, String> incrementLoadCache = new IncrementLoadCache<>();
			incrementLoadCache.setName("testIncrementLoadCache");
			incrementLoadCache.setExpiredAfterWrite(20);
			IIncrementLoadCacheProvider<String, String> provider = new IIncrementLoadCacheProvider<String, String>() {

				@Override
				public void incrementLoad(ICache<String, String> cache) {
					Long current = System.currentTimeMillis();
					cache.put(current.toString(), current.toString());
				}
			};
			incrementLoadCache.setCacheProvider(provider);
			incrementLoadCache.setRefreshInterval(10);
			incrementLoadCache.afterPropertiesSet();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void testFullLoadCache() {
		try {
			FullLoadCache<String, String> fullLoadCache = new FullLoadCache<>();
			fullLoadCache.setName("testFullLoadCache");
			IFullLoadCacheProvider<String, String> provider = new IFullLoadCacheProvider<String, String>() {
				@Override
				public Map<String, String> reload() {
					Map<String, String> map = new HashMap<>();
					Long current = System.currentTimeMillis();
					map.put(current.toString(), current.toString());
					return map;
				}
			};
			fullLoadCache.setCacheProvider(provider);
			fullLoadCache.setRefreshInterval(20);
			fullLoadCache.afterPropertiesSet();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
