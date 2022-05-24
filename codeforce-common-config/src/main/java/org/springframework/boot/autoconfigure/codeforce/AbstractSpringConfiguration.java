package org.springframework.boot.autoconfigure.codeforce;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

/**
 * springboot配置基类
 *
 * @author pengpeng
 * @version 1.0
 */
public abstract class AbstractSpringConfiguration implements EnvironmentAware, ApplicationContextAware, BeanFactoryAware {

    private ConfigurableEnvironment environment;
    
    private ConfigurableApplicationContext applicationContext;

	private ConfigurableListableBeanFactory beanFactory;

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = (ConfigurableEnvironment) environment;
	}

	protected ConfigurableEnvironment getEnvironment() {
		return environment;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = (ConfigurableApplicationContext) applicationContext;
	}

	protected ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
	}

	protected ConfigurableListableBeanFactory getBeanFactory() {
		return beanFactory;
	}

}
