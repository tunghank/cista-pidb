package com.cista.pidb.md.dao;

import java.util.List;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.md.to.IcWaferTo;
import com.cista.pidb.md.to.RptMasksetQueryTo;

public class RptMasksetDao extends PIDBDaoSupport
{
	
	public List<IcWaferTo> queryMasksetLayer(RptMasksetQueryTo queryTo) 
	{
		String sql="";
		StringBuffer sbSql;
		
		sbSql = new StringBuffer();
		sbSql.append("SELECT material_num, variant, proj_code, body_ver, option_ver, ");
		sbSql.append("       proj_code_w_version, routing_wf, routing_bp, routing_cp, "); 
		sbSql.append("       mp_status, remark, fab_device_id, mask_layer_com, ");
		sbSql.append("       tape_out_date, mask_id, revision_item, assign_to, ");
		sbSql.append("       assign_email, status, created_by, modified_by, cp, bp, ");
		sbSql.append("       ds, material_desc, mask_house, mask_num, routing_polish, "); 
		sbSql.append("       routing_color_filter, routing_wafer_cf, routing_csp ");  
		sbSql.append(" FROM PIDB_IC_WAFER  ");
		sbSql.append(" WHERE proj_code IN (SELECT proj_code ");
		sbSql.append("                       FROM PIDB_PROJECT_CODE ");
		sbSql.append("                      WHERE proj_name = ?) ");
		sbSql.append("       AND tape_out_date is not null ");
		sbSql.append(" ORDER BY proj_code_w_version ");
		sql = sbSql.toString();
		
		logger.debug(sql);
		
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		GenericRowMapper<IcWaferTo> rm = new GenericRowMapper<IcWaferTo>(IcWaferTo.class);
		List<IcWaferTo> result = sjt.query(sql, rm, new Object[] {queryTo.getProjName() });
		
		return result;
	}
	
	
	public  boolean isParameterNull(String inputParameter)
    {
        int zeroLength = 0;
        boolean isParameterNull = (inputParameter == null ||
                                   inputParameter.length() == zeroLength);
        return isParameterNull;
    }
}
