package com.cista.pidb.code.dao;

import java.util.List;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.cista.pidb.code.to.SapMasterVendorTo;
import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;

public class SapMasterVendorDao extends PIDBDaoSupport {

    public List findAll() {
        SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
        String sql = "select * from WM_SAP_MASTER_VENDOR Order by VENDOR_CODE";
        logger.debug(sql);
        List<SapMasterVendorTo> result = stj
                .query(sql, new GenericRowMapper<SapMasterVendorTo>(
                        SapMasterVendorTo.class), new Object[] {});
        return result;
    }

    public List<SapMasterVendorTo> findVendorCode(String vendorCode) {
        SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
        String sql = "select distinct VENDOR_CODE from WM_SAP_MASTER_VENDOR where 1=1 ";
        if (vendorCode != null && vendorCode.length() > 0) {
            sql += " and "
                    + super
                            .getSmartSearchQueryString("VENDOR_CODE",
                                    vendorCode);
        }
        logger.debug(sql);
        List<SapMasterVendorTo> result = stj
                .query(sql, new GenericRowMapper<SapMasterVendorTo>(
                        SapMasterVendorTo.class), new Object[] {});
        return result;
    }

    public List<SapMasterVendorTo> queryByVendorCode(String vendorCode) {
        SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
        String sql = "select * from WM_SAP_MASTER_VENDOR where 1=1 ";
        if (vendorCode != null && vendorCode.length() > 0) {
            sql += " and VENDOR_CODE like "
                    + super.getLikeSQLString(vendorCode).toUpperCase();
                            //.getSmartSearchQueryString("VENDOR_CODE",
                                   // vendorCode);
        }
        sql += " Order by VENDOR_CODE ";
        logger.debug(sql);
        List<SapMasterVendorTo> result = stj
                .query(sql, new GenericRowMapper<SapMasterVendorTo>(
                        SapMasterVendorTo.class), new Object[] {});
        return result;
    }
    
    public List<SapMasterVendorTo> queryByShortName(String ShortName) {
        SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
        String sql = "select * from WM_SAP_MASTER_VENDOR where 1=1 ";
        if (ShortName != null && ShortName.length() > 0) {
            sql += " and SHORT_NAME like "
                    + super.getLikeSQLString(ShortName).toUpperCase();
            //System.out.println(sql);
                           // .getSmartSearchQueryString("SHORT_NAME",ShortName);
        }
        sql += " Order by VENDOR_CODE ";
        logger.debug(sql);
        List<SapMasterVendorTo> result = stj
                .query(sql, new GenericRowMapper<SapMasterVendorTo>(
                        SapMasterVendorTo.class), new Object[] {});
        return result;
    }
    
    public SapMasterVendorTo findByVendorCode(String vendorCode) {
        SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
        String sql = "select * from WM_SAP_MASTER_VENDOR where VENDOR_CODE = ? ";
        sql += " Order by VENDOR_CODE ";
        logger.debug(sql);
        List<SapMasterVendorTo> result = stj
                .query(sql, new GenericRowMapper<SapMasterVendorTo>(
                        SapMasterVendorTo.class), new Object[] {vendorCode});
        
        if (result != null && result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
    }    
    
    public SapMasterVendorTo findByShortName(String shortName) {
        SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
        String sql = "select * from WM_SAP_MASTER_VENDOR where SHORT_NAME = ? ";

        logger.debug(sql);
        List<SapMasterVendorTo> result = stj
                .query(sql, new GenericRowMapper<SapMasterVendorTo>(
                        SapMasterVendorTo.class), new Object[] {shortName});
        
        if (result != null && result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
    }
    
    public String codeToShortName(String codes) {
    	if (codes == null || codes.trim().length() == 0) {
    		return null;
    	}
    	
        SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
        String sql = "select * from WM_SAP_MASTER_VENDOR where 1 = 1";
        if (codes.indexOf(",") < 0) {
        	sql += " and VENDOR_CODE = " + getSQLString(codes);
        } else {
        	String[] codeList = codes.split(",");
        	sql += " and (";
        	for (int i = 0; i < codeList.length; i++) {
        		if (i != 0) {
        			sql += " or ";
        		}
        		sql += " VENDOR_CODE = " + getSQLString(codeList[i]);;
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
