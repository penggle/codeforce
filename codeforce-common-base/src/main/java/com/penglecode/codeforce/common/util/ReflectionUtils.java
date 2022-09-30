package com.penglecode.codeforce.common.util;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 反射工具类
 *
 * @author pengpeng
 * @version 1.0.0
 */
@SuppressWarnings({"unchecked"})
public class ReflectionUtils extends org.springframework.util.ReflectionUtils {

	private ReflectionUtils() {}

	/**
	 * 返回指定类的所有字段(包括public|protected|private|default)，包括父类的
	 *
	 * @param targetClass
	 * @return
	 */
	public static Set<Field> getAllFields(Class<?> targetClass) {
		Assert.notNull(targetClass, "Parameter 'targetClass' must be not null!");
		Set<Field> allFields = new LinkedHashSet<>();
		Class<?> searchType = targetClass;
		while (searchType != null && !Object.class.equals(searchType)) {
			Field[] fields = searchType.getDeclaredFields();
			allFields.addAll(Arrays.asList(fields));
			searchType = searchType.getSuperclass();
		}
		return allFields;
	}

	/**
	 * 在目标对象上设置属性字段的值(包括修改final字段的值)
	 * 
	 * @param field
	 * @param target
	 * @param value
	 */
	public static void setFieldValue(Field field, Object target, Object value) {
		try {
			field.setAccessible(true);
			field.set(target, value);
		} catch (Exception e) {
			handleReflectionException(e);
		}
	}
	
	/**
	 * 设置常量值
	 *
	 * @param constClass
	 * @param fieldName
	 * @param value
	 */
	public static void setFinalConstValue(Class<?> constClass, String fieldName, Object value) {
		Field field = findField(constClass, fieldName);
		setFinalFieldValue(null, field, value);
	}

	/**
	 * 设置final字段的值
	 *
	 * @param target
	 * @param fieldName
	 * @param value
	 */
	public static void setFinalFieldValue(Object target, String fieldName, Object value) {
		setFinalFieldValue(target, findField(target.getClass(), fieldName), value);
	}

	/**
	 * 设置final字段的值
	 *
	 * @param target
	 * @param field
	 * @param value
	 */
	public static void setFinalFieldValue(Object target, Field field, Object value) {
		Assert.notNull(field, "Parameter 'field' can not be null!");
    	try {
			field.setAccessible(true);
			final Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
			field.set(target, value);
			//设置完后再将修饰符改回去
			modifiersField.setInt(field, field.getModifiers() | Modifier.FINAL);
			modifiersField.setAccessible(false);
			field.setAccessible(false);
		} catch (Exception e) {
			handleReflectionException(e);
		}
    }
	
	/**
	 * 在目标对象上获取属性字段的值
	 * 
	 * @param field
	 * @param target
	 * @return
	 */
	public static <T> T getFieldValue(Field field, Object target) {
		Assert.notNull(field, "Parameter 'field' can not be null!");
		try {
			field.setAccessible(true);
			return (T) field.get(target);
		} catch (Exception e) {
			handleReflectionException(e);
			return null;
		}
	}
	
	/**
	 * 在目标对象上获取属性字段的值
	 * 
	 * @param target
	 * @param fieldName
	 * @return
	 */
	public static <T> T getFieldValue(Object target, String fieldName) {
		Field field = findField(target.getClass(), fieldName);
		return getFieldValue(field, target);
	}
	
	/**
	 * Determine whether the given method is a "getClass" method.
	 *
	 * @see Object#getClass()
	 * @param method
	 * @return 
	 */
	public static boolean isGetClassMethod(@Nullable Method method) {
		return (method != null && "getClass".equals(method.getName()) && method.getParameterCount() == 0);
	}
	
	/**
	 * Determine whether the given method is a "clone" method.
	 *
	 * @see Object#clone()
	 * @param method
	 * @return 
	 */
	public static boolean isCloneMethod(@Nullable Method method) {
		return (method != null && "clone".equals(method.getName()) && method.getParameterCount() == 0);
	}
	
	/**
	 * Determine whether the given method is a "notify" method.
	 *
	 * @see Object#notify()
	 * @param method
	 * @return 
	 */
	public static boolean isNotifyMethod(@Nullable Method method) {
		return (method != null && "notify".equals(method.getName()) && method.getParameterCount() == 0);
	}
	
	/**
	 * Determine whether the given method is a "notifyAll" method.
	 *
	 * @see Object#notify()
	 * @param method
	 * @return 
	 */
	public static boolean isNotifyAllMethod(@Nullable Method method) {
		return (method != null && "notifyAll".equals(method.getName()) && method.getParameterCount() == 0);
	}
	
	/**
	 * Determine whether the given method is a "wait" method.
	 *
	 * @see Object#wait()
	 * @param method
	 * @return 
	 */
	public static boolean isWaitMethod(@Nullable Method method) {
		return (method != null && "wait".equals(method.getName()) && (method.getParameterCount() == 0 || method.getParameterCount() == 1 || method.getParameterCount() == 2));
	}
	
	/**
	 * Determine whether the given method is a "finalize" method.
	 *
	 * @see Object#finalize()
	 * @param method
	 * @return 
	 */
	public static boolean isFinalizeMethod(@Nullable Method method) {
		return (method != null && "finalize".equals(method.getName()) && method.getParameterCount() == 0);
	}
	
}