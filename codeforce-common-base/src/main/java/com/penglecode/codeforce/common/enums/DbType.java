package com.penglecode.codeforce.common.enums;

/**
 * 主流关系型数据库类型
 *
 * @author pengpeng
 * @version 1.0.0
 */
public enum DbType {
    MYSQL("mysql", "MySql数据库"),
    MARIADB("mariadb", "MariaDB数据库"),
    ORACLE("oracle", "Oracle数据库"),
    DB2("db2", "DB2数据库"),
    H2("h2", "H2数据库"),
    HSQL("hsql", "HSQL数据库"),
    SQLITE("sqlite", "SQLite数据库"),
    POSTGRESQL("postgresql", "PostgreSQL数据库"),
    SQLSERVER("sqlserver", "SQLServer数据库"),
    SYBASE("sybase", "Sybase数据库"),
    CLICKHOUSE("clickhouse", "ClickHouse数据库");

    private final String name;

    private final String desc;

    DbType(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public static DbType of(String name) {
        for(DbType type : values()) {
            if(type.getName().equals(name)) {
                return type;
            }
        }
        return null;
    }

}