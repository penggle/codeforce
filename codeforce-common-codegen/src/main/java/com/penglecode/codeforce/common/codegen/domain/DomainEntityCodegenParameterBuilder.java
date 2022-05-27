package com.penglecode.codeforce.common.codegen.domain;

import com.penglecode.codeforce.common.codegen.config.*;
import com.penglecode.codeforce.common.codegen.support.*;
import com.penglecode.codeforce.common.codegen.util.CodegenUtils;
import com.penglecode.codeforce.common.domain.EntityObject;
import com.penglecode.codeforce.common.domain.ID;
import com.penglecode.codeforce.common.util.StringUtils;
import com.penglecode.codeforce.mybatistiny.annotations.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * 领域实体代码生成参数Builder
 *
 * @author pengpeng
 * @version 1.0
 */
public class DomainEntityCodegenParameterBuilder extends CodegenParameterBuilder<DomainObjectCodegenConfigProperties, DomainEntityConfig, DomainEntityConfig, DomainEntityCodegenParameter> {

    public DomainEntityCodegenParameterBuilder(CodegenContext<DomainObjectCodegenConfigProperties, DomainEntityConfig, DomainEntityConfig> codegenContext) {
        super(codegenContext);
    }

    public DomainEntityCodegenParameterBuilder(DomainObjectCodegenConfigProperties codegenConfig, DomainEntityConfig targetConfig, DomainEntityConfig domainObjectConfig) {
        super(codegenConfig, targetConfig, domainObjectConfig);
    }

    @Override
    protected DomainEntityCodegenParameter setCustomCodegenParameter(DomainEntityCodegenParameter codegenParameter) {
        codegenParameter.setTargetComment(getDomainObjectConfig().getDomainEntityTitle() + "实体");
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(EntityObject.class.getName()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(Table.class.getName()));
        codegenParameter.setTargetAnnotations(Collections.singletonList(String.format("@Table(\"%s\")", getDomainObjectConfig().getDomainEntityTable())));
        List<ObjectFieldParameter> inherentFields = new ArrayList<>(); //实体固有字段
        List<ObjectFieldParameter> supportFields = new ArrayList<>(); //实体辅助字段
        List<ObjectFieldParameter> allFields = new ArrayList<>(); //实体所有字段
        List<DomainEntityCodegenParameter.EnumFieldDecode> enumFieldDecodes = new ArrayList<>();
        for(Map.Entry<String, DomainEntityFieldConfig> entry : getTargetConfig().getDomainEntityFields().entrySet()) {
            DomainEntityFieldConfig domainEntityFieldConfig = entry.getValue();
            if(domainEntityFieldConfig.getFieldGroup().isSupportField()) { //领域实体辅助字段
                supportFields.add(buildEntityFieldParameter(domainEntityFieldConfig, codegenParameter));
                //处理领域对象数据出站DomainObject#beforeOutbound()实现
                if(DomainObjectFieldGroup.DOMAIN_ENTITY_SUPPORTS_QUERY_OUTBOUND_FIELD.equals(domainEntityFieldConfig.getFieldGroup())) {
                    DomainEntityColumnConfig domainEntityColumnConfig = domainEntityFieldConfig.getDomainEntityColumnConfig(); //当前辅助字段是辅助谁的?
                    String shortDomainEnumType = getShortDomainEnumType(domainEntityColumnConfig.getDecodeEnumType());
                    DomainEnumConfig refDomainEnumConfig = resolveDecodeEnumConfig(domainEntityColumnConfig.getDecodeEnumType());
                    if(refDomainEnumConfig != null) {
                        enumFieldDecodes.add(buildEnumFieldDecode(domainEntityFieldConfig, refDomainEnumConfig, codegenParameter));
                    }
                }
            } else { //领域实体固有字段
                inherentFields.add(buildEntityFieldParameter(domainEntityFieldConfig, codegenParameter));
            }
        }
        codegenParameter.setInherentFields(inherentFields);
        codegenParameter.setSupportFields(supportFields);
        codegenParameter.setEnumFieldDecodes(enumFieldDecodes);
        allFields.addAll(inherentFields);
        allFields.addAll(supportFields);
        codegenParameter.setAllFields(allFields);
        attachDomainEntityIdField(codegenParameter);
        return codegenParameter;
    }

    /**
     * 构造实体字段参数
     * @param domainEntityFieldConfig
     * @param codegenParameter
     * @return
     */
    protected ObjectFieldParameter buildEntityFieldParameter(DomainEntityFieldConfig domainEntityFieldConfig, CodegenParameter codegenParameter) {
        ObjectFieldParameter field = new ObjectFieldParameter();
        field.setFieldName(domainEntityFieldConfig.getFieldName());
        field.setFieldType(domainEntityFieldConfig.getFieldType().getShortName());
        field.setFieldComment(domainEntityFieldConfig.getFieldComment());
        List<String> fieldAnnotations = new ArrayList<>();
        Set<CodegenAnnotationMeta> fieldAnnotationMetas = buildEntityFieldAnnotations(domainEntityFieldConfig);
        for(CodegenAnnotationMeta fieldAnnotationMeta : fieldAnnotationMetas) {
            fieldAnnotations.add(fieldAnnotationMeta.getExpression());
            codegenParameter.addTargetImportTypes(fieldAnnotationMeta.getImportTypes());
        }
        field.setFieldAnnotations(fieldAnnotations);
        field.setFieldGetterName(domainEntityFieldConfig.getFieldGetterName());
        field.setFieldSetterName(domainEntityFieldConfig.getFieldSetterName());
        codegenParameter.addTargetImportType(domainEntityFieldConfig.getFieldType());
        return field;
    }

    /**
     * 构造实体字段上的注解
     * @param domainEntityFieldConfig
     * @return
     */
    protected Set<CodegenAnnotationMeta> buildEntityFieldAnnotations(DomainEntityFieldConfig domainEntityFieldConfig) {
        Set<CodegenAnnotationMeta> fieldAnnotations = new LinkedHashSet<>();
        attachValidationAnnotations(fieldAnnotations, domainEntityFieldConfig);
        attachMybatisTinyAnnotations(fieldAnnotations, domainEntityFieldConfig);
        return fieldAnnotations;
    }

    /**
     * 附加上javax.validation注解
     * @param fieldAnnotations
     * @param domainEntityFieldConfig
     */
    protected void attachValidationAnnotations(Set<CodegenAnnotationMeta> fieldAnnotations, DomainEntityFieldConfig domainEntityFieldConfig) {
        DomainEntityColumnConfig domainEntityColumnConfig = domainEntityFieldConfig.getDomainEntityColumnConfig();
        if(domainEntityColumnConfig.isValidateOnInsert() || domainEntityColumnConfig.isValidateOnUpdate()) {
            Class<?> validatorType = FullyQualifiedJavaType.getStringInstance().equals(domainEntityColumnConfig.getIntrospectedColumn().getJavaFieldType()) ? NotBlank.class : NotNull.class;
            fieldAnnotations.add(new CodegenAnnotationMeta(String.format("@%s(message=\"%s\")", validatorType.getSimpleName(), domainEntityFieldConfig.getFieldTitle() + "不能为空!"), Collections.singleton(new FullyQualifiedJavaType(validatorType.getName()))));
        }
    }

    /**
     * 附加上MybatisTiny注解
     * @param fieldAnnotations
     * @param domainEntityFieldConfig
     */
    protected void attachMybatisTinyAnnotations(Set<CodegenAnnotationMeta> fieldAnnotations, DomainEntityFieldConfig domainEntityFieldConfig) {
        DomainEntityColumnConfig domainEntityColumnConfig = domainEntityFieldConfig.getDomainEntityColumnConfig();
        Set<FullyQualifiedJavaType> annotationImports = new LinkedHashSet<>();
        Set<String> annotationProperties = new LinkedHashSet<>();
        //1、处理@Id注解
        if(domainEntityFieldConfig.isIdField()) {
            IdGenerator idGenerator = domainEntityColumnConfig.getIdGenerator();
            if(idGenerator != null) {
                if(IdGenStrategy.IDENTITY.equals(idGenerator.getStrategy())) {
                    annotationProperties.add("strategy=GenerationType.IDENTITY");
                } else if(IdGenStrategy.SEQUENCE.equals(idGenerator.getStrategy())) {
                    annotationProperties.add("strategy=GenerationType.SEQUENCE");
                    annotationProperties.add("generator=\"" + idGenerator.getParameter() + "\"");
                }
                annotationImports.add(new FullyQualifiedJavaType(GenerationType.class.getName()));
            }
            annotationImports.add(new FullyQualifiedJavaType(Id.class.getName()));
            if(domainEntityColumnConfig.isColumnOnUpdate()) {
                annotationProperties.add("updatable=true");
            }
            fieldAnnotations.add(new CodegenAnnotationMeta(String.format("@Id%s", annotationProperties.isEmpty() ? "" : "(" + String.join(", ", annotationProperties) + ")"), annotationImports));
        }

        annotationImports.clear();
        annotationProperties.clear();
        if(!domainEntityFieldConfig.getFieldGroup().isSupportField()) {
            //2、处理@Column注解
            if(!domainEntityColumnConfig.isColumnOnInsert()) {
                annotationProperties.add("insertable=false");
            }
            if(!domainEntityColumnConfig.isColumnOnUpdate()) {
                annotationProperties.add("updatable=false");
            }
            if(StringUtils.isNotBlank(domainEntityColumnConfig.getSelectClause())) {
                annotationProperties.add("select=\"" + domainEntityColumnConfig.getSelectClause() + "\"");
            }
            if(domainEntityColumnConfig.getTypeHandler() != null) {
                annotationProperties.add("typeHandler=" + domainEntityColumnConfig.getTypeHandler().getSimpleName() + ".class");
            }
            if(!annotationProperties.isEmpty()) {
                annotationImports.add(new FullyQualifiedJavaType(Column.class.getName()));
                fieldAnnotations.add(new CodegenAnnotationMeta(String.format("@Column%s", annotationProperties.isEmpty() ? "" : "(" + String.join(", ", annotationProperties) + ")"), annotationImports));
            }
        } else {
            //3、处理@T注解
            annotationImports.add(new FullyQualifiedJavaType(Transient.class.getName()));
            fieldAnnotations.add(new CodegenAnnotationMeta("@Transient", annotationImports));
        }
    }

    /**
     * 构建枚举值字段decode参数
     * @param domainEntityFieldConfig
     * @param refDomainEnumConfig
     * @param codegenParameter
     * @return
     */
    protected DomainEntityCodegenParameter.EnumFieldDecode buildEnumFieldDecode(DomainEntityFieldConfig domainEntityFieldConfig, DomainEnumConfig refDomainEnumConfig, CodegenParameter codegenParameter) {
        DomainEntityCodegenParameter.EnumFieldDecode field = new DomainEntityCodegenParameter.EnumFieldDecode();
        field.setRefEnumTypeName(refDomainEnumConfig.getDomainEnumName());
        field.setEntityFieldName(domainEntityFieldConfig.getDomainEntityColumnConfig().getIntrospectedColumn().getJavaFieldName()); //当前辅助字段关联的枚举值字段
        field.setEntityFieldSetterName(domainEntityFieldConfig.getFieldSetterName());
        field.setRefEnumNameFieldGetterName(CodegenUtils.getGetterMethodName(refDomainEnumConfig.getDomainEnumNameField().getFieldName(), refDomainEnumConfig.getDomainEnumNameField().getFieldType().getFullyQualifiedNameWithoutTypeParameters()));
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(refDomainEnumConfig.getGeneratedTargetName(refDomainEnumConfig.getDomainEnumName(), true, false)));
        codegenParameter.addTargetImportType(refDomainEnumConfig.getDomainEnumNameField().getFieldType());
        codegenParameter.addTargetImportType(new FullyQualifiedJavaType(Optional.class.getName()));
        return field;
    }

    /**
     * 附带上领域实体ID字段参数
     * @param codegenParameter
     */
    protected void attachDomainEntityIdField(DomainEntityCodegenParameter codegenParameter) {
        List<DomainEntityFieldConfig> idFields = getTargetConfig().getIdFields();
        if(idFields.size() == 1) { //单主键
            DomainEntityFieldConfig idField = idFields.get(0);
            codegenParameter.setIdFieldType(idField.getFieldType().getShortName());
            codegenParameter.setIdFieldName(idField.getFieldName());
        } else if(idFields.size() > 1) { //复合主键
            FullyQualifiedJavaType idFieldType = new FullyQualifiedJavaType(ID.class.getName());
            codegenParameter.addTargetImportType(idFieldType);
            StringBuilder sb = new StringBuilder("new ID()");
            for(DomainEntityFieldConfig idField : idFields) {
                sb.append(".addKey(\"").append(idField.getFieldName()).append("\", ").append(idField.getFieldName()).append(")");
            }
            codegenParameter.setIdFieldType(idFieldType.getShortName());
            codegenParameter.setIdFieldName(sb.toString());
        }
    }

    protected String getShortDomainEnumType(String domainEnumType) {
        if(domainEnumType.contains(".")) {
            return domainEnumType.substring(domainEnumType.lastIndexOf('.') + 1);
        }
        return domainEnumType;
    }

    protected DomainEnumConfig resolveDecodeEnumConfig(String domainEnumName) {
        return getCodegenConfig().getDomain().getDomainCommons().getDomainEnums()
                .stream()
                .filter(config -> config.getDomainEnumName().equals(domainEnumName))
                .findFirst().orElse(null);
    }


    @Override
    protected String getTargetTemplateName() {
        return "DomainEntity.ftl";
    }

}
