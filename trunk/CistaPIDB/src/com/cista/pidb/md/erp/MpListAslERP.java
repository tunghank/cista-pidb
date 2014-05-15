package com.cista.pidb.md.erp;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cista.pidb.admin.to.UserTo;
import com.cista.pidb.code.dao.FabCodeDao;
import com.cista.pidb.code.dao.SapDefaultSingleValueDao;
import com.cista.pidb.core.SequenceSupport;
import com.cista.pidb.md.dao.IcFgDao;
import com.cista.pidb.md.dao.IcTapeDao;
import com.cista.pidb.md.dao.MpListDao;
import com.cista.pidb.md.dao.PidbSourceListDao;
import com.cista.pidb.md.dao.ProjectCodeDao;
import com.cista.pidb.md.to.IcFgTo;
import com.cista.pidb.md.to.IcTapeTo;
import com.cista.pidb.md.to.IcWaferTo;
import com.cista.pidb.md.to.MpListTo;
import com.cista.pidb.md.to.PidbSourceListTo;
import com.cista.pidb.md.to.ProjectCodeTo;

public class MpListAslERP {

	/** Logger. */
	private static Log logger = LogFactory.getLog(MpListAslERP.class);

	public static String release(Object md, final UserTo userTo) {

		MpListTo mpListTo = (MpListTo) md;
		MpListDao mpListDao = new MpListDao();
		PidbSourceListTo psListTo = new PidbSourceListTo();
		PidbSourceListDao pidbSourceListDao = new PidbSourceListDao();
		SapDefaultSingleValueDao sapDefaultSingleValueDao = new SapDefaultSingleValueDao();

		// 1.0 SAP Default value
		String plantCode = sapDefaultSingleValueDao.findDefaultValue("plant");
		plantCode = null != plantCode ? plantCode : "";

		// 1.1 Set Bump List
		String wf = mpListTo.getMatWf();
		wf = null != wf ? wf : "";

		FabCodeDao fabCodeDao = new FabCodeDao();

		String wfVendor = fabCodeDao.findWFVendorCodeByPartNum(wf);
		wfVendor = null != wfVendor ? wfVendor : "";

		// 1.2 Set Bump List
		String bump = mpListTo.getMatBp();
		bump = null != bump ? bump : "";

		String bumpVendor = mpListTo.getApproveBpVendor();
		bumpVendor = null != bumpVendor ? bumpVendor : "";

		// 1.3
		String cp = mpListTo.getMatCp();
		cp = null != cp ? cp : "";

		String cpVendor = mpListTo.getApproveCpHouse();
		cpVendor = null != cpVendor ? cpVendor : "";

		// 1.3.1
		String polish = mpListTo.getMatPolish();
		polish = null != polish ? polish : "";

		String polishVendor = mpListTo.getApprovePolishVendor();
		polishVendor = null != polishVendor ? polishVendor : "";

		// 1.3.2
		String colorFilter = mpListTo.getMatCf();
		colorFilter = null != colorFilter ? colorFilter : "";

		String colorFilterVendor = mpListTo.getApproveColorFilterVendor();
		colorFilterVendor = null != colorFilterVendor ? colorFilterVendor : "";

		// 1.3.3
		String waferCF = mpListTo.getMatWafercf();
		waferCF = null != waferCF ? waferCF : "";

		String waferCFVendor = mpListTo.getApproveWaferCfVendor();
		waferCFVendor = null != waferCFVendor ? waferCFVendor : "";
		
		// 1.3.4
		String csp = mpListTo.getMatCsp();
		csp = null != csp ? csp : "";

		String cspVendor = mpListTo.getApproveCpCspVendor();
		cspVendor = null != cspVendor ? cspVendor : "";

		// 1.3.5 TSV
		String tsv = mpListTo.getMatTsv();
		tsv = null != tsv ? tsv : "";

		String tsvVendor = mpListTo.getApproveCpTsvVendor();
		tsvVendor = null != tsvVendor ? tsvVendor : "";
		
		// 1.4
		String assy = mpListTo.getMatAs();
		assy = null != assy ? assy : "";

		String assyVendor = mpListTo.getApproveAssyHouse();
		assyVendor = null != assyVendor ? assyVendor : "";

		// 1.4.1 Get Pkg_tpye
		String icFgMaterialNum = mpListTo.getIcFgMaterialNum();
		icFgMaterialNum = null != icFgMaterialNum ? icFgMaterialNum : "";
		IcFgDao icFgDao = new IcFgDao();
		IcFgTo icFgToMaterial = icFgDao.findByMaterialNum(icFgMaterialNum);
		String pkgType = "";
		List<MpListTo> upAssyMpListToList;
		if (icFgToMaterial != null) {
			pkgType = icFgToMaterial.getPkgType();
		}

		// 1.5
		String tape = mpListTo.getMatTape();
		tape = null != tape ? tape : "";

		// 1.6
		String ft = mpListTo.getIcFgMaterialNum();
		ft = null != ft ? ft : "";

		String ftVendor = mpListTo.getApproveFtHouse();
		ftVendor = null != ftVendor ? ftVendor : "";

		String tapeVendor = mpListTo.getApproveTapeVendor();
		tapeVendor = null != tapeVendor ? tapeVendor : "";

		// 取得projectCode中的Release_To值
		ProjectCodeTo projectCode = new ProjectCodeDao()
				.findByProjectCode(mpListTo.getProjCode());

		if (!wf.equals("") && !wfVendor.equals("")) {
			String mpStatus = ERPHelper.judgeMPByProjWVersion(mpListTo
					.getProjCodeWVersion());

			psListTo.setMaterialNum(wf);
			psListTo.setMpStatus(mpStatus);
			psListTo.setPlantCode(plantCode);
			psListTo.setReleasedBy(userTo.getUserId());
			psListTo.setVendor(wfVendor);
			// 將Release_To 塞到 APL Table中
			psListTo.setReleaseTo(projectCode.getReleaseTo());
			psListTo.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_PIDB_SOURCE_LIST));
			pidbSourceListDao.insert(psListTo, "PIDB_SOURCE_LIST");
		}

		if (!bump.equals("") && !bumpVendor.equals("")) {
			String mpStatus = ERPHelper.judgeMPByProjWVersion(mpListTo
					.getProjCodeWVersion());

			psListTo.setMaterialNum(bump);
			psListTo.setMpStatus(mpStatus);
			psListTo.setPlantCode(plantCode);
			psListTo.setReleasedBy(userTo.getUserId());
			psListTo.setReleaseTo(projectCode.getReleaseTo());

			List<String> bumpVendorList = strToList(bumpVendor);
			for (String bumpTo : bumpVendorList) {
				psListTo.setId(SequenceSupport
						.nextValue(SequenceSupport.SEQ_PIDB_SOURCE_LIST));
				psListTo.setVendor(bumpTo);
				pidbSourceListDao.insert(psListTo, "PIDB_SOURCE_LIST");
			}
		}

		if (!cp.equals("") && !cpVendor.equals("")) {
			String mpStatus = ERPHelper.judgeMPByProjWVersion(mpListTo
					.getProjCodeWVersion());

			psListTo.setMaterialNum(cp);
			psListTo.setMpStatus(mpStatus);
			psListTo.setPlantCode(plantCode);
			psListTo.setReleasedBy(userTo.getUserId());
			psListTo.setReleaseTo(projectCode.getReleaseTo());

			List<String> cpVendorList = strToList(cpVendor);
			for (String cpTo : cpVendorList) {
				psListTo.setId(SequenceSupport
						.nextValue(SequenceSupport.SEQ_PIDB_SOURCE_LIST));
				psListTo.setVendor(cpTo);
				pidbSourceListDao.insert(psListTo, "PIDB_SOURCE_LIST");
			}
		}
		// Polish For ASL
		if (!polish.equals("") && !polishVendor.equals("")) {
			String mpStatus = ERPHelper.judgeMPByProjWVersion(mpListTo
					.getProjCodeWVersion());

			psListTo.setMaterialNum(polish);
			psListTo.setMpStatus(mpStatus);
			psListTo.setPlantCode(plantCode);
			psListTo.setReleasedBy(userTo.getUserId());
			psListTo.setReleaseTo(projectCode.getReleaseTo());

			List<String> polishVendorList = strToList(polishVendor);
			for (String polishTo : polishVendorList) {
				psListTo.setId(SequenceSupport
						.nextValue(SequenceSupport.SEQ_PIDB_SOURCE_LIST));
				psListTo.setVendor(polishTo);
				pidbSourceListDao.insert(psListTo, "PIDB_SOURCE_LIST");
			}
		}

		// COlor Filter For ASL
		if (!colorFilter.equals("") && !colorFilterVendor.equals("")) {
			String mpStatus = ERPHelper.judgeMPByProjWVersion(mpListTo
					.getProjCodeWVersion());

			psListTo.setMaterialNum(colorFilter);
			psListTo.setMpStatus(mpStatus);
			psListTo.setPlantCode(plantCode);
			psListTo.setReleasedBy(userTo.getUserId());
			psListTo.setReleaseTo(projectCode.getReleaseTo());

			List<String> colorFilterList = strToList(colorFilterVendor);
			for (String colorFilterTo : colorFilterList) {
				psListTo.setId(SequenceSupport
						.nextValue(SequenceSupport.SEQ_PIDB_SOURCE_LIST));
				psListTo.setVendor(colorFilterTo);
				pidbSourceListDao.insert(psListTo, "PIDB_SOURCE_LIST");
			}
		}

		// TurnKey(Wafer + COlor Filter) For ASL
		if (!waferCF.equals("") && !waferCFVendor.equals("")) {
			String mpStatus = ERPHelper.judgeMPByProjWVersion(mpListTo
					.getProjCodeWVersion());

			psListTo.setMaterialNum(waferCF);
			psListTo.setMpStatus(mpStatus);
			psListTo.setPlantCode(plantCode);
			psListTo.setReleasedBy(userTo.getUserId());
			psListTo.setReleaseTo(projectCode.getReleaseTo());

			List<String> waferCFList = strToList(waferCFVendor);
			for (String waferCFTo : waferCFList) {
				psListTo.setId(SequenceSupport
						.nextValue(SequenceSupport.SEQ_PIDB_SOURCE_LIST));
				psListTo.setVendor(waferCFTo);
				pidbSourceListDao.insert(psListTo, "PIDB_SOURCE_LIST");
			}
		}
		
		// CSP For ASL
		if (!csp.equals("") && !cspVendor.equals("")) {
			String mpStatus = ERPHelper.judgeMPByProjWVersion(mpListTo
					.getProjCodeWVersion());

			psListTo.setMaterialNum(csp);
			psListTo.setMpStatus(mpStatus);
			psListTo.setPlantCode(plantCode);
			psListTo.setReleasedBy(userTo.getUserId());
			psListTo.setReleaseTo(projectCode.getReleaseTo());

			List<String> cspVendorList = strToList(cspVendor);
			for (String cspTo : cspVendorList) {
				psListTo.setId(SequenceSupport
						.nextValue(SequenceSupport.SEQ_PIDB_SOURCE_LIST));
				psListTo.setVendor(cspTo);
				pidbSourceListDao.insert(psListTo, "PIDB_SOURCE_LIST");
			}
		}

		// TSV For ASL
		if (!tsv.equals("") && !tsvVendor.equals("")) {
			String mpStatus = ERPHelper.judgeMPByProjWVersion(mpListTo
					.getProjCodeWVersion());

			psListTo.setMaterialNum(tsv);
			psListTo.setMpStatus(mpStatus);
			psListTo.setPlantCode(plantCode);
			psListTo.setReleasedBy(userTo.getUserId());
			psListTo.setReleaseTo(projectCode.getReleaseTo());

			List<String> tsvVendorList = strToList(tsvVendor);
			
			for (String tsvTo : tsvVendorList) {
				psListTo.setId(SequenceSupport
						.nextValue(SequenceSupport.SEQ_PIDB_SOURCE_LIST));
				psListTo.setVendor(tsvTo);
				pidbSourceListDao.insert(psListTo, "PIDB_SOURCE_LIST");
			}
		}
		
		if (!pkgType.equals("")) {
			if (pkgType.equals("303")) { // Just release F 料號

				if (!ft.equals("") && !assyVendor.equals("")) {
					String mpStatus = ERPHelper.judgeMP(mpListTo
							.getIcFgMaterialNum());

					psListTo.setMaterialNum(ft);
					psListTo.setMpStatus(mpStatus);
					psListTo.setPlantCode(plantCode);
					psListTo.setReleasedBy(userTo.getUserId());
					psListTo.setReleaseTo(projectCode.getReleaseTo());
					List<String> assyVendorList = strToList(assyVendor);
					for (String assyTo : assyVendorList) {
						psListTo
								.setId(SequenceSupport
										.nextValue(SequenceSupport.SEQ_PIDB_SOURCE_LIST));
						psListTo.setVendor(assyTo);
						pidbSourceListDao.insert(psListTo, "PIDB_SOURCE_LIST");
					}

				}
			} else {

				if (!assy.equals("") && !assyVendor.equals("")) {
					String mpStatus = ERPHelper.judgeMP(mpListTo
							.getIcFgMaterialNum());

					psListTo.setMaterialNum(assy);
					psListTo.setMpStatus(mpStatus);
					psListTo.setPlantCode(plantCode);
					psListTo.setReleasedBy(userTo.getUserId());
					psListTo.setReleaseTo(projectCode.getReleaseTo());
					List<String> assyVendorList = strToList(assyVendor);
					for (String assyTo : assyVendorList) {
						psListTo
								.setId(SequenceSupport
										.nextValue(SequenceSupport.SEQ_PIDB_SOURCE_LIST));
						psListTo.setVendor(assyTo);
						pidbSourceListDao.insert(psListTo, "PIDB_SOURCE_LIST");
					}

				}
				
				IcTapeDao tapeDao = new IcTapeDao();
				if (!tape.equals("") && !tapeVendor.equals("")) {
					String mpStatus = ERPHelper.judgeMPByTapeName(mpListTo
							.getTapeName());

					IcTapeTo tmpTapeTo = tapeDao.findByMaterialNum(tape);
					String releaseTo="";
					if ( tmpTapeTo == null ){
						releaseTo = "HX";
					}else{
						releaseTo = tmpTapeTo.getReleaseTo();
					}
					psListTo.setMaterialNum(tape);
					psListTo.setMpStatus(mpStatus);
					psListTo.setPlantCode(plantCode);
					psListTo.setReleasedBy(userTo.getUserId());
					psListTo.setReleaseTo(releaseTo);
					List<String> tapeVendorList = strToList(tapeVendor);
					for (String tapeTo : tapeVendorList) {
						psListTo
								.setId(SequenceSupport
										.nextValue(SequenceSupport.SEQ_PIDB_SOURCE_LIST));
						psListTo.setVendor(tapeTo);
						pidbSourceListDao.insert(psListTo, "PIDB_SOURCE_LIST");
					}
				}

				if (!ft.equals("") && !ftVendor.equals("")) {
					String mpStatus = ERPHelper.judgeMP(mpListTo
							.getIcFgMaterialNum());

					psListTo.setMaterialNum(ft);
					psListTo.setMpStatus(mpStatus);
					psListTo.setPlantCode(plantCode);
					psListTo.setReleasedBy(userTo.getUserId());
					psListTo.setReleaseTo(projectCode.getReleaseTo());
					List<String> ftVendorList = strToList(ftVendor);
					for (String ftTo : ftVendorList) {
						psListTo
								.setId(SequenceSupport
										.nextValue(SequenceSupport.SEQ_PIDB_SOURCE_LIST));
						psListTo.setVendor(ftTo);
						pidbSourceListDao.insert(psListTo, "PIDB_SOURCE_LIST");
					}

				}
			}

		}

		return null;
	}

	public static String releaseFormICWafer(Object md, final UserTo userTo) {

		IcWaferTo icWaferTo = (IcWaferTo) md;

		PidbSourceListTo psListTo = new PidbSourceListTo();
		PidbSourceListDao pidbSourceListDao = new PidbSourceListDao();
		SapDefaultSingleValueDao sapDefaultSingleValueDao = new SapDefaultSingleValueDao();

		// 取得release_To
		ProjectCodeTo projectCode = new ProjectCodeDao()
				.findByProjectCode(icWaferTo.getProjCode());
		// 1.0 SAP Default value
		String plantCode = sapDefaultSingleValueDao.findDefaultValue("plant");
		plantCode = null != plantCode ? plantCode : "";

		// 1.1 Set IC Wafer List
		String materialNum = icWaferTo.getMaterialNum();
		materialNum = null != materialNum ? materialNum : "";

		FabCodeDao fabCodeDao = new FabCodeDao();

		String wfVendor = fabCodeDao.findICWFVendorCodeByPartNum(materialNum);
		wfVendor = null != wfVendor ? wfVendor : "";

		if (!materialNum.equals("") && !wfVendor.equals("")) {
			String mpStatus = ERPHelper.judgeMPByProjWVersion(icWaferTo
					.getProjCodeWVersion());

			psListTo.setMaterialNum(materialNum);
			psListTo.setMpStatus(mpStatus);
			psListTo.setPlantCode(plantCode);
			psListTo.setReleasedBy(userTo.getUserId());
			psListTo.setVendor(wfVendor);
			psListTo.setReleaseTo(projectCode.getReleaseTo());
			psListTo.setId(SequenceSupport
					.nextValue(SequenceSupport.SEQ_PIDB_SOURCE_LIST));
			pidbSourceListDao.insert(psListTo, "PIDB_SOURCE_LIST");
		}

		return null;
	}

	public static String releaseFormICTape(Object md, final UserTo userTo) {

		IcTapeTo icTapeTo = (IcTapeTo) md;

		PidbSourceListTo psListTo = new PidbSourceListTo();
		PidbSourceListDao pidbSourceListDao = new PidbSourceListDao();
		SapDefaultSingleValueDao sapDefaultSingleValueDao = new SapDefaultSingleValueDao();

		// 取得release_To
		/*
		 * ProjectTo projectTo = new ProjectDao().findByProjectCode(mpListTo
		 * .getProjCode());
		 */
		// 1.0 SAP Default value
		String plantCode = sapDefaultSingleValueDao.findDefaultValue("plant");
		plantCode = null != plantCode ? plantCode : "";

		// 1.1 Set IC Tape List
		String tape = icTapeTo.getMaterialNum();
		tape = null != tape ? tape : "";

		// 1.2 Tape Vendor
		String tapeVendor = icTapeTo.getTapeVendor();
		tapeVendor = null != tapeVendor ? tapeVendor : "";

		if (!tapeVendor.equals("")) {
			tapeVendor = "," + tapeVendor + ",";
		}

		if (!tape.equals("") && !tapeVendor.equals("")) {

			String mpStatus = ERPHelper.judgeMPByTapeName(icTapeTo
					.getTapeName());

			psListTo.setMaterialNum(tape);
			psListTo.setMpStatus(mpStatus);
			psListTo.setPlantCode(plantCode);
			psListTo.setReleasedBy(userTo.getUserId());
			psListTo.setReleaseTo(icTapeTo.getReleaseTo());
			List<String> tapeVendorList = strToList(tapeVendor);

			for (String tapeTo : tapeVendorList) {
				psListTo.setId(SequenceSupport
						.nextValue(SequenceSupport.SEQ_PIDB_SOURCE_LIST));
				psListTo.setVendor(tapeTo);
				pidbSourceListDao.insert(psListTo, "PIDB_SOURCE_LIST");
			}
		}

		return null;
	}

	public static List strToList(String vendor) {
		List vendorList = new ArrayList();
		String[] codeList = vendor.split(",");

		for (int i = 0; i < codeList.length; i++) {
			if (i != 0) {
				vendorList.add(codeList[i]);
			}

		}
		return vendorList;

	}

}
