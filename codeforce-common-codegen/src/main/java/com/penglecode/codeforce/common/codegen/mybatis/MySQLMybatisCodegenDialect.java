package com.penglecode.codeforce.common.codegen.mybatis;

import com.penglecode.codeforce.common.codegen.config.DomainEntityColumnConfig;
import com.penglecode.codeforce.common.codegen.config.DomainEntityConfig;
import com.penglecode.codeforce.common.codegen.config.MybatisCodegenConfigProperties;
import com.penglecode.codeforce.common.codegen.config.MybatisXmlMapperConfig;
import com.penglecode.codeforce.common.codegen.support.CodegenContext;
import com.penglecode.codeforce.common.enums.DbType;

import java.sql.Types;

/**
 * 基于MySQL数据库的Mybatis模块代码生成方言
 *
 * @author pengpeng
 * @version 1.0
 */
public class MySQLMybatisCodegenDialect implements MybatisCodegenDialect {

    @Override
    public DbType getDatabaseType() {
        return DbType.MYSQL;
    }

    @Override
    public String getSelectColumnClause(String columnName, CodegenContext<MybatisCodegenConfigProperties, MybatisXmlMapperConfig, DomainEntityConfig> codegenContext) {
        DomainEntityColumnConfig domainEntityColumnConfig = codegenContext.getDomainObjectConfig().getDomainEntityColumns().get(columnName);
        String columnSelects = domainEntityColumnConfig.getColumnName();
        int jdbcType = domainEntityColumnConfig.getIntrospectedColumn().getJdbcType();
        if(Types.TIMESTAMP == jdbcType) {
            return "DATE_FORMAT(" + columnSelects + ", '%Y-%m-%d %T')";
        } else if (Types.DATE == jdbcType) {
            return "DATE_FORMAT(" + columnSelects + ", '%Y-%m-%d')";
        }
        return columnSelects;
    }

    @Override
    public String getDeleteTargetAliasName(CodegenContext<MybatisCodegenConfigProperties,MybatisXmlMapperConfig,DomainEntityConfig> codegenContext) {
        return "";
    }

}
