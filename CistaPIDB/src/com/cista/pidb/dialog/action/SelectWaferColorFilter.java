/*
 * 2010.03.23/FCG1 @Jere Huang - 多傳入mp status
 */
package com.cista.pidb.dialog.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.md.dao.WaferColorFilterDao;
import com.cista.pidb.md.to.WaferColorFilterTo;

public class SelectWaferColorFilter extends DispatchAction{
	
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
		String condition = request.getParameter("condition");

		String projCodeWVersion = request.getParameter("projCodeWVersion");
		String remark = request.getParameter("remark");
		String materialNum = request.getParameter("materialNum");
		//FCG1
		String mpStatus = request.getParameter("mpStatus");
		
		List<WaferColorFilterTo> to = new ArrayList<WaferColorFilterTo>();
		WaferColorFilterDao dao = new WaferColorFilterDao();
		to = dao.getByProjCodeWVersion(projCodeWVersion);
		
		request.setAttribute("waferCFList", to);
		request.setAttribute("callback", callback);
		request.setAttribute("condition", condition);
		request.setAttribute("remark", remark);
		request.setAttribute("materialNum", materialNum);
		request.setAttribute("projCodeWVersion", projCodeWVersion);
		//FCG1
		request.setAttribute("mpStatus", mpStatus);
		
		return mapping.findForward(forward);
	}

}
