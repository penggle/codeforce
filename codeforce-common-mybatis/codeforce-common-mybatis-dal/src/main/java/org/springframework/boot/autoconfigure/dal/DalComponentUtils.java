package org.springframework.boot.autoconfigure.dal;

import com.penglecode.codeforce.common.util.SpringUtils;
import org.springframework.util.ClassUtils;

/**
 * DAL层组件bean工具类
 *
 * @author pengpeng
 * @version 1.0
 */
public class DalComponentUtils {

    private DalComponentUtils() {}

    public static String genBeanNameOfType(String database, Class<?> beanType) {
        return database + beanType.getSimpleName();
    }

    public static String genBeanNameOfType(String database, String beanTypeName) {
        return database + ClassUtils.getShortName(beanTypeName);
    }

    public static <T> T getBeanOfType(String database, Class<T> beanType) {
        return SpringUtils.getBean(genBeanNameOfType(database, beanType), beanType);
    }

    public static <T> T getBeanOfType(String database, String beanTypeName) {
        return SpringUtils.getBean(genBeanNameOfType(database, beanTypeName));
    }

}
