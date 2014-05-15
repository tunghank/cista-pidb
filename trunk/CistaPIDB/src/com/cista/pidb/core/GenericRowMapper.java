package com.cista.pidb.core;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

/**
 * Convert from ResultSet to POJO automatic.
 * Default type mapping table as following
 * --Java--   ----DB----
 * String     VARCHAR2
 * int        NUMBER(?, 0)
 * Integer    NUMBER(?, 0)
 * long       NUMBER(?, 0)
 * LONG       NUMBER(?, 0)
 * double     NUMBER(?, ?)
 * Double     NUMBER(?, ?)
 * boolean    VARCHAR(1) 'Y', 'N', 'T', 'F'
 * BOOLEAN    VARCHAR(1) 'Y', 'N', 'T', 'F'
 * boolean    NUMBER(1, 0) 0 or 1
 * BOOLEAN    NUMBER(1, 0) 0 or 1
 * @author Matrix
 *
 * @param <T>
 */

public class GenericRowMapper<T> implements ParameterizedRowMapper<T> {
    /** Logger. */
    protected final Log logger = LogFactory.getLog(getClass());
    private NameConverter nameConverter;
    private Class<T> type;

    /**
     * The default constructor.
     * @param _type The result type.
     */
    public GenericRowMapper(final Class<T> _type) {
        this.type = _type;
        this.nameConverter = new DefaultNameConverter();
    }

    /**
     * Convert current row in ResultSet to an object.
     * @param rs SQL ResultSet
     * @param rowNum current result number
     * @throws SQLException SQLException
     * @return The object created.
     */
    public T mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        try {
            T obj = type.newInstance();
            ResultSetMetaData rsmd = rs.getMetaData();
            for (int i = 0; i < rsmd.getColumnCount(); i++) {
                String dbColumnName = rsmd.getColumnName(i + 1).toUpperCase();
                String fieldName = nameConverter.db2java(dbColumnName);
                Field f = null;
                try {
                    f = type.getDeclaredField(fieldName);
                } catch (SecurityException e) {
                    logger.warn("Get field " + fieldName + " from " + type + " fail. " + e.getMessage());
                    continue;
                } catch (NoSuchFieldException e) {
                    logger.warn("Get field " + fieldName + " from " + type + " fail. " + e.getMessage());
                    continue;
                }
                Class fieldType = f.getType();
                String setMethodName = BeanHelper.generateSetMethodNameFromField(fieldName);
                //logger.debug("Class=" + type.getName() + ", Field=" + fieldName + ", DBColumn=" + dbColumnName);
                Object value = null;
                try {
                    if (fieldType == int.class || fieldType == Integer.class) {
                        value = rs.getInt(dbColumnName);
                    } else if (fieldType == String.class) {
                        value = rs.getString(dbColumnName);
                    } else if (fieldType == Date.class) {
                        java.sql.Timestamp dbValue = rs.getTimestamp(dbColumnName);
                        if (dbValue != null) {
                            value = new Date(dbValue.getTime());
                        }
                    } else if (fieldType == double.class
                            || fieldType == Double.class) {
                        value = rs.getDouble(dbColumnName);
                    } else if (fieldType == long.class
                            || fieldType == Long.class) {
                        value = rs.getLong(dbColumnName);
                    } else if (fieldType == boolean.class) {
                        String booleanValue = rs.getString(dbColumnName);
                        if (booleanValue == null) {
                            value = false;
                        } else {
                            booleanValue = booleanValue.toLowerCase().trim();
                            if (booleanValue.equals("1")
                                    || booleanValue.equals("Y")
                                    || booleanValue.equals("T")) {
                                value = true;
                            } else {
                                value = false;
                            }
                        }
                    } else if (fieldType == Boolean.class) {
                    	String boolValue = rs.getString(dbColumnName);
                        if (boolValue == null) {
                            value = null;
                        } else {
                        	boolValue = boolValue.toLowerCase().trim();
                            if (boolValue.equals("1")
                                    || boolValue.equals("Y")
                                    || boolValue.equals("T")) {
                                value = true;
                            } else {
                                value = false;
                            }
                        }
                    }
                } catch (SQLException se) {
                    logger.warn(dbColumnName + ": " + se.getMessage());
                    continue;
                }
                Method m = null;
                try {
                    m = type.getDeclaredMethod(setMethodName, new Class[] {fieldType});
                } catch (SecurityException e) {
                    logger.warn("Get SET method " + setMethodName + " from " + type + " fail. " + e.getMessage());
                    continue;
                } catch (NoSuchMethodException e) {
                    logger.warn("Get SET method " + setMethodName + " from " + type + " fail. " + e.getMessage());
                    continue;
                }

                if (value != null && m != null) {
                    m.invoke(obj, value);
                }
            }
            return obj;
        } catch (InvocationTargetException e) {
            logger.error("ResultSet to object convert fail.", e);
        } catch (InstantiationException e) {
            logger.error("ResultSet to object convert fail.", e);
        } catch (IllegalAccessException e) {
            logger.error("ResultSet to object convert fail.", e);
        }
        return null;
    }

}
