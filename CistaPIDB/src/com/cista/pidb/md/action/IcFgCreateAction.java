package com.cista.pidb.md.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.cista.pidb.admin.dao.UserDao;
import com.cista.pidb.admin.to.UserTo;
import com.cista.pidb.ajax.AjaxHelper;
import com.cista.pidb.code.dao.SapAppCategoryDao;
import com.cista.pidb.code.dao.SapMasterCustomerDao;
import com.cista.pidb.code.dao.SapMasterPackageTypeDao;
import com.cista.pidb.code.to.SapMasterCustomerTo;
import com.cista.pidb.core.HttpHelper;
import com.cista.pidb.core.PIDBContext;
import com.cista.pidb.md.dao.IcFgDao;
import com.cista.pidb.md.dao.MpListDao;
import com.cista.pidb.md.dao.PidbChangeLogDao;
import com.cista.pidb.md.dao.ProductDao;
import com.cista.pidb.md.dao.ProjectDao;
import com.cista.pidb.md.erp.ERPHelper;
import com.cista.pidb.md.erp.IcFgERP;
import com.cista.pidb.md.erp.ReleaseERPException;
import com.cista.pidb.md.to.IcFgTo;
import com.cista.pidb.md.to.ProductTo;
import com.cista.pidb.md.to.ProjectTo;

public class IcFgCreateAction extends DispatchAction {

	public ActionForward pre(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "create";
		// Prepare for dropdown list
		request.setAttribute("appCateGoryList", new SapAppCategoryDao()
				.findAll());
		request.setAttribute("pkgTypeList", new SapMasterPackageTypeDao()
				.findAll());

		if (request.getParameter("ref") != null) {
			String[] ref = request.getParameter("ref").split(",", 2);
			// Create with reference
			String materialNum = ref[0];
			request.setAttribute("ref", new IcFgDao()
					.findByMaterialNum(materialNum));
			request.setAttribute("fab", ref[1]);
		}

		return mapping.findForward(forward);
	}

	public ActionForward save(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "success";
		IcFgDao icFgDao = new IcFgDao();
		IcFgTo icFgTo = (IcFgTo) HttpHelper.pickupForm(IcFgTo.class, request);
		UserTo loginUser = PIDBContext.getLoginUser(request);
		icFgTo.setCreatedBy(loginUser.getUserId());

		String prodCode = icFgTo.getProdCode();
		String projCode = icFgTo.getProjCode();
		String pkgCode = icFgTo.getPkgCode();

		// Create a new variant
		String maxVar = (String) icFgDao
				.findMaxVar(prodCode, projCode, pkgCode);
		if (maxVar != null) {
			String preVar = maxVar.toString();
			if (preVar != null && !preVar.equals("")) {
				char c = (char) ((int) preVar.charAt(0) + 1);
				String realVar = "";
				if (c > 57 && c < 65) {
					realVar = new String(new char[] { 65 });
				} else if (c > 90) {
					realVar = new String(new char[] { 48 });
				} else {
					realVar = new String(new char[] { c });
				}
				icFgTo.setVariant(realVar);
			}
		} else {
			icFgTo.setVariant("0");
		}

		String variant = icFgTo.getVariant();

		ProductDao productDao = new ProductDao();
		ProductTo productTo = productDao.findByProdCode(prodCode);
		String prodName = productTo.getProdName();
		// prodName = prodName.length() > 5 ? prodName
		// .substring(prodName.length() - 5) : prodName;
		if (prodName.length() >= 8) {
			prodName = prodName.substring(2, 6) + prodName.charAt(7);
		} else if (prodName.length() >= 6) {
			prodName = prodName.substring(2, 6) + "0";
		}

		ProjectDao projectDao = new ProjectDao();
		ProjectTo projectTo = projectDao.findByProjectCode(projCode);

		if (icFgTo.getPkgType() != null && icFgTo.getPkgType().equals("303")) {
			pkgCode = "G" + pkgCode.substring(2);
		}
		int pkgLen = pkgCode.length();
		for (int i = 0; i < 5 - pkgLen; i++) {
			pkgCode = pkgCode + "0";
		}

		String materialNum = "F" + prodName + projectTo.getProjOption()
				+ projectTo.getFab() + pkgCode + variant;

		icFgTo.setMaterialNum(materialNum);

		boolean isDraft = false;
		if (icFgTo.getAppCategory() == null
				|| icFgTo.getAppCategory().equals("")) {
			isDraft = true;
		}
		if (icFgTo.getPkgType() == null || icFgTo.getPkgType().equals("")) {
			isDraft = true;
		}
		if (icFgTo.getMcpPkg() == null || icFgTo.getMcpPkg().equals("")) {
			isDraft = true;
		}

		if (isDraft) {
			icFgTo.setStatus("Draft");
		} else {
			icFgTo.setStatus("Completed");
		}

		// add Cust
		SapMasterCustomerDao sapMasterCustomerDao = new SapMasterCustomerDao();
		String custName = request.getParameter("cust");
		custName = null != custName ? custName : "";
		SapMasterCustomerTo custCodeTo = sapMasterCustomerDao.findByShortName(custName);
		if( custCodeTo != null ){
			String custCode = custCodeTo.getCustomerCode();
			custCode = null != custCode ? custCode : "";
			icFgTo.setCust(custCode);
		}
		// add multiple select data
		String[] cpList = request.getParameterValues("cpTestProgNameList");
		String allCp = "";
		if (cpList != null && cpList.length > 0) {
			for (String cp : cpList) {
				allCp += "," + cp;
			}
		}

		if (allCp != null && allCp.length() > 0) {
			icFgTo.setCpTestProgNameList(allCp.substring(1));
		}

		String[] ftList = request.getParameterValues("ftTestProgNameList");
		String allFt = "";
		if (ftList != null && ftList.length > 0) {
			for (String ft : ftList) {
				allFt += "," + ft;
			}
		}

		if (allFt != null && allFt.length() > 0) {
			icFgTo.setFtTestProgList(allFt.substring(1));
		}

		// add mpStatus
		MpListDao mpListDao = new MpListDao();
		List mpList = mpListDao.findByIcFgMaterialNum(materialNum);
		if (mpList != null && mpList.size() > 0) {
			icFgTo.setMpStatus("MP");
		} else {
			icFgTo.setMpStatus("Non-MP");
		}

		// insert PIDB_CP_MATERIAL
		String cpMaterialNum = icFgTo.getCpMaterialNum();
		if (StringUtils.isNotEmpty(cpMaterialNum)) {
			/*
			 * CpTestProgramDao cpTestProgramDao = new CpTestProgramDao();
			 * CpMaterialDao cpMaterialDao = new CpMaterialDao();
			 * 
			 * String projCodeWVersion =
			 * request.getParameter("projCodeWVersion"); String
			 * cpTestProgramNameList = icFgTo.getCpTestProgNameList(); if
			 * (projCodeWVersion == null || projCodeWVersion.equals("")) { if
			 * (cpTestProgramNameList != null && cpTestProgramNameList.length() >
			 * 0) { String[] cpTestProgramName =
			 * cpTestProgramNameList.split(","); for (String s :
			 * cpTestProgramName) { CpTestProgramTo cpTo =
			 * cpTestProgramDao.findByCpTestProgName(s); if (cpTo !=null) {
			 * projCodeWVersion = cpTo.getProjCodeWVersion(); break; } } } }
			 */

			// Remove By Hank 2008/01/07
			/*
			 * IcWaferDao icWaferDao = new IcWaferDao(); IcWaferTo icWaferTo =
			 * icWaferDao .findByProjCodeWVersion(projCodeWVersion);
			 * 
			 * if (icWaferTo != null && icWaferTo.isRoutingCp()) { CpMaterialTo
			 * cpMaterialTo = cpMaterialDao.findByCpList(cpTestProgramNameList,
			 * projCodeWVersion); if (cpMaterialTo == null) { cpMaterialTo =
			 * cpMaterialDao.findByCpMaterialNum(cpMaterialNum); if
			 * (cpMaterialTo == null) { String icWaferMaterialNum =
			 * icWaferTo.getMaterialNum(); cpMaterialTo = new CpMaterialTo(); //
			 * Create a new variant String maxCpVar = (String) cpMaterialDao
			 * .findMaxVar(projCodeWVersion); if (maxCpVar != null) { String
			 * preVar = maxCpVar.toString(); if (preVar != null &&
			 * !preVar.equals("")) { char c = (char) ((int) preVar.charAt(0) +
			 * 1); String realVar = ""; if (c > 57 && c < 65) { realVar = new
			 * String(new char[] { 65 }); } else if (c > 90) { realVar = new
			 * String(new char[] { 48 }); } else { realVar = new String(new
			 * char[] { c }); } cpMaterialTo.setCpVariant(realVar); } } else {
			 * cpMaterialTo.setCpVariant("0"); }
			 * 
			 * cpMaterialNum = "C" + icWaferMaterialNum.substring(1,
			 * icWaferMaterialNum .length() - 1) + cpMaterialTo.getCpVariant();
			 * 
			 * cpMaterialTo.setCpMaterialNum(cpMaterialNum);
			 * cpMaterialTo.setProjectCodeWVersion(projCodeWVersion);
			 * cpMaterialTo.setCpTestProgramNameList(cpTestProgramNameList);
			 * 
			 * cpMaterialDao.insert(cpMaterialTo, "PIDB_CP_MATERIAL"); } } }
			 */

			// Added By Hank 2008/01/07
			/*
			 * CpMaterialTo cpMaterialTo =
			 * cpMaterialDao.findByCpMaterialNum(cpMaterialNum);
			 * 
			 * cpMaterialTo.setCpMaterialNum(cpMaterialNum);
			 * cpMaterialTo.setProjectCodeWVersion(projCodeWVersion);
			 * cpMaterialTo.setCpTestProgramNameList(cpTestProgramNameList);
			 * 
			 * Map<String, Object> key = new HashMap<String, Object>();
			 * key.put("CP_MATERIAL_NUM" , cpMaterialNum);
			 * cpMaterialDao.update(cpMaterialTo, "PIDB_CP_MATERIAL", key);
			 */
		}

		// add assignTo and assignEmail
		// String assigns = icFgTo.getAssignTo();
		String assigns = "";
		String[] assignTo = request.getParameterValues("assignTo");
		if (assignTo != null) {
			for (String a : assignTo) {
				assigns += "," + a;
			}
		} else {
			assignTo = new String[] {};
		}

		assigns = assigns.length() > 0 ? assigns.substring(1) : assigns;

		UserDao userDao = new UserDao();
		List<String> emailList = new ArrayList<String>(Arrays.asList(assignTo));
		emailList.add("(R)default_fg_save");

		String emails = userDao.fetchEmail(emailList);
		icFgTo.setAssignTo(assigns);
		icFgTo.setAssignEmail(emails);

		// icFgDao.insert(icFgTo, "PIDB_IC_FG");
		icFgDao.insert(icFgTo);
		//Change Log
		PidbChangeLogDao chlDao = new PidbChangeLogDao();
		chlDao.insertChange("IC_FG", "INSET", AjaxHelper.bean2String(icFgTo), loginUser.getUserId());
		
		// send mail
		Map<String, String> mailMap = new HashMap<String, String>();
		mailMap.put("fab", projectTo.getFab());

		SendMailDispatch
				.sendMailByCreate("MD-9", icFgTo.getPartNum() + "("
						+ icFgTo.getMaterialNum() + ")", emails,
						SendMailDispatch.getUrl("MD_9_EDIT", request
								.getContextPath(), icFgTo, mailMap));

		String toErp = request.getParameter("toErp");
		final String className = this.getClass().getName();
		if ("1".equals(toErp.trim())) {
			final IcFgTo erpTo = icFgTo;
			final UserTo erpUser = loginUser;
			TransactionCallbackWithoutResult callback = new TransactionCallbackWithoutResult() {
				String result = null;

				public void doInTransactionWithoutResult(
						final TransactionStatus status) {
					// result = IcFgERP.release(erpTo, erpUser);
					result = IcFgERP
							.releaseForMPList(erpTo, erpUser, className);
					if (result != null) {
						throw new ReleaseERPException(result);
					}
				}
			};

			try {
				icFgDao.doInTransaction(callback);

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
						+ "materialNum=" + materialNum + "&partNum="
						+ icFgTo.getPartNum() + "&fab=" + projectTo.getFab());

				return actionForward;
			}

			// update status Released
			icFgTo.setStatus("Released");
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("MATERIAL_NUM", icFgTo.getMaterialNum());
			m.put("PART_NUM", icFgTo.getPartNum());
			icFgDao.update(icFgTo, "PIDB_IC_FG", m);

			// send mail
			emailList.remove("(R)default_fg_save");
			emailList.add("(R)default_fg_erp");
			emails = userDao.fetchEmail(emailList);
			SendMailDispatch.sendMailByErp("MD-9", icFgTo.getPartNum() + "("
					+ icFgTo.getMaterialNum() + ")", emails, "");
		}

		String mes = "";
		if ("1".equals(toErp.trim())) {
			mes = "Save and Release to ERP Successfully";
		} else {
			mes = "Save Successfully";
		}

		request.setAttribute("error", mes);
		String targetForward = mapping.findForward("viewEditJsp").getPath();

		if (targetForward.indexOf('?') == -1) {
			targetForward = targetForward + "?";
		} else {
			targetForward = targetForward + "&";
		}

		ActionForward actionForward = new ActionForward(targetForward
				+ "materialNum=" + materialNum + "&partNum="
				+ icFgTo.getPartNum() + "&fab=" + projectTo.getFab());

		return actionForward;

		// return mapping.findForward(forward);
	}
}
