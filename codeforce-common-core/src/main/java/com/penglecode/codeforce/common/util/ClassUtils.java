package com.penglecode.codeforce.common.util;

import org.springframework.core.ResolvableType;

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

}
