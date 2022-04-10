package com.penglecode.codeforce.mybatistiny.core;

import com.penglecode.codeforce.mybatistiny.dsl.QueryCriteria;

/**
 * 数据库方言
 *
 * @author pengpeng
 * @version 1.0
 */
public enum DatabaseDialect {

    /**
     * 基于Oracle数据库的方言
     */
    ORACLE() {

        private static final String DEFAULT_PAGING_SQL_FORMAT = "SELECT *"
                                                               + " FROM (SELECT rownum rn_, page_inner_table.*"
                                                                       + " FROM (%s) page_inner_table"
                                                                      + " WHERE rownum <= %s) page_outer_table"
                                                              + " WHERE page_outer_table.rn_ > %s";

        @Override
        public String getPageSql(String sql, int offset, int limit) {
            String upperSql = sql.toUpperCase();
            if(upperSql.startsWith("SELECT")) {
                return String.format(DEFAULT_PAGING_SQL_FORMAT, sql, offset + limit, offset);
            }
            return sql;
        }

        @Override
        public String getLimitSql(String sql, int limit) {
            String upperSql = sql.toUpperCase();
            if(upperSql.startsWith("SELECT")) {
                return "SELECT * FROM (" + sql + ") WHERE rownum <= " + limit;
            } else if(upperSql.startsWith("UPDATE") || upperSql.startsWith("DELETE")) { //此分支实现对于复杂SQL可能会存在问题
                if(upperSql.contains(" WHERE ")) {
                    return sql + " AND rownum <= " + limit;
                } else {
                    return sql + " WHERE rownum <= " + limit;
                }
            }
            return sql;
        }

    },

    /**
     * 基于MySQL数据库的方言
     */
    MYSQL() {
        @Override
        public String getPageSql(String sql, int offset, int limit) {
            String upperSql = sql.toUpperCase();
            if(upperSql.startsWith("SELECT")) {
                return sql + " LIMIT " + offset + ", " + limit;
            }
            return sql;
        }

        @Override
        public String getLimitSql(String sql, int limit) {
            String upperSql = sql.toUpperCase();
            if(upperSql.startsWith("SELECT") || upperSql.startsWith("UPDATE") || upperSql.startsWith("DELETE")) {
                return sql + " LIMIT " + limit;
            }
            return sql;
        }

        @Override
        public String getDeleteTargetAlias() {
            return QueryCriteria.TABLE_ALIAS_NAME;
        }
    };

    /**
     * 根据原始查询sql语句及分页参数获取分页sql,
     * (注意：如果SQL语句中使用了left join、right join查询一对多的结果集时,请不要使用该分页处理机制,得另寻他法)
     * @param sql
     * @param offset	- 起始记录行数(从0开始)
     * @param limit		- 从起始记录行数offset开始获取limit行记录
     * @return
     */
    public abstract String getPageSql(String sql, int offset, int limit);

    /**
     * 根据原始查询sql语句及limit参数获取限制查询返回记录数的sql
     *
     * @param sql
     * @param limit
     * @return
     */
    public abstract String getLimitSql(String sql, int limit);

    /**
     * DELETE语句别名方言
     *
     * @return
     */
    public String getDeleteTargetAlias() {
        return "";
    }

    public static DatabaseDialect of(String databaseId) {
        for(DatabaseDialect dbDialect : values()) {
            if(dbDialect.name().equalsIgnoreCase(databaseId)) {
                return dbDialect;
            }
        }
        return null;
    }

}
