package com.penglecode.codeforce.common.codegen.config;

import com.penglecode.codeforce.common.codegen.util.CodegenUtils;
import com.penglecode.codeforce.common.util.StringUtils;
import org.springframework.boot.autoconfigure.mds.NamedDatabase;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mybatis Mapper接口配置
 *
 * @author pengpeng
 * @version 1.0.0
 */
public class MybatisJavaMapperConfig extends GenerableTargetConfig {

    /** 当前领域对象的Mapper接口上的数据库名称,默认单库即:'@NamedDatabase("${runtimeDataSource}")' */
    private Set<String> mapperAnnotations;

    public Set<String> getMapperAnnotations() {
        return mapperAnnotations;
    }

    public void setMapperAnnotations(Set<String> mapperAnnotations) {
        this.mapperAnnotations = mapperAnnotations;
    }

    protected void initMapperAnnotations(DomainConfig domainConfig) {
        Set<String> finalMapperAnnotations = Optional.ofNullable(mapperAnnotations).orElseGet(LinkedHashSet::new);
        finalMapperAnnotations.add("@" + NamedDatabase.class.getSimpleName() + "(\"${runtimeDataSource}\")"); //增加默认的
        finalMapperAnnotations = finalMapperAnnotations
                .stream()
                .filter(StringUtils::isNotBlank)
                .map(expression -> CodegenUtils.parseAnnotationExpression(expression, domainConfig))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        this.setMapperAnnotations(finalMapperAnnotations);
    }

    @Override
    public String getGeneratedTargetName(String domainObjectName, boolean includePackage, boolean includeSuffix) {
        return (includePackage ? getTargetPackage() + "." : "") + domainObjectName + "Mapper" + (includeSuffix ? ".java" : "");
    }

}
