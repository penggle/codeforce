package com.penglecode.codeforce.mybatistiny.support;

import org.springframework.core.ResolvableType;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * 通用工具集合
 *
 * @author pengpeng
 * @version 1.0
 */
@SuppressWarnings("unchecked")
public class Utilities {

    private Utilities() {}

    /**
     * mobile_phone 转 mobilePhone
     *
     * @param paramName
     * @return
     */
    public static String snakeNamingToCamel(String paramName) {
        int fromIndex = 0;
        int targetIndex;
        StringBuilder sb = new StringBuilder(paramName);
        while((targetIndex = sb.indexOf("_", fromIndex)) != -1) {
            fromIndex = targetIndex + 1;
            sb.setCharAt(fromIndex, Character.toUpperCase(sb.charAt(fromIndex)));
        }
        return sb.toString().replace("_", "");
    }

    /**
     * mobilePhone 转 mobile_phone
     *
     * @param paramName
     * @return
     */
    public static String camelNamingToSnake(String paramName) {
        StringBuilder sb = new StringBuilder();
        sb.append(Character.toLowerCase(paramName.charAt(0)));
        for(int i = 1, len = paramName.length(); i < len; i++) {
            char ch = paramName.charAt(i);
            if(Character.isUpperCase(ch)) {
                ch = Character.toLowerCase(ch);
                sb.append("_");
            }
            sb.append(ch);
        }
        return sb.toString();
    }

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
     * <p>在目标对象上获取属性字段的值</p>
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
            ReflectionUtils.handleReflectionException(e);
            return null;
        }
    }

    /**
     * <p>在目标对象上获取属性字段的值</p>
     *
     * @param target
     * @param fieldName
     * @return
     */
    public static <T> T getFieldValue(Object target, String fieldName) {
        Field field = ReflectionUtils.findField(target.getClass(), fieldName);
        return getFieldValue(field, target);
    }

    /**
     * 修改final字段的值
     * @param target
     * @param fieldName
     * @param value
     */
    public static void setFinalFieldValue(Object target, String fieldName, Object value) {
        setFinalFieldValue(target, ReflectionUtils.findField(target.getClass(), fieldName), value);
    }

    /**
     * 修改final字段的值
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
            ReflectionUtils.handleReflectionException(e);
        }
    }

}
