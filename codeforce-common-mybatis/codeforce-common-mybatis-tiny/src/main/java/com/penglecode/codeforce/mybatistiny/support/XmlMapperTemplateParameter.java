package com.penglecode.codeforce.mybatistiny.support;

import com.penglecode.codeforce.common.domain.EntityObject;
import com.penglecode.codeforce.mybatistiny.support.EntityMeta.EntityField;

import java.util.List;

/**
 * BaseMybatisMapper.ftl模板参数
 *
 * @author pengpeng
 * @version 1.0
 */
public class XmlMapperTemplateParameter {

    /** 实体元数据 */
    private EntityMeta<? extends EntityObject> entityMeta;

    /** 实体XML-Mapper的namespace */
    private String mapperNamespace;

    /** MapperHelper类全名 */
    private String mapperHelperClass;

    /** 表名 */
    private String tableName;

    /** 表别名 */
    private String tableAlias;

    /** 实体名称 */
    private String entityName;

    /** ID生成策略(SEQUENCE,IDENTITY,NONE) */
    private String idStrategy;

    /** ID生成器名称，例如当idStrategy=SEQUENCE时，该字段为指定的序列名称 */
    private String idGenerator;

    /** ID列 */
    private List<ColumnParameter> idColumns;

    /** INSERT列 */
    private List<ColumnParameter> insertColumns;

    /** UPDATE列 */
    private List<ColumnParameter> updateColumns;

    /** 全部SELECT列 */
    private List<ColumnParameter> selectColumns;

    /** 全部列 */
    private List<ColumnParameter> allColumns;

    /** DELETE语句别名 */
    private String deleteTargetAlias;

    public EntityMeta<? extends EntityObject> getEntityMeta() {
        return entityMeta;
    }

    public void setEntityMeta(EntityMeta<? extends EntityObject> entityMeta) {
        this.entityMeta = entityMeta;
    }

    public String getMapperNamespace() {
        return mapperNamespace;
    }

    public void setMapperNamespace(String mapperNamespace) {
        this.mapperNamespace = mapperNamespace;
    }

    public String getMapperHelperClass() {
        return mapperHelperClass;
    }

    public void setMapperHelperClass(String mapperHelperClass) {
        this.mapperHelperClass = mapperHelperClass;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableAlias() {
        return tableAlias;
    }

    public void setTableAlias(String tableAlias) {
        this.tableAlias = tableAlias;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getIdStrategy() {
        return idStrategy;
    }

    public void setIdStrategy(String idStrategy) {
        this.idStrategy = idStrategy;
    }

    public String getIdGenerator() {
        return idGenerator;
    }

    public void setIdGenerator(String idGenerator) {
        this.idGenerator = idGenerator;
    }

    public List<ColumnParameter> getIdColumns() {
        return idColumns;
    }

    public void setIdColumns(List<ColumnParameter> idColumns) {
        this.idColumns = idColumns;
    }

    public List<ColumnParameter> getInsertColumns() {
        return insertColumns;
    }

    public void setInsertColumns(List<ColumnParameter> insertColumns) {
        this.insertColumns = insertColumns;
    }

    public List<ColumnParameter> getUpdateColumns() {
        return updateColumns;
    }

    public void setUpdateColumns(List<ColumnParameter> updateColumns) {
        this.updateColumns = updateColumns;
    }

    public List<ColumnParameter> getSelectColumns() {
        return selectColumns;
    }

    public void setSelectColumns(List<ColumnParameter> selectColumns) {
        this.selectColumns = selectColumns;
    }

    public List<ColumnParameter> getAllColumns() {
        return allColumns;
    }

    public void setAllColumns(List<ColumnParameter> allColumns) {
        this.allColumns = allColumns;
    }

    public String getDeleteTargetAlias() {
        return deleteTargetAlias;
    }

    public void setDeleteTargetAlias(String deleteTargetAlias) {
        this.deleteTargetAlias = deleteTargetAlias;
    }

    /**
     * 实体字段对应的数据库列参数
     */
    public static class ColumnParameter {

        /** 列名 */
        private final String columnName;

        /** 列对应的JDBC类型 */
        private final Integer jdbcType;

        /** 列对应的JDBC类型 */
        private final String jdbcTypeName;

        /** 列对应的Java字段名 */
        private final String fieldName;

        /** 列对应的Java字段类型 */
        private final Class<?> fieldType;

        /** 是否是ID列 */
        private final boolean idColumn;

        /** 当前列对应的Java字段 */
        private final EntityField entityField;

        public ColumnParameter(EntityField entityField) {
            this.entityField = entityField;
            this.fieldName = entityField.getFieldName();
            this.fieldType = entityField.getFieldType();
            this.jdbcType = entityField.getJdbcType().TYPE_CODE;
            this.jdbcTypeName = entityField.getJdbcType().name();
            this.columnName = entityField.getColumnName();
            this.idColumn = entityField.getIdAnnotation() != null;
        }

        public String getColumnName() {
            return columnName;
        }

        public Integer getJdbcType() {
            return jdbcType;
        }

        public String getJdbcTypeName() {
            return jdbcTypeName;
        }

        public String getFieldName() {
            return fieldName;
        }

        public Class<?> getFieldType() {
            return fieldType;
        }

        public boolean isIdColumn() {
            return idColumn;
        }

        public EntityField getEntityField() {
            return entityField;
        }
    }

}
