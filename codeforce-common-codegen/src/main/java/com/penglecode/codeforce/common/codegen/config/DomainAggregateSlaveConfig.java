package com.penglecode.codeforce.common.codegen.config;

import com.penglecode.codeforce.common.codegen.support.DomainMasterSlaveMapping;

import java.util.Objects;

/**
 * 领域聚合对象的从属实体对象配置
 *
 * @author pengpeng
 * @version 1.0
 */
public class DomainAggregateSlaveConfig {

    /** 领域聚合对象的从属实体对象(指向domainEntities列表中的某一个) */
    private String aggregateSlaveEntity;

    /** 主要领域对象与从属领域对象的映射关系(支持1:1/1:N) */
    private DomainMasterSlaveMapping masterSlaveMapping;

    /**
     * 保存主要领域对象时是否也级联保存从属领域对象
     */
    private boolean cascadingOnUpsert = true;

    /**
     * 保存主要领域对象时是否校验从属领域对象不为空(仅在cascadingOnUpsert=true时有效)
     */
    private boolean validateOnUpsert = true;

    public String getAggregateSlaveEntity() {
        return aggregateSlaveEntity;
    }

    public void setAggregateSlaveEntity(String aggregateSlaveEntity) {
        this.aggregateSlaveEntity = aggregateSlaveEntity;
    }

    public DomainMasterSlaveMapping getMasterSlaveMapping() {
        return masterSlaveMapping;
    }

    public void setMasterSlaveMapping(String masterSlaveMapping) {
        this.masterSlaveMapping = DomainMasterSlaveMapping.parseMapping(masterSlaveMapping);
    }

    public boolean isCascadingOnUpsert() {
        return cascadingOnUpsert;
    }

    public void setCascadingOnUpsert(boolean cascadingOnUpsert) {
        this.cascadingOnUpsert = cascadingOnUpsert;
    }

    public boolean isValidateOnUpsert() {
        return validateOnUpsert;
    }

    public void setValidateOnUpsert(boolean validateOnUpsert) {
        this.validateOnUpsert = validateOnUpsert;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DomainAggregateSlaveConfig)) return false;
        DomainAggregateSlaveConfig that = (DomainAggregateSlaveConfig) o;
        return aggregateSlaveEntity.equals(that.aggregateSlaveEntity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(aggregateSlaveEntity);
    }

}