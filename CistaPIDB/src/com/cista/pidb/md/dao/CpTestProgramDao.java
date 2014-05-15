package com.cista.pidb.md.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.cista.pidb.code.dao.SapMasterVendorDao;
import com.cista.pidb.code.to.SapMasterVendorTo;
import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.md.to.CpTestProgramQueryTo;
import com.cista.pidb.md.to.CpTestProgramTo;

/**
 * .
 * 
 * @author Hu Meixia
 * 
 */
public class CpTestProgramDao extends PIDBDaoSupport {
	/**
	 * .
	 * 
	 * @param projCode
	 *            String
	 * @return List
	 */
	public List<CpTestProgramTo> findByProjCode(final String projCode) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_CP_TEST_PROGRAM where PROJ_CODE=?";
		GenericRowMapper<CpTestProgramTo> rm = new GenericRowMapper<CpTestProgramTo>(
				CpTestProgramTo.class);

		return sjt.query(sql, rm, new Object[] { projCode });
	}

	/**
	 * Find all ctps in the table.
	 * 
	 * @return List of CpTestProgramTo
	 */
	public List<CpTestProgramTo> findAll() {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_CP_TEST_PROGRAM order by CP_TEST_PROG_NAME";
		logger.debug(sql);
		return sjt.query(sql, new GenericRowMapper<CpTestProgramTo>(
				CpTestProgramTo.class), new Object[] {});
	}

	/**
	 * .
	 * 
	 * @param cpTestProgName
	 *            String
	 * @return CpTestProgramTo
	 */
	public CpTestProgramTo find(final String cpTestProgName) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_CP_TEST_PROGRAM where CP_TEST_PROG_NAME = ?";
		GenericRowMapper<CpTestProgramTo> rm = new GenericRowMapper<CpTestProgramTo>(
				CpTestProgramTo.class);
		List<CpTestProgramTo> result = sjt.query(sql, rm,
				new Object[] { cpTestProgName });
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	/**
	 * .
	 * 
	 * @param cpMaterialNum
	 *            String
	 * @return CpTestProgramTo Add For Hank 2007/12/19
	 */
	public CpTestProgramTo findByCpMaterialNum(final String cpMaterialNum) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_CP_TEST_PROGRAM where CP_MATERIAL_NUM = ? and MULTIPLE_STAGE='1' ";
		GenericRowMapper<CpTestProgramTo> rm = new GenericRowMapper<CpTestProgramTo>(
				CpTestProgramTo.class);
		List<CpTestProgramTo> result = sjt.query(sql, rm,
				new Object[] { cpMaterialNum });
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	/**
	 * .
	 * 
	 * @return List
	 */
	public List<String> findCpTestProgName() {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select distinct CP_TEST_PROG_NAME from PIDB_CP_TEST_PROGRAM order by CP_TEST_PROG_NAME";
		logger.debug(sql);
		List<Map<String, Object>> result = sjt.queryForList(sql,
				new Object[] {});
		List<String> cpTestProgRevision = new ArrayList<String>();
		if (result != null && result.size() > 0) {
			for (Map<String, Object> item : result) {
				cpTestProgRevision.add((String) item.get("CP_TEST_PROG_NAME"));
			}
		}
		return cpTestProgRevision;
	}

	/**
	 * .
	 * 
	 * @return List
	 */
	public List<String> findCpTestProgRevision() {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select distinct CP_TEST_PROG_REVISION from PIDB_CP_TEST_PROGRAM where "
				+ getAssertEmptyString("CP_TEST_PROG_REVISION")
				+ " order by CP_TEST_PROG_REVISION";
		logger.debug(sql);
		List<Map<String, Object>> result = sjt.queryForList(sql,
				new Object[] {});
		List<String> cpTestProgRevision = new ArrayList<String>();
		if (result != null && result.size() > 0) {
			for (Map<String, Object> item : result) {
				cpTestProgRevision.add((String) item
						.get("CP_TEST_PROG_REVISION"));
			}
		}
		return cpTestProgRevision;
	}

	/**
	 * .
	 * 
	 * @param cpTestProgName
	 *            String
	 * @return CpTestProgramTo
	 */
	public CpTestProgramTo findByCpTestProgName(final String cpTestProgName) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_CP_TEST_PROGRAM where CP_TEST_PROG_NAME = ?";
		GenericRowMapper<CpTestProgramTo> rm = new GenericRowMapper<CpTestProgramTo>(
				CpTestProgramTo.class);
		List<CpTestProgramTo> result = sjt.query(sql, rm,
				new Object[] { cpTestProgName });

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
	 *            CpTestProgramQueryTo
	 * @return int
	 */
	public int countResult(final CpTestProgramQueryTo queryTo) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "";
		sql = " select count(*) from("
				+ " select distinct"
				+ " p.CP_TEST_PROG_NAME,"
				+ " p.PROD_CODE,"
				+ " p.PROJ_CODE,"
				+ " p.PROD_NAME,"
				+ " p.PROJ_CODE_W_VERSION,"
				+ " p.CP_TEST_PROG_REVISION,"
				+ " p.VENDOR_CODE,"
				+ " pd.PROJ_NAME,"
				+ " pd.PROJ_OPTION,"
				+ " pj.FAB,"
				+ " pb.FAB_DESCR"
				+ " from PIDB_CP_TEST_PROGRAM p left join (PIDB_PROJECT_CODE pd left"
				+ " join PIDB_PROJECT pj on pd.PROJ_NAME=pj.PROJ_NAME) on p.PROJ_CODE = pd.PROJ_CODE ,T_FAB_CODE pb where pj.FAB = pb.FAB ";

		// 將smartSearch的值轉換成資料庫所存的值
		if (queryTo.getVendorCode() != null
				&& !queryTo.getVendorCode().equals("")) {
			SapMasterVendorDao sapMasterVendorDao = new SapMasterVendorDao();
			String vendor = queryTo.getVendorCode();
			String realVendor = "";
			if (vendor != null && vendor.length() > 0) {
				String custs = vendor.trim();
				if (custs != null) {
					SapMasterVendorTo to = sapMasterVendorDao
							.findByShortName(custs);
					if (to != null) {
						realVendor = to.getVendorCode();

					}
				}
			}
			sql += " and p.VENDOR_CODE='" + realVendor + "'";
		}

		String whereCause = generateWhereCause(queryTo);
		sql += whereCause + ")";
		return sjt.queryForInt(sql, new Object[] {});
	}

	/**
	 * .
	 * 
	 * @param queryTo
	 *            CpTestProgramQueryTo
	 * @return List
	 */
	public List<CpTestProgramQueryTo> query(final CpTestProgramQueryTo queryTo) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select distinct" 
			+ " p.CP_TEST_PROG_NAME," 
			+ " p.PROD_CODE,"
			+ " p.PROJ_CODE," 
			+ " p.PROD_NAME," 
			+ " p.PROJ_CODE_W_VERSION,"
			+ " w.MATERIAL_NUM,"
			+ " w.MATERIAL_DESC," 
			+ " p.CP_TEST_PROG_REVISION,"
			+ " p.CP_MATERIAL_NUM," 
			+ " p.TESTER," 
			+ " p.MULTIPLE_STAGE,"
			+ " p.REMARK,"
			+ " p.CP_TEST_PROG_RELEASE_DATE,"
			+ " p.CP_CPU_TIME,"
			+ " p.CP_INDEX_TIME,"
			+ " p.CONTACT_DIE_QTY,"
			+ " p.TESTER_CONFIG,"
			+ " p.FIRST_CP_TEST_HOUSE,"
			+ " p.ASSIGN_TO,"
			+ " p.ASSIGN_EMAIL,"
			+ " p.CREATED_BY,"
			+ " p.MODIFIED_BY,"
			+ " P.VENDOR_CODE," 
			+ " pd.PROJ_NAME," 
			+ " pd.PROJ_OPTION,"
			+ " pj.FAB," 
			+ " pb.FAB_DESCR"
			+ " from PIDB_CP_TEST_PROGRAM p," 
			+ " PIDB_PROJECT_CODE pd,"
			+ " PIDB_PROJECT pj," 
			+ " T_FAB_CODE pb,PIDB_IC_WAFER w"
			+ " where pj.FAB = pb.FAB " 
			+ " and pd.PROJ_NAME=pj.PROJ_NAME"
			+ " and p.PROJ_CODE = pd.PROJ_CODE"
			+ " and p.proj_code_w_version = w.proj_code_w_version(+)"
			+ " and p.CP_MATERIAL_NUM = 'C'||SUBSTR(w.MATERIAL_NUM(+),2, LENGTH(w.MATERIAL_NUM(+)))";

		// 將smartSearch的值轉換成資料庫所存的值
		if (queryTo.getVendorCode() != null
				&& !queryTo.getVendorCode().equals("")) {
			SapMasterVendorDao sapMasterVendorDao = new SapMasterVendorDao();
			String vendor = queryTo.getVendorCode();
			String realVendor = "";
			if (vendor != null && vendor.length() > 0) {
				String custs = vendor.trim();
				if (custs != null) {
					SapMasterVendorTo to = sapMasterVendorDao
							.findByShortName(custs);
					if (to != null) {
						realVendor = to.getVendorCode();

					}
				}
			}
			sql += " and p.VENDOR_CODE='" + realVendor + "'";
		}
		String whereCause = generateWhereCause(queryTo);
		sql += whereCause;
		sql += " order by p.PROJ_CODE_W_VERSION";

		GenericRowMapper<CpTestProgramQueryTo> rm = new GenericRowMapper<CpTestProgramQueryTo>(
				CpTestProgramQueryTo.class);
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
	 *            CpTestProgramQueryTo
	 * @return List
	 */
	public List<CpTestProgramTo> queryTo(final CpTestProgramQueryTo queryTo) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select distinct" 
			+ " p.CP_TEST_PROG_NAME," 
			+ " p.PROD_CODE,"
			+ " p.PROJ_CODE," 
			+ " p.PROD_NAME," 
			+ " p.PROJ_CODE_W_VERSION,"
			+ " w.MATERIAL_NUM,"
			+ " w.MATERIAL_DESC," 
			+ " p.CP_TEST_PROG_REVISION,"
			+ " p.CP_MATERIAL_NUM," 
			+ " p.TESTER," 
			+ " p.MULTIPLE_STAGE,"
			+ " p.REMARK,"
			+ " p.CP_TEST_PROG_RELEASE_DATE,"
			+ " p.CP_CPU_TIME,"
			+ " p.CP_INDEX_TIME,"
			+ " p.CONTACT_DIE_QTY,"
			+ " p.TESTER_CONFIG,"
			+ " p.FIRST_CP_TEST_HOUSE,"
			+ " p.ASSIGN_TO,"
			+ " p.ASSIGN_EMAIL,"
			+ " p.CREATED_BY,"
			+ " p.MODIFIED_BY,"
			+ " P.VENDOR_CODE," 
			+ " pd.PROJ_NAME," 
			+ " pd.PROJ_OPTION,"
			+ " pj.FAB," 
			+ " pb.FAB_DESCR"
			+ " from PIDB_CP_TEST_PROGRAM p," 
			+ " PIDB_PROJECT_CODE pd,"
			+ " PIDB_PROJECT pj," 
			+ " T_FAB_CODE pb,PIDB_IC_WAFER w"
			+ " where pj.FAB = pb.FAB " 
			+ " and pd.PROJ_NAME=pj.PROJ_NAME"
			+ " and p.PROJ_CODE = pd.PROJ_CODE"
			+ " and p.proj_code_w_version = w.proj_code_w_version(+)"
			+ " and p.CP_MATERIAL_NUM = 'C'||SUBSTR(w.MATERIAL_NUM(+),2, LENGTH(w.MATERIAL_NUM(+)))";
			

		// 將smartSearch的值轉換成資料庫所存的值
		if (queryTo.getVendorCode() != null
				&& !queryTo.getVendorCode().equals("")) {
			SapMasterVendorDao sapMasterVendorDao = new SapMasterVendorDao();
			String vendor = queryTo.getVendorCode();
			String realVendor = "";
			if (vendor != null && vendor.length() > 0) {
				String custs = vendor.trim();
				if (custs != null) {
					SapMasterVendorTo to = sapMasterVendorDao
							.findByShortName(custs);
					if (to != null) {
						realVendor = to.getVendorCode();

					}
				}
			}
			sql += " and p.VENDOR_CODE='" + realVendor + "'";
		}
		String whereCause = generateWhereCause(queryTo);
		sql += whereCause;
		sql += " order by p.PROJ_CODE_W_VERSION";

		GenericRowMapper<CpTestProgramTo> rm = new GenericRowMapper<CpTestProgramTo>(
				CpTestProgramTo.class);
		List<CpTestProgramTo> result = null;
		SapMasterVendorDao sapMasterVendorDao = new SapMasterVendorDao();
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
			for (CpTestProgramTo t : result) {
				if (t.getVendorCode() == null) {
					t.setVendorCode("");
				} else if (!t.getVendorCode().equals("")
						&& t.getVendorCode() != null) {
					SapMasterVendorTo to = sapMasterVendorDao
							.findByVendorCode(t.getVendorCode());
					t.setVendorCode(to.getShortName());
				}
			}
		}
		return result;
	}

	/**
	 * .
	 * 
	 * @param queryTo
	 *            CpTestProgramQueryTo
	 * @return String
	 */
	private String generateWhereCause(final CpTestProgramQueryTo queryTo) {
		StringBuilder sb = new StringBuilder();

		if (queryTo.getProdCode() != null && !queryTo.getProdCode().equals("")) {
			String queryString = getSmartSearchQueryString("p.prod_code",
					queryTo.getProdCode());
			if (queryString != null) {
				sb.append(" and (" + queryString + " )");
			}
		}
		if (queryTo.getProdName() != null && !queryTo.getProdName().equals("")) {
			String queryString = getSmartSearchQueryString("p.prod_name",
					queryTo.getProdName());
			if (queryString != null) {
				sb.append(" and (" + queryString + " )");
			}
		}
		if (queryTo.getProjName() != null
				&& !"".equals(queryTo.getProjName().trim())) {
			String queryString = getSmartSearchQueryString("pd.PROJ_NAME",
					queryTo.getProjName());
			if (queryString != null) {
				sb.append(" and (" + queryString + " )");
			}
		}

		if (queryTo.getFab() != null && !queryTo.getFab().equals("")) {
			sb.append(" and pj.FAB = " + getSQLString(queryTo.getFab()) + " ");
		}

		if (queryTo.getProjOption() != null
				&& !queryTo.getProjOption().equals("")) {
			String queryString = getSmartSearchQueryString("pd.PROJ_OPTION",
					queryTo.getProjOption());
			if (queryString != null) {
				sb.append(" and (" + queryString + " )");
			}
		}

		if (queryTo.getCpTestProgName() != null
				&& !queryTo.getCpTestProgName().equals("")) {
			String queryString = getSmartSearchQueryString(
					"p.CP_TEST_PROG_NAME", queryTo.getCpTestProgName());
			if (queryString != null) {
				sb.append(" and (" + queryString + " )");
			}
		}

		if (queryTo.getCpMaterialNum() != null
				&& !queryTo.getCpMaterialNum().equals("")) {
			String queryString = getSmartSearchQueryString("p.CP_MATERIAL_NUM",
					queryTo.getCpMaterialNum());
			if (queryString != null) {
				sb.append(" and (" + queryString + " )");
			}
		}

		if (queryTo.getCpTestProgRevision() != null
				&& !queryTo.getCpTestProgRevision().equals("")) {
			String queryString = getSmartSearchQueryString(
					"p.CP_TEST_PROG_REVISION", queryTo.getCpTestProgRevision());
			if (queryString != null) {
				sb.append(" and (" + queryString + " )");
			}
		}

		if (queryTo.getProjCodeWVersion() != null
				&& !queryTo.getProjCodeWVersion().equals("")) {
			String queryString = getSmartSearchQueryString(
					"p.PROJ_CODE_W_VERSION", queryTo.getProjCodeWVersion());
			if (queryString != null) {
				sb.append(" and (" + queryString + " )");
			}
		}

		return sb.toString();
	}

	/**
	 * .
	 * 
	 * @param prodCode
	 *            String
	 * @return List
	 */
	public List<Map<String, Object>> findBy(final String prodCode) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select PKG_CODE from PIDB_CP_TEST_PROGRAM where PROD_CODE=?";
		return sjt.queryForList(sql, new Object[] { prodCode });
	}

	/*
	 * public CpTestProgramTo findByPrimaryKey(final String cpTestProgName,
	 * final String projCodeWVersion) { SimpleJdbcTemplate sjt =
	 * getSimpleJdbcTemplate(); String sql = "select * from PIDB_CP_TEST_PROGRAM
	 * where CP_TEST_PROG_NAME = ? and PROJ_CODE_W_VERSION = ?";
	 * GenericRowMapper<CpTestProgramTo> rm = new GenericRowMapper<CpTestProgramTo>(CpTestProgramTo.class);
	 * List<CpTestProgramTo> result = sjt.query(sql, rm, new
	 * Object[]{cpTestProgName,projCodeWVersion}); if (result != null &&
	 * result.size() > 0) { return result.get(0); } else { return null; } }
	 */

	public CpTestProgramTo findByPrimaryKey(final String cpTestProgName,
			final String projCodeWVersion, final String cpMaterialNum) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select distinct" 
			+ " p.CP_TEST_PROG_NAME," 
			+ " p.PROD_CODE,"
			+ " p.PROJ_CODE," 
			+ " p.PROD_NAME," 
			+ " p.PROJ_CODE_W_VERSION,"
			+ " w.MATERIAL_NUM,"
			+ " w.MATERIAL_DESC," 
			+ " p.CP_TEST_PROG_REVISION,"
			+ " p.CP_MATERIAL_NUM," 
			+ " p.TESTER," 
			+ " p.MULTIPLE_STAGE,"
			+ " p.REMARK,"
			+ " p.CP_TEST_PROG_RELEASE_DATE,"
			+ " p.CP_CPU_TIME,"
			+ " p.CP_INDEX_TIME,"
			+ " p.CONTACT_DIE_QTY,"
			+ " p.TESTER_CONFIG,"
			+ " p.FIRST_CP_TEST_HOUSE,"
			+ " p.ASSIGN_TO,"
			+ " p.ASSIGN_EMAIL,"
			+ " p.CREATED_BY,"
			+ " p.MODIFIED_BY,"
			+ " P.VENDOR_CODE," 
			+ " pd.PROJ_NAME," 
			+ " pd.PROJ_OPTION,"
			+ " pj.FAB," 
			+ " pb.FAB_DESCR"
			+ " from PIDB_CP_TEST_PROGRAM p," 
			+ " PIDB_PROJECT_CODE pd,"
			+ " PIDB_PROJECT pj," 
			+ " T_FAB_CODE pb,PIDB_IC_WAFER w"
			+ " where pj.FAB = pb.FAB " 
			+ " and pd.PROJ_NAME=pj.PROJ_NAME"
			+ " and p.PROJ_CODE = pd.PROJ_CODE"
			+ " and p.proj_code_w_version = w.proj_code_w_version(+)"
			+ " and p.CP_MATERIAL_NUM = 'C'||SUBSTR(w.MATERIAL_NUM(+),2, LENGTH(w.MATERIAL_NUM(+)))"
			+ " and p.CP_TEST_PROG_NAME =?"
			+ " and p.PROJ_CODE_W_VERSION =?"
			+ " and p.CP_MATERIAL_NUM =?";
		GenericRowMapper<CpTestProgramTo> rm = new GenericRowMapper<CpTestProgramTo>(
				CpTestProgramTo.class);
		List<CpTestProgramTo> result = sjt.query(sql, rm, new Object[] {
				cpTestProgName, projCodeWVersion, cpMaterialNum });
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}
	
	public CpTestProgramTo findByPrimaryKey(final String cpTestProgName,
			final String projCodeWVersion, final String cpMaterialNum,final String materialNum) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select distinct" 
			+ " p.CP_TEST_PROG_NAME," 
			+ " p.PROD_CODE,"
			+ " p.PROJ_CODE," 
			+ " p.PROD_NAME," 
			+ " p.PROJ_CODE_W_VERSION,"
			+ " w.MATERIAL_NUM,"
			+ " w.MATERIAL_DESC," 
			+ " p.CP_TEST_PROG_REVISION,"
			+ " p.CP_MATERIAL_NUM," 
			+ " p.TESTER," 
			+ " p.MULTIPLE_STAGE,"
			+ " p.REMARK,"
			+ " p.CP_TEST_PROG_RELEASE_DATE,"
			+ " p.CP_CPU_TIME,"
			+ " p.CP_INDEX_TIME,"
			+ " p.CONTACT_DIE_QTY,"
			+ " p.TESTER_CONFIG,"
			+ " p.FIRST_CP_TEST_HOUSE,"
			+ " p.ASSIGN_TO,"
			+ " p.ASSIGN_EMAIL,"
			+ " p.CREATED_BY,"
			+ " p.MODIFIED_BY,"
			+ " P.VENDOR_CODE," 
			+ " pd.PROJ_NAME," 
			+ " pd.PROJ_OPTION,"
			+ " pj.FAB," 
			+ " pb.FAB_DESCR"
			+ " from PIDB_CP_TEST_PROGRAM p," 
			+ " PIDB_PROJECT_CODE pd,"
			+ " PIDB_PROJECT pj," 
			+ " T_FAB_CODE pb,PIDB_IC_WAFER w"
			+ " where pj.FAB = pb.FAB " 
			+ " and pd.PROJ_NAME=pj.PROJ_NAME"
			+ " and p.PROJ_CODE = pd.PROJ_CODE"
			+ " and p.proj_code_w_version = w.proj_code_w_version(+)"
			+ " and p.CP_MATERIAL_NUM = 'C'||SUBSTR(w.MATERIAL_NUM(+),2, LENGTH(w.MATERIAL_NUM(+)))"
			+ " and p.CP_TEST_PROG_NAME =?"
			+ " and p.PROJ_CODE_W_VERSION =?"
			+ " and p.CP_MATERIAL_NUM =?"
			+ " and w.MATERIAL_NUM=?";
		GenericRowMapper<CpTestProgramTo> rm = new GenericRowMapper<CpTestProgramTo>(
				CpTestProgramTo.class);
		List<CpTestProgramTo> result = sjt.query(sql, rm, new Object[] {
				cpTestProgName, projCodeWVersion, cpMaterialNum, materialNum});
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	/**
	 * .
	 * 
	 * @return List
	 */
	public List<String> findTestProgNameByKey(final String projCodeWVersion,
			final String cpMaterialNum) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_CP_TEST_PROGRAM where PROJ_CODE_W_VERSION = ? and CP_MATERIAL_NUM = ? and MULTIPLE_STAGE='1' ";
		logger.debug(sql);

		List<Map<String, Object>> result = sjt.queryForList(sql, new Object[] {
				projCodeWVersion, cpMaterialNum });
		List<String> cpTestProgName = new ArrayList<String>();
		if (result != null && result.size() > 0) {
			for (Map<String, Object> item : result) {
				cpTestProgName.add((String) item.get("CP_TEST_PROG_NAME"));
			}
		}
		return cpTestProgName;
	}

	public void insertCpTest(final CpTestProgramTo newInstance) {
		if (null == newInstance) {
			logger.error("Error the new IcWafer object is null.");
		}
		TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(
					final TransactionStatus status) {

				SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();

				String sql = "insert into PIDB_CP_TEST_PROGRAM "
						+ "(PROJ_CODE," + " PROD_CODE," + " PROD_NAME,"
						+ " PROJ_CODE_W_VERSION," + " CP_TEST_PROG_NAME,"
						+ " CP_TEST_PROG_REVISION,"
						+ " CP_TEST_PROG_RELEASE_DATE," + " CP_CPU_TIME,"
						+ " CP_INDEX_TIME," + " CONTACT_DIE_QTY," + " TESTER,"
						+ " TESTER_CONFIG," + " FIRST_CP_TEST_HOUSE,"
						+ " REMARK," + " ASSIGN_TO," + " ASSIGN_EMAIL,"
						+ " CREATED_BY," + " MODIFIED_BY,"
						+ " CP_MATERIAL_NUM," + " MULTIPLE_STAGE,"
						+ " VENDOR_CODE)"
						+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				logger.debug(sql);
				sjt.update(sql, newInstance.getProjCode(), newInstance
						.getProdCode(), newInstance.getProdName(), newInstance
						.getProjCodeWVersion(),
						newInstance.getCpTestProgName(), newInstance
								.getCpTestProgRevision(), newInstance
								.getCpTestProgReleaseDate(), newInstance
								.getCpCpuTime(), newInstance.getCpIndexTime(),
						newInstance.getContactDieQty(),
						newInstance.getTester(), newInstance.getTesterConfig(),
						newInstance.getFirstCpTestHouse(), newInstance
								.getRemark(), newInstance.getAssignTo(),
						newInstance.getAssignEmail(), newInstance
								.getCreatedBy(), newInstance.getModifiedBy(),
						newInstance.getCpMaterialNum(), newInstance
								.getMultipleStage(), newInstance
								.getVendorCode());

			}
		};
		doInTransaction(callback);
	}

	public void updateCpTest(final CpTestProgramTo newInstance) {
		if (newInstance == null) {
			logger.error("Error the parameter is null.");
		}

		TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(
					final TransactionStatus status) {
				SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
				String sql = "update PIDB_CP_TEST_PROGRAM set "
						+ "PROJ_CODE=?,"
						+ "PROD_CODE=?,"
						+ "PROD_NAME=?,"
						+ "CP_TEST_PROG_REVISION=?,"
						+ "CP_TEST_PROG_RELEASE_DATE=?,"
						+ "CP_CPU_TIME=?,"
						+ "CP_INDEX_TIME=?,"
						+ "CONTACT_DIE_QTY=?,"
						+ "TESTER=?,"
						+ "TESTER_CONFIG=?,"
						+ "FIRST_CP_TEST_HOUSE=?,"
						+ "REMARK=?,"
						+ "ASSIGN_TO=?,"
						+ "ASSIGN_EMAIL=?,"
						+ "CREATED_BY=?,"
						+ "MODIFIED_BY=?,"
						+ "MULTIPLE_STAGE=?,"
						+ "VENDOR_CODE=?"
						+ "where CP_MATERIAL_NUM=? and PROJ_CODE_W_VERSION =? and CP_TEST_PROG_NAME=?";
				logger.debug(sql);
				sjt.update(sql, newInstance.getProjCode(), newInstance
						.getProdCode(), newInstance.getProdName(), newInstance
						.getCpTestProgRevision(), newInstance
						.getCpTestProgReleaseDate(),
						newInstance.getCpCpuTime(), newInstance
								.getCpIndexTime(), newInstance
								.getContactDieQty(), newInstance.getTester(),
						newInstance.getTesterConfig(), newInstance
								.getFirstCpTestHouse(),
						newInstance.getRemark(), newInstance.getAssignTo(),
						newInstance.getAssignEmail(), newInstance
								.getCreatedBy(), newInstance.getModifiedBy(),
						newInstance.getMultipleStage(), newInstance
								.getVendorCode(), newInstance
								.getCpMaterialNum(), newInstance
								.getProjCodeWVersion(), newInstance
								.getCpTestProgName());
			}
		};
		doInTransaction(callback);
	}

}
