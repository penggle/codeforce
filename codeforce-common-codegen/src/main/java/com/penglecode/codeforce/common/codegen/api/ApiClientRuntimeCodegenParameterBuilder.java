package com.penglecode.codeforce.common.codegen.api;

import com.penglecode.codeforce.common.codegen.support.ApiProtocol;
import com.penglecode.codeforce.common.codegen.support.CodegenContext;
import com.penglecode.codeforce.common.codegen.config.ApiClientConfig;
import com.penglecode.codeforce.common.codegen.config.ApiCodegenConfigProperties;
import com.penglecode.codeforce.common.codegen.config.ApiRuntimeConfig;
import com.penglecode.codeforce.common.codegen.config.DomainObjectConfig;
import com.penglecode.codeforce.common.codegen.support.FullyQualifiedJavaType;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

/**
 * API接口的Client实现代码生成参数Builder
 *
 * @author pengpeng
 * @version 1.0.0
 */
public class ApiClientRuntimeCodegenParameterBuilder<D extends DomainObjectConfig> extends ApiRuntimeCodegenParameterBuilder<D> {

    public ApiClientRuntimeCodegenParameterBuilder(CodegenContext<ApiCodegenConfigProperties, ApiRuntimeConfig, D> codegenContext) {
        super(codegenContext);
    }

    public ApiClientRuntimeCodegenParameterBuilder(ApiCodegenConfigProperties codegenConfig, ApiRuntimeConfig targetConfig, D domainObjectConfig) {
        super(codegenConfig, targetConfig, domainObjectConfig);
    }

    @Override
    protected ApiRuntimeCodegenParameter setCommonCodegenParameter(ApiRuntimeCodegenParameter codegenParameter) {
        codegenParameter = super.setCommonCodegenParameter(codegenParameter);
        codegenParameter.setTargetAnnotations(Collections.singletonList("@RestController"));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(RestController.class.getName()));
        ApiClientConfig apiClientConfig = getCodegenConfig().getApi().getClientConfig();
        DomainObjectConfig domainObjectConfig = getDomainObjectConfig();
        if(apiClientConfig.getApiProtocols().contains(ApiProtocol.DUBBO)) {
            //TODO 添加@DubboService注解
        }
        if(getTargetConfig().getApiExtendsClass() != null) {
            codegenParameter.setTargetExtends(getTargetConfig().getApiExtendsClass().getSimpleName());
            codegenParameter.addTargetImportType(new FullyQualifiedJavaType(getTargetConfig().getApiExtendsClass().getName()));
        }
        codegenParameter.setTargetImplements(Collections.singletonList(apiClientConfig.getGeneratedTargetName(domainObjectConfig.getDomainObjectAlias(), false, false)));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(apiClientConfig.getGeneratedTargetName(domainObjectConfig.getDomainObjectAlias(), true, false)));
        return codegenParameter;
    }

    @Override
    protected String getTargetTemplateName() {
        return "ApiClientRuntime.ftl";
    }

}
