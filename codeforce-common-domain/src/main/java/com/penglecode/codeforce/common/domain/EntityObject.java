package com.penglecode.codeforce.common.domain;

import java.io.Serializable;

/**
 * 实体对象(EntityObject)基类，实体对象直接对应着数据库的一张表。
 * 而领域对象(DomainObject)则是包含了实体对象，不一定对应着数据库中的一张表，例如统计报表查询结果它可能是多张表聚合的结果，可能没有主键，所以它只能算是一个值对象
 *
 * @author pengpeng
 * @version 1.0.0
 */
public interface EntityObject extends DomainObject {

    /**
     * 实体对象的唯一标识(Id)
     *
     * @return 返回实体对象的唯一标识(Id)
     */
    default Serializable identity() {
        return null;
    }

}
