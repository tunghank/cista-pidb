package com.cista.pidb.md.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

import com.cista.pidb.admin.to.ParameterTo;
import com.cista.pidb.code.dao.SapMasterVendorDao;
import com.cista.pidb.code.to.SapMasterVendorTo;
import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.md.to.PkgRaQueryTo;
import com.cista.pidb.md.to.PkgRaTo;
import com.cista.pidb.md.to.ProductTo;
import com.cista.pidb.md.to.ProjectTo;

public class PkgRaDao extends PIDBDaoSupport {

	public void insertPkgRa(final PkgRaTo newInstance) {
		super.insert(newInstance, "PIDB_PACKAGE_RA");
	}

	public void updatePkgRa(final PkgRaTo newInstance) {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put("PROD_NAME", newInstance.getProdName());
		keyMap.put("PKG_CODE", newInstance.getPkgCode());
		keyMap.put("WORKSHEET_NUMBER", newInstance.getWorksheetNumber());
		keyMap.put("PKG_TYPE", newInstance.getPkgType());
		super.update(newInstance, "PIDB_PACKAGE_RA", keyMap);
	}

	public List<PkgRaTo> queryForDownload(final PkgRaQueryTo queryTo) {
		SimpleJdbcTemplate sjt = super.getSimpleJdbcTemplate();
		String sql = "select * from PIDB_PACKAGE_RA where 1=1";
		String whereCause = generateWhereCause(queryTo);
		sql += whereCause;
		GenericRowMapper<PkgRaTo> rm = new GenericRowMapper<PkgRaTo>(
				PkgRaTo.class);
		List<PkgRaTo> result = null;
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
			String funName = "RA";
			String funField = "OWNER";
			for (PkgRaTo t : result) {
				if (t.getOwner() == null) {
					t.setOwner("");
				} else if (!t.getOwner().equals("") && t.getOwner() != null) {
					ParameterTo to = findByFieldValue(funName, funField, t
							.getOwner());
					t.setOwner(to.getFieldShowName());

				}
			}
		}
		return result;
	}

	public List<PkgRaTo> query(final PkgRaQueryTo queryTo) {
		SimpleJdbcTemplate sjt = super.getSimpleJdbcTemplate();
		String[] prodCodes = null;
		if (queryTo.getProjName() != null && queryTo.getProjName().length() > 0) {
			ProjectDao projectDao = new ProjectDao();
			ProjectTo projectTo = projectDao.find(queryTo.getProjName());
			if (projectTo != null && projectTo.getProdCodeList() != null
					&& projectTo.getProdCodeList().length() > 0) {
				prodCodes = projectTo.getProdCodeList().split(",");

			}
		}
		List<String> prodNameList = new ArrayList<String>();
		if (prodCodes != null && prodCodes.length > 0) {
			ProductDao prodDao = new ProductDao();
			for (String prodCode : prodCodes) {
				ProductTo prodTo = prodDao.findByProdCode(prodCode);
				if (prodTo != null && prodTo.getProdName() != null
						&& prodTo.getProdName().length() > 0) {
					prodNameList.add(prodTo.getProdName());
				}
			}
		}

		String sql = "select * from PIDB_PACKAGE_RA where 1=1";
		String whereCause = generateWhereCause(queryTo);
		sql += whereCause;
		if (queryTo.getPkgCode() != null && !queryTo.getPkgCode().equals("")) {
			String materialNumQueryString = getSmartSearchQueryString(
					"PKG_CODE", queryTo.getPkgCode());
			if (materialNumQueryString != null) {
				sql += " and (" + materialNumQueryString + " )";
			}
		}

		if (queryTo.getProdName() != null && queryTo.getProdName().length() > 0) {
			if (prodNameList != null && prodNameList.size() > 0) {
				if (prodNameList.contains(queryTo.getProdName())) {
					sql += " and PROD_NAME = '" + queryTo.getProdName() + "'";
				}
			} else {
				sql += " and PROD_NAME = '" + queryTo.getProdName() + "'";
			}
		} else {
			if (prodNameList != null && prodNameList.size() > 0) {
				sql += " and (PROD_NAME = '" + prodNameList.get(0) + "'";
				for (int i = 1; i < prodNameList.size(); i++) {
					sql += " or PROD_NAME = '" + prodNameList.get(i) + "'";
				}
				sql += ")";
			}
		}

		// Add by 990044 2008/07/23

		// Add end

		if (queryTo.getPkgRaActualStartTime() != null
				&& !queryTo.getPkgRaActualStartTime().equals("")
				&& queryTo.getPkgRaActualEndTime() != null
				&& !queryTo.getPkgRaActualEndTime().equals("")) {

			String dateFrom = getSQLDateString(queryTo
					.getPkgRaActualStartTime()
					+ " 00:00:00", "yyyy/MM/dd HH:mm:ss");
			String dateTo = getSQLDateString(queryTo.getPkgRaActualEndTime()
					+ " 23:59:59", "yyyy/MM/dd HH:mm:ss");

			sql += "and PKG_RA_ACTUAL_FINISH_TIME between " + dateFrom
					+ " and " + dateTo + " ";

		}

		sql += " order by PROD_NAME";
		System.out.println("sql= " + sql);
		GenericRowMapper<PkgRaTo> rm = new GenericRowMapper<PkgRaTo>(
				PkgRaTo.class);
		List<PkgRaTo> result = null;
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

	public int countResult(final PkgRaQueryTo queryTo) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String[] prodCodes = null;
		if (queryTo.getProjName() != null && queryTo.getProjName().length() > 0) {
			ProjectDao projectDao = new ProjectDao();
			ProjectTo projectTo = projectDao.find(queryTo.getProjName());
			if (projectTo != null && projectTo.getProdCodeList() != null
					&& projectTo.getProdCodeList().length() > 0) {
				prodCodes = projectTo.getProdCodeList().split(",");

			}
		}
		List<String> prodNameList = new ArrayList<String>();
		if (prodCodes != null && prodCodes.length > 0) {
			ProductDao prodDao = new ProductDao();
			for (String prodCode : prodCodes) {
				ProductTo prodTo = prodDao.findByProdCode(prodCode);
				if (prodTo != null && prodTo.getProdName() != null
						&& prodTo.getProdName().length() > 0) {
					prodNameList.add(prodTo.getProdName());
				}
			}
		}
		String sql = "select count(*) from PIDB_PACKAGE_RA where 1=1";
		String whereCause = generateWhereCause(queryTo);
		sql += whereCause;
		if (queryTo.getPkgCode() != null && !queryTo.getPkgCode().equals("")) {
			String materialNumQueryString = getSmartSearchQueryString(
					"PKG_CODE", queryTo.getPkgCode());
			if (materialNumQueryString != null) {
				sql += " and (" + materialNumQueryString + " )";
			}
		}
		if (queryTo.getProdName() != null && queryTo.getProdName().length() > 0) {
			if (prodNameList != null && prodNameList.size() > 0) {
				if (prodNameList.contains(queryTo.getProdName())) {
					sql += " and PROD_NAME = '" + queryTo.getProdName() + "'";
				}
			} else {
				sql += " and PROD_NAME = '" + queryTo.getProdName() + "'";
			}
		} else {
			if (prodNameList != null && prodNameList.size() > 0) {
				sql += " and (PROD_NAME = '" + prodNameList.get(0) + "'";
				for (int i = 1; i < prodNameList.size(); i++) {
					sql += " or PROD_NAME = '" + prodNameList.get(i) + "'";
				}
				sql += ")";
			}
		}

		if (queryTo.getPkgRaActualStartTime() != null
				&& !queryTo.getPkgRaActualStartTime().equals("")
				&& queryTo.getPkgRaActualEndTime() != null
				&& !queryTo.getPkgRaActualEndTime().equals("")) {

			String dateFrom = getSQLDateString(queryTo
					.getPkgRaActualStartTime()
					+ " 00:00:00", "yyyy/MM/dd HH:mm:ss");
			String dateTo = getSQLDateString(queryTo.getPkgRaActualEndTime()
					+ " 23:59:59", "yyyy/MM/dd HH:mm:ss");

			sql += "and PKG_RA_ACTUAL_FINISH_TIME between " + dateFrom
					+ " and " + dateTo + " ";

		}

		return sjt.queryForInt(sql, new Object[] {});

	}

	public PkgRaTo findByPrimaryKey(final String prodName,
			final String pkgCode, final String pkgType,
			final String worksheetNumber) {
		SimpleJdbcTemplate stj = super.getSimpleJdbcTemplate();
		String sql = "select * from PIDB_PACKAGE_RA where PROD_NAME=? and PKG_CODE = ? And PKG_TYPE = ? and WORKSHEET_NUMBER = ? ";
		List<PkgRaTo> result = stj.query(sql, new GenericRowMapper<PkgRaTo>(
				PkgRaTo.class), new Object[] { prodName, pkgCode, pkgType,
				worksheetNumber });
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

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

	public ParameterTo findByFieldValue(String funName, String funFieldName,
			String fieldShowName) {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_FUN_PARAMETER_VALUE where FUN_NAME=?"
				+ " AND FUN_FIELD_NAME=? AND FIELD_Value=?";
		logger.debug(sql);
		List<ParameterTo> result = stj.query(sql,
				new GenericRowMapper<ParameterTo>(ParameterTo.class),
				new Object[] { funName, funFieldName, fieldShowName });

		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	private String generateWhereCause(final PkgRaQueryTo queryTo) {
		StringBuilder sb = new StringBuilder();

		if (queryTo.getWorksheetNumber() != null
				&& !queryTo.getWorksheetNumber().equals("")) {
			String worksheetQueryString = getSmartSearchQueryString(
					"WORKSHEET_NUMBER", queryTo.getWorksheetNumber());
			if (worksheetQueryString != null) {
				sb.append(" and (" + worksheetQueryString + " )");
			}
		}
		if (queryTo.getPartNum() != null && !queryTo.getPartNum().equals("")) {
			String partNumQueryString = getSmartSearchQueryString("PART_NUM",
					queryTo.getPartNum());
			if (partNumQueryString != null) {
				sb.append(" and (" + partNumQueryString + " )");
			}
		}

		if (queryTo.getTapeVendor() != null
				&& !queryTo.getTapeVendor().equals("")) {
			String field = queryTo.getTapeVendor();
			SapMasterVendorDao dao = new SapMasterVendorDao();
			String realVendor = "";
			if (field != null && !field.equals("")) {
				String[] fields = field.split(",");
				if (fields != null && fields.length > 0) {
					for (String s : fields) {
						SapMasterVendorTo to = dao.findByShortName(s);
						if (to != null) {
							realVendor += to.getVendorCode();
						}
					}
				}
			}
			String tapeVendor = getSmartSearchQueryString("TAPE_VENDOR",
					realVendor);
			if (tapeVendor != null) {
				sb.append(" and (" + tapeVendor + ")");
			}
		}

		if (queryTo.getOwner() != null && !queryTo.getOwner().equals("")) {
			String owner = getSmartSearchQueryLike("OWNER", queryTo.getOwner());
			if (owner != null) {
				sb.append(" and (" + owner + ")");
			}
		}

		if (queryTo.getAssySite() != null && !queryTo.getAssySite().equals("")) {
			String AssySite = getSmartSearchQueryAllLike("ASSY_SITE", queryTo
					.getAssySite());
			if (AssySite != null) {
				sb.append(" and (" + AssySite + ")");
			}

		}
		return sb.toString();

	}
}
