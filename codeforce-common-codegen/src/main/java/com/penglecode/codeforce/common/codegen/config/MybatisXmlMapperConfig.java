package com.penglecode.codeforce.common.codegen.config;

/**
 * Mybatis- Mapper XML文件生成配置
 *
 * @author pengpeng
 * @version 1.0
 */
public class MybatisXmlMapperConfig extends GenerableTargetConfig {

    @Override
    public String getGeneratedTargetName(String domainObjectName, boolean includePackage, boolean includeSuffix) {
        return (includePackage ? getTargetPackage() + "." : "") + domainObjectName + "Mapper" + (includeSuffix ? ".xml" : "");
    }

}
