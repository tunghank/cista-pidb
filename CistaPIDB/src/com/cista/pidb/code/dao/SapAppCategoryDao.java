package com.cista.pidb.code.dao;

import java.util.List;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.cista.pidb.code.to.SapAppCategoryTo;
import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;

public class SapAppCategoryDao extends PIDBDaoSupport {

     public List findAll() {
            SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
            String sql = "select * from WM_SAP_MASTER_APP_CATEGORY";
            logger.debug(sql);
            List<SapAppCategoryTo> result = stj.query(sql, new GenericRowMapper<SapAppCategoryTo>(
                    SapAppCategoryTo.class), new Object[] {});
                return result;
        }
}
