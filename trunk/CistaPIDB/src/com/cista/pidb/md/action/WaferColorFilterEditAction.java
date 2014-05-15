package com.cista.pidb.md.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.md.dao.WaferColorFilterDao;

public class WaferColorFilterEditAction extends DispatchAction{
	
	public ActionForward pre(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "viewJsp";
		String waferCfMaterial = (String) request.getParameter("waferCfMaterialNum");
		String projCodeWVersion = (String) request
				.getParameter("projCodeWVersion");

		request.setAttribute("ref", new WaferColorFilterDao().findByPrimaryKey(
				waferCfMaterial, projCodeWVersion));
		return mapping.findForward(forward);
	}

}
