package com.cista.pidb.code.dao;

import java.util.List;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.cista.pidb.code.to.SapMasterIcTypeTo;
import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;

public class SapMasterIcTypeDao extends PIDBDaoSupport {
    public List<SapMasterIcTypeTo> findAll() {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select * from WM_SAP_MASTER_IC_TYPE order by IC_TYPE";
        GenericRowMapper<SapMasterIcTypeTo> rm = new GenericRowMapper<SapMasterIcTypeTo>(
                SapMasterIcTypeTo.class);
        return sjt.query(sql, rm, new Object[]{});
    }
}
