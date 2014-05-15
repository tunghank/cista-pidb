package com.cista.pidb.code.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.cista.pidb.code.to.SapMasterProductFamilyTo;
import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;

public class SapMasterProductFamilyDao extends PIDBDaoSupport {
    public List<SapMasterProductFamilyTo> findAll() {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select * from WM_SAP_MASTER_PRODUCT_FAMILY order by PRODUCT_FAMILY";
        GenericRowMapper<SapMasterProductFamilyTo> rm = new GenericRowMapper<SapMasterProductFamilyTo>(
                SapMasterProductFamilyTo.class);
        return sjt.query(sql, rm, new Object[]{});
    }

    public List<String> findProdFamily() {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select distinct PRODUCT_FAMILY from WM_SAP_MASTER_PRODUCT_FAMILY order by PRODUCT_FAMILY";
        List<Map<String, Object>> result = sjt.queryForList(sql, new Object[]{});
        List<String> prodFamily = new ArrayList<String>();
        for (Map<String, Object> map : result) {
            prodFamily.add((String) map.get("PRODUCT_FAMILY"));
        }
        return prodFamily;
    }
    
    public String findDescByProdFamily(String prodFamily) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select * from WM_SAP_MASTER_PRODUCT_FAMILY where PRODUCT_FAMILY = ?";
        GenericRowMapper<SapMasterProductFamilyTo> rm = new GenericRowMapper<SapMasterProductFamilyTo>(
                SapMasterProductFamilyTo.class);
        List<SapMasterProductFamilyTo> result = sjt.query(sql, rm, new Object[]{prodFamily});
        if (result != null && result.size() > 0) {
            return result.get(0).getDescription();
        } else {
            return "";
        }
        
    }  
    /*
     * add by jere/傳入desc, 取回value
     */
    public String findFamilyCodeByDesc(String familyDesc) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String sql = "select * from WM_SAP_MASTER_PRODUCT_FAMILY where DESCRIPTION = ?";
        GenericRowMapper<SapMasterProductFamilyTo> rm = new GenericRowMapper<SapMasterProductFamilyTo>(
                SapMasterProductFamilyTo.class);
        List<SapMasterProductFamilyTo> result = sjt.query(sql, rm, new Object[]{familyDesc});
        if (result != null && result.size() > 0) {
            return result.get(0).getProductFamily();
        } else {
            return "";
        }
        
    }  
}
