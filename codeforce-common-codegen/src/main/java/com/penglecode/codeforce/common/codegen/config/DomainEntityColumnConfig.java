package com.penglecode.codeforce.common.codegen.config;

import com.penglecode.codeforce.common.codegen.database.IntrospectedColumn;
import com.penglecode.codeforce.common.codegen.support.IdGenerator;
import org.apache.ibatis.type.TypeHandler;

/**
 * 领域实体对应的数据库表列配置
 *
 * @author pengpeng
 * @version 1.0.0
 */
@SuppressWarnings("rawtypes")
public class DomainEntityColumnConfig {

    /** 列名 */
    private String columnName;

    /** 列中文名 */
    private String columnTitle;

    /** 是否是ID列 */
    private boolean idColumn;

    /** 主键生成策略 */
    private IdGenerator idGenerator;

    /** 是否是插入列 */
    private boolean columnOnInsert;

    /** 插入时是否需要校验 */
    private boolean validateOnInsert;

    /** 是否是更新列 */
    private boolean columnOnUpdate;

    /** 更新时是否需要校验 */
    private boolean validateOnUpdate;

    /** 不为空则指示当前列为分页查询条件列，该条件运算符支持eq,like|likeLeft|likeRight,gt|lt|gte|lte,in等 */
    private String queryOperator;

    /** 当前字段decode所需的枚举类型 */
    private String decodeEnumType;

    /** 覆盖当前列的自省Java类型 */
    private Class<?> javaType;

    /** 当前字段的select字句 */
    private String selectClause;

    /** Mybatis的TypeHandler */
    private Class<? extends TypeHandler> typeHandler;

    /** 对应的自省列 */
    private IntrospectedColumn introspectedColumn;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnTitle() {
        return columnTitle;
    }

    public void setColumnTitle(String columnTitle) {
        this.columnTitle = columnTitle;
    }

    public boolean isIdColumn() {
        return idColumn;
    }

    public void setIdColumn(boolean idColumn) {
        this.idColumn = idColumn;
    }

    public IdGenerator getIdGenerator() {
        return idGenerator;
    }

    public void setIdGenerator(String idGenerator) {
        this.idGenerator = IdGenerator.parseGenerator(idGenerator);
    }

    public boolean isColumnOnInsert() {
        return columnOnInsert;
    }

    public void setColumnOnInsert(boolean columnOnInsert) {
        this.columnOnInsert = columnOnInsert;
    }

    public boolean isValidateOnInsert() {
        return validateOnInsert;
    }

    public void setValidateOnInsert(boolean validateOnInsert) {
        this.validateOnInsert = validateOnInsert;
    }

    public boolean isColumnOnUpdate() {
        return columnOnUpdate;
    }

    public void setColumnOnUpdate(boolean columnOnUpdate) {
        this.columnOnUpdate = columnOnUpdate;
    }

    public boolean isValidateOnUpdate() {
        return validateOnUpdate;
    }

    public void setValidateOnUpdate(boolean validateOnUpdate) {
        this.validateOnUpdate = validateOnUpdate;
    }

    public void setColumnOnUpsert(boolean columnOnUpsert) {
        this.setColumnOnInsert(true);
        this.setColumnOnUpdate(true);
    }

    public void setValidateOnUpsert(boolean validateOnUpsert) {
        this.setValidateOnInsert(true);
        this.setValidateOnUpdate(true);
    }

    public String getQueryOperator() {
        return queryOperator;
    }

    public void setQueryOperator(String queryOperator) {
        this.queryOperator = queryOperator;
    }

    public String getDecodeEnumType() {
        return decodeEnumType;
    }

    public void setDecodeEnumType(String decodeEnumType) {
        this.decodeEnumType = decodeEnumType;
    }

    public Class<?> getJavaType() {
        return javaType;
    }

    public void setJavaType(Class<?> javaType) {
        this.javaType = javaType;
    }

    public String getSelectClause() {
        return selectClause;
    }

    public void setSelectClause(String selectClause) {
        this.selectClause = selectClause;
    }

    public Class<? extends TypeHandler> getTypeHandler() {
        return typeHandler;
    }

    public void setTypeHandler(Class<? extends TypeHandler> typeHandler) {
        this.typeHandler = typeHandler;
    }

    public IntrospectedColumn getIntrospectedColumn() {
        return introspectedColumn;
    }

    public void setIntrospectedColumn(IntrospectedColumn introspectedColumn) {
        this.introspectedColumn = introspectedColumn;
    }

}