package com.cista.pidb.md.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.cista.pidb.md.dao.ZsHdcpKeyDao;


public class ZsHdcpkeyListAction extends DispatchAction {
    public ActionForward list(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        String forward = "success";
        String callback = request.getParameter("callback");
        ZsHdcpKeyDao hdcpkeyDao = new ZsHdcpKeyDao();
        request.setAttribute("selectList", hdcpkeyDao.findAll());
        request.setAttribute("callback", callback);
        return mapping.findForward(forward);
    }
}