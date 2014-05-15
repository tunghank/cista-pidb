package com.cista.pidb.md.dao;

import java.util.List;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.md.to.CpCspMaterialQueryTo;
import com.cista.pidb.md.to.CpCspMaterialTo;

public class CpCspMaterialDao extends PIDBDaoSupport {

	/**
	 * create an CP Polish Material object.
	 * 
	 * @param newInstance
	 */
	public void insertCspMaterial(final CpCspMaterialTo newInstance) {
		if (null == newInstance) {
			logger.error("Error the new CP Polish Material object is null.");
		}
		TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(
					final TransactionStatus status) {

				SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();

				String sql = "insert into PIDB_CP_CSP_MATERIAL "
						+ "(CP_CSP_MATERIAL_NUM," + "PROJECT_CODE_W_VERSION,"
						+ "DESCRIPTION," + "REMARK," + "UPDATE_DATE,"
						+ "CREATED_BY," + "MODIFIED_BY," + "MP_STATUS ,"
						+ "CP_CSP_VARIANT, VERSION)"
						+ " values(?,?,?,?,?,?,?,?,?,?)";
				logger.debug(sql);
				sjt
						.update(sql, newInstance.getCpCspMaterialNum(),
								newInstance.getProjectCodeWVersion(),
								newInstance.getDescription(), newInstance
										.getRemark(), newInstance
										.getUpdateDate(), newInstance
										.getCreatedBy(), newInstance
										.getModifiedBy(), newInstance
										.getMpStatus(), newInstance
										.getCpCspVariant(), newInstance
										.getVersion());

			}
		};
		doInTransaction(callback);
	}

	public CpCspMaterialTo findCspMaterial(final String cpCspMaterial) {
		SimpleJdbcTemplate stj = super.getSimpleJdbcTemplate();
		String sql = "select * from PIDB_CP_CSP_MATERIAL where CP_CSP_MATERIAL_NUM = ?";
		logger.debug(sql);
		List<CpCspMaterialTo> result = stj.query(sql,
				new GenericRowMapper<CpCspMaterialTo>(CpCspMaterialTo.class),
				new Object[] { cpCspMaterial });
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			logger.warn("User with cpCspMaterial = " + cpCspMaterial
					+ " is not found.");
			return null;
		}
	}

	public String findVersionByProjwVersion(final String projectCodeWVersion) {
		SimpleJdbcTemplate stj = super.getSimpleJdbcTemplate();
		String sql = "select distinct Version from PIDB_CP_CSP_MATERIAL where PROJECT_CODE_W_VERSION = ?";
		logger.debug(sql);
		List<CpCspMaterialTo> result = stj.query(sql,
				new GenericRowMapper<CpCspMaterialTo>(CpCspMaterialTo.class),
				new Object[] { projectCodeWVersion });
		if (result != null && result.size() > 0) {
			CpCspMaterialTo cpCspTo = new CpCspMaterialTo();
			CpCspMaterialDao cpcspDao = new CpCspMaterialDao();
			String version = null;
			for (CpCspMaterialTo to : result) {
				cpCspTo = cpcspDao.findByProjwVersion(projectCodeWVersion);
				if (cpCspTo == null) {
					version = "";
				} else {
					version = cpCspTo.getVersion();

				}
			}
			return version;
		} else {
			logger.warn("User with PROJECT_CODE_W_VERSION = "
					+ projectCodeWVersion + " is not found.");
			return null;
		}
	}

	public CpCspMaterialTo findByProjwVersion(final String projectCodeWVersion) {
		SimpleJdbcTemplate stj = super.getSimpleJdbcTemplate();
		String sql = "select * from PIDB_CP_CSP_MATERIAL where PROJECT_CODE_W_VERSION = ?";
		logger.debug(sql);
		List<CpCspMaterialTo> result = stj.query(sql,
				new GenericRowMapper<CpCspMaterialTo>(CpCspMaterialTo.class),
				new Object[] { projectCodeWVersion });
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			logger.warn("User with PROJECT_CODE_W_VERSION = "
					+ projectCodeWVersion + " is not found.");
			return null;
		}
	}

	public List<CpCspMaterialTo> findByProjectCode(String projectCodeWVersion) {
		if (projectCodeWVersion == null && projectCodeWVersion.length() <= 0) {
			logger.warn("Project Code is employ,it is must field.");
		}
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_CP_CSP_MATERIAL where PROJECT_CODE_W_VERSION=? order by 1 ";
		logger.debug(sql);
		List<CpCspMaterialTo> result = stj.query(sql,
				new GenericRowMapper<CpCspMaterialTo>(CpCspMaterialTo.class),
				new Object[] { projectCodeWVersion });

		return result;
	}

	/**
	 * .
	 * 
	 * @param queryTo
	 *            CpCspMaterialQueryTo
	 * @return List
	 */
	public List<CpCspMaterialTo> query(final CpCspMaterialQueryTo queryTo) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "";
		sql = "SELECT distinct " + " CP_CSP_MATERIAL_NUM,"
				+ " PROJECT_CODE_W_VERSION," + " DESCRIPTION," + " REMARK,"
				+ " UPDATE_DATE," + " CREATED_BY," + " MODIFIED_BY,"
				+ " MP_STATUS," + " CP_CSP_VARIANT, VERSION "
				+ " FROM PIDB_CP_CSP_MATERIAL where 1=1 ";

		String whereCause = generateWhereCause(queryTo);
		sql += whereCause;
		sql += " order by CP_CSP_MATERIAL_NUM";

		GenericRowMapper<CpCspMaterialTo> rm = new GenericRowMapper<CpCspMaterialTo>(
				CpCspMaterialTo.class);
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
	 *            CpCspMaterialQueryTo
	 * @return String
	 */
	private String generateWhereCause(final CpCspMaterialQueryTo queryTo) {
		StringBuilder sb = new StringBuilder();

		if (queryTo.getCpCspMaterialNum() != null
				&& !queryTo.getCpCspMaterialNum().equals("")) {
			String queryString = getSmartSearchQueryString(
					"CP_CSP_MATERIAL_NUM", queryTo.getCpCspMaterialNum());
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
		if (queryTo.getCpCspVariant() != null
				&& !"".equals(queryTo.getCpCspVariant().trim())) {
			String queryString = getSmartSearchQueryString("CP_CSP_VARIANT",
					queryTo.getCpCspVariant());
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
	 *            CpCspMaterialQueryTo
	 * @return int
	 */
	public int countResult(final CpCspMaterialQueryTo queryTo) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "";
		sql = " select count(*) from PIDB_CP_CSP_MATERIAL where 1=1";
		String whereCause = generateWhereCause(queryTo);
		sql += whereCause;
		return sjt.queryForInt(sql, new Object[] {});
	}

	/**
	 * Find all csp in the table.
	 * 
	 * @return List of CpMaterialTo
	 */
	public List<CpCspMaterialTo> findAll() {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_CP_CSP_MATERIAL order by CP_CSP_MATERIAL_NUM ";
		logger.debug(sql);
		return sjt.query(sql, new GenericRowMapper<CpCspMaterialTo>(
				CpCspMaterialTo.class), new Object[] {});
	}

	public CpCspMaterialTo findByPrimaryKey(String cpCspMaterialNum,
			String projCodeWVersion) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_CP_CSP_MATERIAL where CP_CSP_MATERIAL_NUM = ? AND PROJECT_CODE_W_VERSION = ?";

		logger.debug(sql);

		GenericRowMapper<CpCspMaterialTo> rm = new GenericRowMapper<CpCspMaterialTo>(
				CpCspMaterialTo.class);
		List<CpCspMaterialTo> result = sjt.query(sql, rm, new Object[] {
				cpCspMaterialNum, projCodeWVersion });

		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}

	}

	public void updateCsp(final CpCspMaterialTo newInstance) {
		if (newInstance == null) {
			logger.error("Error the parameter is null.");
		}
		TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(
					final TransactionStatus status) {
				SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
				String sql = "update PIDB_CP_CSP_MATERIAL set "
						+ "DESCRIPTION=?," + "UPDATE_DATE=?," + "CREATED_BY=?,"
						+ "MODIFIED_BY=? " + "where CP_CSP_MATERIAL_NUM=? ";
				logger.debug(sql);
				sjt.update(sql, newInstance.getDescription(), newInstance
						.getUpdateDate(), newInstance.getCreatedBy(),
						newInstance.getModifiedBy(), newInstance
								.getCpCspMaterialNum());
			}
		};
		doInTransaction(callback);
	}

}
