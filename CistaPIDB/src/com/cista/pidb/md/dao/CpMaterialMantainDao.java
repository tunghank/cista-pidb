package com.cista.pidb.md.dao;

import java.util.List;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.md.to.CpMaterialMatainQueryTo;
import com.cista.pidb.md.to.CpMaterialMatainTo;

public class CpMaterialMantainDao extends PIDBDaoSupport {

	public CpMaterialMatainTo findByCpMaterialNum(String cpMaterialNum) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_CP_MATERIAL_TEST where CP_MATERIAL_NUM = ?";

		logger.debug(sql);

		GenericRowMapper<CpMaterialMatainTo> rm = new GenericRowMapper<CpMaterialMatainTo>(
				CpMaterialMatainTo.class);
		List<CpMaterialMatainTo> result = sjt.query(sql, rm,
				new Object[] { cpMaterialNum });

		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}

	}

	public CpMaterialMatainTo findCpMaterialNum(String cpMaterialNum,
			String projCodeWVersion) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_CP_MATERIAL_TEST where CP_MATERIAL_NUM = ? and PROJ_CODE_W_VERSION = ?";

		logger.debug(sql);

		GenericRowMapper<CpMaterialMatainTo> rm = new GenericRowMapper<CpMaterialMatainTo>(
				CpMaterialMatainTo.class);
		List<CpMaterialMatainTo> result = sjt.query(sql, rm, new Object[] {
				cpMaterialNum, projCodeWVersion });

		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}

	}

	public void insertCpMaterial(final CpMaterialMatainTo newInstance) {
		if (null == newInstance) {
			logger.error("Error the new IcWafer object is null.");
		}
		TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(
					final TransactionStatus status) {

				SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();

				String sql = "insert into PIDB_CP_MATERIAL_TEST "
						+ "(CP_MATERIAL_NUM," + " PROJ_CODE_W_VERSION,"
						+ " CP_VARIANT," + " CP_TEST_PROGRAM_NAME_LIST,"
						+ " DESCRIPTION," + " REMARK," + " UPDATE_DATE,"
						+ " CREATED_BY," + " MODIFIED_BY," + " MP_STATUS)"
						+ " values(?,?,?,?,?,?,?,?,?,?)";
				logger.debug(sql);
				sjt.update(sql, newInstance.getCpMaterialNum(), newInstance
						.getProjCodeWVersion(), newInstance.getCpVariant(),
						newInstance.getCpTestProgramNameList(), newInstance
								.getDescription(), newInstance.getRemark(),
						newInstance.getUpdateDate(),
						newInstance.getCreatedBy(),
						newInstance.getModifiedBy(), newInstance.getMpStatus());

			}
		};
		doInTransaction(callback);
	}

	public void UpdateCpMaterial(final CpMaterialMatainTo newInstance) {
		if (null == newInstance) {
			logger.error("Error the new IcWafer object is null.");
		}
		TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(
					final TransactionStatus status) {

				SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();

				String sql = "UPDATE PIDB_CP_MATERIAL_TEST "
						+ " SET CP_VARIANT = ?,"
						+ " CP_TEST_PROGRAM_NAME_LIST = ?,"
						+ " REMARK=?,"
						+ " UPDATE_DATE = ?,"
						+ " MODIFIED_BY = ?,"
						+ " MP_STATUS = ? "
						+ " WHERE CP_MATERIAL_NUM = ? AND PROJ_CODE_W_VERSION = ?";
				logger.debug(sql);
				sjt.update(sql, newInstance.getCpVariant(), newInstance
						.getCpTestProgramNameList(), newInstance.getRemark(),
						newInstance.getUpdateDate(), newInstance
								.getModifiedBy(), newInstance.getMpStatus(),
						newInstance.getCpMaterialNum(), newInstance
								.getProjCodeWVersion());

			}
		};
		doInTransaction(callback);
	}

	/**
	 * Find all ctps in the table.
	 * 
	 * @return List of CpMaterialMatainTo
	 */
	public List<CpMaterialMatainTo> findAll() {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_CP_MATERIAL_TEST order by CP_MATERIAL_NUM ";
		logger.debug(sql);
		return sjt.query(sql, new GenericRowMapper<CpMaterialMatainTo>(
				CpMaterialMatainTo.class), new Object[] {});
	}

	/**
	 * .
	 * 
	 * @param queryTo
	 *            CpTestProgramQueryTo
	 * @return List
	 */
	public List<CpMaterialMatainQueryTo> query(
			final CpMaterialMatainQueryTo queryTo) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "";
		sql = "SELECT distinct " + " CP_MATERIAL_NUM,"
				+ " PROJ_CODE_W_VERSION," + " CP_VARIANT,"
				+ " CP_TEST_PROGRAM_NAME_LIST," + " DESCRIPTION," + " REMARK,"
				+ " UPDATE_DATE," + " CREATED_BY," + " MODIFIED_BY,"
				+ " MP_STATUS " + " FROM PIDB_CP_MATERIAL_TEST where 1=1 ";

		String whereCause = generateWhereCause(queryTo);
		sql += whereCause;
		sql += " order by CP_MATERIAL_NUM";

		GenericRowMapper<CpMaterialMatainQueryTo> rm = new GenericRowMapper<CpMaterialMatainQueryTo>(
				CpMaterialMatainQueryTo.class);
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
	 *            CpMaterialMatainTo
	 * @return List
	 */
	public List<CpMaterialMatainTo> queryTo(
			final CpMaterialMatainQueryTo queryTo) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "";
		sql = "SELECT distinct " + " CP_MATERIAL_NUM,"
				+ " PROJ_CODE_W_VERSION," + " CP_VARIANT,"
				+ " CP_TEST_PROGRAM_NAME_LIST," + " DESCRIPTION," + " REMARK,"
				+ " UPDATE_DATE," + " CREATED_BY," + " MODIFIED_BY,"
				+ " MP_STATUS " + " FROM PIDB_CP_MATERIAL_TEST where 1=1";
		String whereCause = generateWhereCause(queryTo);
		sql += whereCause;
		sql += " order by CP_MATERIAL_NUM";

		GenericRowMapper<CpMaterialMatainTo> rm = new GenericRowMapper<CpMaterialMatainTo>(
				CpMaterialMatainTo.class);
		List<CpMaterialMatainTo> result = null;
		if (queryTo.getPageNo() > 0) {
			int cursorFrom = (queryTo.getPageNo() - 1) * queryTo.getPageSize()
					+ 1;
			int cursorTo = (queryTo.getPageNo()) * queryTo.getPageSize();
			result = sjt.query(getPagingSql(sql, cursorFrom, cursorTo), rm,
					new Object[] {});
		} else {
			result = sjt.query(sql, rm, new Object[] {});
		}
		if (result != null && result.size() > 0) {
			for (CpMaterialMatainTo t : result) {
				if (t.getMpStatus() == null) {
					t.setMpStatus("");
				} else if (t.getMpStatus().equals("1")) {
					t.setMpStatus("Active");
				} else if (t.getMpStatus().equals("0")) {
					t.setMpStatus("Inactive");
				}
			}
		}
		return result;
	}

	/**
	 * .
	 * 
	 * @param queryTo
	 *            CpMaterialMatainQueryTo
	 * @return String
	 */
	private String generateWhereCause(final CpMaterialMatainQueryTo queryTo) {
		StringBuilder sb = new StringBuilder();

		if (queryTo.getCpMaterialNum() != null
				&& !queryTo.getCpMaterialNum().equals("")) {
			String queryString = getSmartSearchQueryString("CP_MATERIAL_NUM",
					queryTo.getCpMaterialNum());
			if (queryString != null) {
				sb.append(" and (" + queryString + " )");
			}
		}
		if (queryTo.getProjCodeWVersion() != null
				&& !queryTo.getProjCodeWVersion().equals("")) {
			String queryString = getSmartSearchQueryString(
					"PROJ_CODE_W_VERSION", queryTo.getProjCodeWVersion());
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

		if (queryTo.getCpTestProgramNameList() != null
				&& !queryTo.getCpTestProgramNameList().equals("")) {
			String queryString = getSmartSearchQueryString(
					"CP_TEST_PROGRAM_NAME_LIST", queryTo
							.getCpTestProgramNameList());
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
	 *            CpMaterialMatainQueryTo
	 * @return int
	 */
	public int countResult(final CpMaterialMatainQueryTo queryTo) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "";
		sql = " select count(*) from PIDB_CP_MATERIAL_TEST where 1=1";
		String whereCause = generateWhereCause(queryTo);
		sql += whereCause;
		return sjt.queryForInt(sql, new Object[] {});
	}

}
