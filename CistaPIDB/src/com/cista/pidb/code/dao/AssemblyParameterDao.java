package com.cista.pidb.code.dao;

import java.util.List;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.cista.pidb.code.to.FunctionParameterTo;
import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;

public class AssemblyParameterDao extends PIDBDaoSupport{
	
	public List findAll() {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select item,field_value,field_show_name from pidb_fun_parameter_value where "
				+ " fun_field_name='ASSEMBLY_SITE' and fun_name='IC_TAPE' order by item";
		logger.debug(sql);
		List<FunctionParameterTo> result = stj.query(sql,
				new GenericRowMapper<FunctionParameterTo>(
						FunctionParameterTo.class), new Object[] {});
		return result;
	}

	public List<FunctionParameterTo> findLikeFieldValue(String fieldValue) {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select item,field_value,field_show_name from pidb_fun_parameter_value where fun_field_name='ASSEMBLY_SITE' and fun_name='IC_TAPE'";
		if (fieldValue != null && fieldValue.length() > 0) {
			sql += " and field_value like "
					+ super.getLikeSQLString(fieldValue.toUpperCase());
		}
		sql += "order by ITEM ";
		logger.debug(sql);
		List<FunctionParameterTo> result = stj.query(sql,
				new GenericRowMapper<FunctionParameterTo>(
						FunctionParameterTo.class), new Object[] {});
		return result;
	}

	public FunctionParameterTo findByFieldValue(String fieldValue) {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select field_value,field_show_name from pidb_fun_parameter_value where fun_field_name='ASSEMBLY_SITE' and fun_name='IC_TAPE' AND field_value=? "
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

}
