package com.cista.pidb.md.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.md.to.CogQueryTo;
import com.cista.pidb.md.to.CogTo;

/**
 * Data access object for table PIDB_COG.
 * 
 * @author Hu Meixia
 */
public class CogDao extends PIDBDaoSupport {

	/**
	 * Find all cogs in the table.
	 * 
	 * @return List of CogTo
	 */
	public List<CogTo> findAll() {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_COG order by PROD_CODE";
		logger.debug(sql);
		return sjt.query(sql, new GenericRowMapper<CogTo>(CogTo.class),
				new Object[] {});
	}

	/**
	 * Find all cogs in the table.
	 * 
	 * @return List of CogTo
	 */
	public List<String> findTrayDrawingNo(String trayDrawingNo) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "";
		if (trayDrawingNo != null && trayDrawingNo.length() > 0) {
			sql = "select distinct TRAY_DRAWING_NO from ("
					+ " select TRAY_DRAWING_NO1 as TRAY_DRAWING_NO from PIDB_COG where "
					+ super.getSmartSearchQueryString("TRAY_DRAWING_NO1",
							trayDrawingNo)
					+ " union "
					+ " select TRAY_DRAWING_NO2 as TRAY_DRAWING_NO from PIDB_COG where "
					+ super.getSmartSearchQueryString("TRAY_DRAWING_NO2",
							trayDrawingNo)
					+ " union "
					+ " select TRAY_DRAWING_NO3 as TRAY_DRAWING_NO from PIDB_COG where "
					+ super.getSmartSearchQueryString("TRAY_DRAWING_NO3",
							trayDrawingNo)
					+ " union "
					+ " select TRAY_DRAWING_NO4 as TRAY_DRAWING_NO from PIDB_COG where "
					+ super.getSmartSearchQueryString("TRAY_DRAWING_NO4",
							trayDrawingNo)
					+ " union "
					+ " select TRAY_DRAWING_NO5 as TRAY_DRAWING_NO from PIDB_COG where "
					+ super.getSmartSearchQueryString("TRAY_DRAWING_NO5",
							trayDrawingNo)
					+ " union "
					+ " select TRAY_DRAWING_NO6 as TRAY_DRAWING_NO from PIDB_COG where "
					+ super.getSmartSearchQueryString("TRAY_DRAWING_NO6",
							trayDrawingNo) + ") order by TRAY_DRAWING_NO";
		} else {
			sql = "select distinct TRAY_DRAWING_NO from ("
					+ " select TRAY_DRAWING_NO1 as TRAY_DRAWING_NO from PIDB_COG"
					+ " union "
					+ " select TRAY_DRAWING_NO2 as TRAY_DRAWING_NO from PIDB_COG"
					+ " union "
					+ " select TRAY_DRAWING_NO3 as TRAY_DRAWING_NO from PIDB_COG"
					+ " union "
					+ " select TRAY_DRAWING_NO4 as TRAY_DRAWING_NO from PIDB_COG"
					+ " union "
					+ " select TRAY_DRAWING_NO5 as TRAY_DRAWING_NO from PIDB_COG"
					+ " union "
					+ " select TRAY_DRAWING_NO6 as TRAY_DRAWING_NO from PIDB_COG"
					+ ") order by TRAY_DRAWING_NO";
		}
		logger.debug(sql);
		List<Map<String, Object>> result = sjt.queryForList(sql,
				new Object[] {});
		List<String> retList = new ArrayList<String>();
		if (result != null && result.size() > 0) {
			for (Map<String, Object> col : result) {
				retList.add((String) col.get("TRAY_DRAWING_NO"));
				// retList.add((String) col.get("TRAY_DRAWING_NO2"));
				// retList.add((String) col.get("TRAY_DRAWING_NO3"));
				// retList.add((String) col.get("TRAY_DRAWING_NO4"));
				// retList.add((String) col.get("TRAY_DRAWING_NO5"));
				// retList.add((String) col.get("TRAY_DRAWING_NO6"));
			}
		}
		return retList;
		// return sjt.q(sql, new GenericRowMapper<CogTo>(CogTo.class),
		// new Object[] {});
	}

	/**
	 * .
	 * 
	 * @param prodCode
	 *            String
	 * @param pkgCode
	 *            String
	 * @return CogTo
	 */
	public CogTo find(final String prodCode, final String pkgCode) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_COG where 1=1";
		if (pkgCode != null && !(pkgCode.trim()).equals("")) {
			sql += " and PKG_CODE = '" + pkgCode + "'";
		}
		if (prodCode != null && !(prodCode.trim()).equals("")) {
			sql += " and PROD_CODE = '" + prodCode + "'";
		}
		GenericRowMapper<CogTo> rm = new GenericRowMapper<CogTo>(CogTo.class);
		List<CogTo> result = sjt.query(sql, rm, new Object[] {});
		if (result != null && result.size() > 0) {
			return result.get(0);
		}
		return null;
	}

	/**
	 * .
	 * 
	 * @param queryTo
	 *            CogQueryTo
	 * @return int
	 */
	public int countResult(final CogQueryTo queryTo) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "";
		if (queryTo.getProdName() == null
				|| "".equals(queryTo.getProdName().trim())) {
			sql = "select count(*) from PIDB_COG p where 1 = 1 ";
		} else {
			sql = "select count(*) from PIDB_COG p left join PIDB_PRODUCT pd on"
					+ " p.PROD_CODE = pd.PROD_CODE where 1 = 1 ";
		}
		String whereCause = generateWhereCause(queryTo);
		sql += whereCause;
		return sjt.queryForInt(sql, new Object[] {});
	}

	/**
	 * .
	 * 
	 * @param queryTo
	 *            CogQueryTo
	 * @return List
	 */
	public List<CogTo> query(final CogQueryTo queryTo) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "";
		if (queryTo.getProdName() == null
				|| "".equals(queryTo.getProdName().trim())) {
			sql = "select * from PIDB_COG p where 1 = 1 ";
		} else {
			sql = "select "
					+ "p.PROD_CODE,"
					+ "p.PKG_CODE,"
					+ "p.TRAY_SIZE,"
					+ "p.VENDOR,"
					+ "p.DRAWING_APPROVE_DATE,"
					+ "p.SAMPLE_READY_DATE,"
					+ "p.ASSY_SUBCON1,"
					+ "p.ASSY_SUBCON2,"
					+ "p.ASSY_SUBCON3,"
					+ "p.ASSY_SUBCON4,"
					+ "p.ASSY_SUBCON5,"
					+ "p.CHIP_SIZE,"
					+ "p.CAVITY_SIZE,"
					+ "p.TRAY_DRAWING_NO1,"
					+ "p.TRAY_DRAWING_NO_VER1,"
					+ "p.COLOR1,"
					+ "p.COG_CUST_NAME1,"
					+ "p.TRAY_DRAWING_NO2,"
					+ "p.TRAY_DRAWING_NO_VER2,"
					+ "p.COLOR2,"
					+ "p.COG_CUST_NAME2,"
					+ "p.TRAY_DRAWING_NO3,"
					+ "p.TRAY_DRAWING_NO_VER3,"
					+ "p.COLOR3,"
					+ "p.COG_CUST_NAME3,"
					+ "p.TRAY_DRAWING_NO4,"
					+ "p.TRAY_DRAWING_NO_VER4,"
					+ "p.COLOR4,"
					+ "p.COG_CUST_NAME4,"
					+ "p.TRAY_DRAWING_NO5,"
					+ "p.TRAY_DRAWING_NO_VER5,"
					+ "p.COLOR5,"
					+ "p.COG_CUST_NAME5,"
					+ "p.TRAY_DRAWING_NO6,"
					+ "p.TRAY_DRAWING_NO_VER6,"
					+ "p.COLOR6,"
					+ "p.COG_CUST_NAME6,"
					+ "p.REMARK,"
					+ "p.ASSIGN_TO,"
					+ "p.ASSIGN_EMAIL,"
					+ "p.CREATED_BY,"
					+ "p.MODIFIED_BY,"
					+ "pd.PROD_NAME"
					+ " from PIDB_COG p left join PIDB_PRODUCT pd on p.PROD_CODE = pd.PROD_CODE where 1 = 1 ";
		}
		String whereCause = generateWhereCause(queryTo);
		sql += whereCause;
		sql += " order by p.PROD_CODE,p.PKG_CODE";

		GenericRowMapper<CogTo> rm = new GenericRowMapper<CogTo>(CogTo.class);
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
	 *            CogQueryTo
	 * @return String
	 */
	private String generateWhereCause(final CogQueryTo queryTo) {
		StringBuilder sb = new StringBuilder();
		if (queryTo.getProdCode() != null && !queryTo.getProdCode().equals("")) {
			String queryString = getSmartSearchQueryString("p.PROD_CODE",
					queryTo.getProdCode());
			if (queryString != null) {
				sb.append(" and (" + queryString + " )");
			}
		}

		if (queryTo.getPkgCode() != null && !queryTo.getPkgCode().equals("")) {
			String queryString = getSmartSearchQueryString("p.PKG_CODE",
					queryTo.getPkgCode());
			if (queryString != null) {
				sb.append(" and (" + queryString + " )");
			}
		}

		if (queryTo.getProdName() != null
				&& !"".equals(queryTo.getProdName().trim())) {
			String queryString = getSmartSearchQueryString("pd.PROD_NAME",
					queryTo.getProdName());
			if (queryString != null) {
				sb.append(" and (" + queryString + " )");
			}
		}

		if (queryTo.getTraySize() != null && !queryTo.getTraySize().equals("")) {
			String queryString = getSmartSearchQueryString("p.TRAY_SIZE",
					queryTo.getTraySize());
			if (queryString != null) {
				sb.append(" and (" + queryString + " )");
			}
		}

		if (queryTo.getTrayDrawingNo() != null
				&& !queryTo.getTrayDrawingNo().equals("")) {
			String queryString = getSmartSearchQueryString(
					"p.TRAY_DRAWING_NO1", queryTo.getTrayDrawingNo());
			String queryString2 = getSmartSearchQueryString(
					"p.TRAY_DRAWING_NO2", queryTo.getTrayDrawingNo());
			String queryString3 = getSmartSearchQueryString(
					"p.TRAY_DRAWING_NO3", queryTo.getTrayDrawingNo());
			String queryString4 = getSmartSearchQueryString(
					"p.TRAY_DRAWING_NO4", queryTo.getTrayDrawingNo());
			String queryString5 = getSmartSearchQueryString(
					"p.TRAY_DRAWING_NO5", queryTo.getTrayDrawingNo());
			String queryString6 = getSmartSearchQueryString(
					"p.TRAY_DRAWING_NO6", queryTo.getTrayDrawingNo());
			if (queryString != null) {
				sb.append(" and ((" + queryString + " ) or (" + queryString2
						+ " ) or (" + queryString3 + " ) " + "or ("
						+ queryString4 + " ) or (" + queryString5 + " ) or ("
						+ queryString6 + "))");
			}
		}

		if (queryTo.getTrayDrawingNoVer() != null
				&& !queryTo.getTrayDrawingNoVer().equals("")) {
			String queryString = getSmartSearchQueryString(
					"p.TRAY_DRAWING_NO_VER1", queryTo.getTrayDrawingNoVer());
			String queryString2 = getSmartSearchQueryString(
					"p.TRAY_DRAWING_NO_VER2", queryTo.getTrayDrawingNoVer());
			String queryString3 = getSmartSearchQueryString(
					"p.TRAY_DRAWING_NO_VER3", queryTo.getTrayDrawingNoVer());
			String queryString4 = getSmartSearchQueryString(
					"p.TRAY_DRAWING_NO_VER4", queryTo.getTrayDrawingNoVer());
			String queryString5 = getSmartSearchQueryString(
					"p.TRAY_DRAWING_NO_VER5", queryTo.getTrayDrawingNoVer());
			String queryString6 = getSmartSearchQueryString(
					"p.TRAY_DRAWING_NO_VER6", queryTo.getTrayDrawingNoVer());
			if (queryString != null) {
				sb.append(" and ((" + queryString + " ) or (" + queryString2
						+ " ) or (" + queryString3 + " )" + " or ("
						+ queryString4 + " ) or (" + queryString5 + " ) or ("
						+ queryString6 + "))");
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
		String sql = "select Distinct PKG_CODE from PIDB_COG where PROD_CODE=? and "
				+ getAssertEmptyString("PKG_CODE") + " order by PKG_CODE";

		return sjt.queryForList(sql, new Object[] { prodCode });
	}

	/**
	 * .
	 * 
	 * @param prodCode
	 *            String
	 * @return List
	 */
	public List<String> findCustByProdCode(final String prodCode) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select COG_CUST_NAME1,COG_CUST_NAME2,COG_CUST_NAME3,COG_CUST_NAME4,COG_CUST_NAME5,COG_CUST_NAME6 from PIDB_COG where PROD_CODE=? ";

		List<Map<String, Object>> result = sjt.queryForList(sql,
				new Object[] { prodCode });
		List<String> retList = new ArrayList<String>();
		if (result != null && result.size() > 0) {
			for (Map<String, Object> col : result) {
				retList.add((String) col.get("COG_CUST_NAME1"));
				retList.add((String) col.get("COG_CUST_NAME2"));
				retList.add((String) col.get("COG_CUST_NAME3"));
				retList.add((String) col.get("COG_CUST_NAME4"));
				retList.add((String) col.get("COG_CUST_NAME5"));
				retList.add((String) col.get("COG_CUST_NAME6"));
			}
		}
		return retList;
	}

	public List<String> findAllPkgCode() {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select distinct PKG_CODE from PIDB_COG order by PKG_CODE";
		logger.debug(sql);
		List<Map<String, Object>> result = stj.queryForList(sql,
				new Object[] {});
		List<String> pkgCodes = new ArrayList<String>();
		if (result != null && result.size() > 0) {
			for (Map<String, Object> item : result) {
				pkgCodes.add((String) item.get("PKG_CODE"));
			}
		}
		return pkgCodes;
	}

	public List<String> findAllPkgCodeByPkgCode(final String pkgCode) {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select distinct PKG_CODE from PIDB_COG where "
				+ super.getSmartSearchQueryString("PKG_CODE", pkgCode)
				+ " order by PKG_CODE";
		logger.debug(sql);
		List<Map<String, Object>> result = stj.queryForList(sql,
				new Object[] {});
		List<String> pkgCodes = new ArrayList<String>();
		if (result != null && result.size() > 0) {
			for (Map<String, Object> item : result) {
				pkgCodes.add((String) item.get("PKG_CODE"));
			}
		}
		return pkgCodes;
	}

	public List<CogTo> findAllObjectByPkgCode(final String pkgCode) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_COG where PKG_CODE = ?";

		return sjt.query(sql, new GenericRowMapper<CogTo>(CogTo.class),
				new Object[] { pkgCode });
	}

	public List<CogTo> findAllCustByPkgCodeAndProdName(final String pkgCode,
			final String prodName) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_COG pc,PIDB_PRODUCT pp where pc.PROD_CODE=pp.PROD_CODE and pc.PKG_CODE = ? and pp.PROD_NAME = ?";
		logger.debug(sql);
		GenericRowMapper<CogTo> rm = new GenericRowMapper<CogTo>(CogTo.class);
		return sjt.query(sql, rm, new Object[] { pkgCode, prodName });
	}

	public CogTo findBy(final String prodCode, final String pkgCode) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_COG where PKG_CODE = ? and PROD_CODE = ?";
		GenericRowMapper<CogTo> rm = new GenericRowMapper<CogTo>(CogTo.class);
		List<CogTo> result = sjt.query(sql, rm, new Object[] { pkgCode,
				prodCode });
		if (result != null && result.size() > 0) {
			return result.get(0);
		}
		return null;
	}
}
