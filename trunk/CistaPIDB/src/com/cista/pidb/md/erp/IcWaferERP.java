package com.cista.pidb.md.erp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cista.pidb.admin.dao.UserDao;
import com.cista.pidb.admin.to.UserTo;
import com.cista.pidb.code.dao.FabCodeDao;
import com.cista.pidb.code.to.FabCodeTo;
import com.cista.pidb.core.PIDBContext;
import com.cista.pidb.core.SequenceSupport;
import com.cista.pidb.md.action.SendMailDispatch;
import com.cista.pidb.md.dao.BumpMaskDao;
import com.cista.pidb.md.dao.ColorFilterMaterialDao;
import com.cista.pidb.md.dao.CpCspMaterialDao;
import com.cista.pidb.md.dao.CpMaterialDao;
import com.cista.pidb.md.dao.CpPolishMaterialDao;
import com.cista.pidb.md.dao.CpTestProgramDao;
import com.cista.pidb.md.dao.CpTsvMaterialDao;
import com.cista.pidb.md.dao.IcWaferDao;
import com.cista.pidb.md.dao.IfMaterialCharacterDao;
import com.cista.pidb.md.dao.IfMaterialMasterDao;
import com.cista.pidb.md.dao.IfProjectCodeDao;
import com.cista.pidb.md.dao.ProjectCodeDao;
import com.cista.pidb.md.dao.ProjectDao;
import com.cista.pidb.md.dao.WaferColorFilterDao;
import com.cista.pidb.md.to.BumpMaskTo;
import com.cista.pidb.md.to.ColorFilterMaterialTo;
import com.cista.pidb.md.to.CpCspMaterialTo;
import com.cista.pidb.md.to.CpMaterialTo;
import com.cista.pidb.md.to.CpPolishMaterialTo;
import com.cista.pidb.md.to.CpTestProgramTo;
import com.cista.pidb.md.to.CpTsvMaterialTo;
import com.cista.pidb.md.to.IcWaferTo;
import com.cista.pidb.md.to.IfMaterialCharacterTo;
import com.cista.pidb.md.to.IfMaterialMasterTo;
import com.cista.pidb.md.to.IfProjectCodeTo;
import com.cista.pidb.md.to.ProjectCodeTo;
import com.cista.pidb.md.to.ProjectTo;
import com.cista.pidb.md.to.WaferColorFilterTo;

public class IcWaferERP {
	/** Logger. */
	private static Log logger = LogFactory.getLog(IcWaferERP.class);

	public static String release(final Object md, final UserTo userTo) {
		// TODO Auto-generated method stub
		IcWaferTo icWaferTo = (IcWaferTo) md;

		String projCode = icWaferTo.getProjCode();
		ProjectCodeDao projectCodeDao = new ProjectCodeDao();
		IfProjectCodeDao ifCodeDao = new IfProjectCodeDao();

		// release project code
		IfProjectCodeTo ifProjCodeTo = new IfProjectCodeTo();
		ifProjCodeTo.setId(SequenceSupport
				.nextValue(SequenceSupport.SEQ_IF_PROJECT_CODE));
		ifProjCodeTo.setSapStatus("");
		ifProjCodeTo.setInfoMessage("");
		ifProjCodeTo.setTimeStamp(new Date());
		ifProjCodeTo.setReleasedBy(userTo.getUserId());
		ifProjCodeTo.setProjCode(projCode);

		ProjectCodeTo projectCodeTo = projectCodeDao
				.findByProjectCode(projCode);

		// 將Release_To 塞到 PIDB_IF_PROJECT_CODE Table中
		ifProjCodeTo.setReleaseTo(projectCodeTo.getReleaseTo());
		ifCodeDao.insert(ifProjCodeTo);
		String errorWaferInch = "ERP-02-004";
		if (projectCodeTo == null) {
			return errorWaferInch;
		}
		ProjectDao projectDao = new ProjectDao();
		ProjectTo projectTo = projectDao.find(projectCodeTo.getProjName());
		if (projectTo == null) {
			return errorWaferInch;
		}
		if (projectTo.getWaferInch() == null
				|| projectTo.getWaferInch().length() <= 0) {
			return errorWaferInch;
		}
		if (projectTo.getProcTech() == null
				|| projectTo.getProcTech().length() <= 0) {
			return "ERP-02-005";
		}
		if (projectTo.getPolyMetalLayers() == null
				|| projectTo.getPolyMetalLayers().length() <= 0) {
			return "ERP-02-006";
		}
		if (projectTo.getVoltage() == null
				|| projectTo.getVoltage().length() <= 0) {
			return "ERP-02-007";
		}
		if (projectTo.getProcLayerNo() == null
				|| projectTo.getProcLayerNo().length() <= 0) {
			return "ERP-02-008";
		}

		IfMaterialMasterTo ifMaterialMasterTo = new IfMaterialMasterTo();

		// 將Release_To 塞到 PIDB_IF_MATERIAL Table中
		ifMaterialMasterTo.setReleaseTo(projectCodeTo.getReleaseTo());

		// set materialNum and materialDesc and purchsing order text
		ifMaterialMasterTo.setMaterialNum(icWaferTo.getMaterialNum());
		ifMaterialMasterTo.setMaterialDesc(icWaferTo.getMaterialDesc());
		ifMaterialMasterTo
				.setPurchaseOrderText(icWaferTo.getProjCodeWVersion());

		// set materialType
		String mt = "IcWafer";
		String materialType = ERPHelper.getMaterialType(mt);
		ifMaterialMasterTo.setMaterialType(materialType);

		// set materilGroup
		String waferInchParam = projectTo.getWaferInch().substring(0,
				projectTo.getWaferInch().length() - 1);
		String materialGroup = ERPHelper.getMaterialGroup(materialType,
				waferInchParam);
		ifMaterialMasterTo.setMaterialGroup(materialGroup);

		// set Basic_Data_Text
		String basicDataText = projectTo.getProcTech() + "/"
				+ projectTo.getPolyMetalLayers() + "/" + projectTo.getVoltage()
				+ "/" + projectTo.getProcLayerNo();

		if (StringUtils.isNotEmpty(icWaferTo.getFabDeviceId())) {
			basicDataText = icWaferTo.getFabDeviceId() + "/" + basicDataText;
		} else {
			basicDataText = "NA/" + basicDataText;
		}

		FabCodeDao fabCodeDao = new FabCodeDao();
		String fabDescr = fabCodeDao.getByFab(projectTo.getFab()).getFabDescr();
		if (StringUtils.isNotEmpty(fabDescr)) {
			basicDataText = fabDescr + "/" + basicDataText;
		}

		ifMaterialMasterTo.setBasicDataText(basicDataText);

		// set ID
		ifMaterialMasterTo.setReleasedBy(userTo.getUserId());
		ifMaterialMasterTo.setId(SequenceSupport
				.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
		ifMaterialMasterTo.setTimeStamp(new Date());

		// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
		ifMaterialMasterTo.setMpStatus(ERPHelper
				.judgeMPByProjWVersion(icWaferTo.getProjCodeWVersion()));

		// insert to PIDB_IF_MATERIAL_MASTER
		IfMaterialMasterDao ifDao = new IfMaterialMasterDao();
		ifDao.insert(ifMaterialMasterTo, "PIDB_IF_MATERIAL_MASTER");

		IfMaterialCharacterDao ifCharDao = new IfMaterialCharacterDao();
		IfMaterialCharacterTo ifCharTo = new IfMaterialCharacterTo();
		if (projectTo.getFab() == null && projectTo.getFab().length() <= 0) {
			return "ERP-02-009";
		}
		FabCodeDao fabDao = new FabCodeDao();
		FabCodeTo fabTo = fabDao.getByFab(projectTo.getFab());
		if (fabTo == null) {
			return "ERP-02-010";
		}
		if (fabTo.getVendorCode() == null
				|| fabTo.getVendorCode().length() <= 0) {
			return "ERP-02-011";
		}
		if (fabTo.getFabSap() == null || fabTo.getFabSap().length() <= 0) {
			return "ERP-02-012";
		}
		if (projectTo.getGrossDie() == null
				|| projectTo.getGrossDie().length() <= 0) {
			return "ERP-02-013";
		}
		ifCharTo.setMaterialNum(icWaferTo.getMaterialNum());
		ifCharTo.setMaterialType(materialType);
		ifCharTo.setTimeStamp(new Date());

		// 將Release_To 塞到 PIDB_IF_MATERIAL_CHARACTER Table中
		ifCharTo.setReleaseTo(projectCodeTo.getReleaseTo());

		ifCharTo.setReleasedBy(userTo.getUserId());
		ifCharTo.setId(SequenceSupport
				.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));

		ifCharTo.setChTechName("WF_01");
		ifCharTo.setChValue(fabTo.getVendorCode());
		ifCharDao.insert(ifCharTo, "PIDB_IF_MATERIAL_CHARACTER");

		ifCharTo.setChTechName("WF_03");
		ifCharTo.setChValue(projectTo.getWaferInch());
		ifCharTo.setId(SequenceSupport
				.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
		ifCharDao.insert(ifCharTo, "PIDB_IF_MATERIAL_CHARACTER");

		ifCharTo.setChTechName("WF_04");
		ifCharTo.setChValue(projectTo.getProcTech());
		ifCharTo.setId(SequenceSupport
				.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
		ifCharDao.insert(ifCharTo, "PIDB_IF_MATERIAL_CHARACTER");

		ifCharTo.setChTechName("WF_05");
		ifCharTo.setChValue(projectTo.getPolyMetalLayers());
		ifCharTo.setId(SequenceSupport
				.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
		ifCharDao.insert(ifCharTo, "PIDB_IF_MATERIAL_CHARACTER");

		ifCharTo.setChTechName("WF_06");
		ifCharTo.setChValue(projectTo.getVoltage());
		ifCharTo.setId(SequenceSupport
				.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
		ifCharDao.insert(ifCharTo, "PIDB_IF_MATERIAL_CHARACTER");

		ifCharTo.setChTechName("WF_07");
		ifCharTo.setChValue(projectTo.getProcLayerNo());
		ifCharTo.setId(SequenceSupport
				.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
		ifCharDao.insert(ifCharTo, "PIDB_IF_MATERIAL_CHARACTER");

		ifCharTo.setChTechName("WF_02");
		ifCharTo.setChValue(fabTo.getFabSap());
		ifCharTo.setId(SequenceSupport
				.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
		ifCharDao.insert(ifCharTo, "PIDB_IF_MATERIAL_CHARACTER");

		ifCharTo.setChTechName("WF_08");
		ifCharTo.setChValue(projectTo.getGrossDie());
		ifCharTo.setId(SequenceSupport
				.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
		
		
		String subFab = projectTo.getSubFab();
		subFab =null!=subFab?subFab:"";
		
		logger.debug("projectTo.getSubFab() " + subFab);
		if( !subFab.equals("")){
			ifCharTo.setChTechName("WF_09");
			ifCharTo.setChValue(subFab);
			ifCharTo.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
		}
		
		ifCharDao.insert(ifCharTo, "PIDB_IF_MATERIAL_CHARACTER");
		return null;
	}

	public static String releaseBumping(final Object md, final UserTo userTo,
			final IcWaferTo icWaferTo) {
		BumpMaskTo bumpMaskTo = (BumpMaskTo) md;
		String bumpMaterialNum = "B"
				+ icWaferTo.getMaterialNum().substring(1,
						icWaferTo.getMaterialNum().length());
		String mdStr = "BumpMask";
		String materialType = ERPHelper.getMaterialType(mdStr);
		if (materialType == null || materialType.length() <= 0) {
			return "ERP-02-014";
		}

		ProjectDao projectDao = new ProjectDao();
		ProjectTo projectTo = projectDao.findByProjectCode(bumpMaskTo
				.getProjCode());
		if (projectTo == null) {
			return "ERP-02-015";
		}
		String prodFamily = projectTo.getProdFamily();
		if (prodFamily == null || prodFamily.length() <= 0) {
			return "ERP-02-016";
		}

		String materialGroup = ERPHelper.getMaterialGroup(materialType,
				prodFamily);
		if (materialGroup == null || materialGroup.length() <= 0) {
			return "ERP-02-017";
		}
		ProjectCodeTo projectCodeTo = new ProjectCodeDao()
				.findByProjectCode(bumpMaskTo.getProjCode());

		// 將Release_To 塞到 PIDB_IF_PROJECT_CODE Table中
		IfMaterialMasterDao ifMaterialMasterDao = new IfMaterialMasterDao();
		IfMaterialMasterTo ifMaterialMasterTo = new IfMaterialMasterTo();
		ifMaterialMasterTo.setMaterialNum(bumpMaterialNum);
		ifMaterialMasterTo.setMaterialType(materialType);
		ifMaterialMasterTo.setMaterialDesc(icWaferTo.getMaterialDesc());
		ifMaterialMasterTo.setMaterialGroup(materialGroup);
		ifMaterialMasterTo
				.setPurchaseOrderText(icWaferTo.getProjCodeWVersion());
		// 將Release_To 塞到 pidb_if_material_master Table中
		ifMaterialMasterTo.setReleaseTo(projectCodeTo.getReleaseTo());
		ifMaterialMasterTo.setTimeStamp(new Date());
		ifMaterialMasterTo.setId(SequenceSupport
				.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
		ifMaterialMasterTo.setReleasedBy(userTo.getUserId());
		// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
		ifMaterialMasterTo.setMpStatus(ERPHelper
				.judgeMPByProjWVersion(icWaferTo.getProjCodeWVersion()));

		ifMaterialMasterDao.insert(ifMaterialMasterTo,
				"PIDB_IF_MATERIAL_MASTER");

		String waferInch = projectTo.getWaferInch();
		if (waferInch == null || waferInch.length() <= 0) {
			return "ERP-02-018";
		}
		String grossDie = projectTo.getGrossDie();
		if (grossDie == null || grossDie.length() <= 0) {
			return "ERP-02-019";
		}
		if (bumpMaskTo.getRdl() == null || bumpMaskTo.getRdl().length() <= 0) {
			return "ERP-02-020";
		}
		IfMaterialCharacterDao ifMaterialCharacterDao = new IfMaterialCharacterDao();
		IfMaterialCharacterTo ifMaterialCharacterTo = new IfMaterialCharacterTo();
		ifMaterialCharacterTo.setMaterialNum(bumpMaterialNum);
		ifMaterialCharacterTo.setMaterialType(materialType);
		ifMaterialCharacterTo.setTimeStamp(new Date());
		ifMaterialCharacterTo.setId(SequenceSupport
				.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
		// 將Release_To 塞到 pidb_if_material_character Table中
		ifMaterialCharacterTo.setReleaseTo(projectCodeTo.getReleaseTo());
		ifMaterialCharacterTo.setReleasedBy(userTo.getUserId());

		ifMaterialCharacterTo.setChTechName("BP_01");
		ifMaterialCharacterTo.setChValue(waferInch);
		ifMaterialCharacterDao.insert(ifMaterialCharacterTo);

		ifMaterialCharacterTo.setChTechName("BP_02");
		ifMaterialCharacterTo.setChValue(bumpMaskTo.getRdl());
		ifMaterialCharacterTo.setId(SequenceSupport
				.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
		ifMaterialCharacterDao.insert(ifMaterialCharacterTo);

		ifMaterialCharacterTo.setChTechName("BP_03");
		ifMaterialCharacterTo.setChValue(grossDie);
		ifMaterialCharacterTo.setId(SequenceSupport
				.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
		ifMaterialCharacterDao.insert(ifMaterialCharacterTo);
		return null;

	}

	/**
	 * Hank Added 2007/11/26 releaseBpNoMpWithCharacter
	 * 
	 * @param md
	 * @param userTo
	 * @return
	 */
	public static String releaseBpfICWaferWithCharacter(final Object md,
			final UserTo userTo) {
		IcWaferTo icWaferTo = (IcWaferTo) md;
		List bumpMask = new ArrayList();

		String bumpMaterialNum = "B"
				+ icWaferTo.getMaterialNum().substring(1,
						icWaferTo.getMaterialNum().length());
		String mdStr = "BumpMask";
		String materialType = ERPHelper.getMaterialType(mdStr);
		if (materialType == null || materialType.length() <= 0) {
			return "ERP-02-014";
		}

		ProjectDao projectDao = new ProjectDao();
		ProjectTo projectTo = projectDao.findByProjectCode(icWaferTo
				.getProjCode());

		if (projectTo == null) {
			return "ERP-02-015";
		}
		String prodFamily = projectTo.getProdFamily();
		if (prodFamily == null || prodFamily.length() <= 0) {
			return "ERP-02-016";
		}

		String materialGroup = ERPHelper.getMaterialGroup(materialType,
				prodFamily);
		if (materialGroup == null || materialGroup.length() <= 0) {
			return "ERP-02-017";
		}
		// 取得release_to的值
		ProjectCodeTo projectCodeTo = new ProjectCodeDao()
				.findByProjectCode(icWaferTo.getProjCode());

		IfMaterialMasterDao ifMaterialMasterDao = new IfMaterialMasterDao();
		IfMaterialMasterTo ifMaterialMasterTo = new IfMaterialMasterTo();
		ifMaterialMasterTo.setMaterialNum(bumpMaterialNum);
		ifMaterialMasterTo.setMaterialType(materialType);
		ifMaterialMasterTo.setMaterialDesc(icWaferTo.getMaterialDesc());
		ifMaterialMasterTo.setMaterialGroup(materialGroup);
		ifMaterialMasterTo
				.setPurchaseOrderText(icWaferTo.getProjCodeWVersion());
		ifMaterialMasterTo.setTimeStamp(new Date());
		ifMaterialMasterTo.setId(SequenceSupport
				.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
		ifMaterialMasterTo.setReleasedBy(userTo.getUserId());
		// 將Release_To 塞到 pidb_if_material_master Table中
		ifMaterialMasterTo.setReleaseTo(projectCodeTo.getReleaseTo());
		// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
		ifMaterialMasterTo.setMpStatus(ERPHelper
				.judgeMPByProjWVersion(icWaferTo.getProjCodeWVersion()));
		ifMaterialMasterDao.insert(ifMaterialMasterTo,
				"PIDB_IF_MATERIAL_MASTER");

		// 1.0 Insert Character
		IfMaterialCharacterDao ifMaterialCharacterDao = new IfMaterialCharacterDao();
		IfMaterialCharacterTo ifMaterialCharacterTo = new IfMaterialCharacterTo();
		ifMaterialCharacterTo.setMaterialNum(bumpMaterialNum);
		ifMaterialCharacterTo.setMaterialType(materialType);
		ifMaterialCharacterTo.setTimeStamp(new Date());
		ifMaterialCharacterTo.setId(SequenceSupport
				.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
		ifMaterialCharacterTo.setReleasedBy(userTo.getUserId());
		// 將Release_To 塞到 pidb_if_material_character Table中
		ifMaterialCharacterTo.setReleaseTo(projectCodeTo.getReleaseTo());

		String waferInch = projectTo.getWaferInch();
		if (waferInch == null || waferInch.length() <= 0) {
			// return "ERP-02-018";
		} else {
			ifMaterialCharacterTo.setChTechName("BP_01");
			ifMaterialCharacterTo.setChValue(waferInch);
			ifMaterialCharacterDao.insert(ifMaterialCharacterTo);
		}
		String grossDie = projectTo.getGrossDie();
		if (grossDie == null || grossDie.length() <= 0) {
			// return "ERP-02-019";
		} else {
			ifMaterialCharacterTo.setChTechName("BP_03");
			ifMaterialCharacterTo.setChValue(grossDie);
			ifMaterialCharacterTo.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
			ifMaterialCharacterDao.insert(ifMaterialCharacterTo);
		}

		BumpMaskDao bumpMaskDao = new BumpMaskDao();
		bumpMask = bumpMaskDao.getByProjCode(icWaferTo.getProjCode());

		if (bumpMask != null && bumpMask.size() > 0) {

			bumpMask = null != bumpMask ? bumpMask : new ArrayList();
			BumpMaskTo bumpMaskTo = (BumpMaskTo) bumpMask.get(0);
			String rdl = bumpMaskTo.getRdl();
			if (rdl == null || rdl.length() <= 0) {

			} else {
				ifMaterialCharacterTo.setChTechName("BP_02");
				ifMaterialCharacterTo.setChValue(bumpMaskTo.getRdl());
				ifMaterialCharacterTo.setId(SequenceSupport
						.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
				// 將Release_To 塞到 pidb_if_material_character Table中
				ifMaterialCharacterTo
						.setReleaseTo(projectCodeTo.getReleaseTo());
				ifMaterialCharacterDao.insert(ifMaterialCharacterTo);
			}
		}

		return null;
	}

	public static String releaseBpNoMp(final Object md, final UserTo userTo) {
		IcWaferTo icWaferTo = (IcWaferTo) md;
		String bumpMaterialNum = "B"
				+ icWaferTo.getMaterialNum().substring(1,
						icWaferTo.getMaterialNum().length());
		String mdStr = "BumpMask";
		String materialType = ERPHelper.getMaterialType(mdStr);
		if (materialType == null || materialType.length() <= 0) {
			return "ERP-02-014";
		}

		ProjectDao projectDao = new ProjectDao();
		ProjectTo projectTo = projectDao.findByProjectCode(icWaferTo
				.getProjCode());
		if (projectTo == null) {
			return "ERP-02-015";
		}
		String prodFamily = projectTo.getProdFamily();
		if (prodFamily == null || prodFamily.length() <= 0) {
			return "ERP-02-016";
		}

		String materialGroup = ERPHelper.getMaterialGroup(materialType,
				prodFamily);
		if (materialGroup == null || materialGroup.length() <= 0) {
			return "ERP-02-017";
		}

		// 取得release_to的值
		ProjectCodeTo projectCodeTo = new ProjectCodeDao()
				.findByProjectCode(icWaferTo.getProjCode());

		IfMaterialMasterDao ifMaterialMasterDao = new IfMaterialMasterDao();
		IfMaterialMasterTo ifMaterialMasterTo = new IfMaterialMasterTo();
		ifMaterialMasterTo.setMaterialNum(bumpMaterialNum);
		ifMaterialMasterTo.setMaterialType(materialType);
		ifMaterialMasterTo.setMaterialDesc(icWaferTo.getMaterialDesc());
		ifMaterialMasterTo.setMaterialGroup(materialGroup);
		ifMaterialMasterTo
				.setPurchaseOrderText(icWaferTo.getProjCodeWVersion());
		ifMaterialMasterTo.setTimeStamp(new Date());
		ifMaterialMasterTo.setId(SequenceSupport
				.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
		ifMaterialMasterTo.setReleasedBy(userTo.getUserId());
		// 將Release_To 塞到 pidb_if_material_master Table中
		ifMaterialMasterTo.setReleaseTo(projectCodeTo.getReleaseTo());
		// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
		ifMaterialMasterTo.setMpStatus(ERPHelper
				.judgeMPByProjWVersion(icWaferTo.getProjCodeWVersion()));
		ifMaterialMasterDao.insert(ifMaterialMasterTo,
				"PIDB_IF_MATERIAL_MASTER");

		return null;
	}

	public static String releaseCpNoMp(final Object md, final UserTo userTo) {
		IcWaferTo icWaferTo = (IcWaferTo) md;
		IfMaterialMasterTo ifMaterialMasterTo = new IfMaterialMasterTo();
		IfMaterialMasterDao ifMaterialMasterDao = new IfMaterialMasterDao();
		ProjectDao projectDao = new ProjectDao();

		String materialNum = "C" + icWaferTo.getMaterialNum().substring(1);

		String mdStr = "CpMaterial.2";
		String materialType = ERPHelper.getMaterialType(mdStr);
		if (materialType == null || materialType.length() <= 0) {
			return "ERP-02-022";
		}

		// 取得release_to的值
		ProjectCodeTo projectCodeTo = new ProjectCodeDao()
				.findByProjectCode(icWaferTo.getProjCode());

		ifMaterialMasterTo.setMaterialNum(materialNum);
		ifMaterialMasterTo.setMaterialType(materialType);
		ifMaterialMasterTo.setMaterialDesc(icWaferTo.getMaterialDesc());
		ifMaterialMasterTo
				.setPurchaseOrderText(icWaferTo.getProjCodeWVersion());
		ifMaterialMasterTo.setTimeStamp(new Date());
		ifMaterialMasterTo.setId(SequenceSupport
				.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
		ifMaterialMasterTo.setReleasedBy(userTo.getUserId());
		// 將Release_To 塞到 pidb_if_material_master Table中
		ifMaterialMasterTo.setReleaseTo(projectCodeTo.getReleaseTo());

		ProjectTo projectTo = projectDao.findByProjectCode(icWaferTo
				.getProjCode());
		String materialGroup = ERPHelper.getMaterialGroup(materialType,
				projectTo.getProdFamily());
		// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
		ifMaterialMasterTo.setMpStatus(ERPHelper
				.judgeMPByProjWVersion(icWaferTo.getProjCodeWVersion()));
		if (materialGroup != null || materialGroup.length() > 0) {
			ifMaterialMasterTo.setMaterialGroup(materialGroup);
			ifMaterialMasterDao.insert(ifMaterialMasterTo,
					"PIDB_IF_MATERIAL_MASTER");
		} else {
			return "ERP-02-016";
		}

		return null;
	}

	/**
	 * Added 2009/02/18 releasePolishICWafer
	 * 
	 * @param md
	 * @param userTo
	 * @return
	 */
	public static String releaseCpMaterialICWafer(final Object md,
			final UserTo userTo, final String className) {
		IcWaferTo icWaferTo = (IcWaferTo) md;
		IfMaterialMasterTo ifMaterialMasterTo = new IfMaterialMasterTo();
		IfMaterialMasterDao ifMaterialMasterDao = new IfMaterialMasterDao();
		ProjectDao projectDao = new ProjectDao();
		ProjectTo projectTo = new ProjectTo();

		CpMaterialDao cpMaterialDao = new CpMaterialDao();

		String materialNum = "C" + icWaferTo.getMaterialNum().substring(1);
		// Find Out CP Test Program
		List<CpMaterialTo> cpMaterialTo = cpMaterialDao
				.getByProjCodeWVersion(icWaferTo.getProjCodeWVersion());

		// 取得release_to的值
		ProjectCodeTo projectCodeTo = new ProjectCodeDao()
				.findByProjectCode(icWaferTo.getProjCode());

		String mdStr = "CpMaterial.2";
		String materialType = ERPHelper.getMaterialType(mdStr);
		if (materialType == null || materialType.length() <= 0) {
			return "ERP-02-022";
		}
		for (CpMaterialTo cm : cpMaterialTo) {
			ifMaterialMasterTo.setMaterialNum(cm.getCpMaterialNum());
			ifMaterialMasterTo.setMaterialType(materialType);
			if (cm.getDescription() != null && !cm.getDescription().equals("")) {
				ifMaterialMasterTo.setMaterialDesc(cm.getDescription());
			} else {
				ifMaterialMasterTo.setMaterialDesc(icWaferTo
						.getProjCodeWVersion());
			}
			ifMaterialMasterTo.setPurchaseOrderText(icWaferTo
					.getProjCodeWVersion());
			ifMaterialMasterTo.setTimeStamp(new Date());
			ifMaterialMasterTo.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
			ifMaterialMasterTo.setReleasedBy(userTo.getUserId());
			// 將Release_To 塞到 pidb_if_material_master Table中
			ifMaterialMasterTo.setReleaseTo(projectCodeTo.getReleaseTo());

			projectTo = projectDao.findByProjectCode(icWaferTo.getProjCode());
			String materialGroup = ERPHelper.getMaterialGroup(materialType,
					projectTo.getProdFamily());
			// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
			ifMaterialMasterTo.setMpStatus(ERPHelper
					.judgeMPByProjWVersion(icWaferTo.getProjCodeWVersion()));
			if (materialGroup != null || materialGroup.length() > 0) {
				ifMaterialMasterTo.setMaterialGroup(materialGroup);
				ifMaterialMasterDao.insert(ifMaterialMasterTo,
						"PIDB_IF_MATERIAL_MASTER");
			} else {
				return "ERP-02-016";
			}
		}
		return null;
	}

	/**
	 * Added 2009/02/18 releasePolishICWafer
	 * 
	 * @param md
	 * @param userTo
	 * @return
	 */
	public static String releasePolishICWafer(final Object md,
			final UserTo userTo, final String className) {
		IcWaferTo icWaferTo = (IcWaferTo) md;
		IfMaterialMasterTo ifMaterialMasterTo = new IfMaterialMasterTo();
		IfMaterialMasterDao ifMaterialMasterDao = new IfMaterialMasterDao();
		
		IfMaterialCharacterDao ifMaterialCharacterDao = new IfMaterialCharacterDao();
		IfMaterialCharacterTo ifMaterialCharacterTo = new IfMaterialCharacterTo();
				
		ProjectDao projectDao = new ProjectDao();
		ProjectTo projectTo = new ProjectTo();

		CpPolishMaterialDao polishDao = new CpPolishMaterialDao();

		String materialNum = "C" + icWaferTo.getMaterialNum().substring(1);
		// Find Out CP Test Program
		List<CpPolishMaterialTo> polishTo = polishDao
				.findByProjectCode(icWaferTo.getProjCodeWVersion());

		// 取得release_to的值
		ProjectCodeTo projectCodeTo = new ProjectCodeDao()
				.findByProjectCode(icWaferTo.getProjCode());

		String mdStr = "CpMaterial.2";
		String materialType = ERPHelper.getMaterialType(mdStr);
		if (materialType == null || materialType.length() <= 0) {
			return "ERP-02-022";
		}
		for (CpPolishMaterialTo cpm : polishTo) {
			ifMaterialMasterTo.setMaterialNum(cpm.getCpPolishMaterialNum());
			ifMaterialMasterTo.setMaterialType(materialType);
			if (cpm.getDescription() != null
					&& !cpm.getDescription().equals("")) {
				ifMaterialMasterTo.setMaterialDesc(cpm.getDescription());
			} else {
				ifMaterialMasterTo.setMaterialDesc(icWaferTo
						.getProjCodeWVersion());
			}
			ifMaterialMasterTo.setPurchaseOrderText(icWaferTo
					.getProjCodeWVersion());
			ifMaterialMasterTo.setTimeStamp(new Date());
			ifMaterialMasterTo.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
			ifMaterialMasterTo.setReleasedBy(userTo.getUserId());

			// 將Release_To 塞到 pidb_if_material_master Table中
			ifMaterialMasterTo.setReleaseTo(projectCodeTo.getReleaseTo());

			projectTo = projectDao.findByProjectCode(icWaferTo.getProjCode());
			String materialGroup = ERPHelper.getMaterialGroup(materialType,
					projectTo.getProdFamily());
			// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
			ifMaterialMasterTo.setMpStatus(ERPHelper
					.judgeMPByProjWVersion(icWaferTo.getProjCodeWVersion()));
			if (materialGroup != null || materialGroup.length() > 0) {
				ifMaterialMasterTo.setMaterialGroup(materialGroup);
				ifMaterialMasterDao.insert(ifMaterialMasterTo,
						"PIDB_IF_MATERIAL_MASTER");
			} else {
				return "ERP-02-016";
			}
		}
		
		for (CpPolishMaterialTo cpm : polishTo) {
			// 1.0 Insert Character
						
			ifMaterialCharacterTo.setMaterialNum(cpm.getCpPolishMaterialNum());
			ifMaterialCharacterTo.setMaterialType(materialType);
			ifMaterialCharacterTo.setTimeStamp(new Date());
			ifMaterialCharacterTo.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
			// 將Release_To 塞到 pidb_if_material_character Table中
			ifMaterialCharacterTo.setReleaseTo(projectCodeTo.getReleaseTo());
			ifMaterialCharacterTo.setReleasedBy(userTo.getUserId());

			ifMaterialCharacterTo.setChTechName("CP_04");
			ifMaterialCharacterTo.setChValue("polish");
			ifMaterialCharacterDao.insert(ifMaterialCharacterTo);
			
		}
		return null;
	}
	
	/**
	 * Added 2009/02/18 CSP Release to SAP
	 * 
	 * @param md
	 * @param userTo
	 * @return
	 */
	public static String releaseCpCspICWafer(final Object md,
			final UserTo userTo, final String className) {
		IcWaferTo icWaferTo = (IcWaferTo) md;
		IfMaterialMasterTo ifMaterialMasterTo = new IfMaterialMasterTo();
		IfMaterialMasterDao ifMaterialMasterDao = new IfMaterialMasterDao();
		
		IfMaterialCharacterDao ifMaterialCharacterDao = new IfMaterialCharacterDao();
		IfMaterialCharacterTo ifMaterialCharacterTo = new IfMaterialCharacterTo();
				
		ProjectDao projectDao = new ProjectDao();
		ProjectTo projectTo = new ProjectTo();

		CpCspMaterialDao cspDao = new CpCspMaterialDao();

		// Find Out CP Test Program
		List<CpCspMaterialTo> cspTo = cspDao.findByProjectCode(icWaferTo.getProjCodeWVersion());

		// 取得release_to的值
		ProjectCodeTo projectCodeTo = new ProjectCodeDao()
				.findByProjectCode(icWaferTo.getProjCode());

		String materialType = "ZCSP";
		if (materialType == null || materialType.length() <= 0) {
			return "ERP-02-022";
		}
		for (CpCspMaterialTo csp : cspTo) {
			ifMaterialMasterTo.setMaterialNum(csp.getCpCspMaterialNum());
			ifMaterialMasterTo.setMaterialType(materialType);
			if (csp.getDescription() != null
					&& !csp.getDescription().equals("")) {
				ifMaterialMasterTo.setMaterialDesc(csp.getDescription());
			} else {
				ifMaterialMasterTo.setMaterialDesc(icWaferTo
						.getProjCodeWVersion());
			}
			ifMaterialMasterTo.setPurchaseOrderText(icWaferTo
					.getProjCodeWVersion());
			ifMaterialMasterTo.setTimeStamp(new Date());
			ifMaterialMasterTo.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
			ifMaterialMasterTo.setReleasedBy(userTo.getUserId());

			// 將Release_To 塞到 pidb_if_material_master Table中
			ifMaterialMasterTo.setReleaseTo(projectCodeTo.getReleaseTo());

			projectTo = projectDao.findByProjectCode(icWaferTo.getProjCode());
			String materialGroup = "09120";
			// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
			ifMaterialMasterTo.setMpStatus(ERPHelper
					.judgeMPByProjWVersion(icWaferTo.getProjCodeWVersion()));
			if (materialGroup != null || materialGroup.length() > 0) {
				ifMaterialMasterTo.setMaterialGroup(materialGroup);
				ifMaterialMasterDao.insert(ifMaterialMasterTo,
						"PIDB_IF_MATERIAL_MASTER");
			} else {
				return "ERP-02-016";
			}
		}
		
		//CSP 特性值
		/*for (CpCspMaterialTo csp : cspTo) {
			// 1.0 Insert Character
						
			ifMaterialCharacterTo.setMaterialNum(csp.getCpCspMaterialNum());
			ifMaterialCharacterTo.setMaterialType(materialType);
			ifMaterialCharacterTo.setTimeStamp(new Date());
			ifMaterialCharacterTo.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
			// 將Release_To 塞到 pidb_if_material_character Table中
			ifMaterialCharacterTo.setReleaseTo(projectCodeTo.getReleaseTo());
			ifMaterialCharacterTo.setReleasedBy(userTo.getUserId());

			ifMaterialCharacterTo.setChTechName("CP_04");
			ifMaterialCharacterTo.setChValue("CSP");
			ifMaterialCharacterDao.insert(ifMaterialCharacterTo);
			
		}*/
		
		
		//CSP DS
		String projName = icWaferTo.getProjCode().substring(0, 6);
		materialType = "ZDS";
		if (materialType == null || materialType.length() <= 0) {
			return "ERP-02-022";
		}
		
		for (CpCspMaterialTo csp : cspTo) {
			ifMaterialMasterTo.setMaterialNum("D" +csp.getCpCspMaterialNum().substring(1));

			ifMaterialMasterTo.setMaterialType(materialType);
			if (csp.getDescription() != null
					&& !csp.getDescription().equals("")) {
				ifMaterialMasterTo.setMaterialDesc(csp.getDescription());
			} else {
				ifMaterialMasterTo.setMaterialDesc(icWaferTo
						.getProjCodeWVersion());
			}
			ifMaterialMasterTo.setPurchaseOrderText(icWaferTo
					.getProjCodeWVersion());
			ifMaterialMasterTo.setTimeStamp(new Date());
			ifMaterialMasterTo.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
			ifMaterialMasterTo.setReleasedBy(userTo.getUserId());

			// 將Release_To 塞到 pidb_if_material_master Table中
			ifMaterialMasterTo.setReleaseTo(projectCodeTo.getReleaseTo());

			projectTo = projectDao.findByProjectCode(icWaferTo.getProjCode());
			String materialGroup = "10120";
			// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
			ifMaterialMasterTo.setMpStatus(ERPHelper
					.judgeMPByProjWVersion(icWaferTo.getProjCodeWVersion()));
			if (materialGroup != null || materialGroup.length() > 0) {
				ifMaterialMasterTo.setMaterialGroup(materialGroup);
				ifMaterialMasterDao.insert(ifMaterialMasterTo,
						"PIDB_IF_MATERIAL_MASTER");
			} else {
				return "ERP-02-016";
			}
			
			/*
			// 2.0 Set Character
			// if_material_char_to same parameters
			ifMaterialCharacterTo.setTimeStamp(new Date());
			// ifMaterialCharacterTo.setId(SequenceSupport.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
			ifMaterialCharacterTo.setReleasedBy(userTo.getUserId());
			ifMaterialCharacterTo.setMaterialType(materialType);
			ifMaterialCharacterTo.setMaterialNum("D" +csp.getCpCspMaterialNum().substring(1));
			// 將Release_To 塞到 pidb_if_material_master Table中
			ifMaterialCharacterTo.setReleaseTo(projectCodeTo.getReleaseTo());
			// insert if_material_char and get all prodFamily
			List<String> prodFamilyList = new ArrayList<String>();

			projectTo = projectDao.findByProjName(projName);
			if (projectTo == null) {
				return "ERP-02-038";
			}
			if (StringUtils.isEmpty(projectTo.getProdFamily())) {
				return "ERP-02-039";
			}
			if (projectTo != null
					&& StringUtils.isNotEmpty(projectTo.getProdFamily())
					&& !prodFamilyList.contains(projectTo.getProdFamily())) {
				prodFamilyList.add(projectTo.getProdFamily());
			}
			if (StringUtils.isEmpty(projectTo.getGrossDie())) {
				// return "ERP-02-040";
			} else {
				ifMaterialCharacterTo.setChTechName("DS_01");
				ifMaterialCharacterTo.setChValue(projectTo.getGrossDie());
				ifMaterialCharacterTo.setId(SequenceSupport
						.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
				ifMaterialCharacterDao.insert(ifMaterialCharacterTo);
			}

			if (StringUtils.isEmpty(projectTo.getFcstCpYield())) {
				// return "ERP-02-041";
			} else {
				ifMaterialCharacterTo.setChTechName("DS_02");
				ifMaterialCharacterTo.setChValue(projectTo.getFcstCpYield());
				ifMaterialCharacterTo.setId(SequenceSupport
						.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
				ifMaterialCharacterDao.insert(ifMaterialCharacterTo);
			}
			*/
		}
		
		return null;
	}

	
	/**
	 * Added 2010/07/28 TSV Release to SAP
	 * 
	 * @param md
	 * @param userTo
	 * @return
	 */
	public static String releaseCpTsvICWafer(final Object md,
			final UserTo userTo, final String className) {
		IcWaferTo icWaferTo = (IcWaferTo) md;
		IfMaterialMasterTo ifMaterialMasterTo = new IfMaterialMasterTo();
		IfMaterialMasterDao ifMaterialMasterDao = new IfMaterialMasterDao();
				
		ProjectDao projectDao = new ProjectDao();
		ProjectTo projectTo = new ProjectTo();

		CpTsvMaterialDao tsvDao = new CpTsvMaterialDao();

		// Find Out CP Test Program
		List<CpTsvMaterialTo> tsvList = tsvDao.findByProjectCode(icWaferTo.getProjCodeWVersion());

		// 取得release_to的值
		ProjectCodeTo projectCodeTo = new ProjectCodeDao()
				.findByProjectCode(icWaferTo.getProjCode());

		String materialType = "ZTSV";
		if (materialType == null || materialType.length() <= 0) {
			return "ERP-02-022";
		}
		for (CpTsvMaterialTo tsv : tsvList) {
			ifMaterialMasterTo.setMaterialNum(tsv.getCpTsvMaterialNum());
			ifMaterialMasterTo.setMaterialType(materialType);
			if (tsv.getDescription() != null
					&& !tsv.getDescription().equals("")) {
				ifMaterialMasterTo.setMaterialDesc(tsv.getDescription());
			} else {
				ifMaterialMasterTo.setMaterialDesc(icWaferTo
						.getProjCodeWVersion());
			}
			ifMaterialMasterTo.setPurchaseOrderText(icWaferTo
					.getProjCodeWVersion());
			ifMaterialMasterTo.setTimeStamp(new Date());
			ifMaterialMasterTo.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
			ifMaterialMasterTo.setReleasedBy(userTo.getUserId());

			// 將Release_To 塞到 pidb_if_material_master Table中
			ifMaterialMasterTo.setReleaseTo(projectCodeTo.getReleaseTo());

			projectTo = projectDao.findByProjectCode(icWaferTo.getProjCode());
			String materialGroup = "09120";
			// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
			ifMaterialMasterTo.setMpStatus(ERPHelper
					.judgeMPByProjWVersion(icWaferTo.getProjCodeWVersion()));
			if (materialGroup != null || materialGroup.length() > 0) {
				ifMaterialMasterTo.setMaterialGroup(materialGroup);
				ifMaterialMasterDao.insert(ifMaterialMasterTo,
						"PIDB_IF_MATERIAL_MASTER");
			} else {
				return "ERP-02-016";
			}
		}
		
		//CSP DS

		materialType = "ZDS";
		if (materialType == null || materialType.length() <= 0) {
			return "ERP-02-022";
		}
		
		for (CpTsvMaterialTo tsv : tsvList) {
			ifMaterialMasterTo.setMaterialNum("D" +tsv.getCpTsvMaterialNum().substring(1));

			ifMaterialMasterTo.setMaterialType(materialType);
			if (tsv.getDescription() != null
					&& !tsv.getDescription().equals("")) {
				ifMaterialMasterTo.setMaterialDesc(tsv.getDescription());
			} else {
				ifMaterialMasterTo.setMaterialDesc(icWaferTo
						.getProjCodeWVersion());
			}
			ifMaterialMasterTo.setPurchaseOrderText(icWaferTo
					.getProjCodeWVersion());
			ifMaterialMasterTo.setTimeStamp(new Date());
			ifMaterialMasterTo.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
			ifMaterialMasterTo.setReleasedBy(userTo.getUserId());

			// 將Release_To 塞到 pidb_if_material_master Table中
			ifMaterialMasterTo.setReleaseTo(projectCodeTo.getReleaseTo());

			projectTo = projectDao.findByProjectCode(icWaferTo.getProjCode());
			String materialGroup = "10120";
			// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
			ifMaterialMasterTo.setMpStatus(ERPHelper
					.judgeMPByProjWVersion(icWaferTo.getProjCodeWVersion()));
			if (materialGroup != null || materialGroup.length() > 0) {
				ifMaterialMasterTo.setMaterialGroup(materialGroup);
				ifMaterialMasterDao.insert(ifMaterialMasterTo,
						"PIDB_IF_MATERIAL_MASTER");
			} else {
				return "ERP-02-016";
			}

		}
		
		return null;
	}
	/**
	 * Added 2009/02/25 releaseColorFilterICWafer
	 * 
	 * @param md
	 * @param userTo
	 * @return
	 */
	public static String releaseColorFilterICWafer(final Object md,
			final UserTo userTo, final String className) {
		IcWaferTo icWaferTo = (IcWaferTo) md;
		IfMaterialMasterTo ifMaterialMasterTo = new IfMaterialMasterTo();
		IfMaterialMasterDao ifMaterialMasterDao = new IfMaterialMasterDao();
		// ProjectDao projectDao = new ProjectDao();
		// ProjectTo projectTo = new ProjectTo();

		ColorFilterMaterialDao colorFilterDao = new ColorFilterMaterialDao();

		String materialNum = "M" + icWaferTo.getMaterialNum().substring(1);
		// Find Out CP Test Program
		List<ColorFilterMaterialTo> colorFilterTo = colorFilterDao
				.getByProjCodeWVersion(icWaferTo.getProjCodeWVersion());

		String mdStr = "WF_ML";
		String materialType = ERPHelper.getMaterialType(mdStr);

		if (materialType == null || materialType.length() <= 0) {
			return "ERP-02-022";
		}

		String projCode = icWaferTo.getProjCode();
		System.out.println("projCode= " + projCode);

		ProjectCodeDao projectCodeDao = new ProjectCodeDao();
		ProjectCodeTo projectCodeTo = projectCodeDao
				.findByProjectCode(projCode);
		String errorWaferInch = "ERP-02-004";
		if (projectCodeTo == null) {
			return errorWaferInch;
		}
		ProjectDao projectDao = new ProjectDao();
		ProjectTo projectTo = projectDao.find(projectCodeTo.getProjName());

		if (projectTo == null) {
			return errorWaferInch;
		}
		if (projectTo.getWaferInch() == null
				|| projectTo.getWaferInch().length() <= 0) {
			return errorWaferInch;
		}
		if (projectTo.getProcTech() == null
				|| projectTo.getProcTech().length() <= 0) {
			return "ERP-02-005";
		}
		if (projectTo.getPolyMetalLayers() == null
				|| projectTo.getPolyMetalLayers().length() <= 0) {
			return "ERP-02-006";
		}
		if (projectTo.getVoltage() == null
				|| projectTo.getVoltage().length() <= 0) {
			return "ERP-02-007";
		}
		if (projectTo.getProcLayerNo() == null
				|| projectTo.getProcLayerNo().length() <= 0) {
			return "ERP-02-008";
		}

		for (ColorFilterMaterialTo cfm : colorFilterTo) {
			ifMaterialMasterTo.setMaterialNum(cfm.getColorFilterMaterialNum());
			ifMaterialMasterTo.setMaterialType(materialType);
			if (cfm.getDescription() != null
					&& !cfm.getDescription().equals("")) {
				ifMaterialMasterTo.setMaterialDesc(cfm.getDescription());
			} else {
				ifMaterialMasterTo.setMaterialDesc(icWaferTo
						.getProjCodeWVersion());
			}
			ifMaterialMasterTo.setPurchaseOrderText(icWaferTo
					.getProjCodeWVersion());
			ifMaterialMasterTo.setTimeStamp(new Date());
			ifMaterialMasterTo.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
			ifMaterialMasterTo.setReleasedBy(userTo.getUserId());
			// 將Release_To 塞到 pidb_if_material_master Table中
			ifMaterialMasterTo.setReleaseTo(projectCodeTo.getReleaseTo());

			// set materilGroup
			String waferInchParam = projectTo.getWaferInch().substring(0,
					projectTo.getWaferInch().length() - 1);
			String materialGroup = ERPHelper.getMaterialGroup(materialType,
					waferInchParam);
			ifMaterialMasterTo.setMaterialGroup(materialGroup);

			/*
			 * projectTo =
			 * projectDao.findByProjectCode(icWaferTo.getProjCode()); String
			 * materialGroup = ERPHelper.getMaterialGroup(materialType,
			 * projectTo.getProdFamily());
			 */
			// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
			ifMaterialMasterTo.setMpStatus(ERPHelper
					.judgeMPByProjWVersion(icWaferTo.getProjCodeWVersion()));
			if (materialGroup != null || materialGroup.length() > 0) {
				ifMaterialMasterTo.setMaterialGroup(materialGroup);
				ifMaterialMasterDao.insert(ifMaterialMasterTo,
						"PIDB_IF_MATERIAL_MASTER");
			} else {
				return "ERP-02-016";
			}
		}
		return null;
	}

	/**
	 * Added 2009/02/25 releaseColorFilterICWafer
	 * 
	 * @param md
	 * @param userTo
	 * @return
	 */
	public static String releaseWaferCFICWafer(final Object md,
			final UserTo userTo, final String className) {
		IcWaferTo icWaferTo = (IcWaferTo) md;
		IfMaterialMasterTo ifMaterialMasterTo = new IfMaterialMasterTo();
		IfMaterialMasterDao ifMaterialMasterDao = new IfMaterialMasterDao();
		/*
		 * ProjectDao projectDao = new ProjectDao(); ProjectTo projectTo = new
		 * ProjectTo();
		 */

		WaferColorFilterDao waferCFDao = new WaferColorFilterDao();

		String materialNum = "T" + icWaferTo.getMaterialNum().substring(1);
		// Find Out CP Test Program
		List<WaferColorFilterTo> waferCFTo = waferCFDao
				.getByProjCodeWVersion(icWaferTo.getProjCodeWVersion());

		String mdStr = "IcWafer";

		String materialType = ERPHelper.getMaterialType(mdStr);

		if (materialType == null || materialType.length() <= 0) {
			return "ERP-02-022";
		}

		String projCode = icWaferTo.getProjCode();

		ProjectCodeDao projectCodeDao = new ProjectCodeDao();
		ProjectCodeTo projectCodeTo = projectCodeDao
				.findByProjectCode(projCode);
		String errorWaferInch = "ERP-02-004";
		if (projectCodeTo == null) {
			return errorWaferInch;
		}
		ProjectDao projectDao = new ProjectDao();
		ProjectTo projectTo = projectDao.find(projectCodeTo.getProjName());

		if (projectTo == null) {
			return errorWaferInch;
		}
		if (projectTo.getWaferInch() == null
				|| projectTo.getWaferInch().length() <= 0) {
			return errorWaferInch;
		}
		if (projectTo.getProcTech() == null
				|| projectTo.getProcTech().length() <= 0) {
			return "ERP-02-005";
		}
		if (projectTo.getPolyMetalLayers() == null
				|| projectTo.getPolyMetalLayers().length() <= 0) {
			return "ERP-02-006";
		}
		if (projectTo.getVoltage() == null
				|| projectTo.getVoltage().length() <= 0) {
			return "ERP-02-007";
		}
		if (projectTo.getProcLayerNo() == null
				|| projectTo.getProcLayerNo().length() <= 0) {
			return "ERP-02-008";
		}

		for (WaferColorFilterTo waferCF : waferCFTo) {
			ifMaterialMasterTo.setMaterialNum(waferCF.getWaferCfMaterialNum());
			ifMaterialMasterTo.setMaterialType(materialType);
			if (waferCF.getDescription() != null
					&& !waferCF.getDescription().equals("")) {
				ifMaterialMasterTo.setMaterialDesc(waferCF.getDescription());
			} else {
				ifMaterialMasterTo.setMaterialDesc(icWaferTo
						.getProjCodeWVersion());
			}
			ifMaterialMasterTo.setPurchaseOrderText(icWaferTo
					.getProjCodeWVersion());
			ifMaterialMasterTo.setTimeStamp(new Date());
			ifMaterialMasterTo.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
			ifMaterialMasterTo.setReleasedBy(userTo.getUserId());
			// 將Release_To 塞到 pidb_if_material_master Table中
			ifMaterialMasterTo.setReleaseTo(projectCodeTo.getReleaseTo());

			// set materilGroup
			String waferInchParam = projectTo.getWaferInch().substring(0,
					projectTo.getWaferInch().length() - 1);
			String materialGroup = ERPHelper.getMaterialGroup(materialType,
					waferInchParam);
			ifMaterialMasterTo.setMaterialGroup(materialGroup);

			/*
			 * projectTo =
			 * projectDao.findByProjectCode(icWaferTo.getProjCode()); String
			 * materialGroup = ERPHelper.getMaterialGroup(materialType,
			 * projectTo.getProdFamily());
			 */
			// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
			ifMaterialMasterTo.setMpStatus(ERPHelper
					.judgeMPByProjWVersion(icWaferTo.getProjCodeWVersion()));
			if (materialGroup != null || materialGroup.length() > 0) {
				ifMaterialMasterTo.setMaterialGroup(materialGroup);
				ifMaterialMasterDao.insert(ifMaterialMasterTo,
						"PIDB_IF_MATERIAL_MASTER");
			} else {
				return "ERP-02-016";
			}
		}
		return null;
	}

	/**
	 * Hank Added 2007/11/26 releaseCpNoMpWithCharacter
	 * 
	 * @param md
	 * @param userTo
	 * @return
	 */
	public static String releaseCpfICWaferWithCharacter(final Object md,
			final UserTo userTo, final String className) {
		IcWaferTo icWaferTo = (IcWaferTo) md;
		IfMaterialMasterTo ifMaterialMasterTo = new IfMaterialMasterTo();
		IfMaterialMasterDao ifMaterialMasterDao = new IfMaterialMasterDao();
		ProjectDao projectDao = new ProjectDao();
		ProjectTo projectTo = new ProjectTo();

		CpTestProgramDao cpTestProgramDao = new CpTestProgramDao();
		CpMaterialDao cpMaterialDao = new CpMaterialDao();

		String materialNum = "C" + icWaferTo.getMaterialNum().substring(1);
		// Find Out CP Test Program
		CpMaterialTo cpMaterialTo = cpMaterialDao
				.findByCpMaterialNum(materialNum);

		// 取得release_to的值
		ProjectCodeTo projectCodeTo = new ProjectCodeDao()
				.findByProjectCode(icWaferTo.getProjCode());

		String mdStr = "CpMaterial.2";
		String materialType = ERPHelper.getMaterialType(mdStr);
		if (materialType == null || materialType.length() <= 0) {
			return "ERP-02-022";
		}

		ifMaterialMasterTo.setMaterialNum(materialNum);
		ifMaterialMasterTo.setMaterialType(materialType);
		ifMaterialMasterTo.setMaterialDesc(icWaferTo.getMaterialDesc());
		ifMaterialMasterTo
				.setPurchaseOrderText(icWaferTo.getProjCodeWVersion());
		ifMaterialMasterTo.setTimeStamp(new Date());
		ifMaterialMasterTo.setId(SequenceSupport
				.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
		ifMaterialMasterTo.setReleasedBy(userTo.getUserId());
		// 將Release_To 塞到 pidb_if_material_master Table中
		ifMaterialMasterTo.setReleaseTo(projectCodeTo.getReleaseTo());

		projectTo = projectDao.findByProjectCode(icWaferTo.getProjCode());
		String materialGroup = ERPHelper.getMaterialGroup(materialType,
				projectTo.getProdFamily());
		// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
		ifMaterialMasterTo.setMpStatus(ERPHelper
				.judgeMPByProjWVersion(icWaferTo.getProjCodeWVersion()));
		if (materialGroup != null || materialGroup.length() > 0) {
			ifMaterialMasterTo.setMaterialGroup(materialGroup);
			ifMaterialMasterDao.insert(ifMaterialMasterTo,
					"PIDB_IF_MATERIAL_MASTER");
		} else {
			return "ERP-02-016";
		}

		// 1.0 Insert Character
		IfMaterialCharacterDao ifMaterialCharacterDao = new IfMaterialCharacterDao();
		IfMaterialCharacterTo ifMaterialCharacterTo = new IfMaterialCharacterTo();
		ifMaterialCharacterTo.setTimeStamp(new Date());
		ifMaterialCharacterTo.setMaterialNum(materialNum);
		ifMaterialCharacterTo.setMaterialType(materialType);
		ifMaterialCharacterTo.setReleasedBy(userTo.getUserId());
		// 將Release_To 塞到 pidb_if_material_master_Character Table中
		ifMaterialCharacterTo.setReleaseTo(projectCodeTo.getReleaseTo());

		if (projectTo == null) {
			return "ERP-02-032";
		}
		if (projectTo.getProjCode() == null
				|| projectTo.getProjCode().length() <= 0) {
			// return "ERP-02-033";
		} else {
			ifMaterialCharacterTo.setChTechName("CP_02");
			ifMaterialCharacterTo.setChValue(projectTo.getProjCode());
			ifMaterialCharacterTo.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
			ifMaterialCharacterDao.insert(ifMaterialCharacterTo);
		}

		if (projectTo.getWaferInch() == null
				|| projectTo.getWaferInch().length() <= 0) {
			// return "ERP-02-034";
		} else {
			ifMaterialCharacterTo.setChTechName("CP_03");
			ifMaterialCharacterTo.setChValue(projectTo.getWaferInch());
			ifMaterialCharacterTo.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
			ifMaterialCharacterDao.insert(ifMaterialCharacterTo);
		}

		if (projectTo.getGrossDie() == null
				|| projectTo.getGrossDie().length() <= 0) {
			// return "ERP-02-035";
		} else {
			ifMaterialCharacterTo.setChTechName("CP_08");
			ifMaterialCharacterTo.setChValue(projectTo.getGrossDie());
			ifMaterialCharacterTo.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
			ifMaterialCharacterDao.insert(ifMaterialCharacterTo);
		}

		if (projectTo.getFcstCpYield() == null
				|| projectTo.getFcstCpYield().length() <= 0) {
			// return "ERP-02-036";
		} else {
			ifMaterialCharacterTo.setChTechName("CP_11");
			ifMaterialCharacterTo.setChValue(projectTo.getFcstCpYield());
			ifMaterialCharacterTo.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
			ifMaterialCharacterDao.insert(ifMaterialCharacterTo);

		}

		if (projectTo.getProjName() == null
				|| projectTo.getProjName().length() <= 0) {
			// return "ERP-02-036";
		} else {
			ifMaterialCharacterTo.setChTechName("CP_01");
			ifMaterialCharacterTo.setChValue(projectTo.getProjName());
			ifMaterialCharacterTo.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
			ifMaterialCharacterDao.insert(ifMaterialCharacterTo);
		}

		// send mail
		UserDao userDao = new UserDao();

		String subject = PIDBContext.getConfig("MD-13") + "  " + materialNum
				+ " CP Test program not maintain";
		String text = "Please help take care this issue";
		String defaultEmail = userDao
				.fetchEmail(new String[] { "(R)default_MP_CPFT" });

		if (cpMaterialTo != null) {// Check cpMaterialTo have value
			String testProgramNameList = cpMaterialTo
					.getCpTestProgramNameList();
			testProgramNameList = null != testProgramNameList ? testProgramNameList
					: "";
			if (testProgramNameList.equals("N/A")) {
				testProgramNameList = "";
			}

			if (testProgramNameList == null
					|| testProgramNameList.length() <= 0) {
				if (className.indexOf("MpList") > 0) {
					SendMailDispatch.sendMailDefault(subject, text,
							defaultEmail);
				}
				// return "ERP-02-023";
			}
			String[] cpTestProgramName = testProgramNameList.split(",");
			List<String> projCodeList = new ArrayList<String>();
			for (String cpTestPName : cpTestProgramName) {
				if (cpTestPName != null && cpTestPName.length() >= 0) {
					CpTestProgramTo cpTestPTo = cpTestProgramDao
							.find(cpTestPName);
					if (cpTestPTo != null) {
						if (!(projCodeList.contains(cpTestPTo.getProjCode()))) {
							projCodeList.add(cpTestPTo.getProjCode());
						}
					}
				}
			}

			String cp05 = "NO";
			if (cpTestProgramName.length > 1) {
				cp05 = "YES";
			}
			for (String testTemp : cpTestProgramName) {
				if (testTemp != null && testTemp.length() > 0) {

					CpTestProgramTo cpTestPTo = cpTestProgramDao.find(testTemp);
					if (cpTestPTo == null) {

						// return "ERP-02-025";
					} else {
						if (cpTestPTo.getTester() == null
								|| cpTestPTo.getTester().length() <= 0) {
							// return "ERP-02-026";
						} else {
							ifMaterialCharacterTo.setChTechName("CP_04");
							ifMaterialCharacterTo.setChValue(cpTestPTo
									.getTester());
							ifMaterialCharacterTo
									.setId(SequenceSupport
											.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
							ifMaterialCharacterDao
									.insert(ifMaterialCharacterTo);
						}

						if (cpTestPTo.getCpCpuTime() == null) {
							// return "ERP-02-027";
						} else {
							ifMaterialCharacterTo.setChTechName("CP_06");
							ifMaterialCharacterTo.setChValue(cpTestPTo
									.getCpCpuTime().toString());
							ifMaterialCharacterTo
									.setId(SequenceSupport
											.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
							ifMaterialCharacterDao
									.insert(ifMaterialCharacterTo);
						}

						if (cpTestPTo.getCpIndexTime() == null) {
							// return "ERP-02-028";
						} else {
							ifMaterialCharacterTo.setChTechName("CP_07");
							ifMaterialCharacterTo.setChValue(cpTestPTo
									.getCpIndexTime().toString());
							ifMaterialCharacterTo
									.setId(SequenceSupport
											.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
							ifMaterialCharacterDao
									.insert(ifMaterialCharacterTo);

						}

						if (cpTestPTo.getContactDieQty() == null
								|| cpTestPTo.getContactDieQty().length() <= 0) {
							// return "ERP-02-029";
						} else {
							ifMaterialCharacterTo.setChTechName("CP_09");
							ifMaterialCharacterTo.setChValue(cpTestPTo
									.getContactDieQty());
							ifMaterialCharacterTo
									.setId(SequenceSupport
											.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
							ifMaterialCharacterDao
									.insert(ifMaterialCharacterTo);
						}

						if (cpTestPTo.getTesterConfig() == null
								|| cpTestPTo.getTesterConfig().length() <= 0) {
							// return "ERP-02-030";
						} else {
							ifMaterialCharacterTo.setChTechName("CP_10");
							ifMaterialCharacterTo.setChValue(cpTestPTo
									.getTesterConfig());
							ifMaterialCharacterTo
									.setId(SequenceSupport
											.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
							ifMaterialCharacterDao
									.insert(ifMaterialCharacterTo);
						}

						ifMaterialCharacterTo.setChTechName("CP_05");
						ifMaterialCharacterTo.setChValue(cp05);
						ifMaterialCharacterTo
								.setId(SequenceSupport
										.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
						ifMaterialCharacterDao.insert(ifMaterialCharacterTo);

					}
				}
			}
		}
		return null;
	}

	/**
	 * Hank Added 2008/01/29 releaseCpWithVariantICWaferWithCharacter
	 * 
	 * @param md
	 * @param userTo
	 * @return
	 */
	public static String releaseCpWithVariantICWaferWithCharacter(
			final Object md, final UserTo userTo, final String cpWithVariant) {
		IcWaferTo icWaferTo = (IcWaferTo) md;
		IfMaterialMasterTo ifMaterialMasterTo = new IfMaterialMasterTo();
		IfMaterialMasterDao ifMaterialMasterDao = new IfMaterialMasterDao();
		ProjectDao projectDao = new ProjectDao();
		ProjectTo projectTo = new ProjectTo();

		CpTestProgramDao cpTestProgramDao = new CpTestProgramDao();
		CpMaterialDao cpMaterialDao = new CpMaterialDao();

		String materialNum = cpWithVariant;
		// Find Out CP MATERIAL With Variant
		CpMaterialTo cpMaterialTo = cpMaterialDao
				.findByCpMaterialNum(materialNum);

		// 取得release_to的值
		ProjectCodeTo projectCodeTo = new ProjectCodeDao()
				.findByProjectCode(icWaferTo.getProjCode());

		String mdStr = "CpMaterial.2";
		String materialType = ERPHelper.getMaterialType(mdStr);
		if (materialType == null || materialType.length() <= 0) {
			return "ERP-02-022";
		}

		ifMaterialMasterTo.setMaterialNum(materialNum);
		ifMaterialMasterTo.setMaterialType(materialType);
		if (cpMaterialTo.getDescription() != null
				&& !cpMaterialTo.getDescription().equals("")) {
			ifMaterialMasterTo.setMaterialDesc(cpMaterialTo.getDescription());
		} else {
			ifMaterialMasterTo.setMaterialDesc(icWaferTo.getProjCodeWVersion());
		}
		ifMaterialMasterTo
				.setPurchaseOrderText(icWaferTo.getProjCodeWVersion());
		ifMaterialMasterTo.setTimeStamp(new Date());
		ifMaterialMasterTo.setId(SequenceSupport
				.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
		ifMaterialMasterTo.setReleasedBy(userTo.getUserId());
		// 將Release_To 塞到 pidb_if_material_master Table中
		ifMaterialMasterTo.setReleaseTo(projectCodeTo.getReleaseTo());

		projectTo = projectDao.findByProjectCode(icWaferTo.getProjCode());
		String materialGroup = ERPHelper.getMaterialGroup(materialType,
				projectTo.getProdFamily());
		// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
		ifMaterialMasterTo.setMpStatus(ERPHelper
				.judgeMPByProjWVersion(icWaferTo.getProjCodeWVersion()));
		if (materialGroup != null || materialGroup.length() > 0) {
			ifMaterialMasterTo.setMaterialGroup(materialGroup);
			ifMaterialMasterDao.insert(ifMaterialMasterTo,
					"PIDB_IF_MATERIAL_MASTER");
		} else {
			return "ERP-02-016";
		}

		// 1.0 Insert Character
		IfMaterialCharacterDao ifMaterialCharacterDao = new IfMaterialCharacterDao();
		IfMaterialCharacterTo ifMaterialCharacterTo = new IfMaterialCharacterTo();
		ifMaterialCharacterTo.setTimeStamp(new Date());
		ifMaterialCharacterTo.setMaterialNum(materialNum);
		ifMaterialCharacterTo.setMaterialType(materialType);
		ifMaterialCharacterTo.setReleasedBy(userTo.getUserId());
		// 將Release_To 塞到 pidb_if_material_master_Character Table中
		ifMaterialCharacterTo.setReleaseTo(projectCodeTo.getReleaseTo());

		if (projectTo == null) {
			return "ERP-02-032";
		}
		if (projectTo.getProjCode() == null
				|| projectTo.getProjCode().length() <= 0) {
			// return "ERP-02-033";
		} else {
			ifMaterialCharacterTo.setChTechName("CP_02");
			ifMaterialCharacterTo.setChValue(projectTo.getProjCode());
			ifMaterialCharacterTo.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
			ifMaterialCharacterDao.insert(ifMaterialCharacterTo);
		}

		if (projectTo.getWaferInch() == null
				|| projectTo.getWaferInch().length() <= 0) {
			// return "ERP-02-034";
		} else {
			ifMaterialCharacterTo.setChTechName("CP_03");
			ifMaterialCharacterTo.setChValue(projectTo.getWaferInch());
			ifMaterialCharacterTo.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
			ifMaterialCharacterDao.insert(ifMaterialCharacterTo);
		}

		if (projectTo.getGrossDie() == null
				|| projectTo.getGrossDie().length() <= 0) {
			// return "ERP-02-035";
		} else {
			ifMaterialCharacterTo.setChTechName("CP_08");
			ifMaterialCharacterTo.setChValue(projectTo.getGrossDie());
			ifMaterialCharacterTo.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
			ifMaterialCharacterDao.insert(ifMaterialCharacterTo);
		}

		if (projectTo.getFcstCpYield() == null
				|| projectTo.getFcstCpYield().length() <= 0) {
			// return "ERP-02-036";
		} else {
			ifMaterialCharacterTo.setChTechName("CP_11");
			ifMaterialCharacterTo.setChValue(projectTo.getFcstCpYield());
			ifMaterialCharacterTo.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
			ifMaterialCharacterDao.insert(ifMaterialCharacterTo);

		}

		if (projectTo.getProjName() == null
				|| projectTo.getProjName().length() <= 0) {
			// return "ERP-02-036";
		} else {
			ifMaterialCharacterTo.setChTechName("CP_01");
			ifMaterialCharacterTo.setChValue(projectTo.getProjName());
			ifMaterialCharacterTo.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
			ifMaterialCharacterDao.insert(ifMaterialCharacterTo);
		}

		if (cpMaterialTo != null) {// Check cpMaterialTo have value
			String testProgramNameList = cpMaterialTo
					.getCpTestProgramNameList();
			testProgramNameList = null != testProgramNameList ? testProgramNameList
					: "";
			if (testProgramNameList.equals("N/A")) {
				testProgramNameList = "";
			}

			if (testProgramNameList == null
					|| testProgramNameList.length() <= 0) {
				// return "ERP-02-023";
			}
			String[] cpTestProgramName = testProgramNameList.split(",");
			List<String> projCodeList = new ArrayList<String>();
			for (String cpTestPName : cpTestProgramName) {
				if (cpTestPName != null && cpTestPName.length() >= 0) {
					CpTestProgramTo cpTestPTo = cpTestProgramDao
							.find(cpTestPName);
					if (cpTestPTo != null) {
						if (!(projCodeList.contains(cpTestPTo.getProjCode()))) {
							projCodeList.add(cpTestPTo.getProjCode());
						}
					}
				}
			}

			String cp05 = "NO";
			if (cpTestProgramName.length > 1) {
				cp05 = "YES";
			}
			for (String testTemp : cpTestProgramName) {
				if (testTemp != null && testTemp.length() > 0) {

					CpTestProgramTo cpTestPTo = cpTestProgramDao.find(testTemp);
					if (cpTestPTo == null) {

						// return "ERP-02-025";
					} else {
						if (cpTestPTo.getTester() == null
								|| cpTestPTo.getTester().length() <= 0) {
							// return "ERP-02-026";
						} else {
							ifMaterialCharacterTo.setChTechName("CP_04");
							ifMaterialCharacterTo.setChValue(cpTestPTo
									.getTester());
							ifMaterialCharacterTo
									.setId(SequenceSupport
											.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
							ifMaterialCharacterDao
									.insert(ifMaterialCharacterTo);
						}

						if (cpTestPTo.getCpCpuTime() == null) {
							// return "ERP-02-027";
						} else {
							ifMaterialCharacterTo.setChTechName("CP_06");
							ifMaterialCharacterTo.setChValue(cpTestPTo
									.getCpCpuTime().toString());
							ifMaterialCharacterTo
									.setId(SequenceSupport
											.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
							ifMaterialCharacterDao
									.insert(ifMaterialCharacterTo);
						}

						if (cpTestPTo.getCpIndexTime() == null) {
							// return "ERP-02-028";
						} else {
							ifMaterialCharacterTo.setChTechName("CP_07");
							ifMaterialCharacterTo.setChValue(cpTestPTo
									.getCpIndexTime().toString());
							ifMaterialCharacterTo
									.setId(SequenceSupport
											.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
							ifMaterialCharacterDao
									.insert(ifMaterialCharacterTo);

						}

						if (cpTestPTo.getContactDieQty() == null
								|| cpTestPTo.getContactDieQty().length() <= 0) {
							// return "ERP-02-029";
						} else {
							ifMaterialCharacterTo.setChTechName("CP_09");
							ifMaterialCharacterTo.setChValue(cpTestPTo
									.getContactDieQty());
							ifMaterialCharacterTo
									.setId(SequenceSupport
											.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
							ifMaterialCharacterDao
									.insert(ifMaterialCharacterTo);
						}

						if (cpTestPTo.getTesterConfig() == null
								|| cpTestPTo.getTesterConfig().length() <= 0) {
							// return "ERP-02-030";
						} else {
							ifMaterialCharacterTo.setChTechName("CP_10");
							ifMaterialCharacterTo.setChValue(cpTestPTo
									.getTesterConfig());
							ifMaterialCharacterTo
									.setId(SequenceSupport
											.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
							ifMaterialCharacterDao
									.insert(ifMaterialCharacterTo);
						}

						ifMaterialCharacterTo.setChTechName("CP_05");
						ifMaterialCharacterTo.setChValue(cp05);
						ifMaterialCharacterTo
								.setId(SequenceSupport
										.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
						ifMaterialCharacterDao.insert(ifMaterialCharacterTo);

					}
				}
			}
		}
		return null;
	}

	/**
	 * replace by releaseCpNoMp.
	 * 
	 * @deprecated
	 * @param md
	 * @param userTo
	 * @return
	 */
	public static String releaseCpMaterial(final Object md, final UserTo userTo) {
		CpTestProgramDao cpTestProgramDao = new CpTestProgramDao();
		CpMaterialTo cpMaterialTo = (CpMaterialTo) md;
		IcWaferDao icWaferDao = new IcWaferDao();

		IfMaterialMasterTo ifMaterialMasterTo = new IfMaterialMasterTo();
		IfMaterialMasterDao ifMaterialMasterDao = new IfMaterialMasterDao();

		String materialNum = cpMaterialTo.getCpMaterialNum();
		if (materialNum == null || materialNum.length() <= 0) {
			return "ERP-02-021";
		}

		String mdStr = "CpMaterial.2";
		String materialType = ERPHelper.getMaterialType(mdStr);
		if (materialType == null || materialType.length() <= 0) {
			return "ERP-02-022";
		}
		String w_meterialNum = "";
		w_meterialNum = "W" + materialNum.substring(1);
		IcWaferTo icWaferTo = icWaferDao.findByPrimaryKey(w_meterialNum);
		String materialDesc = icWaferTo.getMaterialDesc();

		// 取得release_to的值
		ProjectCodeTo projectCode = new ProjectCodeDao()
				.findByProjectCode(icWaferTo.getProjCode());

		ifMaterialMasterTo.setMaterialNum(materialNum);
		ifMaterialMasterTo.setMaterialType(materialType);
		ifMaterialMasterTo.setMaterialDesc(materialDesc);
		ifMaterialMasterTo.setPurchaseOrderText(cpMaterialTo
				.getProjectCodeWVersion());
		ifMaterialMasterTo.setTimeStamp(new Date());
		ifMaterialMasterTo.setId(SequenceSupport
				.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
		ifMaterialMasterTo.setReleasedBy(userTo.getUserId());
		// 將Release_To 塞到 pidb_if_material_master Table中
		ifMaterialMasterTo.setReleaseTo(projectCode.getReleaseTo());

		// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
		ifMaterialMasterTo.setMpStatus(ERPHelper
				.judgeMPByProjWVersion(cpMaterialTo.getProjectCodeWVersion()));
		String testProgramNameList = cpMaterialTo.getCpTestProgramNameList();
		if (testProgramNameList == null || testProgramNameList.length() <= 0) {
			return "ERP-02-023";
		}
		String[] cpTestProgramName = testProgramNameList.split(",");
		List<String> projCodeList = new ArrayList<String>();
		for (String cpTestPName : cpTestProgramName) {
			if (cpTestPName != null && cpTestPName.length() >= 0) {
				CpTestProgramTo cpTestPTo = cpTestProgramDao.find(cpTestPName);
				if (cpTestPTo != null) {
					if (!(projCodeList.contains(cpTestPTo.getProjCode()))) {
						projCodeList.add(cpTestPTo.getProjCode());
					}
				}
			}
		}
		ProjectCodeDao projectCodeDao = new ProjectCodeDao();
		ProjectCodeTo projectCodeTo = new ProjectCodeTo();
		ProjectDao projectDao = new ProjectDao();
		ProjectTo projectTo = new ProjectTo();
		List<String> prodFamilyList = new ArrayList<String>();
		if (projCodeList != null && projCodeList.size() > 0) {
			for (String temp : projCodeList) {
				projectCodeTo = projectCodeDao.findByProjectCode(temp);
				if (projectCodeTo != null) {
					projectTo = projectDao.find(projectCodeTo.getProjName());
					if (projectTo != null
							&& projectTo.getProdFamily() != null
							&& projectTo.getProdFamily().length() > 0
							&& !prodFamilyList.contains(projectTo
									.getProdFamily())) {
						prodFamilyList.add(projectTo.getProdFamily());
					}
				}
			}
		}
		if (prodFamilyList == null || prodFamilyList.size() <= 0) {
			return "ERP-02-024";
		}

		for (String prodFTemp : prodFamilyList) {
			String materialGroup = ERPHelper.getMaterialGroup(materialType,
					prodFTemp);
			if (materialGroup != null || materialGroup.length() > 0) {
				ifMaterialMasterTo.setMaterialGroup(materialGroup);
				ifMaterialMasterDao.insert(ifMaterialMasterTo,
						"PIDB_IF_MATERIAL_MASTER");
			}
		}

		IfMaterialCharacterDao ifMaterialCharacterDao = new IfMaterialCharacterDao();
		IfMaterialCharacterTo ifMaterialCharacterTo = new IfMaterialCharacterTo();
		ifMaterialCharacterTo.setTimeStamp(new Date());
		ifMaterialCharacterTo.setMaterialNum(materialNum);
		ifMaterialCharacterTo.setMaterialType(materialType);
		ifMaterialCharacterTo.setReleasedBy(userTo.getUserId());
		// 將Release_To 塞到 pidb_if_material_master_Character Table中
		ifMaterialCharacterTo.setReleaseTo(projectCode.getReleaseTo());
		String cp05 = "NO";
		if (cpTestProgramName.length > 1) {
			cp05 = "YES";
		}
		for (String testTemp : cpTestProgramName) {
			if (testTemp != null && testTemp.length() > 0) {
				CpTestProgramTo cpTestPTo = cpTestProgramDao.find(testTemp);
				if (cpTestPTo == null) {
					return "ERP-02-025";
				} else {
					if (cpTestPTo.getTester() == null
							|| cpTestPTo.getTester().length() <= 0) {
						return "ERP-02-026";
					}
					if (cpTestPTo.getCpCpuTime() == null) {
						return "ERP-02-027";
					}
					if (cpTestPTo.getCpIndexTime() == null) {
						return "ERP-02-028";
					}
					if (cpTestPTo.getContactDieQty() == null
							|| cpTestPTo.getContactDieQty().length() <= 0) {
						return "ERP-02-029";
					}
					if (cpTestPTo.getTesterConfig() == null
							|| cpTestPTo.getTesterConfig().length() <= 0) {
						return "ERP-02-030";
					}
					projectCodeTo = projectCodeDao.findByProjectCode(cpTestPTo
							.getProjCode());
					if (projectCodeTo == null) {
						return "ERP-02-031";
					}
					projectTo = projectDao.find(projectCodeTo.getProjName());
					if (projectTo == null) {
						return "ERP-02-032";
					}
					if (projectTo.getProjCode() == null
							|| projectTo.getProjCode().length() <= 0) {
						return "ERP-02-033";
					}
					if (projectTo.getWaferInch() == null
							|| projectTo.getWaferInch().length() <= 0) {
						return "ERP-02-034";
					}
					if (projectTo.getGrossDie() == null
							|| projectTo.getGrossDie().length() <= 0) {
						return "ERP-02-035";
					}
					if (projectTo.getFcstCpYield() == null
							|| projectTo.getFcstCpYield().length() <= 0) {
						return "ERP-02-036";
					}
					ifMaterialCharacterTo.setChTechName("CP_01");
					ifMaterialCharacterTo.setChValue(projectTo.getProjName());
					ifMaterialCharacterTo
							.setId(SequenceSupport
									.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
					ifMaterialCharacterDao.insert(ifMaterialCharacterTo);

					ifMaterialCharacterTo.setChTechName("CP_02");
					ifMaterialCharacterTo.setChValue(projectTo.getProjCode());
					ifMaterialCharacterTo
							.setId(SequenceSupport
									.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
					ifMaterialCharacterDao.insert(ifMaterialCharacterTo);

					ifMaterialCharacterTo.setChTechName("CP_03");
					ifMaterialCharacterTo.setChValue(projectTo.getWaferInch());
					ifMaterialCharacterTo
							.setId(SequenceSupport
									.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
					ifMaterialCharacterDao.insert(ifMaterialCharacterTo);

					ifMaterialCharacterTo.setChTechName("CP_04");
					ifMaterialCharacterTo.setChValue(cpTestPTo.getTester());
					ifMaterialCharacterTo
							.setId(SequenceSupport
									.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
					ifMaterialCharacterDao.insert(ifMaterialCharacterTo);

					ifMaterialCharacterTo.setChTechName("CP_05");
					ifMaterialCharacterTo.setChValue(cp05);
					ifMaterialCharacterTo
							.setId(SequenceSupport
									.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
					ifMaterialCharacterDao.insert(ifMaterialCharacterTo);

					ifMaterialCharacterTo.setChTechName("CP_06");
					ifMaterialCharacterTo.setChValue(cpTestPTo.getCpCpuTime()
							.toString());
					ifMaterialCharacterTo
							.setId(SequenceSupport
									.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
					ifMaterialCharacterDao.insert(ifMaterialCharacterTo);

					ifMaterialCharacterTo.setChTechName("CP_07");
					ifMaterialCharacterTo.setChValue(cpTestPTo.getCpIndexTime()
							.toString());
					ifMaterialCharacterTo
							.setId(SequenceSupport
									.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
					ifMaterialCharacterDao.insert(ifMaterialCharacterTo);

					ifMaterialCharacterTo.setChTechName("CP_08");
					ifMaterialCharacterTo.setChValue(projectTo.getGrossDie());
					ifMaterialCharacterTo
							.setId(SequenceSupport
									.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
					ifMaterialCharacterDao.insert(ifMaterialCharacterTo);

					ifMaterialCharacterTo.setChTechName("CP_09");
					ifMaterialCharacterTo.setChValue(cpTestPTo
							.getContactDieQty());
					ifMaterialCharacterTo
							.setId(SequenceSupport
									.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
					ifMaterialCharacterDao.insert(ifMaterialCharacterTo);

					ifMaterialCharacterTo.setChTechName("CP_10");
					ifMaterialCharacterTo.setChValue(cpTestPTo
							.getTesterConfig());
					ifMaterialCharacterTo
							.setId(SequenceSupport
									.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
					ifMaterialCharacterDao.insert(ifMaterialCharacterTo);

					ifMaterialCharacterTo.setChTechName("CP_11");
					ifMaterialCharacterTo
							.setChValue(projectTo.getFcstCpYield());
					ifMaterialCharacterTo
							.setId(SequenceSupport
									.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
					ifMaterialCharacterDao.insert(ifMaterialCharacterTo);
				}
			}
		}
		return null;
	}

	public static String releaseDsNoMp(final Object md, final UserTo userTo) {
		IcWaferTo icWaferTo = (IcWaferTo) md;
		IfMaterialMasterTo ifMaterialMasterTo = new IfMaterialMasterTo();
		IfMaterialMasterDao ifMaterialMasterDao = new IfMaterialMasterDao();
		ProjectDao projectDao = new ProjectDao();

		String materialNum = "D" + icWaferTo.getMaterialNum().substring(1);

		// 取得release_to的值
		ProjectCodeTo projectCode = new ProjectCodeDao()
				.findByProjectCode(icWaferTo.getProjCode());

		String mdStr = "CpMaterial.1";
		String materialType = ERPHelper.getMaterialType(mdStr);
		if (materialType == null || materialType.length() <= 0) {
			return "ERP-02-022";
		}

		ifMaterialMasterTo.setMaterialNum(materialNum);
		ifMaterialMasterTo.setMaterialType(materialType);
		ifMaterialMasterTo.setMaterialDesc(icWaferTo.getMaterialDesc());
		ifMaterialMasterTo
				.setPurchaseOrderText(icWaferTo.getProjCodeWVersion());
		ifMaterialMasterTo.setTimeStamp(new Date());
		ifMaterialMasterTo.setId(SequenceSupport
				.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
		ifMaterialMasterTo.setReleasedBy(userTo.getUserId());
		// 將Release_To 塞到 pidb_if_material_master Table中
		ifMaterialMasterTo.setReleaseTo(projectCode.getReleaseTo());
		// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
		ifMaterialMasterTo.setMpStatus(ERPHelper
				.judgeMPByProjWVersion(icWaferTo.getProjCodeWVersion()));

		ProjectTo projectTo = projectDao.findByProjectCode(icWaferTo
				.getProjCode());
		String materialGroup = ERPHelper.getMaterialGroup(materialType,
				projectTo.getProdFamily());
		if (materialGroup != null || materialGroup.length() > 0) {
			ifMaterialMasterTo.setMaterialGroup(materialGroup);
			ifMaterialMasterDao.insert(ifMaterialMasterTo,
					"PIDB_IF_MATERIAL_MASTER");
		} else {
			return "ERP-02-016";
		}

		return null;
	}

	/**
	 * Hank Added 2007/11/26 releaseDsNoMpWithCharacter
	 * 
	 * @param md
	 * @param userTo
	 * @return
	 */
	public static String releaseDsfICWaferWithCharacter(final Object md,
			final UserTo userTo) {

		IcWaferTo icWaferTo = (IcWaferTo) md;
		IfMaterialMasterTo ifMaterialMasterTo = new IfMaterialMasterTo();
		IfMaterialMasterDao ifMaterialMasterDao = new IfMaterialMasterDao();
		// 1.0 Insert Character
		IfMaterialCharacterDao ifMaterialCharacterDao = new IfMaterialCharacterDao();
		IfMaterialCharacterTo ifMaterialCharacterTo = new IfMaterialCharacterTo();

		ProjectDao projectDao = new ProjectDao();

		String materialNum = "D" + icWaferTo.getMaterialNum().substring(1);
		String projName = icWaferTo.getProjCode().substring(0, 6);

		// 取得release_to的值
		ProjectCodeTo projectCode = new ProjectCodeDao()
				.findByProjectCode(icWaferTo.getProjCode());

		String mdStr = "CpMaterial.1";
		String materialType = ERPHelper.getMaterialType(mdStr);
		if (materialType == null || materialType.length() <= 0) {
			return "ERP-02-022";
		}

		ifMaterialMasterTo.setMaterialNum(materialNum);
		ifMaterialMasterTo.setMaterialType(materialType);
		ifMaterialMasterTo.setMaterialDesc(icWaferTo.getMaterialDesc());
		ifMaterialMasterTo
				.setPurchaseOrderText(icWaferTo.getProjCodeWVersion());
		ifMaterialMasterTo.setTimeStamp(new Date());
		ifMaterialMasterTo.setId(SequenceSupport
				.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
		ifMaterialMasterTo.setReleasedBy(userTo.getUserId());
		// 將Release_To 塞到 pidb_if_material_master Table中
		ifMaterialMasterTo.setReleaseTo(projectCode.getReleaseTo());
		// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
		ifMaterialMasterTo.setMpStatus(ERPHelper
				.judgeMPByProjWVersion(icWaferTo.getProjCodeWVersion()));

		ProjectTo projectTo = projectDao.findByProjectCode(icWaferTo
				.getProjCode());
		String materialGroup = ERPHelper.getMaterialGroup(materialType,
				projectTo.getProdFamily());
		if (materialGroup != null || materialGroup.length() > 0) {
			ifMaterialMasterTo.setMaterialGroup(materialGroup);
			ifMaterialMasterDao.insert(ifMaterialMasterTo,
					"PIDB_IF_MATERIAL_MASTER");
		} else {
			return "ERP-02-016";
		}

		// 2.0 Set Character
		// if_material_char_to same parameters
		ifMaterialCharacterTo.setTimeStamp(new Date());
		// ifMaterialCharacterTo.setId(SequenceSupport.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
		ifMaterialCharacterTo.setReleasedBy(userTo.getUserId());
		ifMaterialCharacterTo.setMaterialType(materialType);
		ifMaterialCharacterTo.setMaterialNum(materialNum);
		// 將Release_To 塞到 pidb_if_material_master Table中
		ifMaterialCharacterTo.setReleaseTo(projectCode.getReleaseTo());
		// insert if_material_char and get all prodFamily
		List<String> prodFamilyList = new ArrayList<String>();
		ifMaterialCharacterTo.setMaterialNum(materialNum);

		projectTo = projectDao.findByProjName(projName);
		if (projectTo == null) {
			return "ERP-02-038";
		}
		if (StringUtils.isEmpty(projectTo.getProdFamily())) {
			return "ERP-02-039";
		}
		if (projectTo != null
				&& StringUtils.isNotEmpty(projectTo.getProdFamily())
				&& !prodFamilyList.contains(projectTo.getProdFamily())) {
			prodFamilyList.add(projectTo.getProdFamily());
		}
		if (StringUtils.isEmpty(projectTo.getGrossDie())) {
			// return "ERP-02-040";
		} else {
			ifMaterialCharacterTo.setChTechName("DS_01");
			ifMaterialCharacterTo.setChValue(projectTo.getGrossDie());
			ifMaterialCharacterTo.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
			ifMaterialCharacterDao.insert(ifMaterialCharacterTo);
		}

		if (StringUtils.isEmpty(projectTo.getFcstCpYield())) {
			// return "ERP-02-041";
		} else {
			ifMaterialCharacterTo.setChTechName("DS_02");
			ifMaterialCharacterTo.setChValue(projectTo.getFcstCpYield());
			ifMaterialCharacterTo.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
			ifMaterialCharacterDao.insert(ifMaterialCharacterTo);
		}

		return null;
	}

	/**
	 * replace by releaseCpNoMp.
	 * 
	 * @deprecated
	 * @param md
	 * @param userTo
	 * @return
	 */
	public static String releaseDs(final Object md, final UserTo userTo,
			final String projCodeWVersion, final boolean ignore) {
		IfMaterialMasterTo ifMaterialMasterTo = new IfMaterialMasterTo();
		IfMaterialMasterDao ifMaterialMasterDao = new IfMaterialMasterDao();
		IfMaterialCharacterDao ifMaterialCharacterDao = new IfMaterialCharacterDao();
		IfMaterialCharacterTo ifMaterialCharacterTo = new IfMaterialCharacterTo();
		IcWaferDao icWaferDao = new IcWaferDao();

		CpMaterialTo cpTo = (CpMaterialTo) md;
		CpTestProgramDao cpTestProgramDao = new CpTestProgramDao();
		CpTestProgramTo cpTestProgramTo = new CpTestProgramTo();

		ProjectDao projectDao = new ProjectDao();
		ProjectTo projectTo = new ProjectTo();
		ProjectCodeDao projectCodeDao = new ProjectCodeDao();
		ProjectCodeTo projectCodeTo = new ProjectCodeTo();

		// 取得release_to的值
		IcWaferTo icWafer = new IcWaferDao().findByProjCodeWVersion(cpTo
				.getProjectCodeWVersion());
		ProjectCodeTo projectCode = new ProjectCodeDao().findByProjectCode(icWafer
				.getProjCode());

		// if_material_master_to same parameters
		ifMaterialMasterTo.setTimeStamp(new Date());
		// ifMaterialMasterTo.setId(SequenceSupport.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
		ifMaterialMasterTo.setReleasedBy(userTo.getUserId());
		String materialType = ERPHelper.getMaterialType("CpMaterial.1");
		ifMaterialMasterTo.setMaterialType(materialType);

		String w_meterialNum = "";
		w_meterialNum = "W" + cpTo.getCpMaterialNum().substring(1);
		IcWaferTo icWaferTo = icWaferDao.findByPrimaryKey(w_meterialNum);
		String materialDesc = icWaferTo.getMaterialDesc();

		ifMaterialMasterTo.setMaterialDesc(materialDesc);
		// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
		ifMaterialMasterTo.setMpStatus(ERPHelper
				.judgeMPByProjWVersion(projCodeWVersion));

		// if_material_char_to same parameters
		ifMaterialCharacterTo.setTimeStamp(new Date());
		// ifMaterialCharacterTo.setId(SequenceSupport.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
		ifMaterialCharacterTo.setReleasedBy(userTo.getUserId());
		ifMaterialCharacterTo.setMaterialType(materialType);
		
		// 將Release_To 塞到 pidb_if_material_master_Character Table中
		ifMaterialCharacterTo.setReleaseTo(projectCode.getReleaseTo());

		String materialNum = "D"
				+ cpTo.getCpMaterialNum().substring(1,
						cpTo.getCpMaterialNum().length());
		String cpTestProString = cpTo.getCpTestProgramNameList();
		List<String> projNameList = new ArrayList<String>();
		if (cpTestProString != null && cpTestProString.length() > 0) {
			String[] cpTestProArr = cpTestProString.split(",");
			for (String cpTestTemp : cpTestProArr) {
				if (cpTestTemp != null) {
					cpTestProgramTo = cpTestProgramDao.find(cpTestTemp);
					if (cpTestProgramTo != null) {
						projectCodeTo = projectCodeDao
								.findByProjectCode(cpTestProgramTo
										.getProjCode());
					}
					if (projectCodeTo != null
							&& projectCodeTo.getProjName() != null) {
						if (!projNameList.contains(projectCodeTo.getProjName())) {
							projNameList.add(projectCodeTo.getProjName());
						}
					}
				}
			}
		}
		if (projNameList == null || projNameList.size() <= 0) {
			return "ERP-02-037";
		}
		// insert if_material_char and get all prodFamily
		List<String> prodFamilyList = new ArrayList<String>();
		ifMaterialCharacterTo.setMaterialNum(materialNum);
		for (String projName : projNameList) {
			projectTo = projectDao.findByProjName(projName);
			if (!ignore && projectTo == null) {
				return "ERP-02-038";
			}
			if (!ignore && StringUtils.isEmpty(projectTo.getProdFamily())) {
				return "ERP-02-039";
			}
			if (projectTo != null
					&& StringUtils.isNotEmpty(projectTo.getProdFamily())
					&& !prodFamilyList.contains(projectTo.getProdFamily())) {
				prodFamilyList.add(projectTo.getProdFamily());
			}
			if (!ignore && StringUtils.isEmpty(projectTo.getGrossDie())) {
				return "ERP-02-040";
			}
			if (!ignore && StringUtils.isEmpty(projectTo.getFcstCpYield())) {
				return "ERP-02-041";
			}

			if (projectTo != null
					&& StringUtils.isNotEmpty(projectTo.getGrossDie())) {
				ifMaterialCharacterTo.setChTechName("DS_01");
				ifMaterialCharacterTo.setChValue(projectTo.getGrossDie());
				ifMaterialCharacterTo.setId(SequenceSupport
						.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
				ifMaterialCharacterDao.insert(ifMaterialCharacterTo);
			}

			if (projectTo != null
					&& StringUtils.isNotEmpty(projectTo.getFcstCpYield())) {
				ifMaterialCharacterTo.setChTechName("DS_02");
				ifMaterialCharacterTo.setChValue(projectTo.getFcstCpYield());
				ifMaterialCharacterTo.setId(SequenceSupport
						.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
				ifMaterialCharacterDao.insert(ifMaterialCharacterTo);
			}

		}
		// insert if_material_master
		if (prodFamilyList == null || prodFamilyList.size() <= 0) {
			return "ERP-02-042";
		}
		ifMaterialMasterTo.setMaterialNum(materialNum);
		// 將Release_To 塞到 pidb_if_material_master Table中
		ifMaterialMasterTo.setReleaseTo(projectCode.getReleaseTo());
		for (String prodFamily : prodFamilyList) {
			String materialGroup = ERPHelper.getMaterialGroup(materialType,
					prodFamily);
			if (materialGroup == null) {
				return "ERP-02-043";
			}
			ifMaterialMasterTo.setMaterialGroup(materialGroup);
			ifMaterialMasterTo.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
			ifMaterialMasterDao.insert(ifMaterialMasterTo,
					"PIDB_IF_MATERIAL_MASTER");
		}

		return null;
	}

	/**
	 * Hank Added 2008/01/29 releaseCpWithVariantICWaferWithCharacter
	 * 
	 * @param md
	 * @param userTo
	 * @return
	 */
	public static String releaseDsWithVariantICWaferWithCharacter(
			final Object md, final UserTo userTo, final String cpWithVariant) {
		IcWaferTo icWaferTo = (IcWaferTo) md;
		IfMaterialMasterTo ifMaterialMasterTo = new IfMaterialMasterTo();
		IfMaterialMasterDao ifMaterialMasterDao = new IfMaterialMasterDao();
		// 1.0 Insert Character
		IfMaterialCharacterDao ifMaterialCharacterDao = new IfMaterialCharacterDao();
		IfMaterialCharacterTo ifMaterialCharacterTo = new IfMaterialCharacterTo();

		ProjectDao projectDao = new ProjectDao();
		
		// 取得release_to的值
		ProjectCodeTo projectCode = new ProjectCodeDao().findByProjectCode(icWaferTo
				.getProjCode());

		String materialNum = "D" + cpWithVariant.substring(1);
		String projName = icWaferTo.getProjCode().substring(0, 6);

		// Find Out CP MATERIAL With Variant
		CpMaterialDao cpMaterialDao = new CpMaterialDao();
		CpMaterialTo cpMaterialTo = cpMaterialDao
				.findByCpMaterialNum(cpWithVariant);

		String mdStr = "CpMaterial.1";
		String materialType = ERPHelper.getMaterialType(mdStr);
		if (materialType == null || materialType.length() <= 0) {
			return "ERP-02-022";
		}

		ifMaterialMasterTo.setMaterialNum(materialNum);
		ifMaterialMasterTo.setMaterialType(materialType);
		if (cpMaterialTo.getDescription() != null
				&& !cpMaterialTo.getDescription().equals("")) {
			ifMaterialMasterTo.setMaterialDesc(cpMaterialTo.getDescription());
		} else {
			ifMaterialMasterTo.setMaterialDesc(icWaferTo.getProjCodeWVersion());
		}
		ifMaterialMasterTo
				.setPurchaseOrderText(icWaferTo.getProjCodeWVersion());
		ifMaterialMasterTo.setTimeStamp(new Date());
		ifMaterialMasterTo.setId(SequenceSupport
				.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
		ifMaterialMasterTo.setReleasedBy(userTo.getUserId());
		// 將Release_To 塞到 pidb_if_material_master Table中
		ifMaterialMasterTo.setReleaseTo(projectCode.getReleaseTo());
		// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
		ifMaterialMasterTo.setMpStatus(ERPHelper
				.judgeMPByProjWVersion(icWaferTo.getProjCodeWVersion()));

		ProjectTo projectTo = projectDao.findByProjectCode(icWaferTo
				.getProjCode());
		String materialGroup = ERPHelper.getMaterialGroup(materialType,
				projectTo.getProdFamily());
		if (materialGroup != null || materialGroup.length() > 0) {
			ifMaterialMasterTo.setMaterialGroup(materialGroup);
			ifMaterialMasterDao.insert(ifMaterialMasterTo,
					"PIDB_IF_MATERIAL_MASTER");
		} else {
			return "ERP-02-016";
		}

		// 2.0 Set Character
		// if_material_char_to same parameters
		ifMaterialCharacterTo.setTimeStamp(new Date());
		// ifMaterialCharacterTo.setId(SequenceSupport.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
		ifMaterialCharacterTo.setReleasedBy(userTo.getUserId());
		ifMaterialCharacterTo.setMaterialType(materialType);
		ifMaterialCharacterTo.setMaterialNum(materialNum);

		// insert if_material_char and get all prodFamily
		List<String> prodFamilyList = new ArrayList<String>();
		ifMaterialCharacterTo.setMaterialNum(materialNum);
		// 將Release_To 塞到 pidb_if_material_master_Character Table中
		ifMaterialCharacterTo.setReleaseTo(projectCode.getReleaseTo());
		projectTo = projectDao.findByProjName(projName);
		if (projectTo == null) {
			return "ERP-02-038";
		}
		if (StringUtils.isEmpty(projectTo.getProdFamily())) {
			return "ERP-02-039";
		}
		if (projectTo != null
				&& StringUtils.isNotEmpty(projectTo.getProdFamily())
				&& !prodFamilyList.contains(projectTo.getProdFamily())) {
			prodFamilyList.add(projectTo.getProdFamily());
		}
		if (StringUtils.isEmpty(projectTo.getGrossDie())) {
			// return "ERP-02-040";
		} else {
			ifMaterialCharacterTo.setChTechName("DS_01");
			ifMaterialCharacterTo.setChValue(projectTo.getGrossDie());
			ifMaterialCharacterTo.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
			ifMaterialCharacterDao.insert(ifMaterialCharacterTo);
		}

		if (StringUtils.isEmpty(projectTo.getFcstCpYield())) {
			// return "ERP-02-041";
		} else {
			ifMaterialCharacterTo.setChTechName("DS_02");
			ifMaterialCharacterTo.setChValue(projectTo.getFcstCpYield());
			ifMaterialCharacterTo.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
			ifMaterialCharacterDao.insert(ifMaterialCharacterTo);
		}

		return null;
	}
}
