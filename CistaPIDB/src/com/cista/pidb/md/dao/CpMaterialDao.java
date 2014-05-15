package com.cista.pidb.md.dao;

import java.util.List;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.md.to.CpMaterialQueryTo;
import com.cista.pidb.md.to.CpMaterialTo;

public class CpMaterialDao extends PIDBDaoSupport {

	/**
	 * Find all ctps in the table.
	 * 
	 * @return List of CpMaterialTo
	 */
	public List<CpMaterialTo> findAll() {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_CP_MATERIAL order by CP_MATERIAL_NUM ";
		logger.debug(sql);
		return sjt.query(sql, new GenericRowMapper<CpMaterialTo>(
				CpMaterialTo.class), new Object[] {});
	}

	public List<CpMaterialTo> getCpMaterialVariantPolish(String projectCodeWVersion) {
		if (projectCodeWVersion == null && projectCodeWVersion.length() <= 0) {
			logger.warn("Project Code is employ,it is must field.");
		}
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "SELECT * FROM PIDB_CP_MATERIAL  WHERE CP_VARIANT NOT IN "
				+ "( SELECT CP_POLISH_VARIANT FROM PIDB_CP_POLISH_MATERIAL WHERE PROJECT_CODE_W_VERSION= ?) "
				+ " AND PROJECT_CODE_W_VERSION= ? ORDER BY 1 ";
		logger.debug(sql);
		List<CpMaterialTo> result = stj.query(sql,
				new GenericRowMapper<CpMaterialTo>(CpMaterialTo.class),
				new Object[] { projectCodeWVersion, projectCodeWVersion });

		return result;
	}
	

	public List<CpMaterialTo> getCpMaterialVariant(String projectCodeWVersion) {
		if (projectCodeWVersion == null && projectCodeWVersion.length() <= 0) {
			logger.warn("Project Code is employ,it is must field.");
		}
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "SELECT * FROM PIDB_CP_MATERIAL  " +
				" WHERE PROJECT_CODE_W_VERSION= ? ORDER BY 1 ";
		logger.debug(sql);
		List<CpMaterialTo> result = stj.query(sql,
				new GenericRowMapper<CpMaterialTo>(CpMaterialTo.class),
				new Object[] { projectCodeWVersion });

		return result;
	}
	
	public CpMaterialTo findByCpList(String cpList, String projCodeWVersion) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_CP_MATERIAL where pidb_contain(CP_TEST_PROGRAM_NAME_LIST, ',', "
				+ getSQLString(cpList)
				+ ", ',')>=1 and pidb_contain("
				+ getSQLString(cpList)
				+ ", ',', CP_TEST_PROGRAM_NAME_LIST, ',')>=1 and PROJECT_CODE_W_VERSION = ?";
		logger.debug(sql);
		GenericRowMapper<CpMaterialTo> rm = new GenericRowMapper<CpMaterialTo>(
				CpMaterialTo.class);
		List<CpMaterialTo> result = sjt.query(sql, rm,
				new Object[] { projCodeWVersion });
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	public List<CpMaterialTo> findByCpTestProgName(String cpTestProgName,
			String projCodeWVersion) {

		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_CP_MATERIAL where 1=1 and PROJECT_CODE_W_VERSION = ? ";
		if (cpTestProgName != null && cpTestProgName.length() > 0) {
			sql += " and pidb_include(CP_TEST_PROGRAM_NAME_LIST, ',', "
					+ getSQLString(cpTestProgName) + ")>=1 ";

		}

		logger.debug(sql);
		GenericRowMapper<CpMaterialTo> rm = new GenericRowMapper<CpMaterialTo>(
				CpMaterialTo.class);
		List<CpMaterialTo> result = sjt.query(sql, rm,
				new Object[] { projCodeWVersion });

		return result;
	}

	public Object findMaxVar(String projCodeWVersion) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select max(CP_VARIANT) from PIDB_CP_MATERIAL where PROJECT_CODE_W_VERSION = ?";
		logger.debug(sql);
		Object obj = sjt.queryForObject(sql, Object.class,
				new Object[] { projCodeWVersion });
		return obj;
	}

	public CpMaterialTo findByCpMaterialNum(String cpMaterialNum) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_CP_MATERIAL where CP_MATERIAL_NUM = ?";

		logger.debug(sql);

		GenericRowMapper<CpMaterialTo> rm = new GenericRowMapper<CpMaterialTo>(
				CpMaterialTo.class);
		List<CpMaterialTo> result = sjt.query(sql, rm,
				new Object[] { cpMaterialNum });

		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}

	}

	public CpMaterialTo findByPrimaryKey(String cpMaterialNum,
			String projCodeWVersion) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_CP_MATERIAL where CP_MATERIAL_NUM = ? AND PROJECT_CODE_W_VERSION = ?";

		logger.debug(sql);

		GenericRowMapper<CpMaterialTo> rm = new GenericRowMapper<CpMaterialTo>(
				CpMaterialTo.class);
		List<CpMaterialTo> result = sjt.query(sql, rm, new Object[] {
				cpMaterialNum, projCodeWVersion });

		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}

	}

	public CpMaterialTo findByCpMaterialNum(String cpMaterialNum,
			String cpVariant) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_CP_MATERIAL where CP_MATERIAL_NUM = ? AND CP_VARIANT = ? ";

		logger.debug(sql);

		GenericRowMapper<CpMaterialTo> rm = new GenericRowMapper<CpMaterialTo>(
				CpMaterialTo.class);
		List<CpMaterialTo> result = sjt.query(sql, rm, new Object[] {
				cpMaterialNum, cpVariant });

		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}

	}

	public List<CpMaterialTo> getByProjCodeWVersion(
			final String projCodeWVersion) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_CP_MATERIAL where PROJECT_CODE_W_VERSION = ?";
		GenericRowMapper<CpMaterialTo> rm = new GenericRowMapper<CpMaterialTo>(
				CpMaterialTo.class);
		return sjt.query(sql, rm, new Object[] { projCodeWVersion });
	}

	public List<CpMaterialTo> getAll() {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_CP_MATERIAL";
		GenericRowMapper<CpMaterialTo> rm = new GenericRowMapper<CpMaterialTo>(
				CpMaterialTo.class);
		return sjt.query(sql, rm, new Object[] {});
	}

	public CpMaterialTo getByProjCodeWVersionMaxVariant(
			final String projCodeWVersion) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "SELECT * FROM PIDB_CP_MATERIAL B "
				+ " WHERE B.PROJECT_CODE_W_VERSION||'-VARIANT'||B.CP_VARIANT IN ( "
				+ " SELECT A.PROJECT_CODE_W_VERSION||'-VARIANT'||MAX(A.CP_VARIANT) CP_VARIANT "
				+ " FROM PIDB_CP_MATERIAL A "
				+ " WHERE A.PROJECT_CODE_W_VERSION = ? "
				+ " GROUP BY PROJECT_CODE_W_VERSION )";

		GenericRowMapper<CpMaterialTo> rm = new GenericRowMapper<CpMaterialTo>(
				CpMaterialTo.class);
		List<CpMaterialTo> result = sjt.query(sql, rm,
				new Object[] { projCodeWVersion });

		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	/**
	 * .
	 * 
	 * @param queryTo
	 *            CpMaterialQueryTo
	 * @return List
	 */
	public List<CpMaterialTo> query(final CpMaterialQueryTo queryTo) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "";
		
		sql = "SELECT distinct " + " CP_MATERIAL_NUM,"
				+ " PROJECT_CODE_W_VERSION," + " CP_VARIANT,"
				+ " CP_TEST_PROGRAM_NAME_LIST," + " DESCRIPTION," + " REMARK,"
				+ " UPDATE_DATE," + " CREATED_BY," + " MODIFIED_BY "
				+ " FROM PIDB_CP_MATERIAL where 1=1 ";

		String whereCause = generateWhereCause(queryTo);
		sql += whereCause;
		sql += " order by CP_MATERIAL_NUM";

		GenericRowMapper<CpMaterialTo> rm = new GenericRowMapper<CpMaterialTo>(
				CpMaterialTo.class);
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
	 *            CpMaterialQueryTo
	 * @return String
	 */
	private String generateWhereCause(final CpMaterialQueryTo queryTo) {
		StringBuilder sb = new StringBuilder();

		if (queryTo.getCpMaterialNum() != null
				&& !queryTo.getCpMaterialNum().equals("")) {
			String queryString = getSmartSearchQueryString("CP_MATERIAL_NUM",
					queryTo.getCpMaterialNum());
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
		if (queryTo.getCpVariant() != null
				&& !"".equals(queryTo.getCpVariant().trim())) {
			String queryString = getSmartSearchQueryString("CP_VARIANT",
					queryTo.getCpVariant());
			if (queryString != null) {
				sb.append(" and (" + queryString + " )");
			}
		}
		return sb.toString();
	}

	public void updateCpMaterial(final CpMaterialTo newInstance) {
		if (newInstance == null) {
			logger.error("Error the parameter is null.");
		}
		TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(
					final TransactionStatus status) {
				SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
				//modify by jere add remark column
				String sql = "update PIDB_CP_MATERIAL set "
						+ "DESCRIPTION=?,"
						+ "UPDATE_DATE=?,"
						+ "CREATED_BY=?,"
						+ "MODIFIED_BY=?,"
						+ "REMARK=? "
						+ "where CP_MATERIAL_NUM=? ";
				logger.debug(sql);
				sjt.update(sql, newInstance.getDescription(), newInstance
						.getUpdateDate(), newInstance.getCreatedBy(),
						newInstance.getModifiedBy(),newInstance.getRemark() , newInstance.getCpMaterialNum());
			}
		};
		doInTransaction(callback);
	}

	/**
	 * .
	 * 
	 * @param queryTo
	 *            CpMaterialQueryTo
	 * @return int
	 */
	public int countResult(final CpMaterialQueryTo queryTo) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "";
		sql = " select count(*) from PIDB_CP_MATERIAL where 1=1";
		String whereCause = generateWhereCause(queryTo);
		sql += whereCause;
		return sjt.queryForInt(sql, new Object[] {});
	}
}
