package com.cista.pidb.md.action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.admin.dao.UserDao;
import com.cista.pidb.admin.to.UserTo;
import com.cista.pidb.code.dao.FunctionParameterDao;
import com.cista.pidb.code.dao.SapMasterVendorDao;
import com.cista.pidb.code.to.SapMasterVendorTo;
import com.cista.pidb.core.HttpHelper;
import com.cista.pidb.core.PIDBContext;
import com.cista.pidb.md.dao.PkgRaDao;
import com.cista.pidb.md.to.PkgRaTo;

public class PkgRaEditAction extends DispatchAction {

	public ActionForward pre(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "pre_success";
		PkgRaDao dao = new PkgRaDao();
		// get PkgRaTo
		String prodName = (String) request.getParameter("pdn");
		String pkgCode = (String) request.getParameter("pgc");
		String worksheetNumber = (String) request.getParameter("wsn");
		String pkgType = (String) request.getParameter("pgt");
		PkgRaTo pkgRaTo = (PkgRaTo) dao.findByPrimaryKey(prodName, pkgCode,
				pkgType, worksheetNumber);
		//
		if (pkgRaTo != null) {
			request.setAttribute("ref", pkgRaTo);
		}

		if (pkgType != null && !pkgType.equals("")) {
			if (pkgType.equals("TCP") | pkgType.equals("COF")
					| pkgType.equals("COG")) {
				String funName = "IC_TAPE";
				String funFieldName = "ASSEMBLY_SITE";
				request.setAttribute("assemblySiteList",
						new FunctionParameterDao().findValueList(funName,
								funFieldName));
			} else {
				String funName = "TRAD_PKG";
				String funFieldName = "ASSEMBLY_HOUSE";
				request.setAttribute("assemblySiteList",
						new FunctionParameterDao().findValueList(funName,
								funFieldName));
			}
		}

		String fundName = "RA";
		String funFieldName = "OWNER";

		String fundName3 = "PKG_RA";
		String funFieldName3 = "RPT_VERSION_LIST";

		request.setAttribute("reportVersionList", new FunctionParameterDao()
				.findValueList(fundName3, funFieldName3));

		request.setAttribute("ownerList", new FunctionParameterDao()
				.findValueList(fundName, funFieldName));

		return mapping.findForward(forward);
	}

	public ActionForward save(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "save_success";
		PkgRaDao dao = new PkgRaDao();
		// get from page
		PkgRaTo pkgRaTo = (PkgRaTo) HttpHelper.pickupForm(PkgRaTo.class,
				request, true);
		if (pkgRaTo == null) {
			forward = "fail";
			request.setAttribute("error", "Get PkgRaTo from page fail!");
			return mapping.findForward(forward);
		}
		// set customer
		PkgRaTo exist = dao.findByPrimaryKey(pkgRaTo.getProdName(), pkgRaTo
				.getPkgCode(), pkgRaTo.getPkgType(), pkgRaTo
				.getWorksheetNumber());
		pkgRaTo.setCreatedBy(exist.getCreatedBy());

		// assignEmail
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
		pkgRaTo.setAssignTo(assigns);
		pkgRaTo.setAssignEmail(emails);

		if (pkgRaTo.getTapeVendor() != null
				&& !pkgRaTo.getTapeVendor().equals("")) {
			SapMasterVendorDao SapMasterVendorDao = new SapMasterVendorDao();
			SapMasterVendorTo sapMasterVendorTo = SapMasterVendorDao
					.findByShortName(pkgRaTo.getTapeVendor());
			if (sapMasterVendorTo != null) {
				pkgRaTo.setTapeVendor(sapMasterVendorTo.getVendorCode());
			}
		} 

		// set created by and modified by
		//pkgRaTo.setCreatedBy(creater);
		UserTo loginUser = PIDBContext.getLoginUser(request);
		pkgRaTo.setModifiedBy(loginUser.getUserId());
		dao.updatePkgRa(pkgRaTo);

		// send mail
		SendMailDispatch.sendMailByModify("MD-15", pkgRaTo.getPkgCode() + "("
				+ pkgRaTo.getProdName() + ")", emails, SendMailDispatch.getUrl(
				"MD_15_EDIT", request.getContextPath(), pkgRaTo));

		String mes = "Save Successfully";

		request.setAttribute("error", mes);
		String targetForward = mapping.findForward(forward).getPath();

		if (targetForward.indexOf('?') == -1) {
			targetForward = targetForward + "?";
		} else {
			targetForward = targetForward + "&";
		}

		ActionForward actionForward = new ActionForward(targetForward + "pdn="
				+ pkgRaTo.getProdName() + "&pgc=" + pkgRaTo.getPkgCode()
				+ "&wsn=" + pkgRaTo.getWorksheetNumber() + "&pgt="
				+ pkgRaTo.getPkgType());

		return actionForward;
	}
}
