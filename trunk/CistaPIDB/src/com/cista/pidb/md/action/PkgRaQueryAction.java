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
import com.cista.pidb.md.dao.IcTapeDao;
import com.cista.pidb.md.dao.PkgRaDao;
import com.cista.pidb.md.to.PkgRaQueryTo;
import com.cista.pidb.md.to.PkgRaTo;


public class PkgRaQueryAction extends DispatchAction {

	public ActionForward pre(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		IcTapeDao icTapeDao = new IcTapeDao();
		List<String> pkgVersionList = icTapeDao.findAllPkgVersion();
		request.setAttribute("pkgVersionList", pkgVersionList);
		String forward = "pre_success";

		String fundName = "RA";
		String funFieldName = "OWNER";
		
		String fundName2 = "IC_TAPE";
		String funFieldName2 = "ASSEMBLY_SITE";

		request.setAttribute("assemblySiteList", new FunctionParameterDao()
				.findValueList(fundName2, funFieldName2));

		request.setAttribute("ownerList", new FunctionParameterDao()
				.findValueList(fundName, funFieldName));

		return mapping.findForward(forward);
	}

	public ActionForward query(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "query_success";
		PkgRaDao dao = new PkgRaDao();
		PkgRaQueryTo queryTo = (PkgRaQueryTo) HttpHelper.pickupForm(
				PkgRaQueryTo.class, request, true);
		queryTo.setTotalResult(dao.countResult(queryTo));
		List<PkgRaTo> result = dao.query(queryTo);
		request.setAttribute("result", result);
		request.setAttribute("criteria", queryTo);
		return mapping.findForward(forward);
	}

	public ActionForward paging(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "result";
		PkgRaDao pkgRaDao = new PkgRaDao();
		PkgRaQueryTo queryTo = (PkgRaQueryTo) HttpHelper.pickupForm(
				PkgRaQueryTo.class, request, true);
		queryTo.setTotalResult(pkgRaDao.countResult(queryTo));
		request.setAttribute("result", pkgRaDao.query(queryTo));
		request.setAttribute("criteria", queryTo);
		return mapping.findForward(forward);
	}

	public ActionForward download(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		PkgRaDao pkgRaDao = new PkgRaDao();
		PkgRaQueryTo queryTo = (PkgRaQueryTo) HttpHelper.pickupForm(
				PkgRaQueryTo.class, request, true);
		queryTo.setPageNo(-1);
		List<PkgRaTo> result = (List<PkgRaTo>) pkgRaDao
				.queryForDownload(queryTo);
		request.setAttribute("reportTitle", PIDBContext
				.getConfig("MD_15_TITLE"));
		request.setAttribute("reportContent", result);
		request
				.setAttribute("reportColumn", PIDBContext
						.getConfig("MD_15_SEQ"));
		return mapping.findForward("report");
	}

}
