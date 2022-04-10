package com.penglecode.codeforce.mybatistiny.interceptor;

import com.penglecode.codeforce.common.domain.DomainObject;
import com.penglecode.codeforce.common.domain.EntityObject;
import com.penglecode.codeforce.common.model.OrderBy;
import com.penglecode.codeforce.mybatistiny.CustomConfiguration;
import com.penglecode.codeforce.mybatistiny.core.EntityMeta;
import com.penglecode.codeforce.mybatistiny.dsl.QueryCriteria;
import com.penglecode.codeforce.mybatistiny.mapper.BaseMybatisMapper;
import com.penglecode.codeforce.mybatistiny.support.MybatistinyHelper;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.springframework.util.CollectionUtils;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

/**
 * 对{@link BaseMybatisMapper}中各方法中出现的QueryCriteria参数进行拦截处理的拦截器
 *
 * @author pengpeng
 * @version 1.0
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class QueryCriteriaInterceptor implements Interceptor {

    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        BoundSql boundSql = statementHandler.getBoundSql(); //获取绑定sql
        Optional<QueryCriteria<? extends DomainObject>> queryCriteria = MybatistinyHelper.getQueryCriteria(boundSql);
        if(queryCriteria.isPresent()) {
            MetaObject metaObject = MetaObject.forObject(statementHandler, new DefaultObjectFactory(), new DefaultObjectWrapperFactory(), new DefaultReflectorFactory());
            CustomConfiguration configuration = (CustomConfiguration) metaObject.getValue("delegate.configuration");

        }
        return invocation.proceed();
    }

    /**
     * 处理调用{@link BaseMybatisMapper}中的查询方法时：指定了select列，指定了OrderBy排序字段，但是排序字段不在select列中的情况
     *
     * @param queryCriteria
     * @param configuration
     */
    protected void processOrderBy(QueryCriteria<? extends DomainObject> queryCriteria, CustomConfiguration configuration) {
        EntityMeta<? extends EntityObject> entityMeta = configuration.getEntityMeta((Class<? extends EntityObject>) queryCriteria.getExampleType());
        List<OrderBy> orderBys = queryCriteria.getOrders();
        if(!CollectionUtils.isEmpty(orderBys)) {
            for(OrderBy orderBy : orderBys) {
            }
        }
    }

}
