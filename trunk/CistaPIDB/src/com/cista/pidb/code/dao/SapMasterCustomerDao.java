package com.cista.pidb.code.dao;

import java.util.List;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.cista.pidb.code.to.SapMasterCustomerTo;
import com.cista.pidb.code.to.SapMasterVendorTo;
import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;

public class SapMasterCustomerDao extends PIDBDaoSupport {
	

    public List<SapMasterCustomerTo> findAll() {
        SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
        String sql = "select distinct * from WM_SAP_MASTER_CUSTOMER where "
        				+" CUSTOMER_CODE=CUSTOMER_GRP order by CUSTOMER_CODE";
        logger.debug(sql);
        List<SapMasterCustomerTo> result = stj.query(sql,
                new GenericRowMapper<SapMasterCustomerTo>(
                        SapMasterCustomerTo.class), new Object[] {});
        return result;
    }

    public List<SapMasterCustomerTo> findVendorCode(String customerCode) {
        SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
        String sql = "select distinct * from WM_SAP_MASTER_CUSTOMER where 1=1 ";
        if (customerCode != null && customerCode.length() > 0) {
            sql += " and "
                    + super.getSmartSearchQueryString("CUSTOMER_CODE",
                            customerCode);
        }
        logger.debug(sql);
        List<SapMasterCustomerTo> result = stj.query(sql,
                new GenericRowMapper<SapMasterCustomerTo>(
                        SapMasterCustomerTo.class), new Object[] {});
        return result;
    }

    public SapMasterCustomerTo findByVendorCode(String customerCode) {
        SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
        String sql = "select distinct * from WM_SAP_MASTER_CUSTOMER where CUSTOMER_CODE=?";
        logger.debug(sql);
        List<SapMasterCustomerTo> result = stj.query(sql,
                new GenericRowMapper<SapMasterCustomerTo>(
                        SapMasterCustomerTo.class),
                new Object[] { customerCode });

        if (result != null && result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
    }
    
    public SapMasterCustomerTo findByShortName(String shortName) {
        SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
        String sql = "select distinct * from WM_SAP_MASTER_CUSTOMER where SHORT_NAME=?";
        logger.debug(sql);
        List<SapMasterCustomerTo> result = stj.query(sql,
                new GenericRowMapper<SapMasterCustomerTo>(
                        SapMasterCustomerTo.class),
                new Object[] { shortName });

        if (result != null && result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
    } 
    /*
     * Add by 900it From 2008/04/10
     * by like search for short name
     * */
    
    public List<SapMasterCustomerTo> findLikeShortName(String shortName) {
        SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
        String sql = "select distinct * from WM_SAP_MASTER_CUSTOMER where CUSTOMER_CODE=CUSTOMER_GRP ";
        if (shortName != null && shortName.length() > 0) {
            sql += " and SHORT_NAME like "
                    + super.getLikeSQLString(shortName.toUpperCase());
        }
        sql += " order by CUSTOMER_CODE ";
        logger.debug(sql);
        List<SapMasterCustomerTo> result = stj.query(sql,
                new GenericRowMapper<SapMasterCustomerTo>(
                        SapMasterCustomerTo.class), new Object[] {});
        return result;
    }
    
    public String codeToCustShortName(String codes) {
    	if (codes == null || codes.trim().length() == 0) {
    		return null;
    	}
    	
        SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
        String sql = "select * from WM_SAP_MASTER_CUSTOMER where 1 = 1";
        if (codes.indexOf(",") < 0) {
        	sql += " and CUSTOMER_CODE = " + getSQLString(codes);
        } else {
        	String[] codeList = codes.split(",");
        	sql += " and (";
        	for (int i = 0; i < codeList.length; i++) {
        		if (i != 0) {
        			sql += " or ";
        		}
        		sql += " CUSTOMER_CODE = " + getSQLString(codeList[i]);;
        	}
        	sql += ")";
        }
        
        logger.debug(sql);
        
        List<SapMasterVendorTo> result = stj
                .query(sql, new GenericRowMapper<SapMasterVendorTo>(
                        SapMasterVendorTo.class), new Object[] {});
        String shortNames = "";
    	for (SapMasterVendorTo vt : result) {
    		shortNames += "," + vt.getShortName();
    	}
    	
    	if (shortNames.length() > 0) {
    		shortNames = shortNames.substring(1);
    	}
    	logger.debug("shortNames == " + shortNames);
    	
    	return shortNames;
    }
}
