package com.penglecode.codeforce.common.consts;

import java.util.function.Supplier;

/**
 * 基于Spring上下文中的bean的常量
 *
 * 使用示例：
 *
 * 		public static final Supplier<MessageSourceAccessor> DEFAULT_MESSAGE_SOURCE_ACCESSOR = new SpringBeanConstant<MessageSourceAccessor>("defaultMessageSourceAccessor") {};
 *
 * @author pengpeng
 * @version 1.0.0
 */
public abstract class SpringBeanConstant<T> extends Constant<T> {
	
	protected SpringBeanConstant() {
		this(null, (T) null);
	}

	protected SpringBeanConstant(String name) {
		this(name, (T) null);
	}

	protected SpringBeanConstant(String name, T defaultValue) {
		super(name, defaultValue);
	}

	protected SpringBeanConstant(String name, Supplier<T> defaultValue) {
		super(name, defaultValue.get());
	}
	
}
