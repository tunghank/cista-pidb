package com.cista.pidb.md.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.cista.pidb.core.GenericRowMapper;
import com.cista.pidb.core.PIDBDaoSupport;
import com.cista.pidb.md.to.ProjectCodeTo;
import com.cista.pidb.md.to.ProjectQueryTo;
import com.cista.pidb.md.to.ProjectTo;

public class ProjectDao extends PIDBDaoSupport {
	public void insert(final ProjectTo newInstance) {
		if (newInstance == null) {
			logger.error("Error the parameter is null.");
		}
		TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(
					final TransactionStatus status) {
				ProjectCodeDao pcDao = new ProjectCodeDao();
				List<ProjectCodeTo> pctList = pcDao
						.findByProjectName(newInstance.getProjName());
				List<String> prodCodeList = new ArrayList<String>();
				for (ProjectCodeTo pct : pctList) {
					String pc = pct.getProdCode();
					if (pc != null && pc.length() > 0) {
						String[] pcaList = pc.split(",");
						for (String pca : pcaList) {
							if (pca != null && pca.length() > 0
									&& !prodCodeList.contains(pca.trim())) {
								prodCodeList.add(pca.trim());
							}
						}
					}
				}

				if (newInstance.getProdCodeList() != null
						&& newInstance.getProdCodeList().length() > 0) {
					String[] pcaList = newInstance.getProdCodeList().split(",");
					for (String pca : pcaList) {
						if (pca != null && pca.length() > 0
								&& !prodCodeList.contains(pca.trim())) {
							prodCodeList.add(pca.trim());
						}
					}
				}

				String prodCodeListStr = "";
				if (prodCodeList.size() > 0) {
					for (String pc : prodCodeList) {
						prodCodeListStr += "," + pc;
					}
				}

				if (prodCodeListStr.length() > 0) {
					prodCodeListStr = prodCodeListStr.substring(1);
				} else {
					prodCodeListStr = null;
				}

				SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
				// Sql for insert PIDB_PROJECT
				String sql = "insert into PIDB_PROJECT"
						+ "(PROJ_NAME,"
						+ "FAB,"
						+ "PITCH,"
						+ "PAD_TYPE,"
						+ "FS_DATE,"
						+ "PROC_TECH,"
						+ "POLY_METAL_LAYERS,"
						+ "VOLTAGE,"
						+ "MASK_LAYERS_NO,"
						+ "PROC_LAYER_NO,"
						+ "WAFER_TYPE,"
						+ "WAFER_INCH,"
						+ "X_SIZE,"
						+ "Y_SIZE,"
						+ "GROSS_DIE,"
						+ "FCST_CP_YIELD,"
						+ "TO_INCLUDE_SEALRING,"
						+ "SEALRING,"
						+ "SCRIBE_LINE,"
						+ "TEST_LINE,"
						+ "WAFER_THICKNESS,"
						+ "PROC_NAME,"
						+ "ANY_IP_USAGE,"
						+ "EMBEDDED_OTP,"
						+ "OTP_SIZE,"
						+ "PROJ_LEADER,"
						+ "DESIGN_ENGR,"
						+ "PROD_ENGR,"
						+ "ESD_ENGR,"
						+ "APR_ENGR,"
						+ "LAYOUT_ENGR,"
						+ "TEST_ENGR,"
						+ "ASSY_ENGR,"
						+ "APP_ENGR,"
						+ "PM,"
						+ "QA_ENGR,"
						+ "SALES_ENGR,"
						+ "ASSIGN_TO,"
						+ "ASSIGN_EMAIL,"
						+ "STATUS,"
						+ "PROD_FAMILY,"
						+ "PROD_LINE,"
						+ "PANEL_TYPE,"
						+ "IC_TYPE,"
						+ "PROD_CODE_LIST,"
						+ "CREATED_BY,"
						+ "MODIFIED_BY,"
						+ "ESTIMATED,"
						+ "NICK_NAME,"
						+ "SECOND_FOUNDRY_PROJECT, "
						+ "SUB_FAB "
						+ ")"
						+ " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
								"  ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
								"  ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
								"  ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
								"  ?, ?, ?, ?, ?, ?, ?)";
				logger.debug(sql);
				sjt.update(sql, newInstance.getProjName(),
						newInstance.getFab(), newInstance.getPitch(),
						newInstance.getPadType(), newInstance.getFsDate(),
						newInstance.getProcTech(), newInstance
								.getPolyMetalLayers(),
						newInstance.getVoltage(),
						newInstance.getMaskLayersNo(), newInstance
								.getProcLayerNo(), newInstance.getWaferType(),
						newInstance.getWaferInch(), newInstance.getXSize(),
						newInstance.getYSize(), newInstance.getGrossDie(),
						newInstance.getFcstCpYield(), newInstance
								.getToIncludeSealring(), newInstance
								.getSealring(), newInstance.getScribeLine(),
						newInstance.getTestLine(), newInstance
								.getWaferThickness(),
						newInstance.getProcName(), newInstance.getAnyIpUsage(),
						newInstance.getEmbeddedOtp(), newInstance.getOtpSize(),
						newInstance.getProjLeader(), newInstance
								.getDesignEngr(), newInstance.getProdEngr(),
						newInstance.getEsdEngr(), newInstance.getAprEngr(),
						newInstance.getLayoutEngr(), newInstance.getTestEngr(),
						newInstance.getAssyEngr(), newInstance.getAppEngr(),
						newInstance.getPm(), newInstance.getQaEngr(),
						newInstance.getSalesEngr(), newInstance.getAssignTo(),
						newInstance.getAssignEmail(), newInstance.getStatus(),
						newInstance.getProdFamily(), newInstance.getProdLine(),
						newInstance.getPanelType(), newInstance.getIcType(),
						prodCodeListStr, newInstance.getCreatedBy(),
						newInstance.getModifiedBy(),
						newInstance.getEstimated(), newInstance.getNickName(),
						newInstance.getSecondFoundryProject(),
						newInstance.getSubFab());

				// Sql for insert PIDB_PROJECT_CODE
				sql = "insert into PIDB_PROJECT_CODE" + "(PROJ_CODE,"
						+ "PROJ_NAME," + "PROJ_OPTION," + "FUNC_REMARK,"
						+ "CUST," + "KICK_OFF_DATE," + "PROD_CODE," + "REMARK,"
						+ "RELEASE_TO, " + " PROJECT_TYPE " + ")"
						+ " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				logger.debug(sql);
				sjt.update(sql, newInstance.getProjCode(), newInstance
						.getProjName(), newInstance.getProjOption(),
						newInstance.getFuncRemark(), newInstance.getCust(),
						newInstance.getKickOffDate(), newInstance
								.getProdCodeList(), newInstance.getRemark(),
						newInstance.getReleaseTo(), newInstance
								.getProjectType());
			}
		};
		doInTransaction(callback);
	}

	public void update(final ProjectTo newInstance) {
		if (newInstance == null) {
			logger.error("Error the parameter is null.");
		}

		TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(
					final TransactionStatus status) {
				ProjectCodeDao pcDao = new ProjectCodeDao();
				List<ProjectCodeTo> pctList = pcDao
						.findByProjectName(newInstance.getProjName());
				List<String> prodCodeList = new ArrayList<String>();
				for (ProjectCodeTo pct : pctList) {
					if (pct.getProjCode().equals(newInstance.getProjCode())) {
						continue;
					}
					String pc = pct.getProdCode();
					if (pc != null && pc.length() > 0) {
						String[] pcaList = pc.split(",");
						for (String pca : pcaList) {
							if (pca != null && pca.length() > 0
									&& !prodCodeList.contains(pca.trim())) {
								prodCodeList.add(pca.trim());
							}
						}
					}
				}

				if (newInstance.getProdCodeList() != null
						&& newInstance.getProdCodeList().length() > 0) {
					String[] pcaList = newInstance.getProdCodeList().split(",");
					for (String pca : pcaList) {
						if (pca != null && pca.length() > 0
								&& !prodCodeList.contains(pca.trim())) {
							prodCodeList.add(pca.trim());
						}
					}
				}

				String prodCodeListStr = "";
				if (prodCodeList.size() > 0) {
					for (String pc : prodCodeList) {
						prodCodeListStr += "," + pc;
					}
				}

				if (prodCodeListStr.length() > 0) {
					prodCodeListStr = prodCodeListStr.substring(1);
				} else {
					prodCodeListStr = null;
				}

				SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
				// Sql for insert PIDB_PROJECT
				String sql = "update PIDB_PROJECT set "
						+ "FAB = ?,"
						+ "PITCH = ?,"
						+ "PAD_TYPE = ?,"
						+ "FS_DATE = ?,"
						+ "PROC_TECH = ?,"
						+ "POLY_METAL_LAYERS = ?,"
						+ "VOLTAGE = ?,"
						+ "MASK_LAYERS_NO = ?,"
						+ "PROC_LAYER_NO = ?,"
						+ "WAFER_TYPE = ?,"
						+ "WAFER_INCH = ?,"
						+ "X_SIZE = ?,"
						+ "Y_SIZE = ?,"
						+ "GROSS_DIE = ?,"
						+ "FCST_CP_YIELD = ?,"
						+ "TO_INCLUDE_SEALRING = ?,"
						+ "SEALRING = ?,"
						+ "SCRIBE_LINE = ?,"
						+ "TEST_LINE = ?,"
						+ "WAFER_THICKNESS = ?,"
						+ "PROC_NAME = ?,"
						+ "ANY_IP_USAGE = ?,"
						+ "EMBEDDED_OTP = ?,"
						+ "OTP_SIZE = ?,"
						+ "PROJ_LEADER = ?,"
						+ "DESIGN_ENGR = ?,"
						+ "PROD_ENGR = ?,"
						+ "ESD_ENGR = ?,"
						+ "APR_ENGR = ?,"
						+ "LAYOUT_ENGR = ?,"
						+ "TEST_ENGR = ?,"
						+ "ASSY_ENGR = ?,"
						+ "APP_ENGR = ?,"
						+ "PM = ?,"
						+ "QA_ENGR = ?,"
						+ "SALES_ENGR = ?,"
						+ "ASSIGN_TO = ?,"
						+ "ASSIGN_EMAIL = ?,"
						+ "STATUS = ?,"
						+ "PROD_FAMILY = ?,"
						+ "PROD_LINE = ?,"
						+ "PANEL_TYPE = ?,"
						+ "IC_TYPE = ?,"
						+ "PROD_CODE_LIST = ?,"
						+ "MODIFIED_BY = ?,"
						+ "ESTIMATED = ?, " 
						+ "NICK_NAME = ?, " 
						+ "SECOND_FOUNDRY_PROJECT=?, "
						+ "SUB_FAB=?, "
						+ "UPDATE_DATE=TO_CHAR(SYSDATE, 'RRRRMMDDHH24MISS') "
						+ " where PROJ_NAME = ?";

				logger.debug(sql);
				sjt.update(sql, newInstance.getFab(), newInstance.getPitch(),
						newInstance.getPadType(), newInstance.getFsDate(),
						newInstance.getProcTech(), newInstance
								.getPolyMetalLayers(),
						newInstance.getVoltage(),
						newInstance.getMaskLayersNo(), newInstance
								.getProcLayerNo(), newInstance.getWaferType(),
						newInstance.getWaferInch(), newInstance.getXSize(),
						newInstance.getYSize(), newInstance.getGrossDie(),
						newInstance.getFcstCpYield(), newInstance
								.getToIncludeSealring(), newInstance
								.getSealring(), newInstance.getScribeLine(),
						newInstance.getTestLine(), newInstance
								.getWaferThickness(),
						newInstance.getProcName(), newInstance.getAnyIpUsage(),
						newInstance.getEmbeddedOtp(), newInstance.getOtpSize(),
						newInstance.getProjLeader(), newInstance
								.getDesignEngr(), newInstance.getProdEngr(),
						newInstance.getEsdEngr(), newInstance.getAprEngr(),
						newInstance.getLayoutEngr(), newInstance.getTestEngr(),
						newInstance.getAssyEngr(), newInstance.getAppEngr(),
						newInstance.getPm(), newInstance.getQaEngr(),
						newInstance.getSalesEngr(), newInstance.getAssignTo(),
						newInstance.getAssignEmail(), newInstance.getStatus(),
						newInstance.getProdFamily(), newInstance.getProdLine(),
						newInstance.getPanelType(), newInstance.getIcType(),
						prodCodeListStr, newInstance.getModifiedBy(),
						newInstance.getEstimated(), newInstance.getNickName(),
						newInstance.getSecondFoundryProject(), 
						newInstance.getSubFab(), 
						newInstance.getProjName());

				// Sql for insert PIDB_PROJECT_CODE
				sql = "update PIDB_PROJECT_CODE set " + "PROJ_NAME = ?,"
						+ "PROJ_OPTION = ?," + "FUNC_REMARK = ?," + "CUST = ?,"
						+ "KICK_OFF_DATE = ?," + "PROD_CODE = ?,"
						+ "REMARK = ?,RELEASE_TO=? ,PROJECT_TYPE=? "
						+ " where PROJ_CODE = ?";
				logger.debug(sql);
				sjt.update(sql, newInstance.getProjName(), newInstance
						.getProjOption(), newInstance.getFuncRemark(),
						newInstance.getCust(), newInstance.getKickOffDate(),
						newInstance.getProdCodeList(), newInstance.getRemark(),
						newInstance.getReleaseTo(), newInstance
								.getProjectType(), newInstance.getProjCode());
			}
		};
		doInTransaction(callback);
	}

	public void update(final ProjectTo newInstance, final String projCodeAct) {
		if (newInstance == null) {
			logger.error("Error the parameter is null.");
		}
		TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(
					final TransactionStatus status) {
				ProjectCodeDao pcDao = new ProjectCodeDao();
				List<ProjectCodeTo> pctList = pcDao
						.findByProjectName(newInstance.getProjName());
				List<String> prodCodeList = new ArrayList<String>();
				for (ProjectCodeTo pct : pctList) {
					if (pct.getProjCode().equals(newInstance.getProjCode())) {
						continue;
					}
					String pc = pct.getProdCode();
					if (pc != null && pc.length() > 0) {
						String[] pcaList = pc.split(",");
						for (String pca : pcaList) {
							if (pca != null && pca.length() > 0
									&& !prodCodeList.contains(pca.trim())) {
								prodCodeList.add(pca.trim());
							}
						}
					}
				}

				if (newInstance.getProdCodeList() != null
						&& newInstance.getProdCodeList().length() > 0) {
					String[] pcaList = newInstance.getProdCodeList().split(",");
					for (String pca : pcaList) {
						if (pca != null && pca.length() > 0
								&& !prodCodeList.contains(pca.trim())) {
							prodCodeList.add(pca.trim());
						}
					}
				}

				String prodCodeListStr = "";
				if (prodCodeList.size() > 0) {
					for (String pc : prodCodeList) {
						prodCodeListStr += "," + pc;
					}
				}

				if (prodCodeListStr.length() > 0) {
					prodCodeListStr = prodCodeListStr.substring(1);
				} else {
					prodCodeListStr = null;
				}

				SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
				// Sql for insert PIDB_PROJECT
				String sql = "update PIDB_PROJECT set "
						+ "FAB = ?,"
						+ "PITCH = ?,"
						+ "PAD_TYPE = ?,"
						+ "FS_DATE = ?,"
						+ "PROC_TECH = ?,"
						+ "POLY_METAL_LAYERS = ?,"
						+ "VOLTAGE = ?,"
						+ "MASK_LAYERS_NO = ?,"
						+ "PROC_LAYER_NO = ?,"
						+ "WAFER_TYPE = ?,"
						+ "WAFER_INCH = ?,"
						+ "X_SIZE = ?,"
						+ "Y_SIZE = ?,"
						+ "GROSS_DIE = ?,"
						+ "FCST_CP_YIELD = ?,"
						+ "TO_INCLUDE_SEALRING = ?,"
						+ "SEALRING = ?,"
						+ "SCRIBE_LINE = ?,"
						+ "TEST_LINE = ?,"
						+ "WAFER_THICKNESS = ?,"
						+ "PROC_NAME = ?,"
						+ "ANY_IP_USAGE = ?,"
						+ "EMBEDDED_OTP = ?,"
						+ "OTP_SIZE = ?,"
						+ "PROJ_LEADER = ?,"
						+ "DESIGN_ENGR = ?,"
						+ "PROD_ENGR = ?,"
						+ "ESD_ENGR = ?,"
						+ "APR_ENGR = ?,"
						+ "LAYOUT_ENGR = ?,"
						+ "TEST_ENGR = ?,"
						+ "ASSY_ENGR = ?,"
						+ "APP_ENGR = ?,"
						+ "PM = ?,"
						+ "QA_ENGR = ?,"
						+ "SALES_ENGR = ?,"
						+ "ASSIGN_TO = ?,"
						+ "ASSIGN_EMAIL = ?,"
						+ "STATUS = ?,"
						+ "PROD_FAMILY = ?,"
						+ "PROD_LINE = ?,"
						+ "PANEL_TYPE = ?,"
						+ "IC_TYPE = ?,"
						+ "PROD_CODE_LIST = ?,"
						+ "MODIFIED_BY = ?,"
						+ "ESTIMATED = ?,"
						+ "NICK_NAME = ?,SECOND_FOUNDRY_PROJECT=? "
						+ " where PROJ_NAME = ?";

				logger.debug(sql);
				sjt.update(sql, newInstance.getFab(), newInstance.getPitch(),
						newInstance.getPadType(), newInstance.getFsDate(),
						newInstance.getProcTech(), newInstance
								.getPolyMetalLayers(),
						newInstance.getVoltage(),
						newInstance.getMaskLayersNo(), newInstance
								.getProcLayerNo(), newInstance.getWaferType(),
						newInstance.getWaferInch(), newInstance.getXSize(),
						newInstance.getYSize(), newInstance.getGrossDie(),
						newInstance.getFcstCpYield(), newInstance
								.getToIncludeSealring(), newInstance
								.getSealring(), newInstance.getScribeLine(),
						newInstance.getTestLine(), newInstance
								.getWaferThickness(),
						newInstance.getProcName(), newInstance.getAnyIpUsage(),
						newInstance.getEmbeddedOtp(), newInstance.getOtpSize(),
						newInstance.getProjLeader(), newInstance
								.getDesignEngr(), newInstance.getProdEngr(),
						newInstance.getEsdEngr(), newInstance.getAprEngr(),
						newInstance.getLayoutEngr(), newInstance.getTestEngr(),
						newInstance.getAssyEngr(), newInstance.getAppEngr(),
						newInstance.getPm(), newInstance.getQaEngr(),
						newInstance.getSalesEngr(), newInstance.getAssignTo(),
						newInstance.getAssignEmail(), newInstance.getStatus(),
						newInstance.getProdFamily(), newInstance.getProdLine(),
						newInstance.getPanelType(), newInstance.getIcType(),
						prodCodeListStr, newInstance.getModifiedBy(),
						newInstance.getEstimated(), newInstance.getNickName(),
						newInstance.getSecondFoundryProject(),  newInstance.getProjName());
				if ("insert".equals(projCodeAct)) {
					sql = "insert into PIDB_PROJECT_CODE" + "(PROJ_CODE,"
							+ "PROJ_NAME," + "PROJ_OPTION," + "FUNC_REMARK,"
							+ "CUST," + "KICK_OFF_DATE," + "PROD_CODE,"
							+ "REMARK," + "RELEASE_TO, PROJECT_TYPE)"
							+ " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
					logger.debug(sql);
					sjt
							.update(sql, newInstance.getProjCode(), newInstance
									.getProjName(),
									newInstance.getProjOption(), newInstance
											.getFuncRemark(), newInstance
											.getCust(), newInstance
											.getKickOffDate(), newInstance
											.getProdCodeList(), newInstance
											.getRemark(), newInstance
											.getReleaseTo(),newInstance.getProjectType());
				}
			}
		};
		doInTransaction(callback);
	}

	public List<ProjectTo> findAllByProjCode(String projCode) {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select p.PROJ_NAME,pc.PROD_CODE PROD_CODE_LIST ,pc.proj_code from PIDB_PROJECT p, PIDB_PROJECT_CODE pc where p.PROJ_NAME=pc.PROJ_NAME ";
		if (projCode != null && projCode.length() > 0) {
			sql += " and "
					+ super.getSmartSearchQueryString("pc.PROJ_CODE", projCode);
		}
		logger.debug(sql);
		return stj.query(sql, new GenericRowMapper<ProjectTo>(ProjectTo.class),
				new Object[] {});
	}

	public ProjectTo find(String projName) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select "
				+ "p.PROJ_NAME,"
				+ "p.FAB,"
				+ "p.PITCH,"
				+ "p.PAD_TYPE,"
				+ "p.FS_DATE,"
				+ "p.PROC_TECH,"
				+ "p.POLY_METAL_LAYERS,"
				+ "p.VOLTAGE,"
				+ "p.MASK_LAYERS_NO,"
				+ "p.PROC_LAYER_NO,"
				+ "p.WAFER_TYPE,"
				+ "p.WAFER_INCH,"
				+ "p.X_SIZE,"
				+ "p.Y_SIZE,"
				+ "p.GROSS_DIE,"
				+ "p.FCST_CP_YIELD,"
				+ "p.TO_INCLUDE_SEALRING,"
				+ "p.SEALRING,"
				+ "p.SCRIBE_LINE,"
				+ "p.TEST_LINE,"
				+ "p.WAFER_THICKNESS,"
				+ "p.PROC_NAME,"
				+ "p.ANY_IP_USAGE,"
				+ "p.EMBEDDED_OTP,"
				+ "p.OTP_SIZE,"
				+ "p.PROJ_LEADER,"
				+ "p.DESIGN_ENGR,"
				+ "p.PROD_ENGR,"
				+ "p.ESD_ENGR,"
				+ "p.APR_ENGR,"
				+ "p.LAYOUT_ENGR,"
				+ "p.TEST_ENGR,"
				+ "p.ASSY_ENGR,"
				+ "p.APP_ENGR,"
				+ "p.PM,"
				+ "p.QA_ENGR,"
				+ "p.SALES_ENGR,"
				+ "p.ASSIGN_TO,"
				+ "p.ASSIGN_EMAIL,"
				+ "p.STATUS,"
				+ "p.PROD_FAMILY,"
				+ "p.PROD_LINE,"
				+ "p.PANEL_TYPE,"
				+ "p.IC_TYPE,"
				+ "pc.PROD_CODE PROD_CODE_LIST,"
				+ "p.CREATED_BY,"
				+ "p.MODIFIED_BY,"
				+ "p.NICK_NAME,"
				+ "p.SECOND_FOUNDRY_PROJECT,"
				+ "p.SUB_FAB,"
				+ "pc.PROJECT_TYPE,"
				+ "pc.PROJ_CODE,"
				+ "pc.PROJ_OPTION,"
				+ "pc.FUNC_REMARK,"
				+ "pc.CUST,"
				+ "pc.REMARK,"
				+ "pc.KICK_OFF_DATE,"
				+ "pc.RELEASE_TO"
				+ " from pidb_project p left join pidb_project_code pc on p.proj_name=pc.proj_name where P.PROJ_NAME = ?";

		logger.debug(sql);
		
		GenericRowMapper<ProjectTo> rm = new GenericRowMapper<ProjectTo>(
				ProjectTo.class);
		List<ProjectTo> result = sjt.query(sql, rm, new Object[] { projName });
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	public int countResult(ProjectQueryTo queryTo) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select count(*) from pidb_project p left join pidb_project_code pc on p.proj_name=pc.proj_name where 1 = 1 ";
		if (queryTo.getReleaseTo() != null
				&& !queryTo.getReleaseTo().equals("")) {
			if (queryTo.getReleaseTo().equals("HX")
					|| queryTo.getReleaseTo().equals("WP")) {
				sql += " AND (pc.RELEASE_TO ='" + queryTo.getReleaseTo() + "'"
						+ " or pc.RELEASE_TO ='ALL')";
			}
		}
		String whereCause = generateWhereCause(queryTo);
		sql += whereCause;
		return sjt.queryForInt(sql, new Object[] {});
	}

	public List<ProjectTo> query(ProjectQueryTo queryTo) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select "
				+ "p.PROJ_NAME,"
				+ "p.FAB,"
				+ "p.PITCH,"
				+ "p.PAD_TYPE,"
				+ "p.FS_DATE,"
				+ "p.PROC_TECH,"
				+ "p.POLY_METAL_LAYERS,"
				+ "p.VOLTAGE,"
				+ "p.MASK_LAYERS_NO,"
				+ "p.PROC_LAYER_NO,"
				+ "p.WAFER_TYPE,"
				+ "p.WAFER_INCH,"
				+ "p.X_SIZE,"
				+ "p.Y_SIZE,"
				+ "p.GROSS_DIE,"
				+ "p.FCST_CP_YIELD,"
				+ "p.TO_INCLUDE_SEALRING,"
				+ "p.SEALRING,"
				+ "p.SCRIBE_LINE,"
				+ "p.TEST_LINE,"
				+ "p.WAFER_THICKNESS,"
				+ "p.PROC_NAME,"
				+ "p.ANY_IP_USAGE,"
				+ "p.EMBEDDED_OTP,"
				+ "p.OTP_SIZE,"
				+ "p.PROJ_LEADER,"
				+ "p.DESIGN_ENGR,"
				+ "p.PROD_ENGR,"
				+ "p.ESD_ENGR,"
				+ "p.APR_ENGR,"
				+ "p.LAYOUT_ENGR,"
				+ "p.TEST_ENGR,"
				+ "p.ASSY_ENGR,"
				+ "p.APP_ENGR,"
				+ "p.PM,"
				+ "p.QA_ENGR,"
				+ "p.SALES_ENGR,"
				+ "p.ASSIGN_TO,"
				+ "p.ASSIGN_EMAIL,"
				+ "p.STATUS,"
				+ "p.PROD_FAMILY,"
				+ "p.PROD_LINE,"
				+ "p.PANEL_TYPE,"
				+ "p.IC_TYPE,"
				+ "pc.PROD_CODE PROD_CODE_LIST,"
				+ "p.CREATED_BY,"
				+ "p.MODIFIED_BY,"
				+ "p.ESTIMATED,"
				+ "p.NICK_NAME,"
				+ "p.SECOND_FOUNDRY_PROJECT,"
				+ "pc.PROJECT_TYPE,"
				+ "pc.PROJ_CODE,"
				+ "pc.PROJ_OPTION,"
				+ "pc.FUNC_REMARK,"
				+ "pc.CUST,"
				+ "pc.REMARK,"
				+ "pc.KICK_OFF_DATE,"
				+ "pc.RELEASE_TO"
				+ " from pidb_project p left join pidb_project_code pc on p.proj_name=pc.proj_name where 1 = 1 ";
		if (queryTo.getReleaseTo() != null
				&& !queryTo.getReleaseTo().equals("")) {
			if (queryTo.getReleaseTo().equals("HX")
					|| queryTo.getReleaseTo().equals("WP")) {
				sql += " AND (pc.RELEASE_TO ='" + queryTo.getReleaseTo() + "'"
						+ " or pc.RELEASE_TO ='ALL')";
			}
		}
		String whereCause = generateWhereCause(queryTo);
		sql += whereCause;
		sql += " order by pc.PROJ_CODE";

		logger.debug(sql);

		GenericRowMapper<ProjectTo> rm = new GenericRowMapper<ProjectTo>(
				ProjectTo.class);
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

	public List<ProjectTo> queryForDomain(ProjectQueryTo queryTo) {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select "
				+ "p.PROJ_NAME,"
				+ "(select FAB_DESCR from T_FAB_CODE tfc where tfc.FAB=p.FAB) FAB,"
				+ "p.PITCH,"
				+ "p.PAD_TYPE,"
				+ "to_date(substr(to_char(p.FS_DATE,'yyyy/mm/dd'),1,10),'yyyy/mm/dd') AS FS_DATE,"
				+ "p.PROC_TECH,"
				+ "p.POLY_METAL_LAYERS,"
				+ "p.VOLTAGE,"
				+ "p.MASK_LAYERS_NO,"
				+ "p.PROC_LAYER_NO,"
				+ "p.WAFER_TYPE,"
				+ "p.WAFER_INCH,"
				+ "p.X_SIZE,"
				+ "p.Y_SIZE,"
				+ "p.GROSS_DIE,"
				+ "p.FCST_CP_YIELD,"
				+ "p.TO_INCLUDE_SEALRING,"
				+ "p.SEALRING,"
				+ "p.SCRIBE_LINE,"
				+ "p.TEST_LINE,"
				+ "p.WAFER_THICKNESS,"
				+ "p.PROC_NAME,"
				+ "p.ANY_IP_USAGE,"
				+ "p.EMBEDDED_OTP,"
				+ "p.OTP_SIZE,"
				+ "p.PROJ_LEADER,"
				+ "p.DESIGN_ENGR,"
				+ "p.PROD_ENGR,"
				+ "p.ESD_ENGR,"
				+ "p.APR_ENGR,"
				+ "p.LAYOUT_ENGR,"
				+ "p.TEST_ENGR,"
				+ "p.ASSY_ENGR,"
				+ "p.APP_ENGR,"
				+ "p.PM,"
				+ "p.QA_ENGR,"
				+ "p.SALES_ENGR,"
				+ "p.ASSIGN_TO,"
				+ "p.ASSIGN_EMAIL,"
				+ "p.STATUS,"
				+ "(select DESCRIPTION from WM_SAP_MASTER_PRODUCT_FAMILY wsmpf where wsmpf.PRODUCT_FAMILY=p.PROD_FAMILY) PROD_FAMILY,"
				+ "p.PROD_LINE,"
				+ "p.PANEL_TYPE,"
				+ "(select DESCRIPTION from WM_SAP_MASTER_IC_TYPE wsmit where wsmit.IC_TYPE=p.IC_TYPE) IC_TYPE,"
				+ "pc.PROD_CODE PROD_CODE_LIST,"
				+ "p.CREATED_BY,"
				+ "p.MODIFIED_BY,"
				+ "p.ESTIMATED,"
				+ "p.NICK_NAME,"
				+ "p.SECOND_FOUNDRY_PROJECT,"
				+ "p.SUB_FAB,"
				+ "pc.PROJECT_TYPE,"
				+ "pc.PROJ_CODE,"
				+ "pc.PROJ_OPTION,"
				+ "pc.FUNC_REMARK,"
				+ "pc.CUST,"
				+ "pc.REMARK,"
				+ "pc.RELEASE_TO,"
				+ "to_date(substr(to_char(pc.KICK_OFF_DATE,'yyyy/mm/dd'),1,10),'yyyy/mm/dd') AS KICK_OFF_DATE"
				+ " from pidb_project p left join pidb_project_code pc on p.proj_name=pc.proj_name where 1 = 1 ";
		if (queryTo.getReleaseTo() != null
				&& !queryTo.getReleaseTo().equals("")) {
			if (queryTo.getReleaseTo().equals("HX")
					|| queryTo.getReleaseTo().equals("WP")) {
				sql += " AND (pc.RELEASE_TO ='" + queryTo.getReleaseTo() + "'"
						+ " or pc.RELEASE_TO ='ALL')";
			}
		}
		String whereCause = generateWhereCause(queryTo);
		sql += whereCause;
		sql += " order by pc.PROJ_CODE";
		GenericRowMapper<ProjectTo> rm = new GenericRowMapper<ProjectTo>(
				ProjectTo.class);
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

	private String generateWhereCause(final ProjectQueryTo queryTo) {
		StringBuilder sb = new StringBuilder();
		if (queryTo.getProjCode() != null && !queryTo.getProjCode().equals("")) {
			String projCodeQueryString = getSmartSearchQueryAllLike(
					"pc.PROJ_CODE", queryTo.getProjCode());
			if (projCodeQueryString != null) {
				sb.append(" and (" + projCodeQueryString + " )");
			}
		}

		if (queryTo.getProjName() != null && !queryTo.getProjName().equals("")) {
			String projNameQueryString = getSmartSearchQueryAllLike(
					"p.PROJ_NAME", queryTo.getProjName());
			if (projNameQueryString != null) {
				sb.append(" and (" + projNameQueryString + " )");
			}
		}

		/*
		 * if (queryTo.getReleaseTo() != null &&
		 * !queryTo.getReleaseTo().equals("")) {
		 * 
		 * String releaseTo = getSmartSearchQueryString("pc.RELEASE_TO",
		 * queryTo.getReleaseTo()); if (releaseTo != null) { sb.append(" and (" +
		 * releaseTo + ")"); } }
		 */

		if (queryTo.getProdCode() != null && !queryTo.getProdCode().equals("")) {
			String prodCode = getSmartSearchQueryAllLike("pc.PROD_CODE",
					queryTo.getProdCode());
			if (prodCode != null) {
				sb.append(" and (" + prodCode + " )");
			}
		}

		if (queryTo.getFab() != null && !queryTo.getFab().equals("")) {
			sb.append(" and p.FAB = " + getSQLString(queryTo.getFab()) + " ");
		}

		if (queryTo.getProjOption() != null
				&& !queryTo.getProjOption().equals("")) {
			sb.append(" and pc.PROJ_OPTION = "
					+ getSQLString(queryTo.getProjOption()) + " ");
		}

		if (queryTo.getPanelType() != null
				&& !queryTo.getPanelType().equals("")) {
			sb.append(" and p.PANEL_TYPE = "
					+ getSQLString(queryTo.getPanelType()) + " ");
		}

		if (queryTo.getProdFamily() != null
				&& !queryTo.getProdFamily().equals("")) {
			sb.append(" and p.PROD_FAMILY = "
					+ getSQLString(queryTo.getProdFamily()) + " ");
		}

		if (queryTo.getProdLine() != null && !queryTo.getProdLine().equals("")) {
			sb.append(" and p.PROD_LINE = "
					+ getSQLString(queryTo.getProdLine()) + " ");
		}

		if (queryTo.getProcTech() != null && !queryTo.getProcTech().equals("")) {
			sb.append(" and p.PROC_TECH = "
					+ getSQLString(queryTo.getProcTech()) + " ");
		}

		if (queryTo.getCust() != null && !queryTo.getCust().equals("")) {
			String custQueryString = getSmartSearchQueryString("pc.CUST",
					queryTo.getCust());
			if (custQueryString != null) {
				sb.append(" and (" + custQueryString + " )");
			}
		}

		if (queryTo.getTeamMember() != null
				&& !queryTo.getTeamMember().equals("")) {
			// String teamMember =
			// super.getLikeSQLString(queryTo.getTeamMember());
			String teamMember = queryTo.getTeamMember();
			if (teamMember.startsWith("*")) {
				teamMember = "%" + teamMember.substring(1);
			}

			if (teamMember.endsWith("*")) {
				teamMember = teamMember.substring(0, teamMember.length() - 1)
						+ "%";
			}
			teamMember = getSQLString(teamMember);

			sb.append(" and (");
			sb.append(" p.PROJ_LEADER like " + teamMember + " ");
			sb.append(" or p.DESIGN_ENGR like " + teamMember + " ");
			sb.append(" or p.PROD_ENGR like " + teamMember + " ");
			sb.append(" or p.ESD_ENGR like " + teamMember + " ");
			sb.append(" or p.APR_ENGR like " + teamMember + " ");
			sb.append(" or p.LAYOUT_ENGR like " + teamMember + " ");
			sb.append(" or p.TEST_ENGR like " + teamMember + " ");
			sb.append(" or p.ASSY_ENGR like " + teamMember + " ");
			sb.append(" or p.APP_ENGR like " + teamMember + " ");
			sb.append(" or p.PM like " + teamMember + " ");
			sb.append(" or p.QA_ENGR like " + teamMember + " ");
			sb.append(" or p.SALES_ENGR like " + teamMember + " ");
			sb.append(")");
		}

		if (queryTo.getKickOffDateFrom() != null
				&& !queryTo.getKickOffDateFrom().equals("")
				&& queryTo.getKickOffDateTo() != null
				&& !queryTo.getKickOffDateTo().equals("")) {
			// SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			String dateFrom = getSQLDateString(queryTo.getKickOffDateFrom()
					+ " 00:00:00", "yyyy/MM/dd HH:mm:ss");
			String dateTo = getSQLDateString(queryTo.getKickOffDateTo()
					+ " 23:59:59", "yyyy/MM/dd HH:mm:ss");
			sb.append(" and pc.KICK_OFF_DATE between " + dateFrom + " and "
					+ dateTo + " ");
		}

		if (queryTo.getStatus() != null && !queryTo.getStatus().equals("")) {
			sb.append(" and p.STATUS = " + getSQLString(queryTo.getStatus())
					+ " ");
		}

		if (queryTo.getEstimated() != null
				&& !queryTo.getEstimated().equals("")) {
			sb.append(" and p.ESTIMATED = "
					+ getSQLString(queryTo.getEstimated()) + " ");
		}

		if (queryTo.getNickName() != null && !queryTo.getNickName().equals("")) {
			String nickName = queryTo.getNickName();
			if (nickName.startsWith("*")) {
				nickName = "%" + nickName.substring(1);
			}

			if (nickName.endsWith("*")) {
				nickName = nickName.substring(0, nickName.length() - 1) + "%";
			}
			nickName = getSQLString(nickName);

			sb.append(" and p.NICK_NAME like " + nickName + " ");
		}
		return sb.toString();
	}

	public List<String> findProjCode() {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select distinct PROJ_CODE from PIDB_PROJECT_CODE order by PROJ_CODE";
		logger.debug(sql);
		List<Map<String, Object>> result = stj.queryForList(sql,
				new Object[] {});
		List<String> projCodes = new ArrayList<String>();
		if (result != null && result.size() > 0) {
			for (Map<String, Object> item : result) {
				projCodes.add((String) item.get("PROJ_CODE"));
			}
		}

		return projCodes;
	}

	public List<String> findOption() {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select distinct PROJ_OPTION from PIDB_PROJECT_CODE order by PROJ_OPTION";
		logger.debug(sql);
		List<Map<String, Object>> result = stj.queryForList(sql,
				new Object[] {});
		List<String> projCodes = new ArrayList<String>();
		if (result != null && result.size() > 0) {
			for (Map<String, Object> item : result) {
				projCodes.add((String) item.get("PROJ_OPTION"));
			}
		}

		return projCodes;
	}

	public List<String> findProjName() {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select distinct PROJ_NAME from PIDB_PROJECT order by PROJ_NAME";
		logger.debug(sql);
		List<Map<String, Object>> result = stj.queryForList(sql,
				new Object[] {});
		List<String> projNames = new ArrayList<String>();
		if (result != null && result.size() > 0) {
			for (Map<String, Object> item : result) {
				projNames.add((String) item.get("PROJ_NAME"));
			}
		}

		return projNames;
	}

	public ProjectTo findByProjectCode(String projCode) {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select "
				+ "p.PROJ_NAME,"
				+ "p.FAB,"
				+ "p.PITCH,"
				+ "p.PAD_TYPE,"
				+ "p.FS_DATE,"
				+ "p.PROC_TECH,"
				+ "p.POLY_METAL_LAYERS,"
				+ "p.VOLTAGE,"
				+ "p.MASK_LAYERS_NO,"
				+ "p.PROC_LAYER_NO,"
				+ "p.WAFER_TYPE,"
				+ "p.WAFER_INCH,"
				+ "p.X_SIZE,"
				+ "p.Y_SIZE,"
				+ "p.GROSS_DIE,"
				+ "p.FCST_CP_YIELD,"
				+ "p.TO_INCLUDE_SEALRING,"
				+ "p.SEALRING,"
				+ "p.SCRIBE_LINE,"
				+ "p.TEST_LINE,"
				+ "p.WAFER_THICKNESS,"
				+ "p.PROC_NAME,"
				+ "p.ANY_IP_USAGE,"
				+ "p.EMBEDDED_OTP,"
				+ "p.OTP_SIZE,"
				+ "p.PROJ_LEADER,"
				+ "p.DESIGN_ENGR,"
				+ "p.PROD_ENGR,"
				+ "p.ESD_ENGR,"
				+ "p.APR_ENGR,"
				+ "p.LAYOUT_ENGR,"
				+ "p.TEST_ENGR,"
				+ "p.ASSY_ENGR,"
				+ "p.APP_ENGR,"
				+ "p.PM,"
				+ "p.QA_ENGR,"
				+ "p.SALES_ENGR,"
				+ "p.ASSIGN_TO,"
				+ "p.ASSIGN_EMAIL,"
				+ "p.STATUS,"
				+ "p.PROD_FAMILY,"
				+ "p.PROD_LINE,"
				+ "p.PANEL_TYPE,"
				+ "p.IC_TYPE,"
				+ "pc.PROD_CODE PROD_CODE_LIST,"
				+ "p.CREATED_BY,"
				+ "p.MODIFIED_BY,"
				+ "p.ESTIMATED,"
				+ "p.NICK_NAME,"
				+ "p.SECOND_FOUNDRY_PROJECT,"
				+ "p.SUB_FAB,"
				+ "pc.PROJECT_TYPE,"
				+ "pc.PROJ_CODE,"
				+ "pc.PROJ_OPTION,"
				+ "pc.FUNC_REMARK,"
				+ "pc.CUST,"
				+ "pc.REMARK,"
				+ "pc.KICK_OFF_DATE,"
				+ "pc.RELEASE_TO"
				+ " from PIDB_PROJECT p, PIDB_PROJECT_CODE pc where p.PROJ_NAME=pc.PROJ_NAME and pc.PROJ_CODE = ?";
		logger.debug(sql);

		List<ProjectTo> ptl = stj.query(sql, new GenericRowMapper<ProjectTo>(
				ProjectTo.class), new Object[] { projCode });
		if (ptl != null && ptl.size() > 0) {
			return ptl.get(0);
		} else {
			return null;
		}

	}

	public List<String> findProjCust() {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select distinct CUST from PIDB_PROJECT_CODE order by CUST";
		logger.debug(sql);
		List<Map<String, Object>> result = stj.queryForList(sql,
				new Object[] {});
		List<String> projCusts = new ArrayList<String>();
		if (result != null && result.size() > 0) {
			for (Map<String, Object> item : result) {
				projCusts.add((String) item.get("CUST"));
			}
		}

		return projCusts;
	}

	public List<String> findFab() {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select distinct FAB from PIDB_PROJECT where "
				+ getAssertEmptyString("FAB") + " order by FAB";
		logger.debug(sql);
		List<Map<String, Object>> result = stj.queryForList(sql,
				new Object[] {});
		List<String> ret = new ArrayList<String>();
		if (result != null && result.size() > 0) {
			for (Map<String, Object> item : result) {
				ret.add((String) item.get("FAB"));
			}
		}

		return ret;
	}

	public List<String> findAllProdCodes() {
		SimpleJdbcTemplate sjt = getSimpleJdbcTemplate();
		String sql = "select * from PIDB_PROJECT";
		List<ProjectTo> projToList = sjt.query(sql,
				new GenericRowMapper<ProjectTo>(ProjectTo.class),
				new Object[] {});
		List<String> projCodeList = new ArrayList<String>();
		if (projToList != null && projToList.size() > 0) {
			for (ProjectTo oneProjTo : projToList) {
				String projCodes = oneProjTo.getProdCodeList();
				if (projCodes != null && projCodes.length() > 0) {
					String[] projCodeArray = projCodes.split(",");
					for (String temp : projCodeArray) {
						int sign = 0;
						for (String projCode : projCodeList) {
							if (projCode.equals(temp)) {
								sign = 1;
								break;
							}
						}// for projCodeArray
						if (sign == 0) {
							projCodeList.add(temp);
						}
					}// for projCodeList
				}// if projCodes
			} // for projToList
		}// if projToList
		return projCodeList;
	}

	public List<ProjectTo> findByProdCodes(String prodCodes) {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select * from pidb_project";
		if (prodCodes != null) {
			String spilter = ",";
			if (prodCodes != null && prodCodes.indexOf(";") > 0) {
				spilter = ";";
			}

			String subSql = "";
			String[] prodCodeList = prodCodes.split(spilter);
			for (String prodCode : prodCodeList) {
				if (prodCode != null && prodCode.length() > 0) {
					subSql += " or pidb_include(PROD_CODE_LIST, ',','"
							+ prodCode + "')=1";
				}
			}
			if (subSql.length() > 0) {
				subSql = subSql.substring(3);
				sql += " where " + subSql;
			}
		}

		// where pidb_contain(PROD_CODE_LIST, ',', '" + prodCodes + "',
		// '"+spilter+"')=1";
		logger.debug(sql);

		return stj.query(sql, new GenericRowMapper<ProjectTo>(ProjectTo.class),
				new Object[] {});

	}

	public ProjectTo findByProjName(String projName) {
		SimpleJdbcTemplate stj = getSimpleJdbcTemplate();
		String sql = "select * from pidb_project where PROJ_NAME = ? ";

		logger.debug(sql);
		List<ProjectTo> result = stj.query(sql,
				new GenericRowMapper<ProjectTo>(ProjectTo.class),
				new Object[] { projName });

		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}

	}
}
