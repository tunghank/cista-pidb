package com.cista.pidb.admin.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.cista.pidb.admin.to.FunctionTo;
import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.core.SequenceSupport;

public class FunctionDao extends PIDBDaoSupport {
    public List<FunctionTo> findAll() {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select * from PIDB_FUNCTION order by POSITION";
        logger.debug(sql);
        return sjt.query(sql, new GenericRowMapper<FunctionTo>(FunctionTo.class), new Object[] {});
    }

    /**
     * Find role by given id.
     * @param id is the value of Function ID
     * @return FunctionTo
     */
    public FunctionTo find(final int id) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select * from PIDB_FUNCTION where ID = " + id + " ";
        logger.debug(sql);
        return sjt.queryForObject(sql, new GenericRowMapper<FunctionTo>(FunctionTo.class), new Object[]{});
    }


    public List<FunctionTo> findFuncByRoleId(final int roleId) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select PIDB_FUNCTION.* from PIDB_FUNCTION, PIDB_ROLE_FUNC where PIDB_ROLE_FUNC.ROLE_ID=? and PIDB_FUNCTION.ID=PIDB_ROLE_FUNC.FUNC_ID";
        GenericRowMapper<FunctionTo> rm = new GenericRowMapper<FunctionTo>(FunctionTo.class);
        return sjt.query(sql, rm, new Object[]{roleId});
    }
    
    public void delete(final int id) {
        TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
            public void doInTransactionWithoutResult(
                    final TransactionStatus status) {
                SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
                String sql = "delete from PIDB_ROLE_FUNC where FUNC_ID = ?";
                logger.debug(sql);
                sjt.update(sql, id);
                sql = "delete from PIDB_FUNCTION where ID = ?";
                logger.debug(sql);
                sjt.update(sql, id);
            }
        };
        doInTransaction(callback);
    }    
    
    /**
     * Update function information.
     * 
     * @param newInstance
     *            is the value of FunctionTo
     */
    public void update(final FunctionTo newInstance) {
        if (newInstance == null) {
            logger.error("Error the parameter is null.");
        }

        TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
            public void doInTransactionWithoutResult(
                    final TransactionStatus status) {
                Map m = new HashMap();
                m.put("ID", newInstance.getId());
                update(newInstance, "PIDB_FUNCTION", m);
            }
        };
        doInTransaction(callback);
    }    

    /**
     * Create a new function.
     * 
     * @param newInstance
     *            is the value of FunctionTo
     */
    public void insert(final FunctionTo newInstance) {
        if (newInstance == null) {
            logger.error("Error the parameter is null.");
        }

        int newRoleId = SequenceSupport
                .nextValue(SequenceSupport.SEQ_FUNCION);
        newInstance.setId(newRoleId);
        insert(newInstance, "PIDB_FUNCTION");
    }

    public FunctionTo find(String funcName) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select * from PIDB_FUNCTION where FUNC_NAME= ? ";
        logger.debug(sql);

        List<FunctionTo> result = sjt.query(sql, new GenericRowMapper<FunctionTo>(FunctionTo.class), new Object[]{funcName});
        if (result != null && result.size() > 0) {
            return result.get(0);
        }
        return null;

    }        
    
    public List<FunctionTo> findMenu() {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select * from PIDB_FUNCTION where IS_MENU='1' order by POSITION";
        logger.debug(sql);
        
        return sjt.query(sql, new GenericRowMapper<FunctionTo>(FunctionTo.class));

    }            
    
    public List<FunctionTo> findMenuByPrefix(String prefix, Integer userId) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select lf.* from PIDB_FUNCTION lf where lf.ID in (select distinct(PIDB_FUNCTION.ID) from PIDB_USER,PIDB_USER_ROLE,PIDB_ROLE,PIDB_ROLE_FUNC,PIDB_FUNCTION where PIDB_USER.ID=PIDB_USER_ROLE.USER_ID and PIDB_USER_ROLE.ROLE_ID=PIDB_ROLE.ID and PIDB_ROLE.ID=PIDB_ROLE_FUNC.ROLE_ID and PIDB_ROLE_FUNC.FUNC_ID=PIDB_FUNCTION.ID and PIDB_FUNCTION.IS_MENU='1' and PIDB_FUNCTION.FUNC_NAME like '" +
            prefix + "%') order by lf.POSITION";
        logger.debug(sql);
        
        return sjt.query(sql, new GenericRowMapper<FunctionTo>(FunctionTo.class), new Object[]{});

    }                
    
    public List<String> findTopMenu(Integer userId) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select distinct(substr(PIDB_FUNCTION.FUNC_NAME,0,INSTR(PIDB_FUNCTION.FUNC_NAME,'/',1,1)-1)) as ROOT,PIDB_FUNCTION.POSITION from PIDB_USER,PIDB_USER_ROLE,PIDB_ROLE,PIDB_ROLE_FUNC,PIDB_FUNCTION where PIDB_USER.ID=PIDB_USER_ROLE.USER_ID and PIDB_USER_ROLE.ROLE_ID=PIDB_ROLE.ID and PIDB_ROLE.ID=PIDB_ROLE_FUNC.ROLE_ID and PIDB_ROLE_FUNC.FUNC_ID=PIDB_FUNCTION.ID and PIDB_USER.ID=? and PIDB_FUNCTION.IS_MENU='1' order by PIDB_FUNCTION.POSITION";
        logger.debug(sql);
        
        List<Map<String, Object>> lm = sjt.queryForList(sql, new Object[]{userId});
        List ls = new ArrayList();
        
        for (Map<String, Object> m : lm) {
            String v = (String) m.get("ROOT");
            if (!ls.contains(v)) {
                ls.add(v);
            }
        }
        
        return ls;

    }                    
}
