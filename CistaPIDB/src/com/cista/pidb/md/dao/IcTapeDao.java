package com.cista.pidb.md.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.md.to.IcTapeQueryTo;
import com.cista.pidb.md.to.IcTapeTo;

public class IcTapeDao extends PIDBDaoSupport {

	public int countResult(IcTapeQueryTo queryTo) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select count(*) from PIDB_IC_TAPE pit where 1 = 1";
		String whereCause = generateWhereCause(queryTo);
		sql += whereCause;
		logger.debug(sql);
		return sjt.queryForInt(sql, new Object[] {});
	}

	public List<IcTapeQueryTo> query(final IcTapeQueryTo queryTo) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select " + "pit.MATERIAL_NUM," + "pit.PKG_CODE,"
				+ "pit.PKG_VERSION," + "pit.TAPE_NAME," + "pit.PROD_NAME,"
				+ "pit.TAPE_WIDTH," + "pit.SPROCKET_HOLE_NUM,"
				+ "pit.MIN_PITCH," + "pit.TAPE_CUST," + "pit.TAPE_VENDOR"
				+ " from PIDB_IC_TAPE pit where 1 = 1 ";
		String whereCause = generateWhereCause(queryTo);
		sql += whereCause;
		if (queryTo.getTapeCust() != null && !queryTo.getTapeCust().equals("")) {
			sql += " AND pit.TAPE_CUST like "
					+ getLikeSQL(queryTo.getTapeCust());
		}
		if (queryTo.getTapeVendor() != null
				&& !queryTo.getTapeVendor().equals("")) {
			sql += "AND pit.TAPE_VENDOR like "
					+ getLikeSQL(queryTo.getTapeVendor());
		}
		sql += " order by pit.TAPE_NAME";

		logger.debug(sql);

		GenericRowMapper<IcTapeQueryTo> rm = new GenericRowMapper<IcTapeQueryTo>(
				IcTapeQueryTo.class);

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

	// modify by 900it 2008/04/15

	public List<IcTapeTo> queryForDomain(final IcTapeQueryTo queryTo) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select "
				+ "pit.MATERIAL_NUM,"
				+ "pit.TAPE_NAME,"
				+ "pit.TAPE_VARIANT,"
				+ "pit.PKG_VERSION,"
				+ "pit.PROD_NAME,"
				+ "pit.PKG_CODE,"
				+ "pit.TAPE_VENDOR,"
				+ "(select DESCRIPTION from WM_SAP_MASTER_PACKAGE_TYPE wsmpt where wsmpt.PACKAGE_TYPE=pit.TAPE_TYPE) TAPE_TYPE,"
				// + "(select SHORT_NAME from WM_SAP_MASTER_VENDOR wsmv where
				// wsmv.VENDOR_CODE=pit.TAPE_VENDOR) TAPE_VENDOR,"
				+ "pit.TAPE_WIDTH,"
				+ "pit.SPROCKET_HOLE_NUM,"
				+ "pit.MIN_PITCH,"
				+ "pit.TAPE_MATERIAL,"
				+ "pit.ASSY_SITE,"
				+ "(select SHORT_NAME from WM_SAP_MASTER_CUSTOMER wsmc where wsmc.CUSTOMER_CODE=pit.TAPE_CUST) TAPE_CUST,"
				+ "pit.TAPE_CUST_PROJ_NAME," + "pit.TAPE_APPROVE_DATE,"
				+ "pit.REMARK," + "pit.CUST_DRAWING_ISSUE_DATE,"
				+ "pit.TCP_DRAWING_FINISH_DATE,"
				+ "pit.TAPE_MAKER_CONFIRM_DONE_DATE,"
				+ "pit.CUST_APPROVAL_DONE_DATE,"
				+ "pit.TAPE_MAKING_FINISH_DONE_DATE," + "pit.TP_INFO,"
				+ "pit.ASSIGN_TO," + "pit.ASSIGN_EMAIL," + "pit.STATUS,"
				+ "pit.CREATED_BY," + "pit.MODIFIED_BY," + "pit.IL_PITCH,"
				+ "pit.CU_LAYER," + "pit.CU_THICKNESS_PATTERN,"
				+ "pit.CU_THICKNESS_BACK," + "pit.REVISION_REASON,"
				+ " pit.TAPE_PROCESS, pit.RELEASE_TO, "
				+ " pit.OLB_CROSS_TOP, pit.OLB_CROSS_BOTTOM, "
				+ " pit.NEW_PROCESS_REASON, pit.OL_OS_TOTAL_PITCH, pit.OL_IS_TOTAL_PITCH, "
				+ " pit.OUTPUT_CHANNEL, pit.CHIP_SIZE, pit.REEL_SIZE, pit.SR_MATERIAL, "
				+ " pit.SPACER_TYPE"
				+ " from PIDB_IC_TAPE pit where 1 = 1 ";
		String whereCause = generateWhereCause(queryTo);
		sql += whereCause;
		sql += " order by pit.TAPE_NAME";

		logger.debug(sql);
		if (queryTo.getPageNo() > 0) {
			int cursorFrom = (queryTo.getPageNo() - 1) * queryTo.getPageSize()
					+ 1;
			int cursorTo = (queryTo.getPageNo()) * queryTo.getPageSize();
			return sjt.query(getPagingSql(sql, cursorFrom, cursorTo),
					new GenericRowMapper<IcTapeTo>(IcTapeTo.class),
					new Object[] {});
		} else {
			return sjt.query(sql,
					new GenericRowMapper<IcTapeTo>(IcTapeTo.class),
					new Object[] {});
		}
	}

	/*
	 * GenericRowMapper<IcTapeTo> rm = new GenericRowMapper<IcTapeTo>(IcTapeTo.class);
	 * 
	 * 
	 * return sjt.query(sql, rm, new Object[]{}); }
	 */

	private String generateWhereCause(final IcTapeQueryTo queryTo) {
		StringBuilder sb = new StringBuilder();

		if (queryTo.getMaterialNum() != null
				&& !queryTo.getMaterialNum().equals("")) {
			String materialNum = getSmartSearchQueryString("pit.MATERIAL_NUM",
					queryTo.getMaterialNum());
			if (materialNum != null) {
				sb.append(" and (" + materialNum + " )");
			}
		}

		if (queryTo.getPkgCode() != null && !queryTo.getPkgCode().equals("")) {
			sb.append(" and pit.PKG_CODE = "
					+ getSQLString(queryTo.getPkgCode()) + " ");
		}

		if (queryTo.getPkgVersion() != null
				&& !queryTo.getPkgVersion().equals("")) {
			sb.append(" and pit.PKG_VERSION = "
					+ getSQLString(queryTo.getPkgVersion()) + " ");
		}

		if (queryTo.getTapeName() != null && !queryTo.getTapeName().equals("")) {
			String tapeName = getSmartSearchQueryString("pit.TAPE_NAME",
					queryTo.getTapeName());
			if (tapeName != null) {
				sb.append(" and (" + tapeName + " )");
			}
		}

		if (queryTo.getProdName() != null && !queryTo.getProdName().equals("")) {
			String prodName = getSmartSearchQueryString("pit.PROD_NAME",
					queryTo.getProdName());
			if (prodName != null) {
				sb.append(" and (" + prodName + " )");
			}
		}

		if (queryTo.getTapeWidth() != null
				&& !queryTo.getTapeWidth().equals("")) {
			sb.append(" and pit.TAPE_WIDTH = "
					+ getSQLString(queryTo.getTapeWidth()) + " ");
		}

		if (queryTo.getSprocketHoleNum() != null
				&& !queryTo.getSprocketHoleNum().equals("")) {
			String sprocketHoleNum = getSmartSearchQueryString(
					"pit.SPROCKET_HOLE_NUM", queryTo.getSprocketHoleNum());
			if (sprocketHoleNum != null) {
				sb.append(" and (" + sprocketHoleNum + " )");
			}
		}

		if (queryTo.getMinPitch() != null && !queryTo.getMinPitch().equals("")) {
			String minPitch = getSmartSearchQueryString("pit.MIN_PITCH",
					queryTo.getMinPitch());
			if (minPitch != null) {
				sb.append(" and (" + minPitch + " )");
			}
		}

		if (queryTo.getReleaseTo() != null
				&& !queryTo.getReleaseTo().equals("")) {
			String releaseTo = getSmartSearchQueryString("pit.RELEASE_TO",
					queryTo.getReleaseTo());
			if (releaseTo != null) {
				sb.append(" and (" + releaseTo + " )");
			}
		}

		return sb.toString();
	}

	public List<IcTapeTo> findAll() {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_IC_TAPE order by PKG_CODE";
		logger.debug(sql);
		List<IcTapeTo> result = stj.query(sql, new GenericRowMapper<IcTapeTo>(
				IcTapeTo.class), new Object[] {});
		return result;
	}

	public List<IcTapeTo> findApModel() {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select distinct TAPE_CUST_PROJ_NAME from PIDB_IC_TAPE where "
				+ getAssertEmptyString("TAPE_CUST_PROJ_NAME")
				+ " order by TAPE_CUST_PROJ_NAME";
		logger.debug(sql);
		List<IcTapeTo> result = stj.query(sql, new GenericRowMapper<IcTapeTo>(
				IcTapeTo.class), new Object[] {});
		return result;
	}

	public List<IcTapeTo> findCust() {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select distinct TAPE_CUST from PIDB_IC_TAPE where "
				+ getAssertEmptyString("TAPE_CUST") + " order by TAPE_CUST";
		logger.debug(sql);
		List<IcTapeTo> result = stj.query(sql, new GenericRowMapper<IcTapeTo>(
				IcTapeTo.class), new Object[] {});
		return result;
	}

	public List<IcTapeTo> findPkgVersion() {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select distinct PKG_VERSION from PIDB_IC_TAPE order by PKG_VERSION";
		logger.debug(sql);
		List<IcTapeTo> result = stj.query(sql, new GenericRowMapper<IcTapeTo>(
				IcTapeTo.class), new Object[] {});
		return result;
	}

	public IcTapeTo findByMaterialNum(String materialNum) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_IC_TAPE where MATERIAL_NUM = ?";
		logger.debug(sql);
		GenericRowMapper<IcTapeTo> rm = new GenericRowMapper<IcTapeTo>(
				IcTapeTo.class);
		List<IcTapeTo> result = sjt
				.query(sql, rm, new Object[] { materialNum });
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	public Object findMaxVar(String prodName, String pkgCode, String pkgVersion) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select max(TAPE_VARIANT) from PIDB_IC_TAPE where PROD_NAME = ? and PKG_CODE = ? and PKG_VERSION = ?";
		logger.debug(sql);

		Object obj = sjt.queryForObject(sql, Object.class, new Object[] {
				prodName, pkgCode, pkgVersion });

		return obj;
	}

	public List<Map<String, Object>> findByProdCode(String prodCode) {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select distinct pit.PKG_CODE,pit.TAPE_CUST,pit.TAPE_CUST_PROJ_NAME from PIDB_IC_TAPE pit left join PIDB_PRODUCT pp on pit.PROD_NAME=pp.PROD_NAME where pp.PROD_CODE like '%"
				+ prodCode
				+ "%' and "
				+ getAssertEmptyString("pp.PROD_CODE")
				+ " order by pit.PKG_CODE";
		logger.debug(sql);
		return stj.queryForList(sql, new Object[] {});
	}

	public List<IcTapeTo> findTapeName() {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select distinct TAPE_NAME from PIDB_IC_TAPE where "
				+ getAssertEmptyString("TAPE_NAME") + " order by TAPE_NAME";
		logger.debug(sql);
		List<IcTapeTo> result = stj.query(sql, new GenericRowMapper<IcTapeTo>(
				IcTapeTo.class), new Object[] {});
		return result;
	}

	public List<IcTapeTo> findMinPitch() {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select distinct MIN_PITCH from PIDB_IC_TAPE where "
				+ getAssertEmptyString("MIN_PITCH") + " order by MIN_PITCH";
		logger.debug(sql);
		List<IcTapeTo> result = stj.query(sql, new GenericRowMapper<IcTapeTo>(
				IcTapeTo.class), new Object[] {});
		return result;
	}

	public List<IcTapeTo> findSprocketHoleNum() {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select distinct SPROCKET_HOLE_NUM from PIDB_IC_TAPE where "
				+ getAssertEmptyString("SPROCKET_HOLE_NUM")
				+ " order by SPROCKET_HOLE_NUM";
		logger.debug(sql);
		List<IcTapeTo> result = stj.query(sql, new GenericRowMapper<IcTapeTo>(
				IcTapeTo.class), new Object[] {});
		return result;
	}

	public List<String> findCustByProdCode(String prodCode) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select pit.TAPE_CUST from PIDB_IC_TAPE pit left join PIDB_PRODUCT pp on pit.PROD_NAME=pp.PROD_NAME where pp.PROD_CODE like '%"
				+ prodCode + "%'";
		logger.debug(sql);
		List<Map<String, Object>> result = sjt.queryForList(sql,
				new Object[] {});
		List<String> ret = new ArrayList<String>();

		if (result != null && result.size() > 0) {
			for (Map<String, Object> m : result) {
				ret.add((String) m.get("TAPE_CUST"));
			}

		}
		return ret;
	}

	public List<String> findTapeVendorByPkgCode(final String pkgCode) {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "";
		List<Map<String, Object>> result;
		if (pkgCode != null && pkgCode.length() > 0) {
			sql = "select distinct TAPE_VENDOR from PIDB_IC_TAPE where PKG_CODE = ? order by TAPE_VENDOR";
			logger.debug(sql);
			result = stj.queryForList(sql, new Object[] { pkgCode });
		} else {
			sql = "select distinct TAPE_VENDOR from PIDB_IC_TAPE order by TAPE_VENDOR";
			logger.debug(sql);
			result = stj.queryForList(sql, new Object[] {});
		}
		List<String> tapeVendors = new ArrayList<String>();
		if (result != null && result.size() > 0) {
			for (Map<String, Object> item : result) {
				tapeVendors.add((String) item.get("TAPE_VENDOR"));
			}
		}
		return tapeVendors;
	}

	public List<String> findAssySiteByPkgCode(final String pkgCode) {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "";
		List<Map<String, Object>> result;
		if (pkgCode != null && pkgCode.length() > 0) {
			sql = "select distinct ASSY_SITE from PIDB_IC_TAPE where PKG_CODE = ? order by ASSY_SITE";
			logger.debug(sql);
			result = stj.queryForList(sql, new Object[] { pkgCode });
		} else {
			sql = "select distinct ASSY_SITE from PIDB_IC_TAPE order by ASSY_SITE";
			logger.debug(sql);
			result = stj.queryForList(sql, new Object[] {});
		}
		List<String> assySites = new ArrayList<String>();
		if (result != null && result.size() > 0) {
			for (Map<String, Object> item : result) {
				assySites.add((String) item.get("ASSY_SITE"));
			}
		}
		return assySites;
	}

	public List<String> findTapeCustByPkgCodeAndProdName(final String pkgCode,
			final String prodName) {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select distinct TAPE_CUST from PIDB_IC_TAPE where PKG_CODE = ? and PROD_NAME = ?";
		List<Map<String, Object>> result = stj.queryForList(sql, new Object[] {
				pkgCode, prodName });

		/*
		 * if (pkgCode != null && pkgCode.length() > 0) { sql = "select distinct
		 * TAPE_CUST from PIDB_IC_TAPE where PKG_CODE = ?"; if (prodName != null &&
		 * prodName.length() > 0) { sql = sql + " and PROD_NAME = ? order by
		 * TAPE_CUST"; logger.debug(sql); result = stj.queryForList(sql, new
		 * Object[] {pkgCode, prodName}); } else { sql = sql + " order by
		 * TAPE_CUST"; logger.debug(sql); result = stj.queryForList(sql, new
		 * Object[] {pkgCode}); } } else { sql = "select distinct TAPE_CUST from
		 * PIDB_IC_TAPE";; if (prodName != null && prodName.length() > 0) { sql =
		 * sql + " PROD_NAME = ? order by TAPE_CUST"; logger.debug(sql); result =
		 * stj.queryForList(sql, new Object[] {prodName}); } else { sql = sql + "
		 * order by TAPE_CUST"; logger.debug(sql); result =
		 * stj.queryForList(sql, new Object[] {}); } }
		 */
		List<String> tapeCusts = new ArrayList<String>();
		if (result != null && result.size() > 0) {
			for (Map<String, Object> item : result) {
				tapeCusts.add((String) item.get("TAPE_CUST"));
			}
		}
		return tapeCusts;
	}

	public List<String> findAllPkgVersion() {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select distinct PKG_VERSION from PIDB_IC_TAPE order by PKG_VERSION";
		logger.debug(sql);
		List<Map<String, Object>> result = stj.queryForList(sql,
				new Object[] {});
		List<String> projOptions = new ArrayList<String>();
		if (result != null && result.size() > 0) {
			for (Map<String, Object> item : result) {
				projOptions.add((String) item.get("PKG_VERSION"));
			}
		}
		return projOptions;
	}

	public List<String> findAllTapeType(final String pkgCode) {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select distinct TAPE_TYPE from PIDB_IC_TAPE where PKG_CODE = ? order by TAPE_TYPE";
		logger.debug(sql);
		List<Map<String, Object>> result = stj.queryForList(sql,
				new Object[] { pkgCode });
		List<String> tapeTypes = new ArrayList<String>();
		if (result != null && result.size() > 0) {
			for (Map<String, Object> item : result) {
				tapeTypes.add((String) item.get("TAPE_TYPE"));
			}
		}
		return tapeTypes;
	}

	public List<String> findAllPkgCode() {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select distinct PKG_CODE from PIDB_IC_TAPE order by PKG_CODE";
		logger.debug(sql);
		List<Map<String, Object>> result = stj.queryForList(sql,
				new Object[] {});
		List<String> tapeTypes = new ArrayList<String>();
		if (result != null && result.size() > 0) {
			for (Map<String, Object> item : result) {
				tapeTypes.add((String) item.get("PKG_CODE"));
			}
		}
		return tapeTypes;
	}

	public List<String> findPkgCodeByPkgCode(final String pkgCode) {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select distinct PKG_CODE from PIDB_IC_TAPE where ("
				+ super.getSmartSearchQueryString("PKG_CODE", pkgCode)
				+ ") order by PKG_CODE";
		logger.debug(sql);
		List<Map<String, Object>> result = stj.queryForList(sql,
				new Object[] {});
		List<String> tapeTypes = new ArrayList<String>();
		if (result != null && result.size() > 0) {
			for (Map<String, Object> item : result) {
				tapeTypes.add((String) item.get("PKG_CODE"));
			}
		}
		return tapeTypes;
	}

	public List<IcTapeTo> findByPkgCode(final String pkgCode) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_IC_TAPE where PKG_CODE = ? ";
		logger.debug(sql);
		GenericRowMapper<IcTapeTo> rm = new GenericRowMapper<IcTapeTo>(
				IcTapeTo.class);
		return sjt.query(sql, rm, new Object[] { pkgCode });
	}

	public List<IcTapeTo> findByProdNamePkgCode(final String prodName,
			final String pkgCode) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_IC_TAPE where PROD_NAME=? and PKG_CODE = ? ";
		logger.debug(sql);
		GenericRowMapper<IcTapeTo> rm = new GenericRowMapper<IcTapeTo>(
				IcTapeTo.class);
		return sjt.query(sql, rm, new Object[] { prodName, pkgCode });
	}

	public List<IcTapeTo> findByTapeName(String tapeName) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_IC_TAPE where TAPE_NAME = ? ";
		logger.debug(sql);

		GenericRowMapper<IcTapeTo> rm = new GenericRowMapper<IcTapeTo>(
				IcTapeTo.class);
		return sjt.query(sql, rm, new Object[] { tapeName });
	}

	public IcTapeTo findByPrimaryKey(String materialNum, String tapeName) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_IC_TAPE where MATERIAL_NUM = ? and TAPE_NAME = ?";
		logger.debug(sql);
		GenericRowMapper<IcTapeTo> rm = new GenericRowMapper<IcTapeTo>(
				IcTapeTo.class);
		List<IcTapeTo> result = sjt.query(sql, rm, new Object[] { materialNum,
				tapeName });
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	public List<IcTapeTo> findByProdCodeList(final String prodCodeList) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select distinct pit.* from PIDB_PRODUCT ppd, PIDB_PROJECT ppj, PIDB_IC_TAPE pit where pit.PROD_NAME=ppd.PROD_NAME and pidb_include(?,',',ppd.PROD_CODE)>=1";
		logger.debug(sql);
		GenericRowMapper<IcTapeTo> rm = new GenericRowMapper<IcTapeTo>(
				IcTapeTo.class);
		return sjt.query(sql, rm, new Object[] { prodCodeList });
	}

	/**
	 * Find records by PROD_NAME
	 * 
	 * @param prodName
	 *            PROD_NAME
	 * @return List of IcTapeTo
	 */
	public List<IcTapeTo> findByProdName(final String prodName) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_IC_TAPE where PROD_NAME = ? ";
		logger.debug(sql);

		GenericRowMapper<IcTapeTo> mapper = new GenericRowMapper<IcTapeTo>(
				IcTapeTo.class);
		List<IcTapeTo> result = sjt.query(sql, mapper, prodName);

		return result;
	}

	/**
	 * Find records by TAPE_NAME
	 * 
	 * @param prodName
	 *            TAPE_Vendor
	 * @return List of IcTapeTo
	 */

	public List<String> findTapeShortName(final String vendorCode) {

		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "Select SHORT_NAME TAPE_VENDOR from WM_SAP_MASTER_VENDOR where ("
				+ super.getSmartSearchQueryString("VENDOR_CODE", vendorCode)
				+ ") order by VENDOR_CODE";
		logger.debug(sql);

		List<Map<String, Object>> result = stj.queryForList(sql,
				new Object[] {});
		List<String> icTape = new ArrayList<String>();

		if (result != null && result.size() > 0) {
			for (Map<String, Object> item : result) {
				icTape.add((String) item.get("TAPE_VENDOR"));
			}
		}
		return icTape;
	}

	/**
	 * create an IcWafer object.
	 * 
	 * @param newInstance
	 */
	public void insert(final IcTapeTo newInstance) {
		if (null == newInstance) {
			logger.error("Error the new IcWafer object is null.");
		}
		TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(
					final TransactionStatus status) {
				/*
				 * int newSdramId = SequenceSupport
				 * .nextValue(SequenceSupport.SEQ_PIDB_SDRAM);
				 */

				SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();

				String sql = "Insert Into PIDB_IC_TAPE "
						+ "(MATERIAL_NUM, TAPE_NAME, TAPE_VARIANT, PKG_VERSION,"
						+ "PROD_NAME, PKG_CODE, TAPE_TYPE, TAPE_VENDOR,"
						+ "TAPE_WIDTH, SPROCKET_HOLE_NUM, MIN_PITCH, TAPE_MATERIAL,"
						+ "ASSY_SITE, TAPE_CUST, TAPE_CUST_PROJ_NAME,"
						+ "TAPE_APPROVE_DATE, REMARK, CUST_DRAWING_ISSUE_DATE,"
						+ "TCP_DRAWING_FINISH_DATE, TAPE_MAKER_CONFIRM_DONE_DATE,"
						+ "CUST_APPROVAL_DONE_DATE, TAPE_MAKING_FINISH_DONE_DATE,"
						+ "TP_INFO, ASSIGN_TO, ASSIGN_EMAIL, STATUS, CREATED_BY,"
						+ "MODIFIED_BY, IL_PITCH, CU_LAYER, CU_THICKNESS_PATTERN,"
						+ "CU_THICKNESS_BACK, REVISION_REASON, TAPE_PROCESS,"
						+ "RELEASE_TO, OLB_CROSS_TOP, OLB_CROSS_BOTTOM, "
						+ " NEW_PROCESS_REASON, OL_OS_TOTAL_PITCH, OL_IS_TOTAL_PITCH, "
						+ " OUTPUT_CHANNEL, CHIP_SIZE, REEL_SIZE, SR_MATERIAL, "
						+ " SPACER_TYPE )"
						+ " Values(?,?,?,?,?,?,?,?,?,?," +
								" ?,?,?,?,?,?,?,?,?,?," +
								" ?,?,?,?,?,?,?,?,?,?," +
								" ?,?,?,?,?,?,?,?,?,?," +
								" ?,?,?,?,?)";
				logger.debug(sql);
				sjt.update(sql, newInstance.getMaterialNum(), newInstance
						.getTapeName(), newInstance.getTapeVariant(),
						newInstance.getPkgVersion(), newInstance.getProdName(),
						newInstance.getPkgCode(), newInstance.getTapeType(),
						newInstance.getTapeVendor(),
						newInstance.getTapeWidth(), newInstance
								.getSprocketHoleNum(), newInstance
								.getMinPitch(), newInstance.getTapeMaterial(),
						newInstance.getAssySite(), newInstance.getTapeCust(),
						newInstance.getTapeCustProjName(), newInstance
								.getTapeApproveDate(), newInstance.getRemark(),
						newInstance.getCustDrawingIssueDate(), newInstance
								.getTcpDrawingFinishDate(), newInstance
								.getTapeMakerConfirmDoneDate(), newInstance
								.getCustApprovalDoneDate(), newInstance
								.getTapeMakingFinishDoneDate(), newInstance
								.getTpInfo(), newInstance.getAssignTo(),
						newInstance.getAssignEmail(), newInstance.getStatus(),
						newInstance.getCreatedBy(),
						newInstance.getModifiedBy(), newInstance.getIlPitch(),
						newInstance.getCuLayer(), newInstance
								.getCuThicknessPattern(), newInstance
								.getCuThicknessBack(), newInstance
								.getRevisionReason(), newInstance
								.getTapeProcess(), newInstance.getReleaseTo(),
						newInstance.getOlbCrossTop(),newInstance.getOlbCrossBottom(),
						newInstance.getNewProcessReason(),newInstance.getOlOsTotalPitch(),
						newInstance.getOlIsTotalPitch(),newInstance.getOutputChannel(),
						newInstance.getChipSize(),newInstance.getReelSize(),
						newInstance.getSrMaterial(),newInstance.getSpacerType()
				);

			}
		};
		doInTransaction(callback);
	}

	public void updateIcTape(final IcTapeTo icTapeTo) {
		if (icTapeTo == null) {
			logger.error("Error IcTapeTo the parameter is null.");
		}
		TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(
					final TransactionStatus status) {
				SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
				String sql = "Update PIDB_IC_TAPE set TAPE_VARIANT=?,PKG_VERSION=?, " +
				" PROD_NAME=?,PKG_CODE=?,TAPE_TYPE=?,TAPE_VENDOR=?,TAPE_WIDTH=?," +
				" SPROCKET_HOLE_NUM=?, MIN_PITCH=?,TAPE_MATERIAL=?,ASSY_SITE=?," +
				" TAPE_CUST=?,TAPE_CUST_PROJ_NAME=?, TAPE_APPROVE_DATE=?,REMARK=?," +
				" CUST_DRAWING_ISSUE_DATE=?,TCP_DRAWING_FINISH_DATE=?, " +
				" TAPE_MAKER_CONFIRM_DONE_DATE=?,CUST_APPROVAL_DONE_DATE=?," +
				" TAPE_MAKING_FINISH_DONE_DATE=?, TP_INFO=?,ASSIGN_TO=?," +
				
				" ASSIGN_EMAIL=?,STATUS=?,MODIFIED_BY=?,IL_PITCH=?," +
				" CU_LAYER=?,CU_THICKNESS_PATTERN=?,CU_THICKNESS_BACK=?," +
				" REVISION_REASON=?,TAPE_PROCESS=?, RELEASE_TO=?,OLB_CROSS_TOP=?," +
				" OLB_CROSS_BOTTOM=?,NEW_PROCESS_REASON=?,OL_OS_TOTAL_PITCH=?, " +
				" OL_IS_TOTAL_PITCH=?,OUTPUT_CHANNEL=?,CHIP_SIZE=?,REEL_SIZE=?," +
				" SR_MATERIAL=?,SPACER_TYPE=?, UDT=TO_CHAR(SYSDATE, 'RRRRMMDDHH24MISS') " +
				" Where MATERIAL_NUM=?  and TAPE_NAME=? ";
				logger.debug(sql);
				sjt.update(sql, icTapeTo.getTapeVariant(), icTapeTo.getPkgVersion(),
						icTapeTo.getProdName(),icTapeTo.getPkgCode(),icTapeTo.getTapeType(),
						icTapeTo.getTapeVendor(),icTapeTo.getTapeWidth(),
						icTapeTo.getSprocketHoleNum(),icTapeTo.getMinPitch(),icTapeTo.getTapeMaterial(),
						icTapeTo.getAssySite(),icTapeTo.getTapeCust(),icTapeTo.getTapeCustProjName(),
						icTapeTo.getTapeApproveDate(),icTapeTo.getRemark(),
						icTapeTo.getCustDrawingIssueDate(),icTapeTo.getTcpDrawingFinishDate(),
						icTapeTo.getTapeMakerConfirmDoneDate(),icTapeTo.getCustApprovalDoneDate(),
						icTapeTo.getTapeMakingFinishDoneDate(),icTapeTo.getTpInfo(),icTapeTo.getAssignTo(),
						icTapeTo.getAssignEmail(),icTapeTo.getStatus(),icTapeTo.getModifiedBy(),
						icTapeTo.getIlPitch(),icTapeTo.getCuLayer(),icTapeTo.getCuThicknessPattern(),
						icTapeTo.getCuThicknessBack(),icTapeTo.getRevisionReason(),
						icTapeTo.getTapeProcess(),icTapeTo.getReleaseTo(),icTapeTo.getOlbCrossTop(),
						icTapeTo.getOlbCrossBottom(),icTapeTo.getNewProcessReason(),icTapeTo.getOlOsTotalPitch(),
						icTapeTo.getOlIsTotalPitch(),icTapeTo.getOutputChannel(),icTapeTo.getChipSize(),
						icTapeTo.getReelSize(),icTapeTo.getSrMaterial(),icTapeTo.getSpacerType(),
						//PK Key
						icTapeTo.getMaterialNum(),icTapeTo.getTapeName()
						
				);
			}
		};
		doInTransaction(callback);
	}
}
