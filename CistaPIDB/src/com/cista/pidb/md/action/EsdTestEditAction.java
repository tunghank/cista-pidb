package com.cista.pidb.md.action;

import java.util.HashMap;
import java.util.Map;

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

public class EsdTestEditAction extends DispatchAction {

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

		String version = (String) request.getParameter("projCodeWVersion");
		String idEsdTest = (String) request.getParameter("idEsdTesting");
		request.setAttribute("ref", new EsdTestDao().findByProjCodeWVersion(
				version, idEsdTest));
		// }
		return mapping.findForward(forward);
	}

	public ActionForward save(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "save";
		EsdTestDao dao = new EsdTestDao();
		// FunctionParameterDao fpDao = new FunctionParameterDao();
		EsdTestTo esdTo = (EsdTestTo) HttpHelper.pickupForm(EsdTestTo.class,
				request);

		// convert Owner
		/*
		 * String field = esdTo.getOwner(); String fundName = "RA"; String
		 * funFieldName = "OWNER"; String realOwner = ""; if (field != null &&
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
		esdTo.setModifiedBy(loginUser.getUserId());
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("PROJ_CODE_W_VERSION", esdTo.getProjCodeWVersion());
		dao.update(esdTo, "PIDB_ESD_TEST", m);

		String mes = "Save Successfully";

		request.setAttribute("error", mes);
		request.setAttribute("ref", esdTo);

		// send mail
		SendMailDispatch.sendMailByModify("MD-5", esdTo.getProjCodeWVersion(),
				emails, SendMailDispatch.getUrl("MD_5_EDIT", request
						.getContextPath(), esdTo));

		/*
		 * String targetForward = mapping.findForward(forward).getPath(); if
		 * (targetForward.indexOf('?') == -1) { targetForward = targetForward +
		 * "?"; } else { targetForward = targetForward + "&"; }
		 * 
		 * ActionForward actionForward = new ActionForward(targetForward +
		 * "&projCodeWVersion="+ esdTo.getProjCodeWVersion());
		 */

		return mapping.findForward(forward);
	}

}
