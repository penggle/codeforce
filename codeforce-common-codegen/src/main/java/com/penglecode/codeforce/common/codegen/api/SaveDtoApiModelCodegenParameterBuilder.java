package com.penglecode.codeforce.common.codegen.api;

import com.penglecode.codeforce.common.codegen.support.CodegenContext;
import com.penglecode.codeforce.common.codegen.config.ApiCodegenConfigProperties;
import com.penglecode.codeforce.common.codegen.config.ApiModelConfig;
import com.penglecode.codeforce.common.codegen.config.DomainEntityConfig;
import com.penglecode.codeforce.common.codegen.config.DomainEntityFieldConfig;

/**
 * (ApiModelType.SAVE_DTO)API接口数据模型代码生成参数Builder
 *
 * @author pengpeng
 * @version 1.0.0
 */
public class SaveDtoApiModelCodegenParameterBuilder extends DtoApiModelCodegenParameterBuilder {

    public SaveDtoApiModelCodegenParameterBuilder(CodegenContext<ApiCodegenConfigProperties, ApiModelConfig, DomainEntityConfig> codegenContext) {
        super(codegenContext);
    }

    public SaveDtoApiModelCodegenParameterBuilder(ApiCodegenConfigProperties codegenConfig, ApiModelConfig targetConfig, DomainEntityConfig domainObjectConfig) {
        super(codegenConfig, targetConfig, domainObjectConfig);
    }

    @Override
    protected boolean isGenerableField(DomainEntityFieldConfig domainEntityFieldConfig) {
        //不是辅助字段 && 不是create_time/update_time字段
        return !domainEntityFieldConfig.getFieldGroup().isSupportField()
                && !domainEntityFieldConfig.getDomainEntityColumnConfig().getColumnName().equals(getCodegenConfig().getDomain().getDomainCommons().getDefaultCreateTimeColumn())
                && !domainEntityFieldConfig.getDomainEntityColumnConfig().getColumnName().equals(getCodegenConfig().getDomain().getDomainCommons().getDefaultUpdateTimeColumn());
    }

}
