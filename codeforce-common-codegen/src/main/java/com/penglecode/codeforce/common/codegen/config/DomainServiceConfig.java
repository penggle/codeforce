package com.penglecode.codeforce.common.codegen.config;

import com.penglecode.codeforce.common.codegen.support.ServiceType;

/**
 * 领域服务代码生成配置
 *
 * @author pengpeng
 * @version 1.0.0
 */
public class DomainServiceConfig {

    /** 领域服务接口配置 */
    private ServiceInterfaceConfig interfaceConfig;

    /** 领域服务实现配置 */
    private ServiceImplementConfig implementConfig;

    public ServiceInterfaceConfig getInterfaceConfig() {
        return interfaceConfig;
    }

    public void setInterfaceConfig(ServiceInterfaceConfig interfaceConfig) {
        this.interfaceConfig = interfaceConfig;
        if(this.interfaceConfig != null) {
            this.interfaceConfig.setServiceType(ServiceType.DOMAIN_SERVICE);
        }
    }

    public ServiceImplementConfig getImplementConfig() {
        return implementConfig;
    }

    public void setImplementConfig(ServiceImplementConfig implementConfig) {
        this.implementConfig = implementConfig;
        if(this.interfaceConfig != null) {
            this.interfaceConfig.setServiceType(ServiceType.DOMAIN_SERVICE);
        }
    }

}