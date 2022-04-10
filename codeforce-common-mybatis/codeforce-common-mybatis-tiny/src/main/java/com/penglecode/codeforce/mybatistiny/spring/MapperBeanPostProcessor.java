package com.penglecode.codeforce.mybatistiny.spring;

import com.penglecode.codeforce.common.domain.EntityObject;
import com.penglecode.codeforce.mybatistiny.mapper.BaseEntityMapper;
import com.penglecode.codeforce.mybatistiny.interceptor.DomainObjectQueryInterceptor;
import com.penglecode.codeforce.mybatistiny.interceptor.PageLimitInterceptor;
import com.penglecode.codeforce.mybatistiny.core.EntityMapperRegistry;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * Mybatis基于Spring框架的相关配置Bean(SqlSessionFactory、XxxMapper)的Bean后置处理程序，用于向Configuration中动态注册MappedStatement。
 * 该BeanPostProcessor拦截刚创建好的{@link BaseEntityMapper}子类(XxxMapper)的代理实例，做四件事情：
 *
 *  1、代理SqlSessionFactory中的configuration属性，如果可能的话
 *  2、向{@link Configuration}中注册{@link DomainObjectQueryInterceptor}和{@link PageLimitInterceptor}
 *  3、向{@link Configuration}中注册CommonMybatisMapper.xml
 *  4、向{@link Configuration}中注册自动生成的XxxMapper.xml
 *
 * @author pengpeng
 * @version 1.0
 */
@SuppressWarnings("unchecked")
public class MapperBeanPostProcessor<E extends EntityObject> implements BeanPostProcessor {

    private final Map<SqlSessionFactory, EntityMapperRegistry<E>> entityMapperRegistries = new HashMap<>();

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof MapperFactoryBean) {
            MapperFactoryBean<?> mapperFactoryBean = (MapperFactoryBean<?>) bean;
            Class<?> mapperInterface = mapperFactoryBean.getMapperInterface();
            if(BaseEntityMapper.class.isAssignableFrom(mapperInterface)) {
                processEntityMapper((MapperFactoryBean<BaseEntityMapper<E>>)mapperFactoryBean);
            }
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }

    /**
     * 动态生成Java-Mapper接口的XML-Mapper文档并注册到Configuration中去
     *
     * @param mapperFactoryBean
     */
    protected void processEntityMapper(MapperFactoryBean<BaseEntityMapper<E>> mapperFactoryBean) {
        SqlSessionFactory sqlSessionFactory = mapperFactoryBean.getSqlSessionFactory();
        Assert.state(sqlSessionFactory != null, String.format("SqlSessionFactory can not be obtained in '%s'", MapperFactoryBean.class.getName()));
        EntityMapperRegistry<E> entityMapperRegistry = entityMapperRegistries.computeIfAbsent(sqlSessionFactory, EntityMapperRegistry::new);
        entityMapperRegistry.registerEntityMapper(mapperFactoryBean.getMapperInterface());
    }

    protected Map<SqlSessionFactory, EntityMapperRegistry<E>> getEntityMapperRegistries() {
        return entityMapperRegistries;
    }

}
