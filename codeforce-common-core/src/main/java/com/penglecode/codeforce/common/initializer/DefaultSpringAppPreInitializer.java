package com.penglecode.codeforce.common.initializer;

import com.penglecode.codeforce.common.consts.ApplicationConstants;
import com.penglecode.codeforce.common.consts.Constant;
import com.penglecode.codeforce.common.consts.SpringConstantPool;
import com.penglecode.codeforce.common.util.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

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
public class DefaultSpringAppPreInitializer extends AbstractSpringAppContextInitializer {

	private static final String NACOS_LOGGING_DEFAULT_CONFIG_ENABLED_PROPERTY = "nacos.logging.default.config.enabled";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSpringAppPreInitializer.class);
	
	static {
		System.setProperty(NACOS_LOGGING_DEFAULT_CONFIG_ENABLED_PROPERTY, Boolean.FALSE.toString());
	}

	@Override
	public void doInitialize(ConfigurableApplicationContext applicationContext) {
		LOGGER.info(">>> Spring 应用启动前置初始化程序! applicationContext = {}", applicationContext);
		SpringUtils.setApplicationContext(applicationContext);
		SpringUtils.setEnvironment(applicationContext.getEnvironment());
		Constant.setConstantPool(new SpringConstantPool<>());

		ConfigurableEnvironment environment = applicationContext.getEnvironment();
		MapPropertySource appDefaultProperties = (MapPropertySource) environment.getPropertySources().get(ApplicationConstants.APP_DEFAULT_PROPERTY_SOURCE_NAME);
		if(appDefaultProperties != null) { //框架默认基于多数据源，此时应该强制禁用SpringBoot默认的数据源及Mybatis配置
			appDefaultProperties.getSource().put("spring.autoconfigure.exclude[0]", "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration");
			appDefaultProperties.getSource().put("spring.autoconfigure.exclude[1]", "org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration");
		}
	}

}
