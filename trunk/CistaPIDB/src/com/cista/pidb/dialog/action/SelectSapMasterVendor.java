package com.cista.pidb.dialog.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.code.dao.SapMasterVendorDao;

public class SelectSapMasterVendor extends DispatchAction {
    /**
     * .
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     */
    public ActionForward list(final ActionMapping mapping,
               final ActionForm form, final HttpServletRequest request,
               final HttpServletResponse response) {
           String forward = "check_success";
           String callback = request.getParameter("callback");
           String condition = request.getParameter("condition");
           SapMasterVendorDao smcDao = new SapMasterVendorDao();
           request.setAttribute("selectList", smcDao.queryByVendorCode(condition));
           request.setAttribute("callback", callback);
           request.setAttribute("condition", condition);
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
    public ActionForward rad(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        String forward = "radio_success";
        String callback = request.getParameter("callback");
        SapMasterVendorDao smcDao = new SapMasterVendorDao();
        request.setAttribute("selectList", smcDao.findAll());
        request.setAttribute("callback", callback);
        return mapping.findForward(forward);
    }
}
