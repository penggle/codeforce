package com.penglecode.codeforce.common.consts;

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
		this(null, null);
	}

	protected SpringBeanConstant(String name) {
		this(name, null);
	}

	protected SpringBeanConstant(String name, T defaultValue) {
		super(name, defaultValue);
	}
	
}
