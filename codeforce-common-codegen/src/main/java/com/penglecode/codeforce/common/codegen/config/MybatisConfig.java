package com.penglecode.codeforce.common.codegen.config;

/**
 * Mybatis代码生成配置
 *
 * @author pengpeng
 * @version 1.0
 */
public class MybatisConfig {

    /** XxxMapper.xml配置 */
    private MybatisXmlMapperConfig xmlMapperConfig;

    /** XxxMapper.java源码配置 */
    private MybatisJavaMapperConfig javaMapperConfig;

    public MybatisXmlMapperConfig getXmlMapperConfig() {
        return xmlMapperConfig;
    }

    public void setXmlMapperConfig(MybatisXmlMapperConfig xmlMapperConfig) {
        this.xmlMapperConfig = xmlMapperConfig;
    }

    public MybatisJavaMapperConfig getJavaMapperConfig() {
        return javaMapperConfig;
    }

    public void setJavaMapperConfig(MybatisJavaMapperConfig javaMapperConfig) {
        this.javaMapperConfig = javaMapperConfig;
    }

}