package com.cista.pidb.md.dao;

import java.util.List;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.md.to.IcBumpTo;

public class IcBumpDao extends PIDBDaoSupport {


    
    public List<IcBumpTo> findByProjWver(String projWver) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        
        String sql = " Select 'B'||substr(A.MATERIAL_NUM,2,13) MATERIAL_NUM " +
        			" From PIDB_IC_WAFER A " +
        			" Where A.PROJ_CODE_W_VERSION = ? ";
        logger.debug(sql);
        GenericRowMapper<IcBumpTo> rm = new GenericRowMapper<IcBumpTo>(IcBumpTo.class);
        
        return sjt.query(sql, rm, new Object[] { projWver });
        
    }    

}
