package com.cista.pidb.ajax;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.md.dao.WaferColorFilterDao;
import com.cista.pidb.md.to.WaferColorFilterTo;

public class FetchIcWfWaferCf extends Action {
	/**
	 * Do action performance.
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
	 * @throws Exception
	 *             exception
	 */
	public ActionForward execute(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {

		String projWver = request.getParameter("projWver");

		WaferColorFilterDao turnKeyDao = new WaferColorFilterDao();
		List<WaferColorFilterTo> turnKeyList = turnKeyDao
				.getByProjCodeWVersion(projWver);
		StringBuilder sb = new StringBuilder();
		if (turnKeyList != null) {
			for (WaferColorFilterTo to : turnKeyList) {
				sb.append("|").append(to.getWaferCfMaterialNum());
			}
		}

		PrintWriter out = response.getWriter();
		out.print(sb.length() > 0 ? sb.substring(1) : "");

		return null;
	}

}
