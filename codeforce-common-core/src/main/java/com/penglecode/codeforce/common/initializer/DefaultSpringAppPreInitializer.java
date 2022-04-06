package com.penglecode.codeforce.common.initializer;

import com.penglecode.codeforce.common.consts.Constant;
import com.penglecode.codeforce.common.consts.ConstantPool;
import com.penglecode.codeforce.common.consts.SpringConstantPool;
import com.penglecode.codeforce.common.util.ReflectionUtils;
import com.penglecode.codeforce.common.util.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Spring应用启动之处初始化程序
 * 该类配置方式如下：
 * 1、 在web.xml中以contextInitializerClasses上下文参数配置
 * 		<context-param>
 * 			<param-name>contextInitializerClasses</param-name>
 * 			<param-value>xyz.SpringAppPreBootingInitializer</param-value>
 * 		</context-param>
 * 
 * 2、 在Springboot的application.properties中配置
 * 		context.initializer.classes=xyz.SpringAppPreBootingInitializer
 *
 * @author pengpeng
 * @version 1.0
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DefaultSpringAppPreInitializer extends AbstractSpringAppContextInitializer {

	private static final String NACOS_LOGGING_DEFAULT_CONFIG_ENABLED_PROPERTY = "nacos.logging.default.config.enabled";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSpringAppPreInitializer.class);
	
	static {
		System.setProperty(NACOS_LOGGING_DEFAULT_CONFIG_ENABLED_PROPERTY, Boolean.FALSE.toString());
	}

	@Override
	public void doInitialize(ConfigurableApplicationContext applicationContext) {
		LOGGER.info(">>> Spring 应用启动前置初始化程序! applicationContext = {}", applicationContext);
		Method method = Objects.requireNonNull(ReflectionUtils.findMethod(SpringUtils.class, "setApplicationContext", ApplicationContext.class));
		method.setAccessible(true);
		ReflectionUtils.invokeMethod(method, null, applicationContext); //设置ApplicationContext

		method = Objects.requireNonNull(ReflectionUtils.findMethod(SpringUtils.class, "setEnvironment", Environment.class));
		method.setAccessible(true);
		ReflectionUtils.invokeMethod(method, null, applicationContext.getEnvironment()); //设置Environment

		method = Objects.requireNonNull(ReflectionUtils.findMethod(Constant.class, "setConstantPool", ConstantPool.class));
		method.setAccessible(true);
		ReflectionUtils.invokeMethod(method, null, new SpringConstantPool<>()); //设置ConstantPool
	}

}
