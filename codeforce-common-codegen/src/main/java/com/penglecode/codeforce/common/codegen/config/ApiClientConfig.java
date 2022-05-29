package com.penglecode.codeforce.common.codegen.config;

import com.penglecode.codeforce.common.codegen.support.ApiProtocol;
import com.penglecode.codeforce.common.util.CollectionUtils;
import com.penglecode.codeforce.common.util.StringUtils;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * API接口Client配置
 *
 * @author pengpeng
 * @version 1.0
 */
public class ApiClientConfig extends ApiProviderConfig {

    /** API接口协议,feign是必须的,dubbo是可选的 */
    private Set<ApiProtocol> apiProtocols;

    /** API接口继承interface列表 */
    private Set<Class<?>> apiExtendsInterfaces;

    public Set<ApiProtocol> getApiProtocols() {
        return apiProtocols;
    }

    public void setApiProtocols(String apiProtocols) {
        Set<ApiProtocol> apiProtocolSet = null;
        if(StringUtils.isNotBlank(apiProtocols)) {
            apiProtocolSet = Stream.of(apiProtocols.split(",")).map(protocol -> {
                try {
                    return ApiProtocol.valueOf(protocol);
                } catch (IllegalArgumentException e) {
                    return null;
                }
            }).filter(Objects::nonNull).collect(Collectors.toSet());
        }
        this.apiProtocols = CollectionUtils.defaultIfEmpty(apiProtocolSet, Collections.singleton(ApiProtocol.FEIGN));
    }

    public Set<Class<?>> getApiExtendsInterfaces() {
        return apiExtendsInterfaces;
    }

    public void setApiExtendsInterfaces(Set<Class<?>> apiExtendsInterfaces) {
        this.apiExtendsInterfaces = apiExtendsInterfaces;
    }

    @Override
    public String getGeneratedTargetName(String domainObjectName, boolean includePackage, boolean includeSuffix) {
        return (includePackage ? getTargetPackage() + "." : "") + domainObjectName + "ApiService" + (includeSuffix ? ".java" : "");
    }

}
