package com.penglecode.codeforce.mybatistiny.support;

import com.penglecode.codeforce.common.domain.EntityObject;
import com.penglecode.codeforce.mybatistiny.annotations.Column;
import com.penglecode.codeforce.mybatistiny.annotations.Id;
import com.penglecode.codeforce.mybatistiny.annotations.Table;
import org.apache.ibatis.type.JdbcType;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 实体表元数据信息
 *
 * @author pengpeng
 * @version 1.0
 */
public class EntityMeta<E extends EntityObject> {

    private final Class<E> entityClass;

    private final Map<String,FieldMeta> entityFields;

    private final Table tableAnnotation;

    public EntityMeta(Class<E> entityClass, Map<String, FieldMeta> entityFields) {
        this.entityClass = entityClass;
        this.entityFields = entityFields;
        this.tableAnnotation = entityClass.getAnnotation(Table.class);
    }

    public Class<E> getEntityClass() {
        return entityClass;
    }

    public Map<String, FieldMeta> getEntityFields() {
        return entityFields;
    }

    public Table getTableAnnotation() {
        return tableAnnotation;
    }

    public static class FieldMeta {

        /**
         * 字段名
         */
        private final String fieldName;

        /**
         * 列名
         */
        private final String columnName;

        /**
         * 列名的jdbcType
         */
        private final JdbcType jdbcType;

        /**
         * 字段的java.lang.reflect.Field实例
         */
        private final Field javaField;

        /**
         * 主键@Id主键，如果当前字段是主键的话，则该值不为空
         */
        private final Id idAnnotation;

        /**
         * 如果当前字段被@Column注解的话，则该值不为空
         */
        private final Column columnAnnotation;

        public FieldMeta(String fieldName, String columnName, JdbcType jdbcType, Field javaField) {
            this.fieldName = fieldName;
            this.columnName = columnName;
            this.jdbcType = jdbcType;
            this.javaField = javaField;
            this.idAnnotation = javaField.getAnnotation(Id.class);
            this.columnAnnotation = javaField.getAnnotation(Column.class);
        }

        public String getFieldName() {
            return fieldName;
        }

        public String getColumnName() {
            return columnName;
        }

        public JdbcType getJdbcType() {
            return jdbcType;
        }

        public Field getJavaField() {
            return javaField;
        }

        public Id getIdAnnotation() {
            return idAnnotation;
        }

        public Column getColumnAnnotation() {
            return columnAnnotation;
        }

    }


}
