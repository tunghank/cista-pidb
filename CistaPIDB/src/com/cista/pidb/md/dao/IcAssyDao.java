package com.cista.pidb.md.dao;

import java.util.List;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.md.to.IcAssyTo;

public class IcAssyDao extends PIDBDaoSupport {


    
    public List<IcAssyTo> findByPartNum(String partNum) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();

        String sql = " Select 'A'||substr(A.MATERIAL_NUM,2,15) MATERIAL_NUM " +
        			" From PIDB_IC_FG A " +
        			" Where PART_NUM = ? ";
        logger.debug(sql);
        GenericRowMapper<IcAssyTo> rm = new GenericRowMapper<IcAssyTo>(IcAssyTo.class);
        
        return sjt.query(sql, rm, new Object[] { partNum });
        
    }    

}
