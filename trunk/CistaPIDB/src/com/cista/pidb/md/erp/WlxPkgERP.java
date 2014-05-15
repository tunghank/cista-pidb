package com.cista.pidb.md.erp;


import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cista.pidb.admin.to.UserTo;
import com.cista.pidb.core.SequenceSupport;
import com.cista.pidb.md.dao.IfMaterialMasterDao;
import com.cista.pidb.md.dao.ProjectDao;
import com.cista.pidb.md.to.IfMaterialMasterTo;
import com.cista.pidb.md.to.ProjectTo;
import com.cista.pidb.md.to.WlhTo;
import com.cista.pidb.md.to.WlmTo;
import com.cista.pidb.md.to.WloTo;



public class WlxPkgERP {

	/** Logger. */
	private static Log logger = LogFactory.getLog(WlxPkgERP.class);


	/**
	 * Added 2010/07/28 TSV Release to SAP
	 * 
	 * @param md
	 * @param userTo
	 * @return
	 */
	public static String releaseWLO(final Object md,
			final UserTo userTo) {
		WloTo wloTo = (WloTo) md;
		IfMaterialMasterTo ifMaterialMasterTo = new IfMaterialMasterTo();
		IfMaterialMasterDao ifMaterialMasterDao = new IfMaterialMasterDao();
		ProjectDao projectDao = new ProjectDao();
		
		String materialType = "ZOFG";
		if (materialType == null || materialType.length() <= 0) {
			return "ERP-02-022";
		}

		//Material Master
		ifMaterialMasterTo.setMaterialNum(wloTo.getMaterialNum());
		ifMaterialMasterTo.setMaterialType(materialType);
		if (wloTo.getDescription() != null && !wloTo.getDescription().equals("")) {
			ifMaterialMasterTo.setMaterialDesc(wloTo.getDescription());
		} else {
			ifMaterialMasterTo.setMaterialDesc(wloTo.getPartNum());
		}

		ifMaterialMasterTo.setTimeStamp(new Date());
		ifMaterialMasterTo.setId(SequenceSupport
				.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
		ifMaterialMasterTo.setReleasedBy(userTo.getUserId());

		ProjectTo projectTo = projectDao
						.findByProjectCode(wloTo.getProjCode());
		if (projectTo == null) {
				// return "Project is not exist!";
			return "ERP-04-002";
		}
		String prodFamily = projectTo.getProdFamily();

		// 將Release_To 塞到 pidb_if_material_master Table中
		ifMaterialMasterTo.setReleaseTo("HX");
		ifMaterialMasterTo.setProductFamily(prodFamily);
		ifMaterialMasterTo.setPkgType("312");
		ifMaterialMasterTo.setIcType(projectTo.getIcType());
		ifMaterialMasterTo.setAppCategory(wloTo.getAppCategory()); //未
		String materialGroup = "18070";
		// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
		ifMaterialMasterTo.setMpStatus(wloTo.getMpStatus());
		
		//Basic Data , Purchase Order Text
		ifMaterialMasterTo.setPurchaseOrderText(wloTo.getPartNum());
		ifMaterialMasterTo.setBasicDataText(wloTo.getVendorDevice());
		
		if (materialGroup != null || materialGroup.length() > 0) {
			ifMaterialMasterTo.setMaterialGroup(materialGroup);
			ifMaterialMasterDao.insert(ifMaterialMasterTo,
					"PIDB_IF_MATERIAL_MASTER");
		} else {
			return "ERP-02-016";
		}
		
		return null;
	}

	public static String releaseWLH(final Object md,
			final UserTo userTo) {
		WlhTo wlhTo = (WlhTo) md;
		IfMaterialMasterTo ifMaterialMasterTo = new IfMaterialMasterTo();
		IfMaterialMasterDao ifMaterialMasterDao = new IfMaterialMasterDao();
		ProjectDao projectDao = new ProjectDao();
		
		String materialType = "ZHFG";
		if (materialType == null || materialType.length() <= 0) {
			return "ERP-02-022";
		}

		//Material Master
		ifMaterialMasterTo.setMaterialNum(wlhTo.getMaterialNum());
		ifMaterialMasterTo.setMaterialType(materialType);
		if (wlhTo.getDescription() != null && !wlhTo.getDescription().equals("")) {
			ifMaterialMasterTo.setMaterialDesc(wlhTo.getDescription());
		} else {
			ifMaterialMasterTo.setMaterialDesc(wlhTo.getPartNum());
		}

		ifMaterialMasterTo.setTimeStamp(new Date());
		ifMaterialMasterTo.setId(SequenceSupport
				.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
		ifMaterialMasterTo.setReleasedBy(userTo.getUserId());

		ProjectTo projectTo = projectDao
						.findByProjectCode(wlhTo.getProjCode());
		if (projectTo == null) {
				// return "Project is not exist!";
			return "ERP-04-002";
		}
		String prodFamily = projectTo.getProdFamily();

		// 將Release_To 塞到 pidb_if_material_master Table中
		ifMaterialMasterTo.setReleaseTo("HX");
		ifMaterialMasterTo.setProductFamily(prodFamily);
		ifMaterialMasterTo.setPkgType("315");
		ifMaterialMasterTo.setIcType(projectTo.getIcType());
		ifMaterialMasterTo.setAppCategory(wlhTo.getAppCategory()); //未
		String materialGroup = "18090";
		// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
		ifMaterialMasterTo.setMpStatus(wlhTo.getMpStatus());
		//Basic Data , Purchase Order Text
		ifMaterialMasterTo.setPurchaseOrderText(wlhTo.getPartNum());
		ifMaterialMasterTo.setBasicDataText(wlhTo.getVendorDevice());
		
		if (materialGroup != null || materialGroup.length() > 0) {
			ifMaterialMasterTo.setMaterialGroup(materialGroup);
			ifMaterialMasterDao.insert(ifMaterialMasterTo,
					"PIDB_IF_MATERIAL_MASTER");
		} else {
			return "ERP-02-016";
		}
		
		return null;
	}
	
	public static String releaseWLM(final Object md,
			final UserTo userTo) {
		WlmTo wlmTo = (WlmTo) md;
		IfMaterialMasterTo ifMaterialMasterTo = new IfMaterialMasterTo();
		IfMaterialMasterDao ifMaterialMasterDao = new IfMaterialMasterDao();
		ProjectDao projectDao = new ProjectDao();
		
		String materialType = "";
		
		if( wlmTo.getPackingType().equals("T")){
			materialType = "ZTFG";
		}else{//W,0
			materialType = "ZAFG";
		}
		
		if (materialType == null || materialType.length() <= 0 || materialType.equals("") ) {
			return "ERP-02-022";
		}

		//Material Master
		ifMaterialMasterTo.setMaterialNum(wlmTo.getMaterialNum());
		ifMaterialMasterTo.setMaterialType(materialType);
		if (wlmTo.getDescription() != null && !wlmTo.getDescription().equals("")) {
			ifMaterialMasterTo.setMaterialDesc(wlmTo.getDescription());
		} else {
			ifMaterialMasterTo.setMaterialDesc(wlmTo.getPartNum());
		}

		ifMaterialMasterTo.setTimeStamp(new Date());
		ifMaterialMasterTo.setId(SequenceSupport
				.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
		ifMaterialMasterTo.setReleasedBy(userTo.getUserId());

		ProjectTo projectTo = projectDao
						.findByProjectCode(wlmTo.getProjCode());
		if (projectTo == null) {
			// return "Project is not exist!";
			return "ERP-04-002";
		}
		String prodFamily = projectTo.getProdFamily();

		// 將Release_To 塞到 pidb_if_material_master Table中
		ifMaterialMasterTo.setReleaseTo("HX");
		ifMaterialMasterTo.setProductFamily(prodFamily);
		ifMaterialMasterTo.setPkgType("313");
		ifMaterialMasterTo.setIcType(projectTo.getIcType());
		ifMaterialMasterTo.setAppCategory(wlmTo.getAppCategory()); //未
		String materialGroup = "18080";
		// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
		ifMaterialMasterTo.setMpStatus(wlmTo.getMpStatus());
		//Basic Data , Purchase Order Text
		ifMaterialMasterTo.setPurchaseOrderText(wlmTo.getPartNum());
		ifMaterialMasterTo.setBasicDataText(wlmTo.getVendorDevice());
		
		if (materialGroup != null || materialGroup.length() > 0) {
			ifMaterialMasterTo.setMaterialGroup(materialGroup);
			ifMaterialMasterDao.insert(ifMaterialMasterTo,
					"PIDB_IF_MATERIAL_MASTER");
		} else {
			return "ERP-02-016";
		}
		
		return null;
	}
}
