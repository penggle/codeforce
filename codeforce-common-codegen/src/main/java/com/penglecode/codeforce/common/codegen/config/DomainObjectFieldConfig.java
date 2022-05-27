package com.penglecode.codeforce.common.codegen.config;

import com.penglecode.codeforce.common.codegen.support.DomainObjectFieldGroup;
import com.penglecode.codeforce.common.codegen.support.FullyQualifiedJavaType;
import com.penglecode.codeforce.common.codegen.util.CodegenUtils;

/**
 * 领域对象字段配置
 *
 * @author pengpeng
 * @version 1.0
 */
public abstract class DomainObjectFieldConfig {

    /** 字段名称 */
    private final String fieldName;

    /** 字段类型 */
    private final FullyQualifiedJavaType fieldType;

    /** 字段标题 */
    private final String fieldTitle;

    /** 字段注释 */
    private final String fieldComment;

    /** 领域对象字段分组 */
    private final DomainObjectFieldGroup fieldGroup;

    protected DomainObjectFieldConfig(String fieldName, FullyQualifiedJavaType fieldType, String fieldTitle, String fieldComment, DomainObjectFieldGroup fieldGroup) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.fieldTitle = fieldTitle;
        this.fieldComment = fieldComment;
        this.fieldGroup = fieldGroup;
    }

    public String getFieldName() {
        return fieldName;
    }

    public FullyQualifiedJavaType getFieldType() {
        return fieldType;
    }

    public String getFieldTitle() {
        return fieldTitle;
    }

    public String getFieldComment() {
        return fieldComment;
    }

    public DomainObjectFieldGroup getFieldGroup() {
        return fieldGroup;
    }

    public String getFieldGetterName() {
        return CodegenUtils.getGetterMethodName(getFieldName(), getFieldType().getFullyQualifiedNameWithoutTypeParameters());
    }

    public String getFieldSetterName() {
        return CodegenUtils.getSetterMethodName(getFieldName(), getFieldType().getFullyQualifiedNameWithoutTypeParameters());
    }

}
