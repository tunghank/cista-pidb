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
import com.cista.pidb.md.dao.HtolRaDao;
import com.cista.pidb.md.dao.ProjectDao;
import com.cista.pidb.md.to.HtolRaQueryTo;
import com.cista.pidb.md.to.HtolRaTo;

public class HtolRaQueryAction extends DispatchAction {
	public ActionForward pre(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "success";
		ProjectDao projectDao = new ProjectDao();
		List<String> fabList = projectDao.findFab();
		List<String> optionList = projectDao.findOption();

		request.setAttribute("fabList", fabList);
		request.setAttribute("optionList", optionList);

		String fundName = "RA";
		String funFieldName = "OWNER";

		request.setAttribute("ownerList", new FunctionParameterDao()
				.findValueList(fundName, funFieldName));

		return mapping.findForward(forward);
	}

	public ActionForward query(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "result";
		HtolRaDao htolRaDao = new HtolRaDao();
		HtolRaQueryTo queryTo = (HtolRaQueryTo) HttpHelper.pickupForm(
				HtolRaQueryTo.class, request, true);
		queryTo.setTotalResult(htolRaDao.countResult(queryTo));
		request.setAttribute("result", htolRaDao.query(queryTo));
		request.setAttribute("queryTo", queryTo);
		return mapping.findForward(forward);
	}

	public ActionForward paging(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "result";
		HtolRaDao htolRaDao = new HtolRaDao();
		HtolRaQueryTo queryTo = (HtolRaQueryTo) HttpHelper.pickupForm(
				HtolRaQueryTo.class, request, true);
		queryTo.setTotalResult(htolRaDao.countResult(queryTo));
		request.setAttribute("result", htolRaDao.query(queryTo));
		request.setAttribute("queryTo", queryTo);
		return mapping.findForward(forward);
	}

	public ActionForward download(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		HtolRaDao htolRaDao = new HtolRaDao();
		HtolRaQueryTo queryTo = (HtolRaQueryTo) HttpHelper.pickupForm(
				HtolRaQueryTo.class, request, true);
		queryTo.setPageNo(-1);
		List<HtolRaTo> result = htolRaDao.queryForDomain(queryTo);
		request
				.setAttribute("reportTitle", PIDBContext
						.getConfig("MD_7_TITLE"));
		request.setAttribute("reportContent", result);
		request.setAttribute("reportColumn", PIDBContext.getConfig("MD_7_SEQ"));
		return mapping.findForward("report");
	}
}
