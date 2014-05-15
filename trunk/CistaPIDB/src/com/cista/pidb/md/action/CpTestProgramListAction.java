package com.cista.pidb.md.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.md.dao.CpTestProgramDao;
/**
 * .
 * @author Hu Meixia
 *
 */
public class CpTestProgramListAction extends DispatchAction {
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
        String forward = "success";
        String callback = request.getParameter("callback");
        CpTestProgramDao bumpMaskDao = new CpTestProgramDao();
        request.setAttribute("selectList", bumpMaskDao.findAll());
        request.setAttribute("callback", callback);
        return mapping.findForward(forward);
    }
}
