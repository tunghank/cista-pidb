package com.cista.pidb.admin.dao;

import java.util.List;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.cista.pidb.admin.to.ParameterTo;
import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;


public class ParameterDao extends PIDBDaoSupport {

	public List<ParameterTo> findAll() {
		SimpleJdbcTemplate stj = super.getSimpleJdbcTemplate();
		String sql = "select * from PIDB_FUN_PARAMETER_VALUE";
		logger.debug(sql);
		return stj.query(sql, new GenericRowMapper<ParameterTo>(
				ParameterTo.class), new Object[] {});
	}

	public List<ParameterTo> findAllFunName() {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select distinct FUN_NAME from PIDB_FUN_PARAMETER_VALUE order by FUN_NAME";
		logger.debug(sql);
		return stj.query(sql, new GenericRowMapper<ParameterTo>(
				ParameterTo.class), new Object[] {});
	}

	public List<ParameterTo> findMaxItem(final String funName,
			final String funFieldName) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_FUN_PARAMETER_VALUE a where a.item in "
				+ "(select max(item) from pidb_fun_parameter_value b where b.fun_name=? and b.fun_field_name=?)"
				+ "and a.fun_name=? and a.fun_field_name=?";
		logger.debug(sql);
		GenericRowMapper<ParameterTo> rm = new GenericRowMapper<ParameterTo>(
				ParameterTo.class);
		return sjt.query(sql, rm, new Object[] { funName, funFieldName,
				funName, funFieldName });
	}

	public List<ParameterTo> find(final ParameterTo queryTo) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_FUN_PARAMETER_VALUE where 1=1";

		if (queryTo.getFunName() != null && !queryTo.getFunName().equals("")) {
			sql += " AND FUN_NAME = '" + queryTo.getFunName()+"'";
		}

		if (queryTo.getFunFieldName() != null
				&& !queryTo.getFunFieldName().equals("")) {
			sql += " AND FUN_FIELD_NAME = '" + queryTo.getFunFieldName()+"'";
		}

		if (queryTo.getFieldShowName() != null
				&& !queryTo.getFieldShowName().equals("")) {
			sql += " AND FIELD_SHOW_NAME = '" + queryTo.getFieldShowName()+"'";
		}

		GenericRowMapper rm = new GenericRowMapper<ParameterTo>(
				ParameterTo.class);
		return sjt.query(sql, rm, new Object[] {});
	}

	public ParameterTo findList(final String funName,
			final String funFieldName, final String fieldValue,
			final String fieldShowName) {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_FUN_PARAMETER_VALUE where FUN_NAME= ? "
				+ " and FUN_FIELD_NAME = ? and FIELD_VALUE = ? and FIELD_SHOW_NAME = ?";
		logger.debug(sql);
		List<ParameterTo> result = stj
				.query(sql,
						new GenericRowMapper<ParameterTo>(ParameterTo.class),
						new Object[] { funName, funFieldName, fieldValue,
								fieldShowName });
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	public void updateParameter(final ParameterTo newInstance) {
		if (newInstance == null) {
			logger.error("Error the parameter is null.");
		}
		TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(
					final TransactionStatus status) {
				SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
				String sql = "update PIDB_FUN_PARAMETER_VALUE set "
						+ " FUN_NAME=?, FUN_FIELD_NAME=?,"
						+ "FIELD_VALUE=?, ITEM=?," + "FIELD_SHOW_NAME=? "
						+ " where FUN_NAME=? and FUN_FIELD_NAME = ? and ITEM=?";
				logger.debug(sql);
				sjt.update(sql, newInstance.getFunName(), newInstance
						.getFunFieldName(), newInstance.getFieldValue(),
						newInstance.getItem(), newInstance.getFieldShowName(),
						newInstance.getFunName(),
						newInstance.getFunFieldName(), newInstance.getItem());
			}
		};
		doInTransaction(callback);
	}

	public void delete(final ParameterTo newInstance) {
		TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
			public void doInTransactionWithoutResult(
					final TransactionStatus status) {
				SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
				String sql = "delete from PIDB_FUN_PARAMETER_VALUE where fun_name=?"
						+ " and fun_field_name=? and field_value=?";
				logger.debug(sql);
				sjt.update(sql, newInstance.getFunName(), newInstance
						.getFunFieldName(), newInstance.getFieldValue());
				sql = "delete from PIDB_FUN_PARAMETER_VALUE where fun_name=?"
						+ " and fun_field_name=? and field_value=?";
				logger.debug(sql);
				sjt.update(sql, newInstance.getFunName(), newInstance
						.getFunFieldName(), newInstance.getFieldValue());
			}
		};
		doInTransaction(callback);
	}

}
