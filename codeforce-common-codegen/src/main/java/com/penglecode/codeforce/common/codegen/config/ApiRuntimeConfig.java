package com.penglecode.codeforce.common.codegen.config;

import com.penglecode.codeforce.common.codegen.support.ApiMethod;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Set;

/**
 * API接口实现配置
 *
 * @author pengpeng
 * @version 1.0
 */
public class ApiRuntimeConfig extends GenerableTargetConfig {

    /** API接口声明Map类型,[key=领域对象名称,value=接口方法名枚举] */
    private Map<String,Set<ApiMethod>> apiProviders;

    /** API接口继承父类(可以不配置) */
    private Class<?> apiExtendsClass;

    public Map<String, Set<ApiMethod>> getApiProviders() {
        return apiProviders;
    }

    public void setApiProviders(Map<String, Set<ApiMethod>> apiProviders) {
        this.apiProviders = apiProviders;
    }

    public Class<?> getApiExtendsClass() {
        return apiExtendsClass;
    }

    public void setApiExtendsClass(Class<?> apiExtendsClass) {
        this.apiExtendsClass = apiExtendsClass;
    }

    @Override
    public String getGeneratedTargetName(String domainObjectName, boolean includePackage, boolean includeSuffix) {
        String endName = !CollectionUtils.isEmpty(apiProviders) && apiProviders.containsKey(domainObjectName) ? "Controller" : "ApiServiceImpl";
        return (includePackage ? getTargetPackage() + "." : "") + domainObjectName + endName + (includeSuffix ? ".java" : "");
    }

}