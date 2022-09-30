package com.penglecode.codeforce.common.codegen.service;

import com.penglecode.codeforce.common.codegen.config.DomainEntityConfig;
import com.penglecode.codeforce.common.codegen.service.DomainServiceInterfaceCodegenParameter;

/**
 * 领域服务参数
 *
 * @author pengpeng
 * @version 1.0.0
 */
public class DomainServiceParameter {

    private final DomainServiceInterfaceCodegenParameter domainServiceCodegenParameter;

    private final DomainEntityConfig domainEntityConfig;

    private final String domainServiceName;

    private final String domainServiceBeanName;

    private final String domainServiceInstanceName;

    public DomainServiceParameter(DomainServiceInterfaceCodegenParameter domainServiceCodegenParameter, DomainEntityConfig domainEntityConfig, String domainServiceName, String domainServiceBeanName, String domainServiceInstanceName) {
        this.domainServiceCodegenParameter = domainServiceCodegenParameter;
        this.domainEntityConfig = domainEntityConfig;
        this.domainServiceName = domainServiceName;
        this.domainServiceBeanName = domainServiceBeanName;
        this.domainServiceInstanceName = domainServiceInstanceName;
    }

    public DomainServiceInterfaceCodegenParameter getDomainServiceCodegenParameter() {
        return domainServiceCodegenParameter;
    }

    public DomainEntityConfig getDomainEntityConfig() {
        return domainEntityConfig;
    }

    public String getDomainServiceName() {
        return domainServiceName;
    }

    public String getDomainServiceBeanName() {
        return domainServiceBeanName;
    }

    public String getDomainServiceInstanceName() {
        return domainServiceInstanceName;
    }

}