package org.springframework.boot.autoconfigure.dal;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.apache.ibatis.executor.extensions.CustomSqlSessionFactoryBuilder;
import org.apache.ibatis.executor.extensions.DynamicExecutor;
import org.apache.ibatis.executor.extensions.ExecutorSynchronizationManager;

import javax.sql.DataSource;

/**
 * 自定义的DalMybatisComponentsBuilder
 * 启用动态Executor
 *
 * @see DynamicExecutor
 * @see ExecutorSynchronizationManager
 *
 * @author pengpeng
 * @version 1.0
 */
public class CustomDalMybatisComponentsBuilder extends DefaultDalMybatisComponentsBuilder {

    @Override
    public SqlSessionFactoryBean buildSqlSessionFactoryBean(DataSource dataSource, MybatisProperties properties) {
        SqlSessionFactoryBean sqlSessionFactoryBean = super.buildSqlSessionFactoryBean(dataSource, properties);
        sqlSessionFactoryBean.setSqlSessionFactoryBuilder(new CustomSqlSessionFactoryBuilder());
        return sqlSessionFactoryBean;
    }

}
