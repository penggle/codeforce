package com.penglecode.codeforce.common.codegen.config;

import com.penglecode.codeforce.common.codegen.support.FullyQualifiedJavaType;

import java.util.Map;

/**
 /**
 * 领域枚举对象，领域枚举可以看做是的值对象(Value-Object)的一种特殊形式
 *
 * 领域下的枚举定义都是如下约定的格式：
 *
 * public enum ${enumName}Enum {
 *
 *     A(1, 'aaa'), B(2, 'bbb');
 *
 *     private final Integer xxxCode;
 *
 *     private final String xxxName;
 *
 *     private ${enumName}Enum(Integer xxxCode, String xxxName) {...}
 *
 *     public static ${enumName}Enum of(Integer xxxCode) {...}
 *
 *     //getter ...
 *
 * }
 *
 *
 * @author pengpeng
 * @version 1.0.0
 */
public class DomainEnumConfig extends DomainObjectConfig {

    /** 领域枚举类名称 */
    private String domainEnumName;

    /** 领域枚举类标题 */
    private String domainEnumTitle;

    /** 领域枚举Class(如果指定了domainEnumClass则表示当前领域枚举是已经存在的并不需要生成代码) */
    private Class<?> domainEnumClass;

    /** 领域枚举字段-xxxCode */
    private DomainEnumField domainEnumCodeField;

    /** 领域枚举字段-xxxName */
    private DomainEnumField domainEnumNameField;

    /** 领域枚举值 */
    private Map<String,Object[]> domainEnumValues;

    public String getDomainEnumName() {
        return domainEnumName;
    }

    public void setDomainEnumName(String domainEnumName) {
        this.domainEnumName = domainEnumName;
    }

    public String getDomainEnumTitle() {
        return domainEnumTitle;
    }

    public void setDomainEnumTitle(String domainEnumTitle) {
        this.domainEnumTitle = domainEnumTitle;
    }

    public Class<?> getDomainEnumClass() {
        return domainEnumClass;
    }

    public void setDomainEnumClass(Class<?> domainEnumClass) {
        this.domainEnumClass = domainEnumClass;
    }

    public DomainEnumField getDomainEnumCodeField() {
        return domainEnumCodeField;
    }

    public void setDomainEnumCodeField(DomainEnumField domainEnumCodeField) {
        this.domainEnumCodeField = domainEnumCodeField;
    }

    public DomainEnumField getDomainEnumNameField() {
        return domainEnumNameField;
    }

    public void setDomainEnumNameField(DomainEnumField domainEnumNameField) {
        this.domainEnumNameField = domainEnumNameField;
    }

    public Map<String, Object[]> getDomainEnumValues() {
        return domainEnumValues;
    }

    public void setDomainEnumValues(Map<String, Object[]> domainEnumValues) {
        this.domainEnumValues = domainEnumValues;
    }

    @Override
    public String getDomainObjectName() {
        return getDomainEnumName();
    }

    @Override
    public String getDomainObjectTitle() {
        return getDomainEnumTitle();
    }

    @Override
    public String getDomainObjectAlias() {
        return getDomainEnumName();
    }

    @Override
    public String getGeneratedTargetName(String domainObjectName, boolean includePackage, boolean includeSuffix) {
        return (includePackage ? getTargetPackage() + "." : "") + domainObjectName + "Enum" + (includeSuffix ? ".java" : "");
    }

    public static class DomainEnumField {

        private FullyQualifiedJavaType fieldType;

        private String fieldName;

        public FullyQualifiedJavaType getFieldType() {
            return fieldType;
        }

        public void setFieldType(String fieldType) {
            this.fieldType = new FullyQualifiedJavaType(fieldType);
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }
    }

}
