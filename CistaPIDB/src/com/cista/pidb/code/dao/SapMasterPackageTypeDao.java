package com.cista.pidb.code.dao;

import java.util.List;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.cista.pidb.code.to.SapMasterPackageTypeTo;
import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;

public class SapMasterPackageTypeDao extends PIDBDaoSupport {

    public List findAll() {
           SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
           String sql = "select * from WM_SAP_MASTER_PACKAGE_TYPE";
           logger.debug(sql);
           List<SapMasterPackageTypeTo> result = stj.query(sql, new GenericRowMapper<SapMasterPackageTypeTo>(
                  SapMasterPackageTypeTo.class), new Object[] {});
               return result;
    }
    
    public SapMasterPackageTypeTo findByPkgType(String pkgType) {
        SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
        String sql = "select * from WM_SAP_MASTER_PACKAGE_TYPE where PACKAGE_TYPE = ?";
        logger.debug(sql);
        List<SapMasterPackageTypeTo> result = stj.query(sql, new GenericRowMapper<SapMasterPackageTypeTo>(
               SapMasterPackageTypeTo.class), new Object[] {pkgType});
        
        if (result != null && result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
    }    
 
    
    public SapMasterPackageTypeTo findByDesc(String desc) {
        SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
        String sql = "select * from WM_SAP_MASTER_PACKAGE_TYPE where DESCRIPTION = ?";
        logger.debug(sql);
        List<SapMasterPackageTypeTo> result = stj.query(sql, new GenericRowMapper<SapMasterPackageTypeTo>(
               SapMasterPackageTypeTo.class), new Object[] {desc});
        
        if (result != null && result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
    }        
}
