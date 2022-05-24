package com.penglecode.codeforce.common.codegen.config;

import com.penglecode.codeforce.common.codegen.support.DomainObjectFieldClass;
import com.penglecode.codeforce.common.codegen.support.FullyQualifiedJavaType;

/**
 * 领域聚合对象字段配置
 *
 * @author pengpeng
 * @version 1.0
 */
public class DomainAggregateFieldConfig extends DomainObjectFieldConfig {

    /** 当前聚合对象属性对应的聚合对象配置 */
    private final DomainAggregateConfig domainAggregateConfig;

    /** 当前聚合对象属性对应的从属领域对象配置 */
    private final DomainAggregateSlaveConfig domainAggregateSlaveConfig;

    public DomainAggregateFieldConfig(String fieldName, FullyQualifiedJavaType fieldClass, String fieldTitle, String fieldComment, DomainObjectFieldClass fieldType, DomainAggregateConfig domainAggregateConfig, DomainAggregateSlaveConfig domainAggregateSlaveConfig) {
        super(fieldName, fieldClass, fieldTitle, fieldComment, fieldType);
        this.domainAggregateConfig = domainAggregateConfig;
        this.domainAggregateSlaveConfig = domainAggregateSlaveConfig;
    }

    public DomainAggregateConfig getDomainAggregateConfig() {
        return domainAggregateConfig;
    }

    public DomainAggregateSlaveConfig getDomainAggregateSlaveConfig() {
        return domainAggregateSlaveConfig;
    }

}