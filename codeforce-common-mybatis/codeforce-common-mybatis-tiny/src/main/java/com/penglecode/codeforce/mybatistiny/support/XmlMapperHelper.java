package com.penglecode.codeforce.mybatistiny.support;


import com.penglecode.codeforce.mybatistiny.dsl.QueryColumns;
import com.penglecode.codeforce.common.util.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Mybatis的XML-Mapper配置文件辅助类
 *
 * @author pengpeng
 * @version 1.0
 */
public class XmlMapperHelper {

    private XmlMapperHelper() {}

    public static boolean isEmpty(Object paramObj) {
        return !isNotEmpty(paramObj);
    }

    public static boolean isNotEmpty(Object paramObj) {
        if (paramObj == null) {
            return false;
        }
        if (paramObj instanceof String) {
            String str = (String) paramObj;
            return StringUtils.isNotEmpty(str);
        }
        if (paramObj.getClass().isArray()) {
            return Array.getLength(paramObj) > 0;
        }
        if (paramObj instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) paramObj;
            return !map.isEmpty();
        }
        if (paramObj instanceof Collection) {
            Collection<?> collection = (Collection<?>) paramObj;
            return !collection.isEmpty();
        }
        return true;
    }

    public static boolean isArrayOrCollection(Object paramObj) {
        if (paramObj == null) {
            return false;
        }
        return paramObj instanceof Collection || paramObj.getClass().isArray();
    }
    
    public static boolean containsColumn(Map<String,Object> columnNames, String columnName) {
    	if(columnNames != null) {
    		return columnNames.containsKey(columnName);
    	}
    	return false;
    }

    public static boolean containsColumn(QueryColumns[] columnArray, String columnName) {
        boolean selected = true;
        QueryColumns queryColumns = (columnArray != null && columnArray.length > 0) ? columnArray[0] : null;
        if(queryColumns != null) {
            Set<String> selectColumns = queryColumns.getColumns();
            if(!CollectionUtils.isEmpty(selectColumns)) {
                for(String selectColumn : selectColumns) {
                    if(selectColumn.equals(columnName)) {
                        return true;
                    }
                }
                selected = false;
            }
            if(queryColumns.getPredicate() != null) {
                return queryColumns.getPredicate().test(columnName);
            }
        }
        return selected;
    }

}
