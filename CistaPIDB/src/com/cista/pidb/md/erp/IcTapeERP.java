package com.cista.pidb.md.erp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cista.pidb.admin.to.UserTo;
import com.cista.pidb.code.dao.SapMasterPackageTypeDao;
import com.cista.pidb.code.to.SapMasterPackageTypeTo;
import com.cista.pidb.core.SequenceSupport;
import com.cista.pidb.md.dao.IfMaterialCharacterDao;
import com.cista.pidb.md.dao.IfMaterialMasterDao;
import com.cista.pidb.md.dao.ProductDao;
import com.cista.pidb.md.dao.ProjectDao;
import com.cista.pidb.md.to.IcTapeTo;
import com.cista.pidb.md.to.IfMaterialCharacterTo;
import com.cista.pidb.md.to.IfMaterialMasterTo;
import com.cista.pidb.md.to.ProductTo;
import com.cista.pidb.md.to.ProjectTo;

public class IcTapeERP {

	public static String release(Object o, final UserTo userTo) {
		IcTapeTo icTapeTo = (IcTapeTo) o;
		ProjectDao projectDao = new ProjectDao();
		IfMaterialMasterDao ifMaterialMasterDao = new IfMaterialMasterDao();
		IfMaterialCharacterDao ifMaterialCharacterDao = new IfMaterialCharacterDao();
		IfMaterialMasterTo ifMaterialMasterTo = new IfMaterialMasterTo();
		// Added on 3/9
		SapMasterPackageTypeDao sapMasterPackageTypeDao = new SapMasterPackageTypeDao();

		String md = "IcTape";
		String materialType = ERPHelper.getMaterialType(md);
		String tapeType = icTapeTo.getTapeType();

		ProductDao productDao = new ProductDao();
		List<ProductTo> prodList = productDao.findByProdName(icTapeTo
				.getProdName());
		String prodCodeList = "";
		if (prodList != null) {
			for (ProductTo productTo : prodList) {
				prodCodeList += "," + productTo.getProdCode();
			}
		}
		if (prodCodeList.length() > 0) {
			prodCodeList = prodCodeList.substring(1);
		}

		List<ProjectTo> projectToList = projectDao
				.findByProdCodes(prodCodeList);
		String pitch = "";
		if (projectToList != null && projectToList.size() > 0) {
			pitch = projectToList.get(0).getPitch();
		} else {
			return "ERP-05-001";
		}

		if (StringUtils.isEmpty(tapeType)) {
			return "ERP-05-002";
		}
		if (StringUtils.isEmpty(icTapeTo.getTapeWidth())) {
			return "ERP-05-003";
		}
		if (StringUtils.isEmpty(icTapeTo.getSprocketHoleNum())) {
			return "ERP-05-004";
		}
		if (StringUtils.isEmpty(icTapeTo.getMinPitch())) {
			return "ERP-05-005";
		}
		if (StringUtils.isEmpty(icTapeTo.getTapeVendor())) {
			return "ERP-05-006";
		}
		if (StringUtils.isEmpty(icTapeTo.getTapeCustProjName())) {
			return "ERP-05-007";
		}
		if (StringUtils.isEmpty(pitch)) {
			return "ERP-05-008";
		}
		if (StringUtils.isEmpty(icTapeTo.getTapeProcess())) {
			return "ERP-05-009";
		}
		if (StringUtils.isEmpty(icTapeTo.getCuLayer())) {
			return "ERP-05-010";
		}
		if (StringUtils.isEmpty(icTapeTo.getCuThicknessPattern())) {
			return "ERP-05-011";
		}
		if (StringUtils.isEmpty(icTapeTo.getCuThicknessBack())) {
			return "ERP-05-012";
		}
		String tapeWidth = icTapeTo.getTapeWidth();
		if (tapeWidth.endsWith("SW")) {
			tapeWidth = tapeWidth.substring(0, tapeWidth.length() - 2);
		} else if (tapeWidth.endsWith("W")) {
			tapeWidth = tapeWidth.substring(0, tapeWidth.length() - 1);
		}

		String materialGroup = ERPHelper.getMaterialGroup(materialType,
				tapeType);

		ifMaterialMasterTo.setMaterialNum(icTapeTo.getMaterialNum());
		ifMaterialMasterTo.setMaterialType(materialType);
		ifMaterialMasterTo.setMaterialDesc(icTapeTo.getTapeName());
		ifMaterialMasterTo.setMaterialGroup(materialGroup);
		ifMaterialMasterTo.setPurchaseOrderText(icTapeTo.getTapeName());
		ifMaterialMasterTo.setBasicDataText(tapeWidth + "mm/"
				+ icTapeTo.getSprocketHoleNum() + "sph/"
				+ icTapeTo.getMinPitch() + "um");
		ifMaterialMasterTo.setTimeStamp(new Date());
		ifMaterialMasterTo.setReleasedBy(userTo.getUserId());
		ifMaterialMasterTo.setReleaseTo(icTapeTo.getReleaseTo());
		ifMaterialMasterTo.setId(SequenceSupport
				.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
		// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
		ifMaterialMasterTo.setMpStatus(ERPHelper.judgeMPByTapeName(icTapeTo
				.getTapeName()));

		ifMaterialMasterDao.insert(ifMaterialMasterTo,
				"PIDB_IF_MATERIAL_MASTER");

		// Added on 3/9
		SapMasterPackageTypeTo sapMasterPackageTypeTo = sapMasterPackageTypeDao
				.findByPkgType(icTapeTo.getTapeType());

		Map<String, String> classes = new HashMap<String, String>();
		classes.put("TP_01", icTapeTo.getTapeVendor());
		// classes.put("TP_02", icTapeTo.getTapeType());
		// Added on 3/9
		classes.put("TP_02", sapMasterPackageTypeTo.getDescription());
		classes.put("TP_03", tapeWidth);
		classes.put("TP_04", icTapeTo.getSprocketHoleNum());
		classes.put("TP_05", pitch);
		classes.put("TP_06", icTapeTo.getMinPitch());
		classes.put("TP_07", icTapeTo.getTapeProcess());
		classes.put("TP_08", icTapeTo.getCuLayer());
		classes.put("TP_09", icTapeTo.getCuThicknessPattern());
		classes.put("TP_10", icTapeTo.getCuThicknessBack());

		List<IfMaterialCharacterTo> charList = new ArrayList<IfMaterialCharacterTo>();
		for (String s : classes.keySet()) {
			IfMaterialCharacterTo to = new IfMaterialCharacterTo();
			to.setMaterialNum(icTapeTo.getMaterialNum());
			to.setMaterialType(materialType);
			to.setChTechName(s);
			to.setChValue(classes.get(s));
			to.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
			to.setTimeStamp(new Date());
			to.setReleasedBy(userTo.getUserId());
			to.setReleaseTo(icTapeTo.getReleaseTo());
			charList.add(to);
		}

		ifMaterialCharacterDao.batchInsert(charList,
				"PIDB_IF_MATERIAL_CHARACTER");

		return null;
	}

	public static String releaseForProject(Object o, final UserTo userTo) {
		IcTapeTo icTapeTo = (IcTapeTo) o;
		ProjectDao projectDao = new ProjectDao();
		IfMaterialMasterDao ifMaterialMasterDao = new IfMaterialMasterDao();
		IfMaterialCharacterDao ifMaterialCharacterDao = new IfMaterialCharacterDao();
		IfMaterialMasterTo ifMaterialMasterTo = new IfMaterialMasterTo();
		// Added on 3/9
		SapMasterPackageTypeDao sapMasterPackageTypeDao = new SapMasterPackageTypeDao();

		String md = "IcTape";
		String materialType = ERPHelper.getMaterialType(md);
		String tapeType = icTapeTo.getTapeType();

		ProductDao productDao = new ProductDao();
		List<ProductTo> prodList = productDao.findByProdName(icTapeTo
				.getProdName());
		String prodCodeList = "";
		if (prodList != null) {
			for (ProductTo productTo : prodList) {
				prodCodeList += "," + productTo.getProdCode();
			}
		}
		if (prodCodeList.length() > 0) {
			prodCodeList = prodCodeList.substring(1);
		}

		List<ProjectTo> projectToList = projectDao
				.findByProdCodes(prodCodeList);
		String pitch = "";
		if (projectToList != null && projectToList.size() > 0) {
			pitch = projectToList.get(0).getPitch();
		} else {
			return "ERP-05-001";
		}

		if (StringUtils.isEmpty(tapeType)) {
			// return "ERP-05-002";
		}
		if (StringUtils.isEmpty(icTapeTo.getTapeWidth())) {
			// return "ERP-05-003";
		}
		if (StringUtils.isEmpty(icTapeTo.getSprocketHoleNum())) {
			// return "ERP-05-004";
		}
		if (StringUtils.isEmpty(icTapeTo.getMinPitch())) {
			// return "ERP-05-005";
		}
		if (StringUtils.isEmpty(icTapeTo.getTapeVendor())) {
			// return "ERP-05-006";
		}
		if (StringUtils.isEmpty(icTapeTo.getTapeCustProjName())) {
			// return "ERP-05-007";
		}
		if (StringUtils.isEmpty(pitch)) {
			return "ERP-05-008";
		}
		if (StringUtils.isEmpty(icTapeTo.getTapeProcess())) {
			// return "ERP-05-009";
		}
		if (StringUtils.isEmpty(icTapeTo.getCuLayer())) {
			// return "ERP-05-010";
		}
		if (StringUtils.isEmpty(icTapeTo.getCuThicknessPattern())) {
			// return "ERP-05-011";
		}
		if (StringUtils.isEmpty(icTapeTo.getCuThicknessBack())) {
			// return "ERP-05-012";
		}
		String tapeWidth = icTapeTo.getTapeWidth();
		if (tapeWidth.endsWith("SW")) {
			tapeWidth = tapeWidth.substring(0, tapeWidth.length() - 2);
		} else if (tapeWidth.endsWith("W")) {
			tapeWidth = tapeWidth.substring(0, tapeWidth.length() - 1);
		}

		String materialGroup = ERPHelper.getMaterialGroup(materialType,
				tapeType);

		ifMaterialMasterTo.setMaterialNum(icTapeTo.getMaterialNum());
		ifMaterialMasterTo.setMaterialType(materialType);
		ifMaterialMasterTo.setMaterialDesc(icTapeTo.getTapeName());
		ifMaterialMasterTo.setMaterialGroup(materialGroup);
		ifMaterialMasterTo.setPurchaseOrderText(icTapeTo.getTapeName());
		ifMaterialMasterTo.setBasicDataText(tapeWidth + "mm/"
				+ icTapeTo.getSprocketHoleNum() + "sph/"
				+ icTapeTo.getMinPitch() + "um");
		ifMaterialMasterTo.setTimeStamp(new Date());
		ifMaterialMasterTo.setReleasedBy(userTo.getUserId());

        //將Release_To 塞到 PIDB_IF_MATERIAL Table中
		ifMaterialMasterTo.setReleaseTo(icTapeTo.getReleaseTo());
        
		ifMaterialMasterTo.setId(SequenceSupport
				.nextValue(SequenceSupport.SEQ_IF_MATERIAL_MASTER));
		// Add 2008/01/03 Hank To Judge MP->1 or Non-MP->2 or EOL -> 0
		ifMaterialMasterTo.setMpStatus(ERPHelper.judgeMPByTapeName(icTapeTo
				.getTapeName()));

		ifMaterialMasterDao.insert(ifMaterialMasterTo,
				"PIDB_IF_MATERIAL_MASTER");

		// Added on 3/9
		SapMasterPackageTypeTo sapMasterPackageTypeTo = sapMasterPackageTypeDao
				.findByPkgType(icTapeTo.getTapeType());

		Map<String, String> classes = new HashMap<String, String>();
		if (icTapeTo.getTapeVendor() != null
				&& !icTapeTo.getTapeVendor().equals("")) {
			classes.put("TP_01", icTapeTo.getTapeVendor());
			// classes.put("TP_02", icTapeTo.getTapeType());
			// Added on 3/9
		}
		if (sapMasterPackageTypeTo.getDescription() != null
				&& !sapMasterPackageTypeTo.getDescription().equals("")) {
			classes.put("TP_02", sapMasterPackageTypeTo.getDescription());
		}
		if (tapeWidth != null && !tapeWidth.equals("")) {
			classes.put("TP_03", tapeWidth);
		}
		if (icTapeTo.getSprocketHoleNum() != null
				&& !icTapeTo.getSprocketHoleNum().equals("")) {
			classes.put("TP_04", icTapeTo.getSprocketHoleNum());
		}
		if (pitch != null && !pitch.equals("")) {
			classes.put("TP_05", pitch);
		}
		if (icTapeTo.getMinPitch() != null
				&& !icTapeTo.getMinPitch().equals("")) {
			classes.put("TP_06", icTapeTo.getMinPitch());
		}
		if (icTapeTo.getTapeProcess() != null
				&& !icTapeTo.getTapeProcess().equals("")) {
			classes.put("TP_07", icTapeTo.getTapeProcess());
		}
		if (icTapeTo.getCuLayer() != null && !icTapeTo.getCuLayer().equals("")) {
			classes.put("TP_08", icTapeTo.getCuLayer());
		}
		if (icTapeTo.getCuThicknessPattern() != null
				&& !icTapeTo.getCuThicknessPattern().equals("")) {
			classes.put("TP_09", icTapeTo.getCuThicknessPattern());
		}
		if (icTapeTo.getCuThicknessBack() != null
				&& !icTapeTo.getCuThicknessBack().equals("")) {
			classes.put("TP_10", icTapeTo.getCuThicknessBack());
		}

		List<IfMaterialCharacterTo> charList = new ArrayList<IfMaterialCharacterTo>();
		for (String s : classes.keySet()) {
			IfMaterialCharacterTo to = new IfMaterialCharacterTo();
			to.setMaterialNum(icTapeTo.getMaterialNum());
			to.setMaterialType(materialType);
			to.setChTechName(s);
			to.setChValue(classes.get(s));
			to.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_IF_MATERIAL_CHARACTER));
			to.setTimeStamp(new Date());
			to.setReleasedBy(userTo.getUserId());

	        //將Release_To 塞到 PIDB_IF_MATERIAL Table中
			to.setReleaseTo(icTapeTo.getReleaseTo());
	        
			charList.add(to);
		}

		ifMaterialCharacterDao.batchInsert(charList,
				"PIDB_IF_MATERIAL_CHARACTER");

		return null;
	}

}
