package com.cista.pidb.admin.dao;

import java.util.List;

import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.transaction.TransactionStatus;

import com.cista.pidb.admin.to.FunctionTo;
import com.cista.pidb.admin.to.RoleTo;
import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.core.SequenceSupport;

/**
 * Data access object for table PIDB_ROLE.
 * @author Administrator
 *
 */
public class RoleDao extends PIDBDaoSupport {
    /**
     * Find all roles in the table.
     * @return List of RoleTo
     */
    public List<RoleTo> findAll() {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select * from PIDB_ROLE order by ROLE_NAME";
        logger.debug(sql);
        return sjt.query(sql, new GenericRowMapper<RoleTo>(RoleTo.class), new Object[] {});
    }

    /**
     * Find role by given id.
     * @param id is the value of Role ID
     * @return RoleTo
     */
    public RoleTo find(final int id) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select * from PIDB_ROLE where ID= " + id + " ";
        logger.debug(sql);
        return sjt.queryForObject(sql, new GenericRowMapper<RoleTo>(RoleTo.class), new Object[]{});
    }

    /**
     * delete role by given id.
     * @param id is the value of Role ID
     */
    public void delete(final int id) {
        TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
            public void doInTransactionWithoutResult(final TransactionStatus status) {
                SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
                String sql = "delete from PIDB_USER_ROLE where ROLE_ID = ?";
                logger.debug(sql);
                sjt.update(sql, id);
                sql = "delete from PIDB_ROLE_FUNC where ROLE_ID = ?";
                logger.debug(sql);
                sjt.update(sql, id);
                sql = "delete from PIDB_ROLE where ID = ?";
                logger.debug(sql);
                sjt.update(sql, id);
            }
        };
        doInTransaction(callback);
    }

    /**
     * Create a new role.
     * @param newInstance is the value of RoleTo
     */
    public void insert(final RoleTo newInstance) {
        if (newInstance == null) {
            logger.error("Error the parameter is null.");
        }

        TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
            public void doInTransactionWithoutResult(final TransactionStatus status) {
                SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
                String sql = "insert into PIDB_ROLE values(?, ?)";
                int newRoleId = SequenceSupport.nextValue(SequenceSupport.SEQ_ROLE);
                logger.debug(sql);
                sjt.update(sql, newRoleId, newInstance.getRoleName());
                if (newInstance.getFunctions() != null && newInstance.getFunctions().size() > 0) {
                    for (FunctionTo f : newInstance.getFunctions()) {
                        sql = "insert into PIDB_ROLE_FUNC values (?, ?, ?)";
                        logger.debug(sql);
                        sjt.update(sql, SequenceSupport
                                .nextValue(SequenceSupport.SEQ_ROLE_FUNC), f.getId(), newRoleId);
                    }
                }
            }
        };
        doInTransaction(callback);
    }

    /**
     * Update role information.
     * @param newInstance is the value of RoleTo
     */
    public void update(final RoleTo newInstance) {
        if (newInstance == null) {
            logger.error("Error the parameter is null.");
        }

        TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
            public void doInTransactionWithoutResult(final TransactionStatus status) {
                SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
                String sql = "update PIDB_ROLE set ROLE_NAME=? where ID=?";
                logger.debug(sql);
                sjt.update(sql, newInstance.getRoleName(), newInstance.getId());

                sql = "delete from PIDB_ROLE_FUNC where ROLE_ID=?";
                logger.debug(sql);
                sjt.update(sql, newInstance.getId());

                if (newInstance.getFunctions() != null && newInstance.getFunctions().size() > 0) {
                    for (FunctionTo f : newInstance.getFunctions()) {
                        sql = "insert into PIDB_ROLE_FUNC values (?, ?, ?)";
                        logger.debug(sql);
                        sjt.update(sql, SequenceSupport
                                .nextValue(SequenceSupport.SEQ_ROLE_FUNC), f.getId(), newInstance.getId());
                    }
                }
            }
        };
        doInTransaction(callback);
    }

    public List<RoleTo> findRoleByUserId(final int userId) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select PIDB_ROLE.* from PIDB_ROLE, PIDB_USER_ROLE where PIDB_USER_ROLE.USER_ID=? and PIDB_USER_ROLE.ROLE_ID=PIDB_ROLE.ID";
        GenericRowMapper<RoleTo> rm = new GenericRowMapper<RoleTo>(RoleTo.class);
        return sjt.query(sql, rm, new Object[]{userId});
    }

    public RoleTo find(String roleName) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select * from PIDB_ROLE where ROLE_NAME= ? ";
        logger.debug(sql);

        List<RoleTo> result = sjt.query(sql, new GenericRowMapper<RoleTo>(RoleTo.class), new Object[]{roleName});
        if (result != null && result.size() > 0) {
            return result.get(0);
        }
        return null;

    }
}
