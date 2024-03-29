package com.penglecode.codeforce.common.codegen.config;

import com.penglecode.codeforce.common.codegen.support.DomainObjectFieldGroup;
import com.penglecode.codeforce.common.codegen.support.FullyQualifiedJavaType;
import com.penglecode.codeforce.common.codegen.support.QueryConditionOperator;

/**
 * 领域实体字段配置
 *
 * @author pengpeng
 * @version 1.0.0
 */
public class DomainEntityFieldConfig extends DomainObjectFieldConfig {

    /** 是否是ID字段? */
    private final boolean idField;

    /** 如果fieldType.isSupportField()为true，则domainColumnConfig为当前字段的对应数据库列配置，否则为当前字段的关联数据库列配置 */
    private final DomainEntityColumnConfig domainEntityColumnConfig;

    /** 如果是DomainObjectFieldType.DOMAIN_ENTITY_SUPPORTS_QUERY_INPUT_FIELD则有值 */
    private QueryConditionOperator queryConditionOperator;

    public DomainEntityFieldConfig(String fieldName, FullyQualifiedJavaType fieldClass, String fieldTitle, String fieldComment, DomainObjectFieldGroup fieldType, DomainEntityColumnConfig domainEntityColumnConfig, QueryConditionOperator queryConditionOperator) {
        super(fieldName, fieldClass, fieldTitle, fieldComment, fieldType);
        this.idField = domainEntityColumnConfig != null && domainEntityColumnConfig.isIdColumn();
        this.domainEntityColumnConfig = domainEntityColumnConfig;
        this.queryConditionOperator = queryConditionOperator;
    }

    public boolean isIdField() {
        return idField;
    }

    public DomainEntityColumnConfig getDomainEntityColumnConfig() {
        return domainEntityColumnConfig;
    }

    public QueryConditionOperator getQueryConditionOperator() {
        return queryConditionOperator;
    }

    public void setQueryConditionOperator(QueryConditionOperator queryConditionOperator) {
        this.queryConditionOperator = queryConditionOperator;
    }

}