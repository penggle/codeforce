package com.penglecode.codeforce.common.codegen.service;

import com.penglecode.codeforce.common.codegen.config.DomainEntityConfig;
import com.penglecode.codeforce.common.codegen.config.ServiceCodegenConfigProperties;
import com.penglecode.codeforce.common.codegen.config.ServiceInterfaceConfig;
import com.penglecode.codeforce.common.codegen.support.CodegenContext;

/**
 * 领域实体的领域服务接口代码生成参数Builder
 *
 * @author pengpeng
 * @version 1.0.0
 */
public class DomainServiceInterfaceCodegenParameterBuilder extends AbstractDomainServiceCodegenParameterBuilder<ServiceInterfaceConfig, DomainServiceInterfaceCodegenParameter> {

    public DomainServiceInterfaceCodegenParameterBuilder(CodegenContext<ServiceCodegenConfigProperties, ServiceInterfaceConfig, DomainEntityConfig> codegenContext) {
        super(codegenContext);
    }

    public DomainServiceInterfaceCodegenParameterBuilder(ServiceCodegenConfigProperties codegenConfig, ServiceInterfaceConfig targetConfig, DomainEntityConfig domainObjectConfig) {
        super(codegenConfig, targetConfig, domainObjectConfig);
    }

    @Override
    protected DomainServiceInterfaceCodegenParameter setCustomCodegenParameter(DomainServiceInterfaceCodegenParameter codegenParameter) {
        codegenParameter.setTargetComment(getDomainObjectConfig().getDomainObjectTitle() + "领域服务接口");
        return super.setCustomCodegenParameter(codegenParameter);
    }

    @Override
    protected String getTargetTemplateName() {
        return "DomainServiceInterface.ftl";
    }

}
