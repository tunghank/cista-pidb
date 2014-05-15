package com.cista.pidb.md.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.core.HttpHelper;
import com.cista.pidb.core.PIDBContext;
import com.cista.pidb.md.dao.CpMaterialDao;
import com.cista.pidb.md.to.CpMaterialQueryTo;
import com.cista.pidb.md.to.CpMaterialTo;

public class CpMaterialQueryAction extends DispatchAction {
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
	public ActionForward pre(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "query";

		return mapping.findForward(forward);
	}

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
	public ActionForward query(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "result";
		CpMaterialDao ctpDao = new CpMaterialDao();
		CpMaterialQueryTo queryTo = (CpMaterialQueryTo) HttpHelper
				.pickupForm(CpMaterialQueryTo.class, request, true);
		queryTo.setTotalResult(ctpDao.countResult(queryTo));
		request.setAttribute("result", ctpDao.query(queryTo));
		request.setAttribute("queryTo", queryTo);
		return mapping.findForward(forward);
	}

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
	public ActionForward paging(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "result";
		CpMaterialDao ctpDao = new CpMaterialDao();
		CpMaterialQueryTo queryTo = (CpMaterialQueryTo) HttpHelper
				.pickupForm(CpMaterialQueryTo.class, request, true);
		request.setAttribute("result", ctpDao.query(queryTo));
		request.setAttribute("queryTo", queryTo);
		return mapping.findForward(forward);
	}

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
	public ActionForward download(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		CpMaterialDao ctpDao = new CpMaterialDao();
		CpMaterialQueryTo queryTo = (CpMaterialQueryTo) HttpHelper
				.pickupForm(CpMaterialQueryTo.class, request, true);
		queryTo.setPageNo(-1);
		List<CpMaterialTo> result = ctpDao.query(queryTo);
		request
				.setAttribute("reportTitle", PIDBContext
						.getConfig("MD_23_TITLE"));
		request.setAttribute("reportContent", result);
		request.setAttribute("reportColumn", PIDBContext.getConfig("MD_23_SEQ"));
		return mapping.findForward("report");
	}

}
