package com.penglecode.codeforce.common.consts;

import com.penglecode.codeforce.common.util.SpringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.http.MediaType;

import java.time.format.DateTimeFormatter;
import java.util.function.Supplier;

/**
 * 应用的全局常量,其中包括：Spring上下文对象、Servlet上下文对象、应用的上下文路径、应用系统默认字符集、默认Locale、默认日期格式等常量
 *
 * @author pengpeng
 * @version 1.0
 */
public abstract class ApplicationConstants {

	/**
	 * 应用默认的Spring上下文环境变量(Environment)配置
	 */
	public static final String APP_DEFAULT_PROPERTY_SOURCE_NAME = "appDefaultProperties";

	/**
	 * 应用默认的响应类型
	 */
	public static final MediaType DEFAULT_RESPONSE_CONTENT_TYPE = MediaType.APPLICATION_JSON;

	/**
	 * SpringBoot全局SpringApplication实例
	 */
	public static final Supplier<SpringApplication> SPRING_APPLICATION = SpringUtils::getSpringApplication;

	/**
	 * Spring的ROOT上下文,由#ContextLoaderListener加载出来的spring上下文
	 */
	public static final Supplier<ApplicationContext> APPLICATION_CONTEXT = SpringUtils::getApplicationContext;
	
	/**
	 * Spring的环境变量上下文
	 */
	public static final Supplier<Environment> ENVIRONMENT = SpringUtils::getEnvironment;

	/**
	 * Spring管理的资源文件访问器
	 */
	public static final Supplier<MessageSourceAccessor> DEFAULT_MESSAGE_SOURCE_ACCESSOR = new SpringBeanConstant<MessageSourceAccessor>("defaultMessageSourceAccessor") {};
	
	/**
	 * 应用默认的ConversionService
	 */
	public static final Supplier<ConversionService> DEFAULT_CONVERSION_SERVICE = new SpringBeanConstant<ConversionService>("defaultConversionService", ApplicationConversionService.getSharedInstance()) {};

	/**
	 * Spring的资源解析器
	 */
	public static final Supplier<ResourcePatternResolver> DEFAULT_RESOURCE_PATTERN_RESOLVER = new SpringBeanConstant<ResourcePatternResolver>("defaultResourcePatternResolver", new PathMatchingResourcePatternResolver()) {};
	
	/**
	 * SpringMVC的默认HttpMessageConverters
	 */
	public static final Supplier<HttpMessageConverters> DEFAULT_HTTP_MESSAGE_CONVERTERS = new SpringBeanConstant<HttpMessageConverters>() {};

	/**
	 * 默认的Java8日期时间格式化注册器
	 */
	public static final DateTimeFormatterRegistrar DEFAULT_DATETIME_FORMATTER_REGISTRAR = new DateTimeFormatterRegistrar();

	static {
		DEFAULT_DATETIME_FORMATTER_REGISTRAR.setDateFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		DEFAULT_DATETIME_FORMATTER_REGISTRAR.setTimeFormatter(DateTimeFormatter.ofPattern("HH:mm:ss"));
		DEFAULT_DATETIME_FORMATTER_REGISTRAR.setDateTimeFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}

	private ApplicationConstants() {}

}
