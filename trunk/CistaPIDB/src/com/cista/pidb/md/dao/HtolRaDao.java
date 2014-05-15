package com.cista.pidb.md.dao;

import java.util.List;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.cista.pidb.admin.to.ParameterTo;
import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.md.to.HtolRaQueryTo;
import com.cista.pidb.md.to.HtolRaTo;

public class HtolRaDao extends PIDBDaoSupport {

	public int countResult(HtolRaQueryTo queryTo) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select count(*) from PIDB_HTOL_RA phr, PIDB_PRODUCT ppd, PIDB_PROJECT ppj, PIDB_PROJECT_CODE ppjc, PIDB_IC_WAFER piw where 1=1  and piw.PROJ_CODE=ppjc.PROJ_CODE and ppjc.PROJ_NAME=ppj.PROJ_NAME and phr.PROD_CODE(+)=ppd.PROD_CODE "
				+ " and phr.PROJ_CODE_W_VERSION=piw.PROJ_CODE_W_VERSION";
		/*
		 * if (queryTo.getOwner() != null && !queryTo.getOwner().equals("")) { //
		 * 將smartSearch的值轉換成資料庫所存的值 String field = queryTo.getOwner(); String
		 * realOwner = ""; if (field != null && !field.equals("")) { String[]
		 * fields = field.split(","); if (fields != null && fields.length > 0) {
		 * for (String s : fields) { ParameterTo to = findByShortName(s); if (to !=
		 * null) { realOwner += to.getFieldValue(); } } } } sql += " and
		 * phr.OWNER='" + realOwner + "'";
		 *  }
		 */

		String whereCause = generateWhereCause(queryTo);
		sql += whereCause;
		logger.debug(sql);
		return sjt.queryForInt(sql, new Object[] {});
	}

	public List<HtolRaQueryTo> query(final HtolRaQueryTo queryTo) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select distinct "
				+ "ppd.PROD_NAME,"
				+ "ppj.PROJ_NAME,"
				+ "ppj.FAB,"
				+ "ppjc.PROJ_OPTION,"
				+ "phr.PROJ_CODE_W_VERSION, "
				+ "phr.RA_TEST_RESULT,"
				+ "phr.OWNER,"
				+ "phr.RA_TEST_ITEM "
				+ "from PIDB_HTOL_RA phr, PIDB_PRODUCT ppd, PIDB_PROJECT ppj, PIDB_PROJECT_CODE ppjc, PIDB_IC_WAFER piw where 1=1  and piw.PROJ_CODE=ppjc.PROJ_CODE and ppjc.PROJ_NAME=ppj.PROJ_NAME and phr.PROD_CODE=ppd.PROD_CODE "
				+ " and phr.PROJ_CODE_W_VERSION(+)=piw.PROJ_CODE_W_VERSION";

		/*
		 * if (queryTo.getOwner() != null && !queryTo.getOwner().equals("")) { //
		 * 將smartSearch的值轉換成資料庫所存的值 String field = queryTo.getOwner(); String
		 * realOwner = ""; if (field != null && !field.equals("")) { String[]
		 * fields = field.split(","); if (fields != null && fields.length > 0) {
		 * for (String s : fields) { ParameterTo to = findByShortName(s); if (to !=
		 * null) { realOwner += to.getFieldValue(); } } } } sql += " and
		 * phr.OWNER='" + realOwner + "'";
		 *  }
		 */
		String whereCause = generateWhereCause(queryTo);
		sql += whereCause;
		sql += " order by phr.PROJ_CODE_W_VERSION";
		// System.out.println(sql);
		logger.debug(sql);

		GenericRowMapper<HtolRaQueryTo> rm = new GenericRowMapper<HtolRaQueryTo>(
				HtolRaQueryTo.class);

		List<HtolRaQueryTo> result = null;
		if (queryTo.getPageNo() > 0) {

			int cursorFrom = (queryTo.getPageNo() - 1) * queryTo.getPageSize()
					+ 1;
			int cursorTo = (queryTo.getPageNo()) * queryTo.getPageSize();
			result = sjt.query(getPagingSql(sql, cursorFrom, cursorTo), rm,
					new Object[] {});
		} else {

			result = sjt.query(sql, rm, new Object[] {});

		}
		/*
		 * if (result != null && result.size() > 0) { for (HtolRaQueryTo t :
		 * result) { if (!t.getOwner().equals("") && t.getOwner() != null) {
		 * ParameterTo to = findByFieldValue(t.getOwner());
		 * t.setOwner(to.getFieldShowName()); }else if (t.getOwner() == null){
		 * t.setOwner(""); } } }
		 */
		return result;
	}

	public List<HtolRaTo> queryForDomain(final HtolRaQueryTo queryTo) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select distinct "
				+ "phr.* "
				+ "from PIDB_HTOL_RA phr, PIDB_PRODUCT ppd, PIDB_PROJECT ppj, PIDB_PROJECT_CODE ppjc, PIDB_IC_WAFER piw where 1=1  and piw.PROJ_CODE=ppjc.PROJ_CODE and ppjc.PROJ_NAME=ppj.PROJ_NAME and phr.PROD_CODE=ppd.PROD_CODE "
				+ " and phr.PROJ_CODE_W_VERSION(+)=piw.PROJ_CODE_W_VERSION";

		/*
		 * if (queryTo.getOwner() != null && !queryTo.getOwner().equals("")) { //
		 * 將smartSearch的值轉換成資料庫所存的值 String field = queryTo.getOwner(); String
		 * realOwner = ""; if (field != null && !field.equals("")) { String[]
		 * fields = field.split(","); if (fields != null && fields.length > 0) {
		 * for (String s : fields) { ParameterTo to = findByShortName(s); if (to !=
		 * null) { realOwner += to.getFieldValue(); } } } } sql += " and
		 * phr.OWNER='" + realOwner + "'"; }
		 */

		String whereCause = generateWhereCause(queryTo);
		sql += whereCause;
		sql += " order by phr.PROJ_CODE_W_VERSION";
		System.out.println(sql);
		logger.debug(sql);

		GenericRowMapper<HtolRaTo> rm = new GenericRowMapper<HtolRaTo>(
				HtolRaTo.class);

		List<HtolRaTo> result = null;
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
			for (HtolRaTo t : result) {
				if (t.getOwner() == null) {
					t.setOwner("");
				} else if (!t.getOwner().equals("") && t.getOwner() != null) {
					ParameterTo to = findByFieldValue(t.getOwner());
					t.setOwner(to.getFieldShowName());
				}
			}
		}
		return result;
	}

	// 取得 field_value For insert to DataBase

	public ParameterTo findByShortName(String shortName) {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_FUN_PARAMETER_VALUE where FUN_NAME='RA'"
				+ " AND FUN_FIELD_NAME='OWNER' AND FIELD_SHOW_NAME=?";
		logger.debug(sql);
		List<ParameterTo> result = stj.query(sql,
				new GenericRowMapper<ParameterTo>(ParameterTo.class),
				new Object[] { shortName });

		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	public ParameterTo findByFieldValue(String fieldShowName) {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_FUN_PARAMETER_VALUE where FUN_NAME='RA'"
				+ " AND FUN_FIELD_NAME='OWNER' AND FIELD_Value=?";
		logger.debug(sql);
		List<ParameterTo> result = stj.query(sql,
				new GenericRowMapper<ParameterTo>(ParameterTo.class),
				new Object[] { fieldShowName });

		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	private String generateWhereCause(final HtolRaQueryTo queryTo) {
		StringBuilder sb = new StringBuilder();

		if (queryTo.getProdName() != null && !queryTo.getProdName().equals("")) {
			String prodName = getSmartSearchQueryString("ppd.PROD_NAME",
					queryTo.getProdName());
			if (prodName != null) {
				sb.append(" and (" + prodName + " )");
			}
		}

		if (queryTo.getProjName() != null && !queryTo.getProjName().equals("")) {
			String projName = getSmartSearchQueryLike(
					"phr.PROJ_CODE_W_VERSION", queryTo.getProjName());
			if (projName != null) {
				sb.append(" and (" + projName + " )");
			}
		}

		if (queryTo.getProjCodeWVersion() != null
				&& !queryTo.getProjCodeWVersion().equals("")) {
			String projCodeWVersion = getSmartSearchQueryString(
					"phr.PROJ_CODE_W_VERSION", queryTo.getProjCodeWVersion());
			if (projCodeWVersion != null) {
				sb.append(" and (" + projCodeWVersion + " )");
			}
		}

		if (queryTo.getRaTestResult() != null
				&& !queryTo.getRaTestResult().equals("")) {
			String raTestResult = getSmartSearchQueryString(
					" phr.RA_TEST_RESULT", queryTo.getRaTestResult());
			if (raTestResult != null) {
				sb.append(" and (" + raTestResult + " )");
			}
		}

		if (queryTo.getOwner() != null && !queryTo.getOwner().equals("")) {
			String owner = getSmartSearchQueryLike("phr.OWNER", queryTo
					.getOwner());
			if (owner != null) {
				sb.append(" and (" + owner + " )");
			}
		}

		return sb.toString();
	}

	public List<HtolRaTo> findProjCodeWVersion() {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select distinct PROJ_CODE_W_VERSION from PIDB_HTOL_RA where "
				+ getAssertEmptyString("PROJ_CODE_W_VERSION")
				+ " order by PROJ_CODE_W_VERSION";
		logger.debug(sql);
		List<HtolRaTo> result = stj.query(sql, new GenericRowMapper<HtolRaTo>(
				HtolRaTo.class), new Object[] {});
		return result;
	}

	public HtolRaTo findByProjCodeWVersion(String projCodeWVersion) {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_HTOL_RA where PROJ_CODE_W_VERSION=?";
		logger.debug(sql);
		List<HtolRaTo> result = stj.query(sql, new GenericRowMapper<HtolRaTo>(
				HtolRaTo.class), new Object[] { projCodeWVersion });
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}

	}

	public HtolRaTo findByProjCodeWVersion(String projCodeWVersion,
			String raTestItem) {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_HTOL_RA where PROJ_CODE_W_VERSION=? And RA_TEST_ITEM=?";
		logger.debug(sql);
		List<HtolRaTo> result = stj.query(sql, new GenericRowMapper<HtolRaTo>(
				HtolRaTo.class), new Object[] { projCodeWVersion, raTestItem });
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}

	}
}
