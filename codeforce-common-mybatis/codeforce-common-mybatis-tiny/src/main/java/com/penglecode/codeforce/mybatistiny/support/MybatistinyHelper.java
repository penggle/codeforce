package com.penglecode.codeforce.mybatistiny.support;

import com.penglecode.codeforce.common.domain.EntityObject;
import com.penglecode.codeforce.mybatistiny.dsl.QueryCriteria;
import com.penglecode.codeforce.mybatistiny.mapper.BaseEntityMapper;
import org.apache.ibatis.mapping.BoundSql;

import java.util.Map;
import java.util.Optional;

/**
 * Mybatis-Tiny辅助类
 *
 * @author pengpeng
 * @version 1.0
 */
public class MybatistinyHelper {

    private MybatistinyHelper() {}

    /**
     * 如果绑定参数中存在QueryCriteria
     * @param boundSql
     * @return
     */
    public static Optional<QueryCriteria<? extends EntityObject>> getQueryCriteria(BoundSql boundSql) {
        String paramName = BaseEntityMapper.QUERY_CRITERIA_PARAM_NAME;
        Object parameterObject = boundSql.getParameterObject();
        QueryCriteria<? extends EntityObject> queryCriteria = null;
        if(parameterObject instanceof QueryCriteria) {
            queryCriteria = (QueryCriteria<? extends EntityObject>) parameterObject;
        }
        if(parameterObject instanceof Map && ((Map<?, ?>) parameterObject).containsKey(paramName)) {
            Object paramValue = ((Map<?, ?>) parameterObject).get(paramName);
            if(paramValue instanceof QueryCriteria) {
                queryCriteria = (QueryCriteria<? extends EntityObject>) paramValue;
            }
        }
        return Optional.ofNullable(queryCriteria);
    }

}
