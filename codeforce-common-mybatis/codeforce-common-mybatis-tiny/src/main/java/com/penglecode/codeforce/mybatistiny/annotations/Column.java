package com.penglecode.codeforce.mybatistiny.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 映射数据库表的列
 *
 * @author pengpeng
 * @version 1.0
 */
@Documented
@Retention(RUNTIME)
@Target({FIELD})
public @interface Column {

    /**
     * 当前被注释的字段是否包含在INSERT列中? 默认true
     */
    boolean insertable() default true;

    /**
     * 当前被注释的字段是否包含在UPDATE列中? 默认true
     */
    boolean updatable() default true;

}
