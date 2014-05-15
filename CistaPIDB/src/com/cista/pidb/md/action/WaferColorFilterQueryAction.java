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
import com.cista.pidb.md.dao.WaferColorFilterDao;
import com.cista.pidb.md.to.WaferColorFilterQueryTo;
import com.cista.pidb.md.to.WaferColorFilterTo;

public class WaferColorFilterQueryAction extends DispatchAction{
	
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
		WaferColorFilterDao waferCfDao = new WaferColorFilterDao();
		WaferColorFilterQueryTo queryTo = (WaferColorFilterQueryTo) HttpHelper
				.pickupForm(WaferColorFilterQueryTo.class, request, true);
		queryTo.setTotalResult(waferCfDao.countResult(queryTo));
		request.setAttribute("result", waferCfDao.query(queryTo));
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
		WaferColorFilterDao waferCfDao = new WaferColorFilterDao();
		WaferColorFilterQueryTo queryTo = (WaferColorFilterQueryTo) HttpHelper
				.pickupForm(WaferColorFilterQueryTo.class, request, true);
		request.setAttribute("result", waferCfDao.query(queryTo));
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
		WaferColorFilterDao waferCfDao = new WaferColorFilterDao();
		WaferColorFilterQueryTo queryTo = (WaferColorFilterQueryTo) HttpHelper
				.pickupForm(WaferColorFilterQueryTo.class, request, true);
		queryTo.setPageNo(-1);
		List<WaferColorFilterTo> result = waferCfDao.query(queryTo);
		request
				.setAttribute("reportTitle", PIDBContext
						.getConfig("MD_26_TITLE"));
		request.setAttribute("reportContent", result);
		request.setAttribute("reportColumn", PIDBContext.getConfig("MD_26_SEQ"));
		return mapping.findForward("report");
	}

}
