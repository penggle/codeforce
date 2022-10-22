package com.penglecode.codeforce.common.consts;

import java.util.function.Supplier;

/**
 * 基于Spring上下文环境变量的常量
 *
 *  使用示例：
 *
 *  	public static final Constant<String> APP_CODE = new SpringEnvConstant<String>("spring.application.code") {};
 *
 * @author pengpeng
 * @version 1.0.0
 */
public abstract class SpringEnvConstant<T> extends Constant<T> {
	
	protected SpringEnvConstant(String name) {
		this(name, (T) null);
	}

	protected SpringEnvConstant(String name, T defaultValue) {
		super(name, defaultValue);
	}

	protected SpringEnvConstant(String name, Supplier<T> defaultValue) {
		super(name, defaultValue.get());
	}
	
}
