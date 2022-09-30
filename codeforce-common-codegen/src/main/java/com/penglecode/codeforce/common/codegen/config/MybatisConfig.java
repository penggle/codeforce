package com.penglecode.codeforce.common.codegen.config;

/**
 * Mybatis代码生成配置
 *
 * @author pengpeng
 * @version 1.0.0
 */
public class MybatisConfig {

    /** XxxMapper.java源码配置 */
    private MybatisJavaMapperConfig javaMapperConfig;

    public MybatisJavaMapperConfig getJavaMapperConfig() {
        return javaMapperConfig;
    }

    public void setJavaMapperConfig(MybatisJavaMapperConfig javaMapperConfig) {
        this.javaMapperConfig = javaMapperConfig;
    }

}