package com.cista.pidb.dialog.dao;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.dialog.action.SmartSearch;

public class SmartSearchDao extends PIDBDaoSupport {
    public List<Map<String, Object>> find(SmartSearch ss, String inputFieldValue) {
        SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
        String filterEmpty = "";
        String sql = "select ";
        if (ss.getColumns() == null || ss.getColumns().size() == 0) {
            sql += "distinct " + ss.getKeyColumn() + "";
            filterEmpty = getAssertEmptyString(ss.getKeyColumn());
        } else if (ss.getColumns() != null && ss.getColumns().size() == 1) {
            sql += "distinct " + ss.getColumns().get(0) + "";
            filterEmpty = getAssertEmptyString(ss.getColumns().get(0));
        } else {
            List<String> cols = ss.getColumns();
            String distStr = "";
            if (cols != null) {
                for (String s : cols) {
                    distStr += "," + s;
                }
            }
            if (distStr.length() > 0) {
                distStr = distStr.substring(1);
            }
            
            sql += "distinct " + distStr + "";
            //filterEmpty = getAssertEmptyString(ss.getColumns().get(0));            
            //sql += "distinct *";
        }

        sql += " from " + ss.getTable() + " where 1 = 1";

        if (filterEmpty.length() > 0) {
            sql += " and " + filterEmpty;
        }
        if (inputFieldValue != null && inputFieldValue.length() > 0) {
            sql += " and " +ss.getKeyColumn() +" like "+ super.getLikeSQL(inputFieldValue.toUpperCase());
        }

        if (ss.getWhereCause() != null && ss.getWhereCause().length() > 0) {
            sql += " and " + ss.getWhereCause();
        }

        if (ss.getOrderBy() != null && ss.getOrderBy().length() > 0) {
            sql += " order by " + ss.getOrderBy();
        } else {
            sql += " order by " + ss.getKeyColumn();
        }

        return sjt.queryForList(sql, new Object[]{});
    }
}
