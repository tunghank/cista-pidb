package com.cista.pidb.md.erp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cista.pidb.admin.dao.UserDao;
import com.cista.pidb.admin.to.UserTo;
import com.cista.pidb.code.dao.SapMasterPackageTypeDao;
import com.cista.pidb.code.to.SapMasterPackageTypeTo;
import com.cista.pidb.core.LdapHelper;
import com.cista.pidb.core.PIDBContext;
import com.cista.pidb.core.SequenceSupport;
import com.cista.pidb.md.action.SendMailDispatch;
import com.cista.pidb.md.dao.CpMaterialDao;
import com.cista.pidb.md.dao.CpTestProgramDao;
import com.cista.pidb.md.dao.FtTestProgramDao;
import com.cista.pidb.md.dao.IcWaferDao;
import com.cista.pidb.md.dao.IfMaterialCharacterDao;
import com.cista.pidb.md.dao.IfMaterialMasterDao;
import com.cista.pidb.md.dao.ProdStdTestRefDao;
import com.cista.pidb.md.dao.ProjectCodeDao;
import com.cista.pidb.md.dao.ProjectDao;
import com.cista.pidb.md.dao.TradPkgDao;
import com.cista.pidb.md.dao.WlmDao;
import com.cista.pidb.md.dao.WloDao;
import com.cista.pidb.md.erp.ERPHelper;
import com.cista.pidb.md.to.CpMaterialTo;
import com.cista.pidb.md.to.CpTestProgramTo;
import com.cista.pidb.md.to.FtTestProgramTo;
import com.cista.pidb.md.to.IcFgTo;
import com.cista.pidb.md.to.IcWaferTo;
import com.cista.pidb.md.to.IfMaterialCharacterTo;
import com.cista.pidb.md.to.IfMaterialMasterTo;
import com.cista.pidb.md.to.ProdStdTestRefTo;
import com.cista.pidb.md.to.ProjectCodeTo;
import com.cista.pidb.md.to.ProjectTo;
import com.cista.pidb.md.to.TradPkgTo;
import com.cista.pidb.md.to.WlmTo;
import com.cista.pidb.md.to.WloTo;

public class IcFgERP {
	private static final Log logger = LogFactory.getLog(LdapHelper.class);
	public static String release(final Object o, final UserTo userTo) {
		IcFgTo icFgTo = (IcFgTo) o;
		// Patched by matrix to avoid empty appliation category, 20070802
		if (StringUtils.isEmpty(icFgTo.getAppCategory())) {
			return "ERP-04-047";
		} // End.
		IfMaterialMasterDao ifMaterialMasterDao = new IfMaterialMasterDao();
		IfMaterialCharacterDao ifMaterialCharacterDao = new IfMaterialCharacterDao();
		ProjectDao projectDao = new ProjectDao();
		FtTestProgramDao ftTestProgramDao = new FtTestProgramDao();
		TradPkgDao tradPkgDao = new TradPkgDao();
		CpMaterialDao cpMaterialDao = new CpMaterialDao();
		IfMaterialMasterTo ifMaterialMasterTo = new IfMaterialMasterTo();
		IcWaferDao icWaferDao = new IcWaferDao();
		// Added on 3/9
		SapMasterPackageTypeDao sapMasterPackageTypeDao = new SapMasterPackageTypeDao();

		boolean mpFlag = icFgTo.getMpStatus() != null
				&& icFgTo.getMpStatus().equalsIgnoreCase("Non-MP") ? false
				: true;

		// Part I
		// insert PIDB_IF_MATERIAL_MASTER
		String pkgType = icFgTo.getPkgType();
		String md = "IcFg";
		if (pkgType != null) {
			if (pkgType.equalsIgnoreCase("304")
					|| pkgType.equalsIgnoreCase("305")) {
				md += ".2";
			} else {
				md += ".1";
			}
		} else {
			// return "Package Type is error!";
			return "ERP-04-001";
		}

		String materialType = ERPHelper.getMaterialType(md);
		ProjectTo projectTo = projectDao
				.findByProjectCode(icFgTo.getProjCode());
		if (projectTo == null) {
			// return "Project is not exist!";
			return "ERP-04-002";
		}
		String prodFamily = projectTo.getProdFamily();
		String icType = projectTo.getIcType();
		if (StringUtils.isEmpty(prodFamily)) {
			// return "Product family is empty!";
			return "ERP-04-003";
		}
		if (StringUtils.isEmpty(icType)) {
			return "ERP-04-045";
		}
		String materialGroup = ERPHelper.getMaterialGroup(materialType,
				prodFamily);

		List<Map<String, String>> classes = new ArrayList<Map<String, String>>();
		List<IfMaterialCharacterTo> charList = new ArrayList<IfMaterialCharacterTo>();

		String icFgProgList = icFgTo.getFtTestProgList();
		String ftMaterialNum = icFgTo.getMaterialNum();
		// if (mpFlag && StringUtils.isEmpty(icFgProgList)) {
		// return "ERP-04-019";
		// }
		// 取得project_Code中的release_To值
		ProjectCodeTo projectCode = new ProjectCodeDao()
				.findByProjectCode(icFgTo.getProjCode());

		if (icFgTo.getMpStatus().equalsIgnoreCase("MP")
				&& !pkgType.equalsIgnoreCase("303")
				&& !pkgType.equalsIgnoreCase("305")
				&& StringUtils.isEmpty(icFgProgList)) {
			return "ERP-04-019";
		}

		boolean releaseFT = false;
		if (icFgProgList != null) {
			if (icFgProgList.indexOf(",") < 0) {
				FtTestProgramTo tempTo = ftTestProgramDao.findByFtTestProgName(
						icFgProgList, ftMaterialNum);
				if (tempTo != null) {
					releaseFT = true;
				} else {
					releaseFT = false;
				}
			} else {
				releaseFT = true;
			}
		}

		if (pkgType.equalsIgnoreCase("304")) {
			// insert PIDB_IF_MATERIAL_MASTER
			ifMaterialMasterTo.setMaterialNum(icFgTo.getMaterialNum());
			ifMaterialMasterTo.setMaterialDesc(icFgTo.getPartNum());
			ifMaterialMasterTo.setMaterialType(materialType);
			ifMaterialMasterTo.setMaterialGroup(materialGroup);
			ifMaterialMasterTo.setAppCategory(icFgTo.getAppCategory());
			ifMaterialMasterTo.setProductFamily(prodFamily);
			ifMaterialMasterTo.setPkgType(icFgTo.getPkgType());
			ifMaterialMasterTo.setIcType(icType);
			ifMaterialMasterTo.setTimeStamp(new Date());
			ifMaterialMasterTo.setReleasedBy(userTo.getUserId());
			ifMaterialMasterTo.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
			// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
			ifMaterialMasterTo.setMpStatus(ERPHelper.judgeMP(icFgTo
					.getMaterialNum()));
			// 將取得的release_To塞到 PIDB_IF_Material_Master Table 中
			ifMaterialMasterTo.setReleaseTo(projectCode.getReleaseTo());

			ifMaterialMasterDao.insert(ifMaterialMasterTo,
					"PIDB_IF_MATERIAL_MASTER");

			// insert PIDB_IF_MATERIAL_CHARACTER
			Map<String, String> m = new HashMap<String, String>() {
				public String put(String key, String value) {
					if (StringUtils.isNotEmpty(value)) {
						super.put(key, value);
					}
					return null;
				}
			};

			if (mpFlag && StringUtils.isEmpty(icFgTo.getMcpPkg())) {
				// return "MCP Package is empty!";
				return "ERP-04-004";
			}
			if (mpFlag && StringUtils.isEmpty(icFgTo.getMcpDieQty())) {
				// return "MCP Die Quantity is empty!";
				return "ERP-04-005";
			}
			if (mpFlag && StringUtils.isEmpty(icFgTo.getMcpProd1())) {
				// return "MCP Product 1 is empty!";
				return "ERP-04-006";
			}
			if (mpFlag && StringUtils.isEmpty(icFgTo.getMcpProd2())) {
				// return "MCP Product 2 is empty!";
				return "ERP-04-007";
			}
			if (mpFlag && StringUtils.isEmpty(icFgTo.getMcpProd3())) {
				// return "MCP Product 3 is empty!";
				return "ERP-04-008";
			}
			if (mpFlag && StringUtils.isEmpty(icFgTo.getMcpProd4())) {
				// return "MCP Product 4 is empty!";
				return "ERP-04-009";
			}
			if (mpFlag && StringUtils.isEmpty(icFgTo.getPkgType())) {
				// return "Package Type is empty!";
				return "ERP-04-010";
			}

			String mcpPkg = icFgTo.getMcpPkg();
			if (mcpPkg.equalsIgnoreCase("1")) {
				mcpPkg = "Yes";
			} else if (mcpPkg.equalsIgnoreCase("0")) {
				mcpPkg = "No";
			}
			m.put("FG_01", mcpPkg);
			m.put("FG_02", icFgTo.getMcpDieQty());
			m.put("FG_03", icFgTo.getMcpProd1());
			m.put("FG_04", icFgTo.getMcpProd2());
			m.put("FG_05", icFgTo.getMcpProd3());
			m.put("FG_06", icFgTo.getMcpProd4());

			List<TradPkgTo> tradPkgToList = tradPkgDao
					.findByProjNameAndPkgCode(projectTo.getProjName(), icFgTo
							.getPkgCode());

			if (mpFlag
					&& (CollectionUtils.isEmpty(tradPkgToList) || StringUtils
							.isEmpty(tradPkgToList.get(0).getTradPkgType()))) {
				return "ERP-04-041";
			}

			m.put("FG_07", tradPkgToList.get(0).getTradPkgType());
			// if (tradPkgToList != null
			// && tradPkgToList.size() > 0
			// && StringUtils.isNotEmpty(tradPkgToList.get(0)
			// .getTradPkgType())) {
			// m.put("FG_07", tradPkgToList.get(0).getTradPkgType());
			// } else {
			// return "ERP-04-041";
			// }

			// add FT
			if (mpFlag && StringUtils.isEmpty(projectTo.getProjName())) {
				// return "Project Name is empty!";
				return "ERP-04-011";
			}
			if (mpFlag && StringUtils.isEmpty(projectTo.getProjCode())) {
				// return "Project Code is empty!";
				return "ERP-04-012";
			}
			if (mpFlag && StringUtils.isEmpty(projectTo.getWaferInch())) {
				// return "Wafer Inch is empty!";
				return "ERP-04-013";
			}
			m.put("FG_08", projectTo.getProjName());
			m.put("FG_09", projectTo.getProjCode());
			m.put("FG_10", projectTo.getWaferInch());

			if (releaseFT) {
				List<FtTestProgramTo> ftList = ftTestProgramDao
						.findByFtTestProgNameList(icFgTo.getFtTestProgList());
				if (ftList != null && ftList.size() > 0) {
					String tester = "";
					double ftCpuTime = 0;
					double ftIndexTime = 0;
					String contactDieQty = "";
					String testerConfig = "";

					for (FtTestProgramTo ft : ftList) {

						if (mpFlag && StringUtils.isEmpty(ft.getTester())) {
							// return "FT Tester is empty!";
							return "ERP-04-014";
						}
						if (mpFlag && ft.getFtCpuTime() == null) {
							// return "FT CPU Time is empty!";
							return "ERP-04-015";
						}
						if (mpFlag && ft.getFtIndexTime() == null) {
							// return "FT Index Time is empty!";
							return "ERP-04-016";
						}
						if (mpFlag
								&& StringUtils.isEmpty(ft.getContactDieQty())) {
							// return "Contact Die Quantity is empty!";
							return "ERP-04-017";
						}
						if (mpFlag && StringUtils.isEmpty(ft.getTesterConfig())) {
							// return "Tester Config is empty!";
							return "ERP-04-018";
						}

						tester += "," + ft.getTester();
						ftCpuTime += ft.getFtCpuTime().doubleValue();
						ftIndexTime += ft.getFtIndexTime().doubleValue();
						contactDieQty += "," + ft.getContactDieQty();
						testerConfig += "," + ft.getTesterConfig();
					}

					if (ftList.size() > 1) {
						m.put("FG_12", "YES");
					} else {
						m.put("FG_12", "No");
					}
					m.put("FG_11", tester.substring(1));
					m.put("FG_13", ftCpuTime + "");
					m.put("FG_14", ftIndexTime + "");
					m.put("FG_16", contactDieQty.substring(1));
					m.put("FG_17", testerConfig.substring(1));
				} else if (mpFlag) {
					return "ERP-04-042";
				}

			} else {
				ProdStdTestRefDao prodStdTestRefDao = new ProdStdTestRefDao();
				ProdStdTestRefTo prodStdTestRefTo = prodStdTestRefDao
						.findByTestReferenceId(icFgTo.getFtTestProgList());
				if (prodStdTestRefTo != null) {

					if (mpFlag
							&& StringUtils.isEmpty(prodStdTestRefTo
									.getFtTesterE())) {
						return "ERP-04-014";
					}
					if (mpFlag
							&& StringUtils.isEmpty(prodStdTestRefTo
									.getFtCpuTimeE())) {
						return "ERP-04-015";
					}
					if (mpFlag
							&& StringUtils.isEmpty(prodStdTestRefTo
									.getFtIndexTimeE())) {
						return "ERP-04-016";
					}
					if (mpFlag
							&& StringUtils.isEmpty(prodStdTestRefTo
									.getFtContactDieQty())) {
						return "ERP-04-017";
					}
					if (mpFlag
							&& StringUtils.isEmpty(prodStdTestRefTo
									.getFtTesterSpecE())) {
						return "ERP-04-018";
					}

					m.put("FG_12", "No");
					m.put("FG_11", prodStdTestRefTo.getFtTesterE());
					m.put("FG_13", prodStdTestRefTo.getFtCpuTimeE());
					m.put("FG_14", prodStdTestRefTo.getFtIndexTimeE());
					m.put("FG_16", prodStdTestRefTo.getFtContactDieQty());
					m.put("FG_17", prodStdTestRefTo.getFtTesterSpecE());
				} else if (mpFlag) {
					return "ERP-04-043";
				}
			}

			if (mpFlag && StringUtils.isEmpty(projectTo.getGrossDie())) {
				// return "Wafer Gross is empty!";
				return "ERP-04-020";
			}

			m.put("FG_15", projectTo.getGrossDie());

			classes.add(m);
		} else if (Integer.parseInt(pkgType) > 304) {
			// insert PIDB_IF_MATERIAL_MASTER
			ifMaterialMasterTo.setMaterialNum(icFgTo.getMaterialNum());
			ifMaterialMasterTo.setMaterialDesc(icFgTo.getPartNum());
			ifMaterialMasterTo.setMaterialType(materialType);
			ifMaterialMasterTo.setMaterialGroup(materialGroup);
			ifMaterialMasterTo.setAppCategory(icFgTo.getAppCategory());
			ifMaterialMasterTo.setProductFamily(prodFamily);
			ifMaterialMasterTo.setPkgType(icFgTo.getPkgType());
			ifMaterialMasterTo.setIcType(icType);
			ifMaterialMasterTo.setTimeStamp(new Date());
			ifMaterialMasterTo.setReleasedBy(userTo.getUserId());
			ifMaterialMasterTo.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
			// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
			ifMaterialMasterTo.setMpStatus(ERPHelper.judgeMP(icFgTo
					.getMaterialNum()));
			ifMaterialMasterTo.setReleaseTo(projectCode.getReleaseTo());

			ifMaterialMasterDao.insert(ifMaterialMasterTo,
					"PIDB_IF_MATERIAL_MASTER");
		} else {
			// insert PIDB_IF_MATERIAL_MASTER
			ifMaterialMasterTo.setMaterialNum(icFgTo.getMaterialNum());
			ifMaterialMasterTo.setMaterialDesc(icFgTo.getPartNum());
			ifMaterialMasterTo.setMaterialType(materialType);
			ifMaterialMasterTo.setMaterialGroup(materialGroup);
			ifMaterialMasterTo.setAppCategory(icFgTo.getAppCategory());
			ifMaterialMasterTo.setProductFamily(prodFamily);
			ifMaterialMasterTo.setPkgType(icFgTo.getPkgType());
			ifMaterialMasterTo.setIcType(icType);
			ifMaterialMasterTo.setTimeStamp(new Date());
			ifMaterialMasterTo.setReleasedBy(userTo.getUserId());
			ifMaterialMasterTo.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
			// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
			ifMaterialMasterTo.setMpStatus(ERPHelper.judgeMP(icFgTo
					.getMaterialNum()));
			ifMaterialMasterTo.setReleaseTo(projectCode.getReleaseTo());

			ifMaterialMasterDao.insert(ifMaterialMasterTo,
					"PIDB_IF_MATERIAL_MASTER");

			Map<String, String> m = new HashMap<String, String>() {
				public String put(String key, String value) {
					if (StringUtils.isNotEmpty(value)) {
						super.put(key, value);
					}
					return null;
				}
			};

			if (mpFlag && StringUtils.isEmpty(projectTo.getProjName())) {
				// return "Project Name is empty!";
				return "ERP-04-011";
			}
			if (mpFlag && StringUtils.isEmpty(projectTo.getProjCode())) {
				// return "Project Code is empty!";
				return "ERP-04-012";
			}
			if (mpFlag && StringUtils.isEmpty(projectTo.getWaferInch())) {
				// return "Wafer Inch is empty!";
				return "ERP-04-013";
			}
			m.put("FT_01", projectTo.getProjName());
			m.put("FT_02", projectTo.getProjCode());
			m.put("FT_03", projectTo.getWaferInch());

			if (!pkgType.equalsIgnoreCase("303")
					&& !pkgType.equalsIgnoreCase("305")) {
				if (releaseFT) {
					List<FtTestProgramTo> ftList = ftTestProgramDao
							.findByFtTestProgNameList(icFgTo
									.getFtTestProgList());
					if (ftList != null && ftList.size() > 0) {
						String tester = "";
						double ftCpuTime = 0;
						double ftIndexTime = 0;
						String contactDieQty = "";
						String testerConfig = "";

						for (FtTestProgramTo ft : ftList) {

							if (mpFlag && StringUtils.isEmpty(ft.getTester())) {
								// return "FT Tester is empty!";
								return "ERP-04-014";
							}
							if (mpFlag && ft.getFtCpuTime() == null) {
								// return "FT CPU Time is empty!";
								return "ERP-04-015";
							}
							if (mpFlag && ft.getFtIndexTime() == null) {
								// return "FT Index Time is empty!";
								return "ERP-04-016";
							}
							if (mpFlag
									&& StringUtils.isEmpty(ft
											.getContactDieQty())) {
								// return "Contact Die Quantity is empty!";
								return "ERP-04-017";
							}
							if (mpFlag
									&& StringUtils
											.isEmpty(ft.getTesterConfig())) {
								// return "Tester Config is empty!";
								return "ERP-04-018";
							}

							tester += "," + ft.getTester();
							ftCpuTime += ft.getFtCpuTime().doubleValue();
							ftIndexTime += ft.getFtIndexTime().doubleValue();
							contactDieQty += "," + ft.getContactDieQty();
							testerConfig += "," + ft.getTesterConfig();
						}

						if (ftList.size() > 1) {
							m.put("FT_05", "YES");
						} else {
							m.put("FT_05", "No");
						}
						m.put("FT_04", tester.substring(1));
						m.put("FT_06", ftCpuTime + "");
						m.put("FT_07", ftIndexTime + "");
						m.put("FT_09", contactDieQty.substring(1));
						m.put("FT_10", testerConfig.substring(1));
					} else if (mpFlag) {
						return "ERP-04-042";
					}

				} else {
					ProdStdTestRefDao prodStdTestRefDao = new ProdStdTestRefDao();
					ProdStdTestRefTo prodStdTestRefTo = prodStdTestRefDao
							.findByTestReferenceId(icFgTo.getFtTestProgList());
					if (prodStdTestRefTo != null) {

						if (mpFlag
								&& StringUtils.isEmpty(prodStdTestRefTo
										.getFtTesterE())) {
							return "ERP-04-014";
						}
						if (mpFlag
								&& StringUtils.isEmpty(prodStdTestRefTo
										.getFtCpuTimeE())) {
							return "ERP-04-015";
						}
						if (mpFlag
								&& StringUtils.isEmpty(prodStdTestRefTo
										.getFtIndexTimeE())) {
							return "ERP-04-016";
						}
						if (mpFlag
								&& StringUtils.isEmpty(prodStdTestRefTo
										.getFtContactDieQty())) {
							return "ERP-04-017";
						}
						if (mpFlag
								&& StringUtils.isEmpty(prodStdTestRefTo
										.getFtTesterSpecE())) {
							return "ERP-04-018";
						}

						m.put("FT_05", "No");
						m.put("FT_04", prodStdTestRefTo.getFtTesterE());
						m.put("FT_06", prodStdTestRefTo.getFtCpuTimeE());
						m.put("FT_07", prodStdTestRefTo.getFtIndexTimeE());
						m.put("FT_09", prodStdTestRefTo.getFtContactDieQty());
						m.put("FT_10", prodStdTestRefTo.getFtTesterSpecE());
					} else if (mpFlag) {
						return "ERP-04-043";
					}
				}
			}

			if (mpFlag && StringUtils.isEmpty(projectTo.getGrossDie())) {
				// return "Wafer Gross is empty!";
				return "ERP-04-020";
			}
			if (mpFlag && StringUtils.isEmpty(icFgTo.getPkgType())) {
				// return "Package Type is empty!";
				return "ERP-04-021";
			}

			// Added on 3/9
			SapMasterPackageTypeTo sapMasterPackageTypeTo = sapMasterPackageTypeDao
					.findByPkgType(icFgTo.getPkgType());

			m.put("FT_08", projectTo.getGrossDie());
			// m.put("FT_11", icFgTo.getPkgType());
			// Added on 3/9
			m.put("FT_11", sapMasterPackageTypeTo.getDescription());

			classes.add(m);

		}

		for (Map<String, String> m : classes) {
			for (String s : m.keySet()) {
				IfMaterialCharacterTo ifMaterialCharacterTo = new IfMaterialCharacterTo();
				ifMaterialCharacterTo.setMaterialNum(icFgTo.getMaterialNum());
				ifMaterialCharacterTo.setMaterialType(materialType);
				ifMaterialCharacterTo.setChTechName(s);
				ifMaterialCharacterTo.setChValue(m.get(s));
				ifMaterialCharacterTo.setId(SequenceSupport
						.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
				ifMaterialCharacterTo.setTimeStamp(new Date());
				ifMaterialCharacterTo.setReleasedBy(userTo.getUserId());
				ifMaterialCharacterTo.setReleaseTo(projectCode.getReleaseTo());
				charList.add(ifMaterialCharacterTo);
			}
		}
		ifMaterialCharacterDao.batchInsert(charList,
				"PIDB_IF_MATERIAL_CHARACTER");

		// Part II
		// insert PIDB_IF_MATERIAL_MASTER
		classes = new ArrayList<Map<String, String>>();
		charList = new ArrayList<IfMaterialCharacterTo>();
		ifMaterialMasterTo = new IfMaterialMasterTo();
		md = "IcFg";
		materialType = "";
		materialGroup = "";

		if (icFgTo.getRoutingAs()
				&& (pkgType.equalsIgnoreCase("304")
						|| pkgType.equalsIgnoreCase("305") || !pkgType
						.equalsIgnoreCase("303"))) {

			if (icFgTo.getRoutingAs()
					&& (pkgType.equalsIgnoreCase("304") || pkgType
							.equalsIgnoreCase("305"))) {
				// insert ZAS2 to PIDB_IF_MATERIAL_MASTER ELSE
				md += ".4";

			} else if (icFgTo.getRoutingAs()
					&& !pkgType.equalsIgnoreCase("303")
					&& !pkgType.equalsIgnoreCase("305")) {
				// insert ZAS to PIDB_IF_MATERIAL_MASTER ELSE
				md += ".3";
			}

			materialType = ERPHelper.getMaterialType(md);
			materialGroup = ERPHelper
					.getMaterialGroup(materialType, prodFamily);
			ifMaterialMasterTo.setMaterialNum("A"
					+ (icFgTo.getMaterialNum().substring(1)));
			ifMaterialMasterTo.setMaterialDesc(icFgTo.getPartNum());
			ifMaterialMasterTo.setMaterialType(materialType);
			ifMaterialMasterTo.setMaterialGroup(materialGroup);
			ifMaterialMasterTo.setTimeStamp(new Date());
			ifMaterialMasterTo.setReleasedBy(userTo.getUserId());
			ifMaterialMasterTo.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
			// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
			ifMaterialMasterTo.setMpStatus(ERPHelper.judgeMP(icFgTo
					.getMaterialNum()));
			ifMaterialMasterTo.setReleaseTo(projectCode.getReleaseTo());
			ifMaterialMasterDao.insert(ifMaterialMasterTo,
					"PIDB_IF_MATERIAL_MASTER");
		}

		// insert PIDB_IF_MATERIAL_CHARACTER
		// classes = new ArrayList<Map<String, String>>();
		md = "IcFg";
		materialType = "";
		materialGroup = "";

		if (icFgTo.getRoutingAs() && pkgType.equalsIgnoreCase("304")) {
			List<TradPkgTo> pkgList = tradPkgDao.findByProjNameAndPkgCode(
					projectTo.getProjName(), icFgTo.getPkgCode());
			md += ".4";

			TradPkgTo pkg = new TradPkgTo();
			if (pkgList != null && pkgList.size() > 0) {
				pkg = pkgList.get(0);
			} else {
				return "ERP-04-044";
			}

			Map<String, String> m = new HashMap<String, String>() {
				public String put(String key, String value) {
					if (StringUtils.isNotEmpty(value)) {
						super.put(key, value);
					}
					return null;
				}
			};

			if (mpFlag && StringUtils.isEmpty(pkg.getTradPkgType())) {
				// return "Traditional Package Type is empty!";
				return "ERP-04-022";
			}
			if (mpFlag && StringUtils.isEmpty(pkg.getPinCount())) {
				// return "Pin Count is empty!";
				return "ERP-04-023";
			}
			if (mpFlag && StringUtils.isEmpty(pkg.getLeadFrameType())) {
				// return "Lead Frame Type is empty!";
				return "ERP-04-024";
			}
			if (mpFlag && StringUtils.isEmpty(pkg.getSubtractLayer())) {
				// return "Subtract Layer is empty!";
				return "ERP-04-025";
			}
			if (mpFlag && StringUtils.isEmpty(pkg.getBodySize())) {
				// return "Body Size is empty!";
				return "ERP-04-026";
			}
			if (mpFlag && StringUtils.isEmpty(pkg.getGoldenWireWidth())) {
				// return "Golden Wire Width is empty!";
				return "ERP-04-027";
			}
			if (mpFlag && StringUtils.isEmpty(pkg.getMcpPkg())) {
				// return "MCP Package is empty!";
				return "ERP-04-028";
			}
			if (mpFlag && StringUtils.isEmpty(pkg.getMcpDieQty())) {
				// return "MCP Die Quantity is empty!";
				return "ERP-04-029";
			}

			m.put("AS_01", pkg.getTradPkgType());
			m.put("AS_02", pkg.getPinCount());
			m.put("AS_03", pkg.getLeadFrameType());
			m.put("AS_04", pkg.getSubtractLayer());
			m.put("AS_05", pkg.getBodySize());
			m.put("AS_06", pkg.getGoldenWireWidth());
			m
					.put("AS_07", pkg.getMcpPkg().equalsIgnoreCase("Y") ? "Yes"
							: "No");
			m.put("AS_08", pkg.getMcpDieQty());

			classes.add(m);

		} else if (icFgTo.getRoutingAs() && !pkgType.equalsIgnoreCase("303")
				&& !pkgType.equalsIgnoreCase("305")) {
			md += ".3";

			Map<String, String> m = new HashMap<String, String>() {
				public String put(String key, String value) {
					if (StringUtils.isNotEmpty(value)) {
						super.put(key, value);
					}
					return null;
				}
			};

			if (mpFlag && StringUtils.isEmpty(pkgType)) {
				// return "Package Type is empty!";
				return "ERP-04-030";
			}
			if (mpFlag && StringUtils.isEmpty(projectTo.getPitch())) {
				// return "Pitch (UM) is empty!";
				return "ERP-04-031";
			}

			// Added on 3/9
			SapMasterPackageTypeTo sapMasterPackageTypeTo = sapMasterPackageTypeDao
					.findByPkgType(pkgType);

			// m.put("IL_01", pkgType);
			// Added on 3/9
			if( sapMasterPackageTypeTo != null  ){
				String iLPkgType = sapMasterPackageTypeTo.getDescription();
				if(projectTo.getProdFamily().equals("101")){//表Source
					iLPkgType = iLPkgType + "-SD";
				}else if(projectTo.getProdFamily().equals("102") ){//表Gate
					iLPkgType = iLPkgType + "-GD";
				}
				m.put("IL_01", iLPkgType);
			}else{
				m.put("IL_01",
						sapMasterPackageTypeTo != null ? sapMasterPackageTypeTo
								.getDescription() : "");
			}
			m.put("IL_02", projectTo.getPitch());

			classes.add(m);
		}

		materialType = ERPHelper.getMaterialType(md);
		materialGroup = ERPHelper.getMaterialGroup(materialType, prodFamily);

		for (Map<String, String> m : classes) {
			for (String s : m.keySet()) {
				IfMaterialCharacterTo ifMaterialCharacterTo = new IfMaterialCharacterTo();
				ifMaterialCharacterTo.setMaterialNum("A"
						+ (icFgTo.getMaterialNum().substring(1)));
				ifMaterialCharacterTo.setMaterialType(materialType);
				ifMaterialCharacterTo.setChTechName(s);
				ifMaterialCharacterTo.setChValue(m.get(s));
				ifMaterialCharacterTo.setId(SequenceSupport
						.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
				ifMaterialCharacterTo.setTimeStamp(new Date());
				ifMaterialCharacterTo.setReleasedBy(userTo.getUserId());
				ifMaterialCharacterTo.setReleaseTo(projectCode.getReleaseTo());
				charList.add(ifMaterialCharacterTo);
			}
		}
		ifMaterialCharacterDao.batchInsert(charList,
				"PIDB_IF_MATERIAL_CHARACTER");

		// Part III

		classes = new ArrayList<Map<String, String>>();
		charList = new ArrayList<IfMaterialCharacterTo>();
		ifMaterialMasterTo = new IfMaterialMasterTo();
		md = "CpMaterial.2";
		materialType = ERPHelper.getMaterialType(md);
		materialGroup = ERPHelper.getMaterialGroup(materialType, prodFamily);
		CpMaterialTo cpMaterialTo = cpMaterialDao.findByCpMaterialNum(icFgTo
				.getCpMaterialNum());

		if (cpMaterialTo != null) {
			String w_meterialNum = "";
			w_meterialNum = "W" + cpMaterialTo.getCpMaterialNum().substring(1);
			IcWaferTo icWaferTo = icWaferDao.findByPrimaryKey(w_meterialNum);
			String materialDesc = "";
			if (icWaferTo != null) {
				materialDesc = icWaferTo.getMaterialDesc();
			} else {
				materialDesc = cpMaterialTo.getProjectCodeWVersion();
			}

			// insert PIDB_IF_MATERIAL_MASTER
			ifMaterialMasterTo.setMaterialNum(cpMaterialTo.getCpMaterialNum());
			ifMaterialMasterTo.setMaterialDesc(materialDesc);
			ifMaterialMasterTo.setMaterialType(materialType);
			ifMaterialMasterTo.setMaterialGroup(materialGroup);
			ifMaterialMasterTo.setPurchaseOrderText(cpMaterialTo
					.getProjectCodeWVersion());
			ifMaterialMasterTo.setTimeStamp(new Date());
			ifMaterialMasterTo.setReleasedBy(userTo.getUserId());
			ifMaterialMasterTo.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
			// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
			ifMaterialMasterTo.setMpStatus(ERPHelper
					.judgeMPByProjWVersion(cpMaterialTo
							.getProjectCodeWVersion()));
			ifMaterialMasterTo.setReleaseTo(projectCode.getReleaseTo());

			ifMaterialMasterDao.insert(ifMaterialMasterTo,
					"PIDB_IF_MATERIAL_MASTER");

			// insert PIDB_IF_MATERIAL_CHARACTER
			Map<String, String> m = new HashMap<String, String>() {
				public String put(String key, String value) {
					if (StringUtils.isNotEmpty(value)) {
						super.put(key, value);
					}
					return null;
				}
			};
			CpTestProgramDao cpTestProgramDao = new CpTestProgramDao();

			m.put("CP_01", projectTo.getProjName());
			m.put("CP_02", projectTo.getProjCode());
			m.put("CP_03", projectTo.getWaferInch());

			String cpNameList = cpMaterialTo.getCpTestProgramNameList();

			// Remove Hank 2008/01/30
			/*
			 * if (StringUtils.isEmpty(cpNameList)) { // return
			 * "CpTestProgramNameList is empty!"; return "ERP-04-032"; }
			 */

			String[] cpName = cpNameList.split(",");

			if (pkgType.equalsIgnoreCase("304")
					|| pkgType.equalsIgnoreCase("305")) {
				List<IcWaferTo> waferList = icWaferDao.findByProjCode(icFgTo
						.getProjCode());
				if (waferList != null && waferList.size() > 0
						&& waferList.get(0).getRoutingCp()) {

					String tester = "";
					double cpCpuTime = 0;
					double cpIndexTime = 0;
					String contactDieQty = "";
					String testerConfig = "";

					if (cpName.length > 0) {
						for (String s : cpName) {
							CpTestProgramTo cpTestProgramTo = cpTestProgramDao
									.find(s);

							if (cpTestProgramTo == null) {
								continue;
							}

							if (mpFlag
									&& StringUtils.isEmpty(cpTestProgramTo
											.getTester())) {
								// return "Tester is empty!";
								return "ERP-04-033";
							}
							if (mpFlag
									&& cpTestProgramTo.getCpCpuTime() == null) {
								// return "CP CPU Time(S) is empty!";
								return "ERP-04-034";
							}
							if (mpFlag
									&& cpTestProgramTo.getCpIndexTime() == null) {
								// return "CP Index Time(S) is empty!";
								return "ERP-04-035";
							}
							if (mpFlag
									&& StringUtils.isEmpty(cpTestProgramTo
											.getContactDieQty())) {
								// return "Contact Die Quantity is empty!";
								return "ERP-04-036";
							}
							if (mpFlag
									&& StringUtils.isEmpty(cpTestProgramTo
											.getTesterConfig())) {
								// return "Tester Config is empty!";
								return "ERP-04-037";
							}

							tester += "," + cpTestProgramTo.getTester();
							cpCpuTime += cpTestProgramTo.getCpCpuTime()
									.doubleValue();
							cpIndexTime += cpTestProgramTo.getCpIndexTime()
									.doubleValue();
							contactDieQty += ","
									+ cpTestProgramTo.getContactDieQty();
							testerConfig += ","
									+ cpTestProgramTo.getTesterConfig();
						}
					}

					if (cpName.length > 1) {
						m.put("CP_05", "YES");
					} else {
						m.put("CP_05", "No");
					}

					if (mpFlag && StringUtils.isEmpty(projectTo.getGrossDie())) {
						// return "Wafer Gross is empty!";
						return "ERP-04-038";
					}
					if (mpFlag
							&& StringUtils.isEmpty(projectTo.getFcstCpYield())) {
						// return "Forecast CP Yield(%) is empty!";
						return "ERP-04-039";
					}

					if (mpFlag && StringUtils.isEmpty(tester)) {
						return "ERP-04-033";
					}
					if (mpFlag && StringUtils.isEmpty(contactDieQty)) {
						return "ERP-04-036";
					}
					if (mpFlag && StringUtils.isEmpty(testerConfig)) {
						return "ERP-04-037";
					}

					m.put("CP_04", tester.substring(1));
					m.put("CP_06", cpCpuTime + "");
					m.put("CP_07", cpIndexTime + "");
					m.put("CP_09", contactDieQty.substring(1));
					m.put("CP_10", testerConfig.substring(1));
				}

			}

			m.put("CP_08", projectTo.getGrossDie());
			m.put("CP_11", projectTo.getFcstCpYield());

			classes.add(m);
		} else {
			if (pkgType.equalsIgnoreCase("304")
					|| pkgType.equalsIgnoreCase("305")) {
				List<IcWaferTo> waferList = icWaferDao.findByProjCode(icFgTo
						.getProjCode());
				if (waferList != null && waferList.size() > 0
						&& waferList.get(0).getRoutingCp()
						&& icFgTo.getMpStatus().equalsIgnoreCase("MP")) {
					return "ERP-04-046";
				}
			}
		}

		for (Map<String, String> m : classes) {
			for (String s : m.keySet()) {
				IfMaterialCharacterTo ifMaterialCharacterTo = new IfMaterialCharacterTo();
				ifMaterialCharacterTo.setMaterialNum(cpMaterialTo
						.getCpMaterialNum());
				ifMaterialCharacterTo.setMaterialType(materialType);
				ifMaterialCharacterTo.setChTechName(s);
				ifMaterialCharacterTo.setChValue(m.get(s));
				ifMaterialCharacterTo.setId(SequenceSupport
						.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
				ifMaterialCharacterTo.setTimeStamp(new Date());
				ifMaterialCharacterTo.setReleasedBy(userTo.getUserId());
				ifMaterialCharacterTo.setReleaseTo(projectCode.getReleaseTo());
				charList.add(ifMaterialCharacterTo);
			}
		}
		ifMaterialCharacterDao.batchInsert(charList,
				"PIDB_IF_MATERIAL_CHARACTER");

		// Part IV
		classes = new ArrayList<Map<String, String>>();
		charList = new ArrayList<IfMaterialCharacterTo>();
		ifMaterialMasterTo = new IfMaterialMasterTo();
		md = "CpMaterial.1";
		materialType = ERPHelper.getMaterialType(md);
		materialGroup = ERPHelper.getMaterialGroup(materialType, prodFamily);

		if (!StringUtils.isEmpty(icFgTo.getCpMaterialNum())) {
			// insert PIDB_IF_MATERIAL_MASTER
			String cpMateriaNum = cpMaterialTo.getCpMaterialNum();
			String w_meterialNum = "";
			w_meterialNum = "W" + cpMateriaNum.substring(1);
			IcWaferTo icWaferTo = icWaferDao.findByPrimaryKey(w_meterialNum);
			String materialDesc = "";
			if (icWaferTo != null) {
				materialDesc = icWaferTo.getMaterialDesc();
			} else {
				materialDesc = cpMaterialTo.getProjectCodeWVersion();
			}

			ifMaterialMasterTo
					.setMaterialNum("D" + (cpMateriaNum.substring(1)));
			ifMaterialMasterTo.setMaterialDesc(materialDesc);
			ifMaterialMasterTo.setMaterialType(materialType);
			ifMaterialMasterTo.setMaterialGroup(materialGroup);
			ifMaterialMasterTo.setPurchaseOrderText(cpMaterialTo
					.getProjectCodeWVersion());
			ifMaterialMasterTo.setTimeStamp(new Date());
			ifMaterialMasterTo.setReleasedBy(userTo.getUserId());
			ifMaterialMasterTo.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
			// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
			ifMaterialMasterTo.setMpStatus(ERPHelper
					.judgeMPByProjWVersion(cpMaterialTo
							.getProjectCodeWVersion()));
			ifMaterialMasterTo.setReleaseTo(projectCode.getReleaseTo());
			ifMaterialMasterDao.insert(ifMaterialMasterTo,
					"PIDB_IF_MATERIAL_MASTER");

			// insert PIDB_IF_MATERIAL_CHARACTER
			Map<String, String> m = new HashMap<String, String>() {
				public String put(String key, String value) {
					if (StringUtils.isNotEmpty(value)) {
						super.put(key, value);
					}
					return null;
				}
			};
			m.put("DS_01", projectTo.getGrossDie());
			m.put("DS_02", projectTo.getFcstCpYield());
			classes.add(m);
		}

		for (Map<String, String> m : classes) {
			for (String s : m.keySet()) {
				IfMaterialCharacterTo ifMaterialCharacterTo = new IfMaterialCharacterTo();
				ifMaterialCharacterTo.setMaterialNum("D"
						+ (cpMaterialTo.getCpMaterialNum().substring(1)));
				ifMaterialCharacterTo.setMaterialType(materialType);
				ifMaterialCharacterTo.setChTechName(s);
				ifMaterialCharacterTo.setChValue(m.get(s));
				ifMaterialCharacterTo.setId(SequenceSupport
						.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
				ifMaterialCharacterTo.setReleasedBy(userTo.getUserId());
				ifMaterialCharacterTo.setTimeStamp(new Date());
				ifMaterialCharacterTo.setReleaseTo(projectCode.getReleaseTo());
				charList.add(ifMaterialCharacterTo);
			}
		}
		ifMaterialCharacterDao.batchInsert(charList,
				"PIDB_IF_MATERIAL_CHARACTER");

		// end

		return null;

	}

	/*
	 * Added Hank 2008/01/15 For MP Release Just Release F & A not include C & D
	 */

	public static String releaseForMPList(final Object o, final UserTo userTo,
			final String className) {
		IcFgTo icFgTo = (IcFgTo) o;
		// Patched by matrix to avoid empty appliation category, 20070802
		if (StringUtils.isEmpty(icFgTo.getAppCategory())) {
			return "ERP-04-047";
		} // End.
		IfMaterialMasterDao ifMaterialMasterDao = new IfMaterialMasterDao();
		IfMaterialCharacterDao ifMaterialCharacterDao = new IfMaterialCharacterDao();
		ProjectDao projectDao = new ProjectDao();
		FtTestProgramDao ftTestProgramDao = new FtTestProgramDao();
		TradPkgDao tradPkgDao = new TradPkgDao();
		CpMaterialDao cpMaterialDao = new CpMaterialDao();
		IfMaterialMasterTo ifMaterialMasterTo = new IfMaterialMasterTo();
		IcWaferDao icWaferDao = new IcWaferDao();
		// Added on 3/9
		SapMasterPackageTypeDao sapMasterPackageTypeDao = new SapMasterPackageTypeDao();

		boolean mpFlag = icFgTo.getMpStatus() != null
				&& icFgTo.getMpStatus().equalsIgnoreCase("Non-MP") ? false
				: true;

		// Part I
		// insert PIDB_IF_MATERIAL_MASTER
		String pkgType = icFgTo.getPkgType();
		String md = "IcFg";
		if (pkgType != null) {
			if (pkgType.equalsIgnoreCase("304")
					|| pkgType.equalsIgnoreCase("305")) {
				md += ".2";
			} else {
				md += ".1";
			}
		} else {
			// return "Package Type is error!";
			return "ERP-04-001";
		}

		String materialType = ERPHelper.getMaterialType(md);
		ProjectTo projectTo = projectDao
				.findByProjectCode(icFgTo.getProjCode());
		if (projectTo == null) {
			// return "Project is not exist!";
			return "ERP-04-002";
		}
		String prodFamily = projectTo.getProdFamily();
		String icType = projectTo.getIcType();
		if (StringUtils.isEmpty(prodFamily)) {
			// return "Product family is empty!";
			return "ERP-04-003";
		}
		if (StringUtils.isEmpty(icType)) {
			return "ERP-04-045";
		}
		String materialGroup = ERPHelper.getMaterialGroup(materialType,
				prodFamily);

		List<Map<String, String>> classes = new ArrayList<Map<String, String>>();
		List<IfMaterialCharacterTo> charList = new ArrayList<IfMaterialCharacterTo>();

		String icFgProgList = icFgTo.getFtTestProgList();
		String ftMaterialNum = icFgTo.getMaterialNum();
		// if (mpFlag && StringUtils.isEmpty(icFgProgList)) {
		// return "ERP-04-019";
		// }

		// Remove Hank 2008/01/30
		/*
		 * if (icFgTo.getMpStatus().equalsIgnoreCase("MP") &&
		 * !pkgType.equalsIgnoreCase("303") &&
		 * StringUtils.isEmpty(icFgProgList)) { return "ERP-04-019"; }
		 */

		boolean releaseFT = false;
		if (icFgProgList != null) {
			if (icFgProgList.indexOf(",") < 0) {
				FtTestProgramTo tempTo = ftTestProgramDao.findByFtTestProgName(
						icFgProgList, ftMaterialNum);
				if (tempTo != null) {
					releaseFT = true;
				} else {
					releaseFT = false;
				}
			} else {
				releaseFT = true;
			}
		}

		// send mail
		UserDao userDao = new UserDao();

		String subject = PIDBContext.getConfig("MD-13") + "  " + ftMaterialNum
				+ " FT Test program not maintain";
		String text = "Please help take care this issue";
		String defaultEmail = userDao
				.fetchEmail(new String[] { "(R)default_MP_CPFT" });

		// 取得project_code中的release_to值
		ProjectCodeTo projectCode = new ProjectCodeDao()
				.findByProjectCode(icFgTo.getProjCode());

		if (pkgType.equalsIgnoreCase("304")) {
			// insert PIDB_IF_MATERIAL_MASTER
			ifMaterialMasterTo.setMaterialNum(icFgTo.getMaterialNum());
			ifMaterialMasterTo.setMaterialDesc(icFgTo.getPartNum());
			ifMaterialMasterTo.setMaterialType(materialType);
			ifMaterialMasterTo.setMaterialGroup(materialGroup);
			ifMaterialMasterTo.setAppCategory(icFgTo.getAppCategory());
			ifMaterialMasterTo.setProductFamily(prodFamily);
			ifMaterialMasterTo.setPkgType(icFgTo.getPkgType());
			ifMaterialMasterTo.setIcType(icType);
			ifMaterialMasterTo.setTimeStamp(new Date());
			ifMaterialMasterTo.setReleasedBy(userTo.getUserId());
			ifMaterialMasterTo.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
			// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
			ifMaterialMasterTo.setMpStatus(ERPHelper.judgeMP(icFgTo
					.getMaterialNum()));
			ifMaterialMasterTo.setReleaseTo(projectCode.getReleaseTo());
			
			//Basic Data , Purchase Order Text
			ifMaterialMasterTo.setPurchaseOrderText(icFgTo.getPartNum());
			ifMaterialMasterTo.setBasicDataText(icFgTo.getVendorDevice());
			
			ifMaterialMasterDao.insert(ifMaterialMasterTo,
					"PIDB_IF_MATERIAL_MASTER");

			// insert PIDB_IF_MATERIAL_CHARACTER
			Map<String, String> m = new HashMap<String, String>() {
				public String put(String key, String value) {
					if (StringUtils.isNotEmpty(value)) {
						super.put(key, value);
					}
					return null;
				}
			};

			if (mpFlag && StringUtils.isEmpty(icFgTo.getMcpPkg())) {
				// return "MCP Package is empty!";
				return "ERP-04-004";
			}
			if (mpFlag && StringUtils.isEmpty(icFgTo.getMcpDieQty())) {
				// return "MCP Die Quantity is empty!";
				return "ERP-04-005";
			}
			if (mpFlag && StringUtils.isEmpty(icFgTo.getMcpProd1())) {
				// return "MCP Product 1 is empty!";
				return "ERP-04-006";
			}
			if (mpFlag && StringUtils.isEmpty(icFgTo.getMcpProd2())) {
				// return "MCP Product 2 is empty!";
				return "ERP-04-007";
			}
			if (mpFlag && StringUtils.isEmpty(icFgTo.getMcpProd3())) {
				// return "MCP Product 3 is empty!";
				return "ERP-04-008";
			}
			if (mpFlag && StringUtils.isEmpty(icFgTo.getMcpProd4())) {
				// return "MCP Product 4 is empty!";
				return "ERP-04-009";
			}
			if (mpFlag && StringUtils.isEmpty(icFgTo.getPkgType())) {
				// return "Package Type is empty!";
				return "ERP-04-010";
			}

			String mcpPkg = icFgTo.getMcpPkg();
			if (mcpPkg.equalsIgnoreCase("1")) {
				mcpPkg = "Yes";
			} else if (mcpPkg.equalsIgnoreCase("0")) {
				mcpPkg = "No";
			}
			m.put("FG_01", mcpPkg);
			m.put("FG_02", icFgTo.getMcpDieQty());
			m.put("FG_03", icFgTo.getMcpProd1());
			m.put("FG_04", icFgTo.getMcpProd2());
			m.put("FG_05", icFgTo.getMcpProd3());
			m.put("FG_06", icFgTo.getMcpProd4());

			// Remove 2008/08/13
			/*
			 * List<TradPkgTo> tradPkgToList = tradPkgDao
			 * .findByProjNameAndPkgCode(projectTo.getProjName(), icFgTo
			 * .getPkgCode());
			 */

			List<TradPkgTo> tradPkgToList = tradPkgDao.findByPkgNameList(icFgTo
					.getPartNum());
		
			if( tradPkgToList.size()<=0 ){
				return "ERP-04-041";
			}
			
			if (mpFlag
					&& (CollectionUtils.isEmpty(tradPkgToList) || StringUtils
							.isEmpty(tradPkgToList.get(0).getTradPkgType()))) {
				return "ERP-04-041";
			}


			m.put("FG_07", tradPkgToList.get(0).getTradPkgType());
			
			// if (tradPkgToList != null
			// && tradPkgToList.size() > 0
			// && StringUtils.isNotEmpty(tradPkgToList.get(0)
			// .getTradPkgType())) {
			// m.put("FG_07", tradPkgToList.get(0).getTradPkgType());
			// } else {
			// return "ERP-04-041";
			// }

			// add FT
			if (mpFlag && StringUtils.isEmpty(projectTo.getProjName())) {
				// return "Project Name is empty!";
				return "ERP-04-011";
			}
			if (mpFlag && StringUtils.isEmpty(projectTo.getProjCode())) {
				// return "Project Code is empty!";
				return "ERP-04-012";
			}
			if (mpFlag && StringUtils.isEmpty(projectTo.getWaferInch())) {
				// return "Wafer Inch is empty!";
				return "ERP-04-013";
			}
			m.put("FG_08", projectTo.getProjName());
			m.put("FG_09", projectTo.getProjCode());
			m.put("FG_10", projectTo.getWaferInch());

			if (releaseFT) {
				List<FtTestProgramTo> ftList = ftTestProgramDao
						.findByFtTestProgNameList(icFgTo.getFtTestProgList());
				if (ftList != null && ftList.size() > 0) {
					String tester = "";
					double ftCpuTime = 0;
					double ftIndexTime = 0;
					String contactDieQty = "";
					String testerConfig = "";

					for (FtTestProgramTo ft : ftList) {

						if (mpFlag && StringUtils.isEmpty(ft.getTester())) {
							// return "FT Tester is empty!";
							return "ERP-04-014";
						}
						if (mpFlag && ft.getFtCpuTime() == null) {
							// return "FT CPU Time is empty!";
							return "ERP-04-015";
						}
						if (mpFlag && ft.getFtIndexTime() == null) {
							// return "FT Index Time is empty!";
							return "ERP-04-016";
						}
						if (mpFlag
								&& StringUtils.isEmpty(ft.getContactDieQty())) {
							// return "Contact Die Quantity is empty!";
							return "ERP-04-017";
						}
						if (mpFlag && StringUtils.isEmpty(ft.getTesterConfig())) {
							// return "Tester Config is empty!";
							return "ERP-04-018";
						}

						tester += "," + ft.getTester();
						ftCpuTime += ft.getFtCpuTime().doubleValue();
						ftIndexTime += ft.getFtIndexTime().doubleValue();
						contactDieQty += "," + ft.getContactDieQty();
						testerConfig += "," + ft.getTesterConfig();
					}

					if (ftList.size() > 1) {
						m.put("FG_12", "YES");
					} else {
						m.put("FG_12", "No");
					}
					m.put("FG_11", tester.substring(1));
					m.put("FG_13", ftCpuTime + "");
					m.put("FG_14", ftIndexTime + "");
					m.put("FG_16", contactDieQty.substring(1));
					m.put("FG_17", testerConfig.substring(1));
				} else if (mpFlag) {
					return "ERP-04-042";
				}

			} else {
				ProdStdTestRefDao prodStdTestRefDao = new ProdStdTestRefDao();
				ProdStdTestRefTo prodStdTestRefTo = prodStdTestRefDao
						.findByTestReferenceId(icFgTo.getFtTestProgList());
				if (prodStdTestRefTo != null) {

					if (mpFlag
							&& StringUtils.isEmpty(prodStdTestRefTo
									.getFtTesterE())) {
						return "ERP-04-014";
					}
					if (mpFlag
							&& StringUtils.isEmpty(prodStdTestRefTo
									.getFtCpuTimeE())) {
						return "ERP-04-015";
					}
					if (mpFlag
							&& StringUtils.isEmpty(prodStdTestRefTo
									.getFtIndexTimeE())) {
						return "ERP-04-016";
					}
					if (mpFlag
							&& StringUtils.isEmpty(prodStdTestRefTo
									.getFtContactDieQty())) {
						return "ERP-04-017";
					}
					if (mpFlag
							&& StringUtils.isEmpty(prodStdTestRefTo
									.getFtTesterSpecE())) {
						return "ERP-04-018";
					}

					m.put("FG_12", "No");
					m.put("FG_11", prodStdTestRefTo.getFtTesterE());
					m.put("FG_13", prodStdTestRefTo.getFtCpuTimeE());
					m.put("FG_14", prodStdTestRefTo.getFtIndexTimeE());
					m.put("FG_16", prodStdTestRefTo.getFtContactDieQty());
					m.put("FG_17", prodStdTestRefTo.getFtTesterSpecE());
				} else if (mpFlag) {
					if (className.indexOf("MpList") > 0) {
						SendMailDispatch.sendMailDefault(subject, text,
								defaultEmail);
					}
					// return "ERP-04-043";
				}
			}

			if (mpFlag && StringUtils.isEmpty(projectTo.getGrossDie())) {
				// return "Wafer Gross is empty!";
				return "ERP-04-020";
			}

			m.put("FG_15", projectTo.getGrossDie());

			classes.add(m);
		} else if (pkgType.equalsIgnoreCase("305")) {
			// insert PIDB_IF_MATERIAL_MASTER
			ifMaterialMasterTo.setMaterialNum(icFgTo.getMaterialNum());
			ifMaterialMasterTo.setMaterialDesc(icFgTo.getPartNum());
			ifMaterialMasterTo.setMaterialType(materialType);
			ifMaterialMasterTo.setMaterialGroup(materialGroup);
			ifMaterialMasterTo.setAppCategory(icFgTo.getAppCategory());
			ifMaterialMasterTo.setProductFamily(prodFamily);
			ifMaterialMasterTo.setPkgType(icFgTo.getPkgType());
			ifMaterialMasterTo.setIcType(icType);
			ifMaterialMasterTo.setTimeStamp(new Date());
			ifMaterialMasterTo.setReleasedBy(userTo.getUserId());
			ifMaterialMasterTo.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
			// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
			ifMaterialMasterTo.setMpStatus(ERPHelper.judgeMP(icFgTo
					.getMaterialNum()));
			ifMaterialMasterTo.setReleaseTo(projectCode.getReleaseTo());
			
			//Basic Data , Purchase Order Text
			ifMaterialMasterTo.setPurchaseOrderText(icFgTo.getPartNum());
			ifMaterialMasterTo.setBasicDataText(icFgTo.getVendorDevice());
			
			ifMaterialMasterDao.insert(ifMaterialMasterTo,
					"PIDB_IF_MATERIAL_MASTER");

			// insert PIDB_IF_MATERIAL_CHARACTER
			Map<String, String> m = new HashMap<String, String>() {
				public String put(String key, String value) {
					if (StringUtils.isNotEmpty(value)) {
						super.put(key, value);
					}
					return null;
				}
			};

			if (mpFlag && StringUtils.isEmpty(icFgTo.getMcpPkg())) {
				// return "MCP Package is empty!";
				//return "ERP-04-004";
			}
			if (mpFlag && StringUtils.isEmpty(icFgTo.getMcpDieQty())) {
				// return "MCP Die Quantity is empty!";
				//return "ERP-04-005";
			}
			if (mpFlag && StringUtils.isEmpty(icFgTo.getMcpProd1())) {
				// return "MCP Product 1 is empty!";
				//return "ERP-04-006";
			}
			if (mpFlag && StringUtils.isEmpty(icFgTo.getMcpProd2())) {
				// return "MCP Product 2 is empty!";
				//return "ERP-04-007";
			}
			if (mpFlag && StringUtils.isEmpty(icFgTo.getMcpProd3())) {
				// return "MCP Product 3 is empty!";
				//return "ERP-04-008";
			}
			if (mpFlag && StringUtils.isEmpty(icFgTo.getMcpProd4())) {
				// return "MCP Product 4 is empty!";
				//return "ERP-04-009";
			}
			if (mpFlag && StringUtils.isEmpty(icFgTo.getPkgType())) {
				// return "Package Type is empty!";
				//return "ERP-04-010";
			}

			String mcpPkg = icFgTo.getMcpPkg();
			if (mcpPkg.equalsIgnoreCase("1")) {
				mcpPkg = "Yes";
			} else if (mcpPkg.equalsIgnoreCase("0")) {
				mcpPkg = "No";
			}
			m.put("FG_01", mcpPkg);
			m.put("FG_02", icFgTo.getMcpDieQty());
			m.put("FG_03", icFgTo.getMcpProd1());
			m.put("FG_04", icFgTo.getMcpProd2());
			m.put("FG_05", icFgTo.getMcpProd3());
			m.put("FG_06", icFgTo.getMcpProd4());

			// Remove 2008/08/13
			/*
			 * List<TradPkgTo> tradPkgToList = tradPkgDao
			 * .findByProjNameAndPkgCode(projectTo.getProjName(), icFgTo
			 * .getPkgCode());
			 */

			List<TradPkgTo> tradPkgToList = tradPkgDao.findByPkgNameList(icFgTo
					.getPartNum());
		
			if( tradPkgToList.size()<=0 ){
				//return "ERP-04-041";
			}
			
			if (mpFlag
					&& (CollectionUtils.isEmpty(tradPkgToList) || StringUtils
							.isEmpty(tradPkgToList.get(0).getTradPkgType()))) {
				//return "ERP-04-041";
			}


			m.put("FG_07", "CSP");
			
			// if (tradPkgToList != null
			// && tradPkgToList.size() > 0
			// && StringUtils.isNotEmpty(tradPkgToList.get(0)
			// .getTradPkgType())) {
			// m.put("FG_07", tradPkgToList.get(0).getTradPkgType());
			// } else {
			// return "ERP-04-041";
			// }

			// add FT
			if (mpFlag && StringUtils.isEmpty(projectTo.getProjName())) {
				// return "Project Name is empty!";
				//return "ERP-04-011";
			}
			if (mpFlag && StringUtils.isEmpty(projectTo.getProjCode())) {
				// return "Project Code is empty!";
				//return "ERP-04-012";
			}
			if (mpFlag && StringUtils.isEmpty(projectTo.getWaferInch())) {
				// return "Wafer Inch is empty!";
				//return "ERP-04-013";
			}
			m.put("FG_08", projectTo.getProjName());
			m.put("FG_09", projectTo.getProjCode());
			m.put("FG_10", projectTo.getWaferInch());

			if (releaseFT) {
				List<FtTestProgramTo> ftList = ftTestProgramDao
						.findByFtTestProgNameList(icFgTo.getFtTestProgList());
				if (ftList != null && ftList.size() > 0) {
					String tester = "";
					double ftCpuTime = 0;
					double ftIndexTime = 0;
					String contactDieQty = "";
					String testerConfig = "";

					for (FtTestProgramTo ft : ftList) {

						if (mpFlag && StringUtils.isEmpty(ft.getTester())) {
							// return "FT Tester is empty!";
							//return "ERP-04-014";
						}
						if (mpFlag && ft.getFtCpuTime() == null) {
							// return "FT CPU Time is empty!";
							//return "ERP-04-015";
						}
						if (mpFlag && ft.getFtIndexTime() == null) {
							// return "FT Index Time is empty!";
							//return "ERP-04-016";
						}
						if (mpFlag
								&& StringUtils.isEmpty(ft.getContactDieQty())) {
							// return "Contact Die Quantity is empty!";
							//return "ERP-04-017";
						}
						if (mpFlag && StringUtils.isEmpty(ft.getTesterConfig())) {
							// return "Tester Config is empty!";
							//return "ERP-04-018";
						}

						tester += "," + ft.getTester();
						ftCpuTime += ft.getFtCpuTime().doubleValue();
						ftIndexTime += ft.getFtIndexTime().doubleValue();
						contactDieQty += "," + ft.getContactDieQty();
						testerConfig += "," + ft.getTesterConfig();
					}

					if (ftList.size() > 1) {
						m.put("FG_12", "YES");
					} else {
						m.put("FG_12", "No");
					}
					m.put("FG_11", "CSP");
					m.put("FG_13", ftCpuTime + "");
					m.put("FG_14", ftIndexTime + "");
					m.put("FG_16", contactDieQty.substring(1));
					m.put("FG_17", testerConfig.substring(1));
				} else if (mpFlag) {
					//return "ERP-04-042";
				}

			} else {
				ProdStdTestRefDao prodStdTestRefDao = new ProdStdTestRefDao();
				ProdStdTestRefTo prodStdTestRefTo = prodStdTestRefDao
						.findByTestReferenceId(icFgTo.getFtTestProgList());
				if (prodStdTestRefTo != null) {

					if (mpFlag
							&& StringUtils.isEmpty(prodStdTestRefTo
									.getFtTesterE())) {
						//return "ERP-04-014";
					}
					if (mpFlag
							&& StringUtils.isEmpty(prodStdTestRefTo
									.getFtCpuTimeE())) {
						//return "ERP-04-015";
					}
					if (mpFlag
							&& StringUtils.isEmpty(prodStdTestRefTo
									.getFtIndexTimeE())) {
						//return "ERP-04-016";
					}
					if (mpFlag
							&& StringUtils.isEmpty(prodStdTestRefTo
									.getFtContactDieQty())) {
						//return "ERP-04-017";
					}
					if (mpFlag
							&& StringUtils.isEmpty(prodStdTestRefTo
									.getFtTesterSpecE())) {
						//return "ERP-04-018";
					}

					m.put("FG_12", "No");
					m.put("FG_11", prodStdTestRefTo.getFtTesterE());
					m.put("FG_13", prodStdTestRefTo.getFtCpuTimeE());
					m.put("FG_14", prodStdTestRefTo.getFtIndexTimeE());
					m.put("FG_16", prodStdTestRefTo.getFtContactDieQty());
					m.put("FG_17", prodStdTestRefTo.getFtTesterSpecE());
				} else if (mpFlag) {
					if (className.indexOf("MpList") > 0) {
						SendMailDispatch.sendMailDefault(subject, text,
								defaultEmail);
					}
					// return "ERP-04-043";
				}
			}

			if (mpFlag && StringUtils.isEmpty(projectTo.getGrossDie())) {
				// return "Wafer Gross is empty!";
				//return "ERP-04-020";
			}

			m.put("FG_15", projectTo.getGrossDie());

			classes.add(m);
		}else if(pkgType.equalsIgnoreCase("312")) { // WLO
				// insert PIDB_IF_MATERIAL_MASTER
				materialType = "ZOFG";
				ifMaterialMasterTo.setMaterialNum(icFgTo.getMaterialNum());
				WloDao wloDao = new WloDao();
				WloTo  wloTo = wloDao.findByPrimaryKey(icFgTo.getMaterialNum());
				if( wloTo == null ){
					return "ERP-15-002";
				}
				if (materialType == null || materialType.length() <= 0 || materialType.equals("") ) {
					return "ERP-02-022";
				}
				if (wloTo.getDescription() != null && !wloTo.getDescription().equals("")) {
					ifMaterialMasterTo.setMaterialDesc(wloTo.getDescription());
				} else {
					ifMaterialMasterTo.setMaterialDesc(wloTo.getPartNum());
				}
				ifMaterialMasterTo.setMaterialType(materialType);

				ifMaterialMasterTo.setProductFamily(prodFamily);
				ifMaterialMasterTo.setPkgType("312");
				ifMaterialMasterTo.setIcType(icType);
				
				materialGroup = "18070";
				
				ifMaterialMasterTo.setMaterialGroup(materialGroup);
				ifMaterialMasterTo.setAppCategory(icFgTo.getAppCategory());			
				ifMaterialMasterTo.setTimeStamp(new Date());
				ifMaterialMasterTo.setReleasedBy(userTo.getUserId());
				
				ifMaterialMasterTo.setId(SequenceSupport
						.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
				// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
				ifMaterialMasterTo.setMpStatus(ERPHelper.judgeMP(icFgTo
						.getMaterialNum()));
				ifMaterialMasterTo.setReleaseTo(projectCode.getReleaseTo());
				
				//Basic Data , Purchase Order Text
				ifMaterialMasterTo.setPurchaseOrderText(icFgTo.getPartNum());
				ifMaterialMasterTo.setBasicDataText(icFgTo.getVendorDevice());
				
				ifMaterialMasterDao.insert(ifMaterialMasterTo,
						"PIDB_IF_MATERIAL_MASTER");
		}else if(pkgType.equalsIgnoreCase("313")) { // WLM
			
			// insert PIDB_IF_MATERIAL_MASTER
			materialType = "";
			ifMaterialMasterTo.setMaterialNum(icFgTo.getMaterialNum());
			WlmDao wlmDao = new WlmDao();
			WlmTo  wlmTo = wlmDao.findByPrimaryKey(icFgTo.getMaterialNum());
			if( wlmTo == null ){
				return "ERP-15-001";
			}
			if( wlmTo.getPackingType().equals("T")){
				materialType = "ZTFG";
			}else{//W,0
				materialType = "ZAFG";
			}
			if (materialType == null || materialType.length() <= 0 || materialType.equals("") ) {
				return "ERP-02-022";
			}
			
			if (wlmTo.getDescription() != null && !wlmTo.getDescription().equals("")) {
				ifMaterialMasterTo.setMaterialDesc(wlmTo.getDescription());
			} else {
				ifMaterialMasterTo.setMaterialDesc(wlmTo.getPartNum());
			}
			ifMaterialMasterTo.setMaterialType(materialType);

			ifMaterialMasterTo.setReleaseTo("HX");
			ifMaterialMasterTo.setProductFamily(prodFamily);
			ifMaterialMasterTo.setPkgType("313");
			ifMaterialMasterTo.setIcType(icType);
			materialGroup = "18080";
			
			ifMaterialMasterTo.setMaterialGroup(materialGroup);
			ifMaterialMasterTo.setAppCategory(icFgTo.getAppCategory());			
			ifMaterialMasterTo.setTimeStamp(new Date());
			ifMaterialMasterTo.setReleasedBy(userTo.getUserId());
			
			ifMaterialMasterTo.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
			// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
			ifMaterialMasterTo.setMpStatus(ERPHelper.judgeMP(icFgTo
					.getMaterialNum()));
			ifMaterialMasterTo.setReleaseTo(projectCode.getReleaseTo());
			
			//Basic Data , Purchase Order Text
			ifMaterialMasterTo.setPurchaseOrderText(icFgTo.getPartNum());
			ifMaterialMasterTo.setBasicDataText(icFgTo.getVendorDevice());
			
			ifMaterialMasterDao.insert(ifMaterialMasterTo,
					"PIDB_IF_MATERIAL_MASTER");
		
		}else if(pkgType.equalsIgnoreCase("314")) {//TSV
			// insert PIDB_IF_MATERIAL_MASTER
			materialType = "ZFG2";
			ifMaterialMasterTo.setMaterialNum(icFgTo.getMaterialNum());
			ifMaterialMasterTo.setMaterialDesc(icFgTo.getPartNum());
			ifMaterialMasterTo.setMaterialType(materialType);
			ifMaterialMasterTo.setMaterialGroup(materialGroup);
			ifMaterialMasterTo.setAppCategory(icFgTo.getAppCategory());
			ifMaterialMasterTo.setProductFamily(prodFamily);
			ifMaterialMasterTo.setPkgType(icFgTo.getPkgType());
			ifMaterialMasterTo.setIcType(icType);
			ifMaterialMasterTo.setTimeStamp(new Date());
			ifMaterialMasterTo.setReleasedBy(userTo.getUserId());
			ifMaterialMasterTo.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
			// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
			ifMaterialMasterTo.setMpStatus(ERPHelper.judgeMP(icFgTo
					.getMaterialNum()));
			ifMaterialMasterTo.setReleaseTo(projectCode.getReleaseTo());
			ifMaterialMasterDao.insert(ifMaterialMasterTo,
					"PIDB_IF_MATERIAL_MASTER");
		}else if(Integer.parseInt(pkgType) > 305) {
			// insert PIDB_IF_MATERIAL_MASTER
			ifMaterialMasterTo.setMaterialNum(icFgTo.getMaterialNum());
			ifMaterialMasterTo.setMaterialDesc(icFgTo.getPartNum());
			ifMaterialMasterTo.setMaterialType(materialType);
			ifMaterialMasterTo.setMaterialGroup(materialGroup);
			ifMaterialMasterTo.setAppCategory(icFgTo.getAppCategory());
			ifMaterialMasterTo.setProductFamily(prodFamily);
			ifMaterialMasterTo.setPkgType(icFgTo.getPkgType());
			ifMaterialMasterTo.setIcType(icType);
			ifMaterialMasterTo.setTimeStamp(new Date());
			ifMaterialMasterTo.setReleasedBy(userTo.getUserId());
			ifMaterialMasterTo.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
			// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
			ifMaterialMasterTo.setMpStatus(ERPHelper.judgeMP(icFgTo
					.getMaterialNum()));
			ifMaterialMasterTo.setReleaseTo(projectCode.getReleaseTo());
			
			//Basic Data , Purchase Order Text
			ifMaterialMasterTo.setPurchaseOrderText(icFgTo.getPartNum());
			ifMaterialMasterTo.setBasicDataText(icFgTo.getVendorDevice());
			
			ifMaterialMasterDao.insert(ifMaterialMasterTo,
					"PIDB_IF_MATERIAL_MASTER");
		} else {
			// insert PIDB_IF_MATERIAL_MASTER
			ifMaterialMasterTo.setMaterialNum(icFgTo.getMaterialNum());
			ifMaterialMasterTo.setMaterialDesc(icFgTo.getPartNum());
			ifMaterialMasterTo.setMaterialType(materialType);
			ifMaterialMasterTo.setMaterialGroup(materialGroup);
			ifMaterialMasterTo.setAppCategory(icFgTo.getAppCategory());
			ifMaterialMasterTo.setProductFamily(prodFamily);
			ifMaterialMasterTo.setPkgType(icFgTo.getPkgType());
			ifMaterialMasterTo.setIcType(icType);
			ifMaterialMasterTo.setTimeStamp(new Date());
			ifMaterialMasterTo.setReleasedBy(userTo.getUserId());
			ifMaterialMasterTo.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
			// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
			ifMaterialMasterTo.setMpStatus(ERPHelper.judgeMP(icFgTo
					.getMaterialNum()));
			ifMaterialMasterTo.setReleaseTo(projectCode.getReleaseTo());
			
			//Basic Data , Purchase Order Text
			ifMaterialMasterTo.setPurchaseOrderText(icFgTo.getPartNum());
			ifMaterialMasterTo.setBasicDataText(icFgTo.getVendorDevice());
			
			ifMaterialMasterDao.insert(ifMaterialMasterTo,
					"PIDB_IF_MATERIAL_MASTER");

			Map<String, String> m = new HashMap<String, String>() {
				public String put(String key, String value) {
					if (StringUtils.isNotEmpty(value)) {
						super.put(key, value);
					}
					return null;
				}
			};

			if (mpFlag && StringUtils.isEmpty(projectTo.getProjName())) {
				// return "Project Name is empty!";
				return "ERP-04-011";
			}
			if (mpFlag && StringUtils.isEmpty(projectTo.getProjCode())) {
				// return "Project Code is empty!";
				return "ERP-04-012";
			}
			if (mpFlag && StringUtils.isEmpty(projectTo.getWaferInch())) {
				// return "Wafer Inch is empty!";
				return "ERP-04-013";
			}
			m.put("FT_01", projectTo.getProjName());
			m.put("FT_02", projectTo.getProjCode());
			m.put("FT_03", projectTo.getWaferInch());

			if (!pkgType.equalsIgnoreCase("303")
					&& !pkgType.equalsIgnoreCase("305")) {
				if (releaseFT) {
					List<FtTestProgramTo> ftList = ftTestProgramDao
							.findByFtTestProgNameList(icFgTo
									.getFtTestProgList());
					if (ftList != null && ftList.size() > 0) {
						String tester = "";
						double ftCpuTime = 0;
						double ftIndexTime = 0;
						String contactDieQty = "";
						String testerConfig = "";

						for (FtTestProgramTo ft : ftList) {

							if (mpFlag && StringUtils.isEmpty(ft.getTester())) {
								// return "FT Tester is empty!";
								return "ERP-04-014";
							}
							if (mpFlag && ft.getFtCpuTime() == null) {
								// return "FT CPU Time is empty!";
								return "ERP-04-015";
							}
							if (mpFlag && ft.getFtIndexTime() == null) {
								// return "FT Index Time is empty!";
								return "ERP-04-016";
							}
							if (mpFlag
									&& StringUtils.isEmpty(ft
											.getContactDieQty())) {
								// return "Contact Die Quantity is empty!";
								return "ERP-04-017";
							}
							if (mpFlag
									&& StringUtils
											.isEmpty(ft.getTesterConfig())) {
								// return "Tester Config is empty!";
								return "ERP-04-018";
							}

							tester += "," + ft.getTester();
							ftCpuTime += ft.getFtCpuTime().doubleValue();
							ftIndexTime += ft.getFtIndexTime().doubleValue();
							contactDieQty += "," + ft.getContactDieQty();
							testerConfig += "," + ft.getTesterConfig();
						}

						if (ftList.size() > 1) {
							m.put("FT_05", "YES");
						} else {
							m.put("FT_05", "No");
						}
						m.put("FT_04", tester.substring(1));
						m.put("FT_06", ftCpuTime + "");
						m.put("FT_07", ftIndexTime + "");
						m.put("FT_09", contactDieQty.substring(1));
						m.put("FT_10", testerConfig.substring(1));
					} else if (mpFlag) {
						return "ERP-04-042";
					}

				} else {
					ProdStdTestRefDao prodStdTestRefDao = new ProdStdTestRefDao();
					ProdStdTestRefTo prodStdTestRefTo = prodStdTestRefDao
							.findByTestReferenceId(icFgTo.getFtTestProgList());
					if (prodStdTestRefTo != null) {

						if (mpFlag
								&& StringUtils.isEmpty(prodStdTestRefTo
										.getFtTesterE())) {
							return "ERP-04-014";
						}
						if (mpFlag
								&& StringUtils.isEmpty(prodStdTestRefTo
										.getFtCpuTimeE())) {
							return "ERP-04-015";
						}
						if (mpFlag
								&& StringUtils.isEmpty(prodStdTestRefTo
										.getFtIndexTimeE())) {
							return "ERP-04-016";
						}
						if (mpFlag
								&& StringUtils.isEmpty(prodStdTestRefTo
										.getFtContactDieQty())) {
							return "ERP-04-017";
						}
						if (mpFlag
								&& StringUtils.isEmpty(prodStdTestRefTo
										.getFtTesterSpecE())) {
							return "ERP-04-018";
						}

						m.put("FT_05", "No");
						m.put("FT_04", prodStdTestRefTo.getFtTesterE());
						m.put("FT_06", prodStdTestRefTo.getFtCpuTimeE());
						m.put("FT_07", prodStdTestRefTo.getFtIndexTimeE());
						m.put("FT_09", prodStdTestRefTo.getFtContactDieQty());
						m.put("FT_10", prodStdTestRefTo.getFtTesterSpecE());
					} else if (mpFlag) {
						if (className.indexOf("MpList") > 0) {
							SendMailDispatch.sendMailDefault(subject, text,
									defaultEmail);
						}
						// return "ERP-04-043";
					}
				}
			}

			if (mpFlag && StringUtils.isEmpty(projectTo.getGrossDie())) {
				// return "Wafer Gross is empty!";
				return "ERP-04-020";
			}
			if (mpFlag && StringUtils.isEmpty(icFgTo.getPkgType())) {
				// return "Package Type is empty!";
				return "ERP-04-021";
			}

			// Added on 3/9
			SapMasterPackageTypeTo sapMasterPackageTypeTo = sapMasterPackageTypeDao
					.findByPkgType(icFgTo.getPkgType());

			m.put("FT_08", projectTo.getGrossDie());
			// m.put("FT_11", icFgTo.getPkgType());
			// Added on 3/9
			m.put("FT_11", sapMasterPackageTypeTo.getDescription());

			classes.add(m);

		}

		for (Map<String, String> m : classes) {
			for (String s : m.keySet()) {
				IfMaterialCharacterTo ifMaterialCharacterTo = new IfMaterialCharacterTo();
				ifMaterialCharacterTo.setMaterialNum(icFgTo.getMaterialNum());
				ifMaterialCharacterTo.setMaterialType(materialType);
				ifMaterialCharacterTo.setChTechName(s);
				ifMaterialCharacterTo.setChValue(m.get(s));
				ifMaterialCharacterTo.setId(SequenceSupport
						.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
				ifMaterialCharacterTo.setTimeStamp(new Date());
				ifMaterialCharacterTo.setReleasedBy(userTo.getUserId());
				ifMaterialCharacterTo.setReleaseTo(projectCode.getReleaseTo());
				charList.add(ifMaterialCharacterTo);
			}
		}
		ifMaterialCharacterDao.batchInsert(charList,
				"PIDB_IF_MATERIAL_CHARACTER");

		// Part II
		// insert PIDB_IF_MATERIAL_MASTER
		classes = new ArrayList<Map<String, String>>();
		charList = new ArrayList<IfMaterialCharacterTo>();
		ifMaterialMasterTo = new IfMaterialMasterTo();
		md = "IcFg";
		materialType = "";
		materialGroup = "";

		if (icFgTo.getRoutingAs()
				&& (pkgType.equalsIgnoreCase("304")
						|| pkgType.equalsIgnoreCase("305") || !pkgType
						.equalsIgnoreCase("303"))) {

			if (icFgTo.getRoutingAs()
					&& (pkgType.equalsIgnoreCase("304") || pkgType
							.equalsIgnoreCase("305"))) {
				// insert ZAS2 to PIDB_IF_MATERIAL_MASTER ELSE
				md += ".4";

			} else if (icFgTo.getRoutingAs()
					&& !pkgType.equalsIgnoreCase("303")
					&& !pkgType.equalsIgnoreCase("305")) {
				// insert ZAS to PIDB_IF_MATERIAL_MASTER ELSE
				md += ".3";
			}

			materialType = ERPHelper.getMaterialType(md);
			materialGroup = ERPHelper
					.getMaterialGroup(materialType, prodFamily);
			ifMaterialMasterTo.setMaterialNum("A"
					+ (icFgTo.getMaterialNum().substring(1)));
			ifMaterialMasterTo.setMaterialDesc(icFgTo.getPartNum());
			ifMaterialMasterTo.setMaterialType(materialType);
			ifMaterialMasterTo.setMaterialGroup(materialGroup);
			ifMaterialMasterTo.setTimeStamp(new Date());
			ifMaterialMasterTo.setReleasedBy(userTo.getUserId());
			ifMaterialMasterTo.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
			// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
			ifMaterialMasterTo.setMpStatus(ERPHelper.judgeMP(icFgTo
					.getMaterialNum()));
			ifMaterialMasterTo.setReleaseTo(projectCode.getReleaseTo());
			ifMaterialMasterDao.insert(ifMaterialMasterTo,
					"PIDB_IF_MATERIAL_MASTER");
		}

		// insert PIDB_IF_MATERIAL_CHARACTER
		// classes = new ArrayList<Map<String, String>>();
		md = "IcFg";
		materialType = "";
		materialGroup = "";

		if (icFgTo.getRoutingAs() && pkgType.equalsIgnoreCase("304")) {

			// Remove 2008/08/13 Hank
			// List<TradPkgTo> pkgList =
			// tradPkgDao.findByProjNameAndPkgCode(projectTo.getProjName(),
			// icFgTo.getPkgCode());
			List<TradPkgTo> pkgList = tradPkgDao.findByPkgNameList(icFgTo
					.getPartNum());
			md += ".4";

			TradPkgTo pkg = new TradPkgTo();
			if (pkgList != null && pkgList.size() > 0) {
				pkg = pkgList.get(0);
			} else {
				return "ERP-04-044";
			}

			Map<String, String> m = new HashMap<String, String>() {
				public String put(String key, String value) {
					if (StringUtils.isNotEmpty(value)) {
						super.put(key, value);
					}
					return null;
				}
			};

			if (mpFlag && StringUtils.isEmpty(pkg.getTradPkgType())) {
				// return "Traditional Package Type is empty!";
				return "ERP-04-022";
			}
			if (mpFlag && StringUtils.isEmpty(pkg.getPinCount())) {
				// return "Pin Count is empty!";
				return "ERP-04-023";
			}
			if (mpFlag && StringUtils.isEmpty(pkg.getLeadFrameType())) {
				// return "Lead Frame Type is empty!";
				return "ERP-04-024";
			}
			if (mpFlag && StringUtils.isEmpty(pkg.getSubtractLayer())) {
				// return "Subtract Layer is empty!";
				return "ERP-04-025";
			}
			if (mpFlag && StringUtils.isEmpty(pkg.getBodySize())) {
				// return "Body Size is empty!";
				return "ERP-04-026";
			}
			if (mpFlag && StringUtils.isEmpty(pkg.getGoldenWireWidth())) {
				// return "Golden Wire Width is empty!";
				return "ERP-04-027";
			}
			if (mpFlag && StringUtils.isEmpty(pkg.getMcpPkg())) {
				// return "MCP Package is empty!";
				return "ERP-04-028";
			}
			if (mpFlag && StringUtils.isEmpty(pkg.getMcpDieQty())) {
				// return "MCP Die Quantity is empty!";
				return "ERP-04-029";
			}

			m.put("AS_01", pkg.getTradPkgType());
			m.put("AS_02", pkg.getPinCount());
			m.put("AS_03", pkg.getLeadFrameType());
			m.put("AS_04", pkg.getSubtractLayer());
			m.put("AS_05", pkg.getBodySize());
			m.put("AS_06", pkg.getGoldenWireWidth());
			m
					.put("AS_07", pkg.getMcpPkg().equalsIgnoreCase("Y") ? "Yes"
							: "No");
			m.put("AS_08", pkg.getMcpDieQty());

			classes.add(m);

		} else if (icFgTo.getRoutingAs() && !pkgType.equalsIgnoreCase("303")
				&& !pkgType.equalsIgnoreCase("305")) {
			md += ".3";

			Map<String, String> m = new HashMap<String, String>() {
				public String put(String key, String value) {
					if (StringUtils.isNotEmpty(value)) {
						super.put(key, value);
					}
					return null;
				}
			};

			if (mpFlag && StringUtils.isEmpty(pkgType)) {
				// return "Package Type is empty!";
				return "ERP-04-030";
			}
			if (mpFlag && StringUtils.isEmpty(projectTo.getPitch())) {
				// return "Pitch (UM) is empty!";
				return "ERP-04-031";
			}

			// Added on 3/9
			SapMasterPackageTypeTo sapMasterPackageTypeTo = sapMasterPackageTypeDao
					.findByPkgType(pkgType);

			// m.put("IL_01", pkgType);
			// Added on 3/9
			//logger.debug("IL_01");
			if( sapMasterPackageTypeTo != null  ){
				String iLPkgType = sapMasterPackageTypeTo.getDescription();
				if(projectTo.getProdFamily().equals("101")){//表Source
					iLPkgType = iLPkgType + "-SD";
				}else if(projectTo.getProdFamily().equals("102") ){//表Gate
					iLPkgType = iLPkgType + "-GD";
				}
				m.put("IL_01", iLPkgType);
				//logger.debug("IL_01 "  + iLPkgType);
			}else{
				m.put("IL_01",
						sapMasterPackageTypeTo != null ? sapMasterPackageTypeTo
								.getDescription() : "");
				//logger.debug("IL_01 "  + "NA");
			}
			m.put("IL_02", projectTo.getPitch());

			classes.add(m);
		}

		materialType = ERPHelper.getMaterialType(md);
		materialGroup = ERPHelper.getMaterialGroup(materialType, prodFamily);

		for (Map<String, String> m : classes) {
			for (String s : m.keySet()) {
				IfMaterialCharacterTo ifMaterialCharacterTo = new IfMaterialCharacterTo();
				ifMaterialCharacterTo.setMaterialNum("A"
						+ (icFgTo.getMaterialNum().substring(1)));
				ifMaterialCharacterTo.setMaterialType(materialType);
				ifMaterialCharacterTo.setChTechName(s);
				ifMaterialCharacterTo.setChValue(m.get(s));
				ifMaterialCharacterTo.setId(SequenceSupport
						.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
				ifMaterialCharacterTo.setTimeStamp(new Date());
				ifMaterialCharacterTo.setReleasedBy(userTo.getUserId());
				ifMaterialCharacterTo.setReleaseTo(projectCode.getReleaseTo());
				charList.add(ifMaterialCharacterTo);
			}
		}
		ifMaterialCharacterDao.batchInsert(charList,
				"PIDB_IF_MATERIAL_CHARACTER");

		return null;

	}

	public static String releaseForProject(final Object o, final UserTo userTo) {
		IcFgTo icFgTo = (IcFgTo) o;
		// Patched by matrix to avoid empty appliation category, 20070802
		if (StringUtils.isEmpty(icFgTo.getAppCategory())) {
			return "ERP-04-047";
		} // End.
		IfMaterialMasterDao ifMaterialMasterDao = new IfMaterialMasterDao();
		IfMaterialCharacterDao ifMaterialCharacterDao = new IfMaterialCharacterDao();
		ProjectDao projectDao = new ProjectDao();
		FtTestProgramDao ftTestProgramDao = new FtTestProgramDao();
		TradPkgDao tradPkgDao = new TradPkgDao();
		CpMaterialDao cpMaterialDao = new CpMaterialDao();
		IfMaterialMasterTo ifMaterialMasterTo = new IfMaterialMasterTo();
		IcWaferDao icWaferDao = new IcWaferDao();
		// Added on 3/9
		SapMasterPackageTypeDao sapMasterPackageTypeDao = new SapMasterPackageTypeDao();

		boolean mpFlag = icFgTo.getMpStatus() != null
				&& icFgTo.getMpStatus().equalsIgnoreCase("Non-MP") ? false
				: true;

		// Part I
		// insert PIDB_IF_MATERIAL_MASTER
		String pkgType = icFgTo.getPkgType();
		String md = "IcFg";
		if (pkgType != null) {
			if (pkgType.equalsIgnoreCase("304")
					|| pkgType.equalsIgnoreCase("305")) {
				md += ".2";
			} else {
				md += ".1";
			}
		} else {
			// return "Package Type is error!";
			return "ERP-04-001";
		}

		String materialType = ERPHelper.getMaterialType(md);
		ProjectTo projectTo = projectDao
				.findByProjectCode(icFgTo.getProjCode());
		if (projectTo == null) {
			// return "Project is not exist!";
			return "ERP-04-002";
		}
		String prodFamily = projectTo.getProdFamily();
		String icType = projectTo.getIcType();
		if (StringUtils.isEmpty(prodFamily)) {
			// return "Product family is empty!";
			return "ERP-04-003";
		}
		if (StringUtils.isEmpty(icType)) {
			return "ERP-04-045";
		}
		String materialGroup = ERPHelper.getMaterialGroup(materialType,
				prodFamily);

		List<Map<String, String>> classes = new ArrayList<Map<String, String>>();
		List<IfMaterialCharacterTo> charList = new ArrayList<IfMaterialCharacterTo>();

		String icFgProgList = icFgTo.getFtTestProgList();
		String ftMaterialNum = icFgTo.getMaterialNum();
		// if (mpFlag && StringUtils.isEmpty(icFgProgList)) {
		// return "ERP-04-019";
		// }

		if (icFgTo.getMpStatus().equalsIgnoreCase("MP")
				&& !pkgType.equalsIgnoreCase("303")
				&& !pkgType.equalsIgnoreCase("305")
				&& StringUtils.isEmpty(icFgProgList)) {
			// return "ERP-04-019";
		}

		boolean releaseFT = false;
		if (icFgProgList != null) {
			if (icFgProgList.indexOf(",") < 0) {
				FtTestProgramTo tempTo = ftTestProgramDao.findByFtTestProgName(
						icFgProgList, ftMaterialNum);
				if (tempTo != null) {
					releaseFT = true;
				} else {
					releaseFT = false;
				}
			} else {
				releaseFT = true;
			}
		}

		// 取得project_Code中的release_to值
		ProjectCodeTo projectCode = new ProjectCodeDao()
				.findByProjectCode(icFgTo.getProjCode());

		if (pkgType.equalsIgnoreCase("304")) {
			// insert PIDB_IF_MATERIAL_MASTER
			ifMaterialMasterTo.setMaterialNum(icFgTo.getMaterialNum());
			ifMaterialMasterTo.setMaterialDesc(icFgTo.getPartNum());
			ifMaterialMasterTo.setMaterialType(materialType);
			ifMaterialMasterTo.setMaterialGroup(materialGroup);
			ifMaterialMasterTo.setAppCategory(icFgTo.getAppCategory());
			ifMaterialMasterTo.setProductFamily(prodFamily);
			ifMaterialMasterTo.setPkgType(icFgTo.getPkgType());
			ifMaterialMasterTo.setIcType(icType);
			ifMaterialMasterTo.setTimeStamp(new Date());
			ifMaterialMasterTo.setReleasedBy(userTo.getUserId());
			ifMaterialMasterTo.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
			// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
			ifMaterialMasterTo.setMpStatus(ERPHelper.judgeMP(icFgTo
					.getMaterialNum()));
			ifMaterialMasterTo.setReleaseTo(projectCode.getReleaseTo());
			
			//Basic Data , Purchase Order Text
			ifMaterialMasterTo.setPurchaseOrderText(icFgTo.getPartNum());
			ifMaterialMasterTo.setBasicDataText(icFgTo.getVendorDevice());
			
			ifMaterialMasterDao.insert(ifMaterialMasterTo,
					"PIDB_IF_MATERIAL_MASTER");

			// insert PIDB_IF_MATERIAL_CHARACTER
			Map<String, String> m = new HashMap<String, String>() {
				public String put(String key, String value) {
					if (StringUtils.isNotEmpty(value)) {
						super.put(key, value);
					}
					return null;
				}
			};

			if (mpFlag && StringUtils.isEmpty(icFgTo.getMcpPkg())) {
				// return "MCP Package is empty!";
				return "ERP-04-004";
			}
			if (mpFlag && StringUtils.isEmpty(icFgTo.getMcpDieQty())) {
				// return "MCP Die Quantity is empty!";
				return "ERP-04-005";
			}
			if (mpFlag && StringUtils.isEmpty(icFgTo.getMcpProd1())) {
				// return "MCP Product 1 is empty!";
				return "ERP-04-006";
			}
			if (mpFlag && StringUtils.isEmpty(icFgTo.getMcpProd2())) {
				// return "MCP Product 2 is empty!";
				return "ERP-04-007";
			}
			if (mpFlag && StringUtils.isEmpty(icFgTo.getMcpProd3())) {
				// return "MCP Product 3 is empty!";
				return "ERP-04-008";
			}
			if (mpFlag && StringUtils.isEmpty(icFgTo.getMcpProd4())) {
				// return "MCP Product 4 is empty!";
				return "ERP-04-009";
			}
			if (mpFlag && StringUtils.isEmpty(icFgTo.getPkgType())) {
				// return "Package Type is empty!";
				return "ERP-04-010";
			}

			String mcpPkg = icFgTo.getMcpPkg();
			if (mcpPkg.equalsIgnoreCase("1")) {
				mcpPkg = "Yes";
			} else if (mcpPkg.equalsIgnoreCase("0")) {
				mcpPkg = "No";
			}
			m.put("FG_01", mcpPkg);
			m.put("FG_02", icFgTo.getMcpDieQty());
			m.put("FG_03", icFgTo.getMcpProd1());
			m.put("FG_04", icFgTo.getMcpProd2());
			m.put("FG_05", icFgTo.getMcpProd3());
			m.put("FG_06", icFgTo.getMcpProd4());

			List<TradPkgTo> tradPkgToList = tradPkgDao
					.findByProjNameAndPkgCode(projectTo.getProjName(), icFgTo
							.getPkgCode());
			tradPkgToList = null!=tradPkgToList?tradPkgToList:new ArrayList<TradPkgTo>();
			
			if (mpFlag
					&& (CollectionUtils.isEmpty(tradPkgToList) || StringUtils
							.isEmpty(tradPkgToList.get(0).getTradPkgType()))) {
				return "ERP-04-041";
			}

			m.put("FG_07", tradPkgToList.get(0).getTradPkgType());
			// if (tradPkgToList != null
			// && tradPkgToList.size() > 0
			// && StringUtils.isNotEmpty(tradPkgToList.get(0)
			// .getTradPkgType())) {
			// m.put("FG_07", tradPkgToList.get(0).getTradPkgType());
			// } else {
			// return "ERP-04-041";
			// }

			// add FT
			if (mpFlag && StringUtils.isEmpty(projectTo.getProjName())) {
				// return "Project Name is empty!";
				return "ERP-04-011";
			}
			if (mpFlag && StringUtils.isEmpty(projectTo.getProjCode())) {
				// return "Project Code is empty!";
				return "ERP-04-012";
			}
			if (mpFlag && StringUtils.isEmpty(projectTo.getWaferInch())) {
				// return "Wafer Inch is empty!";
				return "ERP-04-013";
			}
			m.put("FG_08", projectTo.getProjName());
			m.put("FG_09", projectTo.getProjCode());
			m.put("FG_10", projectTo.getWaferInch());

			if (releaseFT) {
				List<FtTestProgramTo> ftList = ftTestProgramDao
						.findByFtTestProgNameList(icFgTo.getFtTestProgList());
				if (ftList != null && ftList.size() > 0) {
					String tester = "";
					double ftCpuTime = 0;
					double ftIndexTime = 0;
					String contactDieQty = "";
					String testerConfig = "";

					for (FtTestProgramTo ft : ftList) {

						if (mpFlag && StringUtils.isEmpty(ft.getTester())) {
							// return "FT Tester is empty!";
							// return "ERP-04-014";
						}
						if (mpFlag && ft.getFtCpuTime() == null) {
							// return "FT CPU Time is empty!";
							// return "ERP-04-015";
						}
						if (mpFlag && ft.getFtIndexTime() == null) {
							// return "FT Index Time is empty!";
							// return "ERP-04-016";
						}
						if (mpFlag
								&& StringUtils.isEmpty(ft.getContactDieQty())) {
							// return "Contact Die Quantity is empty!";
							// return "ERP-04-017";
						}
						if (mpFlag && StringUtils.isEmpty(ft.getTesterConfig())) {
							// return "Tester Config is empty!";
							// return "ERP-04-018";
						}

						tester += "," + ft.getTester();
						ftCpuTime += ft.getFtCpuTime().doubleValue();
						ftIndexTime += ft.getFtIndexTime().doubleValue();
						contactDieQty += "," + ft.getContactDieQty();
						testerConfig += "," + ft.getTesterConfig();
					}

					if (ftList.size() > 1) {
						m.put("FG_12", "YES");
					} else {
						m.put("FG_12", "No");
					}
					m.put("FG_11", tester.substring(1));
					m.put("FG_13", ftCpuTime + "");
					m.put("FG_14", ftIndexTime + "");
					m.put("FG_16", contactDieQty.substring(1));
					m.put("FG_17", testerConfig.substring(1));
				} else if (mpFlag) {
					return "ERP-04-042";
				}

			} else {
				ProdStdTestRefDao prodStdTestRefDao = new ProdStdTestRefDao();
				ProdStdTestRefTo prodStdTestRefTo = prodStdTestRefDao
						.findByTestReferenceId(icFgTo.getFtTestProgList());
				if (prodStdTestRefTo != null) {

					if (mpFlag
							&& StringUtils.isEmpty(prodStdTestRefTo
									.getFtTesterE())) {
						// return "ERP-04-014";
					}
					if (mpFlag
							&& StringUtils.isEmpty(prodStdTestRefTo
									.getFtCpuTimeE())) {
						// return "ERP-04-015";
					}
					if (mpFlag
							&& StringUtils.isEmpty(prodStdTestRefTo
									.getFtIndexTimeE())) {
						// return "ERP-04-016";
					}
					if (mpFlag
							&& StringUtils.isEmpty(prodStdTestRefTo
									.getFtContactDieQty())) {
						// return "ERP-04-017";
					}
					if (mpFlag
							&& StringUtils.isEmpty(prodStdTestRefTo
									.getFtTesterSpecE())) {
						// return "ERP-04-018";
					}

					m.put("FG_12", "No");
					m.put("FG_11", prodStdTestRefTo.getFtTesterE());
					m.put("FG_13", prodStdTestRefTo.getFtCpuTimeE());
					m.put("FG_14", prodStdTestRefTo.getFtIndexTimeE());
					m.put("FG_16", prodStdTestRefTo.getFtContactDieQty());
					m.put("FG_17", prodStdTestRefTo.getFtTesterSpecE());
				} else if (mpFlag) {
					// return "ERP-04-043";
				}
			}

			if (mpFlag && StringUtils.isEmpty(projectTo.getGrossDie())) {
				// return "Wafer Gross is empty!";
				return "ERP-04-020";
			}

			m.put("FG_15", projectTo.getGrossDie());

			classes.add(m);
		}else if(pkgType.equalsIgnoreCase("312")) { // WLO
				// insert PIDB_IF_MATERIAL_MASTER
				materialType = "ZOFG";
				ifMaterialMasterTo.setMaterialNum(icFgTo.getMaterialNum());
				WloDao wloDao = new WloDao();
				WloTo  wloTo = wloDao.findByPrimaryKey(icFgTo.getMaterialNum());
				if( wloTo == null ){
					return "ERP-15-002";
				}
				if (materialType == null || materialType.length() <= 0 || materialType.equals("") ) {
					return "ERP-02-022";
				}
				if (wloTo.getDescription() != null && !wloTo.getDescription().equals("")) {
					ifMaterialMasterTo.setMaterialDesc(wloTo.getDescription());
				} else {
					ifMaterialMasterTo.setMaterialDesc(wloTo.getPartNum());
				}
				ifMaterialMasterTo.setMaterialType(materialType);
	
				ifMaterialMasterTo.setProductFamily(prodFamily);
				ifMaterialMasterTo.setPkgType("312");
				ifMaterialMasterTo.setIcType(icType);
				
				materialGroup = "18070";
				
				ifMaterialMasterTo.setMaterialGroup(materialGroup);
				ifMaterialMasterTo.setAppCategory(icFgTo.getAppCategory());			
				ifMaterialMasterTo.setTimeStamp(new Date());
				ifMaterialMasterTo.setReleasedBy(userTo.getUserId());
				
				ifMaterialMasterTo.setId(SequenceSupport
						.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
				// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
				ifMaterialMasterTo.setMpStatus(ERPHelper.judgeMP(icFgTo
						.getMaterialNum()));
				ifMaterialMasterTo.setReleaseTo(projectCode.getReleaseTo());
				
				//Basic Data , Purchase Order Text
				ifMaterialMasterTo.setPurchaseOrderText(icFgTo.getPartNum());
				ifMaterialMasterTo.setBasicDataText(icFgTo.getVendorDevice());
				
				ifMaterialMasterDao.insert(ifMaterialMasterTo,
						"PIDB_IF_MATERIAL_MASTER");
		}else if(pkgType.equalsIgnoreCase("313")) { // WLM
			
			// insert PIDB_IF_MATERIAL_MASTER
			materialType = "";
			ifMaterialMasterTo.setMaterialNum(icFgTo.getMaterialNum());
			WlmDao wlmDao = new WlmDao();
			WlmTo  wlmTo = wlmDao.findByPrimaryKey(icFgTo.getMaterialNum());
			if( wlmTo == null ){
				return "ERP-15-001";
			}
			if( wlmTo.getPackingType().equals("T")){
				materialType = "ZTFG";
			}else{//W,0
				materialType = "ZAFG";
			}
			if (materialType == null || materialType.length() <= 0 || materialType.equals("") ) {
				return "ERP-02-022";
			}
			
			if (wlmTo.getDescription() != null && !wlmTo.getDescription().equals("")) {
				ifMaterialMasterTo.setMaterialDesc(wlmTo.getDescription());
			} else {
				ifMaterialMasterTo.setMaterialDesc(wlmTo.getPartNum());
			}
			ifMaterialMasterTo.setMaterialType(materialType);
	
			ifMaterialMasterTo.setReleaseTo("HX");
			ifMaterialMasterTo.setProductFamily(prodFamily);
			ifMaterialMasterTo.setPkgType("313");
			ifMaterialMasterTo.setIcType(icType);
			materialGroup = "18080";
			
			ifMaterialMasterTo.setMaterialGroup(materialGroup);
			ifMaterialMasterTo.setAppCategory(icFgTo.getAppCategory());			
			ifMaterialMasterTo.setTimeStamp(new Date());
			ifMaterialMasterTo.setReleasedBy(userTo.getUserId());
			
			ifMaterialMasterTo.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
			// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
			ifMaterialMasterTo.setMpStatus(ERPHelper.judgeMP(icFgTo
					.getMaterialNum()));
			ifMaterialMasterTo.setReleaseTo(projectCode.getReleaseTo());
			
			//Basic Data , Purchase Order Text
			ifMaterialMasterTo.setPurchaseOrderText(icFgTo.getPartNum());
			ifMaterialMasterTo.setBasicDataText(icFgTo.getVendorDevice());
			
			ifMaterialMasterDao.insert(ifMaterialMasterTo,
					"PIDB_IF_MATERIAL_MASTER");
		}else if(pkgType.equalsIgnoreCase("314")) {//TSV
				// insert PIDB_IF_MATERIAL_MASTER
				materialType = "ZFG2";
				ifMaterialMasterTo.setMaterialNum(icFgTo.getMaterialNum());
				ifMaterialMasterTo.setMaterialDesc(icFgTo.getPartNum());
				ifMaterialMasterTo.setMaterialType(materialType);
				ifMaterialMasterTo.setMaterialGroup(materialGroup);
				ifMaterialMasterTo.setAppCategory(icFgTo.getAppCategory());
				ifMaterialMasterTo.setProductFamily(prodFamily);
				ifMaterialMasterTo.setPkgType(icFgTo.getPkgType());
				ifMaterialMasterTo.setIcType(icType);
				ifMaterialMasterTo.setTimeStamp(new Date());
				ifMaterialMasterTo.setReleasedBy(userTo.getUserId());
				ifMaterialMasterTo.setId(SequenceSupport
						.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
				// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
				ifMaterialMasterTo.setMpStatus(ERPHelper.judgeMP(icFgTo
						.getMaterialNum()));
				ifMaterialMasterTo.setReleaseTo(projectCode.getReleaseTo());
				
				//Basic Data , Purchase Order Text
				ifMaterialMasterTo.setPurchaseOrderText(icFgTo.getPartNum());
				ifMaterialMasterTo.setBasicDataText(icFgTo.getVendorDevice());
				
				ifMaterialMasterDao.insert(ifMaterialMasterTo,
						"PIDB_IF_MATERIAL_MASTER");
			
		} else if (Integer.parseInt(pkgType) > 304) {
			// insert PIDB_IF_MATERIAL_MASTER
			ifMaterialMasterTo.setMaterialNum(icFgTo.getMaterialNum());
			ifMaterialMasterTo.setMaterialDesc(icFgTo.getPartNum());
			ifMaterialMasterTo.setMaterialType(materialType);
			ifMaterialMasterTo.setMaterialGroup(materialGroup);
			ifMaterialMasterTo.setAppCategory(icFgTo.getAppCategory());
			ifMaterialMasterTo.setProductFamily(prodFamily);
			ifMaterialMasterTo.setPkgType(icFgTo.getPkgType());
			ifMaterialMasterTo.setIcType(icType);
			ifMaterialMasterTo.setTimeStamp(new Date());
			ifMaterialMasterTo.setReleasedBy(userTo.getUserId());
			ifMaterialMasterTo.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
			// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
			ifMaterialMasterTo.setMpStatus(ERPHelper.judgeMP(icFgTo
					.getMaterialNum()));
			ifMaterialMasterTo.setReleaseTo(projectCode.getReleaseTo());
			
			//Basic Data , Purchase Order Text
			ifMaterialMasterTo.setPurchaseOrderText(icFgTo.getPartNum());
			ifMaterialMasterTo.setBasicDataText(icFgTo.getVendorDevice());
			
			ifMaterialMasterDao.insert(ifMaterialMasterTo,
					"PIDB_IF_MATERIAL_MASTER");
		} else {
			// insert PIDB_IF_MATERIAL_MASTER
			ifMaterialMasterTo.setMaterialNum(icFgTo.getMaterialNum());
			ifMaterialMasterTo.setMaterialDesc(icFgTo.getPartNum());
			ifMaterialMasterTo.setMaterialType(materialType);
			ifMaterialMasterTo.setMaterialGroup(materialGroup);
			ifMaterialMasterTo.setAppCategory(icFgTo.getAppCategory());
			ifMaterialMasterTo.setProductFamily(prodFamily);
			ifMaterialMasterTo.setPkgType(icFgTo.getPkgType());
			ifMaterialMasterTo.setIcType(icType);
			ifMaterialMasterTo.setTimeStamp(new Date());
			ifMaterialMasterTo.setReleasedBy(userTo.getUserId());
			ifMaterialMasterTo.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
			// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
			ifMaterialMasterTo.setMpStatus(ERPHelper.judgeMP(icFgTo
					.getMaterialNum()));
			ifMaterialMasterTo.setReleaseTo(projectCode.getReleaseTo());
			
			//Basic Data , Purchase Order Text
			ifMaterialMasterTo.setPurchaseOrderText(icFgTo.getPartNum());
			ifMaterialMasterTo.setBasicDataText(icFgTo.getVendorDevice());
			
			ifMaterialMasterDao.insert(ifMaterialMasterTo,
					"PIDB_IF_MATERIAL_MASTER");

			Map<String, String> m = new HashMap<String, String>() {
				public String put(String key, String value) {
					if (StringUtils.isNotEmpty(value)) {
						super.put(key, value);
					}
					return null;
				}
			};

			if (mpFlag && StringUtils.isEmpty(projectTo.getProjName())) {
				// return "Project Name is empty!";
				return "ERP-04-011";
			}
			if (mpFlag && StringUtils.isEmpty(projectTo.getProjCode())) {
				// return "Project Code is empty!";
				return "ERP-04-012";
			}
			if (mpFlag && StringUtils.isEmpty(projectTo.getWaferInch())) {
				// return "Wafer Inch is empty!";
				return "ERP-04-013";
			}
			m.put("FT_01", projectTo.getProjName());
			m.put("FT_02", projectTo.getProjCode());
			m.put("FT_03", projectTo.getWaferInch());

			if (!pkgType.equalsIgnoreCase("303")
					&& !pkgType.equalsIgnoreCase("305")) {
				if (releaseFT) {
					List<FtTestProgramTo> ftList = ftTestProgramDao
							.findByFtTestProgNameList(icFgTo
									.getFtTestProgList());
					if (ftList != null && ftList.size() > 0) {
						String tester = "";
						double ftCpuTime = 0;
						double ftIndexTime = 0;
						String contactDieQty = "";
						String testerConfig = "";

						for (FtTestProgramTo ft : ftList) {

							if (mpFlag && StringUtils.isEmpty(ft.getTester())) {
								// return "FT Tester is empty!";
								// return "ERP-04-014";
							}
							if (mpFlag && ft.getFtCpuTime() == null) {
								// return "FT CPU Time is empty!";
								// return "ERP-04-015";
							}
							if (mpFlag && ft.getFtIndexTime() == null) {
								// return "FT Index Time is empty!";
								// return "ERP-04-016";
							}
							if (mpFlag
									&& StringUtils.isEmpty(ft
											.getContactDieQty())) {
								// return "Contact Die Quantity is empty!";
								// return "ERP-04-017";
							}
							if (mpFlag
									&& StringUtils
											.isEmpty(ft.getTesterConfig())) {
								// return "Tester Config is empty!";
								// return "ERP-04-018";
							}

							tester += "," + ft.getTester();
							ftCpuTime += ft.getFtCpuTime().doubleValue();
							ftIndexTime += ft.getFtIndexTime().doubleValue();
							contactDieQty += "," + ft.getContactDieQty();
							testerConfig += "," + ft.getTesterConfig();
						}

						if (ftList.size() > 1) {
							m.put("FT_05", "YES");
						} else {
							m.put("FT_05", "No");
						}
						m.put("FT_04", tester.substring(1));
						m.put("FT_06", ftCpuTime + "");
						m.put("FT_07", ftIndexTime + "");
						m.put("FT_09", contactDieQty.substring(1));
						m.put("FT_10", testerConfig.substring(1));
					} else if (mpFlag) {
						return "ERP-04-042";
					}

				} else {
					ProdStdTestRefDao prodStdTestRefDao = new ProdStdTestRefDao();
					ProdStdTestRefTo prodStdTestRefTo = prodStdTestRefDao
							.findByTestReferenceId(icFgTo.getFtTestProgList());
					if (prodStdTestRefTo != null) {

						if (mpFlag
								&& StringUtils.isEmpty(prodStdTestRefTo
										.getFtTesterE())) {
							// return "ERP-04-014";
						}
						if (mpFlag
								&& StringUtils.isEmpty(prodStdTestRefTo
										.getFtCpuTimeE())) {
							// return "ERP-04-015";
						}
						if (mpFlag
								&& StringUtils.isEmpty(prodStdTestRefTo
										.getFtIndexTimeE())) {
							// return "ERP-04-016";
						}
						if (mpFlag
								&& StringUtils.isEmpty(prodStdTestRefTo
										.getFtContactDieQty())) {
							// return "ERP-04-017";
						}
						if (mpFlag
								&& StringUtils.isEmpty(prodStdTestRefTo
										.getFtTesterSpecE())) {
							// return "ERP-04-018";
						}

						m.put("FT_05", "No");
						m.put("FT_04", prodStdTestRefTo.getFtTesterE());
						m.put("FT_06", prodStdTestRefTo.getFtCpuTimeE());
						m.put("FT_07", prodStdTestRefTo.getFtIndexTimeE());
						m.put("FT_09", prodStdTestRefTo.getFtContactDieQty());
						m.put("FT_10", prodStdTestRefTo.getFtTesterSpecE());
					} else if (mpFlag) {
						// return "ERP-04-043";
					}
				}
			}

			if (mpFlag && StringUtils.isEmpty(projectTo.getGrossDie())) {
				// return "Wafer Gross is empty!";
				return "ERP-04-020";
			}
			if (mpFlag && StringUtils.isEmpty(icFgTo.getPkgType())) {
				// return "Package Type is empty!";
				return "ERP-04-021";
			}

			// Added on 3/9
			SapMasterPackageTypeTo sapMasterPackageTypeTo = sapMasterPackageTypeDao
					.findByPkgType(icFgTo.getPkgType());

			m.put("FT_08", projectTo.getGrossDie());
			// m.put("FT_11", icFgTo.getPkgType());
			// Added on 3/9
			m.put("FT_11", sapMasterPackageTypeTo.getDescription());

			classes.add(m);

		}

		for (Map<String, String> m : classes) {
			for (String s : m.keySet()) {
				IfMaterialCharacterTo ifMaterialCharacterTo = new IfMaterialCharacterTo();
				ifMaterialCharacterTo.setMaterialNum(icFgTo.getMaterialNum());
				ifMaterialCharacterTo.setMaterialType(materialType);
				ifMaterialCharacterTo.setChTechName(s);
				ifMaterialCharacterTo.setChValue(m.get(s));
				ifMaterialCharacterTo.setId(SequenceSupport
						.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
				ifMaterialCharacterTo.setTimeStamp(new Date());
				ifMaterialCharacterTo.setReleasedBy(userTo.getUserId());
				ifMaterialCharacterTo.setReleaseTo(projectCode.getReleaseTo());
				charList.add(ifMaterialCharacterTo);
			}
		}
		ifMaterialCharacterDao.batchInsert(charList,
				"PIDB_IF_MATERIAL_CHARACTER");

		// Part II
		// insert PIDB_IF_MATERIAL_MASTER
		classes = new ArrayList<Map<String, String>>();
		charList = new ArrayList<IfMaterialCharacterTo>();
		ifMaterialMasterTo = new IfMaterialMasterTo();
		md = "IcFg";
		materialType = "";
		materialGroup = "";

		if (icFgTo.getRoutingAs()
				&& (pkgType.equalsIgnoreCase("304")
						|| pkgType.equalsIgnoreCase("305") || !pkgType
						.equalsIgnoreCase("303"))) {

			if (icFgTo.getRoutingAs()
					&& (pkgType.equalsIgnoreCase("304") || pkgType
							.equalsIgnoreCase("305"))) {
				// insert ZAS2 to PIDB_IF_MATERIAL_MASTER ELSE
				md += ".4";

			} else if (icFgTo.getRoutingAs()
					&& !pkgType.equalsIgnoreCase("303")
					&& !pkgType.equalsIgnoreCase("305")) {
				// insert ZAS to PIDB_IF_MATERIAL_MASTER ELSE
				md += ".3";
			}

			materialType = ERPHelper.getMaterialType(md);
			materialGroup = ERPHelper
					.getMaterialGroup(materialType, prodFamily);
			ifMaterialMasterTo.setMaterialNum("A"
					+ (icFgTo.getMaterialNum().substring(1)));
			ifMaterialMasterTo.setMaterialDesc(icFgTo.getPartNum());
			ifMaterialMasterTo.setMaterialType(materialType);
			ifMaterialMasterTo.setMaterialGroup(materialGroup);
			ifMaterialMasterTo.setTimeStamp(new Date());
			ifMaterialMasterTo.setReleasedBy(userTo.getUserId());
			ifMaterialMasterTo.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
			// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
			ifMaterialMasterTo.setMpStatus(ERPHelper.judgeMP(icFgTo
					.getMaterialNum()));
			ifMaterialMasterTo.setReleaseTo(projectCode.getReleaseTo());
			ifMaterialMasterDao.insert(ifMaterialMasterTo,
					"PIDB_IF_MATERIAL_MASTER");
		}

		// insert PIDB_IF_MATERIAL_CHARACTER
		// classes = new ArrayList<Map<String, String>>();
		md = "IcFg";
		materialType = "";
		materialGroup = "";

		if (icFgTo.getRoutingAs() && pkgType.equalsIgnoreCase("304")) {
			List<TradPkgTo> pkgList = tradPkgDao.findByProjNameAndPkgCode(
					projectTo.getProjName(), icFgTo.getPkgCode());
			md += ".4";

			TradPkgTo pkg = new TradPkgTo();
			if (pkgList != null && pkgList.size() > 0) {
				pkg = pkgList.get(0);
			} else {
				return "ERP-04-044";
			}

			Map<String, String> m = new HashMap<String, String>() {
				public String put(String key, String value) {
					if (StringUtils.isNotEmpty(value)) {
						super.put(key, value);
					}
					return null;
				}
			};

			if (mpFlag && StringUtils.isEmpty(pkg.getTradPkgType())) {
				// return "Traditional Package Type is empty!";
				return "ERP-04-022";
			}
			if (mpFlag && StringUtils.isEmpty(pkg.getPinCount())) {
				// return "Pin Count is empty!";
				return "ERP-04-023";
			}
			if (mpFlag && StringUtils.isEmpty(pkg.getLeadFrameType())) {
				// return "Lead Frame Type is empty!";
				return "ERP-04-024";
			}
			if (mpFlag && StringUtils.isEmpty(pkg.getSubtractLayer())) {
				// return "Subtract Layer is empty!";
				return "ERP-04-025";
			}
			if (mpFlag && StringUtils.isEmpty(pkg.getBodySize())) {
				// return "Body Size is empty!";
				return "ERP-04-026";
			}
			if (mpFlag && StringUtils.isEmpty(pkg.getGoldenWireWidth())) {
				// return "Golden Wire Width is empty!";
				return "ERP-04-027";
			}
			if (mpFlag && StringUtils.isEmpty(pkg.getMcpPkg())) {
				// return "MCP Package is empty!";
				return "ERP-04-028";
			}
			if (mpFlag && StringUtils.isEmpty(pkg.getMcpDieQty())) {
				// return "MCP Die Quantity is empty!";
				return "ERP-04-029";
			}

			m.put("AS_01", pkg.getTradPkgType());
			m.put("AS_02", pkg.getPinCount());
			m.put("AS_03", pkg.getLeadFrameType());
			m.put("AS_04", pkg.getSubtractLayer());
			m.put("AS_05", pkg.getBodySize());
			m.put("AS_06", pkg.getGoldenWireWidth());
			m
					.put("AS_07", pkg.getMcpPkg().equalsIgnoreCase("Y") ? "Yes"
							: "No");
			m.put("AS_08", pkg.getMcpDieQty());

			classes.add(m);

		} else if (icFgTo.getRoutingAs() && !pkgType.equalsIgnoreCase("303")
				&& !pkgType.equalsIgnoreCase("305")
				&& !pkgType.equalsIgnoreCase("305")) {
			md += ".3";

			Map<String, String> m = new HashMap<String, String>() {
				public String put(String key, String value) {
					if (StringUtils.isNotEmpty(value)) {
						super.put(key, value);
					}
					return null;
				}
			};

			if (mpFlag && StringUtils.isEmpty(pkgType)) {
				// return "Package Type is empty!";
				return "ERP-04-030";
			}
			if (mpFlag && StringUtils.isEmpty(projectTo.getPitch())) {
				// return "Pitch (UM) is empty!";
				return "ERP-04-031";
			}

			// Added on 3/9
			SapMasterPackageTypeTo sapMasterPackageTypeTo = sapMasterPackageTypeDao
					.findByPkgType(pkgType);

			// m.put("IL_01", pkgType);
			// Added on 3/9
			if( sapMasterPackageTypeTo != null  ){
				String iLPkgType = sapMasterPackageTypeTo.getDescription();
				if(projectTo.getProdFamily().equals("101")){//表Source
					iLPkgType = iLPkgType + "-SD";
				}else if(projectTo.getProdFamily().equals("102") ){//表Gate
					iLPkgType = iLPkgType + "-GD";
				}
				m.put("IL_01", iLPkgType);
			}else{
				m.put("IL_01",
						sapMasterPackageTypeTo != null ? sapMasterPackageTypeTo
								.getDescription() : "");
			}
			m.put("IL_02", projectTo.getPitch());

			classes.add(m);
		}

		materialType = ERPHelper.getMaterialType(md);
		materialGroup = ERPHelper.getMaterialGroup(materialType, prodFamily);

		for (Map<String, String> m : classes) {
			for (String s : m.keySet()) {
				IfMaterialCharacterTo ifMaterialCharacterTo = new IfMaterialCharacterTo();
				ifMaterialCharacterTo.setMaterialNum("A"
						+ (icFgTo.getMaterialNum().substring(1)));
				ifMaterialCharacterTo.setMaterialType(materialType);
				ifMaterialCharacterTo.setChTechName(s);
				ifMaterialCharacterTo.setChValue(m.get(s));
				ifMaterialCharacterTo.setId(SequenceSupport
						.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
				ifMaterialCharacterTo.setTimeStamp(new Date());
				ifMaterialCharacterTo.setReleasedBy(userTo.getUserId());
				ifMaterialCharacterTo.setReleaseTo(projectCode.getReleaseTo());
				charList.add(ifMaterialCharacterTo);
			}
		}
		ifMaterialCharacterDao.batchInsert(charList,
				"PIDB_IF_MATERIAL_CHARACTER");

		// Part III

		classes = new ArrayList<Map<String, String>>();
		charList = new ArrayList<IfMaterialCharacterTo>();
		ifMaterialMasterTo = new IfMaterialMasterTo();
		md = "CpMaterial.2";
		materialType = ERPHelper.getMaterialType(md);
		materialGroup = ERPHelper.getMaterialGroup(materialType, prodFamily);
		CpMaterialTo cpMaterialTo = cpMaterialDao.findByCpMaterialNum(icFgTo
				.getCpMaterialNum());

		if (cpMaterialTo != null) {
			String w_meterialNum = "";
			w_meterialNum = "W" + cpMaterialTo.getCpMaterialNum().substring(1);
			IcWaferTo icWaferTo = icWaferDao.findByPrimaryKey(w_meterialNum);
			String materialDesc = "";
			if (icWaferTo != null) {
				materialDesc = icWaferTo.getMaterialDesc();
			} else {
				materialDesc = cpMaterialTo.getProjectCodeWVersion();
			}

			// insert PIDB_IF_MATERIAL_MASTER
			ifMaterialMasterTo.setMaterialNum(cpMaterialTo.getCpMaterialNum());
			ifMaterialMasterTo.setMaterialDesc(materialDesc);
			ifMaterialMasterTo.setMaterialType(materialType);
			ifMaterialMasterTo.setMaterialGroup(materialGroup);
			ifMaterialMasterTo.setPurchaseOrderText(cpMaterialTo
					.getProjectCodeWVersion());
			ifMaterialMasterTo.setTimeStamp(new Date());
			ifMaterialMasterTo.setReleasedBy(userTo.getUserId());
			ifMaterialMasterTo.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
			// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
			ifMaterialMasterTo.setMpStatus(ERPHelper
					.judgeMPByProjWVersion(cpMaterialTo
							.getProjectCodeWVersion()));
			ifMaterialMasterTo.setReleaseTo(projectCode.getReleaseTo());
			ifMaterialMasterDao.insert(ifMaterialMasterTo,
					"PIDB_IF_MATERIAL_MASTER");

			// insert PIDB_IF_MATERIAL_CHARACTER
			Map<String, String> m = new HashMap<String, String>() {
				public String put(String key, String value) {
					if (StringUtils.isNotEmpty(value)) {
						super.put(key, value);
					}
					return null;
				}
			};
			CpTestProgramDao cpTestProgramDao = new CpTestProgramDao();

			m.put("CP_01", projectTo.getProjName());
			m.put("CP_02", projectTo.getProjCode());
			m.put("CP_03", projectTo.getWaferInch());

			String cpNameList = cpMaterialTo.getCpTestProgramNameList();

			// Remove Hank 2008/01/30
			/*
			 * if (StringUtils.isEmpty(cpNameList)) { // return
			 * "CpTestProgramNameList is empty!"; return "ERP-04-032"; }
			 */

			String[] cpName = cpNameList.split(",");

			if (pkgType.equalsIgnoreCase("304")
					|| pkgType.equalsIgnoreCase("305")) {
				List<IcWaferTo> waferList = icWaferDao.findByProjCode(icFgTo
						.getProjCode());
				if (waferList != null && waferList.size() > 0
						&& waferList.get(0).getRoutingCp()) {

					String tester = "";
					double cpCpuTime = 0;
					double cpIndexTime = 0;
					String contactDieQty = "";
					String testerConfig = "";

					if (cpName.length > 0) {
						for (String s : cpName) {
							CpTestProgramTo cpTestProgramTo = cpTestProgramDao
									.find(s);

							if (cpTestProgramTo == null) {
								continue;
							}

							if (mpFlag
									&& StringUtils.isEmpty(cpTestProgramTo
											.getTester())) {
								// return "Tester is empty!";
								// return "ERP-04-033";
							}
							if (mpFlag
									&& cpTestProgramTo.getCpCpuTime() == null) {
								// return "CP CPU Time(S) is empty!";
								// return "ERP-04-034";
							}
							if (mpFlag
									&& cpTestProgramTo.getCpIndexTime() == null) {
								// return "CP Index Time(S) is empty!";
								// return "ERP-04-035";
							}
							if (mpFlag
									&& StringUtils.isEmpty(cpTestProgramTo
											.getContactDieQty())) {
								// return "Contact Die Quantity is empty!";
								// return "ERP-04-036";
							}
							if (mpFlag
									&& StringUtils.isEmpty(cpTestProgramTo
											.getTesterConfig())) {
								// return "Tester Config is empty!";
								// return "ERP-04-037";
							}

							tester += "," + cpTestProgramTo.getTester();
							cpCpuTime += cpTestProgramTo.getCpCpuTime()
									.doubleValue();
							cpIndexTime += cpTestProgramTo.getCpIndexTime()
									.doubleValue();
							contactDieQty += ","
									+ cpTestProgramTo.getContactDieQty();
							testerConfig += ","
									+ cpTestProgramTo.getTesterConfig();
						}
					}

					if (cpName.length > 1) {
						m.put("CP_05", "YES");
					} else {
						m.put("CP_05", "No");
					}

					if (mpFlag && StringUtils.isEmpty(projectTo.getGrossDie())) {
						// return "Wafer Gross is empty!";
						return "ERP-04-038";
					}
					if (mpFlag
							&& StringUtils.isEmpty(projectTo.getFcstCpYield())) {
						// return "Forecast CP Yield(%) is empty!";
						return "ERP-04-039";
					}

					if (mpFlag && StringUtils.isEmpty(tester)) {
						return "ERP-04-033";
					}
					if (mpFlag && StringUtils.isEmpty(contactDieQty)) {
						return "ERP-04-036";
					}
					if (mpFlag && StringUtils.isEmpty(testerConfig)) {
						return "ERP-04-037";
					}

					m.put("CP_04", tester.substring(1));
					m.put("CP_06", cpCpuTime + "");
					m.put("CP_07", cpIndexTime + "");
					m.put("CP_09", contactDieQty.substring(1));
					m.put("CP_10", testerConfig.substring(1));
				}

			}

			m.put("CP_08", projectTo.getGrossDie());
			m.put("CP_11", projectTo.getFcstCpYield());

			classes.add(m);
		} else {
			if (pkgType.equalsIgnoreCase("304")
					|| pkgType.equalsIgnoreCase("305")) {
				List<IcWaferTo> waferList = icWaferDao.findByProjCode(icFgTo
						.getProjCode());
				if (waferList != null && waferList.size() > 0
						&& waferList.get(0).getRoutingCp()
						&& icFgTo.getMpStatus().equalsIgnoreCase("MP")) {
					return "ERP-04-046";
				}
			}
		}

		for (Map<String, String> m : classes) {
			for (String s : m.keySet()) {
				IfMaterialCharacterTo ifMaterialCharacterTo = new IfMaterialCharacterTo();
				ifMaterialCharacterTo.setMaterialNum(cpMaterialTo
						.getCpMaterialNum());
				ifMaterialCharacterTo.setMaterialType(materialType);
				ifMaterialCharacterTo.setChTechName(s);
				ifMaterialCharacterTo.setChValue(m.get(s));
				ifMaterialCharacterTo.setId(SequenceSupport
						.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
				ifMaterialCharacterTo.setTimeStamp(new Date());
				ifMaterialCharacterTo.setReleasedBy(userTo.getUserId());
				ifMaterialCharacterTo.setReleaseTo(projectCode.getReleaseTo());
				charList.add(ifMaterialCharacterTo);
			}
		}
		ifMaterialCharacterDao.batchInsert(charList,
				"PIDB_IF_MATERIAL_CHARACTER");

		// Part IV
		classes = new ArrayList<Map<String, String>>();
		charList = new ArrayList<IfMaterialCharacterTo>();
		ifMaterialMasterTo = new IfMaterialMasterTo();
		md = "CpMaterial.1";
		materialType = ERPHelper.getMaterialType(md);
		materialGroup = ERPHelper.getMaterialGroup(materialType, prodFamily);

		if (!StringUtils.isEmpty(icFgTo.getCpMaterialNum())) {
			// insert PIDB_IF_MATERIAL_MASTER
			String cpMateriaNum = cpMaterialTo.getCpMaterialNum();
			String w_meterialNum = "";
			w_meterialNum = "W" + cpMateriaNum.substring(1);
			IcWaferTo icWaferTo = icWaferDao.findByPrimaryKey(w_meterialNum);
			String materialDesc = "";
			if (icWaferTo != null) {
				materialDesc = icWaferTo.getMaterialDesc();
			} else {
				materialDesc = cpMaterialTo.getProjectCodeWVersion();
			}

			ifMaterialMasterTo
					.setMaterialNum("D" + (cpMateriaNum.substring(1)));
			ifMaterialMasterTo.setMaterialDesc(materialDesc);
			ifMaterialMasterTo.setMaterialType(materialType);
			ifMaterialMasterTo.setMaterialGroup(materialGroup);
			ifMaterialMasterTo.setPurchaseOrderText(cpMaterialTo
					.getProjectCodeWVersion());
			ifMaterialMasterTo.setTimeStamp(new Date());
			ifMaterialMasterTo.setReleasedBy(userTo.getUserId());
			ifMaterialMasterTo.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
			// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
			ifMaterialMasterTo.setMpStatus(ERPHelper
					.judgeMPByProjWVersion(cpMaterialTo
							.getProjectCodeWVersion()));
			ifMaterialMasterTo.setReleaseTo(projectCode.getReleaseTo());
			ifMaterialMasterDao.insert(ifMaterialMasterTo,
					"PIDB_IF_MATERIAL_MASTER");

			// insert PIDB_IF_MATERIAL_CHARACTER
			Map<String, String> m = new HashMap<String, String>() {
				public String put(String key, String value) {
					if (StringUtils.isNotEmpty(value)) {
						super.put(key, value);
					}
					return null;
				}
			};
			m.put("DS_01", projectTo.getGrossDie());
			m.put("DS_02", projectTo.getFcstCpYield());
			classes.add(m);
		}

		for (Map<String, String> m : classes) {
			for (String s : m.keySet()) {
				IfMaterialCharacterTo ifMaterialCharacterTo = new IfMaterialCharacterTo();
				ifMaterialCharacterTo.setMaterialNum("D"
						+ (cpMaterialTo.getCpMaterialNum().substring(1)));
				ifMaterialCharacterTo.setMaterialType(materialType);
				ifMaterialCharacterTo.setChTechName(s);
				ifMaterialCharacterTo.setChValue(m.get(s));
				ifMaterialCharacterTo.setId(SequenceSupport
						.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
				ifMaterialCharacterTo.setReleasedBy(userTo.getUserId());
				ifMaterialCharacterTo.setTimeStamp(new Date());
				ifMaterialCharacterTo.setReleaseTo(projectCode.getReleaseTo());
				charList.add(ifMaterialCharacterTo);
			}
		}
		ifMaterialCharacterDao.batchInsert(charList,
				"PIDB_IF_MATERIAL_CHARACTER");

		// end

		return null;

	}

}
