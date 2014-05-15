package com.cista.pidb.md.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.code.dao.FabCodeDao;
import com.cista.pidb.code.dao.FunctionParameterDao;
import com.cista.pidb.code.dao.SapMasterProductFamilyDao;
import com.cista.pidb.core.HttpHelper;
import com.cista.pidb.core.PIDBContext;
import com.cista.pidb.md.dao.ProjectDao;
import com.cista.pidb.md.to.ProjectQueryTo;
import com.cista.pidb.md.to.ProjectTo;

public class ProjectQueryAction extends DispatchAction {
	public ActionForward pre(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "success";
		FabCodeDao fabCodeDao = new FabCodeDao();
		String fundName = "PROJECT";
		String funFieldName = "PROCESS_TECHNOLOGY";
		request.setAttribute("processTechnology", new FunctionParameterDao()
				.findValueList(fundName, funFieldName));
		String fundName2 = "RELEASE_TO_SAP";
		String funFieldName2 = "RELEASE_TO";
		request.setAttribute("companyNameList", new FunctionParameterDao()
				.findValueList(fundName2, funFieldName2));
		request.setAttribute("fabCodeList", fabCodeDao.findAll());
		SapMasterProductFamilyDao sapMasterProductFamilyDao = new SapMasterProductFamilyDao();
		request.setAttribute("productFamilyList", sapMasterProductFamilyDao
				.findAll());

		return mapping.findForward(forward);
	}

	public ActionForward query(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "result";
		ProjectDao projectDao = new ProjectDao();
		ProjectQueryTo queryTo = (ProjectQueryTo) HttpHelper.pickupForm(
				ProjectQueryTo.class, request, true);
		queryTo.setTotalResult(projectDao.countResult(queryTo));
		request.setAttribute("result", projectDao.query(queryTo));
		request.setAttribute("queryTo", queryTo);
		return mapping.findForward(forward);
	}

	public ActionForward paging(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "result";
		ProjectDao projectDao = new ProjectDao();
		ProjectQueryTo queryTo = (ProjectQueryTo) HttpHelper.pickupForm(
				ProjectQueryTo.class, request, true);
		request.setAttribute("result", projectDao.query(queryTo));
		request.setAttribute("queryTo", queryTo);
		return mapping.findForward(forward);
	}

	public ActionForward download(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		ProjectDao projectDao = new ProjectDao();
		ProjectQueryTo queryTo = (ProjectQueryTo) HttpHelper.pickupForm(
				ProjectQueryTo.class, request, true);
		queryTo.setPageNo(-1);
		List<ProjectTo> result = projectDao.queryForDomain(queryTo);
		request
				.setAttribute("reportTitle", PIDBContext
						.getConfig("MD_1_TITLE"));
		request.setAttribute("reportContent", result);
		request.setAttribute("reportColumn", PIDBContext.getConfig("MD_1_SEQ"));

		return mapping.findForward("report");
	}
}
