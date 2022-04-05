package com.penglecode.codeforce.mybatistiny.support;

import com.penglecode.codeforce.common.domain.EntityObject;
import com.penglecode.codeforce.mybatistiny.exception.XMLMapperParseException;
import com.penglecode.codeforce.mybatistiny.interceptor.DomainObjectQueryInterceptor;
import com.penglecode.codeforce.mybatistiny.interceptor.PageLimitInterceptor;
import com.penglecode.codeforce.mybatistiny.mapper.BaseMybatisMapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.Configuration;
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
 * 通过实体Java-Mapper接口来注册自动生成的XML-Mapper
 *
 *  1、向{@link Configuration}中注册CommonMybatisMapper.xml
 *  2、向{@link Configuration}中注册自动生成的XxxMapper.xml
 *
 * @author pengpeng
 * @version 1.0
 */
public class MapperRegistry<E extends EntityObject> {

    private final Configuration configuration;

    private final MapperTemplateParameterFactory<E> mapperTemplateParameterFactory;

    public MapperRegistry(Configuration configuration) {
        this.configuration = configuration;
        this.mapperTemplateParameterFactory = createTemplateParameterFactory(configuration);
        this.registerCommonPlugin();
        this.registerCommonMapper();
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
        List<Interceptor> interceptors = configuration.getInterceptors();
        if(!CollectionUtils.isEmpty(interceptors)) {
            if(interceptors.stream().noneMatch(interceptor -> interceptor instanceof DomainObjectQueryInterceptor)) {
                configuration.addInterceptor(new DomainObjectQueryInterceptor());
            }
            if(interceptors.stream().noneMatch(interceptor -> interceptor instanceof PageLimitInterceptor)) {
                configuration.addInterceptor(new PageLimitInterceptor());
            }
        }
    }

    protected MapperTemplateParameterFactory<E> createTemplateParameterFactory(Configuration configuration) {
        Assert.hasText(configuration.getDatabaseId(), "Property 'databaseId' must be required in Mybatis Configuration!");
        return new MapperTemplateParameterFactory<>(configuration.getDatabaseId());
    }

    public void registerEntityMapper(Class<BaseMybatisMapper<E>> entityMapperClass) {
        MapperTemplateParameter templateParameter = getMapperTemplateParameterFactory().createTemplateParameter(entityMapperClass);
        freemarker.template.Configuration configuration = new freemarker.template.Configuration(freemarker.template.Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        configuration.setDefaultEncoding(StandardCharsets.UTF_8.name());
        Class<?> resourceLoadClass = getClass();
        configuration.setClassForTemplateLoading(resourceLoadClass, "/" + resourceLoadClass.getPackage().getName().replace(".", "/"));
        try {
            Template xmlMapperTemplate = configuration.getTemplate("BaseMybatisMapper.ftl");
            StringWriter xmlMapperWriter = new StringWriter();
            xmlMapperTemplate.process(templateParameter, xmlMapperWriter);
            String xmlMapperContent = xmlMapperWriter.toString();
            String xmlMapperLocation = entityMapperClass.getName().replace(".", "/") + ".xml";
            System.out.printf("<-----------------------------【%s】----------------------------->%n", xmlMapperLocation);
            System.out.println(xmlMapperContent);
            registerXmlMapper(new ByteArrayResource(xmlMapperContent.getBytes(StandardCharsets.UTF_8), String.format("Auto-Generated XML-Mapper [%s]", xmlMapperLocation)));
        } catch (IOException | TemplateException e) {
            throw new XMLMapperParseException("Failed to parse 'BaseMybatisMapper.ftl'", e);
        }
    }

    protected void registerXmlMapper(Resource xmlMapperResource) {
        try {
            XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(xmlMapperResource.getInputStream(), configuration, xmlMapperResource.toString(), configuration.getSqlFragments());
            xmlMapperBuilder.parse();
        } catch (IOException e) {
            throw new XMLMapperParseException("Failed to parse mapping resource: '" + xmlMapperResource + "'", e);
        } finally {
            ErrorContext.instance().reset();
        }
    }

    protected Configuration getConfiguration() {
        return configuration;
    }

    protected MapperTemplateParameterFactory<E> getMapperTemplateParameterFactory() {
        return mapperTemplateParameterFactory;
    }

}
