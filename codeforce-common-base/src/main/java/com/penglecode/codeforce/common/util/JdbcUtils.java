package com.penglecode.codeforce.common.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * JDBC工具类
 *
 * @author pengpeng
 * @version 1.0.0
 */
public class JdbcUtils {

    /**
     * Constant that indicates an unknown (or unspecified) SQL type.
     * @see java.sql.Types
     */
    public static final int TYPE_UNKNOWN = Integer.MIN_VALUE;

    private static final Log logger = LogFactory.getLog(JdbcUtils.class);

    private static final Map<Integer, String> typeNames = new HashMap<>();

    private static Boolean mysqlDriverVersion6;

    static {
        try {
            for (Field field : Types.class.getFields()) {
                typeNames.put((Integer) field.get(null), field.getName());
            }
        }
        catch (Exception ex) {
            throw new IllegalStateException("Failed to resolve JDBC Types constants", ex);
        }
    }

    /**
     * Close the given JDBC Connection and ignore any thrown exception.
     * This is useful for typical finally blocks in manual JDBC code.
     * @param con the JDBC Connection to close (may be {@code null})
     */
    public static void closeConnection(@Nullable Connection con) {
        if (con != null) {
            try {
                con.close();
            }
            catch (SQLException ex) {
                logger.debug("Could not close JDBC Connection", ex);
            }
            catch (Throwable ex) {
                // We don't trust the JDBC driver: It might throw RuntimeException or Error.
                logger.debug("Unexpected exception on closing JDBC Connection", ex);
            }
        }
    }

    /**
     * Close the given JDBC Statement and ignore any thrown exception.
     * This is useful for typical finally blocks in manual JDBC code.
     * @param stmt the JDBC Statement to close (may be {@code null})
     */
    public static void closeStatement(@Nullable Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            }
            catch (SQLException ex) {
                logger.trace("Could not close JDBC Statement", ex);
            }
            catch (Throwable ex) {
                // We don't trust the JDBC driver: It might throw RuntimeException or Error.
                logger.trace("Unexpected exception on closing JDBC Statement", ex);
            }
        }
    }

    /**
     * Close the given JDBC ResultSet and ignore any thrown exception.
     * This is useful for typical finally blocks in manual JDBC code.
     * @param rs the JDBC ResultSet to close (may be {@code null})
     */
    public static void closeResultSet(@Nullable ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            }
            catch (SQLException ex) {
                logger.trace("Could not close JDBC ResultSet", ex);
            }
            catch (Throwable ex) {
                // We don't trust the JDBC driver: It might throw RuntimeException or Error.
                logger.trace("Unexpected exception on closing JDBC ResultSet", ex);
            }
        }
    }

    /**
     * Retrieve a JDBC column value from a ResultSet, using the specified value type.
     * <p>Uses the specifically typed ResultSet accessor methods, falling back to
     * {@link #getResultSetValue(java.sql.ResultSet, int)} for unknown types.
     * <p>Note that the returned value may not be assignable to the specified
     * required type, in case of an unknown type. Calling code needs to deal
     * with this case appropriately, e.g. throwing a corresponding exception.
     * @param rs is the ResultSet holding the data
     * @param index is the column index
     * @param requiredType the required value type (may be {@code null})
     * @return the value object (possibly not of the specified required type,
     * with further conversion steps necessary)
     * @throws SQLException if thrown by the JDBC API
     * @see #getResultSetValue(ResultSet, int)
     */
    @Nullable
    public static Object getResultSetValue(ResultSet rs, int index, @Nullable Class<?> requiredType) throws SQLException {
        if (requiredType == null) {
            return getResultSetValue(rs, index);
        }

        Object value;

        // Explicitly extract typed value, as far as possible.
        if (String.class == requiredType) {
            return rs.getString(index);
        }
        else if (boolean.class == requiredType || Boolean.class == requiredType) {
            value = rs.getBoolean(index);
        }
        else if (byte.class == requiredType || Byte.class == requiredType) {
            value = rs.getByte(index);
        }
        else if (short.class == requiredType || Short.class == requiredType) {
            value = rs.getShort(index);
        }
        else if (int.class == requiredType || Integer.class == requiredType) {
            value = rs.getInt(index);
        }
        else if (long.class == requiredType || Long.class == requiredType) {
            value = rs.getLong(index);
        }
        else if (float.class == requiredType || Float.class == requiredType) {
            value = rs.getFloat(index);
        }
        else if (double.class == requiredType || Double.class == requiredType ||
                Number.class == requiredType) {
            value = rs.getDouble(index);
        }
        else if (BigDecimal.class == requiredType) {
            return rs.getBigDecimal(index);
        }
        else if (java.sql.Date.class == requiredType) {
            return rs.getDate(index);
        }
        else if (java.sql.Time.class == requiredType) {
            return rs.getTime(index);
        }
        else if (java.sql.Timestamp.class == requiredType || java.util.Date.class == requiredType) {
            return rs.getTimestamp(index);
        }
        else if (byte[].class == requiredType) {
            return rs.getBytes(index);
        }
        else if (Blob.class == requiredType) {
            return rs.getBlob(index);
        }
        else if (Clob.class == requiredType) {
            return rs.getClob(index);
        }
        else if (requiredType.isEnum()) {
            // Enums can either be represented through a String or an enum index value:
            // leave enum type conversion up to the caller (e.g. a ConversionService)
            // but make sure that we return nothing other than a String or an Integer.
            Object obj = rs.getObject(index);
            if (obj instanceof String) {
                return obj;
            }
            else if (obj instanceof Number) {
                // Defensively convert any Number to an Integer (as needed by our
                // ConversionService's IntegerToEnumConverterFactory) for use as index
                return NumberUtils.convertNumberToTargetClass((Number) obj, Integer.class);
            }
            else {
                // e.g. on Postgres: getObject returns a PGObject but we need a String
                return rs.getString(index);
            }
        }

        else {
            // Some unknown type desired -> rely on getObject.
            try {
                return rs.getObject(index, requiredType);
            }
            catch (AbstractMethodError err) {
                logger.debug("JDBC driver does not implement JDBC 4.1 'getObject(int, Class)' method", err);
            }
            catch (SQLFeatureNotSupportedException ex) {
                logger.debug("JDBC driver does not support JDBC 4.1 'getObject(int, Class)' method", ex);
            }
            catch (SQLException ex) {
                logger.debug("JDBC driver has limited support for JDBC 4.1 'getObject(int, Class)' method", ex);
            }

            // Corresponding SQL types for JSR-310 / Joda-Time types, left up
            // to the caller to convert them (e.g. through a ConversionService).
            String typeName = requiredType.getSimpleName();
            switch (typeName) {
                case "LocalDate":
                    return rs.getDate(index);
                case "LocalTime":
                    return rs.getTime(index);
                case "LocalDateTime":
                    return rs.getTimestamp(index);
            }

            // Fall back to getObject without type specification, again
            // left up to the caller to convert the value if necessary.
            return getResultSetValue(rs, index);
        }

        // Perform was-null check if necessary (for results that the JDBC driver returns as primitives).
        return (rs.wasNull() ? null : value);
    }

    /**
     * Retrieve a JDBC column value from a ResultSet, using the most appropriate
     * value type. The returned value should be a detached value object, not having
     * any ties to the active ResultSet: in particular, it should not be a Blob or
     * Clob object but rather a byte array or String representation, respectively.
     * <p>Uses the {@code getObject(index)} method, but includes additional "hacks"
     * to get around Oracle 10g returning a non-standard object for its TIMESTAMP
     * datatype and a {@code java.sql.Date} for DATE columns leaving out the
     * time portion: These columns will explicitly be extracted as standard
     * {@code java.sql.Timestamp} object.
     * @param rs is the ResultSet holding the data
     * @param index is the column index
     * @return the value object
     * @throws SQLException if thrown by the JDBC API
     * @see java.sql.Blob
     * @see java.sql.Clob
     * @see java.sql.Timestamp
     */
    @Nullable
    public static Object getResultSetValue(ResultSet rs, int index) throws SQLException {
        Object obj = rs.getObject(index);
        String className = null;
        if (obj != null) {
            className = obj.getClass().getName();
        }
        if (obj instanceof Blob) {
            Blob blob = (Blob) obj;
            obj = blob.getBytes(1, (int) blob.length());
        }
        else if (obj instanceof Clob) {
            Clob clob = (Clob) obj;
            obj = clob.getSubString(1, (int) clob.length());
        }
        else if ("oracle.sql.TIMESTAMP".equals(className) || "oracle.sql.TIMESTAMPTZ".equals(className)) {
            obj = rs.getTimestamp(index);
        }
        else if (className != null && className.startsWith("oracle.sql.DATE")) {
            String metaDataClassName = rs.getMetaData().getColumnClassName(index);
            if ("java.sql.Timestamp".equals(metaDataClassName) || "oracle.sql.TIMESTAMP".equals(metaDataClassName)) {
                obj = rs.getTimestamp(index);
            }
            else {
                obj = rs.getDate(index);
            }
        }
        else if (obj instanceof java.sql.Date) {
            if ("java.sql.Timestamp".equals(rs.getMetaData().getColumnClassName(index))) {
                obj = rs.getTimestamp(index);
            }
        }
        return obj;
    }

    /**
     * Return whether the given JDBC driver supports JDBC 2.0 batch updates.
     * <p>Typically invoked right before execution of a given set of statements:
     * to decide whether the set of SQL statements should be executed through
     * the JDBC 2.0 batch mechanism or simply in a traditional one-by-one fashion.
     * <p>Logs a warning if the "supportsBatchUpdates" methods throws an exception
     * and simply returns {@code false} in that case.
     * @param con the Connection to check
     * @return whether JDBC 2.0 batch updates are supported
     * @see java.sql.DatabaseMetaData#supportsBatchUpdates()
     */
    public static boolean supportsBatchUpdates(Connection con) {
        try {
            DatabaseMetaData dbmd = con.getMetaData();
            if (dbmd != null) {
                if (dbmd.supportsBatchUpdates()) {
                    logger.debug("JDBC driver supports batch updates");
                    return true;
                }
                else {
                    logger.debug("JDBC driver does not support batch updates");
                }
            }
        }
        catch (SQLException ex) {
            logger.debug("JDBC driver 'supportsBatchUpdates' method threw exception", ex);
        }
        return false;
    }

    /**
     * Extract a common name for the target database in use even if
     * various drivers/platforms provide varying names at runtime.
     * @param source the name as provided in database meta-data
     * @return the common name to be used (e.g. "DB2" or "Sybase")
     */
    @Nullable
    public static String commonDatabaseName(@Nullable String source) {
        String name = source;
        if (source != null && source.startsWith("DB2")) {
            name = "DB2";
        }
        else if ("MariaDB".equals(source)) {
            name = "MySQL";
        }
        else if ("Sybase SQL Server".equals(source) ||
                "Adaptive Server Enterprise".equals(source) ||
                "ASE".equals(source) ||
                "sql server".equalsIgnoreCase(source) ) {
            name = "Sybase";
        }
        return name;
    }

    /**
     * Check whether the given SQL type is numeric.
     * @param sqlType the SQL type to be checked
     * @return whether the type is numeric
     */
    public static boolean isNumeric(int sqlType) {
        return (Types.BIT == sqlType || Types.BIGINT == sqlType || Types.DECIMAL == sqlType ||
                Types.DOUBLE == sqlType || Types.FLOAT == sqlType || Types.INTEGER == sqlType ||
                Types.NUMERIC == sqlType || Types.REAL == sqlType || Types.SMALLINT == sqlType ||
                Types.TINYINT == sqlType);
    }

    /**
     * Resolve the standard type name for the given SQL type, if possible.
     * @param sqlType the SQL type to resolve
     * @return the corresponding constant name in {@link java.sql.Types}
     * (e.g. "VARCHAR"/"NUMERIC"), or {@code null} if not resolvable
     * @since 5.2
     */
    @Nullable
    public static String resolveTypeName(int sqlType) {
        return typeNames.get(sqlType);
    }

    /**
     * Determine the column name to use. The column name is determined based on a
     * lookup using ResultSetMetaData.
     * <p>This method implementation takes into account recent clarifications
     * expressed in the JDBC 4.0 specification:
     * <p><i>columnLabel - the label for the column specified with the SQL AS clause.
     * If the SQL AS clause was not specified, then the label is the name of the column</i>.
     * @param resultSetMetaData the current meta-data to use
     * @param columnIndex the index of the column for the look up
     * @return the column name to use
     * @throws SQLException in case of lookup failure
     */
    public static String lookupColumnName(ResultSetMetaData resultSetMetaData, int columnIndex) throws SQLException {
        String name = resultSetMetaData.getColumnLabel(columnIndex);
        if (!StringUtils.hasLength(name)) {
            name = resultSetMetaData.getColumnName(columnIndex);
        }
        return name;
    }

    /**
     * Convert a column name with underscores to the corresponding property name using "camel case".
     * A name like "customer_number" would match a "customerNumber" property name.
     * @param name the column name to be converted
     * @return the name using "camel case"
     */
    public static String convertUnderscoreNameToPropertyName(@Nullable String name) {
        StringBuilder result = new StringBuilder();
        boolean nextIsUpper = false;
        if (name != null && name.length() > 0) {
            if (name.length() > 1 && name.charAt(1) == '_') {
                result.append(Character.toUpperCase(name.charAt(0)));
            }
            else {
                result.append(Character.toLowerCase(name.charAt(0)));
            }
            for (int i = 1; i < name.length(); i++) {
                char c = name.charAt(i);
                if (c == '_') {
                    nextIsUpper = true;
                }
                else {
                    if (nextIsUpper) {
                        result.append(Character.toUpperCase(c));
                        nextIsUpper = false;
                    }
                    else {
                        result.append(Character.toLowerCase(c));
                    }
                }
            }
        }
        return result.toString();
    }

    /**
     * 根据jdbcUrl来解析其驱动类名
     * @param jdbcUrl
     * @return
     * @throws SQLException
     */
	public static String getDriverClassName(String jdbcUrl) throws SQLException {
        if (jdbcUrl == null) {
            return null;
        }
        
        if (jdbcUrl.startsWith("jdbc:derby:")) {
            return "org.apache.derby.jdbc.EmbeddedDriver";
        } else if (jdbcUrl.startsWith("jdbc:mysql:")) {
            if (mysqlDriverVersion6 == null) {
                Class<?> driverClass = null;
                try {
                    driverClass = ClassUtils.forName("com.mysql.cj.jdbc.Driver", ClassUtils.getDefaultClassLoader());
                } catch (Exception e) {
                    //ignore
                }
                mysqlDriverVersion6 = driverClass != null;
            }

            if (mysqlDriverVersion6) {
                return "com.mysql.cj.jdbc.Driver";
            } else {
                return "com.mysql.jdbc.Driver";
            }
        } else if (jdbcUrl.startsWith("jdbc:log4jdbc:")) {
            return "net.sf.log4jdbc.DriverSpy";
        } else if (jdbcUrl.startsWith("jdbc:mariadb:")) {
            return "org.mariadb.jdbc.Driver";
        } else if (jdbcUrl.startsWith("jdbc:oracle:") //
                   || jdbcUrl.startsWith("JDBC:oracle:")) {
            return "oracle.jdbc.OracleDriver";
        } else if (jdbcUrl.startsWith("jdbc:microsoft:")) {
            return "com.microsoft.jdbc.sqlserver.SQLServerDriver";
        } else if (jdbcUrl.startsWith("jdbc:sqlserver:")) {
            return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        } else if (jdbcUrl.startsWith("jdbc:postgresql:")) {
            return "org.postgresql.Driver";
        } else if (jdbcUrl.startsWith("jdbc:edb:")) {
            return "org.postgresql.Driver";
        } else if (jdbcUrl.startsWith("jdbc:hsqldb:")) {
            return "org.hsqldb.jdbcDriver";
        } else if (jdbcUrl.startsWith("jdbc:db2:")) {
            return "com.ibm.db2.jcc.DB2Driver";
        } else if (jdbcUrl.startsWith("jdbc:sqlite:")) {
            return "org.sqlite.JDBC";
        } else if (jdbcUrl.startsWith("jdbc:ingres:")) {
            return "com.ingres.jdbc.IngresDriver";
        } else if (jdbcUrl.startsWith("jdbc:h2:")) {
            return "org.h2.Driver";
        } else if (jdbcUrl.startsWith("jdbc:hive:")) {
            return "org.apache.hive.jdbc.HiveDriver";
        } else if (jdbcUrl.startsWith("jdbc:hive2:")) {
            return "org.apache.hive.jdbc.HiveDriver";
        } else if (jdbcUrl.startsWith("jdbc:sybase:")) {
            return "com.sybase.jdbc4.jdbc.SybDriver";
        } else if (jdbcUrl.startsWith("jdbc:clickhouse:")) {
            return "ru.yandex.clickhouse.ClickHouseDriver";
        } else {
            throw new SQLException("unknown jdbc driver : " + jdbcUrl);
        }
    }

    /**
     * 根据驱动类名获取其数据库类型
     * @param jdbcUrl
     * @return
     */
    public static String getDbType(String jdbcUrl) {
        if (jdbcUrl == null) {
            return null;
        }

        if (jdbcUrl.startsWith("jdbc:derby:") || jdbcUrl.startsWith("jdbc:log4jdbc:derby:")) {
            return "derby";
        } else if (jdbcUrl.startsWith("jdbc:mysql:") || jdbcUrl.startsWith("jdbc:cobar:") || jdbcUrl.startsWith("jdbc:log4jdbc:mysql:")) {
            return "mysql";
        } else if (jdbcUrl.startsWith("jdbc:mariadb:")) {
            return "mariadb";
        } else if (jdbcUrl.startsWith("jdbc:oracle:") || jdbcUrl.startsWith("jdbc:log4jdbc:oracle:")) {
            return "oracle";
        } else if (jdbcUrl.startsWith("jdbc:microsoft:") || jdbcUrl.startsWith("jdbc:log4jdbc:microsoft:")) {
            return "sqlserver";
        } else if (jdbcUrl.startsWith("jdbc:sqlserver:") || jdbcUrl.startsWith("jdbc:log4jdbc:sqlserver:")) {
            return "sqlserver";
        } else if (jdbcUrl.startsWith("jdbc:postgresql:") || jdbcUrl.startsWith("jdbc:log4jdbc:postgresql:")) {
            return "postgresql";
        } else if (jdbcUrl.startsWith("jdbc:hsqldb:") || jdbcUrl.startsWith("jdbc:log4jdbc:hsqldb:")) {
            return "hsql";
        } else if (jdbcUrl.startsWith("jdbc:db2:")) {
            return "db2";
        } else if (jdbcUrl.startsWith("jdbc:sqlite:")) {
            return "sqlite";
        } else if (jdbcUrl.startsWith("jdbc:ingres:")) {
            return "ingres";
        } else if (jdbcUrl.startsWith("jdbc:h2:") || jdbcUrl.startsWith("jdbc:log4jdbc:h2:")) {
            return "h2";
        } else if (jdbcUrl.startsWith("jdbc:hive:")) {
            return "hive";
        } else if (jdbcUrl.startsWith("jdbc:hive2:")) {
            return "hive";
        } else if (jdbcUrl.startsWith("jdbc:sybase:")) {
            return "sybase";
        } else if (jdbcUrl.startsWith("jdbc:clickhouse:")) {
            return "clickhouse";
        } else {
            return null;
        }
    }

    /**
     * 创建JDBC驱动
     * @param driverClassName
     * @return
     * @throws SQLException
     */
    public static Driver createDriver(String driverClassName) throws SQLException {
        return createDriver(null, driverClassName);
    }

    /**
     * 创建JDBC驱动
     * @param classLoader
     * @param driverClassName
     * @return
     * @throws SQLException
     */
    public static Driver createDriver(ClassLoader classLoader, String driverClassName) throws SQLException {
        Class<?> clazz = null;
        if (classLoader != null) {
            try {
                clazz = classLoader.loadClass(driverClassName);
            } catch (ClassNotFoundException e) {
                // skip
            }
        }

        if (clazz == null) {
            try {
                ClassLoader contextLoader = Thread.currentThread().getContextClassLoader();
                if (contextLoader != null) {
                    clazz = contextLoader.loadClass(driverClassName);
                }
            } catch (ClassNotFoundException e) {
                // skip
            }
        }

        if (clazz == null) {
            try {
                clazz = Class.forName(driverClassName);
            } catch (ClassNotFoundException e) {
                throw new SQLException(e.getMessage(), e);
            }
        }

        try {
            return (Driver) clazz.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new SQLException(e.getMessage(), e);
        }
    }
	
}
