package com.dj.cache;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;

public abstract class BaseCache<K, V> implements ICache<K, V>, InitializingBean, BeanFactoryAware {
	
	private String name;
	
	protected BeanFactory beanFactory;
	
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}
	
	public <T> T getBean(String beanName, Class<T> requiredType) throws BeansException  {
		return (T) this.beanFactory.getBean(beanName, requiredType);
	}
	
	public void afterPropertiesSet() throws Exception {
		CacheManager.getInstance().register(this); // 缓存需要被缓存管理器管理，故注册到缓存管理器
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
