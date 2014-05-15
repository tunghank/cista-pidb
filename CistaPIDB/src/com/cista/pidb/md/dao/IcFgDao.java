package com.cista.pidb.md.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.cista.pidb.code.dao.SapMasterCustomerDao;
import com.cista.pidb.code.to.SapMasterCustomerTo;
import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.md.to.IcFgQueryTo;
import com.cista.pidb.md.to.IcFgTo;
import com.cista.pidb.md.to.ProductTo;
import com.cista.pidb.md.to.ProjectTo;

public class IcFgDao extends PIDBDaoSupport {

	public IcFgTo find(String partNum) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_IC_FG where PART_NUM = ?";
		logger.debug(sql);
		GenericRowMapper<IcFgTo> rm = new GenericRowMapper<IcFgTo>(IcFgTo.class);
		List<IcFgTo> result = sjt.query(sql, rm, new Object[] { partNum });
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	public List<IcFgTo> findByFtTestProgName(String ftTestProgName) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_IC_FG where 1=1 and STATUS = 'Released' ";
		// if (ftTestProgName!=null && ftTestProgName.length()>0) {
		sql += " and pidb_include(FT_TEST_PROG_LIST, ',', "
				+ getSQLString(ftTestProgName) + ")>=1 ";
		// }
		logger.debug(sql);
		GenericRowMapper<IcFgTo> rm = new GenericRowMapper<IcFgTo>(IcFgTo.class);
		List<IcFgTo> result = sjt.query(sql, rm, new Object[] {});

		return result;
	}

	public int countResult(IcFgQueryTo queryTo) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select count(*) from (PIDB_IC_FG pif left join PIDB_PRODUCT ppd on pif.PROD_CODE=ppd.PROD_CODE) " +
				" left join (PIDB_PROJECT_CODE ppjc left join PIDB_PROJECT ppj on ppjc.PROJ_NAME=ppj.PROJ_NAME) " +
				" on pif.PROJ_CODE=ppjc.PROJ_CODE where 1 = 1";
		if (queryTo.getReleaseTo() != null
				&& !queryTo.getReleaseTo().equals("")) {
			if (queryTo.getReleaseTo().equals("HX")
					|| queryTo.getReleaseTo().equals("WP")) {
				sql += " AND (ppjc.RELEASE_TO ='" + queryTo.getReleaseTo()
						+ "'" + " or ppjc.RELEASE_TO ='ALL')";
			}
		}
		String whereCause = generateWhereCause(queryTo);
		sql += whereCause;
		logger.debug(sql);
		return sjt.queryForInt(sql, new Object[] {});
	}

	public List<IcFgQueryTo> query(final IcFgQueryTo queryTo) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select "
				+ "pif.MATERIAL_NUM,"
				+ "pif.PKG_CODE,"
				+ "(select DESCRIPTION from WM_SAP_MASTER_PACKAGE_TYPE wsmpt where wsmpt.PACKAGE_TYPE=pif.PKG_TYPE) PKG_TYPE,"
				+ "pif.PART_NUM,"
				+ "pif.PROD_CODE,"
				+ "ppj.FAB,"
				+ "ppjc.PROJ_OPTION,"
				+ "pif.PROJ_CODE,"
				+ "pif.ROUTING_FG,"
				+ "pif.ROUTING_AS,"
				+ "pif.MP_STATUS,"
				+ "pif.CUST,"
				+ "pif.AP_MODEL,"
				+ "pif.MCP_PKG,"
				+ "pif.STATUS,"
				+ "ppjc.RELEASE_TO "
				+ " from (PIDB_IC_FG pif left join PIDB_PRODUCT ppd on pif.PROD_CODE=ppd.PROD_CODE) left join (PIDB_PROJECT_CODE ppjc left join PIDB_PROJECT ppj on ppjc.PROJ_NAME=ppj.PROJ_NAME) on pif.PROJ_CODE=ppjc.PROJ_CODE where 1 = 1 ";
		if (queryTo.getReleaseTo() != null
				&& !queryTo.getReleaseTo().equals("")) {
			if (queryTo.getReleaseTo().equals("HX")
					|| queryTo.getReleaseTo().equals("WP")) {
				sql += " AND (ppjc.RELEASE_TO ='" + queryTo.getReleaseTo()
						+ "'" + " or ppjc.RELEASE_TO ='ALL')";
			}
		}
		String whereCause = generateWhereCause(queryTo);
		sql += whereCause;
		sql += " order by pif.PART_NUM";

		logger.debug(sql);
		GenericRowMapper<IcFgQueryTo> rm = new GenericRowMapper<IcFgQueryTo>(
				IcFgQueryTo.class);

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

	public List<IcFgTo> queryForDomain(final IcFgQueryTo queryTo) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select "
				+ "pif.MATERIAL_NUM,"
				+ "pif.PROD_CODE,"
				+ "pif.VARIANT,"
				+ "pif.PROJ_CODE,"
				+ "(select DESCRIPTION from WM_SAP_MASTER_PACKAGE_TYPE wsmpt where wsmpt.PACKAGE_TYPE=pif.PKG_TYPE) PKG_TYPE,"
				+ "pif.PKG_CODE,"
				+ "pif.PART_NUM,"
				+ "pif.ROUTING_FG,"
				+ "pif.ROUTING_AS,"
				+ "pif.MP_STATUS,"
				+ "(select SHORT_NAME from WM_SAP_MASTER_CUSTOMER wsmc where wsmc.CUSTOMER_CODE=pif.CUST) CUST,"
				+ "(SELECT A.LF_TOOL FROM PIDB_TRADITIONAL_PKG A WHERE A.PKG_NAME =pif.PART_NUM) LF_TOOL,"
				+ "(SELECT A.CLOSE_LF_NAME FROM PIDB_TRADITIONAL_PKG A WHERE A.PKG_NAME =pif.PART_NUM) CLOSE_LF_NAME,"
				+ "pif.AP_MODEL,"
				+ "(select DESCRIPTION from WM_SAP_MASTER_APP_CATEGORY wsmac where wsmac.APPLICATION_CATEGORY=pif.APP_CATEGORY) APP_CATEGORY,"
				+ "pif.MCP_DIE_QTY,"
				+ "pif.MCP_PKG,"
				+ "pif.MCP_PROD1,"
				+ "pif.MCP_PROD2,"
				+ "pif.MCP_PROD3,"
				+ "pif.MCP_PROD4,"
				+ "pif.REMARK,"
				+ "pif.CP_MATERIAL_NUM,"
				+ "pif.CP_TEST_PROG_NAME_LIST,"
				+ "pif.FT_TEST_PROG_LIST,"
				+ "pif.ASSIGN_TO,"
				+ "pif.ASSIGN_EMAIL,"
				+ "pif.STATUS,"
				+ "pif.CREATED_BY,"
				+ "pif.MODIFIED_BY,"
				+ "pif.VENDOR_DEVICE,"
				+ "ppjc.RELEASE_TO"
				+ " from (PIDB_IC_FG pif left join PIDB_PRODUCT ppd on pif.PROD_CODE=ppd.PROD_CODE) left join (PIDB_PROJECT_CODE ppjc left join PIDB_PROJECT ppj on ppjc.PROJ_NAME=ppj.PROJ_NAME) on pif.PROJ_CODE=ppjc.PROJ_CODE where 1 = 1 ";
		if (queryTo.getReleaseTo() != null
				&& !queryTo.getReleaseTo().equals("")) {
			if (queryTo.getReleaseTo().equals("HX")
					|| queryTo.getReleaseTo().equals("WP")) {
				sql += " AND (ppjc.RELEASE_TO ='" + queryTo.getReleaseTo()
						+ "'" + " or ppjc.RELEASE_TO ='ALL')";
			}
		}
		String whereCause = generateWhereCause(queryTo);
		sql += whereCause;
		sql += " order by pif.PART_NUM";

		logger.debug(sql);
		if (queryTo.getPageNo() > 0) {
			int cursorFrom = (queryTo.getPageNo() - 1) * queryTo.getPageSize()
					+ 1;
			int cursorTo = (queryTo.getPageNo()) * queryTo.getPageSize();
			return sjt
					.query(getPagingSql(sql, cursorFrom, cursorTo),
							new GenericRowMapper<IcFgTo>(IcFgTo.class),
							new Object[] {});
		} else {
			return sjt.query(sql, new GenericRowMapper<IcFgTo>(IcFgTo.class),
					new Object[] {});
		}
	}

	private String generateWhereCause(final IcFgQueryTo queryTo) {
		StringBuilder sb = new StringBuilder();
		if (queryTo.getMaterialNum() != null
				&& !queryTo.getMaterialNum().equals("")) {
			String materialNum = getSmartSearchQueryString("pif.MATERIAL_NUM",
					queryTo.getMaterialNum());
			if (materialNum != null) {
				sb.append(" and (" + materialNum + " )");
			}
		}

		if (queryTo.getPkgCode() != null && !queryTo.getPkgCode().equals("")) {
			String pkgCode = getSmartSearchQueryString("pif.PKG_CODE", queryTo
					.getPkgCode());
			if (pkgCode != null) {
				sb.append(" and ( " + pkgCode + " )");
			}
		}

		if (queryTo.getPartNum() != null && !queryTo.getPartNum().equals("")) {
			String partNum = getSmartSearchQueryString("pif.PART_NUM", queryTo
					.getPartNum());
			if (partNum != null) {
				sb.append(" and ( " + partNum + " )");
			}
		}

		if (queryTo.getProdCode() != null && !queryTo.getProdCode().equals("")) {
			String prodCode = getSmartSearchQueryAllLike("pif.PROD_CODE",
					queryTo.getProdCode());
			if (prodCode != null) {
				sb.append(" and ( " + prodCode + " )");
			}
		}

		if (queryTo.getFab() != null && !queryTo.getFab().equals("")) {
			sb.append(" and ppj.FAB = " + getSQLString(queryTo.getFab()) + " ");
		}

		if (queryTo.getProjOption() != null
				&& !queryTo.getProjOption().equals("")) {
			sb.append(" and ppjc.PROJ_OPTION = "
					+ getSQLString(queryTo.getProjOption()) + " ");
		}

		if (queryTo.getProjCode() != null && !queryTo.getProjCode().equals("")) {
			String projCode = getSmartSearchQueryAllLike("ppjc.PROJ_CODE",
					queryTo.getProjCode());
			if (projCode != null) {
				sb.append(" and ( " + projCode + " )");
			}
		}

		if (queryTo.getRoutingFg() != null
				&& !queryTo.getRoutingFg().equals("")) {
			sb.append(" and pif.ROUTING_FG = "
					+ getSQLString(queryTo.getRoutingFg()) + " ");
		}

		if (queryTo.getRoutingAs() != null
				&& !queryTo.getRoutingAs().equals("")) {
			sb.append(" and pif.ROUTING_AS = "
					+ getSQLString(queryTo.getRoutingAs()) + " ");
		}

		// sb.append(" and pif.ROUTING_FG = "
		// + getSQLString(queryTo.getRoutingFg() ? 1 + "" : 0 + "") + " ");
		//
		// sb.append(" and pif.ROUTING_AS = "
		// + getSQLString(queryTo.getRoutingAs() ? 1 + "" : 0 + "") + " ");

		if (queryTo.getMpStatus() != null && !queryTo.getMpStatus().equals("")) {
			sb.append(" and pif.MP_STATUS = "
					+ getSQLString(queryTo.getMpStatus()) + " ");
		}

		if (queryTo.getCust() != null && !queryTo.getCust().equals("")) {
			// 將smartSearch的值轉換成資料庫所存的值
			String field = queryTo.getCust();
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
				}
			}

			String approveCust = getSmartSearchQueryAllLike("pif.CUST",
					realOwner);
			if (realOwner != null) {
				sb.append("and (" + approveCust + ")");
			}
		}

		if (queryTo.getApModel() != null && !queryTo.getApModel().equals("")) {
			String apModel = getSmartSearchQueryString("pif.AP_MODEL", queryTo
					.getApModel());
			if (apModel != null) {
				sb.append(" and ( " + apModel + " )");
			}
		}

		if (queryTo.getMcpPkg() != null && !queryTo.getMcpPkg().equals("")) {
			sb.append(" and pif.MCP_PKG = " + getSQLString(queryTo.getMcpPkg())
					+ " ");
		}

		if (queryTo.getStatus() != null && !queryTo.getStatus().equals("")) {
			sb.append(" and pif.STATUS = " + getSQLString(queryTo.getStatus())
					+ " ");
		}

		/*
		 * if (queryTo.getReleaseTo() != null &&
		 * !queryTo.getReleaseTo().equals("")) { String releaseTo =
		 * getSmartSearchQueryString("ppjc.RELEASE_TO", queryTo.getReleaseTo());
		 * if (releaseTo != null) { sb.append(" and ( " + releaseTo + " )"); } }
		 */

		return sb.toString();
	}

	public List<IcFgTo> findAll() {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_IC_FG";
		logger.debug(sql);
		List<IcFgTo> result = stj.query(sql, new GenericRowMapper<IcFgTo>(
				IcFgTo.class), new Object[] {});
		return result;
	}

	public List<IcFgTo> findPartNumAll() {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select distinct PART_NUM,PROD_CODE,MATERIAL_NUM,PKG_CODE from PIDB_IC_FG order by PART_NUM";
		logger.debug(sql);
		List<IcFgTo> result = stj.query(sql, new GenericRowMapper<IcFgTo>(
				IcFgTo.class), new Object[] {});
		return result;
	}

	public List<IcFgTo> findPartNum() {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select distinct PART_NUM from PIDB_IC_FG order by PART_NUM";
		logger.debug(sql);
		List<IcFgTo> result = stj.query(sql, new GenericRowMapper<IcFgTo>(
				IcFgTo.class), new Object[] {});
		return result;
	}

	public List<IcFgTo> findPkgCode() {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select distinct PKG_CODE from PIDB_IC_FG order by PKG_CODE";
		logger.debug(sql);
		return sjt.query(sql, new GenericRowMapper<IcFgTo>(IcFgTo.class),
				new Object[] {});
	}

	/**
	 * MD-14 Query product code from PIDB_IC_FG.
	 * 
	 * @return List
	 */
	public List<String> findProdCode() {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select distinct PROD_CODE from PIDB_IC_FG order by PROD_CODE";
		logger.debug(sql);
		List<Map<String, Object>> result = stj.queryForList(sql,
				new Object[] {});
		List<String> prodCode = new ArrayList<String>();
		if (result != null && result.size() > 0) {
			for (Map<String, Object> item : result) {
				prodCode.add((String) item.get("PROD_CODE"));
			}
		}

		return prodCode;
	}

	public IcFgTo findByMaterialNum(String materialNum) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_IC_FG where MATERIAL_NUM = ?";
		logger.debug(sql);
		GenericRowMapper<IcFgTo> rm = new GenericRowMapper<IcFgTo>(IcFgTo.class);
		List<IcFgTo> result = sjt.query(sql, rm, new Object[] { materialNum });
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	public String findProdCodeByPartNum(String partNum) {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select PROD_CODE from PIDB_IC_FG where PART_NUM = ?";
		logger.debug(sql);
		List<Map<String, Object>> result = stj.queryForList(sql,
				new Object[] { partNum });
		String prodCode = "";
		if (result != null && result.size() > 0) {
			Map<String, Object> item = result.get(0);
			prodCode = (String) item.get("PROD_CODE");
		}
		return prodCode;
	}

	public Object findMaxVar(String prodCode, String projCode, String pkgCode) {
		//        
		// ProjectCodeDao projectCodeDao = new ProjectCodeDao();
		// ProjectCodeTo projectCodeTo =
		// projectCodeDao.findByProjectCode(projCode);
		//        
		// List<String> projCodeList =
		// projectCodeDao.findProjCode(projectCodeTo.getProjName());
		// String projCodes = "";
		// if (projCodeList != null && projCodeList.size() > 0) {
		// for (String pc : projCodeList) {
		// if(StringUtils.isNotEmpty(pc)) {
		// projCodes += "," + pc;
		// }
		// }
		// }
		// if (projCodes.length() > 0) {
		// projCodes = projCodes.substring(1);
		// }
		ProductDao productDao = new ProductDao();
		ProductTo productTo = productDao.findByProdCode(prodCode);

		String prodName = "";
		if (productTo != null) {
			prodName = productTo.getProdName();
		}

		List<ProductTo> productToList = productDao.findByProdName(prodName);
		String productToStr = "";
		if (productToList != null) {
			for (ProductTo pt : productToList) {
				productToStr += ",'" + pt.getProdCode() + "'";
			}
		}
		if (productToStr.length() > 0) {
			productToStr = productToStr.substring(1);
		}

		ProjectDao projectDao = new ProjectDao();
		ProjectTo projectTo = projectDao.findByProjectCode(projCode);

		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select max(VARIANT) from (select tp.VARIANT,ppc.PROJ_OPTION,pp.FAB from (select PROJ_CODE,VARIANT from PIDB_IC_FG where PROD_CODE in ("
				+ productToStr
				+ ") and PKG_CODE=?) tp, PIDB_PROJECT_CODE ppc,PIDB_PROJECT pp where tp.PROJ_CODE=ppc.PROJ_CODE and ppc.PROJ_NAME=pp.PROJ_NAME) where PROJ_OPTION=? and FAB=?";
		// String sql = "select max(VARIANT) from "
		// + "(select pp.PROD_NAME,pif.PROJ_CODE,pif.PKG_CODE,pif.VARIANT from
		// PIDB_IC_FG pif,PIDB_PRODUCT pp where pif.PROD_CODE=pp.PROD_CODE)"
		// + " where PROD_NAME=? and PROJ_CODE=? and PKG_CODE=?";
		logger.debug(sql);
		Object obj = sjt.queryForObject(sql, Object.class, new Object[] {
				pkgCode, projectTo.getProjOption(), projectTo.getFab() });
		return obj;
	}

	public List<IcFgTo> findDistinctPartNum() {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();

		String sql = "select distinct PART_NUM from PIDB_IC_FG where "
				+ getAssertEmptyString("PART_NUM") + " order by PART_NUM";
		logger.debug(sql);
		List<IcFgTo> result = stj.query(sql, new GenericRowMapper<IcFgTo>(
				IcFgTo.class), new Object[] {});
		return result;
	}

	public IcFgTo findByPrimaryKey(String materialNum, String partNum) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_IC_FG where MATERIAL_NUM = ? and PART_NUM = ?";
		logger.debug(sql);
		GenericRowMapper<IcFgTo> rm = new GenericRowMapper<IcFgTo>(IcFgTo.class);
		List<IcFgTo> result = sjt.query(sql, rm, new Object[] { materialNum,
				partNum });
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	public List<IcFgTo> findByPkgCode(final String pkgCode) {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_IC_FG where PKG_CODE = ?";
		logger.debug(sql);
		return stj.query(sql, new GenericRowMapper<IcFgTo>(IcFgTo.class),
				new Object[] { pkgCode });
	}

	public List<IcFgTo> findByPkgCodeForTradRelease(final String pkgName) {

		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_IC_FG where PART_NUM = ? and ROUTING_AS='1' and STATUS='Released'";
		logger.debug(sql);
		return stj.query(sql, new GenericRowMapper<IcFgTo>(IcFgTo.class),
				new Object[] { pkgName });
	}

	/**
	 * Find records with the PROJ_CODE specified. Added by fumingjie 2007/01/23,
	 * for ERP release.
	 * 
	 * @param projCode
	 *            PROJ_CODE
	 * @return List of IcFgTo
	 */
	public List<IcFgTo> findByProjCode(final String projCode) {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_IC_FG where PROJ_CODE = ? ";
		logger.debug(sql);

		GenericRowMapper<IcFgTo> mapper = new GenericRowMapper<IcFgTo>(
				IcFgTo.class);
		return stj.query(sql, mapper, new Object[] { projCode });
	}

	public List<IcFgTo> findByPartNum(String partNum) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_IC_FG where PART_NUM = ?";
		logger.debug(sql);
		GenericRowMapper<IcFgTo> rm = new GenericRowMapper<IcFgTo>(IcFgTo.class);
		return sjt.query(sql, rm, new Object[] { partNum });

	}

	public List<IcFgTo> findByCpMaterialNum(String cpMaterialNum) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_IC_FG where MATERIAL_NUM = ?";
		logger.debug(sql);
		GenericRowMapper<IcFgTo> rm = new GenericRowMapper<IcFgTo>(IcFgTo.class);
		return sjt.query(sql, rm, new Object[] { cpMaterialNum });

	}

	public IcFgTo findPartNum(String pkgCode, String prodName) {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select  a.part_num  from PIDB_IC_FG a  where a.pkg_code=? and a.prod_code"
				+ " in ( SELECT c.prod_code FROM pidb_product c where c.prod_name=? )";
		logger.debug(sql);
		List<IcFgTo> result = stj.query(sql, new GenericRowMapper<IcFgTo>(
				IcFgTo.class), new Object[] { pkgCode, prodName });

		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	/**
	 * create an ICFG object.
	 * 
	 * @param newInstance
	 */
	public void insert(final IcFgTo newInstance) {
		if (null == newInstance) {
			logger.error("Error the new IcFg object is null.");
		}
		TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(
					final TransactionStatus status) {
				/*
				 * int newSdramId = SequenceSupport
				 * .nextValue(SequenceSupport.SEQ_PIDB_SDRAM);
				 */

				SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();

				String sql = "insert into PIDB_IC_FG "
						+ "(MATERIAL_NUM,PROD_CODE,VARIANT,PROJ_CODE,PKG_TYPE,PKG_CODE,"
						+ "PART_NUM,ROUTING_FG,ROUTING_AS,MP_STATUS,CUST,AP_MODEL,"
						+ "APP_CATEGORY,MCP_DIE_QTY,MCP_PKG,MCP_PROD1,MCP_PROD2,"
						+ "MCP_PROD3,MCP_PROD4,REMARK,CP_MATERIAL_NUM,CP_TEST_PROG_NAME_LIST,"
						+ "FT_TEST_PROG_LIST,ASSIGN_TO,ASSIGN_EMAIL,STATUS," 
						+ "CREATED_BY,MODIFIED_BY, VENDOR_DEVICE )"
						+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				logger.debug(sql);
				sjt.update(sql, newInstance.getMaterialNum()
								, newInstance.getProdCode()
								, newInstance.getVariant()
								, newInstance.getProjCode()
								, newInstance.getPkgType()
								, newInstance.getPkgCode()
								, newInstance.getPartNum()
								, newInstance.getRoutingFg()
								, newInstance.getRoutingAs()
								, newInstance.getMpStatus()
								, newInstance.getCust()
								, newInstance.getApModel()
								, newInstance.getAppCategory()
								, newInstance.getMcpDieQty()
								, newInstance.getMcpPkg()
								, newInstance.getMcpProd1()
								, newInstance.getMcpProd2()
								, newInstance.getMcpProd3()
								, newInstance.getMcpProd4()
								, newInstance.getRemark()
								, newInstance.getCpMaterialNum()
								, newInstance.getCpTestProgNameList()
								, newInstance.getFtTestProgList()
								, newInstance.getAssignTo()
								, newInstance.getAssignEmail()
								, newInstance.getStatus()
								, newInstance.getCreatedBy()
								, newInstance.getModifiedBy()
								, newInstance.getVendorDevice()
								);
			}
		};
		doInTransaction(callback);
	}

	public void update(final IcFgTo newInstance) {
		if (newInstance == null) {
			logger.error("Error the parameter is null.");
		}
		TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(
					final TransactionStatus status) {
				SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
				String sql = "update PIDB_IC_FG set " + "PROD_CODE= ?,"
						+ "VARIANT= ?," + "PROJ_CODE= ?," + "PKG_TYPE= ?,"
						+ "PKG_CODE= ?," + "PART_NUM= ?," + "ROUTING_FG= ?,"
						+ "ROUTING_AS= ?," + "MP_STATUS= ?," + "CUST= ?,"
						+ "AP_MODEL= ?," + "APP_CATEGORY= ?,"
						+ "MCP_DIE_QTY= ?," + "MCP_PKG= ?," + "MCP_PROD1= ?,"
						+ "MCP_PROD2= ?," + "MCP_PROD3= ?," + "MCP_PROD4= ?,"
						+ "REMARK= ?," + "CP_MATERIAL_NUM= ?,"
						+ "CP_TEST_PROG_NAME_LIST= ?,"
						+ "FT_TEST_PROG_LIST= ?," + "ASSIGN_TO= ?,"
						+ "ASSIGN_EMAIL= ?," + "STATUS= ?," + "CREATED_BY= ?,"
						+ "MODIFIED_BY= ?, " 
						+ "VENDOR_DEVICE= ? " 
						+ " where MATERIAL_NUM= ? ";
				logger.debug(sql);
				sjt.update(sql, newInstance.getProdCode(), newInstance
						.getVariant(), newInstance.getProjCode(), newInstance
						.getPkgType(), newInstance.getPkgCode(), newInstance
						.getPartNum(), newInstance.getRoutingFg(), newInstance
						.getRoutingAs(), newInstance.getMpStatus(), newInstance
						.getCust(), newInstance.getApModel(), newInstance
						.getAppCategory(), newInstance.getMcpDieQty(),
						newInstance.getMcpPkg(), newInstance.getMcpProd1(),
						newInstance.getMcpProd2(), newInstance.getMcpProd3(),
						newInstance.getMcpProd4(), newInstance.getRemark(),
						newInstance.getCpMaterialNum(), newInstance
								.getCpTestProgNameList(), newInstance
								.getFtTestProgList(),
						newInstance.getAssignTo(),
						newInstance.getAssignEmail(), newInstance.getStatus(),
						newInstance.getCreatedBy(),
						newInstance.getModifiedBy(),
						newInstance.getVendorDevice(),
						newInstance.getMaterialNum());
			}
		};
		doInTransaction(callback);
	}

	public void delete(final IcFgTo newInstance) {
		if (newInstance == null) {
			logger.error("Error the parameter is null.");
		}
		TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(
					final TransactionStatus status) {
				SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
				String sql = "Delete PIDB_IC_FG Where MATERIAL_NUM= ? ";
				logger.debug(sql);
				sjt.update(sql, newInstance.getMaterialNum());
			}
		};
		doInTransaction(callback);
	}
	
	public void update(final IcFgTo newInstance, String tableName,
			Map<String, Object> key) {
		if (newInstance == null) {
			logger.error("Error the parameter is null.");
		}
		TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(
					final TransactionStatus status) {
				SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
				String sql = "update PIDB_IC_FG set " + "PROD_CODE= ?,"
						+ "VARIANT= ?," + "PROJ_CODE= ?," + "PKG_TYPE= ?,"
						+ "PKG_CODE= ?," + "PART_NUM= ?," + "ROUTING_FG= ?,"
						+ "ROUTING_AS= ?," + "MP_STATUS= ?," + "CUST= ?,"
						+ "AP_MODEL= ?," + "APP_CATEGORY= ?,"
						+ "MCP_DIE_QTY= ?," + "MCP_PKG= ?," + "MCP_PROD1= ?,"
						+ "MCP_PROD2= ?," + "MCP_PROD3= ?," + "MCP_PROD4= ?,"
						+ "REMARK= ?," + "CP_MATERIAL_NUM= ?,"
						+ "CP_TEST_PROG_NAME_LIST= ?,"
						+ "FT_TEST_PROG_LIST= ?," + "ASSIGN_TO= ?,"
						+ "ASSIGN_EMAIL= ?," + "STATUS= ?," + "CREATED_BY= ?,"
						+ "MODIFIED_BY= ? " + " where MATERIAL_NUM= ? ";
				logger.debug(sql);
				sjt.update(sql, newInstance.getProdCode(), newInstance
						.getVariant(), newInstance.getProjCode(), newInstance
						.getPkgType(), newInstance.getPkgCode(), newInstance
						.getPartNum(), newInstance.getRoutingFg(), newInstance
						.getRoutingAs(), newInstance.getMpStatus(), newInstance
						.getCust(), newInstance.getApModel(), newInstance
						.getAppCategory(), newInstance.getMcpDieQty(),
						newInstance.getMcpPkg(), newInstance.getMcpProd1(),
						newInstance.getMcpProd2(), newInstance.getMcpProd3(),
						newInstance.getMcpProd4(), newInstance.getRemark(),
						newInstance.getCpMaterialNum(), newInstance
								.getCpTestProgNameList(), newInstance
								.getFtTestProgList(),
						newInstance.getAssignTo(),
						newInstance.getAssignEmail(), newInstance.getStatus(),
						newInstance.getCreatedBy(),
						newInstance.getModifiedBy(), newInstance
								.getMaterialNum());
			}
		};
		doInTransaction(callback);
	}

	public List<IcFgTo> findByProdName(String prodName) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "SELECT * FROM  PIDB_IC_FG pif WHERE pif.prod_code in "
				+ "(SELECT ppd.prod_code FROM PIDB_PRODUCT ppd WHERE ppd.prod_name =?) ";
		logger.debug(sql);
		GenericRowMapper<IcFgTo> rm = new GenericRowMapper<IcFgTo>(IcFgTo.class);
		return sjt.query(sql, rm, new Object[] { prodName });

	}
	
	public IcFgTo findPkgType(String pkgCode, String prodName) {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select  *  from PIDB_IC_FG a  where a.pkg_code=? and a.prod_code"
				+ " in ( SELECT c.prod_code FROM pidb_product c where c.prod_name=? )";
		logger.debug(sql);
		List<IcFgTo> result = stj.query(sql, new GenericRowMapper<IcFgTo>(
				IcFgTo.class), new Object[] { pkgCode, prodName });

		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}
}
