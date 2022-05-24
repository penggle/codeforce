package com.penglecode.codeforce.common.codegen.config;

import com.penglecode.codeforce.common.codegen.mybatis.MySQLMybatisCodegenDialect;
import com.penglecode.codeforce.common.codegen.mybatis.MybatisCodegenDialect;
import com.penglecode.codeforce.common.codegen.mybatis.OracleMybatisCodegenDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 模块化代码生成配置类
 *
 * @author pengpeng
 * @version 1.0
 */
@Configuration
public class ModuleCodegenConfiguration {

    @Bean
    public MybatisCodegenDialect mysqlMybatisCodegenDialect() {
        return new MySQLMybatisCodegenDialect();
    }

    @Bean
    public MybatisCodegenDialect oracleMybatisCodegenDialect() {
        return new OracleMybatisCodegenDialect();
    }

}
