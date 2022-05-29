package com.penglecode.codeforce.common.codegen.config;

import com.penglecode.codeforce.common.codegen.support.ApiMethod;
import com.penglecode.codeforce.common.codegen.support.ApiProtocol;
import com.penglecode.codeforce.common.web.servlet.support.ServletHttpApiSupport;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Api代码生成配置
 *
 * @author pengpeng
 * @version 1.0
 */
public class ApiCodegenConfigProperties extends ServiceCodegenConfigProperties {

    private ApiConfig api;

    public ApiCodegenConfigProperties(String module) {
        super(module);
    }

    public ApiConfig getApi() {
        return api;
    }

    public void setApi(ApiConfig api) {
        this.api = api;
    }

    @Override
    protected void validateCodegenConfig() throws Exception {
        super.validateCodegenConfig();
        validateApiCodegenConfig();
    }

    @Override
    protected void initCodegenConfig() throws Exception {
        super.initCodegenConfig();
        initApiCodegenConfig();
    }

    /**
     * 校验Api代码生成配置
     */
    protected void validateApiCodegenConfig() {
        String codegenConfigPrefix = getCodegenConfigPrefix(getModule());
        //忽略xxx.api.targetProject的公共配置
        Assert.hasText(api.getClientConfig().getTargetProject(), String.format("Api代码生成配置(%s.api.clientConfig.targetProject)必须指定!", codegenConfigPrefix));
        Assert.hasText(api.getRuntimeConfig().getTargetProject(), String.format("Api代码生成配置(%s.api.runtimeConfig.targetProject)必须指定!", codegenConfigPrefix));
        Assert.hasText(api.getTargetPackage(), String.format("Api代码生成配置(%s.api.targetPackage)必须指定!", codegenConfigPrefix));
        Assert.isTrue(!CollectionUtils.isEmpty(api.getClientConfig().getApiProviders()) || !CollectionUtils.isEmpty(api.getRuntimeConfig().getApiProviders()), String.format("Api代码生成配置(%s.api.clientConfig.apiDeclarations|%s.api.runtimeConfig.apiDeclarations)必须指定之一!", codegenConfigPrefix, codegenConfigPrefix));
        for(Map.Entry<String,Set<ApiMethod>> entry : api.getClientConfig().getApiProviders().entrySet()) {
            Assert.notEmpty(entry.getValue(), String.format("Api代码生成配置(%s.api.clientConfig.apiProviders.%s)不能为空!", codegenConfigPrefix, entry.getKey()));
            Assert.isTrue(api.getRuntimeConfig().getApiProviders().containsKey(entry.getKey()), String.format("Api接口(domainObjectName = %s)定义只能存在于client和runtime当中之一!", entry.getKey()));
        }
        for(Map.Entry<String,Set<ApiMethod>> entry : api.getRuntimeConfig().getApiProviders().entrySet()) {
            Assert.notEmpty(entry.getValue(), String.format("Api代码生成配置(%s.api.runtimeConfig.apiProviders.%s)不能为空!", codegenConfigPrefix, entry.getKey()));
            Assert.isTrue(api.getClientConfig().getApiProviders().containsKey(entry.getKey()), String.format("Api接口(domainObjectName = %s)定义只能存在于client和runtime当中之一!", entry.getKey()));
        }
    }

    /**
     * 初始化Api代码生成配置
     */
    protected void initApiCodegenConfig() {
        Set<ApiProtocol> apiProtocols = api.getClientConfig().getApiProtocols();
        if(apiProtocols == null) {
            apiProtocols = new HashSet<>();
        }
        apiProtocols.add(ApiProtocol.FEIGN);
        if(api.getRuntimeConfig().getApiExtendsClass() == null) {
            api.getRuntimeConfig().setApiExtendsClass(ServletHttpApiSupport.class);
        }
        //强制覆盖clientConfig和runtimeConfig的targetPackage
        api.getClientConfig().setTargetPackage(api.getTargetPackage());
        api.getRuntimeConfig().setTargetPackage(api.getTargetPackage());
        /*
         * 这里可以通过ApiModelConfig的构造器灵活控制API的数据模型到底放在哪里？
         * 有两种方案：
         * 1、client模块和runtime都会放(默认策略)
         * 2、只放client模块中
         */
        api.getClientConfig().setApiModelConfig(new ApiModelConfig(api.getClientConfig()));
        api.getRuntimeConfig().setApiModelConfig(new ApiModelConfig(api.getRuntimeConfig()));
    }

}
