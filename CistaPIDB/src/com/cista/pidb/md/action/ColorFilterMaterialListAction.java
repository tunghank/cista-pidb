package com.cista.pidb.md.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.md.dao.ColorFilterMaterialDao;

public class ColorFilterMaterialListAction extends DispatchAction{
	
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
        ColorFilterMaterialDao cfDao = new ColorFilterMaterialDao();
        request.setAttribute("selectList", cfDao.findAll());
        request.setAttribute("callback", callback);
        return mapping.findForward(forward);
    }

}
