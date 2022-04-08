package com.penglecode.codeforce.mybatistiny.support;

import com.penglecode.codeforce.common.domain.EntityObject;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Mybatis-Tiny全局配置
 *
 * @author pengpeng
 * @version 1.0
 */
@SuppressWarnings("unchecked")
public class GlobalConfig {

    /**
     * 全部已注册的实体定义
     */
    private static final ConcurrentMap<Class<? extends EntityObject>, EntityMeta<? extends EntityObject>> allEntityMetas = new ConcurrentHashMap<>();

    /**
     * 根据实体Class获取实体定义
     *
     * @param entityClass
     * @param <E>
     * @return
     */
    public static <E extends EntityObject> EntityMeta<E> getEntityMeta(Class<E> entityClass) {
        return (EntityMeta<E>) allEntityMetas.get(entityClass);
    }

    protected static <E extends EntityObject> void setEntityMeta(Class<E> entityClass, EntityMeta<E> entityMeta) {
        allEntityMetas.put(entityClass, entityMeta);
    }

}
