package com.penglecode.codeforce.mybatistiny.support;
/**
 * 数据库分页之mysql
 *
 * @author pengpeng
 * @version 1.0
 */
public class MySQLDialect implements SqlDialect {

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

}
