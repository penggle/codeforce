package com.penglecode.codeforce.common.codegen.config;

/**
 * API接口代码生成配置
 *
 * @author pengpeng
 * @version 1.0
 */
public class ApiConfig {

    /** API接口URL前缀 */
    private String apiUrlPrefix = "/api";

    /** API层的公共基础包,其下约定有子包：dto、request、response、service、controller五个子包 */
    private String targetPackage;

    /** API接口客户端配置 */
    private ApiClientConfig clientConfig;

    /** API接口实现配置 */
    private ApiRuntimeConfig runtimeConfig;

    public String getApiUrlPrefix() {
        return apiUrlPrefix;
    }

    public void setApiUrlPrefix(String apiUrlPrefix) {
        this.apiUrlPrefix = apiUrlPrefix;
    }

    public String getTargetPackage() {
        return targetPackage;
    }

    public void setTargetPackage(String targetPackage) {
        this.targetPackage = targetPackage;
    }

    public ApiClientConfig getClientConfig() {
        return clientConfig;
    }

    public void setClientConfig(ApiClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }

    public ApiRuntimeConfig getRuntimeConfig() {
        return runtimeConfig;
    }

    public void setRuntimeConfig(ApiRuntimeConfig runtimeConfig) {
        this.runtimeConfig = runtimeConfig;
    }

}