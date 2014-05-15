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
import com.cista.pidb.md.dao.CpTestProgramDao;
import com.cista.pidb.md.dao.ProjectDao;
import com.cista.pidb.md.to.CpTestProgramQueryTo;
import com.cista.pidb.md.to.CpTestProgramTo;
/**
 * .
 * @author Hu Meixia
 *
 */
public class CpTestProgramQueryAction extends DispatchAction {
    /**
     * .
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
        CpTestProgramDao ctpDao = new CpTestProgramDao();
        FabCodeDao fabCodeDao = new FabCodeDao();
        ProjectDao projectDao = new ProjectDao();
        request.setAttribute("fabCodeList", fabCodeDao.findAll());
        request.setAttribute("cpTestProgRevisionList", ctpDao.findCpTestProgRevision());
        request.setAttribute("optionList", projectDao.findOption());
        return mapping.findForward(forward);
    }
    /**
     * .
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
          CpTestProgramDao ctpDao = new CpTestProgramDao();
          CpTestProgramQueryTo queryTo = (CpTestProgramQueryTo) HttpHelper.pickupForm(CpTestProgramQueryTo.class,
                  request, true);
          queryTo.setTotalResult(ctpDao.countResult(queryTo));
          request.setAttribute("result", ctpDao.query(queryTo));
          request.setAttribute("queryTo", queryTo);
          return mapping.findForward(forward);
    }
    /**
     * .
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
        CpTestProgramDao ctpDao = new CpTestProgramDao();
        CpTestProgramQueryTo queryTo = (CpTestProgramQueryTo) HttpHelper.pickupForm(CpTestProgramQueryTo.class,
                request, true);
        request.setAttribute("result", ctpDao.query(queryTo));
        request.setAttribute("queryTo", queryTo);
        return mapping.findForward(forward);
    }
    /**
     * .
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     */
    public ActionForward download(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        CpTestProgramDao ctpDao = new CpTestProgramDao();
        CpTestProgramQueryTo queryTo = (CpTestProgramQueryTo) HttpHelper.pickupForm(CpTestProgramQueryTo.class,
                request, true);
        queryTo.setPageNo(-1);
        List<CpTestProgramTo> result = ctpDao.queryTo(queryTo);
        request.setAttribute("reportTitle", PIDBContext.getConfig("MD_8_TITLE"));
        request.setAttribute("reportContent", result);
        request.setAttribute("reportColumn", PIDBContext.getConfig("MD_8_SEQ"));
        return mapping.findForward("report");
    }
}
