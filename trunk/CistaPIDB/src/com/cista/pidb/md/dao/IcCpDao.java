package com.cista.pidb.md.dao;

import java.util.List;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.md.to.IcCpTo;

public class IcCpDao extends PIDBDaoSupport {


    
    public List<IcCpTo> findByProjWver(String projWver) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        
        String sql = " Select A.CP_MATERIAL_NUM MATERIAL_NUM " +
        			" From PIDB_CP_MATERIAL A " +
        			" Where A.PROJECT_CODE_W_VERSION = ? ";
        logger.debug(sql);
        GenericRowMapper<IcCpTo> rm = new GenericRowMapper<IcCpTo>(IcCpTo.class);
        
        return sjt.query(sql, rm, new Object[] { projWver });
        
    }    

    public List<IcCpTo> findByProjCode(String projCode) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        
        String sql = " Select A.CP_MATERIAL_NUM MATERIAL_NUM " +
        			" From PIDB_CP_MATERIAL A " +
        			" Where A.PROJECT_CODE_W_VERSION IN " +
        			"( SELECT B.PROJ_CODE_W_VERSION " +
        			"	FROM PIDB_CP_TEST_PROGRAM B " +
        			"  WHERE B.PROJ_CODE = ? ) Order by A.CP_MATERIAL_NUM ";
  		
        logger.debug(sql);
        GenericRowMapper<IcCpTo> rm = new GenericRowMapper<IcCpTo>(IcCpTo.class);
        
        return sjt.query(sql, rm, new Object[] { projCode });
        
    }    
}
