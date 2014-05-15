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
import com.cista.pidb.md.dao.EsdTestDao;
import com.cista.pidb.md.to.EsdTestQueryTo;
import com.cista.pidb.md.to.EsdTestTo;

public class EsdTestQueryAction extends DispatchAction {

	public ActionForward pre(ActionMapping mapping, final ActionForm form,
			final HttpServletRequest request, final HttpServletResponse response) {
		String forward = "pre_success";

		String fundName = "RA";
		String funFieldName = "OWNER";

		request.setAttribute("ownerList", new FunctionParameterDao()
				.findValueList(fundName, funFieldName));

		return mapping.findForward(forward);
	}

	public ActionForward query(ActionMapping mapping, final ActionForm form,
			final HttpServletRequest request, final HttpServletResponse response) {
		String forward = "query_success";
		EsdTestDao dao = new EsdTestDao();
		EsdTestQueryTo queryTo = (EsdTestQueryTo) HttpHelper.pickupForm(
				EsdTestQueryTo.class, request, true);
		queryTo.setTotalResult((int) dao.countResult(queryTo));
		request.setAttribute("result", dao.query(queryTo));
		request.setAttribute("criteria", queryTo);
		return mapping.findForward(forward);
	}

	public ActionForward paging(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		String forward = "paging_success";
		EsdTestDao dao = new EsdTestDao();
		EsdTestQueryTo queryTo = (EsdTestQueryTo) HttpHelper.pickupForm(
				EsdTestQueryTo.class, request, true);
		// queryTo.setTotalResult(dao.countResult(queryTo));
		request.setAttribute("result", dao.query(queryTo));
		request.setAttribute("criteria", queryTo);
		return mapping.findForward(forward);
	}

	public ActionForward download(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) {
		EsdTestDao dao = new EsdTestDao();
		EsdTestQueryTo queryTo = (EsdTestQueryTo) HttpHelper.pickupForm(
				EsdTestQueryTo.class, request, true);
		queryTo.setPageNo(-1);
		List<EsdTestTo> result = dao.queryForDownload(queryTo);
		request
				.setAttribute("reportTitle", PIDBContext
						.getConfig("MD_5_TITLE"));
		request.setAttribute("reportContent", result);
		request.setAttribute("reportColumn", PIDBContext.getConfig("MD_5_SEQ"));
		return mapping.findForward("report");
	}
}
