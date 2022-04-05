package org.springframework.boot.autoconfigure.custom;

import com.penglecode.codeforce.common.consts.ApplicationConstants;
import com.penglecode.codeforce.common.consts.GlobalConstants;
import com.penglecode.codeforce.common.initializer.DefaultSpringAppPostInitializer;
import com.penglecode.codeforce.common.initializer.DefaultWebServerPreStartupListener;
import com.penglecode.codeforce.common.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.format.FormatterRegistry;

/**
 * 默认的SpringBoot应用配置
 *
 * @author pengpeng
 * @version 1.0
 * @created 2021/5/15 14:02
 */
@Configuration
public class DefaultSpringAppConfiguration extends AbstractSpringConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSpringAppConfiguration.class);

	/**
	 * Spring应用启动完成时的初始化程序
	 */
	@Bean
	@ConditionalOnMissingBean(name="defaultSpringAppPostInitializer")
	public DefaultSpringAppPostInitializer defaultSpringAppPostInitializer() {
		return new DefaultSpringAppPostInitializer();
	}

	/**
	 * 默认的Web应用启动时的初始化程序主类
	 */
	@Bean
	@ConditionalOnMissingBean(name="defaultWebServerPreStartupListener")
	public DefaultWebServerPreStartupListener defaultWebServerPreStartupListener() {
		return new DefaultWebServerPreStartupListener();
	}

	/**
	 * 全局默认的MessageSourceAccessor
	 */
	@Bean
	@ConditionalOnMissingBean(name="defaultMessageSourceAccessor")
	public static MessageSourceAccessor defaultMessageSourceAccessor(MessageSource messageSource) {
		MessageSourceAccessor messageSourceAccessor = new MessageSourceAccessor(messageSource, GlobalConstants.DEFAULT_LOCALE);
		LOGGER.info(">>> 初始化Spring应用的全局国际化资源文件配置! messageSource = {}, messageSourceAccessor = {}", messageSource, messageSourceAccessor);
		return messageSourceAccessor;
	}
	
	/**
	 * 全局默认的ResourcePatternResolver
	 */
	@Bean
	@ConditionalOnMissingBean(name="defaultResourcePatternResolver")
	public static ResourcePatternResolver defaultResourcePatternResolver(AbstractApplicationContext applicationContext) {
		ResourcePatternResolver resourcePatternResolver = ReflectionUtils.getFieldValue(applicationContext, "resourcePatternResolver");
		LOGGER.info(">>> 初始化Spring应用的默认文件资源解析器配置! resourcePatternResolver = {}", resourcePatternResolver);
		return resourcePatternResolver;
	}
	
	/**
	 * 全局默认的ConversionService
	 */
	@Bean
	@ConditionalOnMissingBean(name="defaultConversionService")
	public static ConversionService defaultConversionService() {
		ConversionService conversionService = ApplicationConversionService.getSharedInstance();
		ApplicationConstants.DEFAULT_DATETIME_FORMATTER_REGISTRAR.registerFormatters((FormatterRegistry) conversionService);
        LOGGER.info(">>> 初始化Spring应用的默认类型转换服务配置! conversionService = {}", conversionService.getClass());
        return conversionService;
	}

}
