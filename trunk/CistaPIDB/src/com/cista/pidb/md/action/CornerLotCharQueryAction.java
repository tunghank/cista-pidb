package com.cista.pidb.md.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.code.dao.FabCodeDao;
import com.cista.pidb.core.HttpHelper;
import com.cista.pidb.core.PIDBContext;
import com.cista.pidb.md.dao.CornerLotCharDao;
import com.cista.pidb.md.dao.ProjectCodeDao;
import com.cista.pidb.md.to.CornerLotCharQueryTo;
import com.cista.pidb.md.to.CornerLotCharTo;

public class CornerLotCharQueryAction extends DispatchAction {

    public ActionForward pre(ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        String forward = "pre_success";

        FabCodeDao fabCodeDao = new FabCodeDao();
        request.setAttribute("fabCodeList", fabCodeDao.findAll());

        List<String> optionList = new ArrayList<String>();
        ProjectCodeDao projCodeDao = new ProjectCodeDao();
        optionList = projCodeDao.findDistAllOption();

        request.setAttribute("optionList", optionList);

        return mapping.findForward(forward);
    }

    public ActionForward query(ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        String forward = "query_succuss";
        CornerLotCharDao cornerDao = new CornerLotCharDao();
        CornerLotCharQueryTo queryTo = (CornerLotCharQueryTo) HttpHelper
                .pickupForm(CornerLotCharQueryTo.class, request, true);
        List<CornerLotCharQueryTo> cornerToList = cornerDao
                .queryByQueryPage(queryTo);
        queryTo.setTotalResult(cornerDao.countResult(queryTo));
        request.setAttribute("result", cornerToList);
        request.setAttribute("criteria", queryTo);
        return mapping.findForward(forward);
    }

    public ActionForward paging(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        String forward = "page_success";
        CornerLotCharDao cornerDao = new CornerLotCharDao();
        CornerLotCharQueryTo cornerQueryTo = (CornerLotCharQueryTo) HttpHelper
                .pickupForm(CornerLotCharQueryTo.class, request, true);
        request.setAttribute("result", cornerDao
                .queryByQueryPage(cornerQueryTo));
        request.setAttribute("criteria", cornerQueryTo);
        return mapping.findForward(forward);
    }

    public ActionForward download(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        CornerLotCharDao dao = new CornerLotCharDao();
        CornerLotCharQueryTo queryTo = (CornerLotCharQueryTo) HttpHelper
                .pickupForm(CornerLotCharQueryTo.class, request, true);
        queryTo.setPageNo(-1);
        List<CornerLotCharTo> result = dao.queryForDomain(queryTo);
        request
                .setAttribute("reportTitle", PIDBContext
                        .getConfig("MD_6_TITLE"));
        request.setAttribute("reportContent", result);
        request.setAttribute("reportColumn", PIDBContext.getConfig("MD_6_SEQ"));
        return mapping.findForward("report");
    }

}
