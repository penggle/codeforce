package com.penglecode.codeforce.common.codegen.config;

import com.penglecode.codeforce.common.codegen.support.ServiceType;

/**
 * Service接口配置
 *
 * @author pengpeng
 * @version 1.0.0
 */
public class ServiceInterfaceConfig extends GenerableTargetConfig {

    private ServiceType serviceType;

    protected void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    @Override
    public String getGeneratedTargetName(String domainObjectName, boolean includePackage, boolean includeSuffix) {
        String endName = ServiceType.APPLICATION_SERVICE.equals(serviceType) ? "AppService" : "Service";
        return (includePackage ? getTargetPackage() + "." : "") + domainObjectName + endName + (includeSuffix ? ".java" : "");
    }

}
