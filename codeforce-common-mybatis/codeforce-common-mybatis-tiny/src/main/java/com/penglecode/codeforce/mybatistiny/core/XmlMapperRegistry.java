package com.penglecode.codeforce.mybatistiny.core;

import com.penglecode.codeforce.common.domain.EntityObject;
import com.penglecode.codeforce.common.util.ReflectionUtils;
import com.penglecode.codeforce.mybatistiny.exception.XMLMapperParseException;
import com.penglecode.codeforce.mybatistiny.interceptor.DomainObjectQueryInterceptor;
import com.penglecode.codeforce.mybatistiny.interceptor.PageLimitInterceptor;
import com.penglecode.codeforce.mybatistiny.mapper.BaseMybatisMapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.executor.ErrorContext;
import com.penglecode.codeforce.mybatistiny.CustomConfiguration;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * XML-Mapper注册器
 * 通过实体Java-Mapper接口来注册自动生成的XML-Mapper，具体做了下面四件事
 *
 *  1、代理SqlSessionFactory中的configuration属性，如果可能的话
 *  2、向{@link Configuration}中注册{@link DomainObjectQueryInterceptor}和{@link PageLimitInterceptor}
 *  3、向{@link Configuration}中注册CommonMybatisMapper.xml
 *  4、向{@link Configuration}中注册自动生成的XxxMapper.xml
 *
 * @author pengpeng
 * @version 1.0
 */
public class XmlMapperRegistry<E extends EntityObject> {

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlMapperRegistry.class);

    private final SqlSessionFactory sqlSessionFactory;

    private final CustomConfiguration configuration;

    private final XmlMapperTemplateParameterFactory<E> xmlMapperTemplateParameterFactory;

    public XmlMapperRegistry(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
        this.configuration = delegateConfiguration();
        this.xmlMapperTemplateParameterFactory = createTemplateParameterFactory();
        this.registerCommonPlugin();
        this.registerCommonMapper();
    }

    /**
     * 代理DefaultSqlSessionFactory中Configuration
     */
    protected CustomConfiguration delegateConfiguration() {
        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        Assert.state(sqlSessionFactory instanceof DefaultSqlSessionFactory, String.format("Can not delegate 'configuration' of SqlSessionFactory[%s], expected sqlSessionFactory is the type of %s!", sqlSessionFactory, DefaultSqlSessionFactory.class.getName()));
        CustomConfiguration delegateConfiguration;
        Configuration configuration = sqlSessionFactory.getConfiguration();
        if(!(configuration instanceof CustomConfiguration)) { //避免可能出现的重复delegate
            delegateConfiguration = new CustomConfiguration(configuration);
            ReflectionUtils.setFinalFieldValue(sqlSessionFactory, "configuration", delegateConfiguration);
            LOGGER.info(">>> Successfully delegate 'configuration' of DefaultSqlSessionFactory[{}]", sqlSessionFactory);
        } else {
            delegateConfiguration = (CustomConfiguration) configuration;
        }
        return delegateConfiguration;
    }

    /**
     * 注册CommonMybatisMapper.xml
     * 该公共XML-Mapper每个Configuration仅需注册一次即可
     */
    protected void registerCommonMapper() {
        registerXmlMapper(new ClassPathResource(BaseMybatisMapper.class.getPackage().getName().replace(".", "/") + "/CommonMybatisMapper.xml"));
    }

    /**
     * 注册公共的插件
     */
    protected void registerCommonPlugin() {
        Configuration configuration = getSqlSessionFactory().getConfiguration();
        List<Interceptor> interceptors = configuration.getInterceptors();
        if(CollectionUtils.isEmpty(interceptors) || interceptors.stream().noneMatch(interceptor -> interceptor instanceof DomainObjectQueryInterceptor)) {
            configuration.addInterceptor(new DomainObjectQueryInterceptor());
            LOGGER.info(">>> Dynamically registered interceptor[{}] into {}", DomainObjectQueryInterceptor.class.getName(), configuration);
        }
        if(CollectionUtils.isEmpty(interceptors) || interceptors.stream().noneMatch(interceptor -> interceptor instanceof PageLimitInterceptor)) {
            configuration.addInterceptor(new PageLimitInterceptor());
            LOGGER.info(">>> Dynamically registered interceptor[{}] into {}", PageLimitInterceptor.class.getName(), configuration);
        }
    }

    protected XmlMapperTemplateParameterFactory<E> createTemplateParameterFactory() {
        Configuration configuration = getSqlSessionFactory().getConfiguration();
        Assert.hasText(configuration.getDatabaseId(), "Property 'databaseId' must be required in Mybatis Configuration!");
        return new XmlMapperTemplateParameterFactory<>(configuration.getDatabaseId());
    }

    public void registerEntityMapper(Class<BaseMybatisMapper<E>> entityMapperClass) {
        //创建实体元数据
        EntityMeta<E> entityMeta = createEntityMeta(entityMapperClass);
        //为动态生成的实体XxxMapper.xml创建所需的模板参数
        XmlMapperTemplateParameter templateParameter = getXmlMapperTemplateParameterFactory().createTemplateParameter(entityMeta);
        freemarker.template.Configuration configuration = new freemarker.template.Configuration(freemarker.template.Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        configuration.setDefaultEncoding(StandardCharsets.UTF_8.name());
        Class<?> resourceLoadClass = BaseMybatisMapper.class;
        configuration.setClassForTemplateLoading(resourceLoadClass, "/" + resourceLoadClass.getPackage().getName().replace(".", "/"));
        try {
            Template xmlMapperTemplate = configuration.getTemplate("BaseMybatisMapper.ftl");
            StringWriter xmlMapperWriter = new StringWriter();
            xmlMapperTemplate.process(templateParameter, xmlMapperWriter);
            String xmlMapperContent = xmlMapperWriter.toString();
            String xmlMapperLocation = entityMapperClass.getName().replace(".", "/") + ".xml";
            String xmlMapperResourceName = registerXmlMapper(new ByteArrayResource(xmlMapperContent.getBytes(StandardCharsets.UTF_8), xmlMapperLocation));
            LOGGER.debug("<-----------------------------【{}】----------------------------->\n{}", xmlMapperResourceName, xmlMapperContent);
        } catch (IOException | TemplateException e) {
            throw new XMLMapperParseException("Failed to parse 'BaseMybatisMapper.ftl'", e);
        }
    }

    /**
     * 创建实体元数据
     *
     * @param entityMapperClass
     * @return
     */
    protected EntityMeta<E> createEntityMeta(Class<BaseMybatisMapper<E>> entityMapperClass) {
        return new EntityMeta<>(entityMapperClass);
    }

    /**
     * 注册动态生成的实体XxxMapper.xml
     *
     * @param xmlMapperResource
     * @return
     */
    protected String registerXmlMapper(Resource xmlMapperResource) {
        Configuration configuration = getSqlSessionFactory().getConfiguration();
        try {
            String xmlMapperResourceName = xmlMapperResource.getDescription();
            xmlMapperResourceName = xmlMapperResourceName.substring(xmlMapperResourceName.indexOf('[') + 1, xmlMapperResourceName.lastIndexOf(']'));
            //考虑到开发者可以自定义XxxMapper.xml，所以必须要重命名，否则与mybatis-spring默认加载的有可能重名，导致MappedStatement加载不到Configuration中去
            xmlMapperResourceName = String.format("Auto-Generated XML-Mapper [%s]", xmlMapperResourceName);
            XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(xmlMapperResource.getInputStream(), configuration, xmlMapperResourceName, configuration.getSqlFragments());
            xmlMapperBuilder.parse();
            LOGGER.info(">>> Dynamically registered {} into {}", xmlMapperResourceName, configuration);
            return xmlMapperResourceName;
        } catch (IOException e) {
            throw new XMLMapperParseException("Failed to parse mapping resource: '" + xmlMapperResource + "'", e);
        } finally {
            ErrorContext.instance().reset();
        }
    }

    protected SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }

    protected CustomConfiguration getConfiguration() {
        return configuration;
    }

    protected XmlMapperTemplateParameterFactory<E> getXmlMapperTemplateParameterFactory() {
        return xmlMapperTemplateParameterFactory;
    }

}
