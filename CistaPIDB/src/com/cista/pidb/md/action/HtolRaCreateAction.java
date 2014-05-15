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
import com.cista.pidb.code.dao.SapMasterCustomerDao;
import com.cista.pidb.code.to.SapMasterCustomerTo;
import com.cista.pidb.core.HttpHelper;
import com.cista.pidb.core.PIDBContext;
import com.cista.pidb.md.dao.HtolRaDao;
import com.cista.pidb.md.to.HtolRaTo;

public class HtolRaCreateAction extends DispatchAction {

	public ActionForward pre(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "create";

		if (request.getParameter("ref") != null) {
			// Create with reference
			String projCodeWVersion = request.getParameter("ref");
			request.setAttribute("ref", new HtolRaDao()
					.findByProjCodeWVersion(projCodeWVersion));
		}

		String fundName = "RA";
		String funFieldName = "OWNER";

		request.setAttribute("ownerList", new FunctionParameterDao()
				.findValueList(fundName, funFieldName));

		return mapping.findForward(forward);
	}

	public ActionForward save(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "success";
		HtolRaDao htolRaDao = new HtolRaDao();
		HtolRaTo htolRaTo = (HtolRaTo) HttpHelper.pickupForm(HtolRaTo.class,
				request);
		UserTo loginUser = PIDBContext.getLoginUser(request);
		htolRaTo.setCreatedBy(loginUser.getUserId());

		// convert Customer
		SapMasterCustomerDao sapMasterCustomerDao = new SapMasterCustomerDao();
		String custName = htolRaTo.getCust();
		String realCusts = "";
		if (custName != null && custName.length() > 0) {
			String[] custs = custName.split(",");
			if (custs != null && custs.length > 0) {
				for (String s : custs) {
					SapMasterCustomerTo to = sapMasterCustomerDao
							.findByShortName(s);
					if (to != null) {
						realCusts += "/" + to.getCustomerCode();
					}
				}
			}
		}

		if (realCusts.length() > 0) {
			realCusts = realCusts.substring(1);
		}

		htolRaTo.setCust(realCusts);

		/*
		 * // convert Owner String field = htolRaTo.getOwner(); String realOwner =
		 * ""; if (field != null && !field.equals("")) { String[] fields =
		 * field.split(","); if (fields != null && fields.length > 0) { for
		 * (String s : fields) { ParameterTo to = htolRaDao.findByShortName(s);
		 * if (to != null) { realOwner +=to.getFieldValue(); } } } }
		 * htolRaTo.setOwner(realOwner);
		 */

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
		htolRaTo.setAssignTo(assigns);
		htolRaTo.setAssignEmail(emails);

		htolRaDao.insert(htolRaTo, "PIDB_HTOL_RA");

		// send mail
		SendMailDispatch.sendMailByCreate("MD-7", htolRaTo
				.getProjCodeWVersion(), emails, SendMailDispatch.getUrl(
				"MD_7_EDIT", request.getContextPath(), htolRaTo));

		String mes = "Save Successfully";

		request.setAttribute("error", mes);
		request.setAttribute("ref", htolRaTo);
		return mapping.findForward(forward);
	}
}