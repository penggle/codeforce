package com.penglecode.codeforce.common.codegen.mybatis;


import com.penglecode.codeforce.common.enums.DbType;

/**
 * 默认的Mybatis模块代码生成方言
 *
 * @author pengpeng
 * @version 1.0
 */
public class DefaultMybatisCodegenDialect implements MybatisCodegenDialect {

    @Override
    public DbType getDatabaseType() {
        return null; //返回空代表未知的(暂不支持的)
    }

}
