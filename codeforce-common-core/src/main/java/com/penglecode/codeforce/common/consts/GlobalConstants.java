package com.penglecode.codeforce.common.consts;

import java.util.Locale;

/**
 * 全局共通常量
 *
 * @author pengpeng
 * @version 1.0
 */
public class GlobalConstants {

	/**
	 * 默认字符编码
	 */
	public static final String DEFAULT_CHARSET = "UTF-8";
	
	/**
	 * 默认Locale
	 */
	public static final Locale DEFAULT_LOCALE = new Locale("zh", "CN");

	/**
	 * 针对数据库字段,诸如:'是','真','已删除',...等等由数字"1"代表的真值
	 */
	public static final Integer DEFAULT_YES_TRUE_FLAG = 1;

	/**
	 * 针对数据库字段,诸如:'否','假','未删除',...等等由数字"0"代表的假值
	 */
	public static final Integer DEFAULT_NO_FALSE_FLAG = 0;

	/**
	 * 默认的MDC追踪参数traceId的key名称
	 */
	public static final String DEFAULT_MDC_TRACE_ID_KEY = "traceId";

	/**
	 * 全局应用代码
	 */
	public static final Constant<String> APP_CODE = new SpringEnvConstant<String>("spring.application.code") {};

	private GlobalConstants() {}

}
