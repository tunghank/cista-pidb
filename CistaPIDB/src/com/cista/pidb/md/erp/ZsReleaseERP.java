package com.cista.pidb.md.erp;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cista.pidb.admin.to.UserTo;
import com.cista.pidb.core.SequenceSupport;
import com.cista.pidb.md.dao.IfMaterialCharacterDao;
import com.cista.pidb.md.dao.IfMaterialMasterDao;
import com.cista.pidb.md.to.IfMaterialCharacterTo;
import com.cista.pidb.md.to.IfMaterialMasterTo;
import com.cista.pidb.md.to.ZsEepromTo;
import com.cista.pidb.md.to.ZsFlashramTo;
import com.cista.pidb.md.to.ZsHdcpKeyTo;
import com.cista.pidb.md.to.ZsSdramTo;
import com.cista.pidb.md.to.ZsUsbramTo;



public class ZsReleaseERP {

    /** Logger. */
    private static Log logger = LogFactory.getLog(ZsReleaseERP.class);

    public static String releaseEEPRom(Object md, final UserTo userTo) {
    	ZsEepromTo zsEepromTo = (ZsEepromTo) md;
    	
    	//1.0 Insert to PIDB_IF_MATERIAL_MASTER
        IfMaterialMasterTo ifMaterialMasterTo = new IfMaterialMasterTo();
        
        // set materialNum and materialDesc and purchsing order text
        ifMaterialMasterTo.setMaterialNum(zsEepromTo.getMaterialNum());
        ifMaterialMasterTo.setMaterialDesc(zsEepromTo.getDescription());
        ifMaterialMasterTo
                .setPurchaseOrderText(zsEepromTo.getDescription());

        // set materialType
        String mt = "ZSEEPROM";
        String materialType = ERPHelper.getMaterialType(mt);
        ifMaterialMasterTo.setMaterialType(materialType);

        // set materilGroup
        mt = "ZSE.MATERIAL_GROUP";
        String materialGroup = ERPHelper.getMaterialType(mt);
        
        ifMaterialMasterTo.setMaterialGroup(materialGroup);
        //set Basic_Data_Text
        //ifMaterialMasterTo.setBasicDataText(basicDataText);
        // set ID
        ifMaterialMasterTo.setReleasedBy(userTo.getUserId());
        ifMaterialMasterTo.setId(SequenceSupport
                .nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
        ifMaterialMasterTo.setTimeStamp(new Date());
        //Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
        ifMaterialMasterTo.setMpStatus("1");
        
        //set Release_To
        ifMaterialMasterTo.setReleaseTo(zsEepromTo.getReleaseTo());
        // insert to PIDB_IF_MATERIAL_MASTER
        IfMaterialMasterDao ifDao = new IfMaterialMasterDao();
        ifDao.insert(ifMaterialMasterTo, "PIDB_IF_MATERIAL_MASTER");
        
        //2.0 Insert to PIDB_IF_MATERIAL_CHARACTER
        IfMaterialCharacterDao ifCharDao = new IfMaterialCharacterDao();
        IfMaterialCharacterTo ifCharTo = new IfMaterialCharacterTo();
        //2.1 Default value
        ifCharTo.setMaterialNum(zsEepromTo.getMaterialNum());
        ifCharTo.setMaterialType(materialType);
        ifCharTo.setTimeStamp(new Date());
        ifCharTo.setReleasedBy(userTo.getUserId());
        //set Release_To
        ifCharTo.setReleaseTo(zsEepromTo.getReleaseTo());
        
        ifCharTo.setChTechName("SE_01");
        ifCharTo.setChValue(zsEepromTo.getDescription());
        ifCharTo.setId(SequenceSupport
                .nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
        ifCharDao.insert(ifCharTo, "PIDB_IF_MATERIAL_CHARACTER");
        
        
        String density = zsEepromTo.getDensity();
        density =null !=density?density:"";
        
        if (density.equals("")) {
        	return "ERP-10-002";
        }else{
	        ifCharTo.setChTechName("SE_02");
	        ifCharTo.setChValue(density);
	        ifCharTo.setId(SequenceSupport
	                .nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
	        //set Release_To
	        ifCharTo.setReleaseTo(zsEepromTo.getReleaseTo());
	        ifCharDao.insert(ifCharTo, "PIDB_IF_MATERIAL_CHARACTER");
        }
        
        String operationVoltage = zsEepromTo.getOperationVoltage();
        operationVoltage =null !=operationVoltage?operationVoltage:"";
        
        if (operationVoltage.equals("")) {
        	return "ERP-10-003";
        }else{
	        ifCharTo.setChTechName("SE_03");
	        ifCharTo.setChValue(operationVoltage);
	        ifCharTo.setId(SequenceSupport
	                .nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
	        //set Release_To
	        ifCharTo.setReleaseTo(zsEepromTo.getReleaseTo());
	        ifCharDao.insert(ifCharTo, "PIDB_IF_MATERIAL_CHARACTER");
        }
        
        String thickness = zsEepromTo.getThickness();
        thickness =null !=thickness?thickness:"";
        
        if (thickness.equals("")) {
        	return "ERP-10-004";
        }else{
	        ifCharTo.setChTechName("SE_04");
	        ifCharTo.setChValue(thickness);
	        ifCharTo.setId(SequenceSupport
	                .nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
	        //set Release_To
	        ifCharTo.setReleaseTo(zsEepromTo.getReleaseTo());
	        ifCharDao.insert(ifCharTo, "PIDB_IF_MATERIAL_CHARACTER");
        }
        
        String codeBind = zsEepromTo.getCodeBind();
        codeBind =null !=codeBind?codeBind:"";
        
        if (codeBind.equals("")) {
        	return "ERP-10-005";
        }else{
        	if ( codeBind.trim().equals("1")){
        		codeBind = "Yes";
        	}else{
        		codeBind = "No";
        	}
        	
	        ifCharTo.setChTechName("SE_05");
	        ifCharTo.setChValue(codeBind);
	        ifCharTo.setId(SequenceSupport
	                .nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
	        //set Release_To
	        ifCharTo.setReleaseTo(zsEepromTo.getReleaseTo());
	        ifCharDao.insert(ifCharTo, "PIDB_IF_MATERIAL_CHARACTER");
        }
        
        String vendorCode = zsEepromTo.getVendorCode();
        vendorCode =null !=vendorCode?vendorCode:"";
        
        if (vendorCode.equals("")) {
        	return "ERP-10-006";
        }else{
        	
	        ifCharTo.setChTechName("SE_06");
	        ifCharTo.setChValue(vendorCode);
	        ifCharTo.setId(SequenceSupport
	                .nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
	        //set Release_To
	        ifCharTo.setReleaseTo(zsEepromTo.getReleaseTo());
	        ifCharDao.insert(ifCharTo, "PIDB_IF_MATERIAL_CHARACTER");
        }
        
        return null;
    }
    
    public static String releaseHDCPKey(Object md, final UserTo userTo) {
    	
    	ZsHdcpKeyTo zsHdcpKeyTo = (ZsHdcpKeyTo) md;
    	//1.0 Insert to PIDB_IF_MATERIAL_MASTER
        IfMaterialMasterTo ifMaterialMasterTo = new IfMaterialMasterTo();
        
        // set materialNum and materialDesc and purchsing order text
        ifMaterialMasterTo.setMaterialNum(zsHdcpKeyTo.getMaterialNum());
        ifMaterialMasterTo.setMaterialDesc(zsHdcpKeyTo.getDescription());
        ifMaterialMasterTo
                .setPurchaseOrderText(zsHdcpKeyTo.getDescription());

        // set materialType
        String mt = "ZSHDCPKEY";
        String materialType = ERPHelper.getMaterialType(mt);
        ifMaterialMasterTo.setMaterialType(materialType);

        // set materilGroup
        mt = "ZSH.MATERIAL_GROUP";
        String materialGroup = ERPHelper.getMaterialType(mt);
        
        ifMaterialMasterTo.setMaterialGroup(materialGroup);
        //set Basic_Data_Text
        //ifMaterialMasterTo.setBasicDataText(basicDataText);
        // set ID
        ifMaterialMasterTo.setReleasedBy(userTo.getUserId());
        ifMaterialMasterTo.setId(SequenceSupport
                .nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
        ifMaterialMasterTo.setTimeStamp(new Date());
        //Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
        ifMaterialMasterTo.setMpStatus("1");
        
        //set Release_To
        ifMaterialMasterTo.setReleaseTo(zsHdcpKeyTo.getReleaseTo());
        // insert to PIDB_IF_MATERIAL_MASTER
        IfMaterialMasterDao ifDao = new IfMaterialMasterDao();
        ifDao.insert(ifMaterialMasterTo, "PIDB_IF_MATERIAL_MASTER");

        
        //2.0 Insert to PIDB_IF_MATERIAL_CHARACTER
        IfMaterialCharacterDao ifCharDao = new IfMaterialCharacterDao();
        IfMaterialCharacterTo ifCharTo = new IfMaterialCharacterTo();
        //2.1 Default value
        ifCharTo.setMaterialNum(zsHdcpKeyTo.getMaterialNum());
        ifCharTo.setMaterialType(materialType);
        ifCharTo.setTimeStamp(new Date());
        ifCharTo.setReleasedBy(userTo.getUserId());
        
        
        ifCharTo.setChTechName("SH_01");
        ifCharTo.setChValue(zsHdcpKeyTo.getDescription());
        ifCharTo.setId(SequenceSupport
                .nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
        //set Release_To
        ifCharTo.setReleaseTo(zsHdcpKeyTo.getReleaseTo());
        
        ifCharDao.insert(ifCharTo, "PIDB_IF_MATERIAL_CHARACTER");
        
        
        String vendor = zsHdcpKeyTo.getVendorCode();
        vendor =null !=vendor?vendor:"";
        
        if (vendor.equals("")) {
        	return "ERP-11-002";
        }else{
	        ifCharTo.setChTechName("SH_02");
	        ifCharTo.setChValue(vendor);
	        ifCharTo.setId(SequenceSupport
	                .nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
	        //set Release_To
	        ifCharTo.setReleaseTo(zsHdcpKeyTo.getReleaseTo());
	        ifCharDao.insert(ifCharTo, "PIDB_IF_MATERIAL_CHARACTER");
        }
        
    	return null;
    }

    public static String releaseSDRAM(Object md, final UserTo userTo) {
    	ZsSdramTo zsSdramTo = (ZsSdramTo) md;
    	//1.0 Insert to PIDB_IF_MATERIAL_MASTER
        IfMaterialMasterTo ifMaterialMasterTo = new IfMaterialMasterTo();
        
        // set materialNum and materialDesc and purchsing order text
        ifMaterialMasterTo.setMaterialNum(zsSdramTo.getMaterialNum());
        ifMaterialMasterTo.setMaterialDesc(zsSdramTo.getDescription());
        ifMaterialMasterTo
                .setPurchaseOrderText(zsSdramTo.getDescription());

        // set materialType
        String mt = "ZSDRAM";
        String materialType = ERPHelper.getMaterialType(mt);
        ifMaterialMasterTo.setMaterialType(materialType);

        // set materilGroup
        mt = "ZSD.MATERIAL_GROUP";
        String materialGroup = ERPHelper.getMaterialType(mt);
        
        ifMaterialMasterTo.setMaterialGroup(materialGroup);
        //set Basic_Data_Text
        //ifMaterialMasterTo.setBasicDataText(basicDataText);
        // set ID
        ifMaterialMasterTo.setReleasedBy(userTo.getUserId());
        ifMaterialMasterTo.setId(SequenceSupport
                .nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
        ifMaterialMasterTo.setTimeStamp(new Date());
        //Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
        ifMaterialMasterTo.setMpStatus("1");
        //set Release_To
        ifMaterialMasterTo.setReleaseTo(zsSdramTo.getReleaseTo());
        
        // insert to PIDB_IF_MATERIAL_MASTER
        IfMaterialMasterDao ifDao = new IfMaterialMasterDao();
        ifDao.insert(ifMaterialMasterTo, "PIDB_IF_MATERIAL_MASTER");

        //2.0 Insert to PIDB_IF_MATERIAL_CHARACTER
        IfMaterialCharacterDao ifCharDao = new IfMaterialCharacterDao();
        IfMaterialCharacterTo ifCharTo = new IfMaterialCharacterTo();
        //2.1 Default value
        ifCharTo.setMaterialNum(zsSdramTo.getMaterialNum());
        ifCharTo.setMaterialType(materialType);
        ifCharTo.setTimeStamp(new Date());
        ifCharTo.setReleasedBy(userTo.getUserId());
        //set Release_To
        ifCharTo.setReleaseTo(zsSdramTo.getReleaseTo());
        
        ifCharTo.setChTechName("SD_01");
        ifCharTo.setChValue(zsSdramTo.getDescription());
        ifCharTo.setId(SequenceSupport
                .nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
        ifCharDao.insert(ifCharTo, "PIDB_IF_MATERIAL_CHARACTER");
        
        
        String memorySize = zsSdramTo.getMemorySize();
        memorySize =null !=memorySize?memorySize:"";
        
        if (memorySize.equals("")) {
        	return "ERP-12-002";
        }else{
	        ifCharTo.setChTechName("SD_02");
	        ifCharTo.setChValue(memorySize);
	        ifCharTo.setId(SequenceSupport
	                .nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
	        //set Release_To
	        ifCharTo.setReleaseTo(zsSdramTo.getReleaseTo());
	        ifCharDao.insert(ifCharTo, "PIDB_IF_MATERIAL_CHARACTER");
        }

        String speed = zsSdramTo.getSpeed();
        speed =null !=speed?speed:"";
        
        if (speed.equals("")) {
        	return "ERP-12-003";
        }else{
	        ifCharTo.setChTechName("SD_03");
	        ifCharTo.setChValue(speed);
	        ifCharTo.setId(SequenceSupport
	                .nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
	        //set Release_To
	        ifCharTo.setReleaseTo(zsSdramTo.getReleaseTo());
	        ifCharDao.insert(ifCharTo, "PIDB_IF_MATERIAL_CHARACTER");
        }

        String vendor = zsSdramTo.getVendorCode();
        vendor =null !=vendor?vendor:"";
        
        if (vendor.equals("")) {
        	return "ERP-12-004";
        }else{
	        ifCharTo.setChTechName("SD_04");
	        ifCharTo.setChValue(vendor);
	        ifCharTo.setId(SequenceSupport
	                .nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
	        //set Release_To
	        ifCharTo.setReleaseTo(zsSdramTo.getReleaseTo());
	        ifCharDao.insert(ifCharTo, "PIDB_IF_MATERIAL_CHARACTER");
        }
        
    	return null;
    }
    
    public static String releaseFLASHRAM(Object md, final UserTo userTo) {
    	ZsFlashramTo zsFlashramTo = (ZsFlashramTo) md;
    	//1.0 Insert to PIDB_IF_MATERIAL_MASTER
        IfMaterialMasterTo ifMaterialMasterTo = new IfMaterialMasterTo();
        
        // set materialNum and materialDesc and purchsing order text
        ifMaterialMasterTo.setMaterialNum(zsFlashramTo.getMaterialNum());
        ifMaterialMasterTo.setMaterialDesc(zsFlashramTo.getDescription());
        ifMaterialMasterTo
                .setPurchaseOrderText(zsFlashramTo.getDescription());

        // set materialType
        String mt = "ZFLASHRAM";
        String materialType = ERPHelper.getMaterialType(mt);
        ifMaterialMasterTo.setMaterialType(materialType);

        // set materilGroup
        mt = "ZSF.MATERIAL_GROUP";
        String materialGroup = ERPHelper.getMaterialType(mt);
        
        ifMaterialMasterTo.setMaterialGroup(materialGroup);
        //set Basic_Data_Text
        //ifMaterialMasterTo.setBasicDataText(basicDataText);
        // set ID
        ifMaterialMasterTo.setReleasedBy(userTo.getUserId());
        ifMaterialMasterTo.setId(SequenceSupport
                .nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
        ifMaterialMasterTo.setTimeStamp(new Date());
        //Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
        ifMaterialMasterTo.setMpStatus("1");
        //set Release_To
        ifMaterialMasterTo.setReleaseTo(zsFlashramTo.getReleaseTo());
        
        // insert to PIDB_IF_MATERIAL_MASTER
        IfMaterialMasterDao ifDao = new IfMaterialMasterDao();
        ifDao.insert(ifMaterialMasterTo, "PIDB_IF_MATERIAL_MASTER");

        //2.0 Insert to PIDB_IF_MATERIAL_CHARACTER
        IfMaterialCharacterDao ifCharDao = new IfMaterialCharacterDao();
        IfMaterialCharacterTo ifCharTo = new IfMaterialCharacterTo();
        //2.1 Default value
        ifCharTo.setMaterialNum(zsFlashramTo.getMaterialNum());
        ifCharTo.setMaterialType(materialType);
        ifCharTo.setTimeStamp(new Date());
        ifCharTo.setReleasedBy(userTo.getUserId());
        //set Release_To
        ifCharTo.setReleaseTo(zsFlashramTo.getReleaseTo());
        
        ifCharTo.setChTechName("SF_01");
        ifCharTo.setChValue(zsFlashramTo.getDescription());
        ifCharTo.setId(SequenceSupport
                .nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
        ifCharDao.insert(ifCharTo, "PIDB_IF_MATERIAL_CHARACTER");
        
        
        String memorySize = zsFlashramTo.getMemorySize();
        memorySize =null !=memorySize?memorySize:"";
        
        if (memorySize.equals("")) {
        	return "ERP-13-002";
        }else{
	        ifCharTo.setChTechName("SF_02");
	        ifCharTo.setChValue(memorySize);
	        ifCharTo.setId(SequenceSupport
	                .nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
	        //set Release_To
	        ifCharTo.setReleaseTo(zsFlashramTo.getReleaseTo());
	        ifCharDao.insert(ifCharTo, "PIDB_IF_MATERIAL_CHARACTER");
        }

        String speed = zsFlashramTo.getSpeed();
        speed =null !=speed?speed:"";
        
        if (speed.equals("")) {
        	return "ERP-13-003";
        }else{
	        ifCharTo.setChTechName("SF_03");
	        ifCharTo.setChValue(speed);
	        ifCharTo.setId(SequenceSupport
	                .nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
	        //set Release_To
	        ifCharTo.setReleaseTo(zsFlashramTo.getReleaseTo());
	        ifCharDao.insert(ifCharTo, "PIDB_IF_MATERIAL_CHARACTER");
        }

        String vendor = zsFlashramTo.getVendorCode();
        vendor =null !=vendor?vendor:"";
        
        if (vendor.equals("")) {
        	return "ERP-13-004";
        }else{
	        ifCharTo.setChTechName("SF_04");
	        ifCharTo.setChValue(vendor);
	        ifCharTo.setId(SequenceSupport
	                .nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
	        //set Release_To
	        ifCharTo.setReleaseTo(zsFlashramTo.getReleaseTo());
	        ifCharDao.insert(ifCharTo, "PIDB_IF_MATERIAL_CHARACTER");
        }
        
    	return null;
    }
    
    public static String releaseUSBRAM(Object md, final UserTo userTo) {
    	ZsUsbramTo zsUsbramTo = (ZsUsbramTo) md;
    	//1.0 Insert to PIDB_IF_MATERIAL_MASTER
        IfMaterialMasterTo ifMaterialMasterTo = new IfMaterialMasterTo();
        
        // set materialNum and materialDesc and purchsing order text
        ifMaterialMasterTo.setMaterialNum(zsUsbramTo.getMaterialNum());
        ifMaterialMasterTo.setMaterialDesc(zsUsbramTo.getDescription());
        ifMaterialMasterTo
                .setPurchaseOrderText(zsUsbramTo.getDescription());

        // set materialType
        String mt = "ZUSBRAM";
        String materialType = ERPHelper.getMaterialType(mt);
        ifMaterialMasterTo.setMaterialType(materialType);

        // set materilGroup
        mt = "ZSU.MATERIAL_GROUP";
        String materialGroup = ERPHelper.getMaterialType(mt);
        
        ifMaterialMasterTo.setMaterialGroup(materialGroup);
        //set Basic_Data_Text
        //ifMaterialMasterTo.setBasicDataText(basicDataText);
        // set ID
        ifMaterialMasterTo.setReleasedBy(userTo.getUserId());
        ifMaterialMasterTo.setId(SequenceSupport
                .nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
        ifMaterialMasterTo.setTimeStamp(new Date());
        //Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
        ifMaterialMasterTo.setMpStatus("1");
        //set Release_To
        ifMaterialMasterTo.setReleaseTo(zsUsbramTo.getReleaseTo());
        
        // insert to PIDB_IF_MATERIAL_MASTER
        IfMaterialMasterDao ifDao = new IfMaterialMasterDao();
        ifDao.insert(ifMaterialMasterTo, "PIDB_IF_MATERIAL_MASTER");

        //2.0 Insert to PIDB_IF_MATERIAL_CHARACTER
        IfMaterialCharacterDao ifCharDao = new IfMaterialCharacterDao();
        IfMaterialCharacterTo ifCharTo = new IfMaterialCharacterTo();
        //2.1 Default value
        ifCharTo.setMaterialNum(zsUsbramTo.getMaterialNum());
        ifCharTo.setMaterialType(materialType);
        ifCharTo.setTimeStamp(new Date());
        ifCharTo.setReleasedBy(userTo.getUserId());
        //set Release_To
        ifCharTo.setReleaseTo(zsUsbramTo.getReleaseTo());
        
        ifCharTo.setChTechName("SU_01");
        ifCharTo.setChValue(zsUsbramTo.getDescription());
        ifCharTo.setId(SequenceSupport
                .nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
        ifCharDao.insert(ifCharTo, "PIDB_IF_MATERIAL_CHARACTER");
        
        
        String memorySize = zsUsbramTo.getMemorySize();
        memorySize =null !=memorySize?memorySize:"";
        
        if (memorySize.equals("")) {
        	return "ERP-14-002";
        }else{
	        ifCharTo.setChTechName("SU_02");
	        ifCharTo.setChValue(memorySize);
	        ifCharTo.setId(SequenceSupport
	                .nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
	        //set Release_To
	        ifCharTo.setReleaseTo(zsUsbramTo.getReleaseTo());
	        ifCharDao.insert(ifCharTo, "PIDB_IF_MATERIAL_CHARACTER");
        }

       /* String speed = zsUsbramTo.getSpeed();
        speed =null !=speed?speed:"";
        
        if (speed.equals("")) {
        	return "ERP-14-003";
        }else{
	        ifCharTo.setChTechName("SU_03");
	        ifCharTo.setChValue(speed);
	        ifCharTo.setId(SequenceSupport
	                .nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
	        ifCharDao.insert(ifCharTo, "PIDB_IF_MATERIAL_CHARACTER");
        }*/

        String vendor = zsUsbramTo.getVendorCode();
        vendor =null !=vendor?vendor:"";
        
        if (vendor.equals("")) {
        	return "ERP-14-004";
        }else{
	        ifCharTo.setChTechName("SU_04");
	        ifCharTo.setChValue(vendor);
	        ifCharTo.setId(SequenceSupport
	                .nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
	        //set Release_To
	        ifCharTo.setReleaseTo(zsUsbramTo.getReleaseTo());
	        ifCharDao.insert(ifCharTo, "PIDB_IF_MATERIAL_CHARACTER");
        }
        
    	return null;
    }
}
