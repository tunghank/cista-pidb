package com.cista.pidb.core;


/**
 * The support dao class for LHP DB.
 * All the dao sub-class should extend from LHPDaoSupport.
 * @author Matrix
 *
 */
public class PIDBDaoSupport extends BaseDaoSupport {
    /**
     * Default constructor.
     *
     */
    public PIDBDaoSupport() {
        super();
        setDataSource(PIDBContext.getPIDBDataSource());
        setJdbcTemplate(PIDBContext.getPIDBJdbcTemplate());
        setTransactionTemplate(PIDBContext.getPIDBTransactionTemplate());
        
    }
}
