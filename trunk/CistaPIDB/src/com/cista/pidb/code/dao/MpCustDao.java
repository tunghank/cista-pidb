package com.cista.pidb.code.dao;

import java.util.List;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.cista.pidb.code.to.MpCustTo;
import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;

public class MpCustDao extends PIDBDaoSupport {
    public List<MpCustTo> findAll() {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select * from PIDB_MP_CUST order by CUST_CODE";
        GenericRowMapper<MpCustTo> rm = new GenericRowMapper<MpCustTo>(
                MpCustTo.class);
        return sjt.query(sql, rm, new Object[] {});
    }
}
