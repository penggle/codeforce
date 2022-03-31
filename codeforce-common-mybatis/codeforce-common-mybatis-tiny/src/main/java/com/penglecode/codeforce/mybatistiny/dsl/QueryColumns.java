package com.penglecode.codeforce.mybatistiny.dsl;

import com.penglecode.codeforce.common.domain.DomainObject;
import com.penglecode.codeforce.common.support.BeanIntrospector;
import com.penglecode.codeforce.common.support.SerializableFunction;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * 用于指定查询语句返回的列(这里的column指定就是领域对象字段名，例如userName而不是user_name)，例如：
 *
 *      1、只选择特定的列：userMapper.selectModelById(id, new QueryColumns<User>(User::getUserId, User::getUserName));
 *      2、根据条件选择列：orderMapper.selectModelById(id, new QueryColumns<Order>(column -> column.startWith("product")));
 *
 * @author pengpeng
 * @version 1.0
 */
public class QueryColumns {

    /**
     * 被选择的列对应的领域对象字段名
     */
    private final Set<String> columns;

    /**
     * 根据条件选择列
     */
    private final Predicate<String> predicate;

    @SafeVarargs
    public <T extends DomainObject> QueryColumns(SerializableFunction<T,?>... columns) {
        Set<String> propertyNames = new LinkedHashSet<>();
        if(columns != null && columns.length > 0) {
            for(SerializableFunction<T,?> selectColumn : columns) {
                Field selectField = BeanIntrospector.introspectField(selectColumn);
                propertyNames.add(selectField.getName());
            }
        }
        this.columns = Collections.unmodifiableSet(propertyNames);
        this.predicate = null;
    }

    public QueryColumns(Predicate<String> selectPredicate) {
        this.predicate = selectPredicate;
        this.columns = Collections.emptySet();
    }

    public Set<String> getColumns() {
        return columns;
    }

    public Predicate<String> getPredicate() {
        return predicate;
    }

}
