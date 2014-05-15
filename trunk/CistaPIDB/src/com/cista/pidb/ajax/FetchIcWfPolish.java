package com.cista.pidb.ajax;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.md.dao.CpPolishMaterialDao;
import com.cista.pidb.md.to.CpPolishMaterialTo;

public class FetchIcWfPolish extends Action {

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

		CpPolishMaterialDao polishDao = new CpPolishMaterialDao();
		List<CpPolishMaterialTo> polishList = polishDao
				.findByProjectCode(projWver);
		StringBuilder sb = new StringBuilder();
		if (polishList != null) {
			for (CpPolishMaterialTo to : polishList) {
				sb.append("|").append(to.getCpPolishMaterialNum());
			}
		}

		PrintWriter out = response.getWriter();
		out.print(sb.length() > 0 ? sb.substring(1) : "");

		return null;
	}

}
