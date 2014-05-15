package com.cista.pidb.md.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.md.to.ProductTo;

public class ProductDao extends PIDBDaoSupport {

	public void insert(final ProductTo newInstance) {
		if (newInstance == null) {
			logger.error("Error the parameter is null.");
		}
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();

		// Sql for insert PIDB_PROJECT_CODE
		String sql = "insert into PIDB_PRODUCT" + "(PROD_CODE," + "PROD_NAME,"
				+ "PROD_OPTION," + "REMARK)" + " values(?, ?, ?, ?)";
		logger.debug(sql);
		sjt.update(sql, newInstance.getProdCode(), newInstance.getProdName(),
				newInstance.getProdOption(), newInstance.getRemark());
	}

	public List<ProductTo> findAll() {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_PRODUCT order by PROD_CODE";
		logger.debug(sql);
		return stj.query(sql, new GenericRowMapper<ProductTo>(ProductTo.class),
				new Object[] {});
	}

	public List<String> findDistAllProdCode() {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select distinct PROD_CODE from PIDB_PRODUCT order by PROD_CODE";
		logger.debug(sql);
		List<Map<String, Object>> result = stj.queryForList(sql,
				new Object[] {});
		List<String> prodCodes = new ArrayList<String>();
		if (result != null && result.size() > 0) {
			for (Map<String, Object> item : result) {
				prodCodes.add((String) item.get("PROD_CODE"));
			}
		}
		return prodCodes;
	}

	public ProductTo findByProdCode(String prodCode) {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_PRODUCT where PROD_CODE = ?";
		logger.debug(sql);
		List<ProductTo> ptl = stj.query(sql, new GenericRowMapper<ProductTo>(
				ProductTo.class), new Object[] { prodCode });
		if (ptl != null && ptl.size() > 0) {
			return ptl.get(0);
		} else {
			return null;
		}

	}

	public List<ProductTo> findByProdName(String prodName) {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_PRODUCT where PROD_NAME = ?";
		logger.debug(sql);

		return stj.query(sql, new GenericRowMapper<ProductTo>(ProductTo.class),
				new Object[] { prodName });

	}

	public List<String> findDistAllProdName() {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select distinct PROD_NAME from PIDB_PRODUCT order by PROD_NAME";
		logger.debug(sql);
		List<Map<String, Object>> result = stj.queryForList(sql,
				new Object[] {});
		List<String> prodNames = new ArrayList<String>();
		if (result != null && result.size() > 0) {
			for (Map<String, Object> item : result) {
				prodNames.add((String) item.get("PROD_NAME"));
			}
		}
		return prodNames;
	}

	public List<ProductTo> queryByProdCode(String prodCode) {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_PRODUCT where 1=1";
		if (prodCode != null && !prodCode.equals("")) {
			sql += " and PROD_CODE like "
					+ super.getLikeSQLString(prodCode).toUpperCase();
		}
		sql += " ORDER BY PROD_CODE ";
		logger.debug(sql);
		return stj.query(sql, new GenericRowMapper<ProductTo>(ProductTo.class),
				new Object[] {});
	}

}
