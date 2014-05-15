package com.cista.pidb.core;

/**
 * Java field name to db column name converter.
 * @author Matrix
 *
 */
public interface NameConverter {
    /**
     * Convert a java field name to db field name.
     * As lastLoginDate --> LAST_LOGIN_DATE
     * @param javaName a java field name.
     * @return db name.
     */
    String java2db(String javaName);

    /**
     * Convert a db field name to java field name.
     * As LAST_LOGIN_DATE --> lastLoginDate
     * @param dbName a db field name.
     * @return java field name.
     */
    String db2java(String dbName);
}
