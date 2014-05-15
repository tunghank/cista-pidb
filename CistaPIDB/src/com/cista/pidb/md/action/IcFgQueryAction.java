package com.cista.pidb.md.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.code.dao.FabCodeDao;
import com.cista.pidb.code.dao.FunctionParameterDao;
import com.cista.pidb.code.to.FabCodeTo;
import com.cista.pidb.core.HttpHelper;
import com.cista.pidb.core.PIDBContext;
import com.cista.pidb.md.dao.IcFgDao;
import com.cista.pidb.md.dao.ProjectCodeDao;
import com.cista.pidb.md.to.IcFgQueryTo;
import com.cista.pidb.md.to.IcFgTo;
import com.cista.pidb.md.to.ProjectCodeTo;

public class IcFgQueryAction extends DispatchAction {
	public ActionForward pre(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "success";
		ProjectCodeDao projectCodeDao = new ProjectCodeDao();
		FabCodeDao fabCodeDao = new FabCodeDao();
		List<ProjectCodeTo> pctl = projectCodeDao.findAll();
		List<FabCodeTo> fctl = fabCodeDao.findAll();
		List<String> optionList = new ArrayList<String>();
		List<String> fabList = new ArrayList<String>();

		for (ProjectCodeTo pct : pctl) {
			if (StringUtils.isNotEmpty(pct.getProjOption())
					&& !optionList.contains(pct.getProjOption())) {
				optionList.add(pct.getProjOption());
			}
		}
		for (FabCodeTo fct : fctl) {
			if (fct.getFab() != null && !fct.getFab().equals("")
					&& !fabList.contains(fct.getFab())) {
				fabList.add(fct.getFab());
			}
		}

		String fundName2 = "RELEASE_TO_SAP";
		String funFieldName2 = "RELEASE_TO";
		request.setAttribute("companyNameList", new FunctionParameterDao()
				.findValueList(fundName2, funFieldName2));

		request.setAttribute("optionList", optionList);
		request.setAttribute("fabList", fabCodeDao.findAll());
		return mapping.findForward(forward);
	}

	public ActionForward query(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "result";
		IcFgDao icFgDao = new IcFgDao();
		IcFgQueryTo queryTo = (IcFgQueryTo) HttpHelper.pickupForm(
				IcFgQueryTo.class, request, true);
		queryTo.setTotalResult(icFgDao.countResult(queryTo));
		request.setAttribute("result", icFgDao.query(queryTo));
		request.setAttribute("queryTo", queryTo);
		return mapping.findForward(forward);
	}

	public ActionForward paging(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "result";
		IcFgDao icFgDao = new IcFgDao();
		IcFgQueryTo queryTo = (IcFgQueryTo) HttpHelper.pickupForm(
				IcFgQueryTo.class, request, true);
		queryTo.setTotalResult(icFgDao.countResult(queryTo));
		request.setAttribute("result", icFgDao.query(queryTo));
		request.setAttribute("queryTo", queryTo);
		return mapping.findForward(forward);
	}

	public ActionForward download(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		IcFgDao icFgDao = new IcFgDao();
		IcFgQueryTo queryTo = (IcFgQueryTo) HttpHelper.pickupForm(
				IcFgQueryTo.class, request, true);
		queryTo.setPageNo(-1);
		List<IcFgTo> result = icFgDao.queryForDomain(queryTo);
		request
				.setAttribute("reportTitle", PIDBContext
						.getConfig("MD_9_TITLE"));
		request.setAttribute("reportContent", result);
		request.setAttribute("reportColumn", PIDBContext.getConfig("MD_9_SEQ"));
		return mapping.findForward("report");
	}
}
