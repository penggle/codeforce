package com.penglecode.codeforce.common.codegen.config;

import com.penglecode.codeforce.common.codegen.support.*;
import com.penglecode.codeforce.common.util.StringUtils;
import com.penglecode.codeforce.mybatistiny.annotations.GenerationType;
import com.penglecode.codeforce.mybatistiny.annotations.Id;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * 领域实体字段配置
 *
 * @author pengpeng
 * @version 1.0
 */
public class DomainEntityFieldConfig extends DomainObjectFieldConfig {

    /** 是否是ID字段? */
    private final boolean idField;

    /** 如果fieldType.isSupportField()为true，则domainColumnConfig为当前字段的对应数据库列配置，否则为当前字段的关联数据库列配置 */
    private final DomainEntityColumnConfig domainEntityColumnConfig;

    /** 当前字段上的注解列表 */
    private final Set<CodegenAnnotationMeta> fieldAnnotations;

    /** 如果是DomainObjectFieldType.DOMAIN_ENTITY_SUPPORTS_QUERY_INPUT_FIELD则有值 */
    private QueryConditionOperator queryConditionOperator;

    public DomainEntityFieldConfig(String fieldName, FullyQualifiedJavaType fieldClass, String fieldTitle, String fieldComment, DomainObjectFieldClass fieldType, DomainEntityColumnConfig domainEntityColumnConfig, QueryConditionOperator queryConditionOperator) {
        super(fieldName, fieldClass, fieldTitle, fieldComment, fieldType);
        this.idField = domainEntityColumnConfig != null && domainEntityColumnConfig.isIdColumn();
        this.domainEntityColumnConfig = domainEntityColumnConfig;
        this.queryConditionOperator = queryConditionOperator;
        this.fieldAnnotations = new LinkedHashSet<>();
    }

    public boolean isIdField() {
        return idField;
    }

    public DomainEntityColumnConfig getDomainEntityColumnConfig() {
        return domainEntityColumnConfig;
    }

    public Set<CodegenAnnotationMeta> getFieldAnnotations() {
        return fieldAnnotations;
    }

    public QueryConditionOperator getQueryConditionOperator() {
        return queryConditionOperator;
    }

    public void setQueryConditionOperator(QueryConditionOperator queryConditionOperator) {
        this.queryConditionOperator = queryConditionOperator;
    }

    public void initFieldAnnotations(DomainConfig domainConfig) {
        initValidateAnnotations(domainConfig);
    }

    protected void initValidateAnnotations(DomainConfig domainConfig) {
        if(domainEntityColumnConfig.isValidateOnInsert() || domainEntityColumnConfig.isValidateOnUpdate()) {
            Class<?> validatorType = FullyQualifiedJavaType.getStringInstance().equals(domainEntityColumnConfig.getIntrospectedColumn().getJavaFieldType()) ? NotBlank.class : NotNull.class;
            fieldAnnotations.add(new CodegenAnnotationMeta(String.format("@%s(message=\"%s\")", validatorType.getSimpleName(), domainEntityColumnConfig.getColumnTitle() + "不能为空!"), Collections.singleton(validatorType.getName())));
        }
    }

    protected void initMybatisTinyAnnotations(DomainConfig domainConfig) {
        Set<String> annoImportTypes = new LinkedHashSet<>();
        Set<String> annoProperties = new LinkedHashSet<>();
        //1、处理@Id注解
        if(idField) {
            IdGenerator idGenerator = domainEntityColumnConfig.getIdGenerator();
            if(idGenerator != null) {
                if(IdGenStrategy.IDENTITY.equals(idGenerator.getStrategy())) {
                    annoProperties.add("strategy=GenerationType.IDENTITY");
                } else if(IdGenStrategy.SEQUENCE.equals(idGenerator.getStrategy())) {
                    annoProperties.add("strategy=GenerationType.SEQUENCE");
                    annoProperties.add("generator=\"" + idGenerator.getParameter() + "\"");
                }
                annoImportTypes.add(GenerationType.class.getName());
            }
            annoImportTypes.add(Id.class.getName());
            if(domainEntityColumnConfig.isColumnOnUpdate()) {
                annoProperties.add("updatable=true");
            }
            fieldAnnotations.add(new CodegenAnnotationMeta(String.format("@Id%s", annoProperties.isEmpty() ? "" : "(" + String.join(", ", annoProperties) + ")"), annoImportTypes));
        }

        //2、处理@Column注解
        annoImportTypes.clear();
        annoProperties.clear();
        if(!domainEntityColumnConfig.isColumnOnInsert()) {
            annoProperties.add("insertable=false");
        }
        if(!domainEntityColumnConfig.isColumnOnUpdate()) {
            annoProperties.add("updatable=false");
        }

    }

}