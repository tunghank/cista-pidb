package com.cista.pidb.md.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.code.dao.FunctionParameterDao;
import com.cista.pidb.core.HttpHelper;
import com.cista.pidb.core.PIDBContext;
import com.cista.pidb.md.dao.MpListDao;
import com.cista.pidb.md.to.MpListEolCustTo;
import com.cista.pidb.md.to.MpListQueryTo;
import com.cista.pidb.md.to.MpListTo;

/**
 * .
 * 
 * @author Hu Meixia
 * 
 */
public class MpListQueryAction extends DispatchAction {
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
		MpListDao mpListDao = new MpListDao();

		String fundName2 = "RELEASE_TO_SAP";
		String funFieldName2 = "RELEASE_TO";
		request.setAttribute("companyNameList", new FunctionParameterDao()
				.findValueList(fundName2, funFieldName2));

		request.setAttribute("selectList", mpListDao.findTapeName());
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
		MpListDao mpListDao = new MpListDao();
		MpListQueryTo queryTo = (MpListQueryTo) HttpHelper.pickupForm(
				MpListQueryTo.class, request, true);
		queryTo.setTotalResult(mpListDao.countResult(queryTo));
		request.setAttribute("result", mpListDao.query(queryTo));
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
		MpListDao mpListDao = new MpListDao();
		MpListQueryTo queryTo = (MpListQueryTo) HttpHelper.pickupForm(
				MpListQueryTo.class, request, true);
		request.setAttribute("result", mpListDao.query(queryTo));
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
		MpListDao mpListDao = new MpListDao();
		MpListQueryTo queryTo = (MpListQueryTo) HttpHelper.pickupForm(
				MpListQueryTo.class, request, true);
		queryTo.setPageNo(-1);
		List<MpListTo> result = mpListDao.queryForDomain(queryTo);

		for (MpListTo mplist : result) {
			if (mplist != null) {
				List<MpListEolCustTo> eolCustList = mpListDao
						.findByEolCust(mplist);
				if (eolCustList.size() > 0) {
					String eol = "";
					for (MpListEolCustTo eolCust : eolCustList) {

						eol += "," + eolCust.getEolCust();
					}
					mplist.setEolCust(eol);
				}

			}
		}
		request.setAttribute("reportTitle", PIDBContext
				.getConfig("MD_13_TITLE"));
		request.setAttribute("reportContent", result);
		request
				.setAttribute("reportColumn", PIDBContext
						.getConfig("MD_13_SEQ"));
		return mapping.findForward("report");
	}
}
