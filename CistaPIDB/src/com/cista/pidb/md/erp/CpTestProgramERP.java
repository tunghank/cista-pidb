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
import com.cista.pidb.core.SequenceSupport;
import com.cista.pidb.md.dao.CpMaterialDao;
import com.cista.pidb.md.dao.CpTestProgramDao;
import com.cista.pidb.md.dao.IcWaferDao;
import com.cista.pidb.md.dao.IfMaterialCharacterDao;
import com.cista.pidb.md.dao.IfMaterialMasterDao;
import com.cista.pidb.md.dao.ProjectCodeDao;
import com.cista.pidb.md.dao.ProjectDao;
import com.cista.pidb.md.to.CpMaterialTo;
import com.cista.pidb.md.to.CpTestProgramTo;
import com.cista.pidb.md.to.IcWaferTo;
import com.cista.pidb.md.to.IfMaterialCharacterTo;
import com.cista.pidb.md.to.IfMaterialMasterTo;
import com.cista.pidb.md.to.ProjectCodeTo;
import com.cista.pidb.md.to.ProjectTo;

public class CpTestProgramERP {

	/** Logger. */
	private static Log logger = LogFactory.getLog(CpTestProgramERP.class);

	public static String release(final Object md, final UserTo userTo) {

		final CpTestProgramDao cpTestProgramDao = new CpTestProgramDao();
		CpTestProgramTo cpTestProgramTo = (CpTestProgramTo) md;
		IfMaterialMasterTo ifMaterialMasterTo = new IfMaterialMasterTo();
		IfMaterialMasterDao ifMaterialMasterDao = new IfMaterialMasterDao();
		CpMaterialDao cpMaterialDao = new CpMaterialDao();
		CpMaterialTo updateCpMaterialTo = new CpMaterialTo();
		IcWaferDao icWaferDao = new IcWaferDao();

		String cpMaterialNum = cpTestProgramTo.getCpMaterialNum();
		cpMaterialNum = null != cpMaterialNum ? cpMaterialNum : "";
		cpMaterialNum = cpMaterialNum.trim();

		String projCodeWVersion = cpTestProgramTo.getProjCodeWVersion();
		projCodeWVersion = null != projCodeWVersion ? projCodeWVersion : "";
		/*
		 * //1.0 Add Hank for PhaseII Release Rule IcFgDao updateIcFgDao = new
		 * IcFgDao(); //1.0 Add 2007/12/20 New Rule List<IcFgTo>
		 * updateIcFgToList = updateIcFgDao.findByCpMaterialNum(cpMaterialNum);
		 */

		String cpTestProgName = cpTestProgramTo.getCpTestProgName().trim();
		if (cpTestProgName == null || cpTestProgName.length() <= 0) {
			logger.error("cpTestProgName is null.");
			return "ERP-03-001";
		}

		CpTestProgramTo chkCpTestProgramTo = cpTestProgramDao
				.findByCpMaterialNum(cpMaterialNum);
		updateCpMaterialTo = cpMaterialDao.findByCpMaterialNum(cpMaterialNum);

		// System.out.println("check chkCpTestProgramTo 0");

		if (chkCpTestProgramTo == null) {
			// System.out.println("check chkCpTestProgramTo 1");
			updateCpMaterialTo.setCpTestProgramNameList(cpTestProgName);
			/*
			 * //1.3 Update IC_FG CP_TEST_PROGRAM_LIST for (IcFgTo icFgTo :
			 * updateIcFgToList) { icFgTo.setCpTestProgNameList(cpTestProgName);
			 * Map<String, Object> key1 = new HashMap<String, Object>();
			 * key1.put("CP_MATERIAL_NUM" , cpMaterialNum);
			 * key1.put("MATERIAL_NUM" , icFgTo.getMaterialNum());
			 * updateIcFgDao.update(icFgTo, "PIDB_IC_FG", key1); }
			 */

			// 1.1 Multiple NULL Update PIDB_CP_MATERIAL
			// Add 2007/12/20 New Rule
			Map<String, Object> key = new HashMap<String, Object>();
			key.put("CP_MATERIAL_NUM", cpMaterialNum);
			key.put("PROJECT_CODE_W_VERSION", projCodeWVersion);
			// Update New program
			updateCpMaterialTo.setCpTestProgramNameList(cpTestProgramTo
					.getCpTestProgName());
			cpMaterialDao.update(updateCpMaterialTo, "PIDB_CP_MATERIAL", key);

			// 1.2 Release to ERP

			List<CpMaterialTo> cpMaterialList = cpMaterialDao
					.findByCpTestProgName(cpTestProgName, cpTestProgramTo
							.getProjCodeWVersion());

			/*
			 * try{ Thread.sleep(300000); }catch(Exception e ){
			 * e.printStackTrace(); }
			 */
			// System.out.println("Forward this 0");
			if (cpMaterialList == null || cpMaterialList.size() <= 0) {
				logger.error("Object md is null.");
				return "ERP-03-002";
			}

			for (CpMaterialTo cpMaterialTo : cpMaterialList) {
				if (cpMaterialTo == null) {
					continue;
				}
				String materialNum = cpMaterialTo.getCpMaterialNum();

				if (materialNum == null || materialNum.length() <= 0) {
					logger.error("Material number is null.");
					return "ERP-03-003";
				}

				String testProgramNameList = cpMaterialTo
						.getCpTestProgramNameList();
				if (testProgramNameList == null
						|| testProgramNameList.length() <= 0) {
					logger
							.error("CP test program name list is null in Cp Material table.");
					return "ERP-03-004";
				}

				String mdStr = "CpMaterial.2";
				String materialType = ERPHelper.getMaterialType(mdStr);
				if (materialType == null || materialType.length() <= 0) {
					logger
							.error("Not find Material type, Material type is null.");
					return "ERP-03-005";
				}

				ProjectDao projectDao = new ProjectDao();
				ProjectTo projectTo = projectDao
						.findByProjectCode(cpTestProgramTo.getProjCode());
				if (projectTo == null) {
					logger
							.error("ProjectTo is null,when find projectTo by project code.");
					return "ERP-03-006";
				}

				String prodFamily = projectTo.getProdFamily();
				if (prodFamily == null || prodFamily.length() <= 0) {
					logger.error("Product family is null.");
					return "ERP-03-007";
				}

				String materialGroup = ERPHelper.getMaterialGroup(materialType,
						prodFamily);
				if (materialGroup == null || materialGroup.length() <= 0) {
					logger
							.error("Not find Material group, Material group is null.");
					return "ERP-03-008";
				}

				String projectCodeWVersion = cpTestProgramTo
						.getProjCodeWVersion();
				if (projectCodeWVersion == null
						|| projectCodeWVersion.length() <= 0) {
					logger
							.error("Project code with version is null on Cp test program table.");
					return "ERP-03-009";
				}
				/*
				 * String w_meterialNum = ""; w_meterialNum = "W" +
				 * materialNum.substring(1); IcWaferTo icWaferTo =
				 * icWaferDao.findByPrimaryKey(w_meterialNum);
				 */
				String materialDesc = cpMaterialTo.getDescription();

				ifMaterialMasterTo.setMaterialNum(materialNum);
				if (materialDesc != null && !materialDesc.equals("")) {
					ifMaterialMasterTo.setMaterialDesc(materialDesc);
				} else {
					ifMaterialMasterTo.setMaterialDesc(projectCodeWVersion);
				}
				ifMaterialMasterTo.setMaterialType(materialType);
				ifMaterialMasterTo.setMaterialGroup(materialGroup);
				ifMaterialMasterTo.setPurchaseOrderText(projectCodeWVersion);
				ifMaterialMasterTo.setTimeStamp(new Date());
				ifMaterialMasterTo.setReleasedBy(userTo.getUserId());

				//取得projectCode中的Release_To值
				ProjectCodeTo projectCode = new ProjectCodeDao()
						.findByProjectCode(cpTestProgramTo.getProjCode());
				// 將Release_To 塞到 PIDB_IF_MATERIAL Table中
				ifMaterialMasterTo.setReleaseTo(projectCode.getReleaseTo());

				ifMaterialMasterTo.setId(SequenceSupport
						.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
				// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
				ifMaterialMasterTo.setMpStatus(ERPHelper
						.judgeMPByProjWVersion(projectCodeWVersion));

				// to Interface Table:PIDB_IF_MATERIAL_MASTER
				ifMaterialMasterDao.insert(ifMaterialMasterTo,
						"PIDB_IF_MATERIAL_MASTER");

				IfMaterialCharacterDao ifMaterialCharacterDao = new IfMaterialCharacterDao();
				IfMaterialCharacterTo ifMaterialCharacterTo = new IfMaterialCharacterTo();
				// String[] value = new String[12];
				// for (int i = 0; i < 11; i++) {
				// value[i] = "";
				// }
				// Double cpCpuTime = 0.0;
				// Double cpIndexTime = 0.0;

				Map<String, String> m = new HashMap<String, String>();
				// System.out.println("Forward this 1");
				if (StringUtils.isEmpty(projectTo.getWaferInch())) {
					return "ERP-03-013";
				}
				// System.out.println("Forward this 2");
				m.put("CP_01", projectTo.getProjName());
				m.put("CP_02", projectTo.getProjCode());
				m.put("CP_03", projectTo.getWaferInch());
				// System.out.println("Forward this 3");
				String[] cpName = testProgramNameList.split(",");
				String tester = "";
				double cpCpuTime = 0;
				double cpIndexTime = 0;
				String contactDieQty = "";
				String testerConfig = "";

				if (cpName.length > 0) {
					// System.out.println("Forward this 4");
					for (String s : cpName) {
						CpTestProgramTo tempTo = cpTestProgramDao.find(s);

						if (StringUtils.isEmpty(tempTo.getTester())) {
							// return "Tester is empty!";
							return "ERP-03-014";
						}
						if (tempTo.getCpCpuTime() == null) {
							// return "CP CPU Time(S) is empty!";
							return "ERP-03-015";
						}
						if (tempTo.getCpIndexTime() == null) {
							// return "CP Index Time(S) is empty!";
							return "ERP-03-016";
						}
						if (StringUtils.isEmpty(tempTo.getContactDieQty())) {
							// return "Contact Die Quantity is empty!";
							return "ERP-03-018";
						}
						if (StringUtils.isEmpty(tempTo.getTesterConfig())) {
							// return "Tester Config is empty!";
							return "ERP-03-019";
						}

						tester += "," + tempTo.getTester();
						cpCpuTime = add(cpCpuTime, tempTo.getCpCpuTime());
						cpIndexTime = add(cpIndexTime, tempTo.getCpIndexTime());
						contactDieQty += "," + tempTo.getContactDieQty();
						testerConfig += "," + tempTo.getTesterConfig();
					}
				}

				if (cpName.length > 1) {
					m.put("CP_05", "YES");
				} else {
					m.put("CP_05", "No");
				}

				if (StringUtils.isEmpty(projectTo.getGrossDie())) {
					// return "Wafer Gross is empty!";
					return "ERP-03-017";
				}
				if (StringUtils.isEmpty(projectTo.getFcstCpYield())) {
					// return "Forecast CP Yield(%) is empty!";
					return "ERP-03-020";
				}

				m.put("CP_04", tester.substring(1));
				m.put("CP_06", cpCpuTime + "");
				m.put("CP_07", cpIndexTime + "");
				m.put("CP_08", projectTo.getGrossDie());
				m.put("CP_09", contactDieQty.substring(1));
				m.put("CP_10", testerConfig.substring(1));
				m.put("CP_11", projectTo.getFcstCpYield());
				// System.out.println("Forward this");

				List<IfMaterialCharacterTo> charList = new ArrayList<IfMaterialCharacterTo>();
				for (String s : m.keySet()) {
					IfMaterialCharacterTo tempTo = new IfMaterialCharacterTo();
					tempTo.setMaterialNum(cpMaterialTo.getCpMaterialNum());
					tempTo.setMaterialType(materialType);
					tempTo.setChTechName(s);
					tempTo.setChValue(m.get(s));
					tempTo
							.setId(SequenceSupport
									.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
					tempTo.setTimeStamp(new Date());
					tempTo.setReleasedBy(userTo.getUserId());

					// 將Release_To 塞到 PIDB_IF_MATERIAL_CHARACTER Table中
					tempTo.setReleaseTo(projectTo.getReleaseTo());

					charList.add(tempTo);
				}

				ifMaterialCharacterDao.batchInsert(charList,
						"PIDB_IF_MATERIAL_CHARACTER");

			}
			return null;
		} else {
			// System.out.println("this 0");
			// 2.1 Multiple NOT NULL Update PIDB_CP_MATERIAL
			// Add 2007/12/20 New Rule
			List cpTestProgramList = (List) cpTestProgramDao
					.findTestProgNameByKey(projCodeWVersion, cpMaterialNum);
			String testProgName = "";
			for (int i = 0; i < cpTestProgramList.size(); i++) {
				if (i == (cpTestProgramList.size() - 1)) {
					testProgName = testProgName + cpTestProgramList.get(i);
				} else {
					testProgName = testProgName + cpTestProgramList.get(i)
							+ ",";
				}
			}

			// 2.1.1 Merage New Program
			// Add 2007/12/20 New Rule
			updateCpMaterialTo.setCpTestProgramNameList(testProgName);
			// System.out.println("this 1");
			Map<String, Object> key = new HashMap<String, Object>();
			key.put("CP_MATERIAL_NUM", cpMaterialNum);
			key.put("PROJECT_CODE_W_VERSION", projCodeWVersion);
			cpMaterialDao.update(updateCpMaterialTo, "PIDB_CP_MATERIAL", key);

			/*
			 * //2.1.2 Update IC_FG CP_TEST_PROGRAM_LIST for (IcFgTo icFgTo :
			 * updateIcFgToList) { icFgTo.setCpTestProgNameList(testProgName);
			 * Map<String, Object> key1 = new HashMap<String, Object>();
			 * key1.put("CP_MATERIAL_NUM" , cpMaterialNum);
			 * key1.put("MATERIAL_NUM" , icFgTo.getMaterialNum());
			 * updateIcFgDao.update(icFgTo, "PIDB_IC_FG", key1); }
			 */

			// 2.2 Releae to ERP
			// List<CpMaterialTo> cpMaterialList = cpMaterialDao
			// .findByCpTestProgName(cpTestProgName,
			// cpTestProgramTo.getProjCodeWVersion());
			List<CpMaterialTo> cpMaterialList = cpMaterialDao
					.findByCpTestProgName(testProgName, cpTestProgramTo
							.getProjCodeWVersion());

			if (cpMaterialList == null || cpMaterialList.size() <= 0) {
				logger.error("Object md is null.");
				return "ERP-03-002";
			}

			for (CpMaterialTo cpMaterialTo : cpMaterialList) {
				if (cpMaterialTo == null) {
					continue;
				}
				String materialNum = cpMaterialTo.getCpMaterialNum();
				if (materialNum == null || materialNum.length() <= 0) {
					logger.error("Material number is null.");
					return "ERP-03-003";
				}

				String testProgramNameList = cpMaterialTo
						.getCpTestProgramNameList();
				if (testProgramNameList == null
						|| testProgramNameList.length() <= 0) {
					logger
							.error("CP test program name list is null in Cp Material table.");
					return "ERP-03-004";
				}

				String mdStr = "CpMaterial.2";
				String materialType = ERPHelper.getMaterialType(mdStr);
				if (materialType == null || materialType.length() <= 0) {
					logger
							.error("Not find Material type, Material type is null.");
					return "ERP-03-005";
				}

				ProjectDao projectDao = new ProjectDao();
				ProjectTo projectTo = projectDao
						.findByProjectCode(cpTestProgramTo.getProjCode());
				if (projectTo == null) {
					logger
							.error("ProjectTo is null,when find projectTo by project code.");
					return "ERP-03-006";
				}

				String prodFamily = projectTo.getProdFamily();
				if (prodFamily == null || prodFamily.length() <= 0) {
					logger.error("Product family is null.");
					return "ERP-03-007";
				}

				String materialGroup = ERPHelper.getMaterialGroup(materialType,
						prodFamily);
				if (materialGroup == null || materialGroup.length() <= 0) {
					logger
							.error("Not find Material group, Material group is null.");
					return "ERP-03-008";
				}

				String projectCodeWVersion = cpTestProgramTo
						.getProjCodeWVersion();
				if (projectCodeWVersion == null
						|| projectCodeWVersion.length() <= 0) {
					logger
							.error("Project code with version is null on Cp test program table.");
					return "ERP-03-009";
				}
				String w_meterialNum = "";
				w_meterialNum = "W" + materialNum.substring(1);
				IcWaferTo icWaferTo = icWaferDao
						.findByPrimaryKey(w_meterialNum);
				String materialDesc = icWaferTo.getMaterialDesc();

				ifMaterialMasterTo.setMaterialNum(materialNum);
				ifMaterialMasterTo.setMaterialDesc(materialDesc);
				ifMaterialMasterTo.setMaterialType(materialType);
				ifMaterialMasterTo.setMaterialGroup(materialGroup);
				ifMaterialMasterTo.setPurchaseOrderText(projectCodeWVersion);
				ifMaterialMasterTo.setTimeStamp(new Date());
				ifMaterialMasterTo.setReleasedBy(userTo.getUserId());

				// 將Release_To 塞到 PIDB_IF_MATERIAL Table中
				ifMaterialMasterTo.setReleaseTo(projectTo.getReleaseTo());

				ifMaterialMasterTo.setId(SequenceSupport
						.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
				// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
				ifMaterialMasterTo.setMpStatus(ERPHelper
						.judgeMPByProjWVersion(projectCodeWVersion));
				// 2.2.1 to Interface Table:PIDB_IF_MATERIAL_MASTER
				ifMaterialMasterDao.insert(ifMaterialMasterTo,
						"PIDB_IF_MATERIAL_MASTER");

				// 2.2.2 to Interface Table:PIDB_IF_MATERIAL_MASTER
				IfMaterialCharacterDao ifMaterialCharacterDao = new IfMaterialCharacterDao();
				IfMaterialCharacterTo ifMaterialCharacterTo = new IfMaterialCharacterTo();
				// String[] value = new String[12];
				// for (int i = 0; i < 11; i++) {
				// value[i] = "";
				// }
				// Double cpCpuTime = 0.0;
				// Double cpIndexTime = 0.0;

				Map<String, String> m = new HashMap<String, String>();

				if (StringUtils.isEmpty(projectTo.getWaferInch())) {
					return "ERP-03-013";
				}
				// System.out.println("this 2");
				m.put("CP_01", projectTo.getProjName());
				m.put("CP_02", projectTo.getProjCode());
				m.put("CP_03", projectTo.getWaferInch());

				String[] cpName = testProgramNameList.split(",");
				String tester = "";
				double cpCpuTime = 0;
				double cpIndexTime = 0;

				String contactDieQty = "";
				String testerConfig = "";

				if (cpName.length > 0) {
					// System.out.println("this 3");
					for (String s : cpName) {
						// CpTestProgramTo tempTo = cpTestProgramDao.find(s);
						CpTestProgramTo tempTo = cpTestProgramDao
								.findByPrimaryKey(s, projCodeWVersion,
										cpMaterialNum);

						if (StringUtils.isEmpty(tempTo.getTester())) {
							// return "Tester is empty!";
							return "ERP-03-014";
						}
						if (tempTo.getCpCpuTime() == null) {
							// return "CP CPU Time(S) is empty!";
							return "ERP-03-015";
						}
						if (tempTo.getCpIndexTime() == null) {
							// return "CP Index Time(S) is empty!";
							return "ERP-03-016";
						}
						if (StringUtils.isEmpty(tempTo.getContactDieQty())) {
							// return "Contact Die Quantity is empty!";
							return "ERP-03-018";
						}
						if (StringUtils.isEmpty(tempTo.getTesterConfig())) {
							// return "Tester Config is empty!";
							return "ERP-03-019";
						}

						tester += "," + tempTo.getTester();

						cpCpuTime = add(cpCpuTime, tempTo.getCpCpuTime());
						cpIndexTime = add(cpIndexTime, tempTo.getCpIndexTime());

						contactDieQty += "," + tempTo.getContactDieQty();
						testerConfig += "," + tempTo.getTesterConfig();
					}
				}

				if (cpName.length > 1) {
					m.put("CP_05", "YES");
				} else {
					m.put("CP_05", "No");
				}

				if (StringUtils.isEmpty(projectTo.getGrossDie())) {
					// return "Wafer Gross is empty!";
					return "ERP-03-017";
				}
				if (StringUtils.isEmpty(projectTo.getFcstCpYield())) {
					// return "Forecast CP Yield(%) is empty!";
					return "ERP-03-020";
				}

				m.put("CP_04", tester.substring(1));
				m.put("CP_06", cpCpuTime + "");
				m.put("CP_07", cpIndexTime + "");
				m.put("CP_08", projectTo.getGrossDie());
				m.put("CP_09", contactDieQty.substring(1));
				m.put("CP_10", testerConfig.substring(1));
				m.put("CP_11", projectTo.getFcstCpYield());
				// System.out.println("this 4");
				List<IfMaterialCharacterTo> charList = new ArrayList<IfMaterialCharacterTo>();
				for (String s : m.keySet()) {
					IfMaterialCharacterTo tempTo = new IfMaterialCharacterTo();
					tempTo.setMaterialNum(cpMaterialTo.getCpMaterialNum());
					tempTo.setMaterialType(materialType);
					tempTo.setChTechName(s);
					tempTo.setChValue(m.get(s));
					tempTo
							.setId(SequenceSupport
									.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
					tempTo.setTimeStamp(new Date());
					tempTo.setReleasedBy(userTo.getUserId());

					// 將Release_To 塞到 PIDB_IF_MATERIAL_CHARACTER Table中
					tempTo.setReleaseTo(projectTo.getReleaseTo());

					charList.add(tempTo);
				}

				ifMaterialCharacterDao.batchInsert(charList,
						"PIDB_IF_MATERIAL_CHARACTER");

			}
			return null;
		}

	}

	public static Double add(Double v1, Double v2) {
		BigDecimal b1 = new BigDecimal(v1.toString());
		BigDecimal b2 = new BigDecimal(v2.toString());
		return b1.add(b2).doubleValue();
	}
}
