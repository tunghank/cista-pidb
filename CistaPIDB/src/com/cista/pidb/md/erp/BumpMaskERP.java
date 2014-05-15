package com.cista.pidb.md.erp;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cista.pidb.admin.to.UserTo;
import com.cista.pidb.core.SequenceSupport;
import com.cista.pidb.md.dao.BumpMaskDao;
import com.cista.pidb.md.dao.IcWaferDao;
import com.cista.pidb.md.dao.IfMaterialCharacterDao;
import com.cista.pidb.md.dao.IfMaterialMasterDao;
import com.cista.pidb.md.dao.ProjectCodeDao;
import com.cista.pidb.md.dao.ProjectDao;
import com.cista.pidb.md.to.BumpMaskTo;
import com.cista.pidb.md.to.IcWaferTo;
import com.cista.pidb.md.to.IfMaterialCharacterTo;
import com.cista.pidb.md.to.IfMaterialMasterTo;
import com.cista.pidb.md.to.ProjectCodeTo;
import com.cista.pidb.md.to.ProjectTo;


public class BumpMaskERP {

    /** Logger. */
    private static Log logger = LogFactory.getLog(CpTestProgramERP.class);

    public static String release(Object md, final UserTo userTo) {

        BumpMaskTo bumpMaskTo = (BumpMaskTo) md;
        BumpMaskDao bumpMaskDao = new BumpMaskDao();
        IcWaferDao icWaferDao = new IcWaferDao();
        IfMaterialMasterDao ifMaterialMasterDao = new IfMaterialMasterDao();
        IfMaterialMasterTo ifMaterialMasterTo = new IfMaterialMasterTo();

        String projCode = bumpMaskTo.getProjCode().trim();
        if (projCode == null || projCode.length() <= 0) {
            logger.error("projCode is null.");
            return "ERP-08-001";
        }
        List<IcWaferTo> icWaferList = icWaferDao.findByProjectCode(projCode); //and ROUTING_BP='1'
        if (icWaferList == null || icWaferList.size() <= 0) {
            logger.error("Not find IcWafer.");
            return "ERP-08-002";
        }

        String rdl = bumpMaskTo.getRdl().trim();

        for (IcWaferTo to : icWaferList) {
            if (to == null || !to.getStatus().equalsIgnoreCase("Released")) {
                continue;
            }
            
            String materialNum = to.getMaterialNum();
            if (materialNum == null || materialNum.length() <= 0) {
                logger.error("Material number is null.");
                return "ERP-08-003";
            }

            materialNum = "B" + materialNum.substring(1);
            
            String projCodeWVersion = to.getProjCodeWVersion();
            String materialDesc = to.getMaterialDesc();

            if (projCodeWVersion == null || projCodeWVersion.length() <= 0) {
                logger
                        .error("Project code with version is null on IcWafer table.");
                return "ERP-08-004";
            }

            String mdStr = "BumpMask";
            String materialType = ERPHelper.getMaterialType(mdStr);
            if (materialType == null || materialType.length() <= 0) {
                logger.error("Not find Material type, Material type is null.");
                return "ERP-08-005";
            }

            ProjectDao projectDao = new ProjectDao();
            ProjectTo projectTo = projectDao.findByProjectCode(projCode);
            if (projectTo == null) {
                logger
                        .error("ProjectTo is null,when find projectTo by project code.");
                return "ERP-08-006";
            }

            String prodFamily = projectTo.getProdFamily();
            if (prodFamily == null || prodFamily.length() <= 0) {
                logger.error("Product family is null.");
                return "ERP-08-007";
            }

            String materialGroup = ERPHelper.getMaterialGroup(materialType,
                    prodFamily);
            if (materialGroup == null || materialGroup.length() <= 0) {
                logger
                        .error("Not find Material group, Material group is null.");
                return "ERP-08-008";
            }

            ifMaterialMasterTo.setMaterialNum(materialNum);
            ifMaterialMasterTo.setMaterialDesc(materialDesc);
            ifMaterialMasterTo.setMaterialType(materialType);
            ifMaterialMasterTo.setMaterialGroup(materialGroup);
            ifMaterialMasterTo.setPurchaseOrderText(projCodeWVersion);
            ifMaterialMasterTo.setTimeStamp(new Date());
            ifMaterialMasterTo.setReleasedBy(userTo.getUserId());
            
            //取得projectCode中的Release_To值
			ProjectCodeTo projectCode = new ProjectCodeDao()
					.findByProjectCode(projCode);
			// 將Release_To 塞到 PIDB_IF_MATERIAL Table中
			ifMaterialMasterTo.setReleaseTo(projectCode.getReleaseTo());

            
            ifMaterialMasterTo.setId(SequenceSupport
                    .nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
            //Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
            ifMaterialMasterTo.setMpStatus(ERPHelper.judgeMPByProjWVersion(projCodeWVersion));
            
            // to Interface Table:PIDB_IF_MATERIAL_MASTER
            ifMaterialMasterDao.insert(ifMaterialMasterTo,
                    "PIDB_IF_MATERIAL_MASTER");

            IfMaterialCharacterDao ifMaterialCharacterDao = new IfMaterialCharacterDao();
            IfMaterialCharacterTo ifMaterialCharacterTo = new IfMaterialCharacterTo();
            String[] value = new String[3];
            for (int i = 0; i < 3; i++) {
                value[i] = "";
            }

            String waferInch = projectTo.getWaferInch();
            if (waferInch == null || waferInch.length() <= 0) {
                logger.error("Wafer Inch is null.");
                return "ERP-08-009";
            }
            value[0] = waferInch;
            value[1] = rdl;

            String grossDie = projectTo.getGrossDie();
            if (grossDie == null || grossDie.length() <= 0) {
                logger.error("Gross Die is null.");
                return "ERP-08-010";
            }
            value[2] = grossDie;

            for (int i = 0; i < 3; i++) {
                String chTechName = "BP_";
                String s = "0" + (i + 1);
                ifMaterialCharacterTo.setMaterialNum(materialNum);
                ifMaterialCharacterTo.setMaterialType(materialType);
                chTechName += s.substring(s.length() - 2);

                ifMaterialCharacterTo.setChTechName(chTechName);
                ifMaterialCharacterTo.setChValue(value[i]);
                ifMaterialCharacterTo.setSapStatus("");
                ifMaterialCharacterTo.setInfoMessage("");
                ifMaterialCharacterTo.setTimeStamp(new Date());
                ifMaterialCharacterTo.setReleasedBy(userTo.getUserId());
                
                //將Release_To 塞到 PIDB_IF_MATERIAL_CHARACTER Table中
                ifMaterialCharacterTo.setReleaseTo(projectTo.getReleaseTo());
                
                // to Interface Table:PIDB_IF_MATERIAL_CHARACTER
                ifMaterialCharacterDao.insert(ifMaterialCharacterTo);
            }
        }
        return null;
    }
}
