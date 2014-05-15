package com.cista.pidb.dialog.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.code.dao.FunctionParameterDao;
import com.cista.pidb.md.dao.CpCspMaterialDao;
import com.cista.pidb.md.dao.CpMaterialDao;
import com.cista.pidb.md.to.CpCspMaterialTo;

public class SelectCpCspMaterialNum extends DispatchAction {
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


		String projCodeWVersion = request.getParameter("projCodeWVersion");


		List<CpCspMaterialTo> cpCspList = new ArrayList<CpCspMaterialTo>();
		
		CpCspMaterialDao dao = new CpCspMaterialDao();
		cpCspList = dao.findByProjectCode(projCodeWVersion);
		//Get CSP List
		String fundName = "CSP";
		String funFieldName = "VERSION";
		List versionList = new FunctionParameterDao()
		.findValueList(fundName, funFieldName);
		
		List variantList = new CpMaterialDao()
		.getCpMaterialVariant(projCodeWVersion);
		
		request.setAttribute("versionList", versionList);
		request.setAttribute("variantList", variantList);
		request.setAttribute("cpCspList", cpCspList);
		request.setAttribute("callback", callback);

		request.setAttribute("projCodeWVersion", projCodeWVersion);
		
		return mapping.findForward(forward);
	}

}
