package com.cista.pidb.dialog.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.md.dao.CpMaterialDao;
import com.cista.pidb.md.dao.CpPolishMaterialDao;
import com.cista.pidb.md.to.CpPolishMaterialTo;

public class SelectCpPolishMaterial extends DispatchAction{
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
		String status = request.getParameter("status");

		List<CpPolishMaterialTo> to = new ArrayList<CpPolishMaterialTo>();
		CpPolishMaterialDao dao = new CpPolishMaterialDao();
		to = dao.findByProjectCode(projCodeWVersion);

		request.setAttribute("variantList", new CpMaterialDao().getCpMaterialVariantPolish(projCodeWVersion));
		request.setAttribute("cpPolishList", to);
		request.setAttribute("callback", callback);
		request.setAttribute("condition", condition);
		request.setAttribute("remark", remark);
		request.setAttribute("status", status);
		request.setAttribute("projCodeWVersion", projCodeWVersion);

		return mapping.findForward(forward);
	}

}
