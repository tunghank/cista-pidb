package com.cista.pidb.admin.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.cista.pidb.admin.to.RoleTo;
import com.cista.pidb.admin.to.UserTo;
import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.core.SequenceSupport;

/**
 * UserDao.
 * @author Administrator
 *
 */
public class UserDao extends PIDBDaoSupport {

    /**
     * Find users by given user id, first name and last name.
     * @param userId is the value of USER_ID
     * @param firstName is the value of FIRST_NAME
     * @param lastName is the value of LAST_NAME
     * @return List fo UserTo
     */
    public List<UserTo> find(final String userId, final String firstName,
            final String lastName) {
        SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
        String sql = "select * from PIDB_USER";
        if (userId != null && !userId.trim().equals("")) {
            sql = sql + " where USER_ID = '" + userId.trim() + "'";
        } else {
            sql = sql + " where USER_ID like '%%'";
        }

        if (firstName != null && !firstName.trim().equals("")) {
            sql = sql + " and FIRST_NAME LIKE '" + firstName.trim() + "'";
        }
        if (lastName != null && !lastName.trim().equals("")) {
            sql = sql + " and LAST_NAME LIKE '" + lastName.trim() + "'";
        }
        logger.debug(sql);
        return stj.query(sql, new GenericRowMapper<UserTo>(UserTo.class),
                new Object[] {});
    }

    /**
     * Find users by given user id, first name and last name.
     * @param id is the value of ID
     * @return UserTo
     */
    public UserTo find(final int id) {
        SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
        String sql = "select * from PIDB_USER where ID = ? ";
        logger.debug(sql);
        List<UserTo> result = stj.query(sql, new GenericRowMapper<UserTo>(
                UserTo.class), new Object[] {id});
        if (result != null && result.size() > 0) {
            return result.get(0);
        } else {
            logger.warn("User with id = " + id + " is not found.");
            return null;
        }
    }

    /**
     * Find users by given user id.
     * @param userId is the value of USER_ID
     * @return UserTo
     */
    public UserTo find(final String userId) {
        SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
        String sql = "select * from PIDB_USER where lower(USER_ID) = ? ";
        logger.debug(sql);
        List<UserTo> result = stj.query(sql, new GenericRowMapper<UserTo>(
                UserTo.class), new Object[] {userId.toLowerCase()});
        if (result != null && result.size() > 0) {
            return result.get(0);
        } else {
            logger.warn("User with user id = " + userId + " is not found.");
            return null;
        }
    }

    /**
     * Delete user by given id.
     * @param id is the value of User ID
     */
    public void delete(final int id) {
        TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
            public void doInTransactionWithoutResult(
                    final TransactionStatus status) {
                SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
                String sql = "delete from PIDB_USER_ROLE where USER_ID=?";
                logger.debug(sql);
                sjt.update(sql, id);
                sql = "delete from PIDB_USER where ID=?";
                logger.debug(sql);
                sjt.update(sql, id);
            }
        };
        doInTransaction(callback);
    }

    /**
     * insert a user.
     * @param newInstance is an UserTo
     */
    public void insert(final UserTo newInstance) {
        if (newInstance == null) {
            logger.error("Error the parameter is null.");
        }
        TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(
                    final TransactionStatus status) {
                int newUserId = SequenceSupport
                        .nextValue(SequenceSupport.SEQ_USER);

                SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
                String sql = "insert into PIDB_USER values(?, ?, ?, ?, ?, ?)";
                logger.debug(sql);
                sjt.update(sql, newUserId, newInstance.getUserId(), newInstance
                        .getFirstName(), newInstance.getLastName(), newInstance
                        .getEmail(), newInstance.getActive());

                if (newInstance.getRoles() == null) {
                    logger.debug("The role list is empty.");
                } else {
                    for (int i = 0; i < newInstance.getRoles().size(); i++) {
                        int roleId = 0;
                        RoleTo role = newInstance.getRoles().get(i);
                        roleId = role.getId();
                        sql = "insert into PIDB_USER_ROLE(ID, USER_ID, ROLE_ID) values(?, ?, ?)";
                        logger.debug(sql);
                        sjt.update(sql, SequenceSupport
                                .nextValue(SequenceSupport.SEQ_USER_ROLE),
                                newUserId, roleId);

                    }
                }
            }
        };
        doInTransaction(callback);
    }

    /**
     * Update user’s information.
     *
     * @param newInstance
     *            is the value of UserTo
     */
    public void update(final UserTo newInstance) {
        if (newInstance == null) {
            logger.error("Error the parameter is null.");
        }
        TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(final TransactionStatus status) {
                SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
                String sql = "update PIDB_USER set USER_ID=?, FIRST_NAME=?, LAST_NAME=?, EMAIL=?, ACTIVE=? where ID=?";
                logger.debug(sql);
                sjt.update(sql, newInstance.getUserId(), newInstance.getFirstName(), newInstance.getLastName(),
                        newInstance.getEmail(), newInstance.getActive(), newInstance.getId());
                sql = "delete from PIDB_USER_ROLE where USER_ID=?";
                logger.debug(sql);
                sjt.update(sql, newInstance.getId());

                if (newInstance.getRoles() == null || newInstance.getRoles().isEmpty()) {
                    logger.error("The role list is empty.");
                } else {
                    for (int i = 0; i < newInstance.getRoles().size(); i++) {
                        int roleId = 0;
                        RoleTo role = newInstance.getRoles().get(i);
                        roleId = role.getId();
                        sql = "insert into PIDB_USER_ROLE values(?, ?, ?)";
                        logger.debug(sql);
                        sjt.update(sql, SequenceSupport
                                .nextValue(SequenceSupport.SEQ_USER_ROLE),
                                newInstance.getId(), roleId);

                    }
                }
            }
        };
        doInTransaction(callback);
    }

    public List<UserTo> find(final UserTo queryTo) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select * from PIDB_USER where 1=1";

        if (queryTo.getUserId() != null && !queryTo.getUserId().equals("")) {
            sql += " and USER_ID like " + getLikeSQLString(queryTo.getUserId());
        }

        if (queryTo.getFirstName() != null && !queryTo.getFirstName().equals("")) {
            sql += " and FIRST_NAME like " + getLikeSQLString(queryTo.getFirstName());
        }

        if (queryTo.getLastName() != null && !queryTo.getLastName().equals("")) {
            sql += " and LAST_NAME like " + getLikeSQLString(queryTo.getLastName());
        }

        GenericRowMapper rm = new GenericRowMapper<UserTo>(UserTo.class);
        return sjt.query(sql, rm, new Object[]{});
    }

    public List<String> findAuthenByUser(final Integer id) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select PIDB_FUNCTION.URI_PATTERN from PIDB_USER,PIDB_USER_ROLE,PIDB_ROLE,PIDB_ROLE_FUNC,PIDB_FUNCTION where PIDB_USER.ID=PIDB_USER_ROLE.USER_ID and PIDB_USER_ROLE.ROLE_ID=PIDB_ROLE.ID and PIDB_ROLE.ID=PIDB_ROLE_FUNC.ROLE_ID and PIDB_ROLE_FUNC.FUNC_ID=PIDB_FUNCTION.ID and PIDB_USER.ID=?";
        List<Map<String, Object>> lm = sjt.queryForList(sql, new Object[] { id });
        List ls = new ArrayList();
        
        for (Map<String, Object> m : lm) {
            ls.addAll(m.values());
        }
        
        return ls;
    }
    
    
//    public List<Integer> findMenuByUser(final Integer id) {
//        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
//        String sql = "select PIDB_FUNCTION.ID from PIDB_USER,PIDB_USER_ROLE,PIDB_ROLE,PIDB_ROLE_FUNC,PIDB_FUNCTION where PIDB_USER.ID=PIDB_USER_ROLE.USER_ID and PIDB_USER_ROLE.ROLE_ID=PIDB_ROLE.ID and PIDB_ROLE.ID=PIDB_ROLE_FUNC.ROLE_ID and PIDB_ROLE_FUNC.FUNC_ID=PIDB_FUNCTION.ID and PIDB_USER.ID=? and PIDB_FUNCTION.IS_MENU='1'";
//        List<Map<String, Object>> lm = sjt.queryForList(sql, new Object[] { id });
//        List<Integer> ls = new ArrayList<Integer>();
//        
//        for (Map<String, Object> m : lm) {
//            Collection coll = m.values();
//            for (Object o : coll) {
//                ls.add(((BigDecimal) o).intValue());
//                
//            }
//        }
//        
//        return ls;
//    }     

    public List<UserTo> findAll() {
        SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
        String sql = "select * from PIDB_USER order by USER_ID";
        logger.debug(sql);
        return stj.query(sql, new GenericRowMapper<UserTo>(UserTo.class),
                new Object[] {});
    }
    
    public List<UserTo> findByUserId(String userId) {
        SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
        String sql = "select * from PIDB_USER where USER_ID = ?";
        logger.debug(sql);
        return stj.query(sql, new GenericRowMapper<UserTo>(UserTo.class),
                new Object[] {userId});
    }    
    
    public List<UserTo> findByRoleName(String roleName) {
        SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
        String sql = "select pu.* from PIDB_USER pu, PIDB_ROLE pr, PIDB_USER_ROLE pur where pu.ID=pur.USER_ID and pr.ID=pur.ROLE_ID and pr.ROLE_NAME = ?";
        logger.debug(sql);

        return stj.query(sql, new GenericRowMapper<UserTo>(UserTo.class),
                new Object[] {roleName});
    }        
    
    public String fetchEmail (String assigns) {
        String emails = "";
        if (assigns != null && assigns.length() > 0) {
            
            List<String> emailList = new ArrayList<String>();
            List<UserTo> userList = new ArrayList<UserTo>();
            
            if (assigns.startsWith("(R)")) {
                String[] roles = (assigns.substring(3)).split(",");
                for (String r : roles) {
                    userList.addAll(findByRoleName(r));
                }
            } else {
                String[] users = assigns.split(",");
                for (String u : users) {
                    userList.addAll(findByUserId(u));
                }          
            }
            
            for (UserTo user : userList) {
                if (user != null && !emailList.contains(user.getEmail())) {
                    emailList.add(user.getEmail());
                } 
            }
            
            
            for (String s : emailList) {
                if (s != null && s.length() > 0) {
                    emails += "," + s;
                }
            }
            
            if (emails.length() > 0) {
                emails = emails.substring(1);
            }
            
        }
        return emails;
    }
    
    public String fetchEmail (List<String> assigns) {
        return fetchEmail((String[]) assigns.toArray(new String[0]));
    }
    
    public String fetchEmail (String[] assigns) {
        String emails = "";
        if (assigns != null && assigns.length > 0) {
            
            List<String> emailList = new ArrayList<String>();
            List<UserTo> userList = new ArrayList<UserTo>();
            
//            if (assigns.indexOf("(R)") >= 0) {
//                assigns = assigns.replaceAll("(R)", "");
//            }
            
            for (String ass : assigns) {
                if (ass.startsWith("(R)")) { 
                    userList.addAll(findByRoleName(ass.substring(3)));
                } else {
                    userList.addAll(findByUserId(ass));
                }
            }
            
            for (UserTo user : userList) {
                if (user != null && !emailList.contains(user.getEmail())) {
                    emailList.add(user.getEmail());
                } 
            }
            
            
            for (String s : emailList) {
                if (s != null && s.length() > 0) {
                    emails += "," + s;
                }
            }
            
            if (emails.length() > 0) {
                emails = emails.substring(1);
            }
            
        }
        return emails;
    }    
}
