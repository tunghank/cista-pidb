package com.cista.pidb.md.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.cista.pidb.code.dao.SapMasterCustomerDao;
import com.cista.pidb.code.dao.SapMasterVendorDao;
import com.cista.pidb.code.to.SapMasterCustomerTo;
import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.md.to.MpListEolCustTo;
import com.cista.pidb.md.to.MpListQueryTo;
import com.cista.pidb.md.to.MpListTo;

/**
 * .
 * 
 * @author Hu Meixia
 * 
 */
public class MpListDao extends PIDBDaoSupport {

	/**
	 * @return List
	 */
	public List<MpListTo> findAll() {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_MP_LIST order by PART_NUM";
		logger.debug(sql);
		return sjt.query(sql, new GenericRowMapper<MpListTo>(MpListTo.class),
				new Object[] {});
	}

	/**
	 * @return List
	 */
	public List<MpListTo> findByProjCode(String projCode) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_MP_LIST Where PROJ_CODE like '"
				+ projCode + "%'";
		logger.debug(sql);
				
		if ( projCode == null || projCode.equals("")){
			return null;
		}
		
		return sjt.query(sql, new GenericRowMapper<MpListTo>(MpListTo.class),
				new Object[] {});
	}

	/**
	 * @return List
	 */
	public List<MpListTo> findByProdCodeGreaterThan8(String prodCode) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_MP_LIST Where PROD_CODE like '"
				+ prodCode + "%'";
		logger.debug(sql);
		if ( prodCode == null || prodCode.equals("")){
			return null;
		}
		return sjt.query(sql, new GenericRowMapper<MpListTo>(MpListTo.class),
				new Object[] {});
	}

	/**
	 * @return List
	 */
	public List<MpListTo> findByTapeName(String tapeName) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_MP_LIST Where TAPE_NAME like '"
				+ tapeName + "'";
		logger.debug(sql);
		if ( tapeName == null || tapeName.equals("")){
			return null;
		}
		return sjt.query(sql, new GenericRowMapper<MpListTo>(MpListTo.class),
				new Object[] {});
	}

	/**
	 * @return List
	 */
	public List<MpListTo> findByProdCode(String prodCode) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_MP_LIST Where PROD_CODE like '"
				+ prodCode + "'";
		logger.debug(sql);
		if ( prodCode == null || prodCode.equals("")){
			return null;
		}
		return sjt.query(sql, new GenericRowMapper<MpListTo>(MpListTo.class),
				new Object[] {});
	}

	/**
	 * @return List
	 */
	public List<MpListTo> findByPartNum(String partNum) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_MP_LIST Where PART_NUM like '"
				+ partNum + "'";
		logger.debug(sql);
		if ( partNum == null || partNum.equals("")){
			return null;
		}
		return sjt.query(sql, new GenericRowMapper<MpListTo>(MpListTo.class),
				new Object[] {});
	}

	/**
	 * Find mpList by given id.
	 * 
	 * @param id
	 *            is the value of MpList ID
	 * @return MpListTo
	 */
	public MpListTo find(final int id) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_MP_LIST where ID= " + id + " ";
		logger.debug(sql);
		return sjt.queryForObject(sql, new GenericRowMapper<MpListTo>(
				MpListTo.class), new Object[] {});
	}

	/**
	 * .
	 * 
	 * @param partNum
	 *            String
	 * @return MpListTo
	 */
	public MpListTo find(final String partNum) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_MP_LIST where PART_NUM = ?";
		GenericRowMapper<MpListTo> rm = new GenericRowMapper<MpListTo>(
				MpListTo.class);
		List<MpListTo> result = sjt.query(sql, rm, new Object[] { partNum });
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	public String findEOL(final String icFgMaterialNum) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_MP_LIST where IC_FG_MATERIAL_NUM = ? ";
		GenericRowMapper<MpListTo> rm = new GenericRowMapper<MpListTo>(
				MpListTo.class);
		List<MpListTo> result = sjt.query(sql, rm,
				new Object[] { icFgMaterialNum });

		String mpStatus = "";
		if (result != null && result.size() > 0) {

			for (MpListTo mpList : result) {
				mpStatus = mpList.getMpStatus();
				mpStatus = null != mpStatus ? mpStatus : "";

				if (mpStatus.equals("1") || !mpStatus.equals("0")) {
					mpStatus = "1"; // MP
					break;
				} else {
					mpStatus = "0"; // EOL
					continue;
				}
			}
			return mpStatus;
		} else {
			return "2"; // NON-MP
		}
	}

	public String findEOLByProjWVersion(final String projCodeWVersion) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_MP_LIST where PROJ_CODE_W_VERSION = ? ";
		GenericRowMapper<MpListTo> rm = new GenericRowMapper<MpListTo>(
				MpListTo.class);
		List<MpListTo> result = sjt.query(sql, rm,
				new Object[] { projCodeWVersion });
		String mpStatus = "";
		if (result != null && result.size() > 0) {

			for (MpListTo mpList : result) {
				mpStatus = mpList.getMpStatus();
				mpStatus = null != mpStatus ? mpStatus : "";

				if (mpStatus.equals("1") || !mpStatus.equals("0")) {
					mpStatus = "1"; // MP
					break;
				} else {
					mpStatus = "0"; // EOL
					continue;
				}
			}
			return mpStatus;
		} else {
			return "2"; // NON-MP
		}
	}

	public String findEOLByTapeName(final String TapeName) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_MP_LIST where TAPE_NAME = ? ";
		GenericRowMapper<MpListTo> rm = new GenericRowMapper<MpListTo>(
				MpListTo.class);
		List<MpListTo> result = sjt.query(sql, rm, new Object[] { TapeName });
		String mpStatus = "";
		if (result != null && result.size() > 0) {

			for (MpListTo mpList : result) {
				mpStatus = mpList.getMpStatus();
				mpStatus = null != mpStatus ? mpStatus : "";

				if (mpStatus.equals("1") || !mpStatus.equals("0")) {
					mpStatus = "1"; // MP
					break;
				} else {
					mpStatus = "0"; // EOL
					continue;
				}
			}
			return mpStatus;
		} else {
			return "2"; // NON-MP
		}
	}

	/**
	 * find TAPE_NAME.
	 * 
	 * @param tapeName
	 *            String
	 * @return List<String>
	 */
	public List<String> findTapeName() {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select distinct TAPE_NAME from PIDB_MP_LIST order by TAPE_NAME";
		List<Map<String, Object>> result = sjt.queryForList(sql,
				new Object[] {});
		List<String> retList = new ArrayList<String>();
		if (result != null && result.size() > 0) {
			for (Map<String, Object> col : result) {
				retList.add((String) col.get("TAPE_NAME"));
			}
		}
		return retList;
	}

	/**
	 * .
	 * 
	 * @param queryTo
	 *            MpListQueryTo
	 * @return int
	 */
	public int countResult(final MpListQueryTo queryTo) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select count(*) from PIDB_MP_LIST pml,PIDB_PROJECT_CODE ppc "
				+ " where 1 = 1 AND pml.PROJ_CODE = ppc.PROJ_CODE(+) ";
		if (queryTo.getReleaseTo() != null
				&& !queryTo.getReleaseTo().equals("")) {
			if (queryTo.getReleaseTo().equals("HX")
					|| queryTo.getReleaseTo().equals("WP")) {
				sql += " AND (ppc.RELEASE_TO ='" + queryTo.getReleaseTo() + "'"
						+ " or ppc.RELEASE_TO ='ALL')";
			}
		}
		String whereCause = generateWhereCause(queryTo);
		sql += whereCause;
		return sjt.queryForInt(sql, new Object[] {});
	}

	/**
	 * .
	 * 
	 * @param queryTo
	 *            MpListQueryTo
	 * @return List
	 */
	public List<MpListTo> query(final MpListQueryTo queryTo) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select distinct pml.PART_NUM,"
				+ "pml.IC_FG_MATERIAL_NUM," + "pml.PROD_CODE,"
				+ "pml.PKG_CODE," + "pml.PROJ_CODE,"
				+ "pml.PROJ_CODE_W_VERSION," + "pml.TAPE_NAME,"
				+ "pml.MP_STATUS," + "pml.MP_RELEASE_DATE,"
				+ "pml.APPROVE_CUST," + "pml.APPROVE_TAPE_VENDOR,"
				+ "pml.APPROVE_BP_VENDOR," + "pml.APPROVE_CP_HOUSE,"
				+ "pml.APPROVE_ASSY_HOUSE," + "pml.ASSIGN_TO,"
				+ "pml.ASSIGN_EMAIL," + "pml.CREATED_BY," + "pml.MODIFIED_BY,"
				+ "pml.MP_TRAY_DRAWING_NO1," + "pml.MP_TRAY_DRAWING_NO_VER1,"
				+ "pml.MP_COLOR1," + "pml.MP_CUSTOMER_NAME1,"
				+ "pml.MP_TRAY_DRAWING_NO2," + "pml.MP_TRAY_DRAWING_NO_VER2,"
				+ "pml.MP_COLOR2," + "pml.MP_CUSTOMER_NAME2,"
				+ "pml.MP_TRAY_DRAWING_NO3," + "pml.MP_TRAY_DRAWING_NO_VER3,"
				+ "pml.MP_COLOR3," + "pml.MP_CUSTOMER_NAME3,"
				+ "pml.MP_TRAY_DRAWING_NO4," + "pml.MP_TRAY_DRAWING_NO_VER4,"
				+ "pml.MP_COLOR4," + "pml.MP_CUSTOMER_NAME4,"
				+ "pml.PROCESS_FLOW," + "pml.MAT_TAPE," + "pml.MAT_BP,"
				+ "pml.MAT_CP," + "pml.MAT_AS," + "pml.MAT_WF," + "pml.REMARK,"
				+ "pml.APPROVE_FT_HOUSE," + "pml.REVISION_ITEM,"
				+ "pml.UPDATE_TIME," + "pml.CDT,"
				+ "pml.APPROVE_POLISH_VENDOR," + "pml.MAT_POLISH, "
				+ "pml.APPROVE_COLOR_FILTER_VENDOR," + "pml.MAT_CF,"
				+ "pml.APPROVE_WAFER_CF_VENDOR," + "pml.MAT_WAFERCF, "
				+ "pml.MAT_CSP," + "pml.APPROVE_CP_CSP_VENDOR, "
				+ "ppc.RELEASE_TO "
				+ "from PIDB_MP_LIST pml,PIDB_PROJECT_CODE ppc"
				+ " where 1 = 1  AND pml.PROJ_CODE = ppc.PROJ_CODE(+) ";
		if (queryTo.getReleaseTo() != null
				&& !queryTo.getReleaseTo().equals("")) {
			if (queryTo.getReleaseTo().equals("HX")
					|| queryTo.getReleaseTo().equals("WP")) {
				sql += " AND (ppc.RELEASE_TO ='" + queryTo.getReleaseTo() + "'"
						+ " or ppc.RELEASE_TO ='ALL')";
			}
		}
		String whereCause = generateWhereCause(queryTo);
		sql += whereCause + " order by PART_NUM";
		// System.out.println(sql);

		GenericRowMapper<MpListTo> rm = new GenericRowMapper<MpListTo>(
				MpListTo.class);

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
	 *            MpListQueryTo
	 * @return List
	 */
	public List<MpListTo> queryForDomain(final MpListQueryTo queryTo) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select distinct pml.PART_NUM,"
				+ "pml.IC_FG_MATERIAL_NUM," + "pml.PROD_CODE,"
				+ "pml.PKG_CODE," + "pml.PROJ_CODE,"
				+ "pml.PROJ_CODE_W_VERSION," + "pml.TAPE_NAME,"
				+ "pml.MP_STATUS," + "pml.MP_RELEASE_DATE,"
				+ "pml.APPROVE_CUST," + "pml.APPROVE_TAPE_VENDOR,"
				+ "pml.APPROVE_BP_VENDOR," + "pml.APPROVE_CP_HOUSE,"
				+ "pml.APPROVE_ASSY_HOUSE," + "pml.ASSIGN_TO,"
				+ "pml.ASSIGN_EMAIL," + "pml.CREATED_BY," + "pml.MODIFIED_BY,"
				+ "pml.MP_TRAY_DRAWING_NO1," + "pml.MP_TRAY_DRAWING_NO_VER1,"
				+ "pml.MP_COLOR1," + "pml.MP_CUSTOMER_NAME1,"
				+ "pml.MP_TRAY_DRAWING_NO2," + "pml.MP_TRAY_DRAWING_NO_VER2,"
				+ "pml.MP_COLOR2," + "pml.MP_CUSTOMER_NAME2,"
				+ "pml.MP_TRAY_DRAWING_NO3," + "pml.MP_TRAY_DRAWING_NO_VER3,"
				+ "pml.MP_COLOR3," + "pml.MP_CUSTOMER_NAME3,"
				+ "pml.MP_TRAY_DRAWING_NO4," + "pml.MP_TRAY_DRAWING_NO_VER4,"
				+ "pml.MP_COLOR4," + "pml.MP_CUSTOMER_NAME4,"
				+ "pml.PROCESS_FLOW," + "pml.MAT_TAPE," + "pml.MAT_BP,"
				+ "pml.MAT_CP," + "pml.MAT_AS," + "pml.MAT_WF," + "pml.REMARK,"
				+ "pml.APPROVE_FT_HOUSE," + "pml.REVISION_ITEM,"
				+ "pml.UPDATE_TIME," + "pml.CDT,"
				+ "pml.APPROVE_POLISH_VENDOR," + "pml.MAT_POLISH, "
				+ "pml.APPROVE_COLOR_FILTER_VENDOR," + "pml.MAT_CF,"
				+ "pml.APPROVE_WAFER_CF_VENDOR," + "pml.MAT_WAFERCF, "
				+ "pml.MAT_CSP," + "pml.APPROVE_CP_CSP_VENDOR, "
				+ "ppc.RELEASE_TO, pml.CP_BIN,"
				+ "(SELECT icFg.MCP_DIE_QTY FROM PIDB_IC_FG icFg Where pml.IC_FG_MATERIAL_NUM = icFg.MATERIAL_NUM ) MCP_DIE_QTY, "
				+ "(SELECT icFg.MCP_PKG FROM PIDB_IC_FG icFg Where pml.IC_FG_MATERIAL_NUM = icFg.MATERIAL_NUM ) MCP_PKG, "
				+ "(SELECT icFg.MCP_PROD1 FROM PIDB_IC_FG icFg Where pml.IC_FG_MATERIAL_NUM = icFg.MATERIAL_NUM ) MCP_PROD1, "
				+ "(SELECT icFg.MCP_PROD2 FROM PIDB_IC_FG icFg Where pml.IC_FG_MATERIAL_NUM = icFg.MATERIAL_NUM ) MCP_PROD2, "
				+ "(SELECT icFg.MCP_PROD3 FROM PIDB_IC_FG icFg Where pml.IC_FG_MATERIAL_NUM = icFg.MATERIAL_NUM ) MCP_PROD3, "
				+ "(SELECT icFg.MCP_PROD4 FROM PIDB_IC_FG icFg Where pml.IC_FG_MATERIAL_NUM = icFg.MATERIAL_NUM ) MCP_PROD4, "
				+ "(SELECT tradPkg.LF_TOOL FROM PIDB_TRADITIONAL_PKG tradPkg WHERE tradPkg.PKG_NAME =pml.PART_NUM) LF_TOOL, "
				+ "(SELECT tradPkg.CLOSE_LF_NAME FROM PIDB_TRADITIONAL_PKG tradPkg WHERE tradPkg.PKG_NAME =pml.PART_NUM) CLOSE_LF_NAME "
				+ "from PIDB_MP_LIST pml,PIDB_PROJECT_CODE ppc"
				+ " where 1 = 1  AND pml.PROJ_CODE = ppc.PROJ_CODE(+) ";
		// String sql = "select * from PIDB_MP_LIST where 1 = 1 ";
		if (queryTo.getReleaseTo() != null
				&& !queryTo.getReleaseTo().equals("")) {
			if (queryTo.getReleaseTo().equals("HX")
					|| queryTo.getReleaseTo().equals("WP")) {
				sql += " AND (ppc.RELEASE_TO ='" + queryTo.getReleaseTo() + "'"
						+ " or ppc.RELEASE_TO ='ALL')";
			}
		}
		String whereCause = generateWhereCause(queryTo);

		sql += whereCause + " order by pml.PART_NUM";
		// System.out.println(sql);

		GenericRowMapper<MpListTo> rm = new GenericRowMapper<MpListTo>(
				MpListTo.class);
		List<MpListTo> result = null;
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
			SapMasterVendorDao vendorDao = new SapMasterVendorDao();
			SapMasterCustomerDao custDao = new SapMasterCustomerDao();
			for (MpListTo t : result) {
				t.setApproveTapeVendor(vendorDao.codeToShortName(t
						.getApproveTapeVendor()));
				t.setApproveBpVendor(vendorDao.codeToShortName(t
						.getApproveBpVendor()));
				t.setApproveCpHouse(vendorDao.codeToShortName(t
						.getApproveCpHouse()));
				t.setApproveAssyHouse(vendorDao.codeToShortName(t
						.getApproveAssyHouse()));
				t.setApprovePolishVendor(vendorDao.codeToShortName(t
						.getApprovePolishVendor()));
				t.setApproveColorFilterVendor(vendorDao.codeToShortName(t
						.getApproveColorFilterVendor()));
				t.setApproveWaferCfVendor(vendorDao.codeToShortName(t
						.getApproveWaferCfVendor()));
				t.setApproveCpCspVendor(vendorDao.codeToShortName(t
						.getApproveWaferCfVendor()));
				t.setApproveCust(custDao
						.codeToCustShortName(t.getApproveCust()));
				if (t.getMpStatus() == null) {
					t.setMpStatus("");
				} else if (t.getMpStatus().equals("0")) {
					t.setMpStatus("InActive");
				} else if (t.getMpStatus().equals("1")) {
					t.setMpStatus("Active");
				}
			}
		}
		return result;
	}

	/**
	 * .
	 * 
	 * @param queryTo
	 *            MpListQueryTo
	 * @return String
	 */
	private String generateWhereCause(final MpListQueryTo queryTo) {
		StringBuilder sb = new StringBuilder();

		if (queryTo.getProjCode() != null && !queryTo.getProjCode().equals("")) {
			String queryString = getSmartSearchQueryAllLike("pml.PROJ_CODE",
					queryTo.getProjCode());
			if (queryString != null) {
				sb.append(" and (" + queryString + " )");
			}
		}

		if (queryTo.getPartNum() != null && !queryTo.getPartNum().equals("")) {
			String queryString = getSmartSearchQueryString("pml.PART_NUM",
					queryTo.getPartNum());
			if (queryString != null) {
				sb.append(" and (" + queryString + " )");
			}
		}

		if (queryTo.getPkgCode() != null && !queryTo.getPkgCode().equals("")) {
			String queryString = getSmartSearchQueryString("pml.PKG_CODE",
					queryTo.getPkgCode());
			if (queryString != null) {
				sb.append(" and (" + queryString + " )");
			}
		}

		if (queryTo.getProdCode() != null && !queryTo.getProdCode().equals("")) {
			String queryString = getSmartSearchQueryAllLike("pml.PROD_CODE",
					queryTo.getProdCode());
			if (queryString != null) {
				sb.append(" and (" + queryString + " )");
			}
		}

		if (queryTo.getTapeName() != null && !queryTo.getTapeName().equals("")) {
			String queryString = getSmartSearchQueryString("pml.TAPE_NAME",
					queryTo.getTapeName());
			if (queryString != null) {
				sb.append(" and (" + queryString + " )");
			}
		}

		if (queryTo.getProjCodeWVersion() != null
				&& !queryTo.getProjCodeWVersion().equals("")) {
			String queryString = getSmartSearchQueryString(
					"pml.PROJ_CODE_W_VERSION", queryTo.getProjCodeWVersion());
			if (queryString != null) {
				sb.append(" and (" + queryString + " )");
			}
		}
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		if (queryTo.getMpReleaseDateFrom() != null
				&& !queryTo.getMpReleaseDateFrom().equals("")
				&& queryTo.getMpReleaseDateTo() != null
				&& !queryTo.getMpReleaseDateTo().equals("")) {
			/*
			 * String dateFrom = getSQLDateString(sdf.format(queryTo
			 * .getMpReleaseDateFrom()) + " 00:00:00", "yyyy/MM/dd HH:mm:ss");
			 * String dateTo = getSQLDateString(sdf.format(queryTo
			 * .getMpReleaseDateTo()) + " 23:59:59", "yyyy/MM/dd HH:mm:ss");
			 */
			String dateFrom = getSQLDateString(queryTo.getMpReleaseDateFrom()
					+ " 00:00:00", "yyyy/MM/dd HH:mm:ss");
			String dateTo = getSQLDateString(queryTo.getMpReleaseDateTo()
					+ " 23:59:59", "yyyy/MM/dd HH:mm:ss");
			sb.append(" and pml.MP_RELEASE_DATE between " + dateFrom + " and "
					+ dateTo + " ");
		}

		if (queryTo.getMaterialNum() != null
				&& !queryTo.getMaterialNum().equals("")) {
			String queryString = getSmartSearchQueryString(
					"pml.IC_FG_MATERIAL_NUM", queryTo.getMaterialNum());
			if (queryString != null) {
				sb.append(" and (" + queryString + " )");
			}
		}

		if (queryTo.getRevisionItem() != null
				&& !queryTo.getRevisionItem().equals("")) {
			String queryString = getSmartSearchQueryString("pml.REVISION_ITEM",
					queryTo.getRevisionItem());
			if (queryString != null) {
				sb.append(" and (" + queryString + " )");
			}
		}

		if (queryTo.getApproveCust() != null
				&& !queryTo.getApproveCust().equals("")) {
			// 將smartSearch的值轉換成資料庫所存的值
			String field = queryTo.getApproveCust();
			// System.out.println("Approve_Cust= "+ queryTo.getApproveCust());
			String realOwner = "";
			SapMasterCustomerDao sapMasterCustomerDao = new SapMasterCustomerDao();
			if (field != null && !field.equals("")) {
				String[] fields = field.split(",");
				if (fields != null && fields.length > 0) {
					for (String s : fields) {
						SapMasterCustomerTo to = sapMasterCustomerDao
								.findByShortName(s);
						if (to != null) {
							realOwner += to.getCustomerCode() + ",";
						}
					}
					// System.out.println("realOwner= "+realOwner);
				}
			}
			String approveCust = getSmartSearchQueryAllLike("pml.APPROVE_CUST",
					realOwner);
			if (realOwner != null) {
				sb.append("and (" + approveCust + ")");
			}

		}

		/*
		 * if (queryTo.getReleaseTo() != null &&
		 * !queryTo.getReleaseTo().equals("")) { String queryString =
		 * getSmartSearchQueryString("ppc.RELEASE_TO", queryTo.getReleaseTo());
		 * if (queryString != null) { sb.append(" and (" + queryString + " )"); } }
		 */

		return sb.toString();
	}

	/**
	 * .
	 * 
	 * @param version
	 *            String
	 * @return MpListTo
	 */
	public MpListTo findByProjCodeWVersion(final String version) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_MP_LIST where PROJ_CODE_W_VERSION = '"
				+ version + "'";
		logger.debug(sql);
		MpListTo listTo;
		try {
			listTo = sjt.queryForObject(sql, new GenericRowMapper<MpListTo>(
					MpListTo.class), new Object[] {});
		} catch (Exception e) {
			// e.printStackTrace();
			return null;
		}
		return listTo;
	}

	/**
	 * .
	 * 
	 * @param materialNum
	 *            String
	 * @return List
	 */
	public List<MpListTo> findByIcFgMaterialNum(final String materialNum) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_MP_LIST where IC_FG_MATERIAL_NUM = '"
				+ materialNum + "'";
		logger.debug(sql);

		return sjt.query(sql, new GenericRowMapper<MpListTo>(MpListTo.class),
				new Object[] {});
	}

	/**
	 * .
	 * 
	 * @param partNum
	 *            String
	 * @return MpListTo
	 */
	public MpListTo findByPrimaryKey(final String partNum,
			final String icFgMaterialNum, final String projCodeWVersion,
			final String tapeName, final String pkgCode) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select mp.*, icFg.MCP_DIE_QTY, icFg.MCP_PKG, icFg.MCP_PROD1, " +
				" icFg.MCP_PROD2, icFg.MCP_PROD3, icFg.MCP_PROD4,  " 
				+ "(SELECT A.LF_TOOL FROM PIDB_TRADITIONAL_PKG A WHERE A.PKG_NAME =mp.PART_NUM) LF_TOOL, "
				+ "(SELECT A.CLOSE_LF_NAME FROM PIDB_TRADITIONAL_PKG A WHERE A.PKG_NAME =mp.PART_NUM) CLOSE_LF_NAME "
				+ " from PIDB_MP_LIST mp, PIDB_IC_FG icFg where " 
				+ " mp.PART_NUM = ? and mp.IC_FG_MATERIAL_NUM = ? and mp.PROJ_CODE_W_VERSION = ? "
				+ " and mp.TAPE_NAME = ? and mp.PKG_CODE = ? " +
				  " AND mp.IC_FG_MATERIAL_NUM = icFg.MATERIAL_NUM(+)";
		GenericRowMapper<MpListTo> rm = new GenericRowMapper<MpListTo>(
				MpListTo.class);
		List<MpListTo> result = sjt.query(sql, rm, new Object[] { partNum,
				icFgMaterialNum, projCodeWVersion, tapeName, pkgCode });
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	public MpListTo findByMpStatus(String partNum, String mpStatus) {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_MP_LIST where PART_NUM = ? AND MP_STATUS=? ";
		sql += " Order by PART_NUM ";
		logger.debug(sql);
		List<MpListTo> result = stj.query(sql, new GenericRowMapper<MpListTo>(
				MpListTo.class), new Object[] { partNum, mpStatus });

		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	/**
	 * .
	 * 
	 * @param fileName
	 *            String
	 * @return boolean
	 */
	public boolean findVendor(final String icFgMaterialNum,
			final String projCodeWVersion, final String tapeName,
			final String pkgCode, final String filed, final String filedValue) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "";
		GenericRowMapper<MpListTo> rm;
		List<MpListTo> result;
		if (filedValue != null) {
			sql = sql
					+ "select * from PIDB_MP_LIST where IC_FG_MATERIAL_NUM = ? and PROJ_CODE_W_VERSION = ? and TAPE_NAME = ? and PKG_CODE = ? and "
					+ filed + " = ?";

			rm = new GenericRowMapper<MpListTo>(MpListTo.class);
			result = sjt.query(sql, rm, new Object[] { icFgMaterialNum,
					projCodeWVersion, tapeName, pkgCode, filedValue });
		} else {
			sql = sql
					+ "select * from PIDB_MP_LIST where IC_FG_MATERIAL_NUM = ? and PROJ_CODE_W_VERSION = ? and TAPE_NAME = ? and PKG_CODE = ? and "
					+ filed + " is null ";

			rm = new GenericRowMapper<MpListTo>(MpListTo.class);
			result = sjt.query(sql, rm, new Object[] { icFgMaterialNum,
					projCodeWVersion, tapeName, pkgCode });
		}

		if (result != null && result.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public void updateRemark(final MpListTo newInstance) {
		if (newInstance == null) {
			logger.error("Error the parameter is null.");
		}
		TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(
					final TransactionStatus status) {
				SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
				String sql = "update PIDB_MP_LIST a set a.REMARK=?  where a.prod_code "
						+ " in( SELECT c.prod_code FROM pidb_product c where c.prod_name in "
						+ " ( SELECT b.prod_name  FROM pidb_product b where b.prod_code=? ) ) ";
				logger.debug(sql);
				sjt.update(sql, newInstance.getRemark(), newInstance
						.getProdCode());
			}
		};
		doInTransaction(callback);
	}

	public void updateTapeVendor(final MpListTo newInstance) {
		if (newInstance == null) {
			logger.error("Error the parameter is null.");
		}
		TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(
					final TransactionStatus status) {
				SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
				String sql = "update PIDB_MP_LIST a set a.REMARK=?, a.APPROVE_TAPE_VENDOR=?  where a.tape_name "
						+ " in( SELECT c.tape_name FROM pidb_ic_tape c where c.tape_name = ? ) ";
				logger.debug(sql);
				sjt.update(sql, newInstance.getRemark(), newInstance
						.getApproveTapeVendor(), newInstance.getTapeName());
			}
		};
		doInTransaction(callback);
	}

	public void updateBumpVendor(final MpListTo newInstance) {
		if (newInstance == null) {
			logger.error("Error the parameter is null.");
		}
		TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(
					final TransactionStatus status) {
				SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
				String sql = "update PIDB_MP_LIST a set a.REMARK=? , a.APPROVE_BP_VENDOR=?  where a.proj_code "
						+ " in( SELECT c.proj_code FROM pidb_project_code c where c.prol_name in "
						+ " ( SELECT b.proj_name  FROM pidb_project_code b where b.proj_code=? ) ) ";
				logger.debug(sql);
				sjt.update(sql, newInstance.getRemark(), newInstance
						.getApproveBpVendor(), newInstance.getProjCode());
			}
		};
		doInTransaction(callback);
	}

	public void updateCpVendor(final MpListTo newInstance) {
		if (newInstance == null) {
			logger.error("Error the parameter is null.");
		}
		TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(
					final TransactionStatus status) {
				SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
				String sql = "update PIDB_MP_LIST a set a.REMARK=?, a.APPROVE_CP_HOUSE=?  where a.prod_code "
						+ " in( SELECT c.prod_code FROM pidb_product c where c.prod_name in "
						+ " ( SELECT b.prod_name  FROM pidb_product b where b.prod_code=? ) ) ";
				logger.debug(sql);
				sjt.update(sql, newInstance.getRemark(), newInstance
						.getApproveCpHouse(), newInstance.getProdCode());
			}
		};
		doInTransaction(callback);
	}

	public void updateAssyVendor(final MpListTo newInstance) {
		if (newInstance == null) {
			logger.error("Error the parameter is null.");
		}
		TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(
					final TransactionStatus status) {
				SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
				String sql = "update PIDB_MP_LIST a set a.REMARK=?, a.APPROVE_ASSY_HOUSE=?, a.APPROVE_FT_VENDOR=?  where a.tape_name "
						+ " in( SELECT c.tape_name FROM PIDB_IC_TAPE c where c.tape_name=?  ) ";
				logger.debug(sql);
				sjt.update(sql, newInstance.getRemark(), newInstance
						.getApproveAssyHouse(),
						newInstance.getApproveFtHouse(), newInstance
								.getTapeName());
			}
		};
		doInTransaction(callback);
	}

	public void updateFTVendor(final MpListTo newInstance) {
		if (newInstance == null) {
			logger.error("Error the parameter is null.");
		}
		TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(
					final TransactionStatus status) {
				SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
				String sql = "update PIDB_MP_LIST a set a.REMARK=?, a.APPROVE_ASSY_HOUSE=?, a.APPROVE_FT_VENDOR=?  where a.part_num = ? ) ";
				logger.debug(sql);
				sjt.update(sql, newInstance.getRemark(), newInstance
						.getApproveAssyHouse(),
						newInstance.getApproveFtHouse(), newInstance
								.getPartNum());
			}
		};
		doInTransaction(callback);
	}

	/**
	 * .
	 * 
	 * @param materialNum
	 *            String
	 * @return List
	 */
	public List<MpListEolCustTo> findByEolCust(final MpListTo newInstance) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_MP_LIST_EOL where PART_NUM='"
				+ newInstance.getPartNum() + "'" + " AND IC_FG_MATERIAL_NUM='"
				+ newInstance.getIcFgMaterialNum() + "'" + " AND PKG_CODE='"
				+ newInstance.getPkgCode() + "'" + " AND PROJ_CODE_W_VERSION='"
				+ newInstance.getProjCodeWVersion() + "'" + " AND TAPE_NAME='"
				+ newInstance.getTapeName() + "'";
		logger.debug(sql);

		return sjt.query(sql, new GenericRowMapper<MpListEolCustTo>(
				MpListEolCustTo.class), new Object[] {});
	}

	/**
	 * create an MP_LIST object.
	 * 
	 * @param newInstance
	 */
	public void insert(final MpListTo newInstance) {
		if (null == newInstance) {
			logger.error("Error the new IcWafer object is null.");
		}
		TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(
					final TransactionStatus status) {

				SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();

				String sql = "insert into PIDB_MP_LIST "
						+ "(PART_NUM,IC_FG_MATERIAL_NUM, "
						+ " PROD_CODE,PKG_CODE,PROJ_CODE,PROJ_CODE_W_VERSION,TAPE_NAME,"
						+ " MP_STATUS,MP_RELEASE_DATE,APPROVE_CUST,APPROVE_TAPE_VENDOR,"
						+ " APPROVE_BP_VENDOR,APPROVE_CP_HOUSE,APPROVE_ASSY_HOUSE,ASSIGN_TO,"
						+ " ASSIGN_EMAIL,CREATED_BY,MODIFIED_BY,MP_TRAY_DRAWING_NO1,"
						+ " MP_TRAY_DRAWING_NO_VER1,MP_COLOR1,MP_CUSTOMER_NAME1,MP_TRAY_DRAWING_NO2,"
						+ " MP_TRAY_DRAWING_NO_VER2,MP_COLOR2,MP_CUSTOMER_NAME2,MP_TRAY_DRAWING_NO3,"
						+ " MP_TRAY_DRAWING_NO_VER3,MP_COLOR3,MP_CUSTOMER_NAME3,MP_TRAY_DRAWING_NO4,"
						+ " MP_TRAY_DRAWING_NO_VER4,MP_COLOR4,MP_CUSTOMER_NAME4,PROCESS_FLOW,"
						+ " MAT_TAPE,MAT_WF,MAT_BP,MAT_CP,MAT_AS,REMARK,APPROVE_FT_HOUSE,EOL_CUST,"
						+ " REVISION_ITEM,UPDATE_TIME,CDT,APPROVE_POLISH_VENDOR,MAT_POLISH,"
						+ " APPROVE_COLOR_FILTER_VENDOR,MAT_CF,APPROVE_WAFER_CF_VENDOR," 
						+ "MAT_WAFERCF,MAT_CSP,APPROVE_CP_CSP_VENDOR,CP_BIN,"
						+ "MAT_TSV,APPROVE_CP_TSV_VENDOR)"
						+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?" +
								",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?" +
								",?,?,?,?,?,?,?,?,?,?,?)";
				logger.debug(sql);
				sjt.update(sql, newInstance.getPartNum(), newInstance
						.getIcFgMaterialNum(), newInstance.getProdCode(),
						newInstance.getPkgCode(), newInstance.getProjCode(),
						newInstance.getProjCodeWVersion(), newInstance
								.getTapeName(), newInstance.getMpStatus(),
						newInstance.getMpReleaseDate(), newInstance
								.getApproveCust(), newInstance
								.getApproveTapeVendor(), newInstance
								.getApproveBpVendor(), newInstance
								.getApproveCpHouse(), newInstance
								.getApproveAssyHouse(), newInstance
								.getAssignTo(), newInstance.getAssignEmail(),
						newInstance.getCreatedBy(),
						newInstance.getModifiedBy(), newInstance
								.getMpTrayDrawingNo1(), newInstance
								.getMpTrayDrawingNoVer1(), newInstance
								.getMpColor1(), newInstance
								.getMpCustomerName1(), newInstance
								.getMpTrayDrawingNo2(), newInstance
								.getMpTrayDrawingNoVer2(), newInstance
								.getMpColor2(), newInstance
								.getMpCustomerName2(), newInstance
								.getMpTrayDrawingNo3(), newInstance
								.getMpTrayDrawingNoVer3(), newInstance
								.getMpColor3(), newInstance
								.getMpCustomerName3(), newInstance
								.getMpTrayDrawingNo4(), newInstance
								.getMpTrayDrawingNoVer4(), newInstance
								.getMpColor4(), newInstance
								.getMpCustomerName4(), newInstance
								.getProcessFlow(), newInstance.getMatTape(),
						newInstance.getMatWf(), newInstance.getMatBp(),
						newInstance.getMatCp(), newInstance.getMatAs(),
						newInstance.getRemark(), newInstance
								.getApproveFtHouse(), newInstance.getEolCust(),
						newInstance.getRevisionItem(), newInstance
								.getUpdateTime(), newInstance.getCdt(),
						newInstance.getApprovePolishVendor(), newInstance
								.getMatPolish(), newInstance
								.getApproveColorFilterVendor(), newInstance
								.getMatCf(), newInstance
								.getApproveWaferCfVendor(), newInstance
								.getMatWafercf(), newInstance.getMatCsp(),
						newInstance.getApproveCpCspVendor(),newInstance.getCpBin(),
						newInstance.getMatTsv(),newInstance.getApproveCpTsvVendor()
						
				);

			}
		};
		doInTransaction(callback);
	}

	public void update(final MpListTo newInstance) {
		if (newInstance == null) {
			logger.error("Error the parameter is null.");
		}
		TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(
					final TransactionStatus status) {
				SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
				String sql = "update PIDB_MP_LIST set "
						+ "PROD_CODE = ?,"
						+ "PROJ_CODE = ?,"
						+ "MP_STATUS = ?,"
						+ "MP_RELEASE_DATE = ?,"
						+ "APPROVE_CUST = ?,"
						+ "APPROVE_TAPE_VENDOR = ?,"
						+ "APPROVE_BP_VENDOR = ?,"
						+ "APPROVE_CP_HOUSE = ?,"
						+ "APPROVE_ASSY_HOUSE = ?,"
						+ "ASSIGN_TO = ?,"
						+ "ASSIGN_EMAIL = ?,"
						+ "CREATED_BY = ?,"
						+ "MODIFIED_BY = ?,"
						+ "MP_TRAY_DRAWING_NO1 = ?,"
						+ "MP_TRAY_DRAWING_NO_VER1 = ?,"
						+ "MP_COLOR1 = ?,"
						+ "MP_CUSTOMER_NAME1 = ?,"
						+ "MP_TRAY_DRAWING_NO2 = ?,"
						+ "MP_TRAY_DRAWING_NO_VER2 = ?,"
						+ "MP_COLOR2 = ?,"
						+ "MP_CUSTOMER_NAME2 = ?,"
						+ "MP_TRAY_DRAWING_NO3 = ?,"
						+ "MP_TRAY_DRAWING_NO_VER3 = ?,"
						+ "MP_COLOR3 = ?,"
						+ "MP_CUSTOMER_NAME3 = ?,"
						+ "MP_TRAY_DRAWING_NO4 = ?,"
						+ "MP_TRAY_DRAWING_NO_VER4 = ?,"
						+ "MP_COLOR4 = ?,"
						+ "MP_CUSTOMER_NAME4 = ?,"
						+ "PROCESS_FLOW = ?,"
						+ "MAT_TAPE = ?,"
						+ "MAT_WF = ?,"
						+ "MAT_BP = ?,"
						+ "MAT_CP = ?,"
						+ "MAT_AS = ?,"
						+ "REMARK = ?,"
						+ "APPROVE_FT_HOUSE = ?,"
						+ "EOL_CUST = ?,"
						+ "REVISION_ITEM = ?,"
						+ "UPDATE_TIME = ?,"
						+ "CDT = ?,"
						+ "APPROVE_POLISH_VENDOR = ?,"
						+ "MAT_POLISH = ?,"
						+ "APPROVE_COLOR_FILTER_VENDOR = ?,"
						+ "MAT_CF = ?,"
						+ "APPROVE_WAFER_CF_VENDOR = ?,"
						+ "MAT_WAFERCF = ?,"
						+ "MAT_CSP=?,"
						+ "APPROVE_CP_CSP_VENDOR=?, "
						+ "CP_BIN=?, "
						+ "MAT_TSV=?, "
						+ "APPROVE_CP_TSV_VENDOR=? "
						+ "where PART_NUM = ? and IC_FG_MATERIAL_NUM = ? and PKG_CODE = ?"
						+ " and PROJ_CODE_W_VERSION = ? and TAPE_NAME = ?";
				logger.debug(sql);
				sjt.update(sql, newInstance.getProdCode(), newInstance
						.getProjCode(), newInstance.getMpStatus(), newInstance
						.getMpReleaseDate(), newInstance.getApproveCust(),
						newInstance.getApproveTapeVendor(), newInstance
								.getApproveBpVendor(), newInstance
								.getApproveCpHouse(), newInstance
								.getApproveAssyHouse(), newInstance
								.getAssignTo(), newInstance.getAssignEmail(),
						newInstance.getCreatedBy(),
						newInstance.getModifiedBy(), newInstance
								.getMpTrayDrawingNo1(), newInstance
								.getMpTrayDrawingNoVer1(), newInstance
								.getMpColor1(), newInstance
								.getMpCustomerName1(), newInstance
								.getMpTrayDrawingNo2(), newInstance
								.getMpTrayDrawingNoVer2(), newInstance
								.getMpColor2(), newInstance
								.getMpCustomerName2(), newInstance
								.getMpTrayDrawingNo3(), newInstance
								.getMpTrayDrawingNoVer3(), newInstance
								.getMpColor3(), newInstance
								.getMpCustomerName3(), newInstance
								.getMpTrayDrawingNo4(), newInstance
								.getMpTrayDrawingNoVer4(), newInstance
								.getMpColor4(), newInstance
								.getMpCustomerName4(), newInstance
								.getProcessFlow(), newInstance.getMatTape(),
						newInstance.getMatWf(), newInstance.getMatBp(),
						newInstance.getMatCp(), newInstance.getMatAs(),
						newInstance.getRemark(), newInstance
								.getApproveFtHouse(), newInstance.getEolCust(),
						newInstance.getRevisionItem(), newInstance
								.getUpdateTime(), newInstance.getCdt(),
						newInstance.getApprovePolishVendor(), newInstance
								.getMatPolish(), newInstance
								.getApproveColorFilterVendor(), newInstance
								.getMatCf(), newInstance
								.getApproveWaferCfVendor(), newInstance
								.getMatWafercf(), newInstance.getMatCsp(),
						newInstance.getApproveCpCspVendor(),
						newInstance.getCpBin(), 
						newInstance.getMatTsv(), 
						newInstance.getApproveCpTsvVendor(), 
						newInstance.getPartNum(),
						newInstance.getIcFgMaterialNum(), newInstance
								.getPkgCode(), newInstance
								.getProjCodeWVersion(), newInstance
								.getTapeName());
			}
		};
		doInTransaction(callback);
	}

}
