package com.penglecode.codeforce.common.codegen.service;

import com.penglecode.codeforce.common.codegen.config.DomainAggregateConfig;
import com.penglecode.codeforce.common.codegen.config.ServiceCodegenConfigProperties;
import com.penglecode.codeforce.common.codegen.config.ServiceInterfaceConfig;
import com.penglecode.codeforce.common.codegen.support.CodegenContext;

/**
 * 领域实体的领域服务接口代码生成参数Builder
 *
 * @author pengpeng
 * @version 1.0.0
 */
public class ApplicationServiceInterfaceCodegenParameterBuilder extends AbstractApplicationServiceCodegenParameterBuilder<ServiceInterfaceConfig, ApplicationServiceInterfaceCodegenParameter> {

    public ApplicationServiceInterfaceCodegenParameterBuilder(CodegenContext<ServiceCodegenConfigProperties, ServiceInterfaceConfig, DomainAggregateConfig> codegenContext) {
        super(codegenContext);
    }

    public ApplicationServiceInterfaceCodegenParameterBuilder(ServiceCodegenConfigProperties codegenConfig, ServiceInterfaceConfig targetConfig, DomainAggregateConfig domainObjectConfig) {
        super(codegenConfig, targetConfig, domainObjectConfig);
    }

    @Override
    protected ApplicationServiceInterfaceCodegenParameter setCustomCodegenParameter(ApplicationServiceInterfaceCodegenParameter codegenParameter) {
        codegenParameter.setTargetComment(getDomainObjectConfig().getDomainObjectTitle() + "应用服务接口");
        return super.setCustomCodegenParameter(codegenParameter);
    }

    @Override
    protected String getTargetTemplateName() {
        return "ApplicationServiceInterface.ftl";
    }

}
