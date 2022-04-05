package com.penglecode.codeforce.mybatistiny.spring;

import com.penglecode.codeforce.common.domain.EntityObject;
import com.penglecode.codeforce.mybatistiny.mapper.BaseMybatisMapper;
import com.penglecode.codeforce.mybatistiny.support.MapperRegistry;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * Mybatis基于Spring框架的相关配置Bean(SqlSessionFactory、XxxMapper)的Bean后置处理程序。
 * 该BeanPostProcessor拦截刚创建好的{@link BaseMybatisMapper}子类(XxxMapper)的代理实例，做两件事情：
 *  1、向{@link Configuration}中注册CommonMybatisMapper.xml
 *  2、向{@link Configuration}中注册自动生成的XxxMapper.xml
 *
 * @author pengpeng
 * @version 1.0
 */
@SuppressWarnings("unchecked")
public class MapperBeanPostProcessor<E extends EntityObject> implements BeanPostProcessor {

    private final Map<Configuration,MapperRegistry<E>> mapperRegistries = new HashMap<>();

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof MapperFactoryBean) {
            MapperFactoryBean<?> mapperFactoryBean = (MapperFactoryBean<?>) bean;
            Class<?> mapperInterface = mapperFactoryBean.getMapperInterface();
            if(BaseMybatisMapper.class.isAssignableFrom(mapperInterface)) {
                processEntityMapper((MapperFactoryBean<BaseMybatisMapper<E>>)mapperFactoryBean);
            }
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }

    protected void processEntityMapper(MapperFactoryBean<BaseMybatisMapper<E>> mapperFactoryBean) {
        SqlSessionFactory sqlSessionFactory = mapperFactoryBean.getSqlSessionFactory();
        Assert.state(sqlSessionFactory != null, String.format("SqlSessionFactory can not be obtained in '%s'", MapperFactoryBean.class.getName()));
        MapperRegistry<E> mapperRegistry = mapperRegistries.computeIfAbsent(sqlSessionFactory.getConfiguration(), MapperRegistry::new);
        mapperRegistry.registerEntityMapper(mapperFactoryBean.getMapperInterface());
    }

}
