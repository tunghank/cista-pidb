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
import com.cista.pidb.md.dao.CpPolishMaterialDao;
import com.cista.pidb.md.to.CpPolishMaterialQueryTo;
import com.cista.pidb.md.to.CpPolishMaterialTo;
public class CpPolishMaterialQueryAction extends DispatchAction{
	
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
		CpPolishMaterialDao ctpDao = new CpPolishMaterialDao();
		CpPolishMaterialQueryTo queryTo = (CpPolishMaterialQueryTo) HttpHelper
				.pickupForm(CpPolishMaterialQueryTo.class, request, true);
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
		CpPolishMaterialDao ctpDao = new CpPolishMaterialDao();
		CpPolishMaterialQueryTo queryTo = (CpPolishMaterialQueryTo) HttpHelper
				.pickupForm(CpPolishMaterialQueryTo.class, request, true);
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
		CpPolishMaterialDao ctpDao = new CpPolishMaterialDao();
		CpPolishMaterialQueryTo queryTo = (CpPolishMaterialQueryTo) HttpHelper
				.pickupForm(CpPolishMaterialQueryTo.class, request, true);
		queryTo.setPageNo(-1);
		List<CpPolishMaterialTo> result = ctpDao.query(queryTo);
		request
				.setAttribute("reportTitle", PIDBContext
						.getConfig("MD_24_TITLE"));
		request.setAttribute("reportContent", result);
		request.setAttribute("reportColumn", PIDBContext.getConfig("MD_24_SEQ"));
		return mapping.findForward("report");
	}
}
