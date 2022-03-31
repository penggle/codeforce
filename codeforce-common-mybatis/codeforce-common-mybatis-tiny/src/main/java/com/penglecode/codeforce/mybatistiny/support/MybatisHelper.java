package com.penglecode.codeforce.mybatistiny.support;

import com.penglecode.codeforce.common.consts.GlobalConstants;
import com.penglecode.codeforce.common.domain.DomainObject;
import com.penglecode.codeforce.common.model.Page;
import com.penglecode.codeforce.mybatistiny.dsl.QueryCriteria;
import com.penglecode.codeforce.mybatistiny.mapper.BaseMybatisMapper;
import com.penglecode.codeforce.common.util.ObjectUtils;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.extensions.JdbcBatchOperation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Mybatis操作助手
 *
 * @author pengpeng
 * @version 1.0
 */
public class MybatisHelper {

    /**
     * in SQL语句限制大小
     */
    public static final int IN_SQL_LIMITS = 500;

    private MybatisHelper() {}

    /**
     * 执行Mybatis分页查询
     * (总记录数totalRowCount将会回设进入Page对象中)
     *
     * @param domainMybatisMapper   - 领域对象的Mybatis-Mapper接口
     * @param criteria              - 查询条件
     * @param page                  - 分页参数
     * @param <T>
     * @return 返回分页结果列表
     */
    public static <T extends DomainObject> List<T> selectDomainObjectListByPage(BaseMybatisMapper<T> domainMybatisMapper, QueryCriteria<T> criteria, Page page) {
        List<T> pageList = null;
        int totalRowCount = domainMybatisMapper.selectPageCountByCriteria(criteria);
        if(totalRowCount > 0) {
            pageList = domainMybatisMapper.selectPageListByCriteria(criteria, new RowBounds(page.offset(), page.limit()));
        }
        page.setTotalRowCount(totalRowCount);
        return ObjectUtils.defaultIfNull(pageList, ArrayList::new);
    }

    /**
     * 执行JDBC批量更新操作
     *
     * @param domainObjects         - 被批量更新的领域对象集合
     * @param updateOperation       - 具体执行更新的操作
     * @param domainMybatisMapper   - 领域对象的MybatisMapper
     * @param <T>
     */
    public static <T extends DomainObject> int batchUpdateDomainObjects(List<T> domainObjects, Consumer<T> updateOperation, BaseMybatisMapper<T> domainMybatisMapper) {
        return batchUpdateDomainObjects(domainObjects, GlobalConstants.DEFAULT_JDBC_BATCH_SIZE, updateOperation, domainMybatisMapper);
    }

    /**
     * 执行JDBC批量更新操作
     *
     * @param domainObjects         - 被批量更新的领域对象集合
     * @param batchSize             - 每批次处理多少条
     * @param updateOperation       - 具体执行更新的操作
     * @param domainMybatisMapper   - 领域对象的MybatisMapper
     * @param <T>
     */
    public static <T extends DomainObject> int batchUpdateDomainObjects(List<T> domainObjects, int batchSize, Consumer<T> updateOperation, BaseMybatisMapper<T> domainMybatisMapper) {
        return executeBatchUpdateDomainObjects(domainObjects, batchSize, updateOperation, domainMybatisMapper);
    }

    /**
     * 执行JDBC批量删除操作
     *
     * @param ids                   - 被批量删除的ID集合
     * @param domainMybatisMapper   - 领域对象的MybatisMapper
     * @param <T>
     */
    public static <T extends Serializable, D extends DomainObject> int batchDeleteDomainObjects(List<T> ids, BaseMybatisMapper<D> domainMybatisMapper) {
        return batchDeleteDomainObjects(ids, GlobalConstants.DEFAULT_JDBC_BATCH_SIZE, domainMybatisMapper);
    }

    /**
     * 执行JDBC批量删除操作
     *
     * @param ids                   - 被批量删除的ID集合
     * @param batchSize             - 每批次处理多少条
     * @param domainMybatisMapper   - 领域对象的MybatisMapper
     * @param <T>                   - 操作目标
     * @param <D>                   - 操作领域对象
     */
    public static <T extends Serializable, D extends DomainObject> int batchDeleteDomainObjects(List<T> ids, int batchSize, BaseMybatisMapper<D> domainMybatisMapper) {
        if(ids.size() > IN_SQL_LIMITS) {
            return executeBatchUpdateDomainObjects(ids, batchSize, domainMybatisMapper::deleteById, domainMybatisMapper);
        } else {
            return domainMybatisMapper.deleteByIds(ids);
        }
    }

    /**
     * 执行JDBC批量更新操作
     *
     * @param targetList            - 被批量更新的目标集合
     * @param batchSize             - 每批次处理多少条
     * @param updateOperation       - 具体执行更新的操作
     * @param domainMybatisMapper   - 领域对象的MybatisMapper
     * @param <T>                   - 操作目标
     * @param <D>                   - 操作领域对象
     */
    protected static <T extends Serializable, D extends DomainObject> int executeBatchUpdateDomainObjects(List<T> targetList, int batchSize, Consumer<T> updateOperation, BaseMybatisMapper<D> domainMybatisMapper) {
        batchSize = batchSize > 0 ? batchSize : GlobalConstants.DEFAULT_JDBC_BATCH_SIZE;
        int updateCounts = 0;
        try(JdbcBatchOperation batchOperation = new JdbcBatchOperation()) {
            for(int i = 0, size = targetList.size(); i < size; i++) {
                T target = targetList.get(i);
                if(target != null) {
                    updateOperation.accept(target);
                    if(size > batchSize && ((i + 1) % batchSize == 0)) {
                        List<BatchResult> results = domainMybatisMapper.flushStatements();
                        updateCounts += collectUpdateCounts(results);
                    }
                }
            }
            List<BatchResult> results = domainMybatisMapper.flushStatements();
            updateCounts += collectUpdateCounts(results);
        }
        return updateCounts;
    }

    private static int collectUpdateCounts(List<BatchResult> results) {
        int updateCounts = 0;
        for(BatchResult result : results) {
            for(int updateCount : result.getUpdateCounts()) {
                updateCounts += updateCount;
            }
        }
        return updateCounts;
    }

}
