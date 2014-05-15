package com.cista.pidb.md.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.code.dao.SapMasterProductFamilyDao;
import com.cista.pidb.core.HttpHelper;
import com.cista.pidb.core.PIDBContext;
import com.cista.pidb.md.dao.ProdStdTestRefDao;
import com.cista.pidb.md.to.ProdStdTestRefQueryTo;
import com.cista.pidb.md.to.ProdStdTestRefTo;
/**
 * .
 * @author Hu Meixia
 *
 */
public class ProdStdTestRefQueryAction extends DispatchAction {
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
        SapMasterProductFamilyDao smpfDao = new SapMasterProductFamilyDao();
        request.setAttribute("productFamily", smpfDao.findAll());
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
            ProdStdTestRefDao prodStdTestRefDao = new ProdStdTestRefDao();
            ProdStdTestRefQueryTo queryTo = (ProdStdTestRefQueryTo) HttpHelper.pickupForm(ProdStdTestRefQueryTo.class,
                    request, true);
            queryTo.setTotalResult(prodStdTestRefDao.countResult(queryTo));
            request.setAttribute("result", prodStdTestRefDao.query(queryTo));
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
            ProdStdTestRefDao prodStdTestRefDao = new ProdStdTestRefDao();
            ProdStdTestRefQueryTo queryTo = (ProdStdTestRefQueryTo) HttpHelper.pickupForm(ProdStdTestRefQueryTo.class,
                    request, true);
            queryTo.setTotalResult(prodStdTestRefDao.countResult(queryTo));
            request.setAttribute("result", prodStdTestRefDao.query(queryTo));
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
            ProdStdTestRefDao prodStdTestRefDao = new ProdStdTestRefDao();
            ProdStdTestRefQueryTo queryTo = (ProdStdTestRefQueryTo) HttpHelper.pickupForm(ProdStdTestRefQueryTo.class,
                    request, true);
            queryTo.setPageNo(-1);
            List<ProdStdTestRefTo> result = prodStdTestRefDao.query(queryTo);
            request.setAttribute("reportTitle", PIDBContext.getConfig("MD_17_TITLE"));
            request.setAttribute("reportContent", result);
            request.setAttribute("reportColumn", PIDBContext.getConfig("MD_17_SEQ"));
            return mapping.findForward("report");
        }
}
