package com.cista.pidb.md.erp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cista.pidb.admin.to.UserTo;
import com.cista.pidb.code.dao.SapMasterPackageTypeDao;
import com.cista.pidb.code.to.SapMasterPackageTypeTo;
import com.cista.pidb.core.SequenceSupport;
import com.cista.pidb.md.dao.FtTestProgramDao;
import com.cista.pidb.md.dao.IcFgDao;
import com.cista.pidb.md.dao.IfMaterialCharacterDao;
import com.cista.pidb.md.dao.IfMaterialMasterDao;
import com.cista.pidb.md.dao.ProjectCodeDao;
import com.cista.pidb.md.dao.ProjectDao;
import com.cista.pidb.md.to.FtTestProgramTo;
import com.cista.pidb.md.to.IcFgTo;
import com.cista.pidb.md.to.IfMaterialCharacterTo;
import com.cista.pidb.md.to.IfMaterialMasterTo;
import com.cista.pidb.md.to.ProjectCodeTo;
import com.cista.pidb.md.to.ProjectTo;

/**
 * .
 * 
 * @author Hu Meixia
 * 
 */
public class FtTestProgramERP {

	/** Logger. */
	private static Log logger = LogFactory.getLog(FtTestProgramERP.class);

	public static String release(final Object md, final UserTo userTo) {

		final FtTestProgramDao ftTestProgramDao = new FtTestProgramDao();
		FtTestProgramTo ftTestProgramTo = (FtTestProgramTo) md;
		IfMaterialMasterTo ifMaterialMasterTo = new IfMaterialMasterTo();
		IfMaterialMasterDao ifMaterialMasterDao = new IfMaterialMasterDao();
		// 1.0 Add Hank for PhaseII Release Rule
		/*
		 * IcFgDao updateIcFgDao = new IcFgDao(); IcFgTo updateIcFgTo = new
		 * IcFgTo();
		 */
		String ftMaterialNum = ftTestProgramTo.getFtMaterialNum();
		ftMaterialNum = null != ftMaterialNum ? ftMaterialNum : "";
		ftMaterialNum = ftMaterialNum.trim();

		FtTestProgramTo chkFtTestProgramTo = ftTestProgramDao
				.findByFtMaterialNum(ftMaterialNum);
		String out;
		// 1.0 Add 2007/12/20 New Rule
		// updateIcFgTo = updateIcFgDao.findByMaterialNum(ftMaterialNum);

		if (chkFtTestProgramTo == null) {
			// 1.1 Multiple NULL
			// Add 2007/12/20 New Rule
			System.out.println("chkFtTestProgramTo Is NULL");

			// 1.2 Select FT Program form PIDB_FT_TEST_PROGRAM
			String testProgName = ftTestProgramTo.getFtTestProgName();
			testProgName = null != testProgName ? testProgName : "";
			FtTestProgramTo tmpFtTestProgramTo = ftTestProgramDao
					.findByPrimaryKey(ftMaterialNum, testProgName);

			System.out.println("testProgName " + testProgName);
			// 1.3 Update IC_FG FT_TEST_PROGRAM_LIST
			/*
			 * updateIcFgTo.setFtTestProgList(testProgName); Map<String,
			 * Object> key = new HashMap<String, Object>();
			 * key.put("MATERIAL_NUM" , ftMaterialNum);
			 * updateIcFgDao.update(updateIcFgTo, "PIDB_IC_FG", key);
			 */
			// 1.4 Release to SAP
			out = setReleaseValue(userTo, ftTestProgramTo, testProgName);
			return out;

		} else {
			// 1.1 Multiple NOT NULL -->To Merage Some Character Value
			// Add 2007/12/20 New Rule
			System.out.println("chkFtTestProgramTo NOT NULL");

			// 1.2 Select FT Program form PIDB_FT_TEST_PROGRAM
			List ftTestProgramList = (List) ftTestProgramDao
					.findListByFtMaterialNum(ftMaterialNum);
			String testProgName = "";
			for (int i = 0; i < ftTestProgramList.size(); i++) {
				if (i == (ftTestProgramList.size() - 1)) {
					testProgName = testProgName + ftTestProgramList.get(i);
				} else {
					testProgName = testProgName + ftTestProgramList.get(i)
							+ ",";
				}
			}
			System.out.println("testProgName " + testProgName);
			// 1.3 Update IC_FG FT_TEST_PROGRAM_LIST
			/*
			 * updateIcFgTo.setFtTestProgList(testProgName); Map<String,
			 * Object> key = new HashMap<String, Object>();
			 * key.put("MATERIAL_NUM" , ftMaterialNum);
			 * updateIcFgDao.update(updateIcFgTo, "PIDB_IC_FG", key);
			 */
			// 1.4 Release to SAP
			out = setReleaseValue(userTo, ftTestProgramTo, testProgName);
			return out;

		}

	}

	public static String setReleaseValue(final UserTo userTo,
			FtTestProgramTo ftTestProgramTo, String testProgName) {

		IfMaterialMasterTo ifMaterialMasterTo = new IfMaterialMasterTo();
		IfMaterialMasterDao ifMaterialMasterDao = new IfMaterialMasterDao();
		FtTestProgramDao ftTestProgramDao = new FtTestProgramDao();

		// Added on 3/9
		SapMasterPackageTypeDao sapMasterPackageTypeDao = new SapMasterPackageTypeDao();

		IcFgDao icFgDao = new IcFgDao();
		ProjectDao projectDao = new ProjectDao();
		String ftTestProgName = testProgName;

		if (ftTestProgName == null || ftTestProgName.length() <= 0) {
			logger.error("FtTestProgName is null.");
			return "ERP-07-001";
		}
		List<IcFgTo> icFgList = icFgDao.findByFtTestProgName(ftTestProgName);

		if (icFgList == null || icFgList.size() <= 0) {
			logger.error("Object md is null.");
			return "ERP-07-002";
		}

		for (IcFgTo icFgTo : icFgList) {

			if (icFgTo == null
					|| !icFgTo.getStatus().equalsIgnoreCase("Released")) {
				continue;
			}

			String materialNum = icFgTo.getMaterialNum();
			if (materialNum == null || materialNum.length() <= 0) {
				logger.error("Material number is null.");
				return "ERP-07-003";
			}

			String ftTestProgramNameList = icFgTo.getFtTestProgList();
			System.out
					.println("ftTestProgramNameList " + ftTestProgramNameList);
			if (ftTestProgramNameList == null
					|| ftTestProgramNameList.length() <= 0) {
				logger
						.error("FT test program name list is null in Cp Material table.");
				return "ERP-07-004";
			}

			String[] ftTestProgramName = ftTestProgramNameList.split(",");
			String pkgType = icFgTo.getPkgType();
			String mdStr = "IcFg";
			if (pkgType != null) {
				if (!pkgType.equalsIgnoreCase("304")) {
					mdStr += ".1";
				} else {
					mdStr += ".2";
				}
			} else {
				return "ERP-07-005";
			}
			String materialType = ERPHelper.getMaterialType(mdStr);
			if (materialType == null || materialType.length() <= 0) {
				logger.error("Not find Material type, Material type is null.");
				return "ERP-07-006";
			}

			String partNum = icFgTo.getPartNum();
			if (partNum == null || partNum.length() <= 0) {
				logger.error("PartNum is null.");
				return "ERP-07-007";
			}

			String projCode = icFgTo.getProjCode();
			if (projCode == null || projCode.length() <= 0) {
				logger.error("ProjCode is null.");
				return "ERP-07-008";
			}

			ProjectTo projectTo = projectDao.findByProjectCode(projCode);
			String prodFamily = projectTo.getProdFamily();
			String icType = projectTo.getIcType();
			if (StringUtils.isEmpty(prodFamily)) {
				return "ERP-07-009";
			}
			String materialGroup = ERPHelper.getMaterialGroup(materialType,
					prodFamily);
			if (materialGroup == null || materialGroup.length() <= 0) {
				logger
						.error("Not find Material group, Material group is null.");
				return "ERP-07-010";
			}

			// 取得project_Code中的release_To
			ProjectCodeTo projectCode = new ProjectCodeDao()
					.findByProjectCode(projCode);

			ifMaterialMasterTo.setMaterialNum(materialNum);
			ifMaterialMasterTo.setMaterialDesc(partNum);
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
			ifMaterialMasterTo.setMpStatus(ERPHelper.judgeMP(materialNum));
			ifMaterialMasterTo.setReleaseTo(projectCode.getReleaseTo());
			ifMaterialMasterDao.insert(ifMaterialMasterTo,
					"PIDB_IF_MATERIAL_MASTER");

			// 1.3 Release Character
			IfMaterialCharacterDao ifMaterialCharacterDao = new IfMaterialCharacterDao();
			IfMaterialCharacterTo ifMaterialCharacterTo = new IfMaterialCharacterTo();

			Map<String, String> m = new HashMap<String, String>();

			if (StringUtils.isEmpty(projectTo.getProjName())) {
				// return "Project Name is empty!";
				return "ERP-07-013";
			}
			if (StringUtils.isEmpty(projectTo.getProjCode())) {
				// return "Project Code is empty!";
				return "ERP-07-012";
			}
			if (StringUtils.isEmpty(projectTo.getWaferInch())) {
				// return "Wafer Inch is empty!";
				return "ERP-07-014";
			}

			// 1.4 PKG Type is 304 or Not
			if (pkgType.trim().equals("304")) {
				m.put("FG_08", projectTo.getProjName());
				m.put("FG_09", projectTo.getProjCode());
				m.put("FG_10", projectTo.getWaferInch());

				String tester = "";
				double ftCpuTime = 0;
				double ftIndexTime = 0;
				String contactDieQty = "";
				String testerConfig = "";

				for (int i = 0; i < ftTestProgramName.length; i++) {
					String name = ftTestProgramName[i];
					FtTestProgramTo ft = (FtTestProgramTo) ftTestProgramDao
							.find(name);

					if (StringUtils.isEmpty(ft.getTester())) {
						// return "FT Tester is empty!";
						return "ERP-07-015";
					}
					if (ft.getFtCpuTime() == null) {
						// return "FT CPU Time is empty!";
						return "ERP-07-016";
					}
					if (ft.getFtIndexTime() == null) {
						// return "FT Index Time is empty!";
						return "ERP-07-017";
					}
					if (StringUtils.isEmpty(ft.getContactDieQty())) {
						// return "Contact Die Quantity is empty!";
						return "ERP-07-019";
					}
					if (StringUtils.isEmpty(ft.getTesterConfig())) {
						// return "Tester Config is empty!";
						return "ERP-07-020";
					}

					tester += "," + ft.getTester();
					ftCpuTime = add(ftCpuTime, ft.getFtCpuTime());
					ftIndexTime = add(ftIndexTime, ft.getFtIndexTime());
					contactDieQty += "," + ft.getContactDieQty();
					testerConfig += "," + ft.getTesterConfig();
				}

				m.put("FG_11", tester.substring(1));
				if (ftTestProgramName.length > 1) {
					m.put("FG_12", "YES");
				} else {
					m.put("FG_12", "No");
				}
				m.put("FG_13", ftCpuTime + "");
				m.put("FG_14", ftIndexTime + "");
				m.put("FG_16", contactDieQty.substring(1));
				m.put("FG_17", testerConfig.substring(1));

				if (StringUtils.isEmpty(projectTo.getGrossDie())) {
					// return "Wafer Gross is empty!";
					return "ERP-07-018";
				}

				// Added on 3/9
				SapMasterPackageTypeTo sapMasterPackageTypeTo = sapMasterPackageTypeDao
						.findByPkgType(pkgType);

				m.put("FG_15", projectTo.getGrossDie());

				List<IfMaterialCharacterTo> charList = new ArrayList<IfMaterialCharacterTo>();
				for (String s : m.keySet()) {
					IfMaterialCharacterTo tempTo = new IfMaterialCharacterTo();
					tempTo.setMaterialNum(materialNum);
					tempTo.setMaterialType(materialType);
					tempTo.setChTechName(s);
					tempTo.setChValue(m.get(s));
					tempTo
							.setId(SequenceSupport
									.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
					tempTo.setTimeStamp(new Date());
					tempTo.setReleasedBy(userTo.getUserId());
					tempTo.setReleaseTo(projectCode.getReleaseTo());
					charList.add(tempTo);
				}

				ifMaterialCharacterDao.batchInsert(charList,
						"PIDB_IF_MATERIAL_CHARACTER");
			} else {
				m.put("FT_01", projectTo.getProjName());
				m.put("FT_02", projectTo.getProjCode());
				m.put("FT_03", projectTo.getWaferInch());

				String tester = "";
				double ftCpuTime = 0;
				double ftIndexTime = 0;
				String contactDieQty = "";
				String testerConfig = "";

				for (int i = 0; i < ftTestProgramName.length; i++) {
					String name = ftTestProgramName[i];
					FtTestProgramTo ft = (FtTestProgramTo) ftTestProgramDao
							.find(name);

					if (StringUtils.isEmpty(ft.getTester())) {
						// return "FT Tester is empty!";
						return "ERP-07-015";
					}
					if (ft.getFtCpuTime() == null) {
						// return "FT CPU Time is empty!";
						return "ERP-07-016";
					}
					if (ft.getFtIndexTime() == null) {
						// return "FT Index Time is empty!";
						return "ERP-07-017";
					}
					if (StringUtils.isEmpty(ft.getContactDieQty())) {
						// return "Contact Die Quantity is empty!";
						return "ERP-07-019";
					}
					if (StringUtils.isEmpty(ft.getTesterConfig())) {
						// return "Tester Config is empty!";
						return "ERP-07-020";
					}

					tester += "," + ft.getTester();
					ftCpuTime = add(ftCpuTime, ft.getFtCpuTime());
					ftIndexTime = add(ftIndexTime, ft.getFtIndexTime());
					contactDieQty += "," + ft.getContactDieQty();
					testerConfig += "," + ft.getTesterConfig();
				}

				if (ftTestProgramName.length > 1) {
					m.put("FT_05", "YES");
				} else {
					m.put("FT_05", "No");
				}
				m.put("FT_04", tester.substring(1));
				m.put("FT_06", ftCpuTime + "");
				m.put("FT_07", ftIndexTime + "");
				m.put("FT_09", contactDieQty.substring(1));
				m.put("FT_10", testerConfig.substring(1));

				if (StringUtils.isEmpty(projectTo.getGrossDie())) {
					// return "Wafer Gross is empty!";
					return "ERP-07-018";
				}

				// Added on 3/9
				SapMasterPackageTypeTo sapMasterPackageTypeTo = sapMasterPackageTypeDao
						.findByPkgType(pkgType);

				m.put("FT_08", projectTo.getGrossDie());
				// m.put("FT_11", pkgType);
				// Added on 3/9
				m.put("FT_11", sapMasterPackageTypeTo.getDescription());

				List<IfMaterialCharacterTo> charList = new ArrayList<IfMaterialCharacterTo>();
				for (String s : m.keySet()) {
					IfMaterialCharacterTo tempTo = new IfMaterialCharacterTo();
					tempTo.setMaterialNum(materialNum);
					tempTo.setMaterialType(materialType);
					tempTo.setChTechName(s);
					tempTo.setChValue(m.get(s));
					tempTo
							.setId(SequenceSupport
									.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
					tempTo.setTimeStamp(new Date());
					tempTo.setReleasedBy(userTo.getUserId());
					tempTo.setReleaseTo(projectCode.getReleaseTo());
					charList.add(tempTo);
				}

				ifMaterialCharacterDao.batchInsert(charList,
						"PIDB_IF_MATERIAL_CHARACTER");
			}
		}
		return null;
	}

	public static Double add(Double v1, Double v2) {
		BigDecimal b1 = new BigDecimal(v1.toString());
		BigDecimal b2 = new BigDecimal(v2.toString());
		return b1.add(b2).doubleValue();
	}
}
