package com.cista.pidb.md.erp;

import java.util.Date;
import java.util.List;

import com.cista.pidb.admin.to.UserTo;
import com.cista.pidb.core.SequenceSupport;
import com.cista.pidb.md.dao.IcFgDao;
import com.cista.pidb.md.dao.IfMaterialCharacterDao;
import com.cista.pidb.md.dao.IfMaterialMasterDao;
import com.cista.pidb.md.dao.ProjectCodeDao;
import com.cista.pidb.md.dao.ProjectDao;
import com.cista.pidb.md.to.IcFgTo;
import com.cista.pidb.md.to.IfMaterialCharacterTo;
import com.cista.pidb.md.to.IfMaterialMasterTo;
import com.cista.pidb.md.to.ProjectCodeTo;
import com.cista.pidb.md.to.ProjectTo;
import com.cista.pidb.md.to.TradPkgTo;

public class TradPkgERP {

	public static String release(Object md, final UserTo userTo) {

		// TODO Auto-generated method stub
		TradPkgTo tradPkgTo = (TradPkgTo) md;
		/*
		 * if (tradPkgTo.getPkgCode() == null || tradPkgTo.getPkgCode().length() <=
		 * 0) { return "ERP-06-001"; }
		 */
		if (tradPkgTo.getPkgName() == null
				|| tradPkgTo.getPkgName().length() <= 0) {
			return "ERP-06-014";
		}
		IcFgDao icFgDao = new IcFgDao();
		// List<IcFgTo> icFgToList =
		// icFgDao.findByPkgCode(tradPkgTo.getPkgCode());
		List<IcFgTo> icFgToList = icFgDao.findByPkgCodeForTradRelease(tradPkgTo
				.getPkgName());
		if (icFgToList == null || icFgToList.size() <= 0) {
			return "ERP-06-002-1";
			// return "ERP-06-002";
		}

		ProjectDao projectDao = new ProjectDao();
		ProjectTo projectTo = projectDao.find(tradPkgTo.getProjName());
		if (projectTo == null) {
			return "ERP-06-003";
		}
		if (projectTo.getProdFamily() == null
				|| projectTo.getProdFamily().length() <= 0) {
			return "ERP-06-004";
		}

		for (IcFgTo icTo : icFgToList) {
			if (icTo.getPartNum() == null || icTo.getPartNum().length() <= 0) {
				return "ERP-06-005";
			}
		}
		IfMaterialMasterDao ifMasterDao = new IfMaterialMasterDao();
		IfMaterialMasterTo ifMasterTo = new IfMaterialMasterTo();
		ifMasterTo.setTimeStamp(new Date());

		ifMasterTo.setReleasedBy(userTo.getUserId());
		// set materialType
		// String mt = "ZAS2";
		// String materialType = ERPHelper.getMaterialType(mt);
		String pkgType = "";
		String materialType = "";
		String materialGroup = "";
		// set materilGroup

		for (IcFgTo icTo : icFgToList) {
			materialType = "";
			materialGroup = "";
			// 取得project_Code中的release_To值
			ProjectCodeTo projectCode = new ProjectCodeDao()
					.findByProjectCode(icTo.getProjCode());
			if (icTo.getStatus().equalsIgnoreCase("Released")) {
				pkgType = icTo.getPkgType();
				pkgType = null != pkgType ? pkgType : "";

				if (pkgType.equals("304")) {
					materialType = "ZAS2";
					ifMasterTo.setMaterialType(materialType);
					materialGroup = ERPHelper.getMaterialGroup(materialType,
							projectTo.getProdFamily());

					ifMasterTo.setMaterialGroup(materialGroup);

				} else {
					materialType = "ZAS";
					ifMasterTo.setMaterialType(materialType);
					materialGroup = ERPHelper.getMaterialGroup(materialType,
							projectTo.getProdFamily());

					ifMasterTo.setMaterialGroup(materialGroup);
				}
				ifMasterTo.setMaterialNum("A"
						+ icTo.getMaterialNum().substring(1));
				// set materialDesc
				ifMasterTo.setMaterialDesc(icTo.getPartNum());
				ifMasterTo.setId(SequenceSupport
						.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
				// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
				ifMasterTo
						.setMpStatus(ERPHelper.judgeMP(icTo.getMaterialNum()));
				ifMasterTo.setReleaseTo(projectCode.getReleaseTo());

				ifMasterDao.insert(ifMasterTo, "PIDB_IF_MATERIAL_MASTER");
			}
		}

		if (tradPkgTo.getTradPkgType() == null
				|| tradPkgTo.getTradPkgType().length() <= 0) {
			return "ERP-06-006";
		}
		if (tradPkgTo.getPinCount() == null
				|| tradPkgTo.getPinCount().length() <= 0) {
			return "ERP-06-007";
		}
		if (tradPkgTo.getLeadFrameType() == null
				|| tradPkgTo.getLeadFrameType().length() <= 0) {
			return "ERP-06-008";
		}
		if (tradPkgTo.getSubtractLayer() == null
				|| tradPkgTo.getSubtractLayer().length() <= 0) {
			return "ERP-06-009";
		}
		if (tradPkgTo.getBodySize() == null
				|| tradPkgTo.getBodySize().length() <= 0) {
			return "ERP-06-010";
		}
		if (tradPkgTo.getGoldenWireWidth() == null
				|| tradPkgTo.getGoldenWireWidth().length() <= 0) {
			return "ERP-06-011";
		}
		if (tradPkgTo.getMcpPkg() == null
				|| tradPkgTo.getMcpPkg().length() <= 0) {
			return "ERP-06-012";
		}
		if (tradPkgTo.getMcpDieQty() == null
				|| tradPkgTo.getMcpDieQty().length() <= 0) {
			return "ERP-06-013";
		}

		IfMaterialCharacterDao ifCharDao = new IfMaterialCharacterDao();
		IfMaterialCharacterTo ifCharTo = new IfMaterialCharacterTo();
		ifCharTo.setTimeStamp(new Date());
		ifCharTo.setId(SequenceSupport
				.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
		ifCharTo.setReleasedBy(userTo.getUserId());
		ifCharTo.setMaterialType(materialType);
		for (IcFgTo icTo : icFgToList) {
			// 取得project_Code中的release_To值
			ProjectCodeTo projectCode = new ProjectCodeDao()
					.findByProjectCode(icTo.getProjCode());
			if (icTo.getStatus().equalsIgnoreCase("Released")) {
				ifCharTo.setMaterialNum("A"
						+ icTo.getMaterialNum().substring(1));
				ifCharTo.setReleaseTo(projectCode.getReleaseTo());
				pkgType = icTo.getPkgType();
				pkgType = null != pkgType ? pkgType : "";

				if (pkgType.equals("304")) {
					ifCharTo.setChTechName("AS_01");
					ifCharTo.setChValue(tradPkgTo.getTradPkgType());
					ifCharTo
							.setId(SequenceSupport
									.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
					ifCharDao.insert(ifCharTo);

					ifCharTo.setChTechName("AS_02");
					ifCharTo.setChValue(tradPkgTo.getPinCount());
					ifCharTo
							.setId(SequenceSupport
									.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
					ifCharDao.insert(ifCharTo);

					ifCharTo.setChTechName("AS_03");
					ifCharTo.setChValue(tradPkgTo.getLeadFrameType());
					ifCharTo
							.setId(SequenceSupport
									.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
					ifCharDao.insert(ifCharTo);

					ifCharTo.setChTechName("AS_04");
					ifCharTo.setChValue(tradPkgTo.getSubtractLayer());
					ifCharTo
							.setId(SequenceSupport
									.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
					ifCharDao.insert(ifCharTo);

					ifCharTo.setChTechName("AS_05");
					ifCharTo.setChValue(tradPkgTo.getBodySize());
					ifCharTo
							.setId(SequenceSupport
									.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
					ifCharDao.insert(ifCharTo);

					ifCharTo.setChTechName("AS_06");
					ifCharTo.setChValue(tradPkgTo.getGoldenWireWidth());
					ifCharTo
							.setId(SequenceSupport
									.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
					ifCharDao.insert(ifCharTo);

					ifCharTo.setChTechName("AS_07");
					String mcpPkg = "";
					if (tradPkgTo.getMcpPkg().equalsIgnoreCase("Y")) {
						mcpPkg = "Yes";
					} else if (tradPkgTo.getMcpPkg().equalsIgnoreCase("N")) {
						mcpPkg = "No";
					}
					ifCharTo.setChValue(mcpPkg);
					ifCharTo
							.setId(SequenceSupport
									.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
					ifCharDao.insert(ifCharTo);

					ifCharTo.setChTechName("AS_08");
					ifCharTo.setChValue(tradPkgTo.getMcpDieQty());

					ifCharTo
							.setId(SequenceSupport
									.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
					ifCharDao.insert(ifCharTo);
				} else {

					if (icTo.getPkgType() == null
							|| icTo.getPkgType().length() <= 0) {

					} else {
						ifCharTo.setChTechName("IL_01");
						
						String iLPkgType = icTo.getPkgType();
						if(projectTo.getProdFamily().equals("101")){//表Source
							iLPkgType = iLPkgType + "-SD";
						}else if(projectTo.getProdFamily().equals("102") ){//表Gate
							iLPkgType = iLPkgType + "-GD";
						}
						ifCharTo.setChValue(iLPkgType);

						ifCharTo
								.setId(SequenceSupport
										.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
						ifCharDao.insert(ifCharTo);
					}

					if (projectTo.getPitch() == null
							|| projectTo.getPitch().length() <= 0) {

					} else {
						ifCharTo.setChTechName("IL_02");
						ifCharTo.setChValue(projectTo.getPitch());

						ifCharTo
								.setId(SequenceSupport
										.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
						ifCharDao.insert(ifCharTo);
					}

				}
			}
		}

		return null;
	}

}
