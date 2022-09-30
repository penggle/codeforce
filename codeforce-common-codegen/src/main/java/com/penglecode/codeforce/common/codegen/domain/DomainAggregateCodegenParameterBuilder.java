package com.penglecode.codeforce.common.codegen.domain;

import com.penglecode.codeforce.common.codegen.config.*;
import com.penglecode.codeforce.common.codegen.support.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.lang.annotation.Annotation;
import java.util.*;

/**
 * 领域聚合根代码生成参数Builder
 *
 * @author pengpeng
 * @version 1.0.0
 */
public class DomainAggregateCodegenParameterBuilder extends CodegenParameterBuilder<DomainObjectCodegenConfigProperties, DomainAggregateConfig, DomainAggregateConfig, DomainAggregateCodegenParameter> {

    public DomainAggregateCodegenParameterBuilder(CodegenContext<DomainObjectCodegenConfigProperties, DomainAggregateConfig, DomainAggregateConfig> codegenContext) {
        super(codegenContext);
    }

    public DomainAggregateCodegenParameterBuilder(DomainObjectCodegenConfigProperties codegenConfig, DomainAggregateConfig targetConfig, DomainAggregateConfig domainObjectConfig) {
        super(codegenConfig, targetConfig, domainObjectConfig);
    }

    @Override
    protected DomainAggregateCodegenParameter setCustomCodegenParameter(DomainAggregateCodegenParameter codegenParameter) {
        codegenParameter.setTargetComment(getDomainObjectConfig().getDomainAggregateTitle() + "聚合根");
        List<ObjectFieldParameter> inherentFields = new ArrayList<>();
        DomainAggregateConfig domainAggregateConfig = getTargetConfig();
        DomainEntityConfig masterDomainEntityConfig = getCodegenConfig().getDomain().getDomainEntities().get(domainAggregateConfig.getAggregateMasterEntity());
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(masterDomainEntityConfig.getGeneratedTargetName(masterDomainEntityConfig.getDomainEntityName(), true, false)));
        Map<String, DomainAggregateFieldConfig> domainAggregateFieldConfigs = domainAggregateConfig.getDomainAggregateFields();
        for(Map.Entry<String,DomainAggregateFieldConfig> entry : domainAggregateFieldConfigs.entrySet()) {
            DomainAggregateFieldConfig domainAggregateFieldConfig = entry.getValue();
            DomainEntityConfig slaveDomainEntityConfig = getCodegenConfig().getDomain().getDomainEntities().get(domainAggregateFieldConfig.getDomainAggregateSlaveConfig().getAggregateSlaveEntity());
            inherentFields.add(buildAggregateInherentField(domainAggregateFieldConfig, slaveDomainEntityConfig, codegenParameter)); //添加聚合属性
        }
        codegenParameter.setInherentFields(inherentFields);
        codegenParameter.setTargetExtends(domainAggregateConfig.getAggregateMasterEntity());
        return codegenParameter;
    }

    private ObjectFieldParameter buildAggregateInherentField(DomainAggregateFieldConfig domainAggregateFieldConfig, DomainEntityConfig slaveDomainEntityConfig, CodegenParameter codegenParameter) {
        ObjectFieldParameter field = new ObjectFieldParameter();
        field.setFieldName(domainAggregateFieldConfig.getFieldName());
        field.setFieldType(domainAggregateFieldConfig.getFieldType().getShortName());
        field.setFieldComment(domainAggregateFieldConfig.getFieldComment());
        List<String> fieldAnnotations = new ArrayList<>();
        Set<CodegenAnnotationMeta> fieldAnnotationMetas = buildAggregateFieldAnnotations(domainAggregateFieldConfig);
        for(CodegenAnnotationMeta fieldAnnotationMeta : fieldAnnotationMetas) {
            fieldAnnotations.add(fieldAnnotationMeta.getExpression());
            codegenParameter.addTargetImportTypes(fieldAnnotationMeta.getImportTypes());
        }
        field.setFieldAnnotations(fieldAnnotations);
        field.setFieldGetterName(domainAggregateFieldConfig.getFieldGetterName());
        field.setFieldSetterName(domainAggregateFieldConfig.getFieldSetterName());
        if(DomainMasterSlaveRelation.RELATION_1N.equals(domainAggregateFieldConfig.getDomainAggregateSlaveConfig().getMasterSlaveMapping().getMasterSlaveRelation())) {
            codegenParameter.addTargetImportType(new FullyQualifiedJavaType(List.class.getName()));
        }
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(slaveDomainEntityConfig.getGeneratedTargetName(slaveDomainEntityConfig.getDomainEntityName(), true, false)));
        return field;
    }

    /**
     * 构造聚合根字段上的注解
     * @param domainAggregateFieldConfig
     * @return
     */
    protected Set<CodegenAnnotationMeta> buildAggregateFieldAnnotations(DomainAggregateFieldConfig domainAggregateFieldConfig) {
        Set<CodegenAnnotationMeta> fieldAnnotations = new LinkedHashSet<>();
        Class<? extends Annotation> validatorType = NotNull.class;
        if(DomainMasterSlaveRelation.RELATION_1N.equals(domainAggregateFieldConfig.getDomainAggregateSlaveConfig().getMasterSlaveMapping().getMasterSlaveRelation())) {
            validatorType = NotEmpty.class; //1:N关系使用集合
        }
        fieldAnnotations.add(new CodegenAnnotationMeta(String.format("@%s(message=\"%s\")", validatorType.getSimpleName(), domainAggregateFieldConfig.getFieldTitle() + "不能为空!"), Collections.singleton(new FullyQualifiedJavaType(validatorType.getName()))));
        return fieldAnnotations;
    }

    @Override
    protected String getTargetTemplateName() {
        return "DomainAggregate.ftl";
    }

}
