package com.penglecode.codeforce.common.codegen.config;

import org.springframework.util.CollectionUtils;

/**
 * API接口实现配置
 *
 * @author pengpeng
 * @version 1.0
 */
public class ApiRuntimeConfig extends ApiProviderConfig {

    /** API接口继承父类(可以不配置) */
    private Class<?> apiExtendsClass;

    public Class<?> getApiExtendsClass() {
        return apiExtendsClass;
    }

    public void setApiExtendsClass(Class<?> apiExtendsClass) {
        this.apiExtendsClass = apiExtendsClass;
    }

    @Override
    public String getGeneratedTargetName(String domainObjectName, boolean includePackage, boolean includeSuffix) {
        String endName = !CollectionUtils.isEmpty(getApiProviders()) && getApiProviders().containsKey(domainObjectName) ? "Controller" : "ApiServiceImpl";
        return (includePackage ? getTargetPackage() + "." : "") + domainObjectName + endName + (includeSuffix ? ".java" : "");
    }

}