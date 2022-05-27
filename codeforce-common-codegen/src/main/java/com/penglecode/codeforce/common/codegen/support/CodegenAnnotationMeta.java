package com.penglecode.codeforce.common.codegen.support;

import java.util.Set;

/**
 * 代码生产注解元数据
 *
 * @author pengpeng
 * @version 1.0
 */
public class CodegenAnnotationMeta {

    /** 注解表达式 */
    private final String expression;

    /** 注解表达式中所涉及的导入类型 */
    private final Set<String> importTypes;

    public CodegenAnnotationMeta(String expression, Set<String> importTypes) {
        this.expression = expression;
        this.importTypes = importTypes;
    }

    public String getExpression() {
        return expression;
    }

    public Set<String> getImportTypes() {
        return importTypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CodegenAnnotationMeta)) return false;
        CodegenAnnotationMeta that = (CodegenAnnotationMeta) o;
        return expression.equals(that.expression);
    }

    @Override
    public int hashCode() {
        return expression.hashCode();
    }

}
