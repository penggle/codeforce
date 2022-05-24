package com.penglecode.codeforce.common.codegen.config;

import com.penglecode.codeforce.common.codegen.support.ApiModelType;
import com.penglecode.codeforce.common.util.TemplateUtils;

import java.util.Collections;

/**
 * API接口数据模型配置
 *
 * @author pengpeng
 * @version 1.0
 */
public class ApiModelConfig extends GenerableTargetConfig {

    private final ApiModelType modelType;

    public ApiModelConfig() {
        this(null);
    }

    protected ApiModelConfig(ApiModelType modelType) {
        this.modelType = modelType;
    }

    public ApiModelType getModelType() {
        return modelType;
    }

    @Override
    public String getGeneratedTargetName(String domainObjectName, boolean includePackage, boolean includeSuffix) {
        if(modelType != null) {
            domainObjectName = TemplateUtils.parseTemplate(modelType.getModelNameTemplate(), Collections.singletonMap("domainObjectName", domainObjectName));
        }
        return (includePackage ? getTargetPackage() + "." : "") + domainObjectName + (includeSuffix ? ".java" : "");
    }

    public ApiModelConfig withModelType(ApiModelType modelType) {
        ApiModelConfig newModelConfig = new ApiModelConfig(modelType);
        newModelConfig.setTargetPackage(getTargetPackage());
        newModelConfig.setTargetProject(getTargetProject());
        return newModelConfig;
    }

}
