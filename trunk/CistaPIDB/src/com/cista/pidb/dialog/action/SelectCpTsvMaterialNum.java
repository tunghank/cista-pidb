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
import com.cista.pidb.md.dao.CpMaterialDao;
import com.cista.pidb.md.dao.CpTsvMaterialDao;
import com.cista.pidb.md.to.CpTsvMaterialTo;

public class SelectCpTsvMaterialNum extends DispatchAction {
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


		List<CpTsvMaterialTo> cpTsvList = new ArrayList<CpTsvMaterialTo>();
		
		CpTsvMaterialDao tsvDao = new CpTsvMaterialDao();
		cpTsvList = tsvDao.findByProjectCode(projCodeWVersion);
		//Get CSP List
		String fundName = "TSV";
		String funFieldName = "VERSION";
		List tsvVersionList = new FunctionParameterDao()
		.findValueList(fundName, funFieldName);
		
		List variantList = new CpMaterialDao()
		.getCpMaterialVariant(projCodeWVersion);
		
		request.setAttribute("tsvVersionList", tsvVersionList);
		request.setAttribute("variantList", variantList);
		request.setAttribute("cpTsvList", cpTsvList);
		request.setAttribute("callback", callback);

		request.setAttribute("projCodeWVersion", projCodeWVersion);
		
		return mapping.findForward(forward);
	}

}
