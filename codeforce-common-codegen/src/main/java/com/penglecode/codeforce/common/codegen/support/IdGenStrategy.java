package com.penglecode.codeforce.common.codegen.support;

/**
 * ID生成策略
 *
 * @author pengpeng
 * @version 1.0
 */
public enum IdGenStrategy {
    //数据库自动生成(自增主键)
    IDENTITY() {
        @Override
        public String buildGenerator(String parameter) {
            return this.name() + "(" + ")";
        }
    },
    //ORACLE数据库的序列
    SEQUENCE() {
        @Override
        public String buildGenerator(String parameter) {
            return this.name() + "(" + parameter + ")";
        }
    };

    public abstract String buildGenerator(String parameter);

}
