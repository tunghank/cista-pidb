package com.cista.pidb.admin.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.admin.dao.ParameterDao;
import com.cista.pidb.admin.to.ParameterTo;
import com.cista.pidb.core.HttpHelper;

public class ParameterEditAction extends DispatchAction {
	public ActionForward pre(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "success";
		String funName = request.getParameter("funName");
		String funFieldName = request.getParameter("funFieldName");
		String fieldValue = request.getParameter("fieldValue");
		String fieldShowName = request.getParameter("fieldShowName");
		ParameterTo to = new ParameterDao().findList(funName, funFieldName,
				fieldValue, fieldShowName);
		if (to != null) {
			request.setAttribute("ref", to);
		}
		return mapping.findForward(forward);
	}

	public ActionForward save(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "return2list";
		ParameterDao paraDao = new ParameterDao();
		ParameterTo newList = (ParameterTo) HttpHelper.pickupForm(
				ParameterTo.class, request);

		paraDao.updateParameter(newList);

		String mes = "Save Successfully";
		request.setAttribute("error", mes);
		request.setAttribute("ref", newList);

		String targetForward = mapping.findForward("viewEditJsp").getPath();
		if (targetForward.indexOf('?') == -1) {
			targetForward = targetForward + "?";
		} else {
			targetForward = targetForward + "&";
		}
		ActionForward action = new ActionForward(targetForward + "funName="
				+ newList.getFunName() + "&funFieldName="
				+ newList.getFunFieldName() + "&fieldValue="
				+ newList.getFieldValue() + "&fieldShowName="
				+ newList.getFieldShowName());
		return action;
	}
}