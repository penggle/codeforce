package com.penglecode.codeforce.common.consts;

/**
 * 常量池
 *
 * @author pengpeng
 * @version 1.0
 */
public interface ConstantPool<T> {

	/**
	 * 从常量池中获取常量值
	 * @param constant
	 * @return
	 */
	T get(Constant<T> constant);
	
}