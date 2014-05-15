package com.cista.pidb.admin.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ErrorHandlerAction extends Action {

    @Override
    public ActionForward execute(ActionMapping arg0, ActionForm arg1,
            HttpServletRequest arg2, HttpServletResponse arg3) throws Exception {
        String errorCode = (String) arg2.getAttribute("errorCode");

        if (errorCode != null) {
            if (errorCode.equals("nologin")) {
                return arg0.findForward("nologin");
            } else if (errorCode.equals("accessdenied")) {
                arg2.setAttribute("error", "Access Denied!");
                return arg0.findForward("accessdenied");
            }
        }

        return null;
    }

}

