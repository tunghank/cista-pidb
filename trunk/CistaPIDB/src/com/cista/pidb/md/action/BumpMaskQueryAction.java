package com.cista.pidb.md.action;

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
import com.cista.pidb.md.dao.BumpMaskDao;
import com.cista.pidb.md.dao.ProjectDao;
import com.cista.pidb.md.to.BumpMaskQueryTo;
import com.cista.pidb.md.to.BumpMaskTo;
/**
 * .
 * @author Hu Meixia
 *
 */
public class BumpMaskQueryAction extends DispatchAction {
    /**
     * The method to view the new jsp.
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     */
    public ActionForward pre(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        String forward = "query";
        BumpMaskDao bumpMaskDao = new BumpMaskDao();
        FabCodeDao fabCodeDao = new FabCodeDao();
        ProjectDao projectDao = new ProjectDao();
        request.setAttribute("fabCodeList", fabCodeDao.findAll());
        request.setAttribute("selectList", bumpMaskDao.findAll());
        request.setAttribute("optionList", projectDao.findOption());
        return mapping.findForward(forward);
    }
    /**
     * The method to query.
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     */
    public ActionForward query(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
          String forward = "result";
          BumpMaskDao bumpMaskDao = new BumpMaskDao();
          BumpMaskQueryTo queryTo = (BumpMaskQueryTo) HttpHelper.pickupForm(BumpMaskQueryTo.class, request, true);
          queryTo.setTotalResult(bumpMaskDao.countResult(queryTo));
          request.setAttribute("result", bumpMaskDao.query(queryTo));
          request.setAttribute("queryTo", queryTo);
          return mapping.findForward(forward);
    }
    /**
     * The method to paging.
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     */
    public ActionForward paging(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        String forward = "result";
        BumpMaskDao bumpMaskDao = new BumpMaskDao();
        BumpMaskQueryTo queryTo = (BumpMaskQueryTo) HttpHelper.pickupForm(BumpMaskQueryTo.class, request, true);
        request.setAttribute("result", bumpMaskDao.query(queryTo));
        request.setAttribute("queryTo", queryTo);
        return mapping.findForward(forward);
    }
    /**
     * The method download data to excel.
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     */
    public ActionForward download(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        BumpMaskDao bumpMaskDao = new BumpMaskDao();
        BumpMaskQueryTo queryTo = (BumpMaskQueryTo) HttpHelper.pickupForm(BumpMaskQueryTo.class, request, true);
        queryTo.setPageNo(-1);
        List<BumpMaskTo> result = bumpMaskDao.queryTo(queryTo);
        request.setAttribute("reportTitle", PIDBContext.getConfig("MD_3_TITLE"));
        request.setAttribute("reportContent", result);
        request.setAttribute("reportColumn", PIDBContext.getConfig("MD_3_SEQ"));
        return mapping.findForward("report");
    }
}
