package com.cista.pidb.md.erp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cista.pidb.admin.to.UserTo;
import com.cista.pidb.code.dao.SapDefaultSingleValueDao;
import com.cista.pidb.core.SequenceSupport;
import com.cista.pidb.md.dao.MpListDao;
import com.cista.pidb.md.dao.MpListEolCustDao;
import com.cista.pidb.md.dao.PidbAplDao;
import com.cista.pidb.md.dao.ProjectCodeDao;
import com.cista.pidb.md.to.MpListEolCustTo;
import com.cista.pidb.md.to.MpListTo;
import com.cista.pidb.md.to.PidbAPLTo;
import com.cista.pidb.md.to.ProjectCodeTo;

public class MpListAplERP {

	/** Logger. */
	private static Log logger = LogFactory.getLog(MpListAslERP.class);

	public static String releaseEOL(Object md, final UserTo userTo) {

		MpListTo mpListTo = (MpListTo) md;
		MpListEolCustDao mpListEolDao = new MpListEolCustDao();
		// MpListDao mpListDao = new MpListDao();
		PidbAPLTo pidbAPLTo = new PidbAPLTo();
		PidbAplDao pidbAplDao = new PidbAplDao();
		// SapDefaultSingleValueDao sapDefaultSingleValueDao = new
		// SapDefaultSingleValueDao();

		// 1.0
		String partNum = mpListTo.getPartNum();
		partNum = null != partNum ? partNum : "";

		String ft = mpListTo.getIcFgMaterialNum();
		ft = null != ft ? ft : "";

		String projCodeWVersion = mpListTo.getProjCodeWVersion();
		projCodeWVersion = null != projCodeWVersion ? projCodeWVersion : "";

		String tapeName = mpListTo.getTapeName();
		tapeName = null != tapeName ? tapeName : "";

		String pkgCode = mpListTo.getPkgCode();
		pkgCode = null != pkgCode ? pkgCode : "";

		List<MpListEolCustTo> eolCustList = mpListEolDao.findByPrimaryKey(
				partNum, ft, projCodeWVersion, tapeName, pkgCode);

		// String cust = mpListTo.getApproveCust();

		// cust = null != cust ? cust : "";
		// Date validTo = new Date(8099, 11, 31);

		if (eolCustList != null && eolCustList.size() >= 1) {
			for (MpListEolCustTo custTo : eolCustList) {
				String mpStatus = ERPHelper.judgeMPByProjWVersion(mpListTo
						.getProjCodeWVersion());

				Calendar calender = Calendar.getInstance();
				Date validDate = custTo.getEolDate();
				calender.setTime(validDate);
				String sysFlag = custTo.getFlag();
				sysFlag = null != sysFlag ? sysFlag : "";
				if( !sysFlag.equals("SYSTEM")){// User壓的日期
					int year = calender.get(calender.YEAR) + 1;
					int month = calender.get(calender.MONTH);
					int day = calender.get(calender.DAY_OF_MONTH);

					calender.set(year, month, day);
				}
				Date newDate = calender.getTime();		
				
				//取得projectCode中的Release_To值
				ProjectCodeTo projectCode = new ProjectCodeDao()
						.findByProjectCode(mpListTo.getProjCode());
				// 將Release_To 塞到 APL Table中
				pidbAPLTo.setReleaseTo(projectCode.getReleaseTo());

				pidbAPLTo.setId(SequenceSupport
						.nextValue(SequenceSupport.SEQ_PIDB_APL));
				pidbAPLTo.setMaterialNum(custTo.getIcFgMaterialNum());			
				
				if(mpListTo.getMpReleaseDate() == null){
					return "ERP-09-103";
				}
				pidbAPLTo.setValidFrom(mpListTo.getMpReleaseDate());
				pidbAPLTo.setValidTo(newDate);
				pidbAPLTo.setReleasedBy(userTo.getUserId());
				pidbAPLTo.setCustomer(custTo.getEolCust());
				pidbAplDao.insert(pidbAPLTo, "PIDB_APL");
			}

		}

		return null;
	}

	public static String release(Object md, final UserTo userTo) {

		MpListTo mpListTo = (MpListTo) md;
		MpListDao mpListDao = new MpListDao();
		PidbAPLTo pidbAPLTo = new PidbAPLTo();
		PidbAplDao pidbAplDao = new PidbAplDao();
		SapDefaultSingleValueDao sapDefaultSingleValueDao = new SapDefaultSingleValueDao();

		// 1.0
		String ft = mpListTo.getIcFgMaterialNum();
		ft = null != ft ? ft : "";

		String cust = mpListTo.getApproveCust();
		cust = null != cust ? cust : "";
		Date validTo = new Date(8099, 11, 31);
		
		if (!ft.equals("") && !cust.equals("")) {
			String mpStatus = ERPHelper.judgeMPByProjWVersion(mpListTo
					.getProjCodeWVersion());
			
			if(mpListTo.getMpReleaseDate() == null){
				return "ERP-09-102";
			}
			pidbAPLTo.setMaterialNum(ft);
			pidbAPLTo.setValidFrom(mpListTo.getMpReleaseDate());
			pidbAPLTo.setValidTo(validTo);
			pidbAPLTo.setReleasedBy(userTo.getUserId());
			
			//取得projectCode中的Release_To值
			ProjectCodeTo projectCode = new ProjectCodeDao()
					.findByProjectCode(mpListTo.getProjCode());
			// 將Release_To 塞到 APL Table中
			pidbAPLTo.setReleaseTo(projectCode.getReleaseTo());

			List<String> custList = strToList(cust);
			for (String custTo : custList) {
				pidbAPLTo.setId(SequenceSupport
						.nextValue(SequenceSupport.SEQ_PIDB_APL));
				pidbAPLTo.setCustomer(custTo);
				pidbAplDao.insert(pidbAPLTo, "PIDB_APL");
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
