package com.dj.cache;

import java.util.Collection;
import java.util.Map;

public interface ICache<K, V> {
	
	String getName();
	
	V get(K key);
	
	Map<K, V> get(Collection<K> col);
	
	Map<K, V> getAll();
	
	void put(K key, V value);
	
	void put(Map<K, V> map);
	
	void remove(K key);
	
	void remove(Collection<K> col);
	
	void removeAll();

}
