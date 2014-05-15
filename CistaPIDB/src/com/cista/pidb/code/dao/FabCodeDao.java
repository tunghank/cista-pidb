package com.cista.pidb.code.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.cista.pidb.code.to.FabCodeTo;
import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;

public class FabCodeDao extends PIDBDaoSupport {
	public List<FabCodeTo> findAll() {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from T_FAB_CODE order by FAB";
		GenericRowMapper<FabCodeTo> rm = new GenericRowMapper<FabCodeTo>(
				FabCodeTo.class);
		return sjt.query(sql, rm, new Object[] {});
	}

	public FabCodeTo getByFab(final String fab) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from T_FAB_CODE where fab = ?";
		GenericRowMapper<FabCodeTo> rm = new GenericRowMapper<FabCodeTo>(
				FabCodeTo.class);
		List<FabCodeTo> fabList = sjt.query(sql, rm, new Object[] { fab });
		if (fabList != null && fabList.size() > 0) {
			return fabList.get(0);
		} else {
			return null;
		}
	}

	public String getFabDescr(final String fab) {
		if (fab != null && fab.length() > 0) {
			SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
			String sql = "select distinct FAB_DESCR from T_FAB_CODE where fab=?";
			List<Map<String, Object>> result = sjt.queryForList(sql,
					new Object[] {fab});
			List<String> fabList = new ArrayList<String>();
			if (result != null && result.size() > 0) {
				for (Map<String, Object> item : result) {
					fabList.add((String) item.get("FAB_DESCR"));
				}
				return (String) fabList.get(0);
			}
		}
		return null;
	}
	
    public String findWFVendorCodeByPartNum(String partNum) {
        SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
        String sql = "SELECT Distinct VENDOR_CODE FROM T_Fab_code where Fab=( "
        			+ " SELECT Distinct Fab FROM PIDB_Project where Proj_name=( "
        			+ " SELECT Distinct substr(proj_code,1,6) FROM PIDB_MP_LIST where MAT_WF=? "
        			+ " ))";
        logger.debug(sql);
        List<Map<String, Object>> result = stj.queryForList(sql,
                new Object[] { partNum });
        String vendorCode = "";
        if (result != null && result.size() > 0) {
            Map<String, Object> item = result.get(0);
            vendorCode = (String) item.get("VENDOR_CODE");
        }
        return vendorCode;
    }
    
    public String findICWFVendorCodeByPartNum(String partNum) {
        SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
        String sql = "SELECT Distinct VENDOR_CODE FROM T_Fab_code where Fab=( "
        			+ " SELECT Distinct Fab FROM PIDB_Project where Proj_name=( "
        			+ " SELECT Distinct substr(proj_code,1,6) from PIDB_IC_WAFER where MATERIAL_NUM=? "
        			+ " ))";
        logger.debug(sql);
        List<Map<String, Object>> result = stj.queryForList(sql,
                new Object[] { partNum });
        String vendorCode = "";
        if (result != null && result.size() > 0) {
            Map<String, Object> item = result.get(0);
            vendorCode = (String) item.get("VENDOR_CODE");
        }
        return vendorCode;
    }
}
