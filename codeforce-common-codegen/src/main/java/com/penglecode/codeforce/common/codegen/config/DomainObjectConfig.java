package com.penglecode.codeforce.common.codegen.config;

/**
 * 领域对象(实体、聚合根)配置
 *
 * @author pengpeng
 * @version 1.0
 */
public abstract class DomainObjectConfig extends GenerableTargetConfig {

    /**
     * 返回当前领域对象的名称(英文)
     *
     * @return
     */
    public abstract String getDomainObjectName();

    /**
     * 返回当前领域对象的名称(中文)
     *
     * @return
     */
    public abstract String getDomainObjectTitle();

    /**
     * 返回当前领域对象的别名(英文)
     *
     * @return
     */
    public abstract String getDomainObjectAlias();

}
