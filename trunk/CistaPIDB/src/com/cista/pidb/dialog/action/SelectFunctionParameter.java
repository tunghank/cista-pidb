package com.cista.pidb.dialog.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.code.dao.FunctionParameterDao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SelectFunctionParameter extends DispatchAction{
	protected final Log logger = LogFactory.getLog(getClass());
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

	public ActionForward list(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "success";
		String callback = request.getParameter("callback");
		String funName = request.getParameter("funName");
		String funFieldName = request.getParameter("funFieldName");
		
		logger.debug("funName " + funName);
		logger.debug("funFieldName " + funFieldName);
		
		FunctionParameterDao fpDao = new FunctionParameterDao();
		request.setAttribute("selectList", fpDao.findLikeFieldValue(funName, funFieldName));
		request.setAttribute("callback", callback);

		return mapping.findForward(forward);
	}

}
