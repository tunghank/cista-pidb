package com.cista.pidb.md.dao;

import java.util.List;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.md.to.IfProjectCodeTo;

/**
 * Dao for table PIDB_ IF_PROJECT_CODE.
 * @author fumingjie
 *
 */
public class IfProjectCodeDao extends PIDBDaoSupport {

    /**
     * Insert a record to table PIDB_ IF_PROJECT_CODE.
     * @param ifProjCodeTo IfProjectCodeTo
     */
    public void insert(final IfProjectCodeTo ifProjCodeTo) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "insert into PIDB_IF_PROJECT_CODE(PROJ_CODE, SAP_STATUS, INFO_MESSAGE, TIME_STAMP, ID, ";
        sql += "RELEASED_BY,RELEASE_TO) values(?, ?, ?, ?, ?, ?, ?)";

        Object[] params = new Object[7];
        params[0] = ifProjCodeTo.getProjCode();
        params[1] = ifProjCodeTo.getSapStatus();
        params[2] = ifProjCodeTo.getInfoMessage();
        params[3] = ifProjCodeTo.getTimeStamp();
        params[4] = ifProjCodeTo.getId();
        params[5] = ifProjCodeTo.getReleasedBy();
        params[6] = ifProjCodeTo.getReleaseTo();

        sjt.update(sql, params);
    }
    
    public IfProjectCodeTo findByProjCode(final String projCode) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select * from PIDB_IF_PROJECT_CODE where PROJ_CODE = ? ";
        
        GenericRowMapper<IfProjectCodeTo> rm = new GenericRowMapper<IfProjectCodeTo>(IfProjectCodeTo.class);
        List<IfProjectCodeTo> result = sjt.query(sql, rm, new Object[]{projCode});
        if (result != null && result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
        
    }    
}
