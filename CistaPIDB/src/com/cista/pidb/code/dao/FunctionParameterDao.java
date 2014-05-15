package com.cista.pidb.code.dao;

import java.util.List;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.cista.pidb.code.to.FunctionParameterTo;
import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;

public class FunctionParameterDao extends PIDBDaoSupport {

	public List findAll() {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select item,field_value,field_show_name from pidb_fun_parameter_value where "
				+ " fun_field_name='REVISION_ITEM' and fun_name='IC_WAFER' order by item";
		logger.debug(sql);
		List<FunctionParameterTo> result = stj.query(sql,
				new GenericRowMapper<FunctionParameterTo>(
						FunctionParameterTo.class), new Object[] {});
		return result;
	}

	public List<FunctionParameterTo> findLikeFieldValue(String funName, String funFieldName) {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "SELECT ITEM,FIELD_VALUE,FIELD_SHOW_NAME " +
				" FROM PIDB_FUN_PARAMETER_VALUE " +
				" WHERE FUN_FIELD_NAME= '" + funFieldName + "' " +
				" AND FUN_NAME='" + funName + "'";
		
		sql += "Order by ITEM ";
		logger.debug(sql);
		List<FunctionParameterTo> result = stj.query(sql,
				new GenericRowMapper<FunctionParameterTo>(
						FunctionParameterTo.class), new Object[] {});
		return result;
	}

	public FunctionParameterTo findByFieldValue(String fieldValue) {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select field_value,field_show_name from pidb_fun_parameter_value where fun_field_name='REVISION_ITEM' and fun_name='IC_WAFER' AND field_value=? "
				+ " Order By ITEM ";
		logger.debug(sql);
		List<FunctionParameterTo> result = stj
				.query(sql, new GenericRowMapper<FunctionParameterTo>(
						FunctionParameterTo.class), new Object[] { fieldValue });

		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}
	
	public FunctionParameterTo findValue(String fundName, String funFieldName) {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select * from pidb_fun_parameter_value where  fun_name=? and fun_field_name=? "
				+ " Order By ITEM ";
		logger.debug(sql);
		List<FunctionParameterTo> result = stj
				.query(sql, new GenericRowMapper<FunctionParameterTo>(
						FunctionParameterTo.class), new Object[] { fundName,funFieldName });

		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}
	
	public List<FunctionParameterTo> findValueList(String fundName, String funFieldName) {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select * from pidb_fun_parameter_value where fun_name=? and fun_field_name=?";
		sql += "order by ITEM ";
		logger.debug(sql);
		List<FunctionParameterTo> result = stj.query(sql,
				new GenericRowMapper<FunctionParameterTo>(
						FunctionParameterTo.class), new Object[] {fundName,funFieldName});
		return result;
	}
	
	public FunctionParameterTo findValueByShowName(String fundName, String funFieldName, String shortName) {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select * from pidb_fun_parameter_value where  fun_name=? and fun_field_name=? and FIELD_SHOW_NAME =? "
				+ " Order By ITEM ";
		logger.debug(sql);
		List<FunctionParameterTo> result = stj
				.query(sql, new GenericRowMapper<FunctionParameterTo>(
						FunctionParameterTo.class), new Object[] { fundName,funFieldName,shortName});

		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}
	
	public FunctionParameterTo findValueByFiledValue(String fundName, String funFieldName, String fieldValue) {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select * from pidb_fun_parameter_value where  fun_name=? and fun_field_name=? and FIELD_VALUE=?"
				+ " Order By ITEM ";
		logger.debug(sql);
		List<FunctionParameterTo> result = stj
				.query(sql, new GenericRowMapper<FunctionParameterTo>(
						FunctionParameterTo.class), new Object[] { fundName,funFieldName, fieldValue});

		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

}
