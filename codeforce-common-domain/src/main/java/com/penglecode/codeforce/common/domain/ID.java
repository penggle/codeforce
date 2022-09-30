package com.penglecode.codeforce.common.domain;

import com.penglecode.codeforce.common.support.BeanIntrospector;
import com.penglecode.codeforce.common.support.SerializableFunction;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 联合主键
 *
 * @author pengpeng
 * @version 1.0.0
 */
public class ID extends LinkedHashMap<String,Object> {

    private static final long serialVersionUID = 1L;

    public ID() {
        super();
    }

    public ID(Map<? extends String, Object> m) {
        super(m);
    }

    public ID addKey(String key, Object value) {
        put(key, value);
        return this;
    }

    public <E extends EntityObject, K extends Serializable> ID addKey(SerializableFunction<E,K> keyReference, K keyValue) {
        Field keyField = BeanIntrospector.introspectField(keyReference);
        put(keyField.getName(), keyValue);
        return this;
    }

}
