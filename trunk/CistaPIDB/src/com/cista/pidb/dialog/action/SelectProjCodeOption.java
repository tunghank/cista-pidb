package com.cista.pidb.dialog.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.md.dao.ProjectDao;

public class SelectProjCodeOption extends Action {
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
        ProjectDao projectDao = new ProjectDao();
        request.setAttribute("projCodeOptionList", projectDao.findOption());
        request.setAttribute("callback", callback);
        return mapping.findForward(forward);
    }
}
