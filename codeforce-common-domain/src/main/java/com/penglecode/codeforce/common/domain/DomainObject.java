package com.penglecode.codeforce.common.domain;

import com.penglecode.codeforce.common.support.Convertible;

/**
 * 领域对象(DomainObject)基类，领域对象包括实体对象、聚合根、值对象等。
 * 
 * 实体对象(EntityObject)，它对应着数据库中的一张表；
 * 而领域对象(DomainObject)则是包含了实体对象，不一定对应着数据库中的一张表，例如统计报表查询结果它可能是多张表聚合的结果，可能没有主键，所以它只能算是一个值对象
 *
 * @author pengpeng
 * @version 1.0
 */
public interface DomainObject extends Convertible {

    /**
     * 领域对象数据出站（从存储介质输出出去）前的加工处理
     * 例如根据statusCode(字典值)设置statusName(字典值的字面意思字段)
     *
     * @return 返回处理过得实体对象
     */
    default DomainObject processOutbound() {
        return this;
    }

    /**
     * 领域对象数据入站（从外界进入当前应用）后的加工处理
     *
     * @return 返回处理过得实体对象
     */
    default DomainObject processInbound() {
        return this;
    }

}
