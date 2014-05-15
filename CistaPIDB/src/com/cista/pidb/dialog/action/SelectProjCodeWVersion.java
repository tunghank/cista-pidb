package com.cista.pidb.dialog.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.md.dao.IcWaferDao;


public class SelectProjCodeWVersion extends DispatchAction{

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
             String forward = "success";
            String callback = request.getParameter("callback");
            IcWaferDao icWaferDao = new IcWaferDao();
            request.setAttribute("projCodeWVersionList", icWaferDao.findProjCodeWVersion());
            request.setAttribute("callback", callback);
            return mapping.findForward(forward);

    }
       
    /**
     * Select from T_IC_WAFER a where a.PROJECT_CODE = PROJECT_CODE and a.ROUTING_CP = Check Box Marked.
     * @param mapping ActionMapping
     * @param form ActionForm
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ActionForward
     */
    public ActionForward list(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
             String forward = "success";
            String callback = request.getParameter("callback");
            String projCode = request.getParameter("projCode");
            
            IcWaferDao icWaferDao = new IcWaferDao();
            request.setAttribute("projCodeWVersionList", icWaferDao.findProjCodeWVersion(projCode));
            request.setAttribute("callback", callback);
            return mapping.findForward(forward);

    }
}
