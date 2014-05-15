package com.cista.pidb.md.dao;

import java.util.List;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.md.to.MaskLayerMappingQueryTo;
import com.cista.pidb.md.to.MaskLayerMappingTo;

public class MaskLayerMappingDao extends PIDBDaoSupport {

    public int countResult(MaskLayerMappingQueryTo queryTo) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select count(*) from PIDB_MASK_LAYER_MAPPING pmlm where 1 = 1";
        String whereCause = generateWhereCause(queryTo);
        sql += whereCause;
        logger.debug(sql);
        return sjt.queryForInt(sql, new Object[] {});
    }

    public List<MaskLayerMappingTo> query(final MaskLayerMappingQueryTo queryTo) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select "
                + "pmlm.MASK_NUM,"
                + "pmlm.MASK_LAYER"
                + " from PIDB_MASK_LAYER_MAPPING pmlm where 1 = 1 ";
        String whereCause = generateWhereCause(queryTo);
        sql += whereCause;
        sql += " order by pmlm.MASK_NUM";
        
        logger.debug(sql);
        
        GenericRowMapper<MaskLayerMappingTo> rm = new GenericRowMapper<MaskLayerMappingTo>(MaskLayerMappingTo.class);
        
        if (queryTo.getPageNo() > 0) {
            int cursorFrom = (queryTo.getPageNo() - 1) * queryTo.getPageSize() + 1;
            int cursorTo = (queryTo.getPageNo()) * queryTo.getPageSize();
            return sjt.query(getPagingSql(sql, cursorFrom, cursorTo), rm, new Object[]{});
        } else {
            return sjt.query(sql, rm, new Object[]{});  
        }        
    }

    private String generateWhereCause(final MaskLayerMappingQueryTo queryTo) {
        StringBuilder sb = new StringBuilder();
        
        if (queryTo.getMaskNum() != null
                && !queryTo.getMaskNum().equals("")) {
            String maskNum = getSmartSearchQueryString("pmlm.MASK_NUM",
                    queryTo.getMaskNum());
            if (maskNum != null) {
                sb.append(" and (" + maskNum + " )");
            }            
        }
        

        return sb.toString();
    }

    public List<MaskLayerMappingTo> findAll() {
        SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
        String sql = "select * from PIDB_MASK_LAYER_MAPPING";
        logger.debug(sql);
        List<MaskLayerMappingTo> result = stj.query(sql, new GenericRowMapper<MaskLayerMappingTo>(
                MaskLayerMappingTo.class), new Object[] {});
        return result;
    }
    
    public MaskLayerMappingTo findByMaskNum(String maskNum) {
        SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
        String sql = "select * from PIDB_MASK_LAYER_MAPPING where MASK_NUM=?";
        logger.debug(sql);
        List<MaskLayerMappingTo> result = stj.query(sql, new GenericRowMapper<MaskLayerMappingTo>(
                MaskLayerMappingTo.class), new Object[] {maskNum});
        
        if (result != null && result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }    
    }
    
}

