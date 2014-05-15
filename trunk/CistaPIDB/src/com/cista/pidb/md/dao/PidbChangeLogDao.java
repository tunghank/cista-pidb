package com.cista.pidb.md.dao;

import java.util.UUID;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.cista.pidb.core.PIDBDaoSupport;

public class PidbChangeLogDao extends PIDBDaoSupport{

   
    public void insertChange(final String functionName, final String sqlEvent, 
    		final String sqlObject, final String createdBy)  {
      
        TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(
                    final TransactionStatus status) {
                
                SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();

                String sql = "Insert into PIDB_CHANGE_LOG " +
                	"( LOG_UID, FUNCTION_NAME, SQL_EVENT, SQL_OBJECT, " +
                	" CREATED_BY )"
                + " values(?,?,?,?,? )";
                logger.debug(sql);
                
                sjt.update(sql, UUID.randomUUID().toString().toUpperCase(),
                		functionName,
                		sqlEvent,
                		sqlObject,
                		createdBy
                );
                logger.debug(sjt.toString());
            }
        };
        doInTransaction(callback);
    }
    
 
}
