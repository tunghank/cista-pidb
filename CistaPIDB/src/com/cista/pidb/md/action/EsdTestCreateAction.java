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
import com.cista.pidb.core.HttpHelper;
import com.cista.pidb.core.PIDBContext;
import com.cista.pidb.md.dao.EsdTestDao;
import com.cista.pidb.md.to.EsdTestTo;

public class EsdTestCreateAction extends DispatchAction {

	public ActionForward pre(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "pre";

		String fundName = "RA";
		String funFieldName = "OWNER";
		String funFieldName2 = "TEST_MODE";

		request.setAttribute("ownerList", new FunctionParameterDao()
				.findValueList(fundName, funFieldName));

		request.setAttribute("testModeList", new FunctionParameterDao()
				.findValueList(fundName, funFieldName2));

		if (request.getParameter("ref") != null) {
			// Create with reference
			String projCodeWVersion = request.getParameter("ref");
			request.setAttribute("ref", new EsdTestDao()
					.findByProjCodeWVersion(projCodeWVersion));

			request.setAttribute("ownerList", new FunctionParameterDao()
					.findValueList(fundName, funFieldName));

			request.setAttribute("testModeList", new FunctionParameterDao()
					.findValueList(fundName, funFieldName2));
		}
		return mapping.findForward(forward);
	}

	public ActionForward save(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "save";
		EsdTestTo esdTo = (EsdTestTo) HttpHelper.pickupForm(EsdTestTo.class,
				request);
		// FunctionParameterDao fpDao = new FunctionParameterDao();
		EsdTestDao esdTestRaDao = new EsdTestDao();
		// convert Owner
		/*
		 * String fundName = "RA"; String funFieldName = "OWNER"; String field =
		 * esdTo.getOwner(); String realOwner = ""; if (field != null &&
		 * !field.equals("")) { String[] fields = field.split(","); if (fields !=
		 * null && fields.length > 0) { for (String s : fields) {
		 * FunctionParameterTo to = fpDao.findValueByShowName( fundName,
		 * funFieldName, s); if (to != null) { realOwner += to.getFieldValue(); } } } }
		 * esdTo.setOwner(realOwner);
		 */

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
		esdTo.setAssignTo(assigns);
		esdTo.setAssignEmail(emails);

		UserTo loginUser = PIDBContext.getLoginUser(request);
		esdTo.setCreatedBy(loginUser.getUserId());
		esdTestRaDao.insertEsdTest(esdTo);

		String mes = "Save Successfully";

		request.setAttribute("error", mes);

		request.setAttribute("ref", esdTo);
		// send mail
		SendMailDispatch.sendMailByCreate("MD-5", esdTo.getProjCodeWVersion(),
				emails, SendMailDispatch.getUrl("MD_5_EDIT", request
						.getContextPath(), esdTo));

		return mapping.findForward(forward);
	}
}
