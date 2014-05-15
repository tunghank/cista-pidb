package com.cista.pidb.ajax;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.cista.pidb.core.LdapHelper;

public class CheckUserOnADAction extends Action {
    @Override
    public ActionForward execute(ActionMapping arg0, ActionForm arg1, HttpServletRequest arg2, HttpServletResponse arg3) throws Exception {
        String result = "true";
        try {
            String userId = arg2.getParameter("userId");
            String password = arg2.getParameter("password");
            PrintWriter out = arg3.getWriter();
            

            if (!LdapHelper.checkOUUser(userId, password)) {
                result = "true";
            }
            /*if (!LdapHelper.checkUser(userId, password)) {
                result = "false";
            }*/
            out.print(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
