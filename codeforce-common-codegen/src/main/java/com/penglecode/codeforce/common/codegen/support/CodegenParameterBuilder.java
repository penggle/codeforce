package com.penglecode.codeforce.common.codegen.support;

import com.penglecode.codeforce.common.codegen.exception.CodegenRuntimeException;
import com.penglecode.codeforce.common.codegen.util.CodegenUtils;
import com.penglecode.codeforce.common.codegen.config.DomainObjectConfig;
import com.penglecode.codeforce.common.codegen.config.GenerableTargetConfig;
import com.penglecode.codeforce.common.codegen.config.ModuleCodegenConfigProperties;
import com.penglecode.codeforce.common.util.ClassUtils;
import com.penglecode.codeforce.common.util.DateTimeUtils;
import com.penglecode.codeforce.common.util.StringUtils;

/**
 * 代码生成模板参数Builder
 *
 * @author pengpeng
 * @version 1.0
 */
public abstract class CodegenParameterBuilder<C extends ModuleCodegenConfigProperties, T extends GenerableTargetConfig, D extends DomainObjectConfig, P extends CodegenParameter> {

    /** 代码生成配置 */
    private final C codegenConfig;

    /** 当前生成目标配置 */
    private final T targetConfig;

    /** 当前生成目标绑定的领域对象 */
    private final D domainObjectConfig;

    protected CodegenParameterBuilder(CodegenContext<C,T,D> codegenContext) {
        this(codegenContext.getCodegenConfig(), codegenContext.getTargetConfig(), codegenContext.getDomainObjectConfig());
    }

    protected CodegenParameterBuilder(C codegenConfig, T targetConfig, D domainObjectConfig) {
        this.codegenConfig = codegenConfig;
        this.targetConfig = targetConfig;
        this.domainObjectConfig = domainObjectConfig;
    }

    /**
     * 构建代码生成参数
     * @return
     */
    public P buildCodegenParameter() {
        Class<P> parameterType = ClassUtils.getSuperGenericType(getClass(), CodegenParameterBuilder.class, 3);
        try {
            P codegenParameter = parameterType.getConstructor(String.class).newInstance(getTargetTemplateName());
            return setCustomCodegenParameter(setCommonCodegenParameter(codegenParameter));
        } catch (Exception e) {
            throw new CodegenRuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 设置代码生成参数的公共部分
     * @param codegenParameter
     * @return
     */
    protected P setCommonCodegenParameter(P codegenParameter) {
        String domainObjectName = domainObjectConfig.getDomainObjectName();
        codegenParameter.setTargetFileName(targetConfig.getGeneratedTargetName(domainObjectName, false, true));
        codegenParameter.setTargetPackage(targetConfig.getTargetPackage());
        codegenParameter.setTargetClass(targetConfig.getGeneratedTargetName(domainObjectName, false, false));
        codegenParameter.setTargetAuthor(codegenConfig.getDomain().getDomainCommons().getTargetAuthor());
        codegenParameter.setTargetVersion(codegenConfig.getDomain().getDomainCommons().getTargetVersion());
        codegenParameter.setTargetCreated(DateTimeUtils.formatNow("yyyy'年'MM'月'dd'日' a HH:mm"));
        return codegenParameter;
    }

    /**
     * 设置代码生成参数的自定义部分
     * @param codegenParameter
     * @return
     */
    protected abstract P setCustomCodegenParameter(P codegenParameter);

    /**
     * 获取生成目标对象的模板名称
     * @return
     */
    protected abstract String getTargetTemplateName();

    /**
     * 创建领域对象参数
     * @param domainObjectConfig
     * @return
     */
    protected DomainObjectParameter createDomainObjectParameter(D domainObjectConfig) {
        DomainObjectParameter parameter = new DomainObjectParameter();
        parameter.setDomainObjectName(domainObjectConfig.getDomainObjectName());
        parameter.setDomainObjectTitle(domainObjectConfig.getDomainObjectTitle());
        parameter.setDomainObjectAlias(domainObjectConfig.getDomainObjectAlias());
        parameter.setLowerDomainObjectAlias(StringUtils.lowerCaseFirstChar(domainObjectConfig.getDomainObjectAlias()));
        parameter.setDomainObjectsAlias(CodegenUtils.getPluralName(parameter.getDomainObjectAlias()));
        parameter.setLowerDomainObjectsAlias(StringUtils.lowerCaseFirstChar(parameter.getDomainObjectsAlias()));
        parameter.setLowerDomainObjectName(StringUtils.lowerCaseFirstChar(domainObjectConfig.getDomainObjectAlias()));
        parameter.setLowerDomainObjectsName(CodegenUtils.getPluralName(parameter.getLowerDomainObjectName()));
        //考虑到联合主键，所以统一取做id/ids
        parameter.setDomainObjectIdName("id");
        parameter.setUpperDomainObjectIdName(StringUtils.upperCaseFirstChar(parameter.getDomainObjectIdName()));
        parameter.setDomainObjectIdsName("ids");
        parameter.setUpperDomainObjectIdsName(StringUtils.upperCaseFirstChar(parameter.getDomainObjectIdsName()));
        return parameter;
    }

    protected C getCodegenConfig() {
        return codegenConfig;
    }

    protected T getTargetConfig() {
        return targetConfig;
    }

    protected D getDomainObjectConfig() {
        return domainObjectConfig;
    }

}
