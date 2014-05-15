package com.cista.pidb.code.dao;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.cista.pidb.core.PIDBDaoSupport;

public class SapDefaultSingleValueDao extends PIDBDaoSupport {


    
    public String findDefaultValue(String variableName) {
    	
        SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
        String sql = "select DEFAULT_VALUE from WM_SAP_DEFAULT_SINGLE_VALUE where VARIABLE_NAME = ?";

        List<Map<String, Object>> result = stj.queryForList(sql,
                new Object[] { variableName });
        String defaultValue = "";
        if (result != null && result.size() > 0) {
            Map<String, Object> item = result.get(0);
            defaultValue = (String) item.get("DEFAULT_VALUE");
        }
        return defaultValue;
        
    }
}
