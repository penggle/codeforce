package com.penglecode.codeforce.common.initializer;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Spring应用程序上下文初始化基类
 *
 * @author pengpeng
 * @version 1.0.0
 */
public abstract class AbstractSpringAppContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	private volatile boolean initialized = false;
	
	private static final Object LOCK = new Object();
	
	@Override
	public final void initialize(ConfigurableApplicationContext applicationContext) {
		if(!initialized) {
			synchronized(LOCK) {
				if(!initialized) {
					try {
						doInitialize(applicationContext);
					} finally {
						initialized = true;
					}
				}
			}
		}
	}
	
	public abstract void doInitialize(ConfigurableApplicationContext applicationContext);
	
}
