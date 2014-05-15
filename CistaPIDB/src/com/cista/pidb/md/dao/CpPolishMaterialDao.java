package com.cista.pidb.md.dao;

import java.util.List;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.md.to.CpPolishMaterialQueryTo;
import com.cista.pidb.md.to.CpPolishMaterialTo;

public class CpPolishMaterialDao extends PIDBDaoSupport {

	/**
	 * create an CP Polish Material object.
	 * 
	 * @param newInstance
	 */
	public void insertPolishMaterial(final CpPolishMaterialTo newInstance) {
		if (null == newInstance) {
			logger.error("Error the new CP Polish Material object is null.");
		}
		TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(
					final TransactionStatus status) {

				SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();

				String sql = "insert into PIDB_CP_POLISH_MATERIAL "
						+ "(CP_POLISH_MATERIAL_NUM,"
						+ "PROJECT_CODE_W_VERSION," + "DESCRIPTION,"
						+ "REMARK," + "UPDATE_DATE," + "CREATED_BY,"
						+ "MODIFIED_BY," + "MP_STATUS ," + "CP_POLISH_VARIANT)"
						+ " values(?,?,?,?,?,?,?,?,?)";
				logger.debug(sql);
				sjt.update(sql, newInstance.getCpPolishMaterialNum(),
						newInstance.getProjectCodeWVersion(), newInstance
								.getDescription(), newInstance.getRemark(),
						newInstance.getUpdateDate(),
						newInstance.getCreatedBy(),
						newInstance.getModifiedBy(), newInstance.getMpStatus(),
						newInstance.getCpPolishVariant());

			}
		};
		doInTransaction(callback);
	}

	public CpPolishMaterialTo findPolishMaterial(final String cpPolishMaterial) {
		SimpleJdbcTemplate stj = super.getSimpleJdbcTemplate();
		String sql = "select * from PIDB_CP_POLISH_MATERIAL where CP_POLISH_MATERIAL_NUM = ?";
		logger.debug(sql);
		List<CpPolishMaterialTo> result = stj.query(sql,
				new GenericRowMapper<CpPolishMaterialTo>(
						CpPolishMaterialTo.class),
				new Object[] { cpPolishMaterial });
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			logger.warn("User with cpPolishMaterial = " + cpPolishMaterial
					+ " is not found.");
			return null;
		}
	}

	public CpPolishMaterialTo findByProjwVersion(
			final String projectCodeWVersion) {
		SimpleJdbcTemplate stj = super.getSimpleJdbcTemplate();
		String sql = "select * from PIDB_CP_POLISH_MATERIAL where PROJECT_CODE_W_VERSION = ?";
		logger.debug(sql);
		List<CpPolishMaterialTo> result = stj.query(sql,
				new GenericRowMapper<CpPolishMaterialTo>(
						CpPolishMaterialTo.class),
				new Object[] { projectCodeWVersion });
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			logger.warn("User with PROJECT_CODE_W_VERSION = "
					+ projectCodeWVersion + " is not found.");
			return null;
		}
	}

	public List<CpPolishMaterialTo> findByProjectCode(String projectCodeWVersion) {
		if (projectCodeWVersion == null && projectCodeWVersion.length() <= 0) {
			logger.warn("Project Code is employ,it is must field.");
		}
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_CP_POLISH_MATERIAL where PROJECT_CODE_W_VERSION=? order by 1 ";
		logger.debug(sql);
		List<CpPolishMaterialTo> result = stj.query(sql,
				new GenericRowMapper<CpPolishMaterialTo>(
						CpPolishMaterialTo.class),
				new Object[] { projectCodeWVersion });

		return result;
	}

	/**
	 * .
	 * 
	 * @param queryTo
	 *            CpPolishMaterialQueryTo
	 * @return List
	 */
	public List<CpPolishMaterialTo> query(final CpPolishMaterialQueryTo queryTo) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "";
		sql = "SELECT distinct " + " CP_POLISH_MATERIAL_NUM,"
				+ " PROJECT_CODE_W_VERSION," + " DESCRIPTION," + " REMARK,"
				+ " UPDATE_DATE," + " CREATED_BY," + " MODIFIED_BY,"
				+ " MP_STATUS," + " CP_POLISH_VARIANT "
				+ " FROM PIDB_CP_POLISH_MATERIAL where 1=1 ";

		String whereCause = generateWhereCause(queryTo);
		sql += whereCause;
		sql += " order by CP_POLISH_MATERIAL_NUM";

		GenericRowMapper<CpPolishMaterialTo> rm = new GenericRowMapper<CpPolishMaterialTo>(
				CpPolishMaterialTo.class);
		if (queryTo.getPageNo() > 0) {
			int cursorFrom = (queryTo.getPageNo() - 1) * queryTo.getPageSize()
					+ 1;
			int cursorTo = (queryTo.getPageNo()) * queryTo.getPageSize();
			return sjt.query(getPagingSql(sql, cursorFrom, cursorTo), rm,
					new Object[] {});
		} else {
			return sjt.query(sql, rm, new Object[] {});
		}
	}

	/**
	 * .
	 * 
	 * @param queryTo
	 *            CpPolishMaterialQueryTo
	 * @return String
	 */
	private String generateWhereCause(final CpPolishMaterialQueryTo queryTo) {
		StringBuilder sb = new StringBuilder();

		if (queryTo.getCpPolishMaterialNum() != null
				&& !queryTo.getCpPolishMaterialNum().equals("")) {
			String queryString = getSmartSearchQueryString(
					"CP_POLISH_MATERIAL_NUM", queryTo.getCpPolishMaterialNum());
			if (queryString != null) {
				sb.append(" and (" + queryString + " )");
			}
		}
		if (queryTo.getProjectCodeWVersion() != null
				&& !queryTo.getProjectCodeWVersion().equals("")) {
			String queryString = getSmartSearchQueryString(
					"PROJECT_CODE_W_VERSION", queryTo.getProjectCodeWVersion());
			if (queryString != null) {
				sb.append(" and (" + queryString + " )");
			}
		}
		if (queryTo.getCpPolishVariant() != null
				&& !"".equals(queryTo.getCpPolishVariant().trim())) {
			String queryString = getSmartSearchQueryString("CP_POLISH_VARIANT",
					queryTo.getCpPolishVariant());
			if (queryString != null) {
				sb.append(" and (" + queryString + " )");
			}
		}
		return sb.toString();
	}

	/**
	 * .
	 * 
	 * @param queryTo
	 *            CpPolishMaterialQueryTo
	 * @return int
	 */
	public int countResult(final CpPolishMaterialQueryTo queryTo) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "";
		sql = " select count(*) from PIDB_CP_POLISH_MATERIAL where 1=1";
		String whereCause = generateWhereCause(queryTo);
		sql += whereCause;
		return sjt.queryForInt(sql, new Object[] {});
	}

	/**
	 * Find all ctps in the table.
	 * 
	 * @return List of CpMaterialTo
	 */
	public List<CpPolishMaterialTo> findAll() {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_CP_POLISH_MATERIAL order by CP_POLISH_MATERIAL_NUM ";
		logger.debug(sql);
		return sjt.query(sql, new GenericRowMapper<CpPolishMaterialTo>(
				CpPolishMaterialTo.class), new Object[] {});
	}

	public CpPolishMaterialTo findByPrimaryKey(String cpPolishMaterialNum,
			String projCodeWVersion) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_CP_POLISH_MATERIAL where CP_POLISH_MATERIAL_NUM = ? AND PROJECT_CODE_W_VERSION = ?";

		logger.debug(sql);

		GenericRowMapper<CpPolishMaterialTo> rm = new GenericRowMapper<CpPolishMaterialTo>(
				CpPolishMaterialTo.class);
		List<CpPolishMaterialTo> result = sjt.query(sql, rm, new Object[] {
				cpPolishMaterialNum, projCodeWVersion });

		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}

	}

	public void updatePolish(final CpPolishMaterialTo newInstance) {
		if (newInstance == null) {
			logger.error("Error the parameter is null.");
		}
		TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(
					final TransactionStatus status) {
				SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
				String sql = "update PIDB_CP_POLISH_MATERIAL set "
						+ "DESCRIPTION=?," + "UPDATE_DATE=?," + "CREATED_BY=?,"
						+ "MODIFIED_BY=? " + "where CP_POLISH_MATERIAL_NUM=? ";
				logger.debug(sql);
				sjt.update(sql, newInstance.getDescription(), newInstance
						.getUpdateDate(), newInstance.getCreatedBy(),
						newInstance.getModifiedBy(), newInstance
								.getCpPolishMaterialNum());
			}
		};
		doInTransaction(callback);
	}

}
