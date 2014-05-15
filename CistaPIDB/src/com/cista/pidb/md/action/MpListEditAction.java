package com.cista.pidb.md.action;
/*
 *  2010/03/16 FCG1  by jere : 新增回傳值  
 *  2010/04/13 FCG2  by jere : mp process flow change時, 發mail
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.cista.pidb.admin.dao.FunctionDao;
import com.cista.pidb.admin.dao.UserDao;
import com.cista.pidb.admin.to.UserTo;
import com.cista.pidb.ajax.AjaxHelper;
import com.cista.pidb.code.dao.FunctionParameterDao;
import com.cista.pidb.code.dao.SapMasterCustomerDao;
import com.cista.pidb.code.dao.SapMasterVendorDao;
import com.cista.pidb.core.HttpHelper;
import com.cista.pidb.core.PIDBContext;
import com.cista.pidb.md.dao.BumpMaskDao;
import com.cista.pidb.md.dao.IcFgDao;
import com.cista.pidb.md.dao.IcTapeDao;
import com.cista.pidb.md.dao.IcWaferDao;
import com.cista.pidb.md.dao.MpListCustomerMapDao;
import com.cista.pidb.md.dao.MpListDao;
import com.cista.pidb.md.dao.MpListEolCustDao;
import com.cista.pidb.md.dao.PidbChangeLogDao;
import com.cista.pidb.md.erp.ERPHelper;
import com.cista.pidb.md.erp.IcFgERP;
import com.cista.pidb.md.erp.IcTapeERP;
import com.cista.pidb.md.erp.IcWaferERP;
import com.cista.pidb.md.erp.MpListAplERP;
import com.cista.pidb.md.erp.MpListAslERP;
import com.cista.pidb.md.erp.ReleaseERPException;
import com.cista.pidb.md.to.BumpMaskTo;
import com.cista.pidb.md.to.IcFgTo;
import com.cista.pidb.md.to.IcTapeTo;
import com.cista.pidb.md.to.IcWaferTo;
import com.cista.pidb.md.to.MpListEolCustTo;
import com.cista.pidb.md.to.MpListTo;

/**
 * .
 * 
 * @author Hu Meixia
 */
public class MpListEditAction extends DispatchAction {
	/**
	 * .
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            ActionForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 */
	public ActionForward pre(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {

		String forward = "viewJsp";
		String icFgMaterialNum = request.getParameter("icFgMaterialNum");
		String projCodeWVersion = request.getParameter("projCodeWVersion");
		String tapeName = request.getParameter("tapeName");
		String pkgCode = request.getParameter("pkgCode");
		String partNum = request.getParameter("partNum");
		MpListTo to = new MpListDao().findByPrimaryKey(partNum,
				icFgMaterialNum, projCodeWVersion, tapeName, pkgCode);
		if (to != null) {
			request.setAttribute("ref", to);
		}

		IcFgDao icFgDao = new IcFgDao();
		IcFgTo icFgTo = icFgDao.findByMaterialNum(icFgMaterialNum);

		if (icFgTo != null) {
			request.setAttribute("icFgPkgCode", icFgTo.getPkgCode());
		}

		// Add EOL Customer List From Hank 2008/11/09
		MpListEolCustDao eolCustDao = new MpListEolCustDao();
		MpListCustomerMapDao custMapDao = new MpListCustomerMapDao();
		SapMasterCustomerDao smcDao = new SapMasterCustomerDao();
		SapMasterVendorDao smcVendorDao = new SapMasterVendorDao();
		//FCG1
		FunctionParameterDao funcParmDao = new FunctionParameterDao();
		
		request.setAttribute("custList", smcDao.findAll());
		request.setAttribute("vendorList", smcVendorDao.findAll());
		request.setAttribute("eolCustList", eolCustDao.findByPrimaryKey(
				partNum, icFgMaterialNum, projCodeWVersion, tapeName, pkgCode));
		//For Tape Vendor Map
		request.setAttribute("tapeCustMapList", custMapDao.findByPrimaryKey(
				partNum, icFgMaterialNum, projCodeWVersion, tapeName, pkgCode,"TAPE"));
		//For Bump Vendor Map
		request.setAttribute("bumpCustMapList", custMapDao.findByPrimaryKey(
				partNum, icFgMaterialNum, projCodeWVersion, tapeName, pkgCode,"BUMP"));
		//For CP House Vendor Map
		request.setAttribute("cpCustMapList", custMapDao.findByPrimaryKey(
				partNum, icFgMaterialNum, projCodeWVersion, tapeName, pkgCode,"CP"));
		//For Assembly House Vendor Map
		request.setAttribute("assyCustMapList", custMapDao.findByPrimaryKey(
				partNum, icFgMaterialNum, projCodeWVersion, tapeName, pkgCode,"ASSY"));
		//For FT TEST House Vendor Map
		request.setAttribute("ftTestCustMapList", custMapDao.findByPrimaryKey(
				partNum, icFgMaterialNum, projCodeWVersion, tapeName, pkgCode,"FT"));
		//For Polish Vendor Map
		request.setAttribute("polishCustMapList", custMapDao.findByPrimaryKey(
				partNum, icFgMaterialNum, projCodeWVersion, tapeName, pkgCode,"POLISH"));
		//For Color Filter Vendor Map
		request.setAttribute("cfCustMapList", custMapDao.findByPrimaryKey(
				partNum, icFgMaterialNum, projCodeWVersion, tapeName, pkgCode,"CF"));
		//For TunKey(Wafer +Color Filter) Vendor Map
		request.setAttribute("wcfCustMapList", custMapDao.findByPrimaryKey(
				partNum, icFgMaterialNum, projCodeWVersion, tapeName, pkgCode,"WCF"));
		//For CSP Vendor Map
		request.setAttribute("cspCustMapList", custMapDao.findByPrimaryKey(
				partNum, icFgMaterialNum, projCodeWVersion, tapeName, pkgCode,"CSP"));
		//FCG1
		//For Tray check condtion
		request.setAttribute("trayCustAlarmMapList", funcParmDao.findValueList(
				"MP_LIST", "Tray_Customer_Alarm" ) );
		
		return mapping.findForward(forward); 
	}

	/**
	 * .
	 * 
	 * @param mapping
	 *            ActionMapping
	 * @param form
	 *            ActionForm
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return ActionForward
	 */
	public ActionForward save(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {

		String forward = "success";
		MpListDao mpListDao = new MpListDao();
		MpListTo mpListTo = (MpListTo) HttpHelper.pickupForm(MpListTo.class,
				request);
		UserTo loginUser = PIDBContext.getLoginUser(request);
		mpListTo.setModifiedBy(loginUser.getUserId());

		/** *****塞入修改日期******** */
		// add update_Time
		Calendar rightNow = Calendar.getInstance();
		SimpleDateFormat spf = new SimpleDateFormat("yyyyMMddHHmmss");

		mpListTo.setUpdateTime(spf.format(rightNow.getTime()));

		// add assignTo and assignEmail
		String assigns = "";
		String[] assignTo = request.getParameterValues("assignTo");
		if (assignTo != null) {
			for (String a : assignTo) {
				assigns += "," + a;
			}
		}

		assigns = assigns.length() > 0 ? assigns.substring(1) : assigns;

		UserDao userDao = new UserDao();

		String emails = userDao.fetchEmail(assignTo);
		mpListTo.setAssignTo(assigns);
		mpListTo.setAssignEmail(emails);

		Map<String, Object> key = new HashMap<String, Object>();
		key.put("PART_NUM", request.getParameter("partNum"));
		key.put("PROJ_CODE_W_VERSION", request.getParameter("projCodeWVersion"));
		key.put("TAPE_NAME", request.getParameter("tapeName"));
		key.put("PKG_CODE", request.getParameter("pkgCode"));
		key.put("IC_FG_MATERIAL_NUM", request.getParameter("icFgMaterialNum"));

		String[] appList = request.getParameterValues("approveCust");
		String allApp = "";
		if (appList != null && appList.length > 0) {
			for (String app : appList) {
				allApp += "," + app;
			}
		}

		if (allApp != null && allApp.length() > 0) {
			mpListTo.setApproveCust(allApp + ",");
		}

		appList = request.getParameterValues("approveTapeVendor");

		allApp = "";
		if (appList != null && appList.length > 0) {
			for (String app : appList) {
				allApp += "," + app;
			}
		}

		if (allApp != null && allApp.length() > 0) {
			mpListTo.setApproveTapeVendor(allApp + ",");
		}

		appList = request.getParameterValues("approveBpVendor");
		allApp = "";
		if (appList != null && appList.length > 0) {
			for (String app : appList) {
				allApp += "," + app;
			}
		}

		if (allApp != null && allApp.length() > 0) {
			mpListTo.setApproveBpVendor(allApp + ",");
		}

		appList = request.getParameterValues("approveCpHouse");
		allApp = "";
		if (appList != null && appList.length > 0) {
			for (String app : appList) {
				allApp += "," + app;
			}
		}

		if (allApp != null && allApp.length() > 0) {
			mpListTo.setApproveCpHouse(allApp + ",");
		}

		appList = request.getParameterValues("approveAssyHouse");
		allApp = "";
		if (appList != null && appList.length > 0) {
			for (String app : appList) {
				allApp += "," + app;
			}
		}

		if (allApp != null && allApp.length() > 0) {
			mpListTo.setApproveAssyHouse(allApp + ",");
		}

		appList = request.getParameterValues("approveFtHouse");
		allApp = "";
		if (appList != null && appList.length > 0) {
			for (String app : appList) {
				allApp += "," + app;
			}
		}

		if (allApp != null && allApp.length() > 0) {
			mpListTo.setApproveFtHouse(allApp + ",");
		}

		// polish Vendor
		appList = request.getParameterValues("approvePolishVendor");
		allApp = "";
		if (appList != null && appList.length > 0) {
			for (String app : appList) {
				allApp += "," + app;
			}
		}

		if (allApp != null && allApp.length() > 0) {
			mpListTo.setApprovePolishVendor(allApp + ",");
		}

		// Color Filter Vendor
		appList = request.getParameterValues("approveColorFilterVendor");
		allApp = "";
		if (appList != null && appList.length > 0) {
			for (String app : appList) {
				allApp += "," + app;
			}
		}

		if (allApp != null && allApp.length() > 0) {
			mpListTo.setApproveColorFilterVendor(allApp + ",");
		}

		// TurnKey(Wafer + Color Filter) Vendor
		appList = request.getParameterValues("approveWaferCfVendor");
		allApp = "";
		if (appList != null && appList.length > 0) {
			for (String app : appList) {
				allApp += "," + app;
			}
		}

		if (allApp != null && allApp.length() > 0) {
			mpListTo.setApproveWaferCfVendor(allApp + ",");
		}
		
		// csp Vendor
		appList = request.getParameterValues("approveCpCspVendor");
		allApp = "";
		if (appList != null && appList.length > 0) {
			for (String app : appList) {
				allApp += "," + app;
			}
		}

		if (allApp != null && allApp.length() > 0) {
			mpListTo.setApproveCpCspVendor(allApp + ",");
		}

		// TSV Vendor
		appList = request.getParameterValues("approveCpTsvVendor");
		allApp = "";
		if (appList != null && appList.length > 0) {
			for (String app : appList) {
				allApp += "," + app;
			}
		}

		if (allApp != null && allApp.length() > 0) {
			mpListTo.setApproveCpTsvVendor(allApp + ",");
		}
		
		appList = request.getParameterValues("eolCust");
		allApp = "";
		if (appList != null && appList.length > 0) {
			for (String app : appList) {
				allApp += "," + app;
			}
		}

		if (allApp != null && allApp.length() > 0) {
			mpListTo.setEolCust(allApp + ",");
		}

		// send mail
		SendMailDispatch.sendMailByCreate("MD-13", mpListTo.getPartNum() + "("
				+ mpListTo.getIcFgMaterialNum() + ")", emails, SendMailDispatch
				.getUrl("MD_13_EDIT", request.getContextPath(), mpListTo));

		String subject = PIDBContext.getConfig("MD-13") + "  "
				+ mpListTo.getPartNum() + "(" + mpListTo.getIcFgMaterialNum()
				+ ")" + " Vendor Changed";
		String text = "";
		String defaultEmail = userDao.fetchEmail(new String[] { "(R)default_MP_announce" });

		String icFg = mpListTo.getIcFgMaterialNum();
		String projW = mpListTo.getProjCodeWVersion();
		String tapeName = mpListTo.getTapeName();
		String pkgCode = mpListTo.getPkgCode();

		String appCust = mpListTo.getApproveCust();
		String appTape = mpListTo.getApproveTapeVendor();
		String appBump = mpListTo.getApproveBpVendor();
		String appCP = mpListTo.getApproveCpHouse();
		String appAssy = mpListTo.getApproveAssyHouse();
		String appFt = mpListTo.getApproveFtHouse();
		String appPolish = mpListTo.getApprovePolishVendor();
		String appColorFilter = mpListTo.getApproveColorFilterVendor();
		String appWaferCF = mpListTo.getApproveWaferCfVendor();
		String appCsp = mpListTo.getApproveCpCspVendor();
		String appTsv = mpListTo.getApproveCpTsvVendor();
		
		String vendorChange = "";
		SapMasterVendorDao vendorDao = new SapMasterVendorDao();
		String shortName = "";
		if (!mpListDao.findVendor(icFg, projW, tapeName, pkgCode,
				"APPROVE_CUST", appCust)) {
			shortName = "";
			shortName = appCust;
			shortName = null != shortName ? shortName : "空白";

			vendorChange = vendorChange + "Approve Customer Change to "
					+ shortName + " \r\n";

		}
		if (!mpListDao.findVendor(icFg, projW, tapeName, pkgCode,
				"APPROVE_TAPE_VENDOR", appTape)) {
			shortName = "";
			shortName = vendorDao.codeToShortName(appTape);
			shortName = null != shortName ? shortName : "空白";
			vendorChange = vendorChange + "Approve Tape Vendor Change to "
					+ shortName + " \r\n";

		}
		if (!mpListDao.findVendor(icFg, projW, tapeName, pkgCode,
				"APPROVE_BP_VENDOR", appBump)) {
			shortName = "";
			shortName = vendorDao.codeToShortName(appBump);
			shortName = null != shortName ? shortName : "空白";
			vendorChange = vendorChange + "Approve Bumping Vendor Change to "
					+ shortName + " \r\n";

		}
		if (!mpListDao.findVendor(icFg, projW, tapeName, pkgCode,
				"APPROVE_CP_HOUSE", appCP)) {
			shortName = "";
			shortName = vendorDao.codeToShortName(appCP);
			shortName = null != shortName ? shortName : "空白";
			vendorChange = vendorChange + "Approve CP House Change to "
					+ shortName + " \r\n";

		}
		if (!mpListDao.findVendor(icFg, projW, tapeName, pkgCode,
				"APPROVE_ASSY_HOUSE", appAssy)) {
			shortName = "";
			shortName = vendorDao.codeToShortName(appAssy);
			shortName = null != shortName ? shortName : "空白";
			vendorChange = vendorChange + "Approve Assembly House Change to "
					+ shortName + " \r\n";

		}

		if (!mpListDao.findVendor(icFg, projW, tapeName, pkgCode,
				"APPROVE_FT_HOUSE", appFt)) {
			shortName = "";
			shortName = vendorDao.codeToShortName(appFt);
			shortName = null != shortName ? shortName : "空白";
			vendorChange = vendorChange + "Approve FT TEST House Change to "
					+ shortName + " \r\n";

		}

		if (!mpListDao.findVendor(icFg, projW, tapeName, pkgCode,
				"APPROVE_POLISH_VENDOR", appPolish)) {
			shortName = "";
			shortName = vendorDao.codeToShortName(appPolish);
			shortName = null != shortName ? shortName : "空白";
			vendorChange = vendorChange + "Approve Polish Vendor Change to "
					+ shortName + " \r\n";

		}

		if (!mpListDao.findVendor(icFg, projW, tapeName, pkgCode,
				"APPROVE_COLOR_FILTER_VENDOR", appColorFilter)) {
			shortName = "";
			shortName = vendorDao.codeToShortName(appColorFilter);
			shortName = null != shortName ? shortName : "空白";
			vendorChange = vendorChange
					+ "Approve Color Filter Vendor Change to " + shortName
					+ " \r\n";

		}

		if (!mpListDao.findVendor(icFg, projW, tapeName, pkgCode,
				"APPROVE_WAFER_CF_VENDOR", appWaferCF)) {
			shortName = "";
			shortName = vendorDao.codeToShortName(appWaferCF);
			shortName = null != shortName ? shortName : "空白";
			vendorChange = vendorChange
					+ "Approve TurnKey(Wafer + Color Filter) Vendor Change to "
					+ shortName + " \r\n";

		}
		
		if (!mpListDao.findVendor(icFg, projW, tapeName, pkgCode,
				"APPROVE_CP_CSP_VENDOR", appCsp)) {
			shortName = "";
			shortName = vendorDao.codeToShortName(appCsp);
			shortName = null != shortName ? shortName : "空白";
			vendorChange = vendorChange
					+ "Approve CP CSP Vendor Change to "
					+ shortName + " \r\n";

		}

		if (!mpListDao.findVendor(icFg, projW, tapeName, pkgCode,
				"APPROVE_CP_TSV_VENDOR", appTsv)) {
			shortName = "";
			shortName = vendorDao.codeToShortName(appTsv);
			shortName = null != shortName ? shortName : "空白";
			vendorChange = vendorChange
					+ "Approve CP TSV Vendor Change to "
					+ shortName + " \r\n";

		}
		
		if (!vendorChange.equals("")) {
			text = vendorChange;
			SendMailDispatch.sendMailDefault(subject, text, defaultEmail);
		} else {

		}
		//FCG2 取得舊資料
		String sPartNum = mpListTo.getPartNum();		
		MpListTo preMpListTo = mpListDao.findByPrimaryKey(sPartNum, icFg, projW, tapeName, pkgCode);
		String sPreProcessFlow = preMpListTo.getProcessFlow();
		sPreProcessFlow = sPreProcessFlow!=null?sPreProcessFlow:"";
		String sNewProcessFlow = mpListTo.getProcessFlow();		
		sNewProcessFlow = sNewProcessFlow!=null?sNewProcessFlow:"";
		//判斷是否有改變, 有則發mail
		if(!sPreProcessFlow.equals(sNewProcessFlow))
		{			
			StringBuffer sbSubject = new StringBuffer();
			sbSubject.append(PIDBContext.getConfig("MD-13"));
			sbSubject.append(" ");
			sbSubject.append(mpListTo.getPartNum() );
			sbSubject.append("(");
			sbSubject.append(mpListTo.getIcFgMaterialNum() );
			sbSubject.append(") Process Changed");
			
			StringBuffer sbContentText = new StringBuffer();
			sbContentText.append("Process Flow Change to ");
			sbContentText.append(sNewProcessFlow);
			sbContentText.append(" \r\n");
			//test mail group
			//defaultEmail = userDao.fetchEmail(new String[] { "(R)default_MP_test_announce" });
			
			SendMailDispatch.sendMailDefault(sbSubject.toString(), sbContentText.toString(), defaultEmail);
		}
		
		mpListDao.update(mpListTo);
		//Change Log
		PidbChangeLogDao chlDao = new PidbChangeLogDao();
		chlDao.insertChange("MP_LIST", "UPDATE", AjaxHelper.bean2String(mpListTo), loginUser.getUserId());
		
		mpListDao.updateRemark(mpListTo);

		// *************Remove Customer Add to EOL***********//
		String preCust = preMpListTo.getApproveCust();
		preCust = null != preCust ? preCust : "";
		String newCust = mpListTo.getApproveCust();
		newCust = null != newCust ? newCust : "";
		if( !preCust.equals("")){
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat SDF = new SimpleDateFormat("yyyy/MM/dd");
			cal.add(Calendar.DATE, -1);
			Date eolDate = cal.getTime();
			try{
				eolDate = SDF.parse(SDF.format(cal.getTime()) );
		    }catch (ParseException e) {
	            log.error(e);
	            e.printStackTrace();
	        }catch (Exception e) {
	            log.error(e);
	            e.printStackTrace();
	        }
	        
			log.debug("1 days ago: " + cal.getTime());
			
			MpListEolCustDao eolCustDao = new MpListEolCustDao();
			MpListEolCustTo eolCustTo = new MpListEolCustTo();
			
			preCust = preCust.substring(1, (preCust.length() - 1) );
			log.debug("preCust " + preCust);
			String[] preCustArray = preCust.split(",");
			for(int i=0; i < preCustArray.length ; i++){
				log.debug("preCust " + preCustArray[i]);
				log.debug("newCust " + newCust);
				if( newCust.indexOf(preCustArray[i]) < 0 ){
					log.debug("remove Cust " + preCustArray[i]);
					//Set EOL Customer
					eolCustTo.setIcFgMaterialNum(mpListTo.getIcFgMaterialNum());
					eolCustTo.setPartNum(mpListTo.getPartNum());
					eolCustTo.setPkgCode(mpListTo.getPkgCode());
					eolCustTo.setProjCodeWVersion(mpListTo.getProjCodeWVersion());
					eolCustTo.setTapeName(mpListTo.getTapeName());
					
					eolCustTo.setEolCust(preCustArray[i]);
					eolCustTo.setEolDate(eolDate);
					eolCustTo.setModifiedBy(loginUser.getUserId());
					eolCustTo.setModifiedBy(mpListTo.getModifiedBy());
					eolCustTo.setFlag("SYSTEM");
					eolCustTo.setRemark("MP List Remove Customer: " + preCustArray[i] + 
							" ,\r\n And System Auto Add to EOL List.");
					if( eolCustDao.findByPrimaryKey(eolCustTo) == null ){
						eolCustDao.insert(eolCustTo, "PIDB_MP_LIST_EOL");
						
						//Change Log
						chlDao.insertChange("MP_LIST_EOL", "INSET", AjaxHelper.bean2String(eolCustTo), loginUser.getUserId());
					}
					
				}
				log.debug("------------------------");
			}
		}
		
		
		// ****************Vendor Update Start****************//
		
		/*
		 * 2.0 2008/01/11 Added Hank Update Vendor List For Vendor Change By
		 * Project Code
		 */
		// 2.1 Bumping Vendor Update
		// 2.1.1 Find Bump Update List
		String projCode = mpListTo.getProjCode();
		projCode = null != projCode ? projCode : "";
		String newBump = mpListTo.getApproveBpVendor();
		newBump = null != newBump ? newBump : "";
		if (!projCode.equals("")) {
			projCode = projCode.substring(0, 6);
		}
		List<MpListTo> upBumpMpListToList = mpListDao.findByProjCode(projCode);
		if (upBumpMpListToList != null && upBumpMpListToList.size() > 0) {
			for (MpListTo upBumpMpListTo : upBumpMpListToList) {
				upBumpMpListTo.setApproveBpVendor(newBump);
				Map<String, Object> keyBump = new HashMap<String, Object>();
				keyBump.put("IC_FG_MATERIAL_NUM", upBumpMpListTo
						.getIcFgMaterialNum());
				keyBump.put("PROJ_CODE_W_VERSION", upBumpMpListTo
						.getProjCodeWVersion());
				keyBump.put("TAPE_NAME", upBumpMpListTo.getTapeName());
				keyBump.put("PKG_CODE", upBumpMpListTo.getPkgCode());
				mpListDao.update(upBumpMpListTo);
				//Change Log
				chlDao.insertChange("MP_LIST", "UPDATE", AjaxHelper.bean2String(upBumpMpListTo), loginUser.getUserId());
				
				MpListAslERP.release(upBumpMpListTo, loginUser);
			}

		}

		// 2.2 CP Vendor Update
		// 2.2.1 Find CP Update List
		String prodCode = mpListTo.getProdCode();
		prodCode = null != prodCode ? prodCode : "";
		String newCP = mpListTo.getApproveCpHouse();
		newCP = null != newCP ? newCP : "";
		List<MpListTo> upCpMpListToList;

		if (!prodCode.equals("") && prodCode.length() >= 8) {
			prodCode = prodCode.substring(0, 8);
			upCpMpListToList = mpListDao.findByProdCodeGreaterThan8(prodCode);
		} else {
			upCpMpListToList = mpListDao.findByProdCode(prodCode);
		}

		if (upCpMpListToList != null && upCpMpListToList.size() > 0) {
			for (MpListTo upCpMpListTo : upCpMpListToList) {
				upCpMpListTo.setApproveCpHouse(mpListTo.getApproveCpHouse());
				Map<String, Object> keyCP = new HashMap<String, Object>();
				keyCP.put("IC_FG_MATERIAL_NUM", upCpMpListTo
						.getIcFgMaterialNum());
				keyCP.put("PROJ_CODE_W_VERSION", upCpMpListTo
						.getProjCodeWVersion());
				keyCP.put("TAPE_NAME", upCpMpListTo.getTapeName());
				keyCP.put("PKG_CODE", upCpMpListTo.getPkgCode());
				mpListDao.update(upCpMpListTo);
				//Change Log
				chlDao.insertChange("MP_LIST", "UPDATE", AjaxHelper.bean2String(upCpMpListTo), loginUser.getUserId());
				
				MpListAslERP.release(upCpMpListTo, loginUser);
			}

		}

		// 2.3 Assy&FT Vendor Update
		// 2.3.1 Find Assy Update List
		// 2.3.1.1 Get PKG_CODE
		IcFgDao icFgDao = new IcFgDao();

		String icFgMaterialNum = mpListTo.getIcFgMaterialNum();
		icFgMaterialNum = null != icFgMaterialNum ? icFgMaterialNum : "";

		String partNum = mpListTo.getPartNum();
		partNum = null != partNum ? partNum : "";

		IcFgTo icFgToMaterial = icFgDao.findByMaterialNum(icFgMaterialNum);
		String pkgType = "";
		List<MpListTo> upAssyMpListToList;
		if (icFgToMaterial != null) {
			pkgType = icFgToMaterial.getPkgType();
			if (pkgType.equals("301") || pkgType.equals("302")) {
				String assyTapeName = mpListTo.getTapeName();
				assyTapeName = null != assyTapeName ? assyTapeName : "";
				upAssyMpListToList = mpListDao.findByTapeName(assyTapeName);
				/*
				 * }else if (pkgType.equals("303") ){ if( !prodCode.equals("") &&
				 * prodCode.length() >=8 ){ prodCode = prodCode.substring(0,8);
				 * upAssyMpListToList =
				 * mpListDao.findByProdCodeGreaterThan8(prodCode); }else{
				 * upAssyMpListToList = mpListDao.findByProdCode(prodCode); }
				 */
			} else if (pkgType.equals("304") || pkgType.equals("303")) {
				upAssyMpListToList = mpListDao.findByPartNum(partNum);
			} else {
				upAssyMpListToList = null;
			}
			if (upAssyMpListToList != null && upAssyMpListToList.size() > 0) {
				for (MpListTo upAssyMpListTo : upAssyMpListToList) {
					upAssyMpListTo.setApproveAssyHouse(mpListTo
							.getApproveAssyHouse());
					upAssyMpListTo.setApproveFtHouse(mpListTo
							.getApproveFtHouse());
					Map<String, Object> keyAssy = new HashMap<String, Object>();
					keyAssy.put("IC_FG_MATERIAL_NUM", upAssyMpListTo
							.getIcFgMaterialNum());
					keyAssy.put("PROJ_CODE_W_VERSION", upAssyMpListTo
							.getProjCodeWVersion());
					keyAssy.put("TAPE_NAME", upAssyMpListTo.getTapeName());
					keyAssy.put("PKG_CODE", upAssyMpListTo.getPkgCode());
					mpListDao.update(upAssyMpListTo);
					//Change Log
					chlDao.insertChange("MP_LIST", "UPDATE", AjaxHelper.bean2String(upAssyMpListTo), loginUser.getUserId());
					
					MpListAslERP.release(upAssyMpListTo, loginUser);
				}

			}
		}

		// 2.4 Tape Vendor Update
		// 2.4.1 Find CP Update List
		String upTapeName = mpListTo.getTapeName();
		upTapeName = null != upTapeName ? upTapeName : "";

		List<MpListTo> upTapeMpListToList;
		if (!upTapeName.trim().equals("NA")) {
			upTapeMpListToList = mpListDao.findByTapeName(upTapeName);
		} else {
			upTapeMpListToList = null;
		}

		if (upTapeMpListToList != null && upTapeMpListToList.size() > 0) {
			for (MpListTo upTapeMpListTo : upTapeMpListToList) {
				upTapeMpListTo.setApproveTapeVendor(mpListTo
						.getApproveTapeVendor());
				Map<String, Object> keyCP = new HashMap<String, Object>();
				keyCP.put("IC_FG_MATERIAL_NUM", upTapeMpListTo
						.getIcFgMaterialNum());
				keyCP.put("PROJ_CODE_W_VERSION", upTapeMpListTo
						.getProjCodeWVersion());
				keyCP.put("TAPE_NAME", upTapeMpListTo.getTapeName());
				keyCP.put("PKG_CODE", upTapeMpListTo.getPkgCode());
				mpListDao.update(upTapeMpListTo);
				//Change Log
				chlDao.insertChange("MP_LIST", "UPDATE", AjaxHelper.bean2String(upTapeMpListTo), loginUser.getUserId());
				
				MpListAslERP.release(upTapeMpListTo, loginUser);
			}

		}

		// ****************Vendor Update END****************//

		// update IC Wafer
		IcWaferDao icWaferDao = new IcWaferDao();
		IcWaferTo tempWafer = null;
		List<IcWaferTo> icWaferToList = icWaferDao
				.findListByProjCodeWVersion(mpListTo.getProjCodeWVersion());
		if (icWaferToList != null && icWaferToList.size() > 0) {
			for (IcWaferTo to : icWaferToList) {
				/*
				 * Map<String, Object> tempMap = new HashMap<String,
				 * Object>(); tempMap.put("MATERIAL_NUM", to.getMaterialNum());
				 */

				to.setMpStatus("MP");
				icWaferDao.updateIcWafer(to);
			}
			tempWafer = icWaferToList.get(0);
		}

		// update IC Fg
		IcFgTo icFgTo = icFgDao
				.findByMaterialNum(mpListTo.getIcFgMaterialNum());
		if (icFgTo != null) {
			Map tempMap = new HashMap();
			tempMap.put("MATERIAL_NUM", icFgTo.getMaterialNum());

			icFgTo.setMpStatus("MP");
			icFgDao.update(icFgTo);

		}

		/*
		 * 3.0 2008/01/14 Added Hank Release to SAP ERP
		 */

		// 3.1 Release to SAP ERP
		// IcWaferDao icWaferDao = new IcWaferDao();
		String matWf = mpListTo.getMatWf();
		matWf = null != matWf ? matWf : "";
		IcWaferTo icWaferTo = icWaferDao.findByPrimaryKey(matWf);
		final IcWaferTo icWafer = icWaferTo;
		final UserTo user = loginUser;

		IcFgTo icFgErpTo = icFgDao.findByMaterialNum(icFgMaterialNum);
		final IcFgTo icFgErp = icFgErpTo;

		IcTapeDao icTapeDao = new IcTapeDao();
		String matTape = mpListTo.getMatTape();
		matTape = null != matTape ? matTape : "";
		String matTapeName = mpListTo.getTapeName();
		matTapeName = null != matTapeName ? matTapeName : "";

		IcTapeTo icTapeTo = icTapeDao.findByPrimaryKey(matTape, matTapeName);
		final IcTapeTo tapeErp = icTapeTo;
		// Add By Hank 2008/01/21 For ASL Release to SAP
		final MpListTo mpListErp = mpListTo;

		String toErp = request.getParameter("toErp");
		final String className = this.getClass().getName();

		if ("1".equals(toErp.trim())) {

			// *****3.1.1 Release IC Wafer*****//
			// IC Wafer Start Release to SAP
			TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
				public void doInTransactionWithoutResult(
						final TransactionStatus status) {
					String result = null;
					if (icWafer != null) {
						result = IcWaferERP.release(icWafer, user);
						if (result != null) {
							throw new ReleaseERPException(result);
						}

						if (icWafer.getMpStatus() != null
								&& icWafer.getMpStatus().equalsIgnoreCase(
										"Non-MP")) {
							// insert DS
							// result = IcWaferERP.releaseDsNoMp(icWafer, user);
							result = IcWaferERP.releaseDsfICWaferWithCharacter(
									icWafer, user);

							if (result != null) {
								throw new ReleaseERPException(result);
							}

							// insert CP
							if (icWafer.getRoutingCp()) {
								// result = IcWaferERP.releaseCpNoMp(icWafer,
								// user);
								result = IcWaferERP
										.releaseCpfICWaferWithCharacter(
												icWafer, user, className);

								if (result != null) {
									throw new ReleaseERPException(result);
								}
							}

							// insert BP
							if (icWafer.getRoutingBp()) {
								// result = IcWaferERP.releaseBpNoMp(icWafer,
								// user);
								result = IcWaferERP
										.releaseBpfICWaferWithCharacter(
												icWafer, user);
								if (result != null) {
									throw new ReleaseERPException(result);
								}
							}



						} else {
							// insert DS
							result = IcWaferERP.releaseDsfICWaferWithCharacter(
									icWafer, user);
							if (result != null) {
								throw new ReleaseERPException(result);
							}
							// insert CP
							if (icWafer.getRoutingCp()) {
								// result = IcWaferERP.releaseCpNoMp(icWafer,
								// user);
								result = IcWaferERP
										.releaseCpfICWaferWithCharacter(
												icWafer, user, className);

								if (result != null) {
									throw new ReleaseERPException(result);
								}
							}

							// insert BP
							if (icWafer.getRoutingBp()) {
								BumpMaskDao bumpDao = new BumpMaskDao();
								List<BumpMaskTo> bumpToList = bumpDao
										.getByProjCode(icWafer.getProjCode());

								if (bumpToList != null && bumpToList.size() > 0) {
									result = IcWaferERP.releaseBumping(
											bumpToList.get(0), user, icWafer);
									if (result != null) {
										throw new ReleaseERPException(result);
									}
								}
							}
						}
					}

					// IC FG Start Release to SAP
					if (icFgErp != null) {
						System.out.println("IC FG Start Release to SAP");
						result = IcFgERP.releaseForMPList(icFgErp, user,
								className);

						if (result != null) {
							throw new ReleaseERPException(result);
						}
					}
					// Tape Start Release to SAP
					if (tapeErp != null) {
						result = IcTapeERP.release(tapeErp, user);
						if (result != null) {
							throw new ReleaseERPException(result);
						}
					}

					
					// insert cp Polish Material
					result = IcWaferERP.releasePolishICWafer(icWafer,
							user, className);
					if (result != null) {
						throw new ReleaseERPException(result);
					}
					// insert Color Filter Material
					// if (icWafer.getRoutingColorFilter()){
					result = IcWaferERP.releaseColorFilterICWafer(
							icWafer, user, className);
					if (result != null) {
						throw new ReleaseERPException(result);
					}
					// }
					
					// insert cp CSP Material
					result = IcWaferERP.releaseCpCspICWafer(icWafer,
							user, className);
					if (result != null) {
						throw new ReleaseERPException(result);
					}
					
					// insert TurnKey(Wafer + Color Filter)
					// if (icWafer.getRoutingWaferCf()){
					result = IcWaferERP.releaseWaferCFICWafer(icWafer,
							user, className);
					if (result != null) {
						throw new ReleaseERPException(result);
					}
					// }
					
					// Release TSV to SAP
					if (icWafer.getRoutingTsv()) {
						// result = IcWaferERP.releaseCpNoMp(icWafer, user);
						result = IcWaferERP.releaseCpTsvICWafer(
								icWafer, user, className);
						if (result != null) {
							throw new ReleaseERPException(result);
						}
					}
					
					/*
					 * 3.2 2008/01/21 Added Hank Release ASL List to SAP ERP
					 */

					// ****** 3.2 Start Release ASL List to SAP ERP******//
					if (mpListErp != null) {
						result = MpListAslERP.release(mpListErp, user);
						if (result != null) {
							throw new ReleaseERPException(result);
						}
					}

					// ****** 3.2 End Release ASL List to SAP ERP ******//

					/*
					 * 3.3 2008/04/17 Added Hank Release APL List to SAP ERP
					 */

					// ****** 3.3 Start Release APL List to SAP ERP******//
					if (mpListErp != null) {
						result = MpListAplERP.release(mpListErp, user);
						if (result != null) {
							throw new ReleaseERPException(result);
						}
					}

					if (mpListErp != null) {
						result = MpListAplERP.releaseEOL(mpListErp, user);
						if (result != null) {
							throw new ReleaseERPException(result);
						}
					}
					// ****** 3.3 End Release APL List to SAP ERP ******//

				}
			};

			try {
				new FunctionDao().doInTransaction(callback);

			} catch (ReleaseERPException e) {
				request.setAttribute("error", ERPHelper.getErrorMessage(e
						.getMessage()));
				String targetForward = mapping.findForward("viewEditJsp")
						.getPath();

				if (targetForward.indexOf('?') == -1) {
					targetForward = targetForward + "?";
				} else {
					targetForward = targetForward + "&";
				}

				ActionForward actionForward = new ActionForward(targetForward
						+ " partNum=" + mpListTo.getPartNum()
						+ "&icFgMaterialNum=" + mpListTo.getIcFgMaterialNum()
						+ "&projCodeWVersion=" + mpListTo.getProjCodeWVersion()
						+ "&tapeName=" + mpListTo.getTapeName() + "&pkgCode="
						+ mpListTo.getPkgCode());

				return actionForward;
			}

			// 3.1.3 End Release to SAP

		}
		// ****** End Release To SAP ******//

		// redirect to IcFg modify
		// Remove for Hank 2008/02/12
		/*
		 * IcFgTo icFgTempTo = icFgDao
		 * .findByMaterialNum(mpListTo.getIcFgMaterialNum()); ProjectDao
		 * projectDao = new ProjectDao(); ProjectTo projectTo = projectDao
		 * .findByProjectCode(icFgTo.getProjCode());
		 * 
		 * String path = "/md/ic_fg_edit.do?m=pre&materialNum=" +
		 * mpListTo.getIcFgMaterialNum() + "&fab=" + projectTo.getFab() +
		 * "&reminder=1"; if (tempWafer != null && mpListTo != null &&
		 * mpListTo.getMpStatus().equals("1")) { path +=
		 * "&jumpUrl="+Escape.escape(SendMailDispatch.getUrl("MD_4_EDIT",
		 * request.getContextPath(), tempWafer)); } return new
		 * ActionForward(path, true);
		 */

		// Redirect to MP_List Edit Page
		String mes = "";
		if ("1".equals(toErp.trim())) {
			mes = "Save and Release to ERP Successfully";
		} else {
			mes = "Save Successfully";
		}

		request.setAttribute("error", mes);

		// Add for Hank 2008/02/02
		String targetForward = mapping.findForward("viewEditJsp").getPath();

		if (targetForward.indexOf('?') == -1) {
			targetForward = targetForward + "?";
		} else {
			targetForward = targetForward + "&";
		}

		ActionForward actionForward = new ActionForward(targetForward
				+ " partNum=" + mpListTo.getPartNum() + "&icFgMaterialNum="
				+ mpListTo.getIcFgMaterialNum() + "&projCodeWVersion="
				+ mpListTo.getProjCodeWVersion() + "&tapeName="
				+ mpListTo.getTapeName() + "&pkgCode=" + mpListTo.getPkgCode());

		return actionForward;

	}
}
