package com.penglecode.codeforce.common.util;

import org.springframework.core.ResolvableType;

import java.net.*;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAmount;
import java.util.*;

/**
 * Class工具类
 *
 * @author pengpeng
 * @version 1.0
 */
@SuppressWarnings({"unchecked"})
public class ClassUtils extends org.springframework.util.ClassUtils {

    /**
     * 默认的简单类型的祖先集合(但不包括八大基本类型及其包装类)
     */
    private static final Set<Class<?>> defaultAncestorsOfSimpleType = new HashSet<>();

    /**
     * 基本类型的默认值
     */
    private static final Map<Class<?>,Object> defaultValuesOfPrimitive = new HashMap<>();

    static {
        defaultAncestorsOfSimpleType.add(Number.class);
        defaultAncestorsOfSimpleType.add(CharSequence.class);
        defaultAncestorsOfSimpleType.add(Date.class);
        defaultAncestorsOfSimpleType.add(Calendar.class);
        defaultAncestorsOfSimpleType.add(TemporalAccessor.class);
        defaultAncestorsOfSimpleType.add(TemporalAmount.class);
        defaultAncestorsOfSimpleType.add(SocketAddress.class);
        defaultAncestorsOfSimpleType.add(InetAddress.class);
        defaultAncestorsOfSimpleType.add(URL.class);
        defaultAncestorsOfSimpleType.add(URI.class);

        defaultValuesOfPrimitive.put(boolean.class, false);
        defaultValuesOfPrimitive.put(char.class, '\u0000');
        defaultValuesOfPrimitive.put(byte.class, 0);
        defaultValuesOfPrimitive.put(short.class, 0);
        defaultValuesOfPrimitive.put(int.class, 0);
        defaultValuesOfPrimitive.put(long.class, 0L);
        defaultValuesOfPrimitive.put(float.class, 0.0f);
        defaultValuesOfPrimitive.put(double.class, 0.0d);
    }

    private ClassUtils() {}

    /**
     * 获取指定泛型父类的具体子类的实际泛型声明类型
     *
     * 示例：
     *     public interface BaseMapper<T> {}
     *
     *     public interface BaseUpdateMapper<T> extends BaseMapper<T> {}
     *
     *     public interface BaseSelectMapper<T> extends BaseMapper<T> {}
     *
     *     public static abstract class BaseMybatisMapper<T> implements BaseUpdateMapper<T>, BaseSelectMapper<T> {}
     *
     *     public interface ProductMapper1 extends BaseUpdateMapper<Product>, BaseSelectMapper<Product> {}
     *
     *     public static class ProductMapper2 implements BaseUpdateMapper<Product>, BaseSelectMapper<Product> {}
     *
     *     public static class ProductMapper3 extends BaseMybatisMapper<Product> {}
     *
     *     public static class Product implements Serializable {}
     *
     *     getSuperGenericType(ProductMapper1.class, BaseSelectMapper.class, 0) ==> Product.class
     *     getSuperGenericType(ProductMapper2.class, BaseSelectMapper.class, 0) ==> Product.class
     *     getSuperGenericType(ProductMapper3.class, BaseSelectMapper.class, 0) ==> Product.class
     *
     * @param subType       - 子类类型, subType的运行时泛型类型已经是确定的了，否则方法的返回结果将会为空
     * @param superType     - 父类类型，可以是类或者接口
     * @param index         - zero based
     * @return
     */
    public static <T,G> Class<G> getSuperGenericType(Class<? extends T> subType, Class<T> superType, int index) {
        ResolvableType resolvableType = ResolvableType.forClass(superType, subType);
        return (Class<G>) resolvableType.getGeneric(index).resolve();
    }

    /**
     * 判断指定目标类型targetType是否是简单类型
     *
     * @param targetType    - 目标类型
     * @return
     */
    public static boolean isSimpleType(Class<?> targetType) {
        return isSimpleType(targetType, null);
    }

    /**
     * 判断指定目标类型targetType是否是简单类型
     *
     * @param targetType    - 目标类型
     * @param additional    - 附加的认为是简单类型的类型集合
     * @return
     */
    public static boolean isSimpleType(Class<?> targetType, Set<Class<?>> additional) {
        //八大基本类型及其包装类
        if(isPrimitiveOrWrapper(targetType)) {
            return true;
        }
        //默认的被认为是简单类型的超类集合
        for(Class<?> simpleType : defaultAncestorsOfSimpleType) {
            if(isAssignable(simpleType, targetType)) {
                return true;
            }
        }
        //附加补充的认为是简单类型的集合
        if(additional != null) {
            for(Class<?> simpleType : additional) {
                if(isAssignable(simpleType, targetType)) {
                    return true;
                }
            }
        }
        return false; //否则认为是复杂类型
    }

    /**
     * 获取原始类型的默认值
     *
     * @param primitiveType
     * @param <T>
     * @return
     */
    public static <T> T getDefaultValueOfPrimitive(Class<T> primitiveType) {
        return (T) defaultValuesOfPrimitive.get(primitiveType);
    }

}
