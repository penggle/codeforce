package com.penglecode.codeforce.common.codegen.config;

import org.springframework.util.Assert;

/**
 * Mybatis代码生成配置
 *
 * @author pengpeng
 * @version 1.0.0
 */
public class MybatisCodegenConfigProperties extends DomainObjectCodegenConfigProperties {

    /** Mybatis代码生成配置 */
    private MybatisConfig mybatis;

    public MybatisCodegenConfigProperties(String module) {
        super(module);
    }

    public MybatisConfig getMybatis() {
        return mybatis;
    }

    public void setMybatis(MybatisConfig mybatis) {
        this.mybatis = mybatis;
    }

    @Override
    protected void validateCodegenConfig() throws Exception {
        super.validateCodegenConfig();
        validateMybatisCodegenConfig();
    }

    /**
     * 校验Mybatis代码生成配置
     */
    protected void validateMybatisCodegenConfig() {
        String codegenConfigPrefix = getCodegenConfigPrefix(getModule());
        Assert.hasText(mybatis.getJavaMapperConfig().getTargetProject(), String.format("Mybatis代码生成配置(%s.mybatis.javaMapperConfig.targetProject)必须指定!", codegenConfigPrefix));
        Assert.hasText(mybatis.getJavaMapperConfig().getTargetPackage(), String.format("Mybatis代码生成配置(%s.mybatis.javaMapperConfig.targetPackage)必须指定!", codegenConfigPrefix));
    }

    @Override
    protected void initCodegenConfig() throws Exception {
        super.initCodegenConfig();
        initMybatisCodegenConfig();
    }

    /**
     * 初始化Mybatis代码生成相关配置
     */
    public void initMybatisCodegenConfig() {
        //1、初始化领域对象配置中的mapperAnnotations
        mybatis.getJavaMapperConfig().initMapperAnnotations(getDomain());
    }

}
