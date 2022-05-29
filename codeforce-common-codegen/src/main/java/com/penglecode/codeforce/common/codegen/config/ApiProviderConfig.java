package com.penglecode.codeforce.common.codegen.config;

import com.penglecode.codeforce.common.codegen.support.ApiMethod;
import com.penglecode.codeforce.common.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * API提供者配置
 *
 * @author pengpeng
 * @version 1.0
 */
public abstract class ApiProviderConfig extends GenerableTargetConfig {

    /** API接口数据模型配置 */
    private ApiModelConfig apiModelConfig;

    /** API接口声明Map类型,[key=领域对象名称,value=接口方法名枚举] */
    private Map<String, Set<ApiMethod>> apiProviders = new LinkedHashMap<>();

    public Map<String, Set<ApiMethod>> getApiProviders() {
        return apiProviders;
    }

    public void setApiProviders(Map<String, String> apiProviders) {
        this.apiProviders = apiProviders.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> {
            String methods = entry.getValue();
            if(StringUtils.isNotBlank(methods) && !"ALL".equals(methods)) {
                return Stream.of(methods.split(",")).map(method -> {
                    try {
                        return ApiMethod.valueOf(StringUtils.trim(method));
                    } catch (Exception e) {
                        return null;
                    }
                }).filter(Objects::nonNull).collect(Collectors.toSet());
            }
            return Stream.of(ApiMethod.values()).collect(Collectors.toSet());
        }));
    }

    public ApiModelConfig getApiModelConfig() {
        return apiModelConfig;
    }

    public void setApiModelConfig(ApiModelConfig apiModelConfig) {
        this.apiModelConfig = apiModelConfig;
    }

}
