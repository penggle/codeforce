package com.penglecode.codeforce.common.codegen.support;

import com.penglecode.codeforce.common.codegen.util.CodegenUtils;
import com.penglecode.codeforce.common.util.TemplateUtils;

import java.util.Collections;

/**
 * API接口方法枚举
 *
 * @author pengpeng
 * @version 1.0.0
 */
public enum ApiMethod {

    CREATE("createDomainObject", "创建领域对象"),
    MODIFY_BY_ID("modifyDomainObjectById", "根据ID修改领域对象"),
    REMOVE_BY_ID("removeDomainObjectById", "根据ID删除领域对象"),
    REMOVE_BY_IDS("removeDomainObjectsByIds", "根据多个ID删除领域对象") {
        @Override
        public String getMethodName(String domainObjectName) {
            return super.getMethodName(CodegenUtils.getPluralName(domainObjectName));
        }
    },
    GET_BY_ID("getDomainObjectById", "根据多个ID获取领域对象"),
    GET_BY_IDS("getDomainObjectsByIds", "根据多个ID获取领域对象") {
        @Override
        public String getMethodName(String domainObjectName) {
            return super.getMethodName(CodegenUtils.getPluralName(domainObjectName));
        }
    },
    GET_BY_PAGE("getDomainObjectsByPage", "根据多个ID获取领域对象") {
        @Override
        public String getMethodName(String domainObjectName) {
            return super.getMethodName(CodegenUtils.getPluralName(domainObjectName));
        }
    };

    private final String methodTpl;

    private final String methodDesc;

    ApiMethod(String methodTpl, String methodDesc) {
        this.methodTpl = methodTpl;
        this.methodDesc = methodDesc;
    }

    public String getMethodTpl() {
        return methodTpl;
    }

    public String getMethodDesc() {
        return methodDesc;
    }

    public String getMethodName(String domainObjectName) {
        return TemplateUtils.parseTemplate(getMethodTpl(), Collections.singletonMap("domainObjectName", domainObjectName));
    }

}
