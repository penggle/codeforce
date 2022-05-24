package com.penglecode.codeforce.common.codegen.config;

import com.penglecode.codeforce.common.codegen.support.ServiceType;

/**
 * Service实现配置
 *
 * @author pengpeng
 * @version 1.0
 */
public class ServiceImplementConfig extends GenerableTargetConfig {

    private ServiceType serviceType;

    protected void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    @Override
    public String getGeneratedTargetName(String domainObjectName, boolean includePackage, boolean includeSuffix) {
        String endName = ServiceType.APPLICATION_SERVICE.equals(serviceType) ? "AppServiceImpl" : "ServiceImpl";
        return (includePackage ? getTargetPackage() + "." : "") + domainObjectName + endName + (includeSuffix ? ".java" : "");
    }

}
