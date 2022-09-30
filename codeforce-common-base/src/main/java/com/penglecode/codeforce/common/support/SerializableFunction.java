package com.penglecode.codeforce.common.support;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 可序列化的Function
 *
 * @param <T>
 * @param <R>
 * @author pengpeng
 * @version 1.0.0
 */
@FunctionalInterface
public interface SerializableFunction<T,R> extends Function<T,R>, Serializable {

}