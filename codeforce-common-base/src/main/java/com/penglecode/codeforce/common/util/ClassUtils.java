package com.penglecode.codeforce.common.util;

import org.springframework.core.ResolvableType;

import java.lang.reflect.Field;

/**
 * Class工具类
 *
 * @author pengpeng
 * @version 1.0
 */
@SuppressWarnings({"unchecked"})
public class ClassUtils extends org.springframework.util.ClassUtils {

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
     * 根据索引获得指定类上声明的泛型类型
     *
     * @param clazz
     * @param index
     * @param <T>       - 参数clazz的类型
     * @param <G>       - 参数clazz上的泛型类型
     * @return
     */
    public static <T,G> Class<G> getClassGenericType(final Class<T> clazz, final int index) {
        ResolvableType resolvableType = ResolvableType.forClass(clazz);
        return (Class<G>) resolvableType.getGeneric(index).resolve();
    }

    /**
     * 根据索引获得指定类上声明的泛型类型
     *
     * @param field
     * @param index
     * @param <G>       - 字段上的泛型类型
     * @return
     */
    public static <G> Class<G> getFieldGenericType(final Field field, final int index) {
        ResolvableType resolvableType = ResolvableType.forField(field);
        return (Class<G>) resolvableType.getGeneric(index).resolve();
    }

    /**
     * 根据className反射得到对应的Class对象，如果对应的类不存在则返回null
     *
     * @param className - 类名
     * @return 返回对应的Class对象，如果不存在则返回null
     */
    public static Class<?> forName(String className) {
        try {
            return forName(className, getDefaultClassLoader());
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

}
