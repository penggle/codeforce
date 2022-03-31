package com.penglecode.codeforce.common.domain;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 联合主键
 *
 * @author pengpeng
 * @version 1.0
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

}
