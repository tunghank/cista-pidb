package com.cista.pidb.core.dao;

/********************************************************************
 * Modify History:
 * Date Time  |  Modifier  |Description
 * 2007/09/09 |  Hank_Tang |加入可以讀取多個AD Function.   
 ********************************************************************/

import java.util.List;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.core.to.LDAPConfigTo;

public class LDAPConfigDao extends PIDBDaoSupport {

    public List getAll() {
        SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
        String sql = " Select ldap_site, ldap_url, ldap_factory, ldap_cn, " +
        			" ldap_authentication, ldap_username, ldap_password " +
        			" From T_LDAP_CONFIG ";
        logger.debug(sql);
        List<LDAPConfigTo> result = stj
                .query(sql, new GenericRowMapper<LDAPConfigTo>(
                		LDAPConfigTo.class), new Object[] {});
        return result;
    }


}
