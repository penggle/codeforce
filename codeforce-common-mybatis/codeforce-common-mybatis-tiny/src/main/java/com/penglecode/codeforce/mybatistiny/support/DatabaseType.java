package com.penglecode.codeforce.mybatistiny.support;

/**
 * 支持的数据库类型
 *
 * @author pengpeng
 * @version 1.0
 */
public enum DatabaseType {

    MYSQL, ORACLE;

    public static DatabaseType of(String name) {
        for(DatabaseType em : values()) {
            if(em.name().equalsIgnoreCase(name)) {
                return em;
            }
        }
        throw new UnsupportedOperationException("Unsupported Database Type: " + name);
    }

}
