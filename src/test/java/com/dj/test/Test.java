package com.dj.test;

import java.util.HashMap;
import java.util.Map;

import com.dj.cache.impl.IFullLoadCacheProvider;
import com.dj.cache.impl.local.FullLoadCache;

public class Test {
	
	public static void main(String[] args) {
		testFullLoadCache();
	}
	
	private static void testFullLoadCache() {
		FullLoadCache<String, String> fullLoadCache = new FullLoadCache<>();
		try {
			fullLoadCache.setName("testCache");
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
